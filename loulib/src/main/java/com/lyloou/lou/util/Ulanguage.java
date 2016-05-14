package com.lyloou.lou.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Ulanguage {
    public static void switchTo(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (!config.locale.equals(locale)) {
            config.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(config, dm);
        }
    }

    public static void switchTo(Context context, String language) {
        Locale locale = new Locale(language);
        switchTo(context, locale);
    }

    public static String getLanguageFromConfig(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        return config.locale.getLanguage().toUpperCase();
    }

    public static boolean currentConfigSameAs(Context context, String str) {
        return getLanguageFromConfig(context).equalsIgnoreCase(str);
    }
}
