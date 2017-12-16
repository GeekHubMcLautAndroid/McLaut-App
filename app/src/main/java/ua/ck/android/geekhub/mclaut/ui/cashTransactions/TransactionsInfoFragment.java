package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;

public class TransactionsInfoFragment extends Fragment implements Observer<List<CashTransactionsEntity>> {

    @BindView(R.id.fragment_transactions_info_recycler_view)
    RecyclerView mainRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transactions_info,container,false);
        ButterKnife.bind(this,rootView);

        int transactionsType = getArguments().getInt("transactionsType");

        TransactionsInfoViewModel viewModel = ViewModelProviders.of(this).get(TransactionsInfoViewModel.class);
        viewModel.setTransactionsType(transactionsType);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mainRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mainRecyclerView.addItemDecoration(decoration);

        viewModel.getTransactions().observe(this,this);

        return rootView;
    }

    @Override
    public void onChanged(@Nullable List<CashTransactionsEntity> cashTransactionsEntities) {
        CashTransactionsRecyclerAdapter adapter =
                new CashTransactionsRecyclerAdapter(cashTransactionsEntities);
        mainRecyclerView.setAdapter(adapter);
    }
}
