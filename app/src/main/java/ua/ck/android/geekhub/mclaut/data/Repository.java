package ua.ck.android.geekhub.mclaut.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ua.ck.android.geekhub.mclaut.data.database.LocalDatabase;
import ua.ck.android.geekhub.mclaut.data.model.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.model.LoginResultInfo;
import ua.ck.android.geekhub.mclaut.data.model.PaymentsListEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.data.model.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.model.WithdrawalsListEntity;
import ua.ck.android.geekhub.mclaut.data.network.NetworkDataSource;
import ua.ck.android.geekhub.mclaut.data.network.TachcardDataSource;
import ua.ck.android.geekhub.mclaut.tools.McLautAppExecutor;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.ui.tachcardPay.TachcardPayStep1Fragment;
import ua.ck.android.geekhub.mclaut.ui.tachcardPay.TachcardPayViewModel;

public class Repository {

    private static Repository instance;
    private final MutableLiveData<HashMap<String, UserCharacteristic>>
            mapUsersCharacteristic = new MutableLiveData<>();

    private McLautAppExecutor executor = McLautAppExecutor.getInstance();

    private String refresherCertificate;
    private int refresherCity;

    private UserCharacteristic userCharacteristicForMap = new UserCharacteristic();
    private MutableLiveData<Integer> iObserver = new MutableLiveData<>();
    private HashMap<String, CardInfoEntity> mapCardEntities;

