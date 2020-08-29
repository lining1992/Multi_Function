package com.example.commonlibrary.android.base;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2019/2/22 11:26
 */
public class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    public static <T> boolean contains(T[] container, T element) {
        if (container == null || container.length == 0) {
            return false;
        }
        for (int i = 0, size = container.length; i < size; i++) {
            T t = container[i];
            if (t == null) {
                if (element == null) {
                    return true;
                } else {
                    continue;
                }
            }

            if (t.equals(element)) {
                return true;
            }
        }

        return false;
    }
}
