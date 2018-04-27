package com.example.mytest.utils;

/**
 * 数字工具类
 *
 * @author yhb
 */
public class MathUtil {

    public static int parseInt(String intStr, int defaultValue) {

        if (intStr == null || intStr.length() == 0)
            return defaultValue;

        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    public static long parseLong(String intStr, long defaultValue) {

        if (intStr == null || intStr.length() == 0)
            return defaultValue;

        try {
            return Long.parseLong(intStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    public static float parseFloat(String floatStr, long defaultValue) {

        if (floatStr == null || floatStr.length() == 0)
            return defaultValue;

        try {
            return Float.parseFloat(floatStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    public static double parseDouble(String doubleStr, long defaultValue) {

        if (doubleStr == null || doubleStr.length() == 0)
            return defaultValue;

        try {
            return Double.parseDouble(doubleStr);
        } catch (Exception e) {

            return defaultValue;
        }
    }

    /**
     * Fast round from float to int. This is faster than Math.round()
     * thought it may return slightly different results. It does not try to
     * handle (in any meaningful way) NaN or infinities.
     */
    public static int round(float value) {

        long lx = (long) (value * (65536 * 256f));
        return (int) ((lx + 0x800000) >> 24);
    }
}
