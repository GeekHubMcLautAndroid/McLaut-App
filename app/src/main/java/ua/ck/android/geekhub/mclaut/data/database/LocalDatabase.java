package ua.ck.android.geekhub.mclaut.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import ua.ck.android.geekhub.mclaut.data.model.CardInfoEntity;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;

/**
 * Created by bogda on 15.11.2017.
 */

@Database(entities = {UserInfoEntity.class,
                      UserConnectionsInfo.class,
                      CashTransactionsEntity.class,
                      CardInfoEntity.class}, version = 1, exportSchema = false)

public abstract class LocalDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "McLautDatabase";
    private static final Object LOCK = new Object();
    private static volatile LocalDatabase instance;

    private static final int lastVersion = 1;
    private static final int newVersion = 2;

    private static Migration MIGRATION = new Migration(lastVersion,newVersion) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {}
    };

    public  static  LocalDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class,
                            LocalDatabase.DATABASE_NAME)
                           // .addMigrations(MIGRATION)
                            .build();
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
