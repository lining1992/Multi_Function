/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import java.util.Locale;

import android.util.Log;

/**
 * 日志输出类
 * <p>
 * Created by lishicong on 2016/12/5.
 */

public class L {

    private static final String TAG = "FastFramework";
    private static final String TAG_LOG = " FFLOG |||(~_~).zZ ------>>> ";
    private static final boolean TAG_SAMPLE = true;

    // log trace 开关
    private static final boolean LOG_TRACE_ENABLED = true;

    private static final int VERBOSE = 5;
    private static final int DEBUG = 4;
    private static final int INFO = 3;
    private static final int WARN = 2;
    private static final int ERROR = 1;

    // 输出的log级别
    private static final int LEVEL = VERBOSE;

    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void v(String msg) {

        if (LOG_TRACE_ENABLED && LEVEL >= VERBOSE) {
            Log.v(TAG, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void d(String msg) {

        if (LOG_TRACE_ENABLED && LEVEL >= DEBUG) {
            Log.d(TAG, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void i(String msg) {

        if (LOG_TRACE_ENABLED && LEVEL >= INFO) {
            Log.i(TAG, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void w(String msg) {

        if (LOG_TRACE_ENABLED && LEVEL >= WARN) {
            Log.w(TAG, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void e(String msg) {

        if (LOG_TRACE_ENABLED && LEVEL >= ERROR) {
            Log.e(TAG, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    /**
     * 获取方法的调用栈信息，并格式化
     *
     * @return
     */
    private static String getCurrentStackTraceElement() {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[4];
        String className;
        if (TAG_SAMPLE) {
            className = trace.getClass().getSimpleName();
        } else {
            className = trace.getClassName();
        }
        return String.format(Locale.ENGLISH, "%s:%d", className, trace.getLineNumber());
    }

}
