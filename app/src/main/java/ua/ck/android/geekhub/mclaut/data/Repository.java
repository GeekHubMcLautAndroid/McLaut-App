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

    private String userIdForPuttingToMap;

    private UserCharacteristic userCharacteristicForMap = new UserCharacteristic();
    private MutableLiveData<Integer> iObserver = new MutableLiveData<>();

    private static final Integer ALL_FIELDS_UPDATED = 4;
    private static final Integer NON_FIELDS_UPDATED = 0;
    private static final Integer ADD_NEW_FIELD = 1;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
            instance.initUserCharacteristics();
        }
        return instance;
    }

    public MutableLiveData<HashMap<String, UserCharacteristic>> getMapUsersCharacteristic() {
        return mapUsersCharacteristic;
    }

    private void initUserCharacteristics() {

        executor.databaseExecutor().execute(() -> {
            getAllUsersId().observeForever(new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> usersId) {
                    if (usersId != null) {
                        for (Iterator<String> iter = usersId.iterator(); iter.hasNext(); ) {

                            String userId = iter.next();
                            MutableLiveData<UserCharacteristic> mutableLiveData
                                    = getUserCharacteristics(userId);
                            mutableLiveData.observeForever(new Observer<UserCharacteristic>() {
                                @Override
                                public void onChanged(@Nullable UserCharacteristic userCharacteristic) {
                                    if ((userCharacteristic.getInfo() != null)
                                            && (userCharacteristic.getPaymentsTransactions() != null)
                                            && (userCharacteristic.getWithdrawalsTransactions() != null)) {
                                        HashMap currentMap;

                                        if (mapUsersCharacteristic.getValue() == null) {
                                            currentMap = new HashMap<String, UserCharacteristic>();
                                        } else {
                                            currentMap = mapUsersCharacteristic.getValue();
                                        }

                                        currentMap.put(userId, userCharacteristic);
                                        mapUsersCharacteristic.postValue(currentMap);
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

    private void putOrReplaceUserCharacteristics(String userId, UserCharacteristic userCharacteristic) {
        userIdForPuttingToMap = userId;
        userCharacteristicForMap = userCharacteristic;
        putOrReplaceUserCharacteristics();
    }

    private void putOrReplaceUserCharacteristics() {

        iObserver.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == ALL_FIELDS_UPDATED) {

                    HashMap currentMap = mapUsersCharacteristic.getValue();

                    if (currentMap == null) {
                        currentMap = new HashMap<String, UserCharacteristic>();
                    }

                    currentMap.put(userIdForPuttingToMap, userCharacteristicForMap);
                    mapUsersCharacteristic.postValue(currentMap);
                    iObserver.removeObserver(this);
                    iObserver.postValue(NON_FIELDS_UPDATED);
                }
            }
        });

    }

    private MutableLiveData<UserInfoEntity> getUserInfo(String userId) {

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

    private MutableLiveData<List<String>> getAllUsersId() {
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

        for (Iterator<UserConnectionsInfo> iter = userConnectionsInfoList.iterator();
             iter.hasNext(); ) {
            UserConnectionsInfo userConnectionsInfo = iter.next();
            executor.databaseExecutor()
                    .execute(() -> {
                                LocalDatabase.getInstance(
                                        McLautApplication.getContext()).dao()
                                        .insertUserConnectionsInfo(userConnectionsInfo);
                            }
                    );
        }
    }


    private void insertPaymentsToDatabase(final PaymentsListEntity paymentsList) {

        userCharacteristicForMap.setPaymentsTransactions(paymentsList.getPayments());

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        for (Iterator<CashTransactionsEntity> iter = paymentsList.getPayments().iterator();
             iter.hasNext(); ) {

            CashTransactionsEntity payment = iter.next();
            payment.setTypeOfTransaction(CashTransactionsEntity.PAYMENTS);

            executor.databaseExecutor().execute(() -> {
                LocalDatabase.getInstance(
                        McLautApplication.getContext()).dao()
                        .insertCashTransactionsEntities(payment);
            });
        }
    }

    private void insertWithdrawalsToDatabase(final WithdrawalsListEntity withdrawalsList) {

        userCharacteristicForMap.setWithdrawalsTransactions(withdrawalsList.getWithdrawals());

        if (iObserver.getValue() == null) {
            iObserver.setValue(ADD_NEW_FIELD);
        } else {
            iObserver.setValue(iObserver.getValue() + ADD_NEW_FIELD);
        }

        for (Iterator<CashTransactionsEntity> iter = withdrawalsList.getWithdrawals().iterator();
             iter.hasNext(); ) {

            CashTransactionsEntity withdrawal = iter.next();
            withdrawal.setTypeOfTransaction(CashTransactionsEntity.WITHDRAWALS);

            executor.databaseExecutor().execute(() -> {
                LocalDatabase.getInstance(
                        McLautApplication.getContext()).dao()
                        .insertCashTransactionsEntities(withdrawal);
            });
        }
    }

    public void deleteUserFromDatabase(UserInfoEntity userInfoEntity) {
        executor.databaseExecutor().execute(() -> {
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .deleteUserInfo(userInfoEntity);
        });
    }

    public void deleteUserFromDatabase(String userId) {
        executor.databaseExecutor().execute(() -> {
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao()
                    .deleteUserInfoById(userId);
        });
    }

    public void deleteCardFromDatabase(CardInfoEntity cardInfoEntity) {
        executor.databaseExecutor().execute(() -> {
            LocalDatabase.getInstance(
                    McLautApplication.getContext()).dao().
                    deleteCardEntity(cardInfoEntity);
        });
    }

    public MutableLiveData<LoginResultInfo> addNewUserToDatabase(String login, String password, int city) {

        refresherCity = city;
        putOrReplaceUserCharacteristics();

        MutableLiveData<LoginResultInfo> data = NetworkDataSource.getInstance().checkLogin(login, password, city);

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
                    Repository.getInstance().userIdForPuttingToMap = userInfoEntity.getId();

                    McLautApplication.selectUser(userInfoEntity.getId());

                    putUserInfoToDatabase(userInfoEntity);
                    insertUserConnectionInfoToDatabase(userInfoEntity.getUserConnectionsInfoList());
                    refreshUserCashTransactions();

                    data.removeObserver(this);
                }
            }
        });
    }

    private void putUserInfoToDatabase(UserInfoEntity userInfoEntity) {
        userInfoEntity.setCertificate(Repository.getInstance().refresherCertificate);
        userInfoEntity.setCity(Repository.getInstance().refresherCity);
        insertUserInfoToDatabase(userInfoEntity);
    }


    public void refreshUserDataInDatabase(String userId) {
        userIdForPuttingToMap = userId;
        refreshUserInfo(userId);
        refreshUserCashTransactions();
        putOrReplaceUserCharacteristics();
    }

    private void refreshUserInfo(String userId) {

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

    private void refreshUserCashTransactions() {
        refreshPayments();
        refreshWithdrawals();
    }

    private void refreshPayments() {
        MutableLiveData<PaymentsListEntity> data = NetworkDataSource.getInstance().
                getPayments(refresherCertificate, refresherCity);

        data.observeForever(new Observer<PaymentsListEntity>() {
            @Override
            public void onChanged(@Nullable PaymentsListEntity paymentsListEntity) {
                if (paymentsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {
                    insertPaymentsToDatabase(paymentsListEntity);
                    data.removeObserver(this);
                }
            }
        });
    }

    private void refreshWithdrawals() {
        MutableLiveData<WithdrawalsListEntity> data = NetworkDataSource.
                getInstance().getWithdrawals(refresherCertificate, refresherCity);

        data.observeForever(new Observer<WithdrawalsListEntity>() {
            @Override
            public void onChanged(@Nullable WithdrawalsListEntity withdrawalsListEntity) {
                if (withdrawalsListEntity.getLocalResCode() == NetworkDataSource.RESPONSE_SUCCESSFUL_CODE) {
                    insertWithdrawalsToDatabase(withdrawalsListEntity);
                    data.removeObserver(this);
                }
            }
        });
    }

    public void getPaymentRedirection(MutableLiveData<Document> redirectDocument,String... strings) {
        TachcardDataSource.getInstance().pay(redirectDocument,strings);
    }
}
