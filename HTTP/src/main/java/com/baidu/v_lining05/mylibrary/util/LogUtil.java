//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.util;

import android.annotation.SuppressLint;
import android.util.Log;

public class LogUtil {
    public static final String TAG = "CoDriver_TAG";
    private static int LOG_LEVEL = 2;

    public LogUtil() {
    }

    public static void setLogLevel(int logLevel) {
        LOG_LEVEL = logLevel;
    }

    public static void dumpException(Throwable t) {
        if(isLoggable(5)) {
            StringBuilder err = new StringBuilder(256);
            err.append("Got exception: ");
            err.append(t.toString());
            err.append("\n");
            System.out.println(err.toString());
            t.printStackTrace(System.out);
        }

    }

    public static void v(String aTag, String aMsg) {
        log(2, aTag, aMsg);
    }

    public static void v(String aTag, String aMsg, Throwable aThrowable) {
        log(2, aTag, aMsg, aThrowable);
    }

    public static void d(String aTag, String aMsg) {
        log(3, aTag, aMsg);
    }

    public static void d(String aTag, String aMsg, Throwable aThrowable) {
        log(3, aTag, aMsg, aThrowable);
    }

    public static void i(String aTag, String aMsg) {
        log(4, aTag, aMsg);
    }

    public static void i(String aTag, String aMsg, Throwable aThrowable) {
        log(4, aTag, aMsg, aThrowable);
    }

    public static void w(String aTag, String aMsg) {
        log(5, aTag, aMsg);
    }

    public static void w(String aTag, String aMsg, Throwable aThrowable) {
        log(5, aTag, aMsg, aThrowable);
    }

    public static void e(String aTag, String aMsg) {
        log(6, aTag, aMsg);
    }

    public static void e(String aTag, String aMsg, Throwable aThrowable) {
        log(6, aTag, aMsg, aThrowable);
    }

    public static void log(int aLogLevel, String aTag, String aMessage) {
        log(aLogLevel, aTag, aMessage, (Throwable)null);
    }

    @SuppressLint("LogTagMismatch")
    public static void log(int aLogLevel, String aTag, String aMessage, Throwable aThrowable) {
        if(isLoggable(aLogLevel)) {
            switch(aLogLevel) {
            case 2:
                Log.v("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
                break;
            case 3:
                Log.d("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
                break;
            case 4:
                Log.i("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
                break;
            case 5:
                Log.w("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
                break;
            case 6:
                Log.e("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
                break;
            default:
                Log.e("CoDriver_TAG", aTag + ": " + aMessage, aThrowable);
            }
        }

    }

    public static void method() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        if(null != stack && 2 <= stack.length) {
            StackTraceElement s = stack[1];
            if(null != s) {
                String className = s.getClassName();
                String methodName = s.getMethodName();
                d(className, "+++++" + methodName);
            }

        }
    }

    public static void enter() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        if(null != stack && 2 <= stack.length) {
            StackTraceElement s = stack[1];
            if(null != s) {
                String className = s.getClassName();
                String methodName = s.getMethodName();
                d(className, "====>" + methodName);
            }

        }
    }

    public static void leave() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        if(null != stack && 2 <= stack.length) {
            StackTraceElement s = stack[1];
            if(null != s) {
                String className = s.getClassName();
                String methodName = s.getMethodName();
                d(className, "<====" + methodName);
            }

        }
    }

    public static boolean isLoggable(int aLevel) {
        return aLevel >= LOG_LEVEL;
    }
}
