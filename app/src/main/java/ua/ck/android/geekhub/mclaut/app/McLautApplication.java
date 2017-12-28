package ua.ck.android.geekhub.mclaut.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import ua.ck.android.geekhub.mclaut.synchronization.ScheduleStarter;

/**
 * Created by Sergo on 12/9/17.
 */

public class McLautApplication extends Application{

    public static final String USER_ID = "_user_id";
    private static final String MCLAUT_PREFERENCES = "_mclaut_preferences";
    private static final int JOB_ID = 1;
    public static final int NOTIFICATION_HOUR_OF_DAY = 4;
    public static final int NOTIFICATION_MINUTES_SECONDS_OF_DAY = 0;
    public static final String ALARM_MANAGER_INTENT_ACTION = "start_notification";

    private static String userId;
    private static McLautApplication instance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static McLautApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getBaseContext();
    }

    public static SharedPreferences getPreferences(){
        return  preferences;
    }

    public static void selectUser(String newUserId){
        userId = newUserId;
        editor.putString(USER_ID, userId).commit();
    }

    public static String getSelectedUser(){
        if(userId == null){
            userId = preferences.getString(USER_ID, "NULL");
        }
       return userId;
    }

    @Override
    public void onCreate(){
        instance = this;
        initPreferences();
        initPreferencesEditor();

        if(!checkSchedulerStart()){
            ScheduleStarter.getInstance(this).startSchedule(this);
        }

        if(!checkLowBalanceNotificationAlarmManagerEnabled()){
            setLowBalanceNotificationAlarmManager();
        }

        super.onCreate();
    }

    private static void initPreferences(){
        preferences = instance.getSharedPreferences(MCLAUT_PREFERENCES, MODE_PRIVATE);
    }

    private static void initPreferencesEditor(){
        editor = preferences.edit();
    }

    private boolean checkSchedulerStart(){
        JobScheduler scheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        boolean hasBeenScheduled = false;
        if (scheduler != null) {
            for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == JOB_ID) {
                    hasBeenScheduled = true;
                    break;
                }
            }
        }
        return hasBeenScheduled;
    }
    private void setLowBalanceNotificationAlarmManager(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTES_SECONDS_OF_DAY);
        calendar.set(Calendar.SECOND, NOTIFICATION_MINUTES_SECONDS_OF_DAY);

        Intent alarmIntent = new Intent(ALARM_MANAGER_INTENT_ACTION);
        PendingIntent alarmPendingIntent = PendingIntent.
                getBroadcast(this,0,alarmIntent,0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,alarmPendingIntent);
        }
    }
    private boolean checkLowBalanceNotificationAlarmManagerEnabled(){
        return (PendingIntent.getBroadcast(this, 0,
                new Intent(ALARM_MANAGER_INTENT_ACTION),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
