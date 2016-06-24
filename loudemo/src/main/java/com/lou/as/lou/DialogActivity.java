package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyloou.lou.dialog.LouDialogAtBottom;
import com.lyloou.lou.dialog.LouDialogProgressTips;
import com.lyloou.lou.dialog.LouDialogToast;
import com.lyloou.lou.util.Uview;

public class DialogActivity extends AppCompatActivity {

    LouDialogProgressTips mProgressTipsDialog;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_dialog);

        mProgressTipsDialog = LouDialogProgressTips.getInstance(mContext);

        Uview.clickEffectByNoEffect(mClickListener,
                findViewById(R.id.btn_show_progressDialog),
                findViewById(R.id.btn_show_toastDialog),
                findViewById(R.id.btn_show_LouDialogAtBottom));
    }

    private android.view.View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_show_progressDialog:
                    mProgressTipsDialog.show("扫描中...");
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressTipsDialog.dismiss();
                        }
                    }, 2000);

                    break;
                case R.id.btn_show_toastDialog:
                    LouDialogToast.show(mContext, "欢迎您！");
                    break;
                case R.id.btn_show_LouDialogAtBottom:
                    LouDialogAtBottom louDialogAtBottom = LouDialogAtBottom
                            .newInstance(mContext, R.layout.activity_dialog);
                    louDialogAtBottom.draggable(true);
                    louDialogAtBottom.show();
                    break;
            }
        }
    };

}
