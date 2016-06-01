package com.lou.as.lou;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.view.RatioColor;

public class ViewRatioColorActivity extends LouActivity {
    private Activity mContext;
    private RatioColor mRatioColor;
    private LinearLayout mLayout;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mLayout = new LinearLayout(mContext);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        final int PADDING = Uscreen.dp2Px(mContext, 16);
        mLayout.setPadding(PADDING, PADDING * 2, PADDING, PADDING);

        setContentView(mLayout);

        mSpu = SharedPreferencesUtil.getInstance(mContext);
        initView();
    }


    private void initView() {
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
        getWindow().getDecorView().setBackgroundColor(color);
        mRatioColor.setCheckedColor(color);
        if (mSpu.getSkin() != color) {
            mSpu.saveSkin(color);
        }
    }
}
