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
        String deviceInfo = "API[" + Build.VERSION.SDK + "], "
                + "Device[" + Build.DEVICE + "], "
                + "Model[" + Build.MODEL + "],"
                + "Product[" + Build.PRODUCT + "]";
        String queryAction = "action=checkLogin&" +
                "login=" + login +
                "&pass=" + password +
                "&city=" + city;

        String fullUrl = "app=mobile&version=" + BuildConfig.VERSION_CODE + "." +
                BuildConfig.VERSION_NAME +  "&version_code="+BuildConfig.VERSION_CODE+"&" + queryAction;

        return Base64.encodeToString(
                (fullUrl +
                        "&system_info=" +
                        Base64.encodeToString(
                                deviceInfo.getBytes(StandardCharsets.UTF_8),
                                2)
                ).getBytes(StandardCharsets.UTF_8), 2);

    }
}
