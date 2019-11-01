package com.lyloou.test.man;

enum OperateType {
    DELETE("删除"),
    CLEAR_HISTORY("清除历史");
    String title;

    OperateType(String title) {
        this.title = title;
    }

    public static OperateType indexOf(int index) {
        return OperateType.values()[index];
    }

    public static String[] toStrArray() {
        OperateType[] values = OperateType.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].title;
        }
        return result;
    }
}