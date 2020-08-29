package com.example.commonlibrary;

import android.app.Application;

import com.example.commonlibrary.manager.ApplicationManager;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationManager.getInstance().init(this);
    }
}
