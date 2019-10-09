package com.lyloou.lou.dialog;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;

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
        mTvTips.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mTvTips.setTextSize(16);
        layout.addView(mTvTips, WRAP_CONTENT);

        mDialog = LouDialog.newInstance(context, layout, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
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
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvTips.setText(tips);
                }
            });
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }

    }

    public void setCancelble(boolean cancelble) {
        mDialog.setCancelable(cancelble);
    }

    public void hide() {
        mDialog.dismiss();
    }
}
