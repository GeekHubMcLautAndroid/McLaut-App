package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.TypeConverter;


import java.util.Date;

/**
 * Created by bogda on 15.11.2017.
 */

public class DateConverter {
    @TypeConverter
    public static Date fromUnixEpoch(String milis){
        return new Date(Long.parseLong(milis) * 1000);
    }
}
