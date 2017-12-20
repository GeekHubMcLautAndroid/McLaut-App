package ua.ck.android.geekhub.mclaut.app;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ua.ck.android.geekhub.mclaut.synchronization.ScheduleStarter;

/**
 * Created by Sergo on 12/9/17.
 */

public class McLautApplication extends Application{

    public static final String USER_ID = "_user_id";
    private static final String MCLAUT_PREFERENCES = "_mclaut_preferences";
    private static final int JOB_ID = 1;

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
}
