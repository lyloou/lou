package com.lyloou.test.common;

import com.lyloou.test.onearticle.OneArticleUtil;
import com.lyloou.test.util.Utime;

public class ImageHelper {
    public static String getShareImage(String day) {
        return String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", day);
    }

    public static String getBigImage(String day) {
        int image = Integer.parseInt(day) % 98;
        return OneArticleUtil.getImage(image);
//        return String.format("http://cdn.iciba.com/news/word/big_%sb.jpg", day);
    }

    public static String getTodayBigImage() {
        return getOneArticleImg();
//        return String.format("http://cdn.iciba.com/news/word/big_%sb.jpg", Utime.getDayWithFormatTwo());
    }

    private static String getOneArticleImg() {
        String day = Utime.getDayWithFormatTwo();
        int image = Integer.parseInt(day) % 98;
        return OneArticleUtil.getImage(image);
    }

    public static String getTodayShareImage() {
        return String.format("http://cdn.iciba.com/web/news/longweibo/imag/%s.jpg", Utime.getDayWithFormatOne());
    }

}
