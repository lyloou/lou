package com.lyloou.lou.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lou on 2016/4/10.
 * <p>
 * (https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat. html)
 * <p>
 * Letter Date or Time Component Presentation Examples
 * <p>
 * G Era designator Text AD
 * <p>
 * y Year Year 1996; 96
 * <p>
 * Y Week year Year 2009; 09
 * <p>
 * M Month in year Month July; Jul; 07
 * <p>
 * w Week in year Number 27
 * <p>
 * W Week in month Number 2
 * <p>
 * D Day in year Number 189
 * <p>
 * d Day in month Number 10
 * <p>
 * F Day of week in month Number 2
 * <p>
 * E Day name in week Text Tuesday; Tue
 * <p>
 * u Day number of week (1 = Monday, ..., 7 = Sunday) Number 1
 * <p>
 * a Am/pm marker Text PM
 * <p>
 * H Hour in day (0-23) Number 0
 * <p>
 * k Hour in day (1-24) Number 24
 * <p>
 * K Hour in am/pm (0-11) Number 0
 * <p>
 * h Hour in am/pm (1-12) Number 12
 * <p>
 * m Minute in hour Number 30
 * <p>
 * s Second in minute Number 55
 * <p>
 * S Millisecond Number 978
 * <p>
 * z Time zone General time zone Pacific Standard Time; PST; GMT-08:00
 * <p>
 * Z Time zone RFC 822 time zone -0800
 * <p>
 * X Time zone ISO 8601 time zone -08; -0800; -08:00
 * <p>
 */
public class Udate {

	public static String format(Date date, String pattern) {
		return format(date, pattern, Locale.getDefault());
	}

	public static String format(Date date, String pattern, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		return sdf.format(date);
	}

	public static String format(long millisecond, String pattern) {
		return format(millisecond, pattern, Locale.getDefault());
	}

	public static String format(long millisecond, String pattern, Locale locale) {
		Date date = new Date(millisecond);
		return format(date, pattern, locale);
	}

	private static volatile Calendar c;

	public static int getField(Date date, int field) {
		if (c == null) {
			c = Calendar.getInstance();
		}
		c.setTime(date);
		return c.get(field);
	}

	public static int getYear(Date date) {
		return getField(date, Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		return getField(date, Calendar.MONTH);
	}

	public static int getDay(Date date) {
		return getField(date, Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		return getField(date, Calendar.HOUR);
	}

	public static int getMinute(Date date) {
		return getField(date, Calendar.MINUTE);
	}

}
