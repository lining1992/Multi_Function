/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.R;
import com.fast.framework.util.AnimationUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by lishicong on 2017/5/10.
 */

public class FastAlertDialog extends DialogFragment {

    protected View mView;

    private TextView mTextViewTitle;
    private TextView mTextViewContent;
    private TextView mTextViewButton;
    private TextView mTextViewButton2;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = inflater.inflate(R.layout.fast_alert_dialog, container, false);

        mTextViewTitle = (TextView) mView.findViewById(R.id.fast_alert_dialog_title);
        mTextViewContent = (TextView) mView.findViewById(R.id.fast_alert_dialog_content);
        mTextViewButton = (TextView) mView.findViewById(R.id.fast_alert_dialog_button);
        mTextViewButton2 = (TextView) mView.findViewById(R.id.fast_alert_dialog_button2);

        initView();

        AnimationUtils.slideToUp(mView);
        return mView;
    }

    private void initView() {
        if (!TextUtils.isEmpty(title)) {
            mTextViewTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            mTextViewContent.setText(content);
        }
        if (!TextUtils.isEmpty(button)) {
            mTextViewButton.setText(button);
        }
        if (!TextUtils.isEmpty(button2)) {
            mTextViewButton2.setText(button2);
        }
        if (listener != null) {
            mTextViewButton.setOnClickListener(listener);
        }
        if (listener2 != null) {
            mTextViewButton2.setOnClickListener(listener2);
        }
    }

    private String title;
    private String content;
    private String button;
    private String button2;
    private View.OnClickListener listener;
    private View.OnClickListener listener2;

    public void setFastTitle(String title) {
        this.title = title;
    }

    public void setFastContent(String content) {
        this.content = content;
    }

    public void setFastButton(String button, View.OnClickListener listener) {
        this.button = button;
        this.listener = listener;
    }

    public void setFastButton2(String button, View.OnClickListener listener) {
        this.button2 = button;
        this.listener2 = listener;
    }

    public void updateFastButton(String button) {
        this.mTextViewButton.setText(button);
    }
}
