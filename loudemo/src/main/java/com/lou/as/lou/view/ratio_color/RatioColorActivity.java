package com.lou.as.lou.view.ratio_color;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uscreen;
import com.lyloou.lou.util.Usp;
import com.lyloou.lou.view.RatioColor;

import static com.lou.as.lou.view.ratio_color.Keys.KEY_SKIN;

public class RatioColorActivity extends LouActivity {
    private Activity mContext;
    private RatioColor mRatioColor;

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

        transparent();

        initView();
    }

    private void transparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final int PADDING = Uscreen.dp2Px(mContext, 16);
        layout.setPadding(PADDING, PADDING * 2, PADDING, PADDING);
        setContentView(layout);

        mRatioColor = new RatioColor(mContext);
        mRatioColor.addItems(COLORS);
        mRatioColor.setOnCheckedColorListener(this::setCurrentBgColor);

        layout.addView(mRatioColor);

        // 设置当前背景色
        setCurrentBgColor(Usp.init(this).getInt(KEY_SKIN, Color.LTGRAY));
    }


    public void setCurrentBgColor(int color) {
        getWindow().getDecorView().setBackgroundColor(color);
        mRatioColor.setCheckedColor(color);
        if (Usp.init(this).getInt(KEY_SKIN, Color.LTGRAY) != color) {
            Usp.init(this).putInt(KEY_SKIN, color).commit();
        }
    }
}
