package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ua.ck.android.geekhub.mclaut.data.entities.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;

/**
 * Created by bogda on 15.11.2017.
 */
@android.arch.persistence.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfoEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserConnectionsInfo(UserConnectionsInfo entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCashTransactionsEntities(CashTransactionsEntity entity);

    @Query("SELECT * FROM cashTransactions " +
            "        WHERE typeOfTransaction = 1 " + // 1 equals PAYMENTS
            "        ORDER BY date")
    List<CashTransactionsEntity> findAllPaymentsEntities();

    @Query("SELECT * FROM cashTransactions " +
            "        WHERE typeOfTransaction = 0" + // 0 equals WITHDRAWALS
            "        ORDER BY date")
    List<CashTransactionsEntity> findAllWithdrawalsEntities();

    @Query("SELECT * FROM userInfo" +
            "        WHERE id = :userId")
    UserInfoEntity getUserInfoEntityById(String userId);

    @Query("SELECT * FROM userConnectionsInfo" +
            "        WHERE idClient = :userId")
    UserConnectionsInfo findUserConnectionInfoEntityById(String userId);

    @Query("SELECT id FROM userInfo")
    List<String> getAllUsersId();

    @Query("SELECT name FROM userInfo" +
            "           WHERE id = :userId")
    String getUserNameById(String userId);

    @Query("SELECT certificate FROM userInfo" +
            "                  WHERE id = :userId")
    String getUserCertificate(String userId);

    @Query("SELECT city FROM userInfo" +
            "                  WHERE id = :userId")
    int getUserCity(String userId);

    //TODO: Write other needs methods
}
