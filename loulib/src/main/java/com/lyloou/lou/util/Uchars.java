package com.lyloou.lou.util;

public class Uchars {

    public static String plusOne(String str) {
        char[] dst = new char[str.length()];
        char[] dstChange = new char[str.length()];
        str.getChars(0, str.length(), dst, 0);
        for (int i = 0; i < dst.length; i++) {
            char c = dst[i];
            dstChange[i] = (char) (c + 1);
        }
        return String.valueOf(dstChange);
    }

    public static String minusOne(String str) {
        char[] dst = new char[str.length()];
        char[] dstChange = new char[str.length()];
        str.getChars(0, str.length(), dst, 0);
        for (int i = 0; i < dst.length; i++) {
            char c = dst[i];
            dstChange[i] = (char) (c - 1);
        }
        return String.valueOf(dstChange);
    }

}
