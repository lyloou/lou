package com.lyloou.lou.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * 类描述：自定义的扇形进度条
 * 创建人： Lou
 * 创建时间： 2016/8/4 13:41
 * 用法：
 * {@code
 * <com.lyloou.lou.view.ProgressView
 * android:id="@+id/pv_sync"
 * android:layout_width="38dp"
 * android:layout_height="38dp"/>
 * }
 * <p>
 * {@code
 * int currentIndex = 20;
 * int totalCount = 100;
 * ProgressView pvSync = ButterKnife.findById(view, R.id.pv_sync);
 * pvSync.updateProgress(currentIndex, totalCount);
 * }
 */
public class ProgressView extends View {
    private Paint mPaint;
    private RectF mRectF;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int weight = getWidth();
        // 画外环
        mRectF.set(0, 0, weight, height);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(mRectF, mPaint);

        // 画扇形
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawArc(mRectF, -90, mAngle, true, mPaint);
    }

    private int mAngle;

    public void updateProgress(int currentIndex, int totalCount) {
        mAngle = (int) (360 * (currentIndex * 1.0f / totalCount));
        postInvalidate();
    }
}
