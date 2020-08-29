package com.example.commonlibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.commonlibrary.manager.ApplicationManager;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class LogUtil {

    public static String TAG = "commonLog";
    public static String versionName = "";

    private static boolean isLogEnabled = true;

    public static boolean isLogEnabled() {
        return isLogEnabled;
    }

    public static void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }

    /**
     * 调试
     *
     * @param msg 内容
     */
    public static void d(String msg) {
        d(null, msg);
    }

    /**
     * 调试
     *
     * @param tag
     * @param msg 内容
     */
    public static void d(String tag, String msg) {
        if (isLogEnabled()) {
            if (tag == null || "".equals(tag)) {
                tag = TAG;
            }
            msg = getLogMsg(msg);
            Log.d(tag, msg);
//            LogFileUtil.d(tag, msg);
        }
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (isLogEnabled()) {
            if (tag == null || "".equals(tag)) {
                tag = TAG;
            }
            msg = getLogMsg(msg);
            Log.i(tag, msg);
//            LogFileUtil.i(tag, msg);
        }
    }


    /**
     * 错误
     *
     * @param exceptionStr 内容
     */
    public static void e(String exceptionStr) {
        if (isLogEnabled()) {
            e(null, exceptionStr, null);
        }
    }

    /**
     * 错误
     *
     * @param tag
     * @param exceptionStr 内容
     */
    public static void e(String tag, String exceptionStr) {
        if (isLogEnabled()) {
            e(tag, exceptionStr, null);
        }
    }


    /**
     * 错误
     *
     * @param exception 内容
     */
    public static void e(Throwable exception) {
        if (isLogEnabled()) {
            e(null, exception);
        }
    }

    /**
     * 错误
     *
     * @param exception 内容
     */
    public static void e(String tag, Throwable exception) {
        if (isLogEnabled()) {
            e(tag, null, exception);
        }
    }

    /**
     * 错误
     *
     * @param exception 内容
     */
    public static void e(String tag, String exceptionStr, Throwable exception) {
        if (tag == null || "".equals(tag)) {
            tag = TAG;
        }
        String errorStr = getExceptionStr(exceptionStr, exception);
        if (isLogEnabled()) {
//            while (errorStr.length() > 4000) {
//                Log.e(tag, errorStr.substring(0, 4000));
//                errorStr = errorStr.substring(4000);
//            }
            errorStr = getLogMsg(errorStr);

            Log.e(tag, errorStr);
//            LogFileUtil.e(tag, errorStr);
        }
    }

    /**
     * 打印日志
     *
     * @param e
     * @return
     */
    public static String getExceptionStr(String exceptionStr, Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (exceptionStr != null) {
            sb.append(exceptionStr);
        }
        if (e != null) {
            StackTraceElement[] stack = e.getStackTrace();
            sb.append(e.toString()).append("\n");
            for (int i = 0; i < stack.length; i++) {
                sb.append("at ");
                sb.append(stack[i].toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String getLogMsg(String msg) {
        try {
            if (TextUtils.isEmpty(versionName)) {
                Context context = ApplicationManager.getInstance().getContext();
                if (context != null) {
                    String str = ApkUtil.getAppVersionName(context);
                    if (!TextUtils.isEmpty(str)) {
                        versionName = str;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }

        if (TextUtils.isEmpty(versionName)) {
            return msg;
        } else {
            return versionName + ": " + msg;
        }

    }
}
