package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import ua.ck.android.geekhub.mclaut.data.entities.PaymentsEntity;
import ua.ck.android.geekhub.mclaut.data.entities.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.data.entities.WithdrawalsEntity;

/**
 * Created by bogda on 15.11.2017.
 */
@android.arch.persistence.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfoEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPaymentsEntities(PaymentsEntity... entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWithdrawalsEntities(WithdrawalsEntity... entities);


    //TODO: Write other needs methods
}
