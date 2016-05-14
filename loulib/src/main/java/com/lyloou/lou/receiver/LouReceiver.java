package com.lyloou.lou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class LouReceiver {

	LocalBroadcastManager mLocalBroadcastManager;
	TtcReceiverListener mListener;

	public LouReceiver(Context context, TtcReceiverListener listener) {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
		mListener = listener;
	}

	public interface TtcReceiverListener {

		IntentFilter makeFilter();

		void onReceive(Context context, Intent intent);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mListener.onReceive(context, intent);
		}
	};

	public void regist() {
		mLocalBroadcastManager.registerReceiver(mReceiver, mListener.makeFilter());
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