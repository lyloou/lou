package com.lou.as.lou.view.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;

import com.lou.as.lou.R;
import com.lyloou.lou.dialog.LouDialogAtBottom;
import com.lyloou.lou.dialog.LouDialogProgressTips;
import com.lyloou.lou.dialog.LouDialogToast;
import com.lyloou.lou.util.Uview;

public class DialogActivity extends AppCompatActivity {

    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        Uview.clickEffectByNoEffect(v -> {
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
                },
                findViewById(R.id.btn_show_progressDialog),
                findViewById(R.id.btn_show_toastDialog),
                findViewById(R.id.btn_show_LouDialogAtBottom),
                findViewById(R.id.btn_show_LouDialogAtBottomSheet)
        );
    }

}
