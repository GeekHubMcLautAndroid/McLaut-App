package ua.ck.android.geekhub.mclaut.ui.userInfo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;


public class UserInfoViewModel extends ViewModel {

    //TEMP
    private MutableLiveData<UserInfoEntity> userData = new MutableLiveData<>();



    public MutableLiveData<UserInfoEntity> getUserData() {
        return userData;
    }
}
