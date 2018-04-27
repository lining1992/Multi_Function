/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import android.os.Build;
import android.view.View;

/**
 * 引导页最后一页控制滑动消失类.
 */
public class GuideViewHider implements OnGuideScreenPageChangeListener {

    private View view;
    private int lastPage;
    private OnViewHiddenListener listener = null;
    // enabled为在最后一页时，是否支持滑动消失的开关，为true时在最后一页继续向右滑动消失进入app.
    private boolean enabled = false;

    public GuideViewHider(View viewToHide) {
        view = viewToHide;
    }

    public interface OnViewHiddenListener {
        void onViewHidden();
    }

    public void setOnViewHiddenListener(OnViewHiddenListener listener) {
        this.listener = listener;
    }

    @Override
    public void setup(GuideConfiguration config) {
        enabled = config.getSwipeToDismiss();
        lastPage = config.lastPageIndex();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (!enabled) {
            return;
        }

        if (position == lastPage && positionOffset == 0 && listener != null) {
            listener.onViewHidden();
        }

        if (Build.VERSION.SDK_INT < 11) {
            return;
        }

        boolean shouldSetAlpha = position == lastPage - 1;
        float alpha = 1f - positionOffset;

        boolean beforeLastPage = position < lastPage - 1;

        if (shouldSetAlpha) {
            view.setAlpha(alpha);
        } else if (beforeLastPage && view.getAlpha() != 1f) {
            view.setAlpha(1f);
        }

    }

    @Override
    public void onPageSelected(int position) {
        // Not needed
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Not needed
    }
}
