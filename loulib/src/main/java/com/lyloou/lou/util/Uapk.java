package com.lyloou.lou.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.lyloou.lou.activity.LouActivity;

public class Uapk {

    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static boolean isDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDebugable(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ApplicationInfo info = packageInfo.applicationInfo;
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 功能：添加快捷图标到桌面
     * <p/>
     * <p/>
     * 注意：需要权限支持：
     * {@code
     * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
     * }
     *
     * @param context
     * @param shortcutName
     * @param iconId
     * @param clazz        要执行的Activity需要指定export属性,
     *                     否则点击图标没有反应
     *                     {@code android:exported="true"},
     *                     clazz要和context属于同一个packageName.
     */
    public static void addActivityShortcut(Context context,
                                           String shortcutName,
                                           int iconId,
                                           Class clazz) {
        Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        addShortcutIntent.putExtra("duplicate", false);  // 是否允许图标重复
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);  // 添加快捷图标的名称
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconId));// 添加快捷图标的icon

        Intent launchIntent = new Intent();
        launchIntent.setClassName(context.getPackageName(), clazz.getName());
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);  // 添加启动的程序

        context.sendBroadcast(addShortcutIntent);        // 发送广播添加图标
    }
}

