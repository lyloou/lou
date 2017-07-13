package com.lyloou.lou.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016/8/5 16:49
 * Description: 本地广播帮助类
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2016/8/5 16:49    Lou                 1.0             1.0
 * Why & What is modified:
 */
public class Ubroadcast {

    public static interface ReceiverListener {

        void initIntentFilter(IntentFilter intentFilter);

        BroadcastReceiver getReceiver();

        Context getContext();

    }


    public static void send(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void send(Context context, String action) {
        send(context, new Intent(action));
    }

    public static void register(ReceiverListener listener) {
        if (listener == null) {
            throw new NullPointerException("ReceiverListener为null");
        }

        if (listener.getContext() == null) {
            throw new NullPointerException("context 为 null");
        }

        IntentFilter intentFilter = new IntentFilter();
        listener.initIntentFilter(intentFilter);

        LocalBroadcastManager
                .getInstance(listener.getContext())
                .registerReceiver(listener.getReceiver(), intentFilter);
    }

    public static void unregister(ReceiverListener listener) {
        if (listener == null) {
            throw new NullPointerException("ReceiverListener为null");
        }

        if (listener.getContext() == null) {
            throw new NullPointerException("context 为 null");
        }

        LocalBroadcastManager.getInstance(listener.getContext()).unregisterReceiver(listener.getReceiver());
    }
}
