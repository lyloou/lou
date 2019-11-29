package com.lyloou.test.util;

import android.text.TextUtils;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utime {

    private static final SimpleDateFormat SDF_ONE = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private static final SimpleDateFormat SDF_TWO = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    private static final SimpleDateFormat SDF_THREE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
    private static final String URL_RR_BASE = "http://ip138.com/";

    public static int[] getValidTime(String time) {
        int hourOfDay;
        int minute;
        if (isInvalidTime(time)) {
            Calendar instance = Calendar.getInstance();
            hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
            minute = instance.get(Calendar.MINUTE);
        } else {
            String[] ts = time.split(":");
            hourOfDay = Integer.parseInt(ts[0]);
            minute = Integer.parseInt(ts[1]);
        }
        return new int[]{hourOfDay, minute};
    }

    /**
     * 判断时间是否有效
     *
     * @param time 时间字符串，如：16:32
     * @return 是否有效的结果
     */
    public static boolean isInvalidTime(String time) {
        if (time == null) {
            return true;
        }
        String[] split = time.split(":");
        if (split.length != 2) {
            return true;
        }

        if (!TextUtils.isDigitsOnly(split[0])) {
            return true;
        }
        int hour = Integer.parseInt(split[0]);
        if (hour < 0 || hour > 24) {
            return true;
        }

        if (!TextUtils.isDigitsOnly(split[1])) {
            return true;
        }
        int minute = Integer.parseInt(split[1]);
        if (minute < 0 || minute > 60) {
            return true;
        }

        return false;
    }

    public static String getTimeString(int h, int m) {
        String hour = h < 10 ? ("0" + h) : ("" + h);
        String minute = m < 10 ? ("0" + m) : ("" + m);
        return hour + ":" + minute;
    }


    public static String getInterval(String timeStart, String timeEnd) {
        if (timeStart == null || timeEnd == null) {
            return null;
        }
        if (timeStart.compareTo(timeEnd) > 0) {
            return null;
        }
        int[] startArr = Utime.getValidTime(timeStart);
        int[] endArr = Utime.getValidTime(timeEnd);

        int sH = startArr[0];
        int sM = startArr[1];

        int eH = endArr[0];
        int eM = endArr[1];
        int spendAllMinute = (eH * 60 + eM) - (sH * 60 + sM);
        int spendHour = spendAllMinute / 60;
        int spendMinute = spendAllMinute % 60;
        return Utime.getTimeString(spendHour, spendMinute);
    }

    public static String transferTwoToOne(String day) {
        Date parse = null;
        try {
            parse = SDF_TWO.parse(day);
        } catch (ParseException e) {
            return null;
        }
        return SDF_ONE.format(parse);
    }

    public static String transferThreeToOne(String day) {
        Date parse;
        try {
            parse = SDF_THREE.parse(day);
        } catch (ParseException e) {
            return null;
        }
        return SDF_ONE.format(parse);
    }

    public static String today() {
        Date parse = Calendar.getInstance().getTime();
        return SDF_TWO.format(parse);
    }

    public static String getFormatTime(String timeStr) {
        if (timeStr == null) {
            return "--:--";
        }
        return timeStr;
    }

    public static String getDayWithFormatOne() {
        return SDF_ONE.format(Calendar.getInstance().getTime());
    }

    public static String getDayWithFormatTwo() {
        return SDF_TWO.format(Calendar.getInstance().getTime());
    }

    /*
     * 获取时间戳（始终使用网络时间，如果获取网络时间失败，才使用本地时间）
     * 返回的是以秒为单位的时间字符串
     */
    public static String getTimeStamp(String url, boolean usePhoneTime) {

        final long phoneTime = System.currentTimeMillis() / 1000;
        if (usePhoneTime) {
            return String.valueOf(phoneTime);
        }

        final String urlRrBase = getBaseUrl(url);

        try {
            URLConnection uc = new URL(urlRrBase).openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long webTimeMillis = uc.getDate();
            long webTime = webTimeMillis / 1000;// 读取网站日期时间
            return String.valueOf(webTime);
        } catch (Exception e) {
            return String.valueOf(phoneTime);
        }
    }

    private static String getBaseUrl(String urls) {
        if (TextUtils.isEmpty(urls)) {
            return URL_RR_BASE;
        }

        try {
            URI uri = new URI(urls);
            return uri.getScheme() + "://" + uri.getHost();
        } catch (Exception e) {
            return URL_RR_BASE;
        }

    }
}
