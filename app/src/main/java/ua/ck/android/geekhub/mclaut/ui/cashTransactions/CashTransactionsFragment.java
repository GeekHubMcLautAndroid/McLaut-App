package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;


public class CashTransactionsFragment extends Fragment {
    public static final int TRANSACTION_TYPE_PAYMENTS = 0;
    public static final int TRANSACTION_TYPE_WITHDRAWALS = 1;
    public static final int TRANSACTION_TYPE_ALL = 2;

    @BindView(R.id.fragment_cash_trasactions_tab_layout)
    TabLayout cashTransactionsTabLayout;
    @BindView(R.id.fragment_cash_transaction_viewpager)
    ViewPager cashTransactionsViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cash_transactions,container,false);

        ButterKnife.bind(this, rootView);

        setupViewPager(cashTransactionsViewPager);
        cashTransactionsTabLayout.setupWithViewPager(cashTransactionsViewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager){
        CashTransactionsViewPagerAdapter adapter = new CashTransactionsViewPagerAdapter(getChildFragmentManager());

        addFragmentToAdapter(adapter,TRANSACTION_TYPE_PAYMENTS,getString(R.string.label_tab_payments));
        addFragmentToAdapter(adapter,TRANSACTION_TYPE_WITHDRAWALS,getString(R.string.label_tab_withdrawals));
        addFragmentToAdapter(adapter,TRANSACTION_TYPE_ALL,getString(R.string.label_tab_all));

        viewPager.setAdapter(adapter);
    }
    private void addFragmentToAdapter(CashTransactionsViewPagerAdapter adapter, int transactionType, String label){
        TransactionsInfoFragment fragment = new TransactionsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("transactionsType", transactionType);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment,label);
    }
}
