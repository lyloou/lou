package com.lyloou.test;

import android.app.Application;

import com.lyloou.test.common.CrashHandler;
import com.lyloou.test.util.Usp;

public class MyApplication extends Application {

    // 是否跳过欢迎界面
    public static boolean sSkipWelcome;

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashLog();
        Usp.init(this);
    }

    private void initCrashLog() {
        CrashHandler.getInstance()
                .setCrashDir("/com.lyloou.test/crash_log/")
                .init(this.getApplicationContext());
    }

}
