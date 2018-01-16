package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;


public class CashTransactionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int TRANSACTION_TYPE_PAYMENTS = 0;
    public static final int TRANSACTION_TYPE_WITHDRAWALS = 1;
    public static final int TRANSACTION_TYPE_ALL = 2;

    @BindView(R.id.fragment_cash_trasactions_tab_layout)
    TabLayout cashTransactionsTabLayout;
    @BindView(R.id.fragment_cash_transaction_viewpager)
    ViewPager cashTransactionsViewPager;
    @BindView(R.id.cash_transactions_swipe_layout)
    SwipeRefreshLayout refreshLayout;
    CashTransactionsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cash_transactions,container,false);

        ButterKnife.bind(this, rootView);

        viewModel = ViewModelProviders.of(this).get(CashTransactionsViewModel.class);

        setupViewPager(cashTransactionsViewPager);
        cashTransactionsTabLayout.setupWithViewPager(cashTransactionsViewPager);

        refreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_dark),
                (getResources().getColor(android.R.color.holo_green_dark)),
                (getResources().getColor(android.R.color.holo_red_dark)));

        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager){
        CashTransactionsViewPagerAdapter adapter = new CashTransactionsViewPagerAdapter(getChildFragmentManager());

        addFragmentToAdapter(adapter,TRANSACTION_TYPE_ALL,getString(R.string.label_tab_all));
        addFragmentToAdapter(adapter,TRANSACTION_TYPE_PAYMENTS,getString(R.string.label_tab_payments));
        addFragmentToAdapter(adapter,TRANSACTION_TYPE_WITHDRAWALS,getString(R.string.label_tab_withdrawals));

        viewPager.setAdapter(adapter);

        //load all fragments to memory
        viewPager.setOffscreenPageLimit(3);
    }
    private void addFragmentToAdapter(CashTransactionsViewPagerAdapter adapter, int transactionType, String label){
        TransactionsInfoFragment fragment = new TransactionsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("transactionsType", transactionType);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment,label);
    }

    private static final int SHORT_DURATION = 2000;
    private static Long lastUsedTime;
    private static Long momentTime;

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
