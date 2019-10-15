package com.lyloou.test.bus.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lyloou.test.bus.BusTvActivity;
import com.lyloou.test.util.Unotification;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Unotification.show(context, "帅帅的人", "去上班咯", BusTvActivity.class);
    }
}