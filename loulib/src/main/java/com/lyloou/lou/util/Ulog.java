package com.lyloou.lou.util;

import android.util.Log;

import com.lyloou.lou.app.LouApplication;

@Deprecated
public class Ulog {

	private static final boolean DEBUG_MODE = LouApplication.DEBUG;

	public static void d(String tag, String msg){if(DEBUG_MODE){Log.d(tag, msg);}}
	public static void e(String tag, String msg){if(DEBUG_MODE){Log.e(tag, msg);}}
	public static void i(String tag, String msg){if(DEBUG_MODE){Log.i(tag, msg);}}
}
                                                  