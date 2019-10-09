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

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.lyloou.test.util.Uscreen;


public class LouProgressBar {

    private Activity mContext;

    private final LouDialog mDialog;
    private static final LinearLayout.LayoutParams WRAP_CONTENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    private final TextView mTvTips;

    private LouProgressBar(Activity context) {
        mContext = context;
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
        mTvTips.setTextColor(Color.WHITE);
        mTvTips.setTextSize(16);
        layout.addView(mTvTips, WRAP_CONTENT);

        mDialog = LouDialog.newInstance(context, layout, android.R.style.Theme_Holo_Dialog_NoActionBar)
                .setDimAmount(0.3f)
                .setCancelable(false);
    }

    // 创建 Dialog 需要在主线程中运行；
    @MainThread
    public static LouProgressBar buildDialog(Activity context) {
        if (context == null) {
            throw new NullPointerException("The context can't be null");
        }

        return new LouProgressBar(context);
    }


    public void show(final String tips) {
        if (!TextUtils.isEmpty(tips)) {
            mContext.runOnUiThread(() -> mTvTips.setText(tips));
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }

    }

    public void hide() {
        mDialog.dismiss();
    }
}
