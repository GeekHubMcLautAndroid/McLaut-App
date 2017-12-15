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

    public UserInfoViewModel() {
        Repository.getInstance().getMapUsersCharacteristic().observeForever(this);
    }

    public MutableLiveData<UserInfoEntity> getUserData() {
        return userData;
    }

    @Override
    public void onChanged(@Nullable HashMap<String, UserCharacteristic> stringUserCharacteristicHashMap) {
        UserCharacteristic characteristic = stringUserCharacteristicHashMap.get(McLautApplication.getSelectedUser());
        userData.postValue(characteristic.getInfo());
    }

    void refreshData(){
        Repository.getInstance().refreshUserInfo(McLautApplication.getSelectedUser());
    }
}
