package com.lyloou.lou.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcelable;

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
     *
     * 功能：添加快捷图标到桌面
     * <p/>
     * <p/>
     * 注意：需要权限支持：
     * {@code
     * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
     * }
     */
    public static void addShortcutToLauncher(Context context, String shortcutName, int iconId, Class targetClass){
        Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 添加快捷图标的名称
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);

        // 是否允许图标重复
        addShortcutIntent.putExtra("duplicate", false);

        // 添加快捷图标的icon
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconId);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        // 添加启动的程序
        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.setClassName(context.getPackageName(), targetClass.getName());
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);

        // 发送广播添加图标
        context.sendBroadcast(addShortcutIntent);
    }
}
