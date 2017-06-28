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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.28 12:09
 * <p>
 * Description:
 */
public final class Uactivity {

    public static void start(Context context, Class clazz) {
        context = context.getApplicationContext();
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static List<Class> getActivitiesFromManifest(Context context, String packageName) {
        List<Class> classes = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activities = packageInfo.activities;
            if (activities != null) {
                for (ActivityInfo info : activities) {
                    Class<?> clazz = Class.forName(info.name);
                    if (Activity.class.isAssignableFrom(clazz)) {
                        classes.add(clazz);
                    }
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // 获取一个有序（按照label排序）的map：<label, class>
    public static Map<String, Class> getActivitiesMapFromManifest(Context context, String packageName) {
        Map<String, Class> classMap = new TreeMap<>();
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activities = packageInfo.activities;
            if (activities != null) {
                for (ActivityInfo info : activities) {
                    Class<?> clazz = Class.forName(info.name);
                    if (Activity.class.isAssignableFrom(clazz)) {
                        int labelRes = info.labelRes;
                        if (labelRes == 0) {
                            continue;
                        }
                        String label = context.getResources().getString(labelRes);
                        classMap.put(label, clazz);
                    }
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classMap;
    }
}
