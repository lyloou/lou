package com.lou.as.lou.view.ratio_color;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lou.as.lou.SharedPreferencesUtil;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.view.RatioColor;

public class RatioColorActivity extends LouActivity {
    private Activity mContext;
    private RatioColor mRatioColor;

    private SharedPreferencesUtil mSpu;
    private static final int[] COLORS = new int[]{
            Color.parseColor("#990000"),
            Color.parseColor("#009900"),
            Color.parseColor("#000099"),
            Color.parseColor("#009999"),
            Color.parseColor("#990099"),
            Color.parseColor("#999900"),
            Color.parseColor("#999999"),
            Color.LTGRAY,
            Color.RED,
            Color.CYAN,
            Color.DKGRAY,
            Color.YELLOW,
            Color.GREEN,
            Color.BLACK,
            Color.WHITE,
            Color.LTGRAY,
            Color.RED,
            Color.CYAN,
            Color.DKGRAY,
            Color.YELLOW,
            Color.GREEN,
            Color.BLACK,
            Color.WHITE,
            Color.LTGRAY,
            Color.RED,
            Color.CYAN,
            Color.DKGRAY,
            Color.YELLOW,
            Color.GREEN,
            Color.BLACK,
            Color.WHITE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initData();
        initView();
    }


    private void initData() {
        mSpu = SharedPreferencesUtil.getInstance(mContext);
    }


    private void initView() {

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final int PADDING = Uscreen.dp2Px(mContext, 16);
        layout.setPadding(PADDING, PADDING * 2, PADDING, PADDING);
        setContentView(layout);

        mRatioColor = new RatioColor(mContext);
        mRatioColor.addItems(COLORS);
        mRatioColor.setOnCheckedColorListener(new RatioColor.onCheckedColorListener() {
            @Override
            public void doColor(int color) {
                setCurrentBgColor(color);
            }
        });

        layout.addView(mRatioColor);

        // 设置当前背景色
        setCurrentBgColor(mSpu.getSkin());
    }


    public void setCurrentBgColor(int color) {
        getWindow().getDecorView().setBackgroundColor(color);
        mRatioColor.setCheckedColor(color);
        if (mSpu.getSkin() != color) {
            mSpu.saveSkin(color);
        }
    }
}
