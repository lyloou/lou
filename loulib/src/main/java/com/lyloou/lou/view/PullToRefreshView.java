package com.lyloou.lou.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;


/**
 * Created by Lou on 2016/3/29.
 */
public class PullToRefreshView extends ListView {
    private View mHeadView;
    private int HEAD_VIEW_HEIGHT = 0;

    private int mStatus;
    private boolean mLoaded;

    private OnChangeStatusListener mOnChangeStatusListener;
    private int mFirstVisibleItem = -1;

    public PullToRefreshView(Context context) {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 用来判断是否要展示HeadView
                mFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                move(ev);
                break;

            case MotionEvent.ACTION_UP:
                up();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public View getHeadView() {
        return mHeadView;
    }

    public void setHeadView(int layoutId) {
        mHeadView = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addHeaderView(mHeadView);

        // 获取 HeadView 的布局高度；
        HEAD_VIEW_HEIGHT = getLayoutHeight(mHeadView);

        //初始化的时候隐藏 HeadView；
        changePullViewPaddingTop(-HEAD_VIEW_HEIGHT);
    }


    private void changePullViewPaddingTop(int currentPaddingTop) {

        if (mLoaded) {
            changeStatus(OnChangeStatusListener.STATUS_COMPLETE, currentPaddingTop);
        } else {
            int boundary = ScreenUtil.dp2Px(getContext(), 2);
            if (currentPaddingTop > -boundary) {
                if (currentPaddingTop > 0)
                    currentPaddingTop = 0;

                changeStatus(OnChangeStatusListener.STATUS_RELEASE, currentPaddingTop);
            } else {
                changeStatus(OnChangeStatusListener.STATUS_PULL, currentPaddingTop);
            }
        }

        mHeadView.setPadding(mHeadView.getPaddingLeft(), currentPaddingTop, mHeadView.getPaddingRight(), mHeadView.getPaddingBottom());
    }


    public void setOnChangeStatusListener(OnChangeStatusListener listener) {
        mOnChangeStatusListener = listener;
    }


    private void changeStatus(int status, int currentPaddingTop) {
        mStatus = status;
        if (mOnChangeStatusListener != null) {
            mOnChangeStatusListener.changeStatus(status, currentPaddingTop);
        }
    }

    private void changePullViewPaddingTopByDelta(int delta) {
        int currentPaddingTop = mHeadView.getPaddingTop();
        changePullViewPaddingTop(currentPaddingTop + delta);
    }


    private float mLastY;

    private void move(MotionEvent ev) {
        // 如果没有设置HeadView，则拖拽的时候什么都不处理；
        // 如果第一个 item 不可见，则拖拽的时候什么都不处理；
        // 如果没有设置更改监听，则拖拽的时候什么都不处理；
        // 如果正在加载，则拖拽的时候什么都不处理；
        if (mHeadView == null || mFirstVisibleItem != 0 || mOnChangeStatusListener == null || mLoaded) {
            return;
        }

        // 如果第一个 item 可见，并且往下拖拽，开始一点点显示 mHeadView ;
        float delta = (ev.getY() - mLastY);
        changePullViewPaddingTopByDelta((int) (delta / 1.5));
        mLastY = ev.getY();
    }

    // 松开手指后根据当前的状态进行具体的操作；
    private void up() {
        switch (mStatus) {
            case OnChangeStatusListener.STATUS_PULL:
                recoverPullView();
                break;
            case OnChangeStatusListener.STATUS_RELEASE:
                if (mOnChangeStatusListener != null) {
                    mLoaded = true;
                    changeStatus(OnChangeStatusListener.STATUS_RUNNING, mHeadView.getPaddingTop());
                    mOnChangeStatusListener.loadData();
                }
                break;
        }
    }


    // 恢复到原始状态(让PaddingTop逐渐变为 -HEAD_VIEW_HEIGHT)

    private void recoverPullView() {
        ValueAnimator animator = ValueAnimator.ofInt(mHeadView.getPaddingTop(), -HEAD_VIEW_HEIGHT);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                changePullViewPaddingTop(value);
            }

        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLoaded = false;
            }
        });
        animator.setDuration(400);
        animator.start();
    }
    
    /**
     * 外部调用，用于恢复视图；
     * @param loaded 是否已经加载过数据；
     */
    public void recover(boolean loaded){
    	if(loaded){
    	  changeStatus(OnChangeStatusListener.STATUS_COMPLETE, 0);
          recoverPullView();
    	} else {
    		recoverPullView();
    	}
    }
    

    /**
     * 外部调用，用于一开始的时候显示正在运行；
     */
    public void initRunning(){
    	changePullViewPaddingTop(0);
    	changeStatus(OnChangeStatusListener.STATUS_RUNNING, mHeadView.getPaddingTop());
    	mLoaded = true;
    }

    // 用于回调的接口；
    public interface OnChangeStatusListener {
        // 状态，0：下拉可以刷新；1：松开开始刷新；2：正在刷新；3：加载完成；
        int STATUS_PULL = 0;
        int STATUS_RELEASE = 1;
        int STATUS_RUNNING = 2;
        int STATUS_COMPLETE = 3;

        void changeStatus(int status, int currentPaddingTop);

        void loadData();
    }


    // ---------------帮助方法
    public static int getLayoutHeight(View view) {
        Display display = ((Activity) (view.getContext())).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        view.measure(size.x, size.y);
        return view.getMeasuredHeight();
    }
    // ~~~~~~~~~~~~~~~~~

    // --------------------帮助类
    private static class ScreenUtil {

        public static DisplayMetrics getMetrics(Context context) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(metrics);
            return metrics;
        }

        public static int getScreenWidth(Context context) {
            return getMetrics(context).widthPixels;
        }

        public static int getScreenHeight(Context context) {
            return getMetrics(context).heightPixels;
        }

        public static float getScreenDensity(Context context) {
            return getMetrics(context).density;
        }

        public static float getScreenScaleDensity(Context context) {
            return getMetrics(context).scaledDensity;
        }

        public static int dp2Px(Context context, float dp) {
            float px = (int) (getScreenDensity(context) * dp);
            return (int) (px + 0.5f);
        }

        public static float sp2Px(Context context, float sp) {
            float px = getScreenScaleDensity(context);
            return sp * px;
        }
    }
}

