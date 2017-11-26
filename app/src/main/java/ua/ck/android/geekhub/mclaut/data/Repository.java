package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.List;

import ua.ck.android.geekhub.mclaut.data.database.LocalDatabase;
import ua.ck.android.geekhub.mclaut.data.entities.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.entities.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.WithdrawalsListEntity;
import ua.ck.android.geekhub.mclaut.data.network.NetworkDataSource;
import ua.ck.android.geekhub.mclaut.tools.McLautAppExecutor;

public class Repository {

    private static Repository instance;
    private McLautAppExecutor executor = McLautAppExecutor.getInstance();

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

        final MutableLiveData<UserInfoEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            UserInfoEntity userInfoEntity = LocalDatabase.getInstance(context).dao()
                    .getUserInfoEntityById(userId);
            request.postValue(userInfoEntity);
        });

        return request;
    }

    public MutableLiveData<WithdrawalsListEntity> getWithdrawalsInfo(Context context, String userId){

        final MutableLiveData<WithdrawalsListEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            WithdrawalsListEntity withdrawalsList = new WithdrawalsListEntity(
                LocalDatabase.getInstance(context).dao()
                        .findAllWithdrawalsEntities(userId));

            request.postValue(withdrawalsList);
        });


        return request;
    }

    public MutableLiveData<PaymentsListEntity> getPaymentsInfo(Context context, String userId){
        final MutableLiveData<PaymentsListEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            PaymentsListEntity paymentsList = new PaymentsListEntity(
                    LocalDatabase.getInstance(context).dao()
                            .findAllPaymentsEntities(userId));
            request.setValue(paymentsList);
        });

        return request;
    }

    public MutableLiveData<List<String>> getAllUsersId(Context context){
        final MutableLiveData<List<String>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            List<String> usersIdList = LocalDatabase.getInstance(context).dao().getAllUsersId();
            request.postValue(usersIdList);
        });

        return request;
    }

    public MutableLiveData<List<CardInfoEntity>> getAllCardList(Context context) {
        final MutableLiveData<List<CardInfoEntity>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            List<CardInfoEntity> cardInfoList = LocalDatabase.getInstance(context).dao()
                    .getAllCardsInfo();
            request.postValue(cardInfoList);
        });

        return request;
    }

    private void insertUserInfoToDatabase(final UserInfoEntity userInfoEntity){

        executor.databaseExecutor()
                .execute(() -> {
                    LocalDatabase.getInstance(refresherContext).dao()
                                            .insertUserInfo(userInfoEntity);
                }
                );
    }

    private void insertUserConnectionInfoToDatabase( List<UserConnectionsInfo> userConnectionsInfoList){
        int SINGLE_ELEMENT_INDEX = 0;

        if (userConnectionsInfoList != null) {
            final UserConnectionsInfo userConnectionsInfo = userConnectionsInfoList.get(SINGLE_ELEMENT_INDEX);
            executor.databaseExecutor()
                    .execute(() -> {
                                LocalDatabase.getInstance(refresherContext).dao()
                                        .insertUserConnectionsInfo(userConnectionsInfo);
                            }
                    );
        }
}

    private void insertPaymentsToDatabase(PaymentsListEntity paymentsList){

        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                iter.hasNext(); ){

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);

            executor.databaseExecutor().execute(() -> {
                        LocalDatabase.getInstance(refresherContext).dao()
                                .insertCashTransactionsEntities(payment);
            });
        }
    }

    private void insertWithdrawalsToDatabase(WithdrawalsListEntity withdrawalsList){

        for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
             iter.hasNext(); ){

            CashTransactionsEntity withdrawal = iter.next();
            withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);

            executor.databaseExecutor().execute(() -> {
                LocalDatabase.getInstance(refresherContext).dao()
                        .insertCashTransactionsEntities(withdrawal);
            });
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
                        insertUserConnectionInfoToDatabase(userInfoEntity.getUserConnectionsInfoList());
                        putAllUserDataToDatabase(userInfoEntity.getId());
                    }
                }
            });
    }

    private void putUserInfoToDatabase(UserInfoEntity userInfoEntity){
        userInfoEntity.setCertificate(Repository.getInstance().refresherCertificate);
        userInfoEntity.setCity(Repository.getInstance().refresherCity);
        insertUserInfoToDatabase(userInfoEntity);
    }


    public void putAllUserDataToDatabase(String userId){
        refreshUserInfo(userId);
        refreshUserCashTransactions();
    }

    public void refreshUserDataInDatabase(Context context, String userId){
        refresherContext = context;
        refreshUserInfo(userId);
        refreshUserCashTransactions();
    }

    private void refreshUserInfo(String userId){

        executor.databaseExecutor()
                .execute(() -> {
                    refresherCertificate = LocalDatabase.getInstance(refresherContext).dao()
                            .getUserCertificate(userId);
                    refresherCity = LocalDatabase.getInstance(refresherContext).dao()
                            .getUserCity(userId);

                    findUserInfoInInternet();
                });
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
