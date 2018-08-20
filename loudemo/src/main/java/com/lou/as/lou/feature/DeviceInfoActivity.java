package com.lou.as.lou.feature;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uapp;

public class DeviceInfoActivity extends LouActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        TextView tvDevice = new TextView(this);
        tvDevice.setTextSize(14);
        tvDevice.setTextColor(Color.DKGRAY);
        tvDevice.setText(Uapp.collectDeviceInfo(this));
        scrollView.addView(tvDevice);
        setContentView(scrollView);
    }
}
