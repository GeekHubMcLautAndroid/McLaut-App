package ua.ck.android.geekhub.mclaut.synchronization;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.joda.time.Hours;


public class StartSyncReceiver extends BroadcastReceiver {
    public static final int PERIODIC_HOURS = 6;
    public static final int JOB_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID,
                new ComponentName(context.getPackageName(), SyncJobService.class.getName()))
                .setPeriodic(Hours.hours(PERIODIC_HOURS).toStandardDuration().getMillis())
                .setPersisted(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobBuilder.build());
        }
    }
}
