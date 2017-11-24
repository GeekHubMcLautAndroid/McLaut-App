package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.Iterator;
import java.util.List;

import ua.ck.android.geekhub.mclaut.data.database.LocalDatabase;
import ua.ck.android.geekhub.mclaut.data.entities.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.entities.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.WithdrawalsListEntity;
import ua.ck.android.geekhub.mclaut.data.network.NetworkDataSource;

public class Repository {

    private static Repository instance;
    private LocalDatabase localDatabase;

    private Repository(Context context) {
        localDatabase = LocalDatabase.getInstance(context);
    }

    public static Repository getInstance(Context context){
        if(instance == null){
            instance = new Repository(context);
        }
        return instance;
    }

    public MutableLiveData<LoginResultInfo> getLoginInfo(String login, String password, int city){

        return NetworkDataSource.getInstance().checkLogin(login,password,city);
    }

    public MutableLiveData<UserInfoEntity> getUserInfo(int userId){

        return localDatabase.dao().getUserInfoEntityById(userId);
    }

    public MutableLiveData<WithdrawalsListEntity> getWithdrawalsInfo(String certificate, int city){

        WithdrawalsListEntity withdrawalsList = new WithdrawalsListEntity(
                localDatabase.dao()
                        .findAllWithdrawalsEntities());

        MutableLiveData<WithdrawalsListEntity> request = new MutableLiveData<>();
        request.setValue(withdrawalsList);

        return request;
    }

    public MutableLiveData<PaymentsListEntity> getPaymentsInfo(String certificate, int city){

        PaymentsListEntity paymentsList = new PaymentsListEntity(
                localDatabase.dao()
                        .findAllPaymentsEntities());

        MutableLiveData<PaymentsListEntity> request = new MutableLiveData<>();
        request.setValue(paymentsList);

        return request;
    }

    public void insertUserInfoToDatabase(UserInfoEntity userInfoEntity){
        localDatabase.dao().insertUserInfo(userInfoEntity);
    }

    public void insertUserConnectionInfoToDatabase(UserConnectionsInfo userConnectionsInfo){
        localDatabase.dao().insertUserConnectionsInfo(userConnectionsInfo);
    }

    public void insertPaymentsToDatabase(PaymentsListEntity paymentsList){
        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                iter.hasNext();){

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);
            localDatabase.dao().insertCashTransactionsEntities(payment);
        }
    }

    public void insertWithdrawalsToDatabase(WithdrawalsListEntity withdrawalsList){
        for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
             iter.hasNext();){

            CashTransactionsEntity withdrawal = iter.next();
            withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);
            localDatabase.dao().insertCashTransactionsEntities(withdrawal);
        }
    }

    //TODO: Write new information from network to local database
}
