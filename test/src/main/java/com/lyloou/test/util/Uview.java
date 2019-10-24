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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.lyloou.test.common.Consumer;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.11 13:28
 * <p>
 * Description:
 */
public class Uview {
    public static void initStatusBar(Activity context, int statusBarColor) {
        ImmersionBar.with(context)
                .statusBarDarkFont(false)
                .navigationBarDarkIcon(false)
                .statusBarColor(statusBarColor)
                .init();
    }

    public static Bitmap getBitmapFromImageView(ImageView imageView) {

        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                return bitmap;
            }
        }

        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        return bitmap;
    }

    public static View getRootView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    /**
     * [How to hide soft keyboard on android after clicking outside EditText? - Stack Overflow](https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext)
     *
     * @param context 上下文
     * @param view    EditText上层 view
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void registerHideSoftKeyboardListener(Activity context, View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(context);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                registerHideSoftKeyboardListener(context, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                context.getCurrentFocus().getWindowToken(), 0);
    }

    public static void doWhenSoftKeyboardChanged(Activity activity, Consumer<Boolean> consumer) {
        final View activityRootView = activity.findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            activityRootView.getWindowVisibleDisplayFrame(r);
            int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
            boolean hide = heightDiff > 100;
            consumer.accept(!hide);
        });
    }
}
