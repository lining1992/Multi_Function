/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.base;

import com.fast.framework.FastActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by lishicong on 2016/12/2.
 */

public abstract class BaseFragment extends Fragment {

    protected FastActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (FastActivity) getActivity();
    }
}
