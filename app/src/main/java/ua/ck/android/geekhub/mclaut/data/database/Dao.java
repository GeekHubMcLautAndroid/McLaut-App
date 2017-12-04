package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ua.ck.android.geekhub.mclaut.data.entities.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;

/**
 * Created by bogda on 15.11.2017.
 */
@android.arch.persistence.room.Dao
public interface Dao {
// UserInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfoEntity entity);

    @Delete
    void deleteUserInfo(UserInfoEntity entity);

    @Query("DELETE FROM userInfo" +
            "        WHERE id = :userId")
    void deleteUserInfoById(String userId);

    @Query("SELECT * FROM userInfo" +
            "        WHERE id = :userId")
    UserInfoEntity findUserInfoEntityById(String userId);

    @Query("SELECT name FROM userInfo" +
            "           WHERE id = :userId")
    String getUserNameById(String userId);

    @Query("SELECT certificate FROM userInfo" +
            "                  WHERE id = :userId")
    String getUserCertificate(String userId);

    @Query("SELECT city FROM userInfo" +
            "                  WHERE id = :userId")
    int getUserCity(String userId);

////

// UserConnectionInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserConnectionsInfo(UserConnectionsInfo entity);

    @Query("SELECT * FROM userConnectionsInfo" +
            "        WHERE idClient = :userId")
    List<UserConnectionsInfo> findUserConnectionInfoEntityById(String userId);

////

// СashTransactions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCashTransactionsEntities(CashTransactionsEntity entity);

    @Query("SELECT * FROM cashTransactions " +
            "        WHERE idClient = :userId AND typeOfTransaction = 1 " + // 1 equals PAYMENTS
            "        ORDER BY date")
    List<CashTransactionsEntity> findAllPaymentsEntities(String userId);

    @Query("SELECT * FROM cashTransactions " +
            "        WHERE idClient = :userId AND typeOfTransaction = 0" + // 0 equals WITHDRAWALS
            "        ORDER BY date")
    List<CashTransactionsEntity> findAllWithdrawalsEntities(String userId);

////

// CardInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCardInfoEntity(CardInfoEntity entity);

    @Delete
    void deleteCardEntity(CardInfoEntity entity);

    @Update
    void updateCardEntity(CardInfoEntity entity);

    @Query("SELECT * FROM cardEntity " +
            "        ORDER BY counterOfUses DESC")
    List<CardInfoEntity> getAllCardsInfo();

////

    @Query("SELECT id FROM userInfo")
    List<String> getAllUsersId();



    //TODO: Write other needs methods
}
