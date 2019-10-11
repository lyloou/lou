package com.lyloou.test.util;

import android.support.annotation.NonNull;

public class Ustring {
    public static String join(@NonNull String delimiter, String... elements) {
        int length = elements.length;
        if (length == 0) {
            return "";
        }
        if (length == 1) {
            return elements[0];
        }
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                tmp.append(delimiter);
            }
            tmp.append(elements[i]);
        }
        return tmp.toString();
    }
}
