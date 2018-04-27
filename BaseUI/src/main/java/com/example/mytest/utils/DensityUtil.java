package com.example.mytest.utils;

import android.support.annotation.DimenRes;

import com.joy.ui.BaseApplication;


public class DensityUtil {

    public static int dip2px(float dpValue) {

        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getDimensionPixelSize(@DimenRes int id) {

        return BaseApplication.getAppResources().getDimensionPixelSize(id);
    }
}
