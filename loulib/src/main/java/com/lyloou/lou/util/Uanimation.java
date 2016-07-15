package com.lyloou.lou.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class Uanimation {
    public static Animation getRotateAnimation(int duration) {
        AnimationSet set = new AnimationSet(true);

        Animation anim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);

        set.setInterpolator(new LinearInterpolator());
        set.setRepeatMode(Animation.RESTART);
        set.setRepeatCount(Animation.INFINITE);
        set.addAnimation(anim);
        return set;
    }

    public static Animation getScaleAnimation(int duration, float from, float to) {
        AnimationSet set = new AnimationSet(true);
        Animation anim = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);

        set.setInterpolator(new LinearInterpolator());
        set.setDuration(duration);
        set.addAnimation(anim);
        set.setFillAfter(true);
        return set;
    }

    public static Animation getAlphaAnimation(int duration, float from, float to) {
        Animation anim = new AlphaAnimation(from, to);
        anim.setDuration(duration);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }
}