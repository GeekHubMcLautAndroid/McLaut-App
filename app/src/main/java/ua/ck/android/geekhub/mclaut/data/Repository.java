package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
    private static final MutableLiveData< HashMap <String, UserCharacteristic>>
            mapUsersCharacteristic = new MutableLiveData<>();

    private static McLautAppExecutor executor = McLautAppExecutor.getInstance();

    private static Context refresherContext;
    private static String refresherCertificate;
    private static int refresherCity;

    private static String tempUserId;
    private static final Integer ALL_FIELDS_UPDATED = 4;
    private static final Integer NON_FIELDS_UPDATED = 0;
    private static final Integer ADD_NEW_FIELD = 1;

    private static UserCharacteristic tempUserCharacteristic = new UserCharacteristic();
    private static MutableLiveData<Integer> iObserver = new MutableLiveData<>();


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


    public void initUserCharacteristics(Context context){

        refresherContext = context;

        executor.databaseExecutor().execute(() ->{
            getAllUsersId(context).observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> usersId) {
                    if(usersId != null) {
                        for(Iterator<String> iter = usersId.iterator(); iter.hasNext(); ){

                            String userId = iter.next();
                            MutableLiveData<UserCharacteristic> mutableLiveData
                                    = getUserCharacteristics(userId);
                            mutableLiveData.observeForever(new Observer<UserCharacteristic>() {
                                @Override
                                public void onChanged(@Nullable UserCharacteristic userCharacteristic) {
                                    if((   userCharacteristic.getInfo() != null)
                                        &&(userCharacteristic.getConnections() != null)
                                        &&(userCharacteristic.getPaymentsTransactions() != null)
                                        &&(userCharacteristic.getWithdrawalsTransactions() != null)) {
                                        HashMap currentMap;
                                        if(mapUsersCharacteristic.getValue() == null) {
                                            currentMap = new HashMap<String, UserCharacteristic>();
                                            currentMap.put(userId, userCharacteristic);
                                            mapUsersCharacteristic.setValue(currentMap);
                                        } else {
                                            currentMap = mapUsersCharacteristic.getValue();
                                            currentMap.put(userId, userCharacteristic);
                                            mapUsersCharacteristic.setValue(currentMap);
                                        }
                                        mutableLiveData.removeObserver(this);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        });

    }

    private MutableLiveData<UserCharacteristic> getUserCharacteristics(String userId) {

        UserCharacteristic userCharacteristic = new UserCharacteristic();

        MutableLiveData<UserCharacteristic> request = new MutableLiveData<>();
        request.postValue(userCharacteristic);


        MutableLiveData<UserInfoEntity> userInfo
                = getUserInfo(refresherContext, userId);
        MutableLiveData<List<UserConnectionsInfo>> userConnectionInfo
                = getUserConnectionInfo(refresherContext, userId);
        MutableLiveData<PaymentsListEntity> paymentsList
                = getPaymentsInfo(refresherContext, userId);
        MutableLiveData<WithdrawalsListEntity> withdrawalsList
                = getWithdrawalsInfo(refresherContext, userId);

        userInfo.observeForever(new Observer<UserInfoEntity>() {
            @Override
            public void onChanged(@Nullable UserInfoEntity userInfoEntity) {
                if (userInfoEntity != null) {
                    userCharacteristic.setInfo(userInfoEntity);
                    request.postValue(userCharacteristic);
                    userInfo.removeObserver(this);
                }
            }
        });

        userConnectionInfo.observeForever(new Observer<List<UserConnectionsInfo>>() {
            @Override
            public void onChanged(@Nullable List<UserConnectionsInfo> userConnectionsInfo) {
                if (userConnectionInfo != null) {
                    userCharacteristic.setConnections(userConnectionsInfo);
                    request.postValue(userCharacteristic);
                    userConnectionInfo.removeObserver(this);
                }
            }
        });

        paymentsList.observeForever(new Observer<PaymentsListEntity>() {
            @Override
            public void onChanged(@Nullable PaymentsListEntity paymentsListEntity) {
                if (paymentsListEntity != null) {
                    userCharacteristic.setPaymentsTransactions(paymentsListEntity.getPayments());
                    request.postValue(userCharacteristic);
                    paymentsList.removeObserver(this);
                }
            }
        });

        withdrawalsList.observeForever(new Observer<WithdrawalsListEntity>() {
            @Override
            public void onChanged(@Nullable WithdrawalsListEntity withdrawalsListEntity) {
                if (withdrawalsListEntity != null) {
                    userCharacteristic.setWithdrawalsTransactions(withdrawalsListEntity.getWithdrawals());
                    request.postValue(userCharacteristic);
                    withdrawalsList.removeObserver(this);
                }
            }
        });

        return request;
    }

    private static void putOrReplaceUserCharacteristics(String userId, UserCharacteristic userCharacteristic) {
        tempUserId = userId;
        tempUserCharacteristic = userCharacteristic;
        putOrReplaceUserCharacteristics();
    }

    private static void putOrReplaceUserCharacteristics(){
        if(mapUsersCharacteristic.getValue() == null) {
            Repository.getInstance().initUserCharacteristics(refresherContext);
        }

        iObserver.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer >= ALL_FIELDS_UPDATED) {

                    HashMap currentMap = mapUsersCharacteristic.getValue();
                    if (currentMap != null) {
                        currentMap.put(tempUserId, tempUserCharacteristic);
                        mapUsersCharacteristic.postValue(currentMap);
                        iObserver.removeObserver(this);
                        iObserver.setValue(NON_FIELDS_UPDATED);
                    }
                }
            }
        });

    }

    public MutableLiveData<UserInfoEntity> getUserInfo(Context context, String userId){

        final MutableLiveData<UserInfoEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            UserInfoEntity userInfoEntity = LocalDatabase.getInstance(context).dao()
                            .findUserInfoEntityById(userId);

            request.postValue(userInfoEntity);
        });

        return request;
    }

    public MutableLiveData<List<UserConnectionsInfo>> getUserConnectionInfo(Context context, String userId){

        final MutableLiveData<List<UserConnectionsInfo>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() ->{
            List<UserConnectionsInfo> userConnectionInfo = LocalDatabase.getInstance(context).dao()
                    .findUserConnectionInfoEntityById(userId);

            request.postValue(userConnectionInfo);
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
            request.postValue(paymentsList);
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

        tempUserCharacteristic.setInfo(userInfoEntity);
        iObserver.setValue(ADD_NEW_FIELD);

        executor.databaseExecutor()
                .execute(() -> {
                    LocalDatabase.getInstance(refresherContext).dao()
                                            .insertUserInfo(userInfoEntity);
                            }
                );
    }

    private void insertUserConnectionInfoToDatabase(final List<UserConnectionsInfo> userConnectionsInfoList){

        tempUserCharacteristic.setConnections(userConnectionsInfoList);

        if(iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        for (Iterator<UserConnectionsInfo> iter = userConnectionsInfoList.iterator();
                iter.hasNext(); ) {
            UserConnectionsInfo userConnectionsInfo = iter.next();
            executor.databaseExecutor()
                    .execute(() -> {
                                LocalDatabase.getInstance(refresherContext).dao()
                                        .insertUserConnectionsInfo(userConnectionsInfo);
                            }
                    );
        }
    }


    private void insertPaymentsToDatabase(final PaymentsListEntity paymentsList){

        tempUserCharacteristic.setPaymentsTransactions(paymentsList.getPayments());

        if(iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                iter.hasNext(); ) {

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);

            executor.databaseExecutor().execute(() -> {
                        LocalDatabase.getInstance(refresherContext).dao()
                                .insertCashTransactionsEntities(payment);
            });
        }
    }

    private void insertWithdrawalsToDatabase(final WithdrawalsListEntity withdrawalsList){

        tempUserCharacteristic.setWithdrawalsTransactions(withdrawalsList.getWithdrawals());

        if(iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

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

    public void deleteUserFromDatabase(Context context, UserInfoEntity userInfoEntity){
        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(context).dao()
                    .deleteUserInfo(userInfoEntity);
        });
    }

    public void deleteUserFromDatabase(Context context, String userId){
        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(context).dao()
                    .deleteUserInfoById(userId);
        });
    }

    public void deleteCardFromDatabase(Context context, CardInfoEntity cardInfoEntity){
        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(context).dao().
                    deleteCardEntity(cardInfoEntity);
        });
    }

    public MutableLiveData<LoginResultInfo> addNewUserToDatabase(Context context, String login, String password, int city){
        refresherCity = city;
        refresherContext = context;
        putOrReplaceUserCharacteristics();

        MutableLiveData<LoginResultInfo> data = NetworkDataSource.getInstance().checkLogin(login,password,city);

        data.observeForever(new Observer<LoginResultInfo>() {
            @Override
            public void onChanged(@Nullable LoginResultInfo loginResultInfo) {
                if (loginResultInfo.getResultCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {

                    Repository.getInstance().refresherCertificate = loginResultInfo.getCertificate();
                    findUserInfoInInternet();
                    data.removeObserver(this);
                }
            }
        });
        return data;
    }

    private void findUserInfoInInternet() {
        MutableLiveData<UserInfoEntity> data = NetworkDataSource.
                getInstance().getUserInfo(refresherCertificate, refresherCity);
        data.observeForever(new Observer<UserInfoEntity>() {
                @Override
                public void onChanged(@Nullable UserInfoEntity userInfoEntity) {
                    if (userInfoEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {
                        Repository.getInstance().tempUserId = userInfoEntity.getId();
                        putUserInfoToDatabase(userInfoEntity);
                        insertUserConnectionInfoToDatabase(userInfoEntity.getUserConnectionsInfoList());
                        refreshUserCashTransactions();
                        data.removeObserver(this);
                    }
                }
            });
    }

    private void putUserInfoToDatabase(UserInfoEntity userInfoEntity){
        userInfoEntity.setCertificate(Repository.getInstance().refresherCertificate);
        userInfoEntity.setCity(Repository.getInstance().refresherCity);
        insertUserInfoToDatabase(userInfoEntity);
    }


    public void refreshUserDataInDatabase(Context context, String userId){
        refresherContext = context;
        tempUserId = userId;
        refreshUserInfo(userId);
        refreshUserCashTransactions();
        putOrReplaceUserCharacteristics();
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
        MutableLiveData<PaymentsListEntity> data = NetworkDataSource.getInstance().
                getPayments(refresherCertificate, refresherCity);

        data.observeForever(new Observer<PaymentsListEntity>() {
             @Override
             public void onChanged(@Nullable PaymentsListEntity paymentsListEntity) {
                 if(paymentsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                     insertPaymentsToDatabase(paymentsListEntity);
                     data.removeObserver(this);
                 }
             }
        });

    }

    private void refreshWithdrawals(){
        MutableLiveData<WithdrawalsListEntity> data = NetworkDataSource.
                getInstance().getWithdrawals(refresherCertificate, refresherCity);

        data.observeForever(new Observer<WithdrawalsListEntity>() {
             @Override
             public void onChanged(@Nullable WithdrawalsListEntity withdrawalsListEntity) {
                 if(withdrawalsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                     insertWithdrawalsToDatabase(withdrawalsListEntity);
                     data.removeObserver(this);
                 }
             }
        });
    }
}
