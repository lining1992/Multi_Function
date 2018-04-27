/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.R;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by lishicong on 2017/5/17.
 */

public class LogDialog extends BottomDialog {

    private TextView mTextView;
    private String mLog;

    public LogDialog() {
        mLayoutId = R.layout.fast_dialog_log;
    }

    public void setLog(String log) {
        this.mLog = log;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextView = (TextView) mView.findViewById(R.id.f_dialog_log_txt);
        mTextView.setText(mLog);
    }
}
