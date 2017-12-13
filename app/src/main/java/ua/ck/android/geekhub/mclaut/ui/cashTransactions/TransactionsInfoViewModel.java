package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;


public class TransactionsInfoViewModel extends ViewModel implements Observer<HashMap<String, UserCharacteristic>> {
    private MutableLiveData<List<CashTransactionsEntity>> transactions = new MutableLiveData<>();
    public static final int TRANSACTION_TYPE_PAYMENTS = 0;
    public static final int TRANSACTION_TYPE_WITHDRAWALS = 1;
    public static final int TRANSACTION_TYPE_ALL = 2;
    private int transactionsType;

    public TransactionsInfoViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);
    }

    public MutableLiveData<List<CashTransactionsEntity>> getTransactions() {
        return transactions;
    }


    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        switch (transactionsType){
            case TRANSACTION_TYPE_PAYMENTS:
                transactions.postValue(stringUserCharacteristicHashMap.
                        get(McLautApplication.getSelectedUser()).getPaymentsTransactions());
                break;
            case TRANSACTION_TYPE_WITHDRAWALS:
                transactions.postValue(stringUserCharacteristicHashMap.
                        get(McLautApplication.getSelectedUser()).getWithdrawalsTransactions());
                break;
            case TRANSACTION_TYPE_ALL:
                List<CashTransactionsEntity> allTransactions = new ArrayList<>(stringUserCharacteristicHashMap
                .get(McLautApplication.getSelectedUser()).getPaymentsTransactions());
                allTransactions.addAll(stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser()).
                        getWithdrawalsTransactions());
                transactions.postValue(allTransactions);
                break;
        }
    }

    public void setTransactionsType(int transactionsType) {
        this.transactionsType = transactionsType;
    }
}
