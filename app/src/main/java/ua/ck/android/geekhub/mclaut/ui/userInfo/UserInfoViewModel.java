package ua.ck.android.geekhub.mclaut.ui.userInfo;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;


public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<UserInfoEntity> userData = new MutableLiveData<>();


    public MutableLiveData<UserInfoEntity> getUserData(Context context) {
        return null;
    }
}
