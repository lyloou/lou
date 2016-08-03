package com.lyloou.lou.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 类描述：
 * 创建人： Lou
 * 创建时间： 2016/7/28 11:28
 * 修改人： Lou
 * 修改时间：2016/7/28 11:28
 * 修改备注：
 */
public class Udevice {

    // 需要添加权限：    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }
}
