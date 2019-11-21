package com.lyloou.test.common.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DbCommand<T> {
    private static ExecutorService sService = Executors.newSingleThreadExecutor();
    private final static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public final void execute() {
        sService.execute(() -> postResult(doInBackground()));
    }

    private void postResult(T result) {
        sUIHandler.post(() -> onPostExecute(result));
    }

    protected void onPostExecute(T result) {

    }

    protected abstract T doInBackground();

}
