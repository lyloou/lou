package com.lyloou.lou.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Utoast {
    private static Toast mToast;

    public static void show(Context context, CharSequence text) {
        show(context, text, 0);
    }

    public static void show(Context context, int resId) {
        show(context, resId, 0);
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, CharSequence text, int mode) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, mode);
        } else {
            mToast.setText(text);
            mToast.setDuration(mode);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, int resId, int mode) {

        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), resId, mode);
        } else {
            mToast.setText(resId);
            mToast.setDuration(mode);
        }

        mToast.show();
    }

    public static void show(final Activity context, final String str) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Utoast.show(context, str);
            }
        });
    }

    public static void show(final Activity context, final int strId) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Utoast.show(context, strId);
            }
        });
    }

    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
