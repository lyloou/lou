package com.lyloou.lou.constant;

public class LouConstant {
    public static final String TAG = "Lou";

    public static byte[] newByteArray(int byteArrayLength){
        return null;
    }

    private static class InnerByteArrayConstant {
        private final byte[] mBytes;

        InnerByteArrayConstant(int byteArrayLength) {
            mBytes = new byte[byteArrayLength];
        }


        public InnerByteArrayConstant setByte(int index, byte value) {
            if (index < 0 || index >= mBytes.length) {
                throw new IndexOutOfBoundsException("异常（数组越界）：数组的长度为" + mBytes.length + " ，设置的下标索引为" + index);
            }
            mBytes[index] = value;
            return this;
        }
    }
}
                                                  