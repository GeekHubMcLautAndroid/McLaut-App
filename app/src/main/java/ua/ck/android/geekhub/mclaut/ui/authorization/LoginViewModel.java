package ua.ck.android.geekhub.mclaut.ui.authorization;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;


import java.util.HashMap;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;


public class LoginViewModel extends ViewModel implements Observer<LoginResultInfo> {
    private MutableLiveData<Boolean> showProgressStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> resultStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> resultLoadDataAboutUserFromInternet = new MutableLiveData<>();

    private Repository repo = Repository.getInstance();

    public void login(String login, String password, int city){
        showProgressStatus.postValue(true);
        resultLoadDataAboutUserFromInternet.setValue(false);
        repo.addNewUserToDatabase(login,password,city).observeForever(this);
        repo.getMapUsersCharacteristic().observeForever(new Observer<HashMap<String, UserCharacteristic>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
                if((stringUserCharacteristicHashMap != null)
                        &&(stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser()) != null)) {
                    resultLoadDataAboutUserFromInternet.postValue(true);
                    showProgressStatus.postValue(false);
                    repo.getMapUsersCharacteristic().removeObserver(this);
                }
            }
        });
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

    public MutableLiveData<Boolean> getResultLoadDataAboutUserFromInternet() {
        return resultLoadDataAboutUserFromInternet;
    }

}
