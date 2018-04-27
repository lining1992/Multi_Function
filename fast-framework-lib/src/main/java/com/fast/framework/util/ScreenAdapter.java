/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */

package com.fast.framework.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * 屏幕适配
 * Created by zhanglong02 on 2017/8/1.
 */

public class ScreenAdapter {

    private static final float BASE_DPI = 160.0f;

    enum StandardScreen {
        W_854_H_480(854, 480, 240, 1.0f),
        W_1280_H_720(1280, 720, 320, 1.0f),
        W_1280_H_620(1280, 620, 240, 1.0f),
        W_1280_H_480(1280, 480, 240, 1.0f),
        W_1198_H_480(1198, 480, 240, 1.0f),
        W_1024_H_600(1024, 600, 240, 1.0f);

        int width;
        int height;
        int density;
        float fontScale;

        boolean match(int width, int height) {
            return this.width == width && this.height == height;
        }

        StandardScreen(int width, int height, int density, float fontScale) {
            this.width = width;
            this.height = height;
            this.density = density;
            this.fontScale = fontScale;
        }
    }

    private List<StandardScreen> mScreenList;

    private ScreenAdapter() {
        mScreenList = new ArrayList<>();
        mScreenList.add(StandardScreen.W_854_H_480);
        mScreenList.add(StandardScreen.W_1280_H_720);
        mScreenList.add(StandardScreen.W_1280_H_620);
        mScreenList.add(StandardScreen.W_1280_H_480);
        mScreenList.add(StandardScreen.W_1198_H_480);
        mScreenList.add(StandardScreen.W_1024_H_600);
    }

    private static final class InstanceHolder {
        private static final ScreenAdapter INSTANCE = new ScreenAdapter();
    }

    public static ScreenAdapter getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 检测是否为后视镜屏幕
     *
     * @param context
     *
     * @return
     */
    public boolean isMirror(Context context) {
        // 屏幕宽高比大于16:9时，认为是后视镜
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        if (StandardScreen.W_1280_H_620.match(screenWidth, screenHeight)) {
            return false;
        }
        return screenWidth * 1.0f / screenHeight > 16.0f / 9;
    }

    public void fullScreen(final Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int arg0) {
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
    }

    public void adaptDensity(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int densityDpi = displayMetrics.densityDpi;
        float density = displayMetrics.density;
        float scaledDensity = displayMetrics.scaledDensity;
        float fontScale = configuration.fontScale;
        Log.i("ScreenAdapter", "resolution=" + screenWidth + "*" + screenHeight + ",densityDpi="
                + densityDpi + ",density=" + density + ",scaledDensity=" + scaledDensity + ",fontScale=" + fontScale);
        for (StandardScreen screen : mScreenList) {
            if (screen.match(screenWidth, screenHeight)) {
                if (densityDpi != screen.density) {
                    updateConfiguration(resources, screen.density, screen.fontScale);
                } else {
                    Log.i("ScreenAdapter", "the screen params is standard");
                }
                return;
            }
        }
        Log.e("ScreenAdapter", "screen adapt failed, resolution=" + screenWidth + "*" + screenHeight);
    }

    private void updateConfiguration(Resources resources, int densityDpi, float fontScale) {
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float density = densityDpi / BASE_DPI;
        configuration.densityDpi = densityDpi;
        configuration.fontScale = fontScale;
        displayMetrics.densityDpi = densityDpi;
        displayMetrics.density = density;
        displayMetrics.scaledDensity = density;
        resources.updateConfiguration(configuration, displayMetrics);
        Log.i("ScreenAdapter",
                "[new] densityDpi=" + densityDpi + ",density=scaledDensity=" + density + ",fontScale=" + fontScale);
    }
}
