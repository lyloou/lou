package com.lyloou.demo;

import android.app.Application;

import com.lyloou.lou.util.Usp;
import com.lyloou.lou.util.Uvolley;

/**
 * Created by Administrator on 2016.11.04.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Uvolley.init(this);
    }
}
