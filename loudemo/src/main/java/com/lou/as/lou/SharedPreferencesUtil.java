package com.lou.as.lou;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class SharedPreferencesUtil {
    private static final String SPU_NAME = "my_spu";
    private static final String KEY_SKIN = "KEY_SKIN";
    private static SharedPreferencesUtil sSharedPreferencesUtil;
    private SharedPreferences sp;

    private SharedPreferencesUtil(Context context) {
        sp = context.getSharedPreferences(SPU_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (sSharedPreferencesUtil == null) {
            sSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sSharedPreferencesUtil;
    }

    public void saveSkin(int color) {
        sp.edit().putInt(KEY_SKIN, color).commit();
    }

    public int getSkin() {
        return sp.getInt(KEY_SKIN, Color.LTGRAY);
    }

}