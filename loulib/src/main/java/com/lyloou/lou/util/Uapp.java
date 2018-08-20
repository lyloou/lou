package com.lyloou.lou.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class Uapp {


    private static final String TAG = "Uapp";

    /**
     * 判断是否开启通知权限
     * (https://blog.csdn.net/reglog/article/details/79863751)
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("unchecked")
    public static boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        /* Context.APP_OPS_MANAGER */
        try {
            Class appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    // 跳转APP设置界面
    public static void openAppDetailSettings(Context context) {
        //跳转设置界面
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }

        context.startActivity(intent);
    }


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

    public static String getApplicationMetaValue(Context context, String name) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
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
     * 收集设备参数信息
     * 参考资料：CrashHandler源代码
     *
     * @param context
     */
    public static String collectDeviceInfo(Context context) {
        final String SEP = "\n\t";
        StringBuilder infos = new StringBuilder("设备信息");
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos
                        .append(SEP).append("app包名:").append(packageName)
                        .append(SEP).append("app版本名:").append(versionName)
                        .append(SEP).append("app版本号:").append(versionCode)
                        .append(SEP).append("设备型号:").append(Build.MODEL)
                        .append(SEP).append("设备SDK版本:").append(Build.VERSION.SDK_INT)
                        .append(SEP).append("设备的系统版本:").append(Build.VERSION.RELEASE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.append(SEP).append(field.getName()).append(":").append(field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return infos.toString();
    }

    // 获取设备唯一标识
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        // http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 判断某一个类是否存在任务栈里面
     * https://blog.csdn.net/androidstarjack/article/details/46708243
     *
     * @return
     */
    public static boolean isInRunningTasks(Context context, Class<? extends Activity> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            if (am != null) {
                List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
                for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                    if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                        flag = true;
                        break;  //跳出循环，优化效率
                    }
                }
            }
        }
        return flag;

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

    // http://www.jianshu.com/p/2a1d052b8139
    public static boolean isAppInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    // https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/util/AppUtils.java
    public static boolean isAppInstalled2(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    public static boolean isWbInstall(Context context) {
        Intent intent = new Intent("com.sina.weibo.action.sdkidentity");
        intent.addCategory("android.intent.category.DEFAULT");
        List list = context.getPackageManager().queryIntentServices(intent, 0);
        return list != null && !list.isEmpty();
    }

    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    /**
     * [获取apk签名指纹的md5值 防止重新被打包](http://blog.csdn.net/manymore13/article/details/50717622)
     * 获取app签名md5值
     */
    public static String getSignMd5Str(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}

