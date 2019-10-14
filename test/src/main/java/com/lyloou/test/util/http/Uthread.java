package com.lyloou.test.util.http;

import android.os.Handler;
import android.os.Looper;

public class Uthread {
    public static void runInMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
