package com.example.commonlibrary.collection;

/**
 * @author : leixing
 * @date : 2018/6/22 17:39
 * <p>
 * description : Extractor
 */
public interface ListConverter<T, E> {
    E convert(int index, T t);
}
