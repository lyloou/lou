package com.lyloou.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lyloou.test.common.NetWork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            Glide.with(getApplicationContext())
                                    .load(daily.getFenxiang_img())
                                    .into(new GlideDrawableImageViewTarget(ivWelcome) {
                                        @Override
                                        protected void setResource(GlideDrawable resource) {
                                            // https://github.com/bumptech/glide/issues/275
                                            super.setResource(resource);
                                            ivWelcome.setVisibility(View.VISIBLE);
                                        }
                                    });

                            ivWelcome.postDelayed(this::toNext, 3000);
                        }
                        , Throwable::printStackTrace);
    }

    private void toNext() {
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
}
