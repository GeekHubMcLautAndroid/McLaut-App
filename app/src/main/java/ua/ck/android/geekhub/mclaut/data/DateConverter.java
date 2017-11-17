package ua.ck.android.geekhub.mclaut.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by bogda on 15.11.2017.
 */

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(String milis) {
        return new Date(Long.parseLong(milis) * 1000);
    }

    @TypeConverter
    public static String fromDateToTimestamp(Date date) {
        return String.valueOf(date.getTime() / 1000);
    }
}
