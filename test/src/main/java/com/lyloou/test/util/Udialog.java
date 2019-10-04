package com.lyloou.test.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.lyloou.test.flow.Consumer;

public class Udialog {
    public static void alert(Context context, String message, Consumer<Boolean> consumer) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("是的", (dialog, which) -> {
                    consumer.accept(true);
                })
                .setNegativeButton("再想想", (dialog, which) -> {
                    consumer.accept(false);
                })
                .setCancelable(true)
                .create()
                .show();
    }
}
