package com.lyloou.test.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.lyloou.test.common.Consumer;

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

    public static void showTimePicker(Context context, TimePickerDialog.OnTimeSetListener listener, int[] time) {
        if (time == null || time.length != 2) {
            Toast.makeText(context, "程序异常", Toast.LENGTH_LONG).show();
            return;
        }
        new TimePickerDialog(context, 0, listener, time[0], time[1], true).show();
    }
}
