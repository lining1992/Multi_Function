//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {
    public static final String SP_NAME = "codriver_setting";

    public SharePreferenceUtil() {
    }

    public static boolean getBoolean(Context context, String key, boolean dValue) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        boolean result = preferences.getBoolean(key, dValue);
        return result;
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String dValue) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        String result = preferences.getString(key, dValue);
        return result;
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long dValue) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        long result = preferences.getLong(key, dValue);
        return result;
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key, int dValue) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        int result = preferences.getInt(key, dValue);
        return result;
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("codriver_setting", 0);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
