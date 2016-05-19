package com.lou.as.lou;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import com.lyloou.lou.view.RatioColor;

public class SkinChangeActivity extends Activity {
    private Activity mContext;
    private RatioColor mRatioColor;
    private ViewGroup mLayout;
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
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_skin_change);

        mSpu = SharedPreferencesUtil.getInstance(mContext);

        initView();
    }


    private void initView() {
        mLayout = (ViewGroup) findViewById(R.id.llyt_main);
        if (mLayout == null) {
            throw new NullPointerException("未找到视图");
        }

        mRatioColor = new RatioColor(mContext);
        mRatioColor.addItems(COLORS);
        mRatioColor.setOnCheckedColorListener(new RatioColor.onCheckedColorListener() {
            @Override
            public void doColor(int color) {
                setCurrentBgColor(color);
            }
        });

        mLayout.addView(mRatioColor);

        // 设置当前背景色
        setCurrentBgColor(mSpu.getSkin());
    }


    public void setCurrentBgColor(int color) {
        mLayout.setBackgroundColor(color);
        mRatioColor.setCheckedColor(color);
        if (mSpu.getSkin() != color) {
            mSpu.saveSkin(color);
        }
    }
}
