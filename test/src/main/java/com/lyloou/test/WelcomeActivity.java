package com.lyloou.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lyloou.test.util.Utime;

/**
 * Created by Administrator on 2016.10.18.
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        ImageView ivWelcome = findViewById(R.id.iv_welcome);
        ivWelcome.setOnClickListener(v -> toNext());
        Glide.with(getApplicationContext())
                .load(String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", Utime.getDayWithFormatOne()))
                .into(ivWelcome);
        handler.postDelayed(toNext, 3600);
    }

    private void toNext() {
        handler.removeCallbacks(toNext);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    // 欢迎页面禁止按键点击
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    Handler handler = new Handler();
    Runnable toNext = this::toNext;
}
