package com.example.mytest.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Daisw on 16/9/18.
 */

public class GenericUtil {

    // getGenericClass(getClass(), 0); -> <Object> -> Object.class
    // getGenericClass(getClass(), 0, 0); -> <Object1> or <List<Object2>> -> Object1.class or Object2.class
    // getGenericClass(getClass(), 1, 0); -> <Integer, Object1> or <List<Object2>> -> Object1.class or Object2.class
    // getGenericClass(getClass(), 1, 1); -> <Integer, Object1> or <Map<String, Object2>> -> Object1.class or Object2.class
    // getGenericClass(getClass(), 1, 1, 0); -> <Integer, Object1> or <Map<String, <List<Object2>>> -> Object1.class or Object2.class
    // ...
    public static Class<?> getGenericClass(Class<?> inputClass, int... indexs) {

        if (inputClass == null)
            return null;

        ParameterizedType parameterizedType = (ParameterizedType) inputClass.getGenericSuperclass();
        return xxx(parameterizedType, indexs, 0);
    }

    private static Class<?> xxx(ParameterizedType parameterizedType, int[] indexs, int i) {

        Type[] types = parameterizedType.getActualTypeArguments();
        Type type = types[indexs[i]];

        if (type instanceof Class<?>) {

            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {

            return xxx((ParameterizedType) type, indexs, i++);
        } else {

            return null;
        }
    }
}
