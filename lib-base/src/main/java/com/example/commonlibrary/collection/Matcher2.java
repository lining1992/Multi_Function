package com.example.commonlibrary.collection;

/**
 * @author : leixing
 * @date : 2018/6/22 19:05
 * <p>
 * description : matcher
 */
public interface Matcher2<E, T> {
    boolean isMatch(E e, T t);
}
