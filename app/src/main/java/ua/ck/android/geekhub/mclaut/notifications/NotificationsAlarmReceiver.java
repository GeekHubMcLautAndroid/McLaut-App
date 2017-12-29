package ua.ck.android.geekhub.mclaut.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;

import java.util.List;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.tools.NotificationHelper;


/**
 * Created by bogda on 29.12.2017.
 */

public class NotificationsAlarmReceiver extends BroadcastReceiver implements Observer<List<String>> {
    public static final int ALARM_MANAGER_REQUEST_CODE = 101;
    @Override
    public void onReceive(Context context, Intent intent) {
        Repository.getInstance().getAllUsersId().observeForever(this);
    }
    public void startAlarmManager(Context context){
        Intent alarmIntent = new Intent(context,NotificationsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_MANAGER_REQUEST_CODE,alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //TEMP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, pendingIntent);
            }
        } else {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, pendingIntent);
            }
        }
    }

    //When get all user's ids
    @Override
    public void onChanged(@Nullable List<String> strings) {
        //TEMP
        if (strings != null && strings.size() != 0) {
            for(String id : strings){
                UserInfoEntity entity = Repository.getInstance().
                        getMapUsersCharacteristic().getValue().
                        get(id).getInfo();
                NotificationHelper.
                        getInstance(McLautApplication.getContext()).
                        showLowBalanceNotification(McLautApplication.getContext(),entity.getAccount(),entity.getBalance(),1);
            }
        }
    }
}
