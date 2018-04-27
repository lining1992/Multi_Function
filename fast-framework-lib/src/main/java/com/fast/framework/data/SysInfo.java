/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.data;

import com.fast.framework.FastApplication;
import com.fast.framework.util.ApkUtil;

import android.os.Build;

/**
 * 系统信息
 * <p>
 * Created by lishicong on 2017/6/9.
 */

public class SysInfo {

    private static SysInfo instance;

    private String channel; // 渠道号
    private String device; // 平台
    private String packageName; // 包名
    private String model; // 设备型号
    private int sdkVersion; // android sdk version
    private int appVersionCode; // app 版本号
    private String appVersionName; // app 版本名

    private SysInfo() {
        channel = ApkUtil.getAppChannel(FastApplication.getContext());
        device = "Android";
        packageName = ApkUtil.getAppPackageName(FastApplication.getContext());
        model = Build.MODEL;
        sdkVersion = Build.VERSION.SDK_INT;
        appVersionCode = ApkUtil.getAppVersionCode(FastApplication.getContext());
        appVersionName = ApkUtil.getAppVersionName(FastApplication.getContext());
    }

    public static SysInfo getInstance() {
        if (instance == null) {
            synchronized (SysInfo.class) {
                if (instance == null) {
                    instance = new SysInfo();
                }
            }
        }
        return instance;
    }

    public String getChannel() {
        return channel;
    }

    public String getDevice() {
        return device;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getModel() {
        return model;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }
}
