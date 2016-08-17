package com.lyloou.lou.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uimg;


/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016/8/17 15:12
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2016/8/17 15:12    Lou                 1.0             1.0
 * Why & What is modified:
 */
public class ColorPickerLayout extends FrameLayout {
    public interface OnColorChangeListener {
        void doColor(int color);
    }

    public void setOnColorChangeListener(OnColorChangeListener listener) {
        mOnColorChangeListener = listener;
    }

    private OnColorChangeListener mOnColorChangeListener;

    public ColorPickerLayout(Context context) {
        this(context, null);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    private void setUp(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_color_picker, this, true);
        final ImageView ivIndicator = (ImageView) view.findViewById(R.id.iv_simple_light_color_picker_indicator);
        final ImageView ivPointerBg = (ImageView) view.findViewById(R.id.iv_simple_light_color_picker_pointer_bg);
        final ImageView ivBg = (ImageView) view.findViewById(R.id.iv_simple_light_color_picker_bg);
        final Bitmap bgBitmap = Uimg.getBitmap(ivBg);

        ivBg.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int color = 0;

                if (x >= 0 && y >= 0 && x < bgBitmap.getWidth() && y < bgBitmap.getHeight()) {
                    color = bgBitmap.getPixel(x, y);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        if (color != 0) {
                            changePointerBgColor(ivPointerBg, color);
                            changeIndicatorPosition(ivIndicator, x, y);
                        }
                }
                return true;
            }
        });
    }

    private void changeIndicatorPosition(ImageView ivIndicator, int x, int y) {
        ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) ivIndicator.getLayoutParams();
        layoutParams.leftMargin = x - ivIndicator.getWidth() / 2;
        layoutParams.topMargin = y - ivIndicator.getHeight() / 2;
        ivIndicator.requestLayout();

        if (ivIndicator.getVisibility() != VISIBLE) {
            ivIndicator.setVisibility(VISIBLE);
        }
    }


    private void changePointerBgColor(ImageView iv, int color) {
        iv.setBackgroundColor(color);

        if (mOnColorChangeListener != null) {
            mOnColorChangeListener.doColor(color);
        }
    }
}
