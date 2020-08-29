package com.example.myapplication;

import android.app.Application;

import com.example.commonlibrary.net.RetrofitClient;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(getApplicationContext());
    }
}
