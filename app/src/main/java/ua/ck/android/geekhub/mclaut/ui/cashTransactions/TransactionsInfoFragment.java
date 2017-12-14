package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import ua.ck.android.geekhub.mclaut.R;

public class TransactionsInfoFragment extends Fragment {
    public static final int TRANSACTION_TYPE_PAYMENTS = 0;
    public static final int TRANSACTION_TYPE_WITHDRAWALS = 1;
    public static final int TRANSACTION_TYPE_ALL = 2;
    private int transactionsType;

    private TransactionsInfoViewModel viewModel;

    @BindView(R.id.fragment_transactions_info_recycler_view)
    RecyclerView mainRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cash_transactions,container,false);

        transactionsType = getArguments().getInt("transactionsType");

        viewModel = ViewModelProviders.of(this).get(TransactionsInfoViewModel.class);
        viewModel.setTransactionsType(transactionsType);

        return rootView;
    }
}
