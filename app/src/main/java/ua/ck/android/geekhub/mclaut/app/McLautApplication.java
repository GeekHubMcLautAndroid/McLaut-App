package ua.ck.android.geekhub.mclaut.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sergo on 12/9/17.
 */

public class McLautApplication extends Application{

    public static final String USER_ID = "_user_id";
    private static final String MCLAUT_PREFERENCES = "_mclaut_preferences";

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

        super.onCreate();
    }

    private static void initPreferences(){
        preferences = instance.getSharedPreferences(MCLAUT_PREFERENCES, MODE_PRIVATE);
    }

    private static void initPreferencesEditor(){
        editor = preferences.edit();
    }

}
