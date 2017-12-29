package ua.ck.android.geekhub.mclaut.ui.userInfo;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.HashMap;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;


public class UserInfoViewModel extends ViewModel implements Observer<HashMap<String,UserCharacteristic>> {

    private MutableLiveData<UserInfoEntity> userData = new MutableLiveData<>();
    private MutableLiveData<Boolean> refresher = new MutableLiveData<>();

    public UserInfoViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);
    }

    public MutableLiveData<UserInfoEntity> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getRefresher() {
        if(!refresher.hasObservers()) {
            refreshData();
        }
        return refresher;
    }

    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        UserCharacteristic characteristic = stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser());
        if(characteristic != null) {
            userData.postValue(characteristic.getInfo());
        }
    }

    private void refreshData(){
        refresher = Repository.getInstance().refreshUser(McLautApplication.getSelectedUser());
    }
}
