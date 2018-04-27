package com.example.mytest.utils;

import android.util.Log;


public class LogMgr {

    private static final String TAG = "LogMgr";
    public static boolean DEBUG = true;

    public static void v(String log) {

        v(TAG, log);
    }

    public static void v(String tag, String log) {

        if (DEBUG)
            Log.v(tag, log);
    }

    public static void d(String log) {

        d(TAG, log);
    }

    public static void d(String tag, String log) {

        if (DEBUG)
            Log.d(tag, log);
    }

    public static void i(String log) {

        i(TAG, log);
    }

    public static void i(String tag, String log) {

        if (DEBUG)
            Log.i(tag, log);
    }

    public static void w(String log) {

        w(TAG, log);
    }

    public static void w(String tag, String log) {

        if (DEBUG)
            Log.w(tag, log);
    }

    public static void e(String log) {

        e(TAG, log);
    }

    public static void e(String tag, String log) {

        if (DEBUG)
            Log.e(tag, log);
    }
}
