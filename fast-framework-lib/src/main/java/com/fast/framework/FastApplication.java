/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework;

import com.fast.framework.support.CrashHandler;

import android.app.Application;
import android.content.Context;

/**
 * Created by lishicong on 2016/12/5.
 */

public class FastApplication extends Application {

    private static Context mContext;

    static {
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        CrashHandler.getInstance().init(this);
    }

    public static Context getContext() {
        return mContext;
    }

}
