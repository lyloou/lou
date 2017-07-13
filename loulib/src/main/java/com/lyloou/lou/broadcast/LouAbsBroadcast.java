package com.lyloou.lou.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public abstract class LouAbsBroadcast {

    private LocalBroadcastManager mLocalBroadcastManager;

    public LouAbsBroadcast(Context context) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    protected abstract IntentFilter makeFilter();

    protected abstract void onReceive(Context context, Intent intent);

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LouAbsBroadcast.this.onReceive(context, intent);
        }
    };

    public void regist() {
        mLocalBroadcastManager.registerReceiver(mReceiver, makeFilter());
    }

    public void unregist() {
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    // -----------------帮助方法；
    // 发送广播；
    public static void send(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    // 发送广播；
    public static void send(Context context, String action) {
        Intent intent = new Intent(action);
        send(context, intent);
    }

}