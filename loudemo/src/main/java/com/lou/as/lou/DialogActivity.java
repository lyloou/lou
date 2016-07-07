package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyloou.lou.dialog.LouDialogAtBottom;
import com.lyloou.lou.dialog.LouDialogProgressTips;
import com.lyloou.lou.dialog.LouDialogToast;
import com.lyloou.lou.util.Ulog;
import com.lyloou.lou.util.Usp;
import com.lyloou.lou.util.Uview;

public class DialogActivity extends AppCompatActivity {

    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_dialog);

        // Test Ulog.java
        Ulog.d("Hello World");
        Ulog.e("e:Hello World");
        Ulog.i("i: Hello World");
        Ulog.w("w: Hello World");
        Ulog.wtf("wtf: Hello World");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Ulog.w("w: wwwwwwwwwwwwww jjj");
            }
        }).start();

        // Test Dialog
        Uview.clickEffectByNoEffect(mClickListener,
                findViewById(R.id.btn_show_progressDialog),
                findViewById(R.id.btn_show_toastDialog),
                findViewById(R.id.btn_show_LouDialogAtBottom),
                findViewById(R.id.btn_show_LouDialogAtBottomSheet)
        );

        //: Test Usp.java
        String KEY_FIRST = "FIRST";
        Ulog.d("Before First:" + Usp.init(this).getInt(KEY_FIRST, 22));
        Usp.init(this).putInt(KEY_FIRST, 101).commit();
        Ulog.d("After First:" + Usp.init(this).getInt(KEY_FIRST, 23));


    }

    private android.view.View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_show_progressDialog:
                    // 使用自定义的ProgressBar
                    LouDialogProgressTips.getInstance(mContext).show("扫描中...");
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LouDialogProgressTips.dismiss();
                        }
                    }, 2000);

                    break;
                case R.id.btn_show_toastDialog:
                    // 使用自定义的Toast
                    LouDialogToast.show(mContext, "欢迎您！");
                    break;
                case R.id.btn_show_LouDialogAtBottom:
                    // 使用自定义的自底而上的Dialog
                    LouDialogAtBottom
                            .newInstance(mContext, R.layout.activity_dialog).draggable(true).show();
                    break;
                case R.id.btn_show_LouDialogAtBottomSheet:
                    // 使用支持库的自底而上的Dialog
                    BottomSheetDialog bsd = new BottomSheetDialog(mContext);
                    bsd.setContentView(R.layout.activity_dialog);
                    bsd.show();
                    break;
            }
        }
    };

}
