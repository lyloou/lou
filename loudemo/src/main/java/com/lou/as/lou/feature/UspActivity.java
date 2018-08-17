package com.lou.as.lou.feature;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Usp;

public class UspActivity extends LouActivity {

    public static final String KEY_SP_LOGIN_TIMES = "KEY_SP_LOGIN_TIMES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tvDevice = new TextView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT);
        tvDevice.setGravity(Gravity.CENTER);
        tvDevice.setLayoutParams(layoutParams);
        setContentView(tvDevice);

        tvDevice.setTextSize(18);
        int loginTimes = Usp.init(this).getInt(KEY_SP_LOGIN_TIMES, 0);
        tvDevice.setText(String.format("第%s进入", loginTimes));
        Usp.init(this).putInt(KEY_SP_LOGIN_TIMES, loginTimes + 1).commit();
    }
}
