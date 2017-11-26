package ua.ck.android.geekhub.mclaut.tools;

import android.os.Looper;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Sergo on 11/26/17.
 */

public class McLautAppExecutor  {

    private static final Object LOCK = new Object();
    private static McLautAppExecutor mInstance;
    private final Executor databaseExecutor;
    private final Executor mainThread;

    private McLautAppExecutor(Executor databaseExecutor, Executor mainThread) {
        this.databaseExecutor = databaseExecutor;
        this.mainThread = mainThread;
    }

    public static McLautAppExecutor getInstance(){
        if (mInstance == null){
            synchronized (LOCK) {
                mInstance = new McLautAppExecutor(
                        Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
            }
        }
        return mInstance;
    }

    public Executor databaseExecutor(){
        return databaseExecutor;
    }

    public Executor mainThread(){
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
