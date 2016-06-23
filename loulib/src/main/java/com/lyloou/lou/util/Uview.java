package com.lyloou.lou.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Uview {

    public static void clickEffectByAlpha(OnClickListener clickListener, View... views) {
        clickEffectByAlpha(clickListener, true, true, views);
    }

    public static void clickEffectByAlphaWithSrc(OnClickListener clickListener, View... views) {
        clickEffectByAlpha(clickListener, false, true, views);
    }

    public static void clickEffectByAlphaWithBg(OnClickListener clickListener, View... views) {
        clickEffectByAlpha(clickListener, true, false, views);
    }

    public static void clickEffectByAlpha(OnClickListener clickListener, final boolean bgIsAlpha, final boolean srcIsAlpha, View... views) {
        if (clickListener == null) {
            return;
        }
        // 逐个处理每个view
        for (final View v : views) {
            if (v == null) {
                continue;
            }
            // 设置监听
            v.setOnClickListener(clickListener);
            // 根据背景是否为空，来处理点击后的效果：不为空则设置透明，为空则设置默认颜色；
            final boolean bgIsNull = v.getBackground() == null;
            // 设置触摸效果
            v.setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            doDown(view, bgIsNull);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            doReset(view, bgIsNull);
                            break;
                        case MotionEvent.ACTION_UP:
                            doReset(view, bgIsNull);
                            break;
                    }
                    return false;
                }

                // 按下时
                private void doDown(View view, boolean bgIsNull) {
                    if (srcIsAlpha) {
                        view.animate().cancel();
                        view.setAlpha(0.4f);
                    }
                    if (bgIsAlpha) {
                        if (bgIsNull) {
                            view.setBackgroundColor(0x88ffffff);
                        } else {
                            view.getBackground().setAlpha(202);
                        }
                    }
                }

                // 恢复视图
                private void doReset(View view, boolean bgIsNull) {
                    if (srcIsAlpha) {
                        view.animate().alpha(1f).setDuration(100).start();
                    }
                    if (bgIsAlpha) {
                        if (bgIsNull) {
                            view.setBackground(null);
                        } else {
                            view.getBackground().setAlpha(255);
                        }
                    }
                }
            });
        }
    }

    public static void clickEffectByNoEffect(OnClickListener clickListener, View... views) {
        if (clickListener == null) {
            return;
        }
        // 逐个处理每个view
        for (final View v : views) {
            if (v == null) {
                continue;
            }
            // 设置监听
            v.setOnClickListener(clickListener);
        }
    }

    public static void clickEffectByScale(OnClickListener clickListener, final float ratio, View... views) {
        if (clickListener == null) {
            return;
        }
        // 逐个处理每个view
        for (final View v : views) {
            if (v == null) {
                continue;
            }
            // 设置监听
            v.setOnClickListener(clickListener);
            // 设置触摸效果
            v.setOnTouchListener(new OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            doDown(v);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            doReset(v);
                            break;
                        case MotionEvent.ACTION_UP:
                            doReset(v);
                            break;
                    }
                    return false;
                }

                // 按下时
                private void doDown(View view) {
                    view.clearAnimation();
                    view.startAnimation(Uanimation.getScaleAnimation(200, 1.0f, ratio));
                }

                // 恢复视图
                private void doReset(View view) {
                    view.startAnimation(Uanimation.getScaleAnimation(200, ratio, 1.0f));
                }
            });
        }
    }

    public static View inflate(Context context, int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public static int getSizeFromMeasureSpec(int measureSpec, int defaultSize) {
        int result = 0;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = defaultSize;
            if (mode == View.MeasureSpec.AT_MOST) {
                result = Math.min(defaultSize, size);
            }
        }
        return result;
    }

    /**
     * @param context
     * @param resId         the imageView id
     * @param dstColor      the color of the dst
     * @param width         the total width of view
     * @param height        the total height of view
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     * @param mode          the mode to use
     * @return
     */
    public static Bitmap getBitmapByXfermode(Context context,
                                             int resId,
                                             int dstColor,
                                             int width,
                                             int height,
                                             int paddingLeft,
                                             int paddingTop,
                                             int paddingRight,
                                             int paddingBottom,
                                             PorterDuff.Mode mode) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        // get the diameter of the output bitmap.
        int diameter = Math.min(width - (paddingLeft + paddingRight),
                height - (paddingTop + paddingBottom));

        // Scale bitmap by ratio.
        float sx = (diameter * 1.0f) / bWidth;
        float sy = (diameter * 1.0f) / bHeight;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);

        // get the output bitmap and the canvas of the bitmap.
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);

        // init paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        // draw dst
        RectF rectF = new RectF((width - diameter) / 2, (height - diameter) / 2, (width + diameter) / 2, (height + diameter) / 2);
        paint.setColor(dstColor);
        canvas.drawOval(rectF, paint);

        // set mode
        paint.setXfermode(new PorterDuffXfermode(mode));

        // draw src
        canvas.translate((width - diameter) / 2, (height - diameter) / 2); // it's important;
        canvas.drawBitmap(bitmap, matrix, paint);

        return outBitmap;
    }

    public static Bitmap getBitmapByXfermode(Context context,
                                             int resId,
                                             int dstColor,
                                             int width,
                                             int height,
                                             PorterDuff.Mode mode) {
        return getBitmapByXfermode(context, resId, dstColor, width, height, 2, 2, 2, 2, mode);
    }

    public static Bitmap getBitmapByXfermode(Context context,
                                             Bitmap bitmap,
                                             int dstColor,
                                             int width,
                                             int height,
                                             PorterDuff.Mode mode) {
        return getBitmapByXfermode(bitmap, dstColor, width, height, 2, 2, 2, 2, mode);
    }

    public static Bitmap getBitmapByXfermode(
            Bitmap bitmap,
            int dstColor,
            int width,
            int height,
            int paddingLeft,
            int paddingTop,
            int paddingRight,
            int paddingBottom,
            PorterDuff.Mode mode) {

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        // get the diameter of the output bitmap.
        int diameter = Math.min(width - (paddingLeft + paddingRight),
                height - (paddingTop + paddingBottom));

        // Scale bitmap by ratio.
        float sx = (diameter * 1.0f) / bWidth;
        float sy = (diameter * 1.0f) / bHeight;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);

        // get the output bitmap and the canvas of the bitmap.
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);

        // init paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        // draw dst
        RectF rectF = new RectF((width - diameter) / 2, (height - diameter) / 2, (width + diameter) / 2, (height + diameter) / 2);
        paint.setColor(dstColor);
        canvas.drawOval(rectF, paint);

        // set mode
        paint.setXfermode(new PorterDuffXfermode(mode));

        // draw src
        canvas.translate((width - diameter) / 2, (height - diameter) / 2); // it's important;
        canvas.drawBitmap(bitmap, matrix, paint);

        return outBitmap;
    }

    public static Bitmap getCircleTextBitmap(Context context,
                                             String text,
                                             int width,
                                             int height,
                                             int bgColor,
                                             int fontColor,
                                             float strokeWidth,
                                             float textSize) {

        // get the output bitmap and the canvas of the bitmap.
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);

        // init paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        // draw dst
        RectF rectF = new RectF(0, 0, width, height);
        paint.setColor(bgColor);
        canvas.drawOval(rectF, paint);

        paint.setStrokeWidth(strokeWidth);
        paint.setTextSize(textSize);
        paint.setColor(fontColor);
        Paint.FontMetricsInt pmi = paint.getFontMetricsInt();
        int baseLine = (int) ((rectF.top + rectF.bottom - pmi.bottom - pmi.top) / 2);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, rectF.centerX(), baseLine, paint);

        return outBitmap;
    }

    public static void enable(boolean enable, boolean withAnim, View... vs) {
        for (View v : vs) {
            if(v == null){
                continue;
            }

            v.setEnabled(enable);
            if (enable) {
                if (withAnim) {
                    v.animate().alpha(1.0f).setDuration(100).start();
                } else {
                    v.setAlpha(1.0f);
                }
            } else {
                if (withAnim) {
                    v.animate().alpha(0.4f).setDuration(100).start();
                } else {
                    v.setAlpha(0.4f);
                }
            }

        }
    }

    public static void finishActivityByClickView(final Activity context, View v){
        clickEffectByAlphaWithSrc(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.finish();
            }
        }, v);
    }

    public static void changeTimePickerSepColor(ViewGroup group, int color) {
        for(NumberPicker np : getNumberPickers(group)){
            changeNumberPickerSepColor(np, color);
        }
    }


    private static List<NumberPicker> getNumberPickers(ViewGroup group){
        List<NumberPicker> lists = new ArrayList<NumberPicker>();
        if(group == null){
            return lists;
        }

        for(int i=0; i<group.getChildCount();i++){
            View v = group.getChildAt(i);
            if( v instanceof NumberPicker){
                lists.add((NumberPicker) v);
            } else if(v instanceof LinearLayout){
                List<NumberPicker> ls = getNumberPickers((ViewGroup) v);
                if(ls.size()>0){
                    return ls;
                }
            }
        }
        return lists;
    }

    public static void changeNumberPickerSepColor(NumberPicker np, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for(Field f : pickerFields){
            if(f.getName().equals("mSelectionDivider")){
                try{
                    f.setAccessible(true);
                    f.set(np, new ColorDrawable(color));
                } catch(Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
