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

package com.lyloou.sdkdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private Activity mContext;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        showDialog(this, event.message);
        System.out.println("===============>1" + event.message + " " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 2)
    public void onMessageEventBG(MessageEvent event) {
        System.out.println("===============>2" + event.message + " " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 2)
    public void onMessageEventBG2(MessageEvent event) {
        System.out.println("===============>3" + event.message + " " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onMessageEventPosting(MessageEvent event) {
        System.out.println("===============>0" + event.message + " " + Thread.currentThread().getName());
        EventBus.getDefault().cancelEventDelivery(event);
    }

    public void onClick(View view) {
        EventBus.getDefault().post(new MessageEvent("Loading..."));
    }

    private void showDialog(final Context ctx, final String tips) {
        if (ctx instanceof Activity) {
            final Activity context = (Activity) ctx;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int spacing = Uscreen.dp2Px(context, 16);
                    int margin = Uscreen.dp2Px(context, 6);
                    float fontSize = Uscreen.sp2Px(context, 12);
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(layoutParams);
                    layout.setGravity(Gravity.CENTER);
                    layout.setPadding(spacing, spacing, spacing, spacing);
                    // add ProgressBar
                    ProgressBar bar = new ProgressBar(context);
                    layout.addView(bar, layoutParams);

                    // add Space
                    Space space = new Space(context);
                    LinearLayout.LayoutParams spaceMargin = new LinearLayout.LayoutParams(margin, margin);
                    layout.addView(space, spaceMargin);

                    // add TextView
                    TextView tvTips = new TextView(context);
                    tvTips.setTextColor(Color.GRAY);
                    tvTips.setTextSize(fontSize);
                    tvTips.setText(tips);
                    tvTips.setPadding(spacing, 0, spacing, 0);
                    layout.addView(tvTips, layoutParams);

                    // 黑色主题：android.R.style.Theme_Holo_Dialog_NoActionBar
                    dialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                    dialog.setCancelable(true);
                    dialog.getWindow().setDimAmount(0.3f);
                    dialog.setContentView(layout);
                    dialog.show();
                }
            });
        }

    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static class MessageEvent {
        public final String message;

        public MessageEvent(String message) {
            this.message = message;
        }
    }

}
