package com.lyloou.lou.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;

/**
 * 类描述：
 * 创建人： Lou
 * 创建时间： 2016/6/23 9:41
 * 修改人：Lou
 * 修改时间：2016/6/23 9:41
 * 修改备注：
 */
public class LouDialogAtBottom {
    final LouDialog mLouDialog;

    public static LouDialogAtBottom newInstance(Context context, int layoutId) {
        return new LouDialogAtBottom(context, layoutId);
    }

    private LouDialogAtBottom(Context context, int layoutId) {
        mLouDialog = LouDialog.newInstance(context, layoutId,
                R.style.lou_dialog_theme_bottom);
        mLouDialog.setWindowAnimation(R.style.lou_anim_bottom)
                .setWH(Uscreen.getScreenWidth(context), -1)
                .setPositionAndAlpha(Gravity.BOTTOM, 0, 0, 1f)
                .setDimAmount(0.6f)
                .setCancelable(true);

    }

    private static final String TAG = "LouDialogAtBottom";

    private int mDownY = 0;

    public void draggable(boolean dragable) {
        if (!dragable) return;

        final View layoutView = mLouDialog.getLayoutView();
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) layoutView.getLayoutParams();

        layoutView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaY = (int) (event.getRawY() - mDownY);
                        if (deltaY < 0) deltaY = 0;
                        layoutParams.bottomMargin = -deltaY;
                        layoutView.setLayoutParams(layoutParams);
                        layoutView.requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        doUp(layoutView);
                        break;
                }
                return true;
            }

            // 松开后逐渐恢复
            private void doUp(final View layoutView) {
                final int MAX_H = layoutView.getHeight();
                final int MIN_H = 0;
                final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) layoutView.getLayoutParams();
                int start = layoutParams.bottomMargin;
                int end = MIN_H;

                if (Math.abs(start) > MAX_H / 2) {
                    end = -MAX_H;
                }

                ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end).setDuration(160);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int currentMargin = (int) animation.getAnimatedValue();

                        layoutParams.bottomMargin = currentMargin;
                        layoutView.setLayoutParams(layoutParams);
                        layoutView.requestLayout();

                        if (Math.abs(currentMargin) == MAX_H) {
                            mLouDialog.dismiss();
                        }
                    }
                });
                valueAnimator.start();
            }
        });
    }

    public <T extends View> T getView(int viewId) {
        return mLouDialog.getView(viewId);
    }

    public LouDialog getLouDialog() {
        return mLouDialog;
    }

    public void show() {
        mLouDialog.show();
    }
}
