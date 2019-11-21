package com.lyloou.test.util.dialog;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lyloou.test.R;
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


    @SuppressLint("InflateParams")
    public static void showInputDialog(Context context, Content content, Consumer<String> consumer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_et, null, false);
        EditText editText = v.findViewById(R.id.et_content);

        if (!TextUtils.isEmpty(content.getHint())) {
            editText.setHint(content.getHint());
        }
        if (!TextUtils.isEmpty(content.getDefaultContext())) {
            editText.setText(content.getDefaultContext());
        }
        if (!TextUtils.isEmpty(content.getTitle())) {
            builder.setTitle(content.getTitle());
        }
        builder.setView(v);
        builder.setPositiveButton("确定", (dialog, whichButton) -> {
            String url = editText.getText().toString();
            consumer.accept(url);
        });
        builder.setNegativeButton("取消", (dialog, whichButton) -> consumer.accept(""));
        builder.show();

        if (content.isFocus()) {
            editText.setFocusable(true);
            editText.requestFocus();
        }
    }

    public static void showTimePicker(Context context, TimePickerDialog.OnTimeSetListener listener, int[] time) {
        if (time == null || time.length != 2) {
            Toast.makeText(context, "参数异常", Toast.LENGTH_LONG).show();
            return;
        }
        new TimePickerDialog(context, 0, listener, time[0], time[1], true).show();
    }

    public static void showHttpAuthRequest(WebView view, HttpAuthHandler handler) {
        Context context = view.getContext();
        // [Android-WebView's onReceivedHttpAuthRequest() not called again - Stack Overflow](https://stackoverflow.com/questions/20399339/android-webviews-onreceivedhttpauthrequest-not-called-again)
        final EditText usernameInput = new EditText(context);
        usernameInput.setHint("Username");

        final EditText passwordInput = new EditText(context);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(usernameInput);
        ll.addView(passwordInput);

        new AlertDialog
                .Builder(context)
                .setTitle("Authentication")
                .setView(ll)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    String username = usernameInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    handler.proceed(username, password);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    dialog.dismiss();
                    view.stopLoading();
                })
                .show();
    }
}
