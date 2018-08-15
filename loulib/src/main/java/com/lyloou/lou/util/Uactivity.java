package com.lyloou.lou.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Uactivity {

    public static void showUrlInDefaultBrowser(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void showHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static void showApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

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
    public static Map<String, Class> getActivitiesMapFromManifest(
            Context context,
            String packageName) {

        Map<String, Class> classMap = new TreeMap<>();
        try {
            PackageInfo packageInfo = context
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
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
