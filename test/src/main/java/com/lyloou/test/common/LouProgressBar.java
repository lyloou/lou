/*
 * Copyright  (c) 2017 Lyloou
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

package com.lyloou.test.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.lyloou.test.R;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.http.Uthread;


public class LouProgressBar {

    private Context mContext;
    private String mTips;

    private TextView mTvTips;
    private LouDialog mDialog;
    private static final LinearLayout.LayoutParams WRAP_CONTENT =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

    private LouProgressBar(Context context) {
        mContext = context;
        // 创建 Dialog 需要在主线程中运行；
        Uthread.runInMainThread(() -> initView(mContext));
    }

    private void initView(Context context) {
        int PADDING = Uscreen.dp2Px(context, 16);
        int MARGIN_6DP = Uscreen.dp2Px(context, 6);
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(WRAP_CONTENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(PADDING, PADDING, PADDING, PADDING);

        Space space = new Space(context);
        LinearLayout.LayoutParams SPACE_MARGIN = new LinearLayout.LayoutParams(MARGIN_6DP, MARGIN_6DP);
        layout.addView(space, SPACE_MARGIN);

        // add ProgressBar
        ProgressBar bar = new ProgressBar(context);
        layout.addView(bar, WRAP_CONTENT);

        space = new Space(context);
        layout.addView(space, SPACE_MARGIN);

        // add TextView
        mTvTips = new TextView(context);
        mTvTips.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mTvTips.setTextSize(16);
        layout.addView(mTvTips, WRAP_CONTENT);
        mDialog = LouDialog.newInstance(mContext, layout, R.style.TransparentDialog).setCancelable(false);
    }

    public LouProgressBar tips(String tips) {
        this.mTips = tips;
        return this;
    }

    public static LouProgressBar builder(@NonNull Context context) {
        return new LouProgressBar(context);
    }


    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    public void show() {
        Uthread.runInMainThread(this::_show);
    }

    private void _show() {
        if (!TextUtils.isEmpty(mTips)) {
            mTvTips.setText(mTips);
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void hide() {
        Uthread.runInMainThread(mDialog::dismiss);
    }
}
