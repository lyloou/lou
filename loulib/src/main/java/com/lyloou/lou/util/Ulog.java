package com.lyloou.lou.util;

import com.orhanobut.logger.Logger;

/**
 * 需要自定义Application，继承自LouApplication并在Mainfest中使用；
 * <p>
 * 注意：发布的版本不会打印，调试的版本会打印（这取决于LouApplication.initLogger中的配置）
 * 标签设置的是「Ulog」，可以以此过滤；
 */
public class Ulog {

    public static void t(String tag) {
        Logger.t(tag);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Logger.json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Logger.xml(xml);
    }

}

                                                  