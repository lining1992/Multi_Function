package com.example.mytest.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.joy.ui.BaseApplication;


/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class AppUtil {

    /**
     * 获取应用第一次安装时间
     *
     * @return
     */
    public static long getInstallTime() {

        long firstInstallTime = 0;

        try {

            Context ctx = BaseApplication.getContext();
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            firstInstallTime = packageInfo.firstInstallTime;// 应用第一次安装的时间

        } catch (Exception e) {

            e.printStackTrace();
        }

        return firstInstallTime;
    }
}