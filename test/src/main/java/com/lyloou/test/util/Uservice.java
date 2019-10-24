package com.lyloou.test.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Uservice {

    // https://stackoverflow.com/questions/46445265/android-8-0-java-lang-illegalstateexception-not-allowed-to-start-service-inten
    // https://stackoverflow.com/questions/52496563/java-lang-securityexception-permission-denial-startforeground-android-9-0-pie
    // <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    public static void start(Context context, Class clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, clazz));
        } else {
            context.startService(new Intent(context, clazz));
        }
    }
}
