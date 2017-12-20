package ua.ck.android.geekhub.mclaut.synchronization;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;


import java.util.List;

import ua.ck.android.geekhub.mclaut.data.Repository;


public class SyncJobService extends JobService implements Observer<List<String>> {
    private LiveData<List<String>> data = new MutableLiveData<>();
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        data = Repository.getInstance().getAllUsersId();
        data.observeForever(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        data.removeObserver(this);
        return false;
    }


    //all user id`s from repository
    @Override
    public void onChanged(@Nullable List<String> strings) {
        if (strings != null) {
            for(String s : strings){
                Repository.getInstance().refreshUserInfo(s);
            }
        }
    }
}
