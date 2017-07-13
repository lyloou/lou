package com.lyloou.lou.util;

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

    public static void show(Context context, CharSequence text, int mode) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, mode);
        } else {
            mToast.setText(text);
            mToast.setDuration(mode);
        }
        mToast.show();
    }

    public static void show(Context context, int resId, int mode) {

        if (mToast == null) {
            mToast = Toast.makeText(context, resId, mode);
        } else {
            mToast.setText(resId);
            mToast.setDuration(mode);
        }

        mToast.show();
    }

    public static void toastOnMain(final Activity context, final String str) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Utoast.show(context, str);
            }
        });
    }

    public static void toastOnMain(final Activity context, final int strId) {
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
