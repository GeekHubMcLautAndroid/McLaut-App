package ua.ck.android.geekhub.mclaut.tools;

import android.os.Build;
import android.util.Base64;


import java.nio.charset.StandardCharsets;

import ua.ck.android.geekhub.mclaut.BuildConfig;

/**
 * Created by bogda on 19.11.2017.
 */

public class McLautHashGenerator {

    public static String generateLoginHash(String login, String password, int city){
        String queryAction = "action=checkLogin&" +
                "login=" + login +
                "&pass=" + password +
                "&city=" + city;


        return Base64.encodeToString(
                (generateFullUrlHash(queryAction) +
                        "&system_info=" +
                        Base64.encodeToString(
                                generateDeviceInfoHash().getBytes(StandardCharsets.UTF_8),
                                2)
                ).getBytes(StandardCharsets.UTF_8), 2);
    }

    public static String getUserInfo(String certificate,int city){
        return generateGetDataHash("getInfo",certificate,city);
    }
    public static String getWithdrawalsInfo(String certificate, int city){
        return generateGetDataHash("getWithdrawals",certificate,city);
    }
    public static String getPaymentsInfo(String certificate, int city){
        return generateGetDataHash("getPayments",certificate,city);
    }

    private static String generateGetDataHash(String action,String certificate, int city){
        String queryAction = "action="+ action +"&" +
                "certificate=" + certificate +
                "&city=" + city;


        return Base64.encodeToString(
                (generateFullUrlHash(queryAction) +
                        "&system_info=" +
                        Base64.encodeToString(
                                generateDeviceInfoHash().getBytes(StandardCharsets.UTF_8),
                                2)
                ).getBytes(StandardCharsets.UTF_8), 2);
    }

    private static String generateDeviceInfoHash(){
        return "API[" + Build.VERSION.SDK + "], "
                + "Device[" + Build.DEVICE + "], "
                + "Model[" + Build.MODEL + "],"
                + "Product[" + Build.PRODUCT + "]";
    }
    private static String generateFullUrlHash(String queryAction){
        return "app=mobile&version=" + BuildConfig.VERSION_CODE + "." +
                BuildConfig.VERSION_NAME +  "&version_code="+BuildConfig.VERSION_CODE+"&" + queryAction;
    }
}
