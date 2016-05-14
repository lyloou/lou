package com.lyloou.lou.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Uscreen {

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    public static float getScreenDensity(Context context) {
        return getMetrics(context).density;
    }

    public static float getScreenScaleDensity(Context context) {
        return getMetrics(context).scaledDensity;
    }

    public static int dp2Px(Context context, float dp) {
        float px = (int) (getScreenDensity(context) * dp);
        return (int) (px + 0.5f);
    }

    public static float sp2Px(Context context, float sp) {
        float px = getScreenScaleDensity(context);
        return sp * px;
    }
}