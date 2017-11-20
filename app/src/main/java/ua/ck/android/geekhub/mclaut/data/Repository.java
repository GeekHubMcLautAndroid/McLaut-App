package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;

import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.network.NetworkDataSource;

public class Repository {

    private static Repository instance;

    private Repository() {
    }

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }



    public MutableLiveData<LoginResultInfo> getLoginInfo(String login, String password, int city){
        MutableLiveData<LoginResultInfo> loginResultsLiveData = NetworkDataSource.getInstance().
                checkLogin(login,password,city);
        return loginResultsLiveData;
    }
}
