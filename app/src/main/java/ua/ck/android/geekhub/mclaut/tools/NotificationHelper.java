package ua.ck.android.geekhub.mclaut.tools;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.ui.tachcardPay.TachcardPayActivity;


public class NotificationHelper {

    private static final String MCLAUT_NOTIFICATIONS_CHANNEL_ID = "ua.ck.android.geekhub.mclaut.notifications";
    private static final String MCLAUT_NOTIFICATIONS_CHANNEL_NAME = "MCLAUT_APPLICATION";
    private static final int NOTIFY_ID = 101;

    private static NotificationHelper instance;
    private NotificationManager manager;
    private NotificationHelper() {
    }

    public static NotificationHelper getInstance(Context context){
        if(instance == null){
            instance = new NotificationHelper();
            instance.setManager((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
            instance.addNotificationsChannel();
        }
        return instance;
    }


    private void setManager(NotificationManager manager) {
        this.manager = manager;
    }

    public void showLowBalanceNotification(Context context, String account, String balance, int days){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MCLAUT_NOTIFICATIONS_CHANNEL_ID);
        String title = context.getString(R.string.notification_low_balance_title);
        String content = context.getString(R.string.notification_low_balance_content,account,balance,days);
        //TODO: set app icon
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setChannelId(MCLAUT_NOTIFICATIONS_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        builder.setSound(null);
        builder.setVibrate(new long[]{0,0,0,0,0});
        Intent showPayActivityIntent = new Intent(context, TachcardPayActivity.class);
        PendingIntent notificationContentIntent = PendingIntent.getActivity(context,0,showPayActivityIntent,
                0);
        builder.setContentIntent(notificationContentIntent);
        manager.notify(NOTIFY_ID,builder.build());
    }

    private void addNotificationsChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(MCLAUT_NOTIFICATIONS_CHANNEL_ID, MCLAUT_NOTIFICATIONS_CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(false);
            channel.setSound(null,null);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
    }
}
