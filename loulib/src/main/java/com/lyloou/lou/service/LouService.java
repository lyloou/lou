package com.lyloou.lou.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

public class LouService extends Service {

	private final IBinder mBind = new TtcServiceBind();

	public class TtcServiceBind extends Binder {
		public LouService getService() {
			return LouService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBind;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}

	public static void bind(Context context, ServiceConnection conn) {
		context.bindService(new Intent(context, LouService.class), conn, Context.BIND_AUTO_CREATE);
	}

	public static void unBind(Context context, ServiceConnection conn) {
		context.unbindService(conn);
	}

}
