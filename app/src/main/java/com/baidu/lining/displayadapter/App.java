package com.baidu.lining.displayadapter;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/4/28.
 */
public class App extends Application{

    private static RequestQueue queues;

    public static RequestQueue getHttpQueues(){
        return queues;
    }

    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1d6e2e4002a7b", "04f6de449a013d97c765d34649ee7e1d");

        queues = Volley.newRequestQueue(getApplicationContext());
        mInstance = this;
    }

    public static App getInstance() {

        return mInstance;
    }
}
