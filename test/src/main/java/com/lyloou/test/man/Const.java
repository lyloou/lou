package com.lyloou.test.man;

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
