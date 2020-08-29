package com.example.commonlibrary.android.viewpager.table;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

    public static <T> List<List<List<T>>> to3D(List<T> list, int row, int column) {
        // noinspection unchecked
        return toMultiDimension(list, row, column);
    }

    public static <T> List<T> from3D(List<List<List<T>>> lists) {
        return fromMultiDimension(lists, 3);
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

    private static <T> List toMultiDimension(Iterable<T> list, int... sizes) {
        List<T> result = new ArrayList<>();
        if (isEmpty(list)) {
            return result;
        }
        int[] capacities = new int[sizes.length + 1];
        capacities[sizes.length] = 1;
        for (int i = sizes.length - 1; i >= 0; i--) {
            capacities[i] = capacities[i + 1] * sizes[i];
        }

        int[] indexArray = new int[sizes.length + 1];
        int index;
        Iterator<T> iterator = list.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            index = i;

            for (int j = 0; j < indexArray.length; j++) {
                indexArray[j] = index / capacities[j];
                index = index % capacities[j];
            }

            List parent = result;
            for (int j = 0; j < indexArray.length - 1; j++) {
                index = indexArray[j];
                Object child = safetyGet(parent, index);
                if (child == null) {
                    child = new ArrayList();
                    // noinspection unchecked
                    parent.add(child);
                }
                parent = (List) child;
            }
            // noinspection unchecked
            parent.add(t);
            i++;
        }

        return result;
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> List<T> fromMultiDimension(Iterable iterable, int dimension) {
        List<T> result = new ArrayList<>();
        if (isEmpty(iterable)) {
            return result;
        }

        Iterable i = iterable;
        for (int d = 0; d < dimension - 1; d++) {
            ArrayList<Iterable> iterableList = new ArrayList<>();
            for (Object o1 : i) {
                Iterable iterable1 = (Iterable) o1;
                if (d < dimension - 2) {
                    for (Object o2 : iterable1) {
                        iterableList.add((Iterable) o2);
                    }
                } else {
                    for (Object o : iterable1) {
                        // noinspection unchecked
                        result.add((T) o);
                    }
                }
            }
            i = iterableList;
        }

        return result;
    }


    static void departFromParent(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }
}
