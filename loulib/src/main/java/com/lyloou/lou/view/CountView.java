package com.lyloou.lou.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.lyloou.lou.util.Uscreen;


public class CountView extends View {

    public interface OnChangeListener {
        void doIndex(int index);
    }

    private final Context CONTEXT = getContext();
    private final int W = Uscreen.dp2Px(CONTEXT, 36);// 每一项的固定长度
    private final int LINE_H = Uscreen.dp2Px(CONTEXT, 6); // 长直线的厚度
    private final int ITEM_LINE_W = LINE_H / 3; // 竖直刻度直线的厚度
    private final int ITEM_LINE_H = Uscreen.dp2Px(CONTEXT, 16); // 竖直刻度直线的高度
    private final int BOTTOM_FONT_SIZE = (int) Uscreen.sp2Px(CONTEXT, 12); // 底部的文字大小

    private static final int COLOR_LINE = Color.parseColor("#4422f9"); // 长直线和刻度线的颜色
    private static final int MIN_ITEM = 1; // 显示的最小刻度值
    private static final int MAX_ITEM = 20; // 显示的最大刻度值
    private static final int EXTRA_COUNT = 2; // 左边和右边额外的刻度个数

    private Paint mLinePaint;
    private TextPaint mTextPaint;
    private Scroller mScroller;
    private OnChangeListener mChangeListener;

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
        initData();
    }


    public CountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountView(Context context) {
        this(context, null);
    }

    private void setUp() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(COLOR_LINE);

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(BOTTOM_FONT_SIZE);

        mScroller = new Scroller(CONTEXT);
    }

    private void initData() {
        setIndex(MIN_ITEM);
    }

    private int mLastX;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int deltaX = x - mLastX;
                scrollBy(-deltaX, 0);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                doIndex(getIndex());
                break;
        }
        return true;
    }

    private void doIndex(int index) {
        if (mChangeListener != null) {
            mChangeListener.doIndex(index + MIN_ITEM);
        }
    }

    public int getIndex() {
        int index = 0;
        int scrollX = getScrollX();
        index = scrollX / W;
        if (scrollX <= 0) {
            index = 0;
            smoothScrollTo(index);
        } else if (index >= (MAX_ITEM - MIN_ITEM)) {
            index = MAX_ITEM - MIN_ITEM;
            smoothScrollTo(index * W);
        } else {
            int shift = scrollX % W;
            if (shift >= W / 2) {
                index++;
                smoothScrollBy(W - shift);
            } else {
                smoothScrollBy(-shift);
            }
        }
        return index;
    }

    public void setIndex(int index) {
        int newIndex = index - MIN_ITEM;
        scrollTo(newIndex * W, 0);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    private void smoothScrollTo(int dextX) {
        smoothScrollBy(dextX - getScrollX());
    }

    private void smoothScrollBy(int deltaX) {
        mScroller.startScroll(getScrollX(), 0, deltaX, 0, 320);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller != null && mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int shift = getWidth() / 2 - W / 2; // 将原点移到屏幕中间；
        int height = getHeight();

        for (int i = MIN_ITEM - EXTRA_COUNT; i < MAX_ITEM + EXTRA_COUNT; i++) {
            int startX = (i - MIN_ITEM) * W + shift;

            // 画直线
            mLinePaint.setStrokeWidth(LINE_H);
            canvas.drawLine(startX, height / 2, startX + W, height / 2, mLinePaint);

            // 画刻度
            mLinePaint.setStrokeWidth(ITEM_LINE_W);
            canvas.drawLine(startX + W / 2, height / 2 - ITEM_LINE_H, startX + W / 2, height / 2, mLinePaint);

            // 在刻度范围内，才画文字
            if (i >= MIN_ITEM && i <= MAX_ITEM) {
                String text = i + "次";
                float textWidth = mTextPaint.measureText(text);
                float textHeight = mTextPaint.getFontMetrics().bottom;
                canvas.drawText(text, startX + (W - textWidth) / 2, (height + textHeight) / 2 + textHeight * 10,
                        mTextPaint);
            }
        }
    }
}
