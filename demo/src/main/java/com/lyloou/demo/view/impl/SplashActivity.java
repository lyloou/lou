/*
 * Copyright  (c) 2016. Lou Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.demo.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lyloou.demo.R;
import com.lyloou.demo.presenter.SplashPresenter;
import com.lyloou.demo.view.ISplashView;
import com.lyloou.lou.activity.LouActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.17 19:38
 * <p>
 * Description:
 */
public class SplashActivity extends Activity implements ISplashView {
    @BindView(R.id.textView)
    TextView mTextView;
    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new SplashPresenter();
        mPresenter.setView(this);
        showProgress();

        mTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.didFinishLoading();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showProgress() {
        mTextView.setText("显示中...");
    }

    @Override
    public void hideProgress() {
        mTextView.setText("已经隐藏...");
    }

    @Override
    public void showNoInetErrorMsg() {
        mTextView.setText("无网络");
    }

    @Override
    public void moveToMainView() {
        startActivity(new Intent(this, MainActivity.class));
    }


}
