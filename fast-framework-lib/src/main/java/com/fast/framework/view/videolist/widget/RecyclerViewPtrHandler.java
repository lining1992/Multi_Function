/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.widget;

import com.fast.framework.view.pulltorefresh.PtrDefaultHandler;
import com.fast.framework.view.pulltorefresh.PtrFrameLayout;
import com.fast.framework.view.pulltorefresh.PtrHandler;

import android.view.View;

/**
 * Created by lishicong on 2016/10/27.
 */

public class RecyclerViewPtrHandler implements PtrHandler {

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {

    }

}

