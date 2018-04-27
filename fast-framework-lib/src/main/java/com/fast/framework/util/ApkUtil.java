/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import com.fast.framework.support.L;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * App信息工具类
 * <p>
 * Created by lishicong on 2017/6/7.
 */
public class ApkUtil {

    /**
     * 获取App包名
     *
     * @param context
     *
     * @return
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取App版本号
     *
     * @param context
     *
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("ApkUtil getAppVersionCode error");
        }
        return versionCode;
    }

    /**
     * 获取App版本名
     *
     * @param context
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("ApkUtil getAppVersionName error");
        }
        return versionName;
    }

    /**
     * 获取App渠道名
     *
     * @param context
     *
     * @return
     */
    public static String getAppChannel(Context context) {
        return getAppMetaData(context, "channel");
    }

    /**
     * 获取meta信息
     *
     * @param context
     * @param metaName
     *
     * @return
     */
    public static String getAppMetaData(Context context, String metaName) {
        ApplicationInfo ai = null;

        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = ai.metaData;

        return bundle.getString(metaName);
    }

}
