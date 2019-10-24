package com.lyloou.test.bus.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.lyloou.test.MainActivity;
import com.lyloou.test.R;
import com.lyloou.test.util.Usp;

import java.util.Calendar;

public class LongRunningService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundCompact();
    }

    private void startForegroundCompact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.lyloou.test";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(TaskStackBuilder.create(this)
                        .addNextIntent(new Intent(this, MainActivity.class))
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Always in your side")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        setSpecifyTimeAlarm();
//        setIntervalAlarm();

        return super.onStartCommand(intent, flags, startId);
    }

    private void setIntervalAlarm() {
        Context context = getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + interval;
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }

    private void setSpecifyTimeAlarm() {
        Context context = getApplicationContext();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Usp.init(context).getInt(NotificationConstant.KEY_ALARM_CLOCK_BUS_HOUR_OF_DAY, 8));
        calendar.set(Calendar.MINUTE, Usp.init(context).getInt(NotificationConstant.KEY_ALARM_CLOCK_BUS_MINUTE, 30));
        calendar.set(Calendar.SECOND, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
