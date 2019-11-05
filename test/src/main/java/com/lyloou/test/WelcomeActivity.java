package com.lyloou.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
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

    @SuppressLint("CheckResult")
    private void initView() {
        ImageView ivWelcome = findViewById(R.id.iv_welcome);
        // http://cdn.iciba.com/news/word/big_20181103b.jpg
        ivWelcome.setOnClickListener(v -> toNext());
        Glide.with(getApplicationContext())
                .load(String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", Utime.getDayWithFormatOne()))
                .into(new GlideDrawableImageViewTarget(ivWelcome) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        // https://github.com/bumptech/glide/issues/275
                        super.setResource(resource);
                        ivWelcome.setVisibility(View.VISIBLE);
                    }
                });
        handler.postDelayed(toNext, 3600);
    }

    private void toNext() {
        handler.removeCallbacks(toNext);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        overridePendingTransition(0, 0);
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
