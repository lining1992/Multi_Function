/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类.
 * <p>
 * Created by lishicong on 2016/10/24.
 */
public class PrefUtil {

    private PrefUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查key是否存在
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static boolean contains(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.contains(key);
    }

    /**
     * 清除shared preferences所有数据
     *
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 保存key-value
     *
     * @param context
     * @param key
     * @param obj
     */
    public static void setPref(Context context, String key, Object obj) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = pref.edit();

        String value = String.valueOf(obj);

        if (obj instanceof Boolean) {
            edit.putBoolean(key, Boolean.valueOf(value));
        } else if (obj instanceof Float) {
            edit.putFloat(key, Float.valueOf(value));
        } else if (obj instanceof Integer) {
            edit.putInt(key, Integer.valueOf(value));
        } else if (obj instanceof Long) {
            edit.putLong(key, Long.valueOf(value));
        } else if (obj instanceof String) {
            edit.putString(key, value);
        }

        edit.commit();
    }

    /**
     * 获取boolean类型的value
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static boolean getBooleanPref(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(key, false);
    }

    /**
     * 获取float类型的value
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static float getFloatPref(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getFloat(key, 0.0f);
    }

    /**
     * 获取int类型的value
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static int getIntPref(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(key, 0);
    }

    /**
     * 获取long类型的value
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static long getLongPref(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getLong(key, 0);
    }

    /**
     * 获取string类型的value
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static String getPref(Context context, String key) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, null);
    }
}
