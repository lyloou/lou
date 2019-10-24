/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

/**
 * 类描述：SharedPreference 工具类；
 * 装饰者模式
 */
public final class Usp implements SharedPreferences.Editor, SharedPreferences {
    private static Usp sInstance;
    private static SharedPreferences.Editor sEditor;

    private final SharedPreferences mSharedPreferences;

    private Usp(Context context, String spName) {
        if (TextUtils.isEmpty(spName)) {
            // 默认的sp名称: 包名_SP
            spName = context.getApplicationContext().getPackageName() + "_SP";
        }
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 一个单例的实例
     *
     * @param context 上下文
     * @param name    sp名称
     * @return Usp 实例
     */
    @SuppressLint("CommitPrefEdits")
    public static Usp init(Context context, String name) {
        if (sInstance == null) {
            synchronized (Usp.class) {
                if (sInstance == null) {
                    sInstance = new Usp(context, name);
                }
            }
        }

        if (sEditor == null)
            sEditor = sInstance.mSharedPreferences.edit();
        return sInstance;
    }

    public static Usp init(Context context) {
        return init(context, null);
    }

    @Override
    public boolean commit() {
        return sEditor.commit();
    }

    @Override
    public void apply() {
        sEditor.apply();
    }

    @Override
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

    @Override
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    @Override
    public String getString(String var1, String var2) {
        return mSharedPreferences.getString(var1, var2);
    }

    @Override
    public Set<String> getStringSet(String var1, Set<String> var2) {
        return mSharedPreferences.getStringSet(var1, var2);
    }

    @Override
    public int getInt(String var1, int var2) {
        return mSharedPreferences.getInt(var1, var2);
    }

    @Override
    public long getLong(String var1, long var2) {
        return mSharedPreferences.getLong(var1, var2);
    }

    @Override
    public float getFloat(String var1, float var2) {
        return mSharedPreferences.getFloat(var1, var2);
    }

    @Override
    public boolean getBoolean(String var1, boolean var2) {
        return mSharedPreferences.getBoolean(var1, var2);
    }

    @Override
    public boolean contains(String var1) {
        return mSharedPreferences.contains(var1);
    }

    @Override
    public Editor edit() {
        return sEditor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }


}
