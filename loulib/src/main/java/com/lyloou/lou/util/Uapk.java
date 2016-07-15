package com.lyloou.lou.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.List;

public class Uapk {

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isRunOnForeground(Context context) {
        // Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

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
     * 功能：给Activity添加快捷图标到桌面
     * <p>
     * <p>
     * 注意：需要权限支持：
     * {@code
     * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
     * }
     *
     * @param context
     * @param shortcutName
     * @param iconId
     * @param clazz        被添加快捷方式的Activity需要在Manifest中指定export属性, 否则点击图标没有反应
     *                     {@code android:exported="true"}, clazz要和context属于同一个packageName.
     *                     <p>
     *                     android:exported
     *                     这个属性用于设置该Activity能否由另一个应用程序的组件来启动，如果设置为true，则可以启动，否则不能启动。如果设置为false，那么该Activity只能被同一个应用程序中的组件或带有相同用户ID的应用程序来启动。
     *                     它的默认值要依赖与该Activity是否包含了Intent过滤器。如果没有任何过滤器，则意味着该Activity只能通过明确的类名来调用，这样就暗示者该Activity只能在应用程序内部使用（因为其他用户不会知道它的类名），因此在这种情况下，默认值是false。在另一方面，至少存在一个过滤器，则暗示着该Activity可被外部使用，因此默认值是true。
     *                     这个属性不是限制Activity暴露给其他应用程序的唯一方法。还可以使用权限来限制外部实体对该Activity的调用。
     *                     <p>
     *                     http://blog.csdn.net/mark0614/article/details/8704934
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

