/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class Uscreen {

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

    // [Android App 沉浸式状态栏解决方案 - Android - 掘金](https://juejin.im/entry/56c3d26679bc44005128cabc)
    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    public static void setViewMarginTop(Activity activity, View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = getStatusBarHeight(activity);
    }

    public static void setToolbarMarginTop(Activity activity, Toolbar toolbar) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        params.topMargin = getStatusBarHeight(activity);
    }

    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public static int getStatusBarHeight(Context activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }


    public static void setBackgroundViaBitmap(Context context, final Bitmap sourceBitmap) {

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        final int sourceWidth = sourceBitmap.getWidth();
        final int sourceHeight = sourceBitmap.getHeight();
        final int letterboxedWidth = Uscreen.getScreenWidth(context);
        final int letterboxedHeight = Uscreen.getScreenHeight(context) - Uscreen.getStatusBarHeight(context);

        final float resizeRatioX = (float) letterboxedWidth / sourceWidth;
        final float resizeRatioY = (float) letterboxedHeight / sourceHeight;
        final float resizeRatio = resizeRatioX - resizeRatioY > 0 ? resizeRatioX : resizeRatioY;

        final Bitmap letterboxedBitmap = Bitmap.createBitmap(letterboxedWidth, letterboxedHeight, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(letterboxedBitmap);
        canvas.drawRGB(0, 0, 0);

        final Matrix transformations = new Matrix();
        transformations.postScale(resizeRatio, resizeRatio);
        transformations.postTranslate(0, Uscreen.getStatusBarHeight(context));
        canvas.drawBitmap(sourceBitmap, transformations, null);

        try {
            wallpaperManager.setBitmap(letterboxedBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setWallpaper(ImageView imageView) {
        imageView.setOnLongClickListener(v -> {

            Bitmap bitmap = Uview.getBitmapFromImageView(imageView);

            if (bitmap == null) {
                Snackbar.make(imageView,"无法设壁纸" , Snackbar.LENGTH_SHORT).show();
            } else {
                Uscreen.setBackgroundViaBitmap(imageView.getContext(), bitmap);
                Snackbar.make(imageView,"已设壁纸" , Snackbar.LENGTH_SHORT).show();
            }
            return false;
        });
    }

}