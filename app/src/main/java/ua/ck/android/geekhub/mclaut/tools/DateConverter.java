package ua.ck.android.geekhub.mclaut.tools;

import org.joda.time.DateTime;


/**
 * Created by bogda on 14.12.2017.
 */

public class DateConverter {
    private DateConverter() {
    }

    public static DateTime fromTimestampToDateTime(long timestamp){
        return new DateTime(timestamp * 1000L);
    }
}
