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

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;

import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.CashTransactionsEntity;
import ua.ck.android.geekhub.mclaut.data.model.UserConnectionsInfo;
import ua.ck.android.geekhub.mclaut.data.model.UserInfoEntity;
import ua.ck.android.geekhub.mclaut.tools.DateConverter;
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

    public void startAlarmManager(Context context) {
        Intent alarmIntent = new Intent(context, NotificationsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_MANAGER_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTES_SECONDS_OF_DAY);
        calendar.set(Calendar.SECOND, NOTIFICATION_MINUTES_SECONDS_OF_DAY);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
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
                    MutableLiveData<CashTransactionsEntity> cashEntityLiveData = Repository.getInstance().getLastTransaction(id);
                    cashEntityLiveData.observeForever(cashEntity -> {
                        int dayCounter = 0;
                        double payAtDay = 0;
                        if (cashEntity == null) {
                            return;
                        }
                        double summAfterLastTransaction = cashEntity.getSumBefore();
                        if (cashEntity.getTypeOfTransaction() == 1)  // 1 equals PAYMENTS
                        {
                            summAfterLastTransaction += cashEntity.getSum();
                        } else {
                            summAfterLastTransaction -= cashEntity.getSum();
                        }
                        for (UserConnectionsInfo info : entity.getUserConnectionsInfoList()) {
                            payAtDay += Double.parseDouble(info.getPayAtDay());
                        }
                        dayCounter += (summAfterLastTransaction) / payAtDay;
                        DateTime transactionDate = DateConverter.fromTimestampToDateTime(cashEntity.getDate());
                        DateTime now = DateTime.now();
                        int daysToEnd;
                        if (now.getYear() == transactionDate.getYear()) {
                            daysToEnd = transactionDate.getDayOfYear() + dayCounter - now.getDayOfYear();
                        } else {
                            int yearDif = Math.abs(now.getYear() - transactionDate.getYear());
                            daysToEnd = transactionDate.getDayOfYear() + dayCounter - now.getDayOfYear() - 365 * yearDif;//Maybe sometimes I make it better;
                        }
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(McLautApplication.getContext());
                        int days = Integer.parseInt(prefs.getString("pref_alarm", PREFERENCE_DAYS_DEFAULT_VALUE));
                        if (daysToEnd <= days && daysToEnd >= 0) {
                            NotificationHelper.
                                    getInstance(McLautApplication.getContext()).
                                    showLowBalanceNotification(McLautApplication.getContext(), entity.getAccount(), summAfterLastTransaction, daysToEnd);
                        }
                    });
                }
            }
            userIds.removeObserver(this);
        } catch (NullPointerException ignored) {
        }
    }
}
