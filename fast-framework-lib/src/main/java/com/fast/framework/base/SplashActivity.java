/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.base;

import com.fast.framework.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 闪屏界面，进入应用的第一个activity界面可以继承此类.
 * <p>
 * Created by lishicong on 16/8/17.
 */
public abstract class SplashActivity extends BaseActivity {

    // 闪屏的时间--3秒
    private static final long SPLASH_DELAY_MILLIS = 3000L;

    protected RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_splash, mBaseLayout);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.a_splash_rl);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                into();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    /**
     * 进入应用
     */
    protected abstract void into();
}
