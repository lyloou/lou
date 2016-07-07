package com.lyloou.lou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Set;

/**
 * 类描述：SharedPreference 工具类；
 * 创建人： Lou
 * 创建时间： 2016/7/7 14:38
 * 修改人： Lou
 * 修改时间：2016/7/7 14:38
 * 修改备注：
 */
public final class Usp {
    ///////////////////////////////////////////////////////////////////////////
    // 对外提供接口
    ///////////////////////////////////////////////////////////////////////////
    private static Usp INSTANCE;
    private static SharedPreferences.Editor sEditor;

    public static Usp init(Context context, String spName) {
        if (INSTANCE == null)
            INSTANCE = new Usp(context, spName);

        if (sEditor == null)
            sEditor = INSTANCE.mSharedPreferences.edit();
        return INSTANCE;
    }

    public static Usp init(Context context) {
        return init(context, null);
    }

    public void commit() {
        sEditor.commit();
    }

    public Usp clear() {
        sEditor.clear().apply();
        return INSTANCE;
    }

    public Usp putString(String var1, String var2) {
        sEditor.putString(var1, var2).apply();
        return INSTANCE;
    }

    public Usp putString(String var1, Set<String> var2) {
        sEditor.putStringSet(var1, var2).apply();
        return INSTANCE;
    }


    public Usp putInt(String var1, int var2) {
        sEditor.putInt(var1, var2).apply();
        return INSTANCE;
    }

    public Usp putLong(String var1, long var2) {
        sEditor.putLong(var1, var2).apply();
        return INSTANCE;
    }

    public Usp putFloat(String var1, float var2) {
        sEditor.putFloat(var1, var2).apply();
        return INSTANCE;
    }

    public Usp putBoolean(String var1, boolean var2) {
        sEditor.putBoolean(var1, var2).apply();
        return INSTANCE;
    }

    public Usp remove(String var1) {
        sEditor.remove(var1).apply();
        return INSTANCE;
    }

    public String getString(String var1, String var2) {
        return INSTANCE.mSharedPreferences.getString(var1, var2);
    }

    public Set<String> getStringSet(String var1, Set<String> var2) {
        return INSTANCE.mSharedPreferences.getStringSet(var1, var2);
    }

    public int getInt(String var1, int var2) {
        return INSTANCE.mSharedPreferences.getInt(var1, var2);
    }

    public long getLong(String var1, long var2) {
        return INSTANCE.mSharedPreferences.getLong(var1, var2);
    }

    public float getFloat(String var1, float var2) {
        return INSTANCE.mSharedPreferences.getFloat(var1, var2);
    }

    public boolean getBoolean(String var1, boolean var2) {
        return INSTANCE.mSharedPreferences.getBoolean(var1, var2);
    }

    public boolean contains(String var1) {
        return INSTANCE.mSharedPreferences.contains(var1);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Self
    ///////////////////////////////////////////////////////////////////////////
    private final SharedPreferences mSharedPreferences;

    private Usp(Context context, @Nullable String spName) {
        if (TextUtils.isEmpty(spName)) {
            // 默认的包名使用「包名_SP」
            spName = context.getApplicationContext().getPackageName() + "_SP";
        }
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }


}
