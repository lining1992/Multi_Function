/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

import com.fast.framework.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义的Toast工具类
 * <p>
 * Created by lishicong on 2017/2/21.
 */

public class FT {

    private static Toast mToast;

    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null; // toast隐藏后，将其置为null
        }
    };

    private FT() {
    }

    /**
     * 显示toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fast_toast, null); // 自定义布局
        TextView text = (TextView) view.findViewById(R.id.fast_toast_message); // 显示的提示文字
        text.setText(message);
        mHandler.removeCallbacks(r);

        if (mToast == null) {
            // 只有mToast==null时才重新创建，否则只需更改提示文字
            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.BOTTOM, 0, 150);
            mToast.setView(view);
        }

        mHandler.postDelayed(r, 1000); // 延迟1秒隐藏toast
        mToast.show();
    }
}