    private static final Integer ALL_FIELDS_UPDATED = 4;
    private static final Integer NON_FIELDS_UPDATED = 0;
    private static final Integer ADD_NEW_FIELD = 1;
    public static final Integer ONE_USER = 1;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
            instance.initUserCharacteristics();
            instance.initMapCardEntities();
        }
        return instance;
    }

    public MutableLiveData<HashMap<String, UserCharacteristic>> getMapUsersCharacteristic() {
        return mapUsersCharacteristic;
    }

    public HashMap<String, CardInfoEntity> getMapCardEntities() {
        return mapCardEntities;
    }

    private void initUserCharacteristics(){

        executor.databaseExecutor().execute(() ->{
            MutableLiveData<List<String>> userIds = getAllUsersId();
            userIds.observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> usersId) {
                    if(usersId != null) {
                        userIds.removeObserver(this);

                        HashMap currentMap;

                        if(mapUsersCharacteristic.getValue() == null) {
                            currentMap = new HashMap<String, UserCharacteristic>();
                        } else {
                            currentMap = mapUsersCharacteristic.getValue();
                        }
                        for(Iterator<String> iter = usersId.iterator(); iter.hasNext(); ){
                            String currentUserId = iter.next();

                            MutableLiveData<UserCharacteristic> mutableLiveData
                                    = findUserCharactistics(currentUserId);
                            mutableLiveData.observeForever(new Observer<UserCharacteristic>() {
                                @Override
                                public void onChanged(@Nullable UserCharacteristic userCharacteristic) {
                                    if((userCharacteristic.getInfo() != null)
                                            &&(userCharacteristic.getPaymentsTransactions() != null)
                                            &&(userCharacteristic.getWithdrawalsTransactions() != null)) {
                                        mutableLiveData.removeObserver(this);
                                        currentMap.put(currentUserId ,userCharacteristic);
                                        mutableLiveData.removeObserver(this);

                                        if(currentMap.size() == usersId.size()){
                                            mapUsersCharacteristic.postValue(currentMap);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        });

    }

    private void initMapCardEntities() {
        executor.databaseExecutor().execute(() ->{
            MutableLiveData<List<CardInfoEntity>> cardEntitiesList = getAllCardList();
            cardEntitiesList.observeForever(new Observer<List<CardInfoEntity>>() {
                @Override
                public void onChanged(@Nullable List<CardInfoEntity> cardInfoEntities) {
                    if(cardInfoEntities != null){
                        cardEntitiesList.removeObserver(this);
                        mapCardEntities = new HashMap<>();

                        for(Iterator<CardInfoEntity> iter = cardInfoEntities.iterator();
                            iter.hasNext(); ){
                            CardInfoEntity currentCard = iter.next();
                            mapCardEntities.put(currentCard.getCardNumber(), currentCard);
                        }
                    }
                }
            });
        });
    }

    private MutableLiveData<UserCharacteristic> findUserCharactistics(String userId) {

        UserCharacteristic userCharacteristic = new UserCharacteristic();

        MutableLiveData<UserCharacteristic> request = new MutableLiveData<>();
        request.postValue(userCharacteristic);


        MutableLiveData<UserInfoEntity> userInfo
                = getUserInfo(userId);

        MutableLiveData<List<UserConnectionsInfo>> userConnectionInfo
                = getUserConnectionInfo(userId);

        MutableLiveData<PaymentsListEntity> paymentsList
                = getPaymentsInfo(userId);

        MutableLiveData<WithdrawalsListEntity> withdrawalsList
                = getWithdrawalsInfo(userId);

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
                    userCharacteristic.getInfo().setUserConnectionsInfoList(userConnectionsInfo);
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

    private MutableLiveData<UserInfoEntity> getUserInfo(String userId){

        final MutableLiveData<UserInfoEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            UserInfoEntity userInfoEntity = LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .findUserInfoEntityById(userId);

            request.postValue(userInfoEntity);
        });

        return request;
    }

    private MutableLiveData<List<UserConnectionsInfo>> getUserConnectionInfo(String userId) {

        final MutableLiveData<List<UserConnectionsInfo>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            List<UserConnectionsInfo> userConnectionInfo = LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .findUserConnectionInfoEntityById(userId);

            request.postValue(userConnectionInfo);
        });

        return request;
    }

    private MutableLiveData<WithdrawalsListEntity> getWithdrawalsInfo(String userId) {

        final MutableLiveData<WithdrawalsListEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            WithdrawalsListEntity withdrawalsList = new WithdrawalsListEntity(
                    LocalDatabase.getInstance(
                            McLautApplication.getContext()).dao()
                            .findAllWithdrawalsEntities(userId));

            request.postValue(withdrawalsList);
        });

        return request;
    }

    private MutableLiveData<PaymentsListEntity> getPaymentsInfo(String userId) {
        final MutableLiveData<PaymentsListEntity> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            PaymentsListEntity paymentsList = new PaymentsListEntity(
                    LocalDatabase.getInstance(
                            McLautApplication.getContext()).dao()
                            .findAllPaymentsEntities(userId));
            request.postValue(paymentsList);
        });

        return request;
    }

    public MutableLiveData<List<String>> getAllUsersId() {
        final MutableLiveData<List<String>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            List<String> usersIdList = LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao().getAllUsersId();
            request.postValue(usersIdList);
        });

        return request;
    }

    private MutableLiveData<List<CardInfoEntity>> getAllCardList() {
        final MutableLiveData<List<CardInfoEntity>> request = new MutableLiveData<>();

        executor.databaseExecutor().execute(() -> {
            List<CardInfoEntity> cardInfoList = LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .getAllCardsInfo();
            request.postValue(cardInfoList);
        });

        return request;
    }

    private void insertUserInfoToDatabase(final UserInfoEntity userInfoEntity) {

        userCharacteristicForMap.setInfo(userInfoEntity);

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        executor.databaseExecutor()
                .execute(() -> {
                            LocalDatabase.getInstance(
                                    McLautApplication.getContext()).dao()
                                    .insertUserInfo(userInfoEntity);
                        }
                );
    }

    private void insertUserConnectionInfoToDatabase(final List<UserConnectionsInfo> userConnectionsInfoList) {

        userCharacteristicForMap.getInfo().setUserConnectionsInfoList(userConnectionsInfoList);

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        executor.databaseExecutor()
                .execute(() -> {
                    if (userConnectionsInfoList != null) {
                        for (Iterator<UserConnectionsInfo> iter = userConnectionsInfoList.iterator();
                             iter.hasNext(); ) {
                            UserConnectionsInfo userConnectionsInfo = iter.next();
                            LocalDatabase.getInstance(
                                    McLautApplication.getContext()).dao()
                                    .insertUserConnectionsInfo(userConnectionsInfo);
                        }
                    }
                });
    }


    private void insertPaymentsToDatabase(final PaymentsListEntity paymentsList) {

        userCharacteristicForMap.setPaymentsTransactions(paymentsList.getPayments());

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        executor.databaseExecutor().execute(() -> {
            for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
                 iter.hasNext(); ) {

                CashTransactionsEntity payment = iter.next();
                payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);

                LocalDatabase.getInstance(
                        McLautApplication.getContext()).dao()
                        .insertCashTransactionsEntities(payment);
            }
        });

    }

    private void insertWithdrawalsToDatabase(final WithdrawalsListEntity withdrawalsList) {

        userCharacteristicForMap.setWithdrawalsTransactions(withdrawalsList.getWithdrawals());

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        executor.databaseExecutor().execute(() -> {
            for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
                 iter.hasNext(); ) {

                CashTransactionsEntity withdrawal = iter.next();
                withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);

                LocalDatabase.getInstance(
                        McLautApplication.getContext()).dao()
                        .insertCashTransactionsEntities(withdrawal);
            }
        });
    }

    public void addNewCard(CardInfoEntity cardInfoEntity){

        mapCardEntities.put(cardInfoEntity.getCardNumber(), cardInfoEntity);

        insertCardToDatabase(cardInfoEntity);
    }

    private void insertCardToDatabase(final CardInfoEntity cardInfoEntity){
        executor.databaseExecutor().execute(() -> {
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .insertCardInfoEntity(cardInfoEntity);
        });
    }

    public void deleteCard(String cardName){

        deleteCardFromDatabase(mapCardEntities.get(cardName));
        mapCardEntities.remove(cardName);
    }

    private void deleteCardFromDatabase(CardInfoEntity cardInfoEntity){
        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao().
                    deleteCardEntity(cardInfoEntity);
        });
    }

    public MutableLiveData<LoginResultInfo> addNewUserToDatabase(String login, String password, int city) {

        refresherCity = city;

        MutableLiveData<LoginResultInfo> data = NetworkDataSource.getInstance().checkLogin(login, password, city);

        data.observeForever(new Observer<LoginResultInfo>() {
            @Override
            public void onChanged(@Nullable LoginResultInfo loginResultInfo) {
                if (loginResultInfo.getResultCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {
                    if (!loginResultInfo.getCertificate().equals("0")) {
                        refresherCertificate = loginResultInfo.getCertificate();
                        findUserInfoInInternet();
                    }
                    data.removeObserver(this);
                }
            }
        });
        return data;
    }

    public void deleteUserFromDatabase(String userId){
        HashMap<String, UserCharacteristic>
                currentCharacteristics = mapUsersCharacteristic.getValue();
        currentCharacteristics.remove(userId);
        mapUsersCharacteristic.setValue(currentCharacteristics);

        executor.databaseExecutor().execute(() ->{
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .deleteUserInfoById(userId);
        });
    }

    private void findUserInfoInInternet() {

        MutableLiveData<UserInfoEntity> data = NetworkDataSource.
                getInstance().getUserInfo(refresherCertificate, refresherCity);

        data.observeForever(new Observer<UserInfoEntity>() {
                @Override
                public void onChanged(@Nullable UserInfoEntity userInfoEntity) {
                    if (userInfoEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {

                        userInfoEntity.setCertificate(refresherCertificate);
                        userInfoEntity.setCity(refresherCity);

                        iObserver.setValue(NON_FIELDS_UPDATED);
                        putOrReplaceUserCharacteristics();

                        insertUserInfoToDatabase(userInfoEntity);
                        insertUserConnectionInfoToDatabase(userInfoEntity.getUserConnectionsInfoList());
                        findUserCashTransactions();

                    data.removeObserver(this);
                }
            }
        });
    }

    private void putOrReplaceUserCharacteristics(){
        iObserver.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == ALL_FIELDS_UPDATED) {

                    McLautApplication.selectUser(userCharacteristicForMap.getInfo().getId());

                    HashMap currentMap = mapUsersCharacteristic.getValue();
                    if (currentMap == null) {
                        currentMap = new HashMap<String, UserCharacteristic>();
                    }
                    currentMap.put(userCharacteristicForMap.getInfo().getId(), userCharacteristicForMap);
                    userCharacteristicForMap = new UserCharacteristic();

                    mapUsersCharacteristic.postValue(currentMap);

                    iObserver.removeObserver(this);
                }
            }
        });
    }

    public void refreshUserInfo(String userId){

        executor.databaseExecutor()
                .execute(() -> {
                    refresherCertificate = LocalDatabase.getInstance(
                            McLautApplication.getContext()).dao()
                            .getUserCertificate(userId);
                    refresherCity = LocalDatabase.getInstance(
                            McLautApplication.getContext()).dao()
                            .getUserCity(userId);

                    findUserInfoInInternet();
                });
    }

    private void findUserCashTransactions(){
        findPayments();
        findWithdrawals();
    }

    private void findPayments(){
        MutableLiveData<PaymentsListEntity> data = NetworkDataSource.getInstance().
                getPayments(refresherCertificate, refresherCity);

        data.observeForever(new Observer<PaymentsListEntity>() {
             @Override
             public void onChanged(@Nullable PaymentsListEntity paymentsListEntity) {
                 if(paymentsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                     data.removeObserver(this);
                     insertPaymentsToDatabase(paymentsListEntity);
                 }
             }
        });
    }

    private void findWithdrawals(){
        MutableLiveData<WithdrawalsListEntity> data = NetworkDataSource.
                getInstance().getWithdrawals(refresherCertificate, refresherCity);

        data.observeForever(new Observer<WithdrawalsListEntity>() {
             @Override
             public void onChanged(@Nullable WithdrawalsListEntity withdrawalsListEntity) {
                 if(withdrawalsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE ) {
                     data.removeObserver(this);
                     insertWithdrawalsToDatabase(withdrawalsListEntity);
                 }
             }
        });
    }

    public void getPaymentRedirection(MutableLiveData<Document> redirectDocument,String... strings) {
        TachcardDataSource.getInstance().pay(redirectDocument,strings);
    }

}
