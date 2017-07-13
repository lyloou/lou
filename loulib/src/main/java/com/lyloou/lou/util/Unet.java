package com.lyloou.lou.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Unet {
    // 需要权限支持：<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    public static boolean isAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isAvailable());
    }
}
