package com.example.commonlibrary.android.viewpager;

import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * description : 工具类
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/2 14:31
 * <p>
 */
class Util {
    private static <T> int getSize(Iterable<T> iterable) {
        if (iterable == null) {
            return 0;
        }
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).size();
        }
        int size = 0;
        for (T ignored : iterable) {
            size++;
        }
        return size;
    }

    private static boolean isOutOfIndex(Iterable<?> iterable, int index) {
        return index < 0 || index >= getSize(iterable);
    }

    public static <T> void move(List<T> list, int fromIndex, int toIndex) {
        if (isOutOfIndex(list, fromIndex)) {
            return;
        }

        if (isOutOfIndex(list, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            for (int i = fromIndex; i > toIndex; i--) {
                Collections.swap(list, i, i - 1);
            }
        } else {
            for (int i = fromIndex; i < toIndex; i++) {
                Collections.swap(list, i, i + 1);
            }
        }
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || (iterable instanceof Collection && isEmpty((Collection) iterable))
                || !iterable.iterator().hasNext();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private static <T> T safetyGet(List<T> list, int index) {
        T t = null;
        if (isValidIndex(list, index)) {
            t = list.get(index);
        }

        return t;
    }

    private static boolean isValidIndex(List<?> list, int index) {
        return index < getSize(list) && index >= 0;
    }

    static void departFromParent(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }
}
