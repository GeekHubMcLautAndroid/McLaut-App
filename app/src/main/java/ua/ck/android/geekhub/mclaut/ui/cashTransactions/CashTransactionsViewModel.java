package ua.ck.android.geekhub.mclaut.ui.cashTransactions;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;

/**
 * Created by bogda on 16.01.2018.
 */

public class CashTransactionsViewModel extends ViewModel {
    private MutableLiveData<Boolean> refresher = new MutableLiveData<>();


    public MutableLiveData<Boolean> getRefresher() {
        if(!refresher.hasObservers()) {
            refreshData();
        }
        return refresher;
    }
    private void refreshData(){
        refresher = Repository.getInstance().refreshUser(McLautApplication.getSelectedUser());
    }
}
