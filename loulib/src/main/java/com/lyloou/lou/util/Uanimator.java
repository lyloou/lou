package com.lyloou.lou.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

public class Uanimator {

    public static AnimatorSet animatorTranslationX(Object target, float toXDelta, int duration) {
        return animatorTranslation(target, toXDelta, 0, duration, 0);
    }

    public static AnimatorSet animatorTranslationY(Object target, float toYDelta, int duration) {
        return animatorTranslation(target, 0, toYDelta, duration, 0);
    }

    public static AnimatorSet animatorTranslation(Object target, float toXDelta, float toYDelta, int duration,
                                                  int delay) {

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "translationX", toXDelta);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "translationY", toYDelta);
        oa1.setDuration(duration);
        oa2.setDuration(duration);
        set.setDuration(duration);
        set.playTogether(oa1, oa2);
        set.setStartDelay(delay);
        return set;
    }


    public static AnimatorSet animatorRotate(Object target, int duration, float... degrees) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "rotation", degrees);
        oa1.setDuration(duration);
        oa1.setRepeatCount(ObjectAnimator.INFINITE);
        oa1.setRepeatMode(ObjectAnimator.RESTART);
        set.setDuration(duration);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(oa1);
        return set;
    }

    public static AnimatorSet animatorScaleIn(Object target, int duration) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 0f, 1f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 0f, 1f);
        set.setDuration(duration);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(oa1, oa2);
        return set;
    }

    public static AnimatorSet animatorScaleOut(Object target, int duration) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 1f, 0f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 1f, 0f);
        set.setDuration(duration);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(oa1, oa2);
        return set;
    }

    // 高度渐变的动画；
    public static synchronized void animHeightToView(final View v, final int start, final int end, final boolean isToShow,
                                                     long duration) {

        ValueAnimator va = ValueAnimator.ofInt(start, end);
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (Integer) animation.getAnimatedValue();
                layoutParams.height = h;
                v.setLayoutParams(layoutParams);
                v.requestLayout();
            }
        });

        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isToShow) {
                    v.setVisibility(View.VISIBLE);
                }
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isToShow) {
                    v.setVisibility(View.GONE);
                }
            }
        });
        va.setDuration(duration);
        if (isToShow) {
            // 加延迟，目的是保证之前是处于关闭状态；（例如：用户按了stop后，立刻按start，就会出问题）；
            va.setStartDelay(182);
        }
        va.start();
    }

    public static void animHeightToView(final Activity context, final View v, final boolean isToShow, final long duration) {

        if (isToShow) {
            // 显示：通过上下文获取可见度是 gone 的 view 的高度；
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            v.measure(size.x, size.y);
            int start = v.getMeasuredHeight();
            animHeightToView(v, 0, start, isToShow, duration);
        } else {
            // 隐藏：从当前高度变化到0，最后设置不可见；
            animHeightToView(v, v.getLayoutParams().height, 0, isToShow, duration);
        }
    }
}