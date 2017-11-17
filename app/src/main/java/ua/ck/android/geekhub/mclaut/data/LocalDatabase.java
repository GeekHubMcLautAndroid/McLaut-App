package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by bogda on 15.11.2017.
 */

@Database(entities = {UserInfoEntity.class,
        UserConnectionsInfo.class,
        PaymentsEntity.class,
        WithdrawalsEntity.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class LocalDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "McLautDatabase";
    private static final Object LOCK = new Object();
    private static volatile LocalDatabase instance;

    public  static  LocalDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class,
                            LocalDatabase.DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract Dao dao();

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
