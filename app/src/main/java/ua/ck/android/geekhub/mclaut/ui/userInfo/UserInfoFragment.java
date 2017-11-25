package ua.ck.android.geekhub.mclaut.ui.userInfo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel viewModel;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_user_info,container,false);

        ButterKnife.bind(this,rootView);



        return rootView;
    }
}
