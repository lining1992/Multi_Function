package com.baidu.lining.displayadapter;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.base.delegate.AppDelegate;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.Preconditions;

import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/4/28.
 */
public class App extends Application implements com.jess.arms.base.App {

    private static RequestQueue queues;
    public static RequestQueue getHttpQueues(){
        return queues;
    }
    private AppLifecycles mAppDelegate;
    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }
        SMSSDK.initSDK(this, "1d6e2e4002a7b", "04f6de449a013d97c765d34649ee7e1d");

        queues = Volley.newRequestQueue(getApplicationContext());
        mInstance = this;
    }

    public static App getInstance() {

        return mInstance;
    }

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }
        this.mAppDelegate.attachBaseContext(base);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @see ArmsUtils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     * @return AppComponent
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof com.jess.arms.base.App, "%s must be implements %s", mAppDelegate.getClass().getName(), com.jess.arms.base.App.class.getName());
        return ((com.jess.arms.base.App) mAppDelegate).getAppComponent();
    }
}
