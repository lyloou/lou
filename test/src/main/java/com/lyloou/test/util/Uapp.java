package com.lyloou.test.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;

import com.lyloou.test.common.Consumer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Uapp {
    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static void handlePackageIntent(Context context, String packageName, Consumer<Intent> intentConsumer) {
        PackageManager packageManager = context.getPackageManager();
        if (Uapp.checkPackInfo(context, packageName)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            intentConsumer.accept(intent);
        } else {
            intentConsumer.accept(null);
        }
    }

    // 原文链接：https://blog.csdn.net/htwhtw123/article/details/76032997
    public static void requestPermission(Activity context, PermissionListener listener) {
        String permission = listener.name();
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                new AlertDialog.Builder(context)
                        .setMessage("我们需要这些权限")
                        .setPositiveButton("可以", (dialog, which) -> {
                            ActivityCompat.requestPermissions(context,
                                    new String[]{permission}, 1);
                        })
                        .create()
                        .show();
                listener.whenShouldShowRequest().run();
            } else {
                ActivityCompat.requestPermissions(context, new String[]{permission}, 1);
            }
        } else {
            listener.whenGranted().run();
        }
    }

    /**
     * //    原文链接：https://blog.csdn.net/lyabc123456/article/details/86716857
     * 添加桌面快捷方式
     *
     * @param context
     * @param className 快捷方式的目标类(全包名的路径)
     * @param id        快捷方式对应的id
     * @param iconResId 快捷方式的图标资源
     * @param label     显示名称
     */
    public static void addShortCutCompat(Activity context, String className, String id, int iconResId, String label) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        String installShortcut = Manifest.permission.INSTALL_SHORTCUT;

        if (ContextCompat.checkSelfPermission(context, installShortcut)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String[] permissions = {installShortcut};
        ActivityCompat.requestPermissions(context, permissions, 0);

        Intent shortcutInfoIntent = new Intent();
        shortcutInfoIntent.setClassName(context, className);
        shortcutInfoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
        addShortCutCompatWithoutCheckPermission(context, shortcutInfoIntent, id, iconResId, label);
    }

    public static void addShortCutCompat(Activity context, Intent intent, String id, int iconResId, String label) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        String installShortcut = Manifest.permission.INSTALL_SHORTCUT;

        if (ContextCompat.checkSelfPermission(context, installShortcut)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String[] permissions = {installShortcut};
        ActivityCompat.requestPermissions(context, permissions, 0);
        addShortCutCompatWithoutCheckPermission(context, intent, id, iconResId, label);
    }


    /**
     * 添加桌面快捷方式
     *
     * @param context
     * @param shortcutInfoIntent 点击快捷方式时的启动目标intent
     * @param id                 快捷方式对应的id
     * @param iconResId          快捷方式的图标资源
     * @param label
     */
    public static void addShortCutCompatWithoutCheckPermission(Context context, Intent shortcutInfoIntent, String id, int iconResId, String label) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, id)
                    .setIcon(IconCompat.createWithResource(context, iconResId))
                    .setShortLabel(label)
                    .setIntent(shortcutInfoIntent)
                    .build();
            //这里第二个参数可以传一个回调，用来接收当快捷方式被创建时的响应
            ShortcutManagerCompat.requestPinShortcut(context, info, null);
        }
    }

    /**
     * 删除快捷方式
     *
     * @param context
     * @param shortCutTitle 快捷方式的名称
     * @param className     快捷方式的目标类(全包名的路径)
     */
    public static void deleteShortCut(Context context, String shortCutTitle, String className) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setComponent(new ComponentName(context.getPackageName(), className));
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", shortCutTitle);
        intent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        intent.putExtra("duplicate", true);
        context.sendBroadcast(intent);
    }


    /**
     * [一键设置 Android http proxy 的尝试 | Liu Tao](http://tao93.top/2018/11/01/%E4%B8%80%E9%94%AE%E8%AE%BE%E7%BD%AE%20Android%20http%20proxy%20%E7%9A%84%E5%B0%9D%E8%AF%95/)
     * 设置代理信息 exclList是添加不用代理的网址用的
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setHttpProxySetting(Context context, String host, int port)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = getCurrentWifiConfiguration(wifiManager);
        ProxyInfo mInfo = ProxyInfo.buildDirectProxy(host, port);
        if (config != null) {
            Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
            Class parmars = Class.forName("android.net.ProxyInfo");
            Method method = clazz.getMethod("setHttpProxy", parmars);
            method.invoke(config, mInfo);
            Object mIpConfiguration = getDeclaredFieldObject(config, "mIpConfiguration");

            setEnumField(mIpConfiguration, "STATIC", "proxySettings");
            setDeclaredFieldObject(config, "mIpConfiguration", mIpConfiguration);

            // save the settings
            wifiManager.updateNetwork(config);
            wifiManager.disconnect();
            wifiManager.reconnect();
        }

    }

    /**
     * 取消代理设置
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void unSetHttpProxy(Context context)
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException,
            NoSuchFieldException, NoSuchMethodException {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration configuration = getCurrentWifiConfiguration(wifiManager);
        ProxyInfo mInfo = ProxyInfo.buildDirectProxy(null, 0);
        if (configuration != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                configuration.setHttpProxy(mInfo);
            } else {
                Class clazz = Class.forName("android.net.wifi.WifiConfiguration");
                Class parmars = Class.forName("android.net.ProxyInfo");
                Method method = clazz.getMethod("setHttpProxy", parmars);
                method.invoke(configuration, mInfo);
                Object mIpConfiguration = getDeclaredFieldObject(configuration, "mIpConfiguration");
                setEnumField(mIpConfiguration, "NONE", "proxySettings");
                setDeclaredFieldObject(configuration, "mIpConfiguration", mIpConfiguration);
            }

            wifiManager.updateNetwork(configuration);
            wifiManager.disconnect();
            wifiManager.reconnect();
        }
    }

    public static void setEnumField(Object obj, String value, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field f = obj.getClass().getField(name);
        f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
    }

    public static Object getDeclaredFieldObject(Object obj, String name)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        Object out = f.get(obj);
        return out;
    }

    public static void setDeclaredFieldObject(Object obj, String name, Object object) {
        Field f = null;
        try {
            f = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            f.set(obj, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 获取当前的Wifi连接
    public static WifiConfiguration getCurrentWifiConfiguration(WifiManager wifiManager) {
        if (!wifiManager.isWifiEnabled())
            return null;
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int cur = wifiManager.getConnectionInfo().getNetworkId();
        // Log.d("当前wifi连接信息",wifiManager.getConnectionInfo().toString());
        for (int i = 0; i < configurationList.size(); ++i) {
            WifiConfiguration wifiConfiguration = configurationList.get(i);
            if (wifiConfiguration.networkId == cur)
                configuration = wifiConfiguration;
        }
        return configuration;
    }
}
