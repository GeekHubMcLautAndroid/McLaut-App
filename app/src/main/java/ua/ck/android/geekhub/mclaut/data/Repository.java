package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

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

    private Context refresherContext;
    private String refresherCertificate;
    private int refresherCity;

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

        MutableLiveData<UserInfoEntity> request = new MutableLiveData<>();
        request.setValue(LocalDatabase.getInstance(context).dao().getUserInfoEntityById(userId));
        return request;
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

    private void insertUserInfoToDatabase(UserInfoEntity userInfoEntity){
        LocalDatabase.getInstance(refresherContext).dao().insertUserInfo(userInfoEntity);
    }

    private void insertUserConnectionInfoToDatabase(UserConnectionsInfo userConnectionsInfo){
        LocalDatabase.getInstance(refresherContext).dao().insertUserConnectionsInfo(userConnectionsInfo);
    }

    private void insertPaymentsToDatabase(PaymentsListEntity paymentsList){
        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                iter.hasNext(); ){

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);
            LocalDatabase.getInstance(refresherContext).dao().insertCashTransactionsEntities(payment);
        }
    }

    private void insertWithdrawalsToDatabase(WithdrawalsListEntity withdrawalsList){

        for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
             iter.hasNext(); ){

            CashTransactionsEntity withdrawal = iter.next();
            withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);
            LocalDatabase.getInstance(refresherContext).dao().insertCashTransactionsEntities(withdrawal);
        }
    }

    public void addNewUserToDatabase(Context context, String login, String password, int city){
        refresherCity = city;
        refresherContext = context;

        NetworkDataSource.getInstance().checkLogin(login, password, city)
                .observeForever(new Observer<LoginResultInfo>() {
            @Override
            public void onChanged(@Nullable LoginResultInfo loginResultInfo) {
                if (loginResultInfo.getResultCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {

                    Repository.getInstance().refresherCertificate = loginResultInfo.getCertificate();
                    findUserInfoInInternet();
                }
            }
        });
    }

    private void findUserInfoInInternet() {
        NetworkDataSource.getInstance().getUserInfo(refresherCertificate, refresherCity)
            .observeForever(new Observer<UserInfoEntity>() {
                @Override
                public void onChanged(@Nullable UserInfoEntity userInfoEntity) {
                    if (userInfoEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {
                        putUserInfoToDatabase(userInfoEntity);
                        refreshAllDataForUser(userInfoEntity.getId());
                    }
                }
            });
    }

    private void putUserInfoToDatabase(UserInfoEntity userInfoEntity){
        userInfoEntity.setCertificate(Repository.getInstance().refresherCertificate);
        userInfoEntity.setCity(Repository.getInstance().refresherCity);
        insertUserInfoToDatabase(userInfoEntity);
    }


    public void refreshAllDataForUser(String userId){
        refreshUserInfo(userId);
        refreshUserCashTransactions();
    }

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

        findUserInfoInInternet();
    }

    private void refreshUserCashTransactions(){
        refreshPayments();
        refreshWithdrawals();
    }

    private void refreshPayments(){
         NetworkDataSource.getInstance().getPayments(refresherCertificate, refresherCity)
                 .observeForever(new Observer<PaymentsListEntity>() {
                     @Override
                     public void onChanged(@Nullable PaymentsListEntity paymentsListEntity) {
                         if(paymentsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                             insertPaymentsToDatabase(paymentsListEntity);
                         }
                     }
                 });

    }

    private void refreshWithdrawals(){
        NetworkDataSource.getInstance().getWithdrawals(refresherCertificate, refresherCity)
                .observeForever(new Observer<WithdrawalsListEntity>() {
                    @Override
                    public void onChanged(@Nullable WithdrawalsListEntity withdrawalsListEntity) {
                        if(withdrawalsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                            insertWithdrawalsToDatabase(withdrawalsListEntity);
                        }
                    }
                });
    }

    //TODO: Write new information from network to local database
}
