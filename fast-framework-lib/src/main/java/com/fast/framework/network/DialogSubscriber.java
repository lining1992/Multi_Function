/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import com.fast.framework.R;
import com.fast.framework.util.NetworkUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 加载网络数据时，显示的loading状态框，设置了loading message后，才会显示loading提示框
 * <p>
 * Created by lishicong on 2016/12/16.
 */

public abstract class DialogSubscriber<T> extends FastSubscriber<T> {

    private Dialog loadingDialog;
    private String loadingMsg;
    private boolean showLoading;

    /**
     * 构造方法
     *
     * @param context
     */
    public DialogSubscriber(Context context) {
        super(context);
    }

    /**
     * 构造方法，带loading提示框
     *
     * @param context
     * @param loadingMsg 提示消息
     */
    public DialogSubscriber(Context context, String loadingMsg) {
        super(context);
        if (context instanceof Activity && !TextUtils.isEmpty(loadingMsg)) {
            this.loadingMsg = loadingMsg;
            this.showLoading = true;
        }
    }

    protected void showDialog() {
        dismissDialog();

        loadingDialog = new Dialog(mContext);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(initView(mContext, loadingMsg));
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        //        lp.width = (int) (DensityUtil.getScreenWidth(mContext) * 0.6); // loading提示框的宽
        //        lp.height = DensityUtil.dp2px(mContext, 90); // loading提示框的高
        loadingDialog.getWindow().setAttributes(lp);
        loadingDialog.setCancelable(false);

        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    loadingDialog.show();
                }
            } else {
                if (activity != null && !activity.isFinishing()) {
                    loadingDialog.show();
                }
            }
        }
    }

    protected void dismissDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!NetworkUtil.isConnected(mContext)) {
            onFailure(ExceptionHandle.handleException(ExceptionHandle.ERROR.NETWORK));
            return;
        }

        if (showLoading) {
            showDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onCompleted() {
        dismissDialog();
    }

    private View initView(Context context, CharSequence msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.fast_dialog, null);
        if (!TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.tv_content)).setText(msg);
        }
        return view;
    }

}

