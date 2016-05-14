package com.lyloou.lou.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyloou.lou.util.Uscreen;

/**
 * Created by admin on 2016/4/27.
 */
public class LouDialogToast {
    private static final LinearLayout.LayoutParams LAYOUT_PARAM_WRAP_CONTENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    private static LouDialogToast INSTANCE;
    private static final java.lang.Runnable sDismissCallback = new Runnable() {
        @Override
        public void run() {
            INSTANCE.hide();
        }
    };
    private static Handler sHandler;
    private TextView mTvTips;
    private LouDialog sProgressDialog;
    private Activity mContext;

    private LouDialogToast(Activity context) {
        sHandler = new Handler(Looper.myLooper());

        int MARGIN_16DP = Uscreen.dp2Px(context, 16);
        int MARGIN_32DP = Uscreen.dp2Px(context, 32);

        mContext = context;

        // 代码设置布局；
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(LAYOUT_PARAM_WRAP_CONTENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(MARGIN_32DP, MARGIN_16DP, MARGIN_32DP, MARGIN_16DP);

        mTvTips = new TextView(context);
        mTvTips.setTextColor(Color.WHITE);
        mTvTips.setTextSize(16);
        layout.addView(mTvTips, LAYOUT_PARAM_WRAP_CONTENT);

        sProgressDialog = LouDialog.newInstance(context, layout, android.R.style.Theme_Holo_Dialog_NoActionBar)
                .setWindowAnimation(android.R.style.Animation_Toast)
                .setPositionAndAlpha(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, MARGIN_32DP, 0.8f)
                .setDimAmount(0.0f).setCancelable(true);
    }

    private static void init(final Activity context) {
        if (context == null) {
            throw new NullPointerException("The context can not be null");
        }
        if (INSTANCE == null || INSTANCE.mContext.isDestroyed()) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    INSTANCE = new LouDialogToast(context);
                }
            });
        }
    }

    public static void show(Activity context, String tips) {
        init(context);
        INSTANCE.setTips(tips);
        INSTANCE.show();
        sHandler.removeCallbacks(sDismissCallback);
        sHandler.postDelayed(sDismissCallback, 3000);
    }

    public static void show(Activity context, int strId) {
        show(context, context.getResources().getString(strId));
    }

    public static void dismiss() {
        if (INSTANCE != null) {
            INSTANCE.hide();
        }
    }

    private void setTips(final String tips) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTips.setText(tips);
            }
        });
    }

    private void show() {
        if (!sProgressDialog.isShowing()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sProgressDialog.show();
                }
            });
        }
    }

    private void hide() {
        if (sProgressDialog != null && sProgressDialog.isShowing()) {
            sProgressDialog.dismiss();
        }
    }
}
