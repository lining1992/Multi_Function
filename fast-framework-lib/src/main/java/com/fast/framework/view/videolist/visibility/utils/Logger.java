/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.visibility.utils;


import android.util.Log;

public class Logger {

    public static int err(final String tag, final String message) {
        return Log.e(tag, message);
    }

    public static int err(final String tag, final String message, Throwable throwable) {
        return Log.e(tag, message, throwable);
    }

    public static int w(final String tag, final String message) {
        return Log.w(tag, message);
    }

    public static int inf(final String tag, final String message) {
        return Log.i(tag, message);
    }

    public static int d(final String tag, final String message) {
        return Log.d(tag, message);
    }

    public static int v(final String tag, final String message) {
        return Log.v(tag, message);
    }
}
