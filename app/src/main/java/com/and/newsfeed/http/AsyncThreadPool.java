package com.and.newsfeed.http;

import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncThreadPool {

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // Gets the number of available cores (not always the same as the maximum
    // number of cores)
    private static int NUMBER_OF_CORES = Runtime.getRuntime()
            .availableProcessors();
    private static AsyncThreadPool instance;
    // A queue of Runnables, using LinkedBlockingQueue
    private final BlockingQueue<Runnable> mDecodeWorkQueue;
    private ThreadPoolExecutor mExecutor;
    private Handler mUIHandler;

    private AsyncThreadPool(Handler uiHandler) {
        mUIHandler = uiHandler;
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
        mExecutor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDecodeWorkQueue);
    }

    public synchronized static void init(Handler uiHandler) {
        instance = new AsyncThreadPool(uiHandler);
    }

    public static AsyncThreadPool get() {
        if (instance == null)
            throw new RuntimeException("Tracker has not been initialized");
        return instance;
    }

    public Executor getExecutor() {
        return mExecutor;
    }

    public void add(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
