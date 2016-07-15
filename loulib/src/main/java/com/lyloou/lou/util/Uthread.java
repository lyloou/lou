package com.lyloou.lou.util;

import android.os.Looper;

/**
 * Created by admin on 2016/5/31.
 */
public class Uthread {

    /**
     * 判断当前线程是否是主线程；
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
