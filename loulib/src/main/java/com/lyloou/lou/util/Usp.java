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
    private static Usp sInstance;
    private static SharedPreferences.Editor sEditor;

    private final SharedPreferences mSharedPreferences;

    private Usp(Context context, @Nullable String spName) {
        if (TextUtils.isEmpty(spName)) {
            // 默认的包名使用「包名_SP」
            spName = context.getApplicationContext().getPackageName() + "_SP";
        }
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static Usp init(Context context, String spName) {
        if (sInstance == null)
            sInstance = new Usp(context, spName);

        if (sEditor == null)
            sEditor = sInstance.mSharedPreferences.edit();
        return sInstance;
    }

    public static Usp getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Not yet initialized, Please call Usp.init(Context) in your Application");
        }
        return sInstance;
    }

    public static Usp init(Context context) {
        return init(context, null);
    }

    public void commit() {
        sEditor.commit();
    }

    public void apply() {
        sEditor.apply();
    }

    public Usp clear() {
        sEditor.clear();
        return sInstance;
    }

    public Usp putString(String var1, String var2) {
        sEditor.putString(var1, var2);
        return sInstance;
    }

    public Usp putStringSet(String var1, Set<String> var2) {
        sEditor.putStringSet(var1, var2);
        return sInstance;
    }


    public Usp putInt(String var1, int var2) {
        sEditor.putInt(var1, var2);
        return sInstance;
    }

    public Usp putLong(String var1, long var2) {
        sEditor.putLong(var1, var2);
        return sInstance;
    }

    public Usp putFloat(String var1, float var2) {
        sEditor.putFloat(var1, var2);
        return sInstance;
    }

    public Usp putBoolean(String var1, boolean var2) {
        sEditor.putBoolean(var1, var2);
        return sInstance;
    }

    public Usp remove(String var1) {
        sEditor.remove(var1);
        return sInstance;
    }

    public String getString(String var1, String var2) {
        return sInstance.mSharedPreferences.getString(var1, var2);
    }

    public Set<String> getStringSet(String var1, Set<String> var2) {
        return sInstance.mSharedPreferences.getStringSet(var1, var2);
    }

    public int getInt(String var1, int var2) {
        return sInstance.mSharedPreferences.getInt(var1, var2);
    }

    public long getLong(String var1, long var2) {
        return sInstance.mSharedPreferences.getLong(var1, var2);
    }

    public float getFloat(String var1, float var2) {
        return sInstance.mSharedPreferences.getFloat(var1, var2);
    }

    public boolean getBoolean(String var1, boolean var2) {
        return sInstance.mSharedPreferences.getBoolean(var1, var2);
    }

    public boolean contains(String var1) {
        return sInstance.mSharedPreferences.contains(var1);
    }


}
