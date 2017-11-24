package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.Iterator;

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

    public MutableLiveData<UserInfoEntity> getUserInfo(Context context, String userId){

        return LocalDatabase.getInstance(context).dao().getUserInfoEntityById(userId);
    }

    public MutableLiveData<WithdrawalsListEntity> getWithdrawalsInfo(Context context, String certificate, int city){

        WithdrawalsListEntity withdrawalsList = new WithdrawalsListEntity(
                LocalDatabase.getInstance(context).dao()
                        .findAllWithdrawalsEntities());

        MutableLiveData<WithdrawalsListEntity> request = new MutableLiveData<>();
        request.setValue(withdrawalsList);

        return request;
    }

    public MutableLiveData<PaymentsListEntity> getPaymentsInfo(Context context, String certificate, int city){

        PaymentsListEntity paymentsList = new PaymentsListEntity(
                LocalDatabase.getInstance(context).dao()
                        .findAllPaymentsEntities());

        MutableLiveData<PaymentsListEntity> request = new MutableLiveData<>();
        request.setValue(paymentsList);

        return request;
    }

    public void insertUserInfoToDatabase(Context context,UserInfoEntity userInfoEntity){
        LocalDatabase.getInstance(context).dao().insertUserInfo(userInfoEntity);
    }

    public void insertUserConnectionInfoToDatabase(Context context, UserConnectionsInfo userConnectionsInfo){
        LocalDatabase.getInstance(context).dao().insertUserConnectionsInfo(userConnectionsInfo);
    }

    public void insertPaymentsToDatabase(Context context, PaymentsListEntity paymentsList){
        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                iter.hasNext(); ){

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);
            LocalDatabase.getInstance(context).dao().insertCashTransactionsEntities(payment);
        }
    }

    public void insertWithdrawalsToDatabase(Context context, WithdrawalsListEntity withdrawalsList){

        for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
             iter.hasNext(); ){

            CashTransactionsEntity withdrawal = iter.next();
            withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);
            LocalDatabase.getInstance(context).dao().insertCashTransactionsEntities(withdrawal);
        }
    }

    public void addNewUserToDatabase(Context context, String login, String password, int city){
        MutableLiveData<LoginResultInfo> loginResult = NetworkDataSource
                .getInstance()
                .checkLogin(login, password, city);

        MutableLiveData<UserInfoEntity> newUserInfo = NetworkDataSource
                .getInstance()
                .getUserInfo(loginResult.getValue().getCertificate(), city);

        newUserInfo.getValue().setCertificate(loginResult.getValue().getCertificate());
        newUserInfo.getValue().setCity(city);

        insertUserInfoToDatabase(context, newUserInfo.getValue());
        refreshAllDataForUser(context, newUserInfo.getValue().getId());
    }

    private Context refresherContext;
    private String refresherCertificate;
    private int refresherCity;

    public void refreshAllDataForUser(Context context, String userId){
        refresherContext = context;
        refreshUserInfo(userId);
        refreshUserCashTransactions();
    }

    private void refreshUserInfo(String userId){

        refresherCertificate =  LocalDatabase.getInstance(refresherContext).dao()
                .getUserCertificate(userId);
        refresherCity =  LocalDatabase.getInstance(refresherContext).dao()
                .getUserCity(userId);

        MutableLiveData<UserInfoEntity> updatedUserInfo = NetworkDataSource
                .getInstance()
                .getUserInfo(refresherCertificate, refresherCity);

        updatedUserInfo.getValue().setCertificate(refresherCertificate);

        updatedUserInfo.getValue().setCity(refresherCity);


        insertUserInfoToDatabase(refresherContext, updatedUserInfo.getValue());
    }

    private void refreshUserCashTransactions(){
        refreshPayments();
        refreshWithdrawals();
    }

    private void refreshPayments(){
        MutableLiveData<PaymentsListEntity> updatedUserPayments = NetworkDataSource
                .getInstance()
                .getPayments(refresherCertificate, refresherCity);
        insertPaymentsToDatabase(refresherContext, updatedUserPayments.getValue());
    }

    private void refreshWithdrawals(){
        MutableLiveData<PaymentsListEntity> updatedUserWithdramals = NetworkDataSource
                .getInstance()
                .getPayments(refresherCertificate, refresherCity);
        insertPaymentsToDatabase(refresherContext, updatedUserWithdramals.getValue());
    }

    //TODO: Write new information from network to local database
}
