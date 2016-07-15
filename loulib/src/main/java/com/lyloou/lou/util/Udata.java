package com.lyloou.lou.util;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Lou on 2016/4/10.
 */
public class Udata {

    public static String byteArray2HexString(byte[] datas, int byteLength) {
        if (datas.length > byteLength) {
            throw new IndexOutOfBoundsException();
        }

        byte[] tmp = new byte[byteLength];
        for (int i = datas.length; i > 0; i--) {
            tmp[tmp.length - i] = datas[datas.length - i];
        }
        return byteArray2HexString(tmp);
    }

    public static String byteArray2HexString(byte[] datas) {
        StringBuilder sb = new StringBuilder();
        for (byte data : datas) {
            sb.append(byte2HexString(data));
        }
        return sb.toString();
    }

    public static String byteArray2HexStringWithSeparator(byte[] datas, String separator) {
        StringBuilder sb = new StringBuilder();
        for (byte data : datas) {
            sb.append(byte2HexString(data) + separator);
        }
        return sb.toString();
    }

    public static byte[] hexString2ByteArray(String hexString) {
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < result.length; i++) {
            String hex = hexString.substring(i * 2, i * 2 + 2);
            int data = Integer.valueOf(hex, 16);
            result[i] = (byte) data;
        }
        return result;
    }

    public static byte getCheckByte(byte[] datas) {
        return getCheckByte(datas, 0, datas.length);
    }

    /**
     * @param datas 数据；
     * @param start 包含起始位置start;
     * @param end   不包含终止位置end;
     * @return 得到的校验位；
     */
    public static byte getCheckByte(byte[] datas, int start, int end) {
        if (start < -1 || end > datas.length || end < start) {
            throw new IllegalArgumentException();
        }

        int result = 0;
        for (int i = start; i < end; i++) {
            byte data = datas[i];
            result = result + data;
        }
        return (byte) result;
    }

    /**
     * 追加一个校验位到byte数组的最后；
     *
     * @param datas 待追加的数组；
     * @param start 要校验的起始位置，包含start；
     * @param end   要校验的终止位置，不包含end；
     * @return 返回新的已经校验过的byte数组；
     */
    public static byte[] appendCheckByte(byte[] datas, int start, int end) {
        byte[] result = new byte[datas.length + 1];
        System.arraycopy(datas, 0, result, 0, datas.length);
        byte checkByte = getCheckByte(datas, start, end);
        result[datas.length] = checkByte;
        return result;
    }

	/*
     *
	 * · hexString2Binary
	 *
	 *
	 */

    public static int getBit(int data, int shift) {
        if (data > 0xff) {
            throw new IllegalArgumentException();
        }
        return data >> shift & 0x01;
    }

    public static int getBit(byte data, int shift) {
        return getBit(byte2Int(data), shift);
    }

    public static int getBit(byte[] datas, int index, int shift) {
        return getBit(datas[index], shift);
    }

    /**
     * @param data  要改变的data；
     * @param index 左到右，以0开始；
     * @param value 改变的值：true, false；不能有其他值；
     * @return 改变后的data;
     */
    public static byte changeBit(byte data, int index, boolean value) {
        return changeBit(data, index, value ? 1 : 0);
    }

    /**
     * @param data  要改变的data；
     * @param index 要改变的byte转换成二进制后，第几个bit要改变；左到右，以0开始；
     * @param value 改变成什么值：0、1；不能有其他值；
     * @return 改变后的data;
     */
    public static byte changeBit(byte data, int index, int value) {
        if (data > 0xff || value > 1 || value < 0) {
            throw new IllegalArgumentException();
        }

        byte result = 0;
        String tmp = getBitString(data, 8);
        char[] chars = tmp.toCharArray();
        chars[index] = (char) ('0' + value);
        tmp = String.valueOf(chars);
        result = (byte) Integer.parseInt(tmp, 2);
        return result;
    }

    /**
     * @param datas     要改变的datas；
     * @param dataIndex datas数组中的第几个byte；
     * @param byteIndex 要改变的byte转换成二进制后，第几个bit要改变；左到右，以0开始；
     * @param value     改变成什么值，只能是0、1；
     * @return 返回改变后的datas数组；
     */
    public static byte[] changeBit(byte[] datas, int dataIndex, int byteIndex, int value) {
        changeBit(datas[dataIndex], dataIndex, byteIndex);
        return datas;
    }

    public static byte[] setBit(byte[] datas, int index, int shift) {
        return null;
    }

    // getBitInBitString

    public static int byte2Int(byte data) {
        return data & 0xff;
    }

    public static String byte2HexString(byte data) {
        return String.format(Locale.US, "%02x", byte2Int(data));
    }

    public static boolean byteArrayContainByte(byte[] datas, byte containData) {
        boolean result = false;
        for (byte data : datas) {
            if (data == containData) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String getBitString(int data, int length) {
        if (data > 0xff) {
            throw new IllegalArgumentException();
        }

        String result = "";
        for (int i = 0; i < length; i++) {
            result = (data >> i & 0x01) + result;
        }
        return result;
    }

    public static String getBitString(byte data, int length) {
        return getBitString(byte2Int(data), length);
    }

    public static String getBitString(byte[] datas) {
        String result = "";
        for (byte data : datas) {
            result += getBitString(data, 8);
        }
        return result;
    }

    public static boolean[] int2BooleanArray(int data, int length) {
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            result[i] = (data >> i & 0x01) == 1;
        }
        return result;
    }

    public static int booleanArray2Int(boolean[] data) {
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            result = (int) (result + Math.pow(2, i) * (data[i] ? 1 : 0));
        }
        return result;
    }

    public static String format(String pattern, Object object) {
        return new DecimalFormat(pattern).format(object);
    }
}
