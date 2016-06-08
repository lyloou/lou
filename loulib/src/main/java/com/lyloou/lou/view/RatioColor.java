package com.lyloou.lou.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;

import com.lyloou.lou.R;
import com.lyloou.lou.util.Uscreen;

public class RatioColor extends RadioGroup {
    public interface onCheckedColorListener {
        void doColor(int color);
    }

    private onCheckedColorListener mColorListener;
    private final Context CONTEXT = getContext();
    private final int W = Uscreen.dp2Px(CONTEXT, 48);

    public RatioColor(Context context) {
        this(context, null);
    }

    public RatioColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }


    private void setUp() {
        // 设置默认宽高
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, W));
        // 设置默认样式
        setProperties(Color.parseColor("#eeeeee"), RadioGroup.HORIZONTAL, Gravity.START);

        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
    }

    private GestureDetectorCompat mDetector;

    public void setProperties(int bgColor, int orientation, int gravity) {
        setBackgroundColor(bgColor);
        setOrientation(orientation);
        setGravity(gravity);

        ViewGroup.LayoutParams params = getLayoutParams();
        // 在父布局里居左显示
        if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).gravity = Gravity.START;
        }
        // 动态更改方向
        if (orientation == VERTICAL) {
            params.width = W;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = W;
        }
    }

    /**
     * 设置颜色变化的监听
     */

    public void setOnCheckedColorListener(onCheckedColorListener listener) {
        mColorListener = listener;
    }


    /**
     * 根据一组颜色值添加一组元素
     */

    public void addItems(int... colorArray) {
        for (int color : colorArray) {
            addItem(color);
        }
    }

    /**
     * 重新设置一组元素
     */

    public void resetItems(int[] colorArray) {
        removeAllViews();
        if (colorArray != null)
            addItems(colorArray);
    }

    /**
     * 根据颜色值添加一个元素
     *
     * @param color
     */
    public void addItem(final int color) {

        RadioButton rbtn = new RadioButton(CONTEXT);
        rbtn.setFocusable(false);
        rbtn.setFocusableInTouchMode(false);
        rbtn.setLayoutParams(new RadioGroup.LayoutParams(W, W));
        rbtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT)); // 取消默认的样式
        rbtn.setBackgroundResource(R.drawable.selector_skin_color); // 设置自己的样式（shape）

        // 动态设置颜色值（基于selector）
        StateListDrawable gradientDrawable = (StateListDrawable) rbtn.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) gradientDrawable.getConstantState();
        Drawable[] children = drawableContainerState.getChildren();
        for (Drawable child : children) {
            if (child instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) child;
                Drawable drawable = layerDrawable.getDrawable(0);
                if (drawable instanceof GradientDrawable) {
                    GradientDrawable selectedDrawable = (GradientDrawable) drawable;
                    selectedDrawable.mutate(); // 此句不可少
                    selectedDrawable.setColor(color);
                }
            }
        }

        // 将颜色信息保存到Tag中
        rbtn.setTag(color);
        clickEffectByScaleAnim(rbtn, mOnCheckedChangeListener);
        addView(rbtn);
    }

    /**
     * 设置某个颜色为选中状态
     *
     * @param checkedColor
     */
    public void setCheckedColor(int checkedColor) {

        for (int i = 0; i < getChildCount(); i++) {
            CompoundButton cbtn = (CompoundButton) getChildAt(i);
            int color = (int) cbtn.getTag();
            if (checkedColor == color) {
                cbtn.setChecked(true);
            }
        }
    }

    /**
     * 获取当前选中的颜色
     *
     * @return
     */
    public int getCheckedColor() {
        for (int i = 0; i < getChildCount(); i++) {
            CompoundButton cbtn = (CompoundButton) getChildAt(i);
            if (cbtn.isChecked()) {
                return (int) cbtn.getTag();
            }
        }
        return 0;
    }

    // 共用一个listener，避免多次创建消耗过多内存
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                // 从Tag中取出color信息
                int color = (int) buttonView.getTag();
                if (mColorListener != null) {
                    mColorListener.doColor(color);
                }
            }
        }
    };


    // -------------------- 滑动处理
    private int mLastX;
    private int mLastY;
    private Scroller mScroller = new Scroller(CONTEXT);

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void smoothScrollTo(int dextX) {
        smoothScrollBy(dextX - getScrollX());
    }

    private void smoothScrollBy(int deltaX) {
        mScroller.startScroll(getScrollX(), 0, deltaX, 0, 480);
        invalidate();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {

            mScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(RatioColor.this);
            Log.e("Lou", "---- when down ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            mScroller.forceFinished(true);
            // 最大宽度=子视图宽度-视图自身宽度
            int maxWidth = getChildCount() * W - (getWidth() - getPaddingStart() - getPaddingEnd());
            if (maxWidth < 0) maxWidth = 0;
            mScroller.fling(getScrollX(), 0, (int) -velocityX, 0, 0, maxWidth, 0, 0);
            ViewCompat.postInvalidateOnAnimation(RatioColor.this);
            return true;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                // 松开后判断是否需要回弹
                int scrollX = getScrollX();
                int maxX = getChildCount() * W - getWidth();
                if (scrollX < 0) {
                    smoothScrollTo(0);
                } else if (scrollX > maxX) {
                    smoothScrollTo(maxX);
                }
                break;
        }
        mLastX = x;
        mLastY = y;

        return true;
    }


    // 滑动冲突处理
    private float mLastXIntercepted;
    private float mLastYIntercepted;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动的水平距离大于垂直距离时，进行拦截
                intercepted = Math.abs(x - mLastXIntercepted) >= Math.abs(y - mLastYIntercepted) + 4;
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }

        mLastX = x; // 此句不可少
        mLastY = y;
        mLastXIntercepted = x;
        mLastYIntercepted = y;
        return intercepted;
    }

    // ~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~ 工具方法
    public static void clickEffectByScaleAnim(final RadioButton radioButton,
                                              RadioButton.OnCheckedChangeListener listener) {
        // 设置监听
        radioButton.setOnCheckedChangeListener(listener);

        // 设置触摸效果
        radioButton.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (radioButton.isChecked()) { // 如果已经是选中状态，则不做任何效果处理；
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        doDown(v);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        doCancel(v);
                        break;
                    case MotionEvent.ACTION_UP:
                        doUp(v);
                        break;
                }
                return false;
            }

            // 按下时开始动画
            private void doDown(View view) {
                view.startAnimation(newAnimation(1f, 2f, 1000, true));
            }

            // 恢复视图
            private void doUp(View view) {
                view.clearAnimation();
                view.startAnimation(newAnimation(0.4f, 1f, 400, false));
            }

            // 取消动画
            private void doCancel(View view) {
                view.clearAnimation();
            }
        });
    }

    public static Animation newAnimation(float from, float to, int duration, boolean repeat) {
        Animation anim = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(false);
        if (repeat) {
            anim.setRepeatCount(Animation.INFINITE);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setInterpolator(new BounceInterpolator());
        }
        return anim;
    }
    // --------------------
}