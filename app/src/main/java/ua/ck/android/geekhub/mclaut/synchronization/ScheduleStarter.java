package ua.ck.android.geekhub.mclaut.synchronization;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import org.joda.time.Hours;

public class ScheduleStarter {
    private static final int PERIODIC_HOURS = 2;
    private static final int JOB_ID = 1;

    private static ScheduleStarter instance;
    private JobScheduler jobScheduler;

    private ScheduleStarter() {
    }

    public static ScheduleStarter getInstance(Context context){
        if(instance == null){
            instance = new ScheduleStarter();
            instance.jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        return instance;
    }

    public void startSchedule(Context context){
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID,
                new ComponentName(context.getPackageName(), SyncJobService.class.getName())).
                setPeriodic(Hours.hours(PERIODIC_HOURS).toStandardDuration().getMillis())
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobBuilder.build());
        }
    }
}
