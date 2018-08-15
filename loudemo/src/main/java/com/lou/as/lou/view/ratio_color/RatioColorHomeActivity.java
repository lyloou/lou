package com.lou.as.lou.view.ratio_color;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Usp;

import static com.lou.as.lou.view.ratio_color.Keys.KEY_SKIN;

public class RatioColorHomeActivity extends LouActivity {

    private Activity mContext;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        transparent();

        initView();
        setContentView(mBtn);
    }

    private void transparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        mBtn = new Button(mContext);
        mBtn.setText("点我改变背景色去囖");
        mBtn.setOnClickListener(v -> startActivity(new Intent(mContext, RatioColorActivity.class)));
    }


    @Override
    protected void onStart() {
        super.onStart();
        mBtn.setBackgroundColor(Usp.init(this).getInt(KEY_SKIN, Color.LTGRAY));
    }

}
