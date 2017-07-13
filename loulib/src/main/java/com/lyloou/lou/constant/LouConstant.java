package com.lyloou.lou.constant;

import java.util.Arrays;

public class LouConstant {

    // 测试
    public static void main(String[] args) {
        ByteArray byteArray = new ByteArray(10);
        System.out.println("byteArray = " + byteArray);

        byteArray.setByte(2, (byte) 12);
        byteArray.setByte(3, (byte) 2);
        System.out.println("setByte: byteArray = " + byteArray);

        byteArray.reset();
        System.out.println("reset: byteArray = " + byteArray);

        byte[] tmpBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1};
        byteArray.reset(tmpBytes);
        System.out.println("reset(byte[]): byteArray = " + byteArray);

        tmpBytes = new byte[]{1, 2, 3, 4};
        byteArray.reset(tmpBytes);
        System.out.println("reset(byte[])2: byteArray = " + byteArray);

    }


    //: 对数组进行封装处理
    public static class ByteArray {

        // 数组一旦确定，就不能更改；
        private final byte[] mBytes;


        ByteArray(int length) {
            mBytes = new byte[length];
        }

        // 重置bytes的元素全为0
        public void reset() {
            for (int i = 0; i < mBytes.length; i++) {
                mBytes[i] = 0;
            }
        }

        // 根据传递的byte数组进行重置
        public void reset(byte[] bytes) {
            reset();
            if (bytes == null) {
                return;
            }

            int length = Math.min(bytes.length, mBytes.length);
            System.arraycopy(bytes, 0, mBytes, 0, length);
        }

        public byte[] getBytes() {
            return mBytes;
        }

        public byte getByte(int index) {
            if (index < 0 || index >= mBytes.length) {
                throw new IndexOutOfBoundsException("异常（数组越界）：数组的长度为" + mBytes.length + " ，设置的下标索引为" + index);
            }
            return mBytes[index];
        }

        public ByteArray setByte(int index, byte value) {
            if (index < 0 || index >= mBytes.length) {
                throw new IndexOutOfBoundsException("异常（数组越界）：数组的长度为" + mBytes.length + " ，设置的下标索引为" + index);
            }
            mBytes[index] = value;
            return this;
        }

        @Override
        public String toString() {
            return Arrays.toString(mBytes);
        }
    }
}
                                                  