package com.lyloou.test;

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
import com.lyloou.test.kingsoftware.KingsoftwareUtil;

/**
 * Created by Administrator on 2016.10.18.
 */

public class WelcomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private Runnable mToNextRunnable = this::toNext;

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
                .load(KingsoftwareUtil.getTodayShareImage())
                .into(new GlideDrawableImageViewTarget(ivWelcome) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        // https://github.com/bumptech/glide/issues/275
                        super.setResource(resource);
                        ivWelcome.animate().alpha(1.0f).setDuration(300).start();
                        ivWelcome.setVisibility(View.VISIBLE);
                    }
                });
        mHandler.postDelayed(mToNextRunnable, 3600);
    }

    private void toNext() {
        mHandler.removeCallbacks(mToNextRunnable);
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


}
