package com.lyloou.test;

import android.app.Application;

import com.lyloou.test.common.CrashHandler;

public class MyApplication extends Application {

    // 是否跳过欢迎界面
    public static boolean sSkipWelcome;

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashLog();
    }

    private void initCrashLog() {
        CrashHandler.getInstance()
                .setCrashDir("/com.lyloou.test/crash_log/")
                .init(this.getApplicationContext());
    }

}
