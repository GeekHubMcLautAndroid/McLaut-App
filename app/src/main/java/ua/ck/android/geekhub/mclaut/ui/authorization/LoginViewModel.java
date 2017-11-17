package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;



public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();

    public void login(String login, String password){
        showProgressStatus.postValue(true);
        //TODO: login procedure

        //When login is final
        //showProgressStatus.postValue(false);
    }

    public MutableLiveData<Boolean> getProgressStatusData(){
        return showProgressStatus;
    }
}
