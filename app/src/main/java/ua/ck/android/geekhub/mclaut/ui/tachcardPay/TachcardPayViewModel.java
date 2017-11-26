package ua.ck.android.geekhub.mclaut.ui.tachcardPay;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.ck.android.geekhub.mclaut.data.Repository;

public class TachcardPayViewModel extends ViewModel {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();



    public MutableLiveData<Boolean> getProgressStatusData(){
        return showProgressStatus;
    }
}
