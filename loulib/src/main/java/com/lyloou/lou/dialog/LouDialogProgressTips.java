package com.lyloou.lou.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.lyloou.lou.util.Uscreen;

public class LouDialogProgressTips {

    private static LouDialogProgressTips sLouDialogProgressTips;
    private static Activity sContext;

    private final LouDialog mProgressDialog;
    private static final LinearLayout.LayoutParams LAYOUT_PARAM_WRAP_CONTENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    private final TextView mTvTips;

    private LouDialogProgressTips(Activity context) {
        sContext = context;
        int PADDING = Uscreen.dp2Px(context, 16);
        int MARGIN_6DP = Uscreen.dp2Px(context, 6);
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(LAYOUT_PARAM_WRAP_CONTENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(PADDING, PADDING, PADDING, PADDING);

        Space space = new Space(context);
        LinearLayout.LayoutParams SPACE_MARGIN = new LinearLayout.LayoutParams(MARGIN_6DP, MARGIN_6DP);
        layout.addView(space, SPACE_MARGIN);

        // add ProgressBar
        ProgressBar bar = new ProgressBar(context);
        layout.addView(bar, LAYOUT_PARAM_WRAP_CONTENT);

        space = new Space(context);
        layout.addView(space, SPACE_MARGIN);

        // add TextView
        mTvTips = new TextView(context);
        mTvTips.setTextColor(Color.WHITE);
        mTvTips.setTextSize(16);
        layout.addView(mTvTips, LAYOUT_PARAM_WRAP_CONTENT);

        mProgressDialog = LouDialog.newInstance(context, layout, android.R.style.Theme_Holo_Dialog_NoActionBar)
                .setDimAmount(0.3f).setCancelable(false);
    }

    // 创建 Dialog 需要在主线程中运行；
    @MainThread
    public static LouDialogProgressTips getInstance(Activity context) {
        if(context == null){
            throw new NullPointerException("The context can't be null");
        }

        if (sContext == context && sLouDialogProgressTips != null) {
            return sLouDialogProgressTips;
        }
        return sLouDialogProgressTips = new LouDialogProgressTips(context);
    }


    public void show(final String tips) {
        if (sContext != null && mProgressDialog != null) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            sContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTips(tips);
                }
            });
        }
    }

    public void dismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // 更改视图需要在主线程中运行
    @MainThread
    private void setTips(String tips) {
        if (mProgressDialog != null) {
            if (mTvTips != null)
                mTvTips.setText(tips);
        }
    }
}
