package ua.ck.android.geekhub.mclaut.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.tools.NotificationHelper;


/**
 * Created by bogda on 29.12.2017.
 */

public class NotificationsAlarmReceiver extends BroadcastReceiver implements Observer<List<String>> {
    public static final int ALARM_MANAGER_REQUEST_CODE = 101;
    public static final int NOTIFICATION_HOUR_OF_DAY = 4;
    public static final int NOTIFICATION_MINUTES_SECONDS_OF_DAY = 0;
    public static final String PREFERENCE_DAYS_DEFAULT_VALUE = "-1";
    private MutableLiveData<List<String>> userIds;

    @Override
    public void onReceive(Context context, Intent intent) {
        userIds = Repository.getInstance().getAllUsersId();
        userIds.observeForever(this);
    }

    public void startAlarmManager(Context context){
        Intent alarmIntent = new Intent(context,NotificationsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_MANAGER_REQUEST_CODE,alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTES_SECONDS_OF_DAY);
        calendar.set(Calendar.SECOND, NOTIFICATION_MINUTES_SECONDS_OF_DAY);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    }

    //When get all user's ids
    @Override
    public void onChanged(@Nullable List<String> strings) {
        try {
            if (strings != null && strings.size() != 0) {
                for (String id : strings) {

                    UserInfoEntity entity = Repository.getInstance().
                            getMapUsersCharacteristic().getValue().
                            get(id).getInfo();
                    double dayCounter = 0;
                    for (UserConnectionsInfo info : entity.getUserConnectionsInfoList()) {
                        dayCounter += (Double.parseDouble(entity.getBalance()) /
                                Double.parseDouble(info.getPayAtDay()));
                    }
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(McLautApplication.getContext());
                    int days = Integer.parseInt(prefs.getString("pref_alarm",PREFERENCE_DAYS_DEFAULT_VALUE));
                    if(days != Integer.parseInt(PREFERENCE_DAYS_DEFAULT_VALUE) && dayCounter <= days) {
                        NotificationHelper.
                                getInstance(McLautApplication.getContext()).
                                showLowBalanceNotification(McLautApplication.getContext(), entity.getAccount(), entity.getBalance(), 2);
                    }
                }
            }
            userIds.removeObserver(this);
        }
        catch (NullPointerException e){
            return;
        }
    }
}
