package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyloou.lou.dialog.LouDialogProgressTips;
import com.lyloou.lou.dialog.LouDialogToast;
import com.lyloou.lou.util.Ulog;
import com.lyloou.lou.util.Uview;

public class MainActivity extends AppCompatActivity {

    LouDialogProgressTips mProgressTipsDialog;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);

        mProgressTipsDialog = LouDialogProgressTips.getInstance(mContext);
        Uview.clickEffectByNoEffect(mClickListener,
                findViewById(R.id.btn_show_progressDialog),
                findViewById(R.id.btn_show_toastDialog));
    }


    private void isMain(String tips) {
        Ulog.e(tips + " :" + "---->" + (Looper.myLooper() == Looper.getMainLooper()));
    }


    private android.view.View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_show_progressDialog:
                    isMain("click");
                    mProgressTipsDialog.show("扫描中...");
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isMain("vv1");
                            mProgressTipsDialog.show("改变了...");
                        }
                    }, 2000);

                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    isMain("vv2");
                                    mProgressTipsDialog.show("再变一次...");
                                }
                            }).start();

                        }
                    }, 4000);

                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    isMain("vv3");
                                    mProgressTipsDialog.dismiss();
                                }
                            }).start();
                        }
                    }, 6000);

                    break;
                case R.id.btn_show_toastDialog:

                    LouDialogToast.show(mContext, "SHow Me1");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            LouDialogToast.show(mContext, "SHow Me2");
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            LouDialogToast.show(mContext, R.string.app_name);
                        }
                    }).start();

                    break;
            }
        }
    };

}
