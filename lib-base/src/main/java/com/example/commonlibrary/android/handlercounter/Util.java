package com.example.commonlibrary.android.handlercounter;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/16 15:31
 */
@SuppressWarnings("WeakerAccess")
public class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    public static long nextValue(long currentValue, long endValue, long step) {
        long nextValue;
        if (currentValue < endValue) {
            nextValue = Math.min(currentValue + step, endValue);
        } else if (currentValue > endValue) {
            nextValue = Math.max(currentValue - step, endValue);
        } else {
            nextValue = currentValue;
        }
        return nextValue;
    }
}