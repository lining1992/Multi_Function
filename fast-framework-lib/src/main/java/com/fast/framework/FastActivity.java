/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.fast.framework.base.EmptyFragment;
import com.fast.framework.support.L;
import com.fast.framework.util.ActivityManager;
import com.fast.framework.util.ScreenAdapter;

/**
 * 界面管理框架的activity基类，所有使用动态界面管理的activity都需要继承此类。
 * <p>
 * Created by lishicong on 2016/12/2.
 */

public class FastActivity extends AppCompatActivity {

    private FastFragment mFragment;

    protected long clickLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ScreenAdapter.getInstance().adaptDensity(this);
        // 某些设备全屏需要，不通用暂时屏蔽
        // ScreenAdapter.getInstance().fullScreen(this);
        this.setContentView(R.layout.activity_fast);
        // View view = View.inflate(this, R.layout.activity_fast, mBaseLayout);
        mFragment = FastManager.dispatch(this, getIntent(), EmptyFragment.class);

        ActivityManager.addActivity(this);
    }

    public FastFragment getFastFragment() {
        return mFragment;
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            mFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public boolean isClickAble() {
        long tempClickTime = System.currentTimeMillis();
        if (Math.abs(tempClickTime - clickLastTime) > 1000) {
            clickLastTime = tempClickTime;
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragment.onKeyDown(keyCode, event)) {
            // undo
            L.d("onKeyDown");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNewIntent(Intent intent) {
        mFragment.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }
}
