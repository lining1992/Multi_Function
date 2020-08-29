package com.example.commonlibrary.manager;

import android.app.Application;
import android.content.Context;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class ApplicationManager {

    private static volatile ApplicationManager INSTANCE;
    private Application mApplication;

    public static ApplicationManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ApplicationManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationManager();
                }
            }
        }
        return INSTANCE;
    }

    private ApplicationManager() {

    }

    public void init(Application application) {
        mApplication = application;
    }

    public Context getContext() {
        if (mApplication != null) {
            return mApplication.getApplicationContext();
        } else {
            return null;
        }
    }
}
