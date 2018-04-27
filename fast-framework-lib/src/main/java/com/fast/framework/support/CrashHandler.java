/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import com.fast.framework.util.DateUtil;
import com.fast.framework.util.FileUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

/**
 * 拦截捕获app crash，记录到文件中.
 * <p>
 * Created by lishicong on 2016/12/5.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler INSTANCE;

    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> mDeviceInfo = new TreeMap<String, String>();

    public void init(Context context) {

        this.mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {

            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(100);
                ex.printStackTrace();
            } catch (InterruptedException e) {
                L.e("uncaught exception:" + e.getMessage());
            }

            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return false;
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.",
                // Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        this.collectDeviceInfo();
        // 保存日志文件
        this.saveCrashInfo2File(ex);

        return true;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo() {

        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = null;

        try {
            pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            L.e("name not found exception:" + e.getMessage());
        }

        if (pi != null) {
            String versionName = pi.versionName == null ? "null" : pi.versionName;
            String versionCode = pi.versionCode + "";
            mDeviceInfo.put("versionName", versionName);
            mDeviceInfo.put("versionCode", versionCode);
        }

        Field[] fields = Build.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceInfo.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                L.e("illegal access exception:" + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     *
     * @return
     */
    private void saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();

        for (String key : mDeviceInfo.keySet()) {
            sb.append(key + "=" + mDeviceInfo.get(key) + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());

        long timestamp = System.currentTimeMillis();
        String date = DateUtil.formatter(timestamp, DateUtil.DATE_FORMAT_MMDDHHMMSS);

        // example: /sdcard/Android/data/your.package/cache/log/crash_1205162858.log

        String logFileName = "crash_" + date + ".log";

        try {
            FileUtil.writeByteToFile(sb.toString().getBytes(), FileUtil.getExternalCacheDir(mContext, "log"),
                                     logFileName);
        } catch (Exception e) {
            L.e("save crash info to file exceptin:" + e.getMessage());
        }
    }
}
