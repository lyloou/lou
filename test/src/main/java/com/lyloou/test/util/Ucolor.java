package com.lyloou.test.util;

import android.graphics.Color;

public class Ucolor {

    // [Change int color opacity in java/android - Stack Overflow](https://stackoverflow.com/questions/28483497/change-int-color-opacity-in-java-android)
    public static int getTransparentColor(int color, double alpha1) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        // Set alpha based on your logic, here I'm making it 25% of it's initial value.
        alpha *= alpha1;

        return Color.argb(alpha, red, green, blue);
    }

    public static int getTransparentColor(int color) {
        return getTransparentColor(color, 0.55);
    }
}
