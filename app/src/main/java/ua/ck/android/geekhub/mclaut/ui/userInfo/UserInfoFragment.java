package ua.ck.android.geekhub.mclaut.ui.userInfo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.model.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;

public class UserInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String USER_IS_ACTIVE = "1";

    @BindView(R.id.user_info_fragment_balance_textview)
    TextView balanceTextView;
    @BindView(R.id.user_info_fragment_period_finish)
    TextView periodFinishTextView;
    @BindView(R.id.user_info_fragment_username_textview)
    TextView usernameTextView;
    @BindView(R.id.user_info_fragment_isactive_textview)
    TextView isActiveTextView;
    @BindView(R.id.user_info_fragment_account_number_textview)
    TextView accountNumberTextView;
    @BindView(R.id.user_info_fragment_connections_recyclerview)
    RecyclerView connectionsRecycler;
    @BindView(R.id.user_info_fragment_swipe_layout)
    SwipeRefreshLayout refreshLayout;

    private UserInfoViewModel viewModel = new UserInfoViewModel();

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_user_info,container,false);

        ButterKnife.bind(this,rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        connectionsRecycler.setLayoutManager(layoutManager);

        //disable recyclerview scroll
        connectionsRecycler.setOnTouchListener((view, motionEvent) -> true);

        viewModel.getUserData().observe(this, new Observer<UserInfoEntity>() {
            @Override
            public void onChanged(@Nullable UserInfoEntity userInfoEntity) {
                if(userInfoEntity != null) {
                    setUserData(userInfoEntity);
                }
            }
        });

        refreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_dark),
                (getResources().getColor(android.R.color.holo_green_dark)),
                (getResources().getColor(android.R.color.holo_red_dark)));

        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }



    private void setUserData(UserInfoEntity userInfoEntity){
        if (userInfoEntity != null) {
            String balance = userInfoEntity.getBalance() + getString(R.string.uah_symbol);
            balanceTextView.setText(balance);
            usernameTextView.setText(userInfoEntity.getName());
            if (userInfoEntity.getIsActive() != null &&
                    userInfoEntity.getIsActive().equals(USER_IS_ACTIVE)) {
                isActiveTextView.setText(getString(R.string.active_label));
                isActiveTextView.setTextColor(ContextCompat.getColor(getActivity(),
                        android.R.color.holo_green_dark));
            } else {
                isActiveTextView.setText(getString(R.string.no_active_label));
                isActiveTextView.setTextColor(ContextCompat.getColor(getActivity(),
                        android.R.color.holo_red_dark));
            }
            String accountNumber = getString(R.string.account_number_label) + " " + userInfoEntity.getAccount();
            accountNumberTextView.setText(accountNumber);
            List<UserConnectionsInfo> currUserConnectionsInfo = userInfoEntity.getUserConnectionsInfoList();
            if(currUserConnectionsInfo != null) {
                double dayCounter = 0;
                for (UserConnectionsInfo info : currUserConnectionsInfo) {
                    dayCounter += (Double.parseDouble(userInfoEntity.getBalance()) /
                            Double.parseDouble(info.getPayAtDay()));
                }
                String days = getString(R.string.its_left_label) + " " + (int) Math.ceil(dayCounter) +
                        " " + getString(R.string.days_of_service_label);
                periodFinishTextView.setText(days);
            }
            ConnectionsRecyclerAdapter recyclerAdapter =
                    new ConnectionsRecyclerAdapter(currUserConnectionsInfo, getActivity());
            connectionsRecycler.setAdapter(recyclerAdapter);
        }
        double balanceD = Double.parseDouble(userInfoEntity.getBalance());

        String balance = String.format("%.2f",balanceD) + getString(R.string.uah_symbol);
        balanceTextView.setText(balance);
        usernameTextView.setText(userInfoEntity.getName());

        if(userInfoEntity.getIsActive() != null &&
                userInfoEntity.getIsActive().equals(USER_IS_ACTIVE)){
            isActiveTextView.setText(getString(R.string.active_label));
            isActiveTextView.setTextColor(ContextCompat.getColor(getActivity(),
                    android.R.color.holo_green_dark));
        }
        else{
            isActiveTextView.setText(getString(R.string.no_active_label));
            isActiveTextView.setTextColor(ContextCompat.getColor(getActivity(),
                    android.R.color.holo_red_dark));
        }
        String accountNumber = getString(R.string.account_number_label) +" " + userInfoEntity.getAccount();
        accountNumberTextView.setText(accountNumber);
    }

    private static final int SHORT_DURATION = 2000;
    private static Long lastUsedTime;
    private static Long momentTime;

    //on swipe
    @Override
    public void onRefresh() {
        momentTime = System.currentTimeMillis();
        refreshLayout.setRefreshing(true);
        if ((lastUsedTime == null)
                || (momentTime - lastUsedTime > SHORT_DURATION)) {
            lastUsedTime = System.currentTimeMillis();
            viewModel.getRefresher().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    if (!aBoolean) {
                        Toast.makeText(
                                McLautApplication.getContext()
                                , McLautApplication.getContext()
                                        .getResources().getString(R.string.connection_failed)
                                , Toast.LENGTH_SHORT).show();
                    }
                    refreshLayout.setRefreshing(false);
                    viewModel.getRefresher().removeObserver(this);
                }
            });
        } else {
            refreshLayout.setRefreshing(false);
        }
    }
}
