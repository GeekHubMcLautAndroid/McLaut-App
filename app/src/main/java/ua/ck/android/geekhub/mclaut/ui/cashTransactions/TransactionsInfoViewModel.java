package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;


public class TransactionsInfoViewModel extends ViewModel implements Observer<HashMap<String, UserCharacteristic>> {
    private MutableLiveData<List<CashTransactionsEntity>> transactions = new MutableLiveData<>();
    private static final int TRANSACTION_TYPE_PAYMENTS = 0;
    private static final int TRANSACTION_TYPE_WITHDRAWALS = 1;
    private static final int TRANSACTION_TYPE_ALL = 2;
    private int transactionsType;

    MutableLiveData<List<CashTransactionsEntity>> getTransactions() {
        return transactions;
    }


    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        List<CashTransactionsEntity> transactionsList = new ArrayList<>();
        switch (transactionsType){
            case TRANSACTION_TYPE_PAYMENTS:
                transactionsList = stringUserCharacteristicHashMap.
                        get(McLautApplication.getSelectedUser()).getPaymentsTransactions();
                break;
            case TRANSACTION_TYPE_WITHDRAWALS:
                transactionsList = stringUserCharacteristicHashMap.
                        get(McLautApplication.getSelectedUser()).getWithdrawalsTransactions();
                break;
            case TRANSACTION_TYPE_ALL:
                transactionsList = new ArrayList<>(stringUserCharacteristicHashMap
                .get(McLautApplication.getSelectedUser()).getPaymentsTransactions());
                transactionsList.addAll(stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser()).
                        getWithdrawalsTransactions());
                break;
        }
        Collections.sort(transactionsList, (cashTransactionsEntity, t1) ->
                cashTransactionsEntity.getDate() > t1.getDate() ? -1 : 1);
        transactions.postValue(transactionsList);
    }

    public void setTransactionsType(int transactionsType) {
        this.transactionsType = transactionsType;
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);

    }
}
