package com.lyloou.test.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.lyloou.test.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Unotification {
    public static void show(Context context, String title, String message, Class cls) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String channelId = "channel-id";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.lyloou)//R.mipmap.ic_launcher
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent nextIntent = new Intent(context, cls);
        Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (mainIntent != null) {
            mainIntent.setPackage(null); // 加上这句代码
            stackBuilder.addNextIntent(mainIntent);
        }
        stackBuilder.addNextIntent(nextIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);

//        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(new Date()));
        int id = (title + message).hashCode();
        if (isAlreadyShow(context, id)) {
            return;
        }
        notificationManager.notify(id, mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isAlreadyShow(Context context, int notificationId) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == notificationId) {
                return true;
            }
        }
        return false;
    }
}
