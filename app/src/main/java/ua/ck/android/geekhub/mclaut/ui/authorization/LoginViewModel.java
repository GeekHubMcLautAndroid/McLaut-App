package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;


public class LoginViewModel extends ViewModel implements Observer<LoginResultInfo> {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> resultStatus = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    public void login(String login, String password, int city){
        showProgressStatus.postValue(true);

        repo.getLoginInfo(login,password,city).observeForever(this);
    }

    @Override
    public void onChanged(@Nullable LoginResultInfo loginResultInfo) {
                resultStatus.postValue(loginResultInfo.getLocalResultCode());
    }

    public MutableLiveData<Boolean> getProgressStatusData(){
        return showProgressStatus;
    }

    public MutableLiveData<Integer> getResultStatus() {
        return resultStatus;
    }
}
