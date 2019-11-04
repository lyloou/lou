package com.lyloou.test.man;

import android.content.Context;

/**
 * you can use ctrl+alt+F7 to navigate usage position
 */
interface Const {

    enum Extra implements Str {
        WEB_DATA,
        WEB_TITLE,
        ;

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }

    }

    enum Action implements Str {
        POSITION,
        ;

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }
    }

    enum Key implements Str {
        ;

        public static String get(Context context, String key) {
            return context.getPackageName().concat(".").concat(key);
        }

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }
    }

    static String getSimpleStr(Class clazz, String name) {
        return clazz.getSimpleName().toUpperCase().concat("_").concat(name);
    }

    interface Str {
        String str();
    }
}
