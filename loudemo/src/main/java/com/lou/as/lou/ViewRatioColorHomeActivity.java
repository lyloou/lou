package com.lou.as.lou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lyloou.lou.activity.LouActivity;

public class ViewRatioColorHomeActivity extends LouActivity {

    private Activity mContext;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        mBtn = new Button(mContext);
        mBtn.setText("点我改变背景色去囖");
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ViewRatioColorActivity.class));
            }
        });
        setContentView(mBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBtn.setBackgroundColor(SharedPreferencesUtil.getInstance(mContext).getSkin());
    }

}
