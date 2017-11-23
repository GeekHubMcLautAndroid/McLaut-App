package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;

import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.entities.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.WithdrawalsListEntity;
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

        return NetworkDataSource.getInstance().checkLogin(login,password,city);
    }

    public MutableLiveData<UserInfoEntity> getUserInfo(String certificate, int city){

        //TODO: get user info from local database
        //Temp return from network
        return NetworkDataSource.getInstance().getUserInfo(certificate,city);
    }

    public MutableLiveData<WithdrawalsListEntity> getWithdrawalsInfo(String certificate, int city){

        //TODO: get withdrawals from local database
        //Temp return from network
        return NetworkDataSource.getInstance().getWithdrawals(certificate,city);
    }
    public MutableLiveData<PaymentsListEntity> getPaymentsInfo(String certificate, int city){

        //TODO: get withdrawals from local database
        //Temp return from network
        return NetworkDataSource.getInstance().getPayments(certificate,city);
    }

    //TODO: Write new information from network to local database
}
