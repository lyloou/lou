package com.lyloou.test.kingsoftware;

import com.lyloou.test.util.Utime;

public class KingsoftwareUtil {
    public static String getShareImage(String day) {
        return String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", day);
    }

    public static String getBigImage(String day) {
        return String.format("http://cdn.iciba.com/news/word/big_%sb.jpg", day);
    }

    public static String getTodayBigImage() {
        return String.format("http://cdn.iciba.com/news/word/big_%sb.jpg", Utime.getDayWithFormatTwo());
    }

    public static String getTodayShareImage() {
        return String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", Utime.getDayWithFormatOne());
    }

}
