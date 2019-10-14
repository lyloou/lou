package com.lyloou.test.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

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

    public static void showInputDialog(Context context, String defaultText, Consumer<String> consumer) {
        EditText editText = new EditText(context);
        editText.setHint(defaultText);

        new AlertDialog.Builder(context)
                .setTitle("Moustachify Link")
                .setMessage("Paste in the link of an image to moustachify!")
                .setView(editText)
                .setPositiveButton("是的", (dialog, whichButton) -> {
                    String url = editText.getText().toString();
                    consumer.accept(url);
                })
                .setNegativeButton("再想想", (dialog, whichButton) -> consumer.accept(""))
                .show();
    }
}
