package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.LoginResultInfo;


public class LoginViewModel extends ViewModel implements Observer<LoginResultInfo> {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> resultStatus = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    public void login(Context context, String login, String password, int city){
        showProgressStatus.postValue(true);
        repo.addNewUserToDatabase(login,password,city).observeForever(this);
    }

    @Override
    public void onChanged(@Nullable LoginResultInfo loginResultInfo) {
                resultStatus.postValue(loginResultInfo.getLocalResultCode());
                showProgressStatus.postValue(false);
    }

    public MutableLiveData<Boolean> getProgressStatusData(){
        return showProgressStatus;
    }

    public MutableLiveData<Integer> getResultStatus() {
        return resultStatus;
    }
}
