package com.example.mytest.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.example.mytest.BaseApplication;


public class ToastUtil {

    private static Toast mToast;

    private static void initToast() {

        if (mToast == null)
            mToast = Toast.makeText(BaseApplication.getContext(), "", Toast.LENGTH_SHORT);
    }

    public static void showToast(@StringRes int resId) {

        try {

            initToast();
            mToast.setText(resId);
            mToast.show();
        } catch (Throwable t) {
        }
    }

    public static void showToast(String text) {

        if (TextUtil.isEmpty(text))
            return;

        try {

            initToast();
            mToast.setText(text);
            mToast.show();
        } catch (Throwable t) {
        }
    }

    public static void showToast(@StringRes int resId, Object... args) {

        showToast(BaseApplication.getAppString(resId, args));
    }

    public static void release() {

        mToast = null;
    }
}
