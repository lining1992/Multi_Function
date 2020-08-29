package com.example.commonlibrary.collection;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/**
 * @author : leixing
 * @date : 2017-02-20
 * Email       : leixing@baidu.com
 * Version     : 0.0.1
 * <p>
 * Description : util for collection
 * <p>
 * // TODO: 2018/6/25 Collection -> Iterable
 * // TODO: 2018/6/25 局部变量使用真实类型
 * // TODO: 2018/6/26 泛型界定
 */

@SuppressWarnings({"WeakerAccess", "unused", "BooleanMethodIsAlwaysInverted", "unchecked"})
public class CollectionUtil {

    private CollectionUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || (iterable instanceof Collection && isEmpty((Collection) iterable))
                || !iterable.iterator().hasNext();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean hasElement(Iterable<?> iterable) {
        return iterable != null && iterable.iterator().hasNext();
    }

    public static boolean hasElement(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean hasElement(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> boolean hasElement(T[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(boolean[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(byte[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(char[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(int[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(long[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(float[] array) {
        return array != null && array.length > 0;
    }

    public static boolean hasElement(double[] array) {
        return array != null && array.length > 0;
    }

    public static <T> int getSize(Iterable<T> iterable) {
        if (iterable == null) {
            return 0;
        }
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).size();
        }
        int size = 0;
        for (T e : iterable) {
            size++;
        }
        return size;
    }

    public static int getSize(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    public static <T> int getSize(T[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(boolean[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(byte[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(char[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(int[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(long[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(float[] array) {
        return array == null ? 0 : array.length;
    }

    public static int getSize(double[] array) {
        return array == null ? 0 : array.length;
    }

    public static boolean isValidIndex(Iterable<?> iterable, int index) {
        return index < getSize(iterable) && index >= 0;
    }

    public static boolean isValidIndex(Collection<?> collection, int index) {
        return index < getSize(collection) && index >= 0;
    }

    public static boolean isValidIndex(List<?> list, int index) {
        return index < getSize(list) && index >= 0;
    }

    public static boolean isValidIndex(boolean[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static <T> boolean isValidIndex(T[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(byte[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(char[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(int[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(float[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(double[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isValidIndex(long[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean isOutOfIndex(Iterable<?> iterable, int index) {
        return index < 0 || index >= getSize(iterable);
    }

    public static <T> boolean isOutOfIndex(T[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(boolean[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(byte[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(char[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(int[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(float[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(double[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static boolean isOutOfIndex(long[] array, int index) {
        return index < 0 || index >= getSize(array);
    }

    public static <T> boolean hasElementByKey(Map<T, ?> map, T key) {
        return !(map == null || map.isEmpty()) && map.get(key) != null;
    }

    public static <T> boolean hasElementAt(Iterable<T> iterable, int index) {
        if (iterable == null) {
            return false;
        }

        if (iterable instanceof List) {
            return isValidIndex((List<T>) iterable, index)
                    && ((List<T>) iterable).get(index) != null;
        }

        int i = 0;
        for (T t : iterable) {
            if (i == index) {
                return t != null;
            }
            i++;
        }
        return false;
    }

    public static <T> boolean hasElementAt(T[] array, int index) {
        return isValidIndex(array, index) && array[index] != null;
    }

    public static boolean hasElementAt(boolean[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(byte[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(char[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(int[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(long[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(float[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static boolean hasElementAt(double[] array, int index) {
        return index < getSize(array) && index >= 0;
    }

    public static <T> T safetyGet(Iterable<T> iterable, int index) {
        if (iterable == null) {
            return null;
        }
        if (iterable instanceof Collection) {
            return safetyGet((Collection<T>) iterable, index);
        }
        int i = 0;
        for (T t : iterable) {
            if (i == index) {
                return t;
            }
            i++;
        }
        return null;
    }

    public static <T> T safetyGet(Collection<T> collection, int index) {
        if (isOutOfIndex(collection, index)) {
            return null;
        }
        if (collection instanceof List) {
            return safetyGet((List<T>) collection, index);
        }
        return (T) collection.toArray()[index];
    }

    public static <T> T safetyGet(List<T> list, int index) {
        T t = null;
        if (isValidIndex(list, index)) {
            t = list.get(index);
        }

        return t;
    }

    public static <T> T safetyGet(T[] array, int index) {
        T t = null;
        if (isValidIndex(array, index)) {
            t = array[index];
        }

        return t;
    }

    public static <K, V> V safetyGet(Map<K, V> map, K key) {
        return map == null ? null : map.get(key);
    }

    public static <T> boolean equals(T t1, T t2) {
        if (t1 == t2) {
            return true;
        }
        if (t1 != null) {
            return t1.equals(t2);
        }
        //noinspection ConstantConditions
        return t2.equals(t1);
    }

    public static <T> boolean equals(Set<T> set1, Set<T> set2) {
        if (set1 == set2) {
            return true;
        }
        if (set1 == null || set2 == null) {
            return false;
        }

        if (set1.size() != set2.size()) {
            return false;
        }

        for (T t : set1) {
            if (t == null) {
                continue;
            }

            if (!set2.contains(t)) {
                return false;
            }
        }

        for (T t : set2) {
            if (t == null) {
                continue;
            }

            if (!set1.contains(t)) {
                return false;
            }
        }

        return true;
    }

    public static <K, V> boolean equals(Map<K, V> map1, Map<K, V> map2) {
        if (map1 == map2) {
            return true;
        }
        if (map1 == null || map2 == null) {
            return false;
        }
        Set<K> keySet1 = map1.keySet();
        Set<K> keySet2 = map2.keySet();
        if (!equals(keySet1, keySet2)) {
            return false;
        }

        for (K k : keySet1) {
            if (!equals(map1.get(k), map2.get(k))) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equals(Iterable<T> list1, Iterable<T> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }

        if (list1 instanceof Collection && list2 instanceof Collection
                && ((Collection) list1).size() != ((Collection) list2).size()) {
            return false;
        }

        Iterator<T> iterator1 = list1.iterator();
        Iterator<T> iterator2 = list2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            T t1 = iterator1.next();
            T t2 = iterator2.next();
            if (!equals(t1, t2)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equals(T[] array1, T[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (!equals(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(boolean[] array1, boolean[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(byte[] array1, byte[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(char[] array1, char[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(int[] array1, int[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(long[] array1, long[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(float[] array1, float[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double[] array1, double[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0, size = array1.length; i < size; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static <T> void swap(List<T> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    public static <T> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(boolean[] array, int i, int j) {
        boolean tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(byte[] array, int i, int j) {
        byte tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(char[] array, int i, int j) {
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(long[] array, int i, int j) {
        long tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(float[] array, int i, int j) {
        float tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static <T> void swap(double[] array, int i, int j) {
        double tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static int getIndexOf(boolean[] array, boolean target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(boolean[] array, boolean target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(byte[] array, byte target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(byte[] array, byte target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(char[] array, char target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(char[] array, char target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(int[] array, int target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(int[] array, int target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(float[] array, float target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(float[] array, float target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(double[] array, double target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(double[] array, double target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static int getIndexOf(long[] array, long target) {
        return getIndexOf(array, target, 0);
    }

    public static int getIndexOf(long[] array, long target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (target == array[i]) {
                return i;
            }
        }

        return -1;
    }

    public static <T> int getIndexOf(T[] array, T target) {
        return getIndexOf(array, target, 0);
    }

    public static <T> int getIndexOf(T[] array, T target, int startIndex) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (equals(target, array[i])) {
                return i;
            }
        }

        return -1;
    }


    public static <T> int getIndexOf(Iterable<T> iterable, T target) {
        return getIndexOf(iterable, target, 0);
    }

    public static <T> int getIndexOf(Iterable<T> iterable, T target, int startIndex) {
        if (iterable == null) {
            return -1;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                if (equals(target, list.get(i))) {
                    return i;
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (equals(target, t)) {
                    return index;
                }
            }
        }

        return -1;
    }

    public static int getIndexOf(boolean[] array, Matcher<Boolean> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(boolean[] array, int startIndex, Matcher<Boolean> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(byte[] array, Matcher<Byte> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(byte[] array, int startIndex, Matcher<Byte> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(char[] array, Matcher<Character> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(char[] array, int startIndex, Matcher<Character> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(int[] array, Matcher<Integer> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(int[] array, int startIndex, Matcher<Integer> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(long[] array, Matcher<Long> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(long[] array, int startIndex, Matcher<Long> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(float[] array, Matcher<Float> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(float[] array, int startIndex, Matcher<Float> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int getIndexOf(double[] array, Matcher<Double> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static int getIndexOf(double[] array, int startIndex, Matcher<Double> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int getIndexOf(T[] array, Matcher<T> matcher) {
        return getIndexOf(array, 0, matcher);
    }

    public static <T> int getIndexOf(T[] array, int startIndex, Matcher<T> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = 0, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int getIndexOf(Iterable<T> iterable, Matcher<T> matcher) {
        return getIndexOf(iterable, 0, matcher);
    }

    public static <T> int getIndexOf(Iterable<T> iterable, int startIndex, Matcher<T> matcher) {
        if (isOutOfIndex(iterable, startIndex)) {
            return -1;
        }
        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                if (matcher.isMatch(list.get(i))) {
                    return i;
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t)) {
                    return index;
                }
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(boolean[] array, V target, Matcher2<Boolean, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(boolean[] array, int startIndex, V target, Matcher2<Boolean, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(byte[] array, V target, Matcher2<Byte, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(byte[] array, int startIndex, V target, Matcher2<Byte, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(char[] array, V target, Matcher2<Character, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(char[] array, int startIndex, V target, Matcher2<Character, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(int[] array, V target, Matcher2<Integer, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(int[] array, int startIndex, V target, Matcher2<Integer, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(long[] array, V target, Matcher2<Long, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(long[] array, int startIndex, V target, Matcher2<Long, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(float[] array, V target, Matcher2<Float, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(float[] array, int startIndex, V target, Matcher2<Float, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <V> int getIndexOf(double[] array, V target, Matcher2<Double, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <V> int getIndexOf(double[] array, int startIndex, V target, Matcher2<Double, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <T, V> int getIndexOf(T[] array, V target, Matcher2<T, V> matcher) {
        return getIndexOf(array, 0, target, matcher);
    }

    public static <T, V> int getIndexOf(T[] array, int startIndex, V target, Matcher2<T, V> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return -1;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            if (matcher.isMatch(array[i], target)) {
                return i;
            }
        }

        return -1;
    }

    public static <T, V> int getIndexOf(Iterable<T> iterable, V target, Matcher2<T, V> matcher) {
        return getIndexOf(iterable, 0, target, matcher);
    }

    public static <T, V> int getIndexOf(Iterable<T> iterable, int startIndex, V target, Matcher2<T, V> matcher) {
        if (isOutOfIndex(iterable, startIndex)) {
            return -1;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int index = startIndex, size = list.size(); index < size; index++) {
                if (matcher.isMatch(list.get(index), target)) {
                    return index;
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t, target)) {
                    return index;
                }
            }
        }

        return -1;
    }

    public static <T> T find(T[] array, Matcher<T> matcher) {
        return find(array, 0, matcher);
    }

    public static <T> T find(T[] array, int startIndex, Matcher<T> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return null;
        }
        for (int i = startIndex, size = array.length; i < size; i++) {
            T t = array[i];
            if (matcher.isMatch(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> T find(Iterable<T> iterable, Matcher<T> matcher) {
        return find(iterable, 0, matcher);
    }

    public static <T> T find(Iterable<T> iterable, int startIndex, Matcher<T> matcher) {
        if (isOutOfIndex(iterable, startIndex)) {
            return null;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                T t = list.get(i);
                if (matcher.isMatch(t)) {
                    return t;
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t)) {
                    return t;
                }
            }
        }

        return null;
    }

    public static <T, K> T find(Iterable<T> iterable, K key, Matcher2<T, K> matcher) {
        return find(iterable, 0, key, matcher);
    }

    public static <T, K> T find(Iterable<T> iterable, int startIndex, K key, Matcher2<T, K> matcher) {
        if (isOutOfIndex(iterable, startIndex)) {
            return null;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                T t = list.get(i);
                if (matcher.isMatch(t, key)) {
                    return t;
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t, key)) {
                    return t;
                }
            }
        }

        return null;
    }

    public static <T, K> T find(T[] array, K key, Matcher2<T, K> matcher) {
        return find(array, 0, key, matcher);
    }

    public static <T, K> T find(T[] array, int startIndex, K key, Matcher2<T, K> matcher) {
        if (isOutOfIndex(array, startIndex)) {
            return null;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            T t = array[i];
            if (matcher.isMatch(t, key)) {
                return t;
            }
        }

        return null;
    }

    public static <T> SearchResult<T> find(T[][] array2, Matcher<T> matcher) {
        return find(array2, 0, 0, matcher);
    }

    public static <T> SearchResult<T> find(T[][] array2, int startIndex0, int startIndex1, Matcher<T> matcher) {
        if (isOutOfIndex(array2, startIndex0)) {
            return null;
        }
        for (int index0 = startIndex0, size0 = array2.length; index0 < size0; index0++) {
            T[] array = array2[index0];
            if (isOutOfIndex(array, startIndex1)) {
                continue;
            }
            for (int index1 = startIndex1, size1 = array.length; index1 < size1; index1++) {
                T t = array[index1];
                if (matcher.isMatch(t)) {
                    return new SearchResult<>(t, index0, index1);
                }
            }
        }
        return null;
    }

    public static <T, K> SearchResult<T> find(T[][] array2, K key, Matcher2<T, K> matcher) {
        return find(array2, 0, 0, key, matcher);
    }

    public static <T, K> SearchResult<T> find(T[][] array2, int startIndex0, int startIndex1, K key, Matcher2<T, K> matcher) {
        if (isOutOfIndex(array2, startIndex0)) {
            return null;
        }
        for (int index0 = startIndex0, size0 = array2.length; index0 < size0; index0++) {
            T[] array = array2[index0];
            if (isOutOfIndex(array, startIndex1)) {
                continue;
            }
            for (int index1 = startIndex1, size1 = array.length; index1 < size1; index1++) {
                T t = array[index1];
                if (matcher.isMatch(t, key)) {
                    return new SearchResult<>(t, index0, index1);
                }
            }
        }
        return null;
    }

    public static <T> ArrayList<T> findAll(T[] array, Matcher<T> matcher) {
        return findAll(array, 0, matcher);
    }

    public static <T> ArrayList<T> findAll(T[] array, int startIndex, Matcher<T> matcher) {
        ArrayList<T> result = new ArrayList<>();

        if (isOutOfIndex(array, startIndex)) {
            return result;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            T t = array[i];
            if (matcher.isMatch(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T> ArrayList<T> findAll(Iterable<T> iterable, Matcher<T> matcher) {
        return findAll(iterable, 0, matcher);
    }

    public static <T> ArrayList<T> findAll(Iterable<T> iterable, int startIndex, Matcher<T> matcher) {
        ArrayList<T> result = new ArrayList<>();

        if (isOutOfIndex(iterable, startIndex)) {
            return result;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                T t = list.get(i);
                if (matcher.isMatch(t)) {
                    result.add(t);
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t)) {
                    result.add(t);
                }
            }
        }

        return result;
    }

    public static <T, K> ArrayList<T> findAll(T[] array, K key, Matcher2<T, K> matcher) {
        return findAll(array, 0, key, matcher);
    }

    public static <T, K> ArrayList<T> findAll(T[] array, int startIndex, K key, Matcher2<T, K> matcher) {
        ArrayList<T> result = new ArrayList<>();

        if (isOutOfIndex(array, startIndex)) {
            return result;
        }

        for (int i = startIndex, size = array.length; i < size; i++) {
            T t = array[i];
            if (matcher.isMatch(t, key)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T, K> ArrayList<T> findAll(Iterable<T> iterable, K key, Matcher2<T, K> matcher) {
        return findAll(iterable, 0, key, matcher);
    }

    public static <T, K> ArrayList<T> findAll(Iterable<T> iterable, int startIndex, K key, Matcher2<T, K> matcher) {
        ArrayList<T> result = new ArrayList<>();

        if (isOutOfIndex(iterable, startIndex)) {
            return result;
        }

        if (iterable instanceof List && iterable instanceof RandomAccess) {
            List<T> list = (List<T>) iterable;
            for (int i = startIndex, size = list.size(); i < size; i++) {
                T t = list.get(i);
                if (matcher.isMatch(t, key)) {
                    result.add(t);
                }
            }
        } else {
            int index = -1;
            for (T t : iterable) {
                index++;
                if (index < startIndex) {
                    continue;
                }
                if (matcher.isMatch(t, key)) {
                    result.add(t);
                }
            }
        }

        return result;
    }

    public static <T> void filter(Iterable<T> iterable, Matcher<T> matcher) {
        if (isEmpty(iterable)) {
            return;
        }
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (!matcher.isMatch(t)) {
                iterator.remove();
            }
        }
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

    public static <T> void move(T[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            T temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            T temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(boolean[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            boolean temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            boolean temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(byte[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            byte temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            byte temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(char[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            char temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            char temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(int[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            int temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            int temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(long[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            long temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            long temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(float[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            float temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            float temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void move(double[] array, int fromIndex, int toIndex) {
        if (isOutOfIndex(array, fromIndex)) {
            return;
        }

        if (isOutOfIndex(array, toIndex)) {
            return;
        }

        if (fromIndex == toIndex) {
            return;
        }

        if (fromIndex > toIndex) {
            double temp = array[fromIndex];
            System.arraycopy(array, toIndex, array, toIndex + 1, fromIndex - toIndex);
            array[toIndex] = temp;
        } else {
            double temp = array[fromIndex];
            System.arraycopy(array, fromIndex + 1, array, fromIndex, toIndex - fromIndex);
            array[toIndex] = temp;
        }
    }

    public static <T> void distinct(Iterable<T> iterable) {
        distinct(iterable, false);
    }

    public static <T> void distinct(Iterable<T> collection, boolean keepNull) {
        if (isEmpty(collection)) {
            return;
        }
        HashSet<T> set = new HashSet<>();

        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (t == null && !keepNull) {
                iterator.remove();
            }
            if (set.contains(t)) {
                iterator.remove();
                continue;
            }
            set.add(t);
        }
    }

    public static <T, K> void distinct(Collection<T> collection, Extractor<T, K> extractor) {
        distinct(collection, false, extractor);
    }

    public static <T, K> void distinct(Collection<T> collection, boolean keepNull, Extractor<T, K> extractor) {
        if (isEmpty(collection)) {
            return;
        }
        HashSet<K> set = new HashSet<>();

        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            K k = extractor.extract(t);
            if (k == null && !keepNull) {
                iterator.remove();
            }
            if (set.contains(k)) {
                iterator.remove();
                continue;
            }
            set.add(k);
        }
    }


    public static <T> ArrayList<T> createDistinctList(Collection<T> collection) {
        return createDistinctList(collection, false);
    }

    public static <T> ArrayList<T> createDistinctList(Collection<T> collection, boolean keepNull) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }

        HashSet<T> set = new HashSet<>();

        for (T t : collection) {
            if (t == null && !keepNull) {
                continue;
            }
            if (set.contains(t)) {
                continue;
            }
            set.add(t);
            list.add(t);
        }

        return list;
    }

    public static <T, K> ArrayList<T> createDistinctList(Collection<T> collection, Extractor<T, K> extractor) {
        return createDistinctList(collection, false, extractor);
    }

    public static <T, K> ArrayList<T> createDistinctList(Collection<T> collection, boolean keepNull, Extractor<T, K> extractor) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }

        HashSet<K> set = new HashSet<>();

        for (T t : collection) {
            K key = extractor.extract(t);
            if (key == null && !keepNull) {
                continue;
            }

            if (set.contains(key)) {
                continue;
            }

            set.add(key);
            list.add(t);
        }
        set.clear();
        return list;
    }

    public static <T> ArrayList<T> merge(Collection<T>... collections) {
        return merge(false, collections);
    }

    public static <T> ArrayList<T> merge(boolean keepNull, Collection<T>... collections) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collections)) {
            return list;
        }
        HashSet<T> set = new HashSet<>();

        for (Collection<T> c : collections) {
            if (c == null) {
                continue;
            }
            for (T t : c) {
                if (t == null && !keepNull) {
                    continue;
                }
                if (set.contains(t)) {
                    continue;
                }
                list.add(t);
                set.add(t);
            }
        }
        return list;
    }

    public static <T, K> ArrayList<T> merge(Extractor<T, K> extractor, Collection<T>... collections) {
        return merge(extractor, false, collections);
    }

    public static <T, K> ArrayList<T> merge(Extractor<T, K> extractor, boolean keepNull, Collection<T>... collections) {
        return merge(collections, keepNull, extractor);
    }

    public static <T, K> ArrayList<T> merge(Collection<T>[] collections, Extractor<T, K> extractor) {
        return merge(collections, false, extractor);
    }

    public static <T, K> ArrayList<T> merge(Collection<T>[] collections, boolean keepNull, Extractor<T, K> extractor) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collections)) {
            return list;
        }
        HashSet<K> set = new HashSet<>();

        for (Collection<T> c : collections) {
            if (c == null) {
                continue;
            }
            for (T t : c) {
                K k = extractor.extract(t);
                if (k == null && !keepNull) {
                    continue;
                }
                if (set.contains(k)) {
                    continue;
                }
                list.add(t);
                set.add(k);
            }
        }
        return list;
    }

    public static <T> ArrayList<T> subcollectionInclude(Collection<T> collection, Collection<T> target) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }
        if (isEmpty(target)) {
            return list;
        }

        HashSet<T> set;
        if (target instanceof HashSet) {
            set = (HashSet<T>) target;
        } else {
            set = new HashSet<>(target);
        }

        for (T t : collection) {
            if (set.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T, K> ArrayList<T> subcollectionInclude(Collection<T> collection, Collection<K> target, Extractor<T, K> extractor) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }
        if (isEmpty(target)) {
            return list;
        }

        HashSet<K> set;
        if (target instanceof HashSet) {
            set = (HashSet<K>) target;
        } else {
            set = new HashSet<>(target);
        }

        for (T t : collection) {
            K k = extractor.extract(t);
            if (set.contains(k)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T> ArrayList<T> subCollectionExclude(Collection<T> collection, Collection<T> target) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }
        if (isEmpty(target)) {
            list.addAll(collection);
            return list;
        }

        HashSet<T> set;
        if (target instanceof HashSet) {
            set = (HashSet<T>) target;
        } else {
            set = new HashSet<>(target);
        }

        for (T t : collection) {
            if (!set.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T, K> ArrayList<T> subCollectionExclude(Collection<T> collection, Collection<K> target, Extractor<T, K> extractor) {
        ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection)) {
            return list;
        }
        if (isEmpty(target)) {
            list.addAll(collection);
            return list;
        }

        HashSet<K> set;
        if (target instanceof HashSet) {
            set = (HashSet<K>) target;
        } else {
            set = new HashSet<>(target);
        }

        for (T t : collection) {
            K k = extractor.extract(t);
            if (!set.contains(k)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T> void traversal(Collection<T> collection, Processor<T> processor) {
        if (isEmpty(collection)) {
            return;
        }

        int index = -1;
        for (T t : collection) {
            index++;
            processor.process(index, t);
        }
    }

    public static <T> void traversal(T[] array, Processor<T> processor) {
        if (isEmpty(array)) {
            return;
        }

        int index = -1;
        for (T t : array) {
            index++;
            processor.process(index, t);
        }
    }

    public static <T> void traversal(List<T> list, Processor<T> processor) {
        if (isEmpty(list)) {
            return;
        }
        int index = -1;
        for (T t : list) {
            index++;
            processor.process(index, t);
        }
    }

    public static <T, K> HashSet<K> extractSet(Collection<T> collection, Extractor<T, K> extractor) {
        return extractSet(collection, false, extractor);
    }

    public static <T, K> HashSet<K> extractSet(Collection<T> collection, boolean keepNull, Extractor<T, K> extractor) {
        HashSet<K> set = new HashSet<>();

        if (isEmpty(collection)) {
            return set;
        }

        for (T t : collection) {
            K key = extractor.extract(t);
            if (key == null && !keepNull) {
                continue;
            }

            set.add(key);
        }

        return set;
    }

    public static <T, K> ArrayList<K> extractList(Collection<T> collection, Extractor<T, K> extractor) {
        return extractList(collection, false, extractor);
    }

    public static <T, K> ArrayList<K> extractList(Collection<T> collection, boolean keepNull, Extractor<T, K> extractor) {
        ArrayList<K> list = new ArrayList<>();

        if (isEmpty(collection)) {
            return list;
        }

        for (T t : collection) {
            K key = extractor.extract(t);
            if (key == null && !keepNull) {
                continue;
            }

            list.add(key);
        }

        return list;
    }

    public static <T> Range getSizeRange(T[][] arrays) {
        boolean initialized = false;
        int min = 0;
        int max = 0;


        for (T[] array : arrays) {
            int length = getSize(array);
            if (initialized) {
                min = Math.min(length, min);
                max = Math.max(length, max);
            } else {
                min = length;
                max = length;
                initialized = true;
            }
        }

        if (!initialized) {
            throw new IllegalStateException("no array found");
        }

        return new Range(min, max);
    }

    public static <T> int getMinSize(T[][] arrays) {
        return getSizeRange(arrays).getMin();
    }

    public static <T> int getMaxSize(T[][] arrays) {
        return getSizeRange(arrays).getMax();
    }

    public static <T> T findCommonElement(T[][] arrays) {
        return findCommonElement(arrays, 0);
    }

    public static <T> T findCommonElement(T[][] arrays, int startIndex) {
        if (isEmpty(arrays)) {
            return null;
        }

        int minSize = getMinSize(arrays);
        if (minSize == 0) {
            return null;
        }

        T common = null;

        for (int index = startIndex; index < minSize; index++) {
            T tmp = null;
            boolean initialized = false;
            boolean sameElements = false;

            for (T[] array : arrays) {
                T t = array[index];
                if (t == null) {
                    return common;
                }

                if (!initialized) {
                    tmp = t;
                    initialized = true;
                    continue;
                }

                if (tmp != null && !tmp.equals(t)) {
                    return common;
                }
            }

            common = tmp;
        }

        return common;
    }

    public static <T> T findLastCommonElement(T[][] arrays) {
        return findLastCommonElement(arrays, 0);
    }

    public static <T> T findLastCommonElement(T[][] arrays, int startIndex) {
        if (isEmpty(arrays)) {
            return null;
        }

        int minSize = getMinSize(arrays);
        if (minSize == 0) {
            return null;
        }

        T common = null;

        for (int index = startIndex; index < minSize; index++) {
            T tmp = null;
            boolean initialized = false;

            for (T[] array : arrays) {
                T t = array[index];
                if (t == null) {
                    return common;
                }

                if (!initialized) {
                    tmp = t;
                    initialized = true;
                    continue;
                }

                if (tmp != null && !tmp.equals(t)) {
                    return common;
                }
            }

            common = tmp;
        }

        return common;
    }

    public static <T> ArrayList<T> clone(Iterable<T> src, CloneFactory<T> factory) {
        ArrayList<T> cloneList = new ArrayList<>();

        if (src == null) {
            return cloneList;
        }

        for (T t : src) {
            T clone = factory.clone(t);
            cloneList.add(clone);
        }

        return cloneList;
    }

    public static int compare(boolean x, boolean y) {
        return Boolean.compare(x, y);
    }

    public static int compare(byte x, byte y) {
        return Byte.compare(x, y);
    }

    public static int compare(char x, char y) {
        return Character.compare(x, y);
    }

    public static int compare(int x, int y) {
        return Integer.compare(x, y);
    }

    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    public static int compare(float x, float y) {
        return Float.compare(x, y);
    }

    public static int compare(double x, double y) {
        return Double.compare(x, y);
    }

    public static <K, V> ArrayList<Map.Entry<K, V>> getEntryList(Map<K, V> map) {
        ArrayList<Map.Entry<K, V>> entryList = new ArrayList<>();
        if (isEmpty(map)) {
            return entryList;
        }

        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        if (!isEmpty(entrySet)) {
            entryList.addAll(entrySet);
        }
        return entryList;
    }

    public static <T, E> ArrayList<E> convert(Iterable<T> srcList, ListConverter<T, E> listConverter) {
        ArrayList<E> dstList = new ArrayList<>();
        if (srcList == null) {
            return dstList;
        }
        int i = 0;
        for (T t : srcList) {
            dstList.add(listConverter.convert(i, t));
            i++;
        }

        return dstList;
    }

    public static <T, E> void convert(T[] srcArray, E[] dstArray, ListConverter<T, E> listConverter) {
        if (isEmpty(srcArray) || isEmpty(dstArray)) {
            return;
        }

        for (int i = 0, size = Math.min(srcArray.length, dstArray.length); i < size; i++) {
            T t = srcArray[i];
            dstArray[i] = listConverter.convert(i, t);
        }
    }

    public static <T> T[] concat(T[]... arrays) {
        SearchResult<T> result = find(arrays, new Matcher<T>() {
            @Override
            public boolean isMatch(T t) {
                return t != null;
            }
        });

        if (result == null) {
            return null;
        }
        Class<T> cls = (Class<T>) result.getResult().getClass();

        return concat(cls, arrays);
    }

    public static <T> T[] concat(Class<T> cls, T[]... arrays) {
        int size = getSize(arrays);
        if (size == 0) {
            return (T[]) Array.newInstance(cls, 0);
        }


        int length = 0;

        for (T[] other : arrays) {
            length += getSize(other);
        }

        T[] result = (T[]) Array.newInstance(cls, length);
        int index = 0;

        for (T[] array : arrays) {
            if (isEmpty(array)) {
                continue;
            }
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }

        return result;
    }

    public static <T> boolean contains(T[] container, T[] elements) {
        if (isEmpty(elements)) {
            return true;
        }
        if (isEmpty(container)) {
            return false;
        }
        for (int i = 0, elementSize = getSize(elements); i < elementSize; i++) {
            T element = elements[i];
            if (!contains(container, element)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean contains(T[] container, T element) {
        if (isEmpty(container)) {
            return false;
        }
        for (int i = 0, size = getSize(container); i < size; i++) {
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

    public static <T> List<T> listOf(T... array) {
        if (isEmpty(array)) {
            return new ArrayList<>();
        }

        return Arrays.asList(array);
    }

    public static <T> T[] arrayOf(T... array) {
        return array;
    }

    public static <T> List<T> toList(T[] array) {
        return array == null ? new ArrayList<T>() : new ArrayList<>(Arrays.asList(array));
    }

    public static <T> T[] toArray(Iterable<T> iterable) {
        T t = find(iterable, new Matcher<T>() {
            @Override
            public boolean isMatch(T t) {
                return t != null;
            }
        });
        if (t == null) {
            return null;
        }
        return toArray(iterable, (Class<T>) t.getClass());
    }

    public static <T> T[] toArray(Iterable<T> iterable, Class<T> cls) {
        int size = getSize(iterable);
        T[] array = (T[]) Array.newInstance(cls, size);
        if (size == 0) {
            return array;
        }

        if (iterable instanceof List) {
            return ((List<T>) iterable).toArray(array);
        }

        int index = -1;
        for (T t : iterable) {
            index++;
            array[index] = t;
        }

        return array;
    }
}