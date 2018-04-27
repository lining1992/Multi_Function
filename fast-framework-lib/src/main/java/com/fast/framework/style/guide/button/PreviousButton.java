/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide.button;

import com.fast.framework.style.guide.GuideConfiguration;

import android.view.View;

public class PreviousButton extends GuideViewWrapper {

    private boolean shouldShow = false;

    public PreviousButton(View button) {
        super(button);
    }

    @Override
    public void setup(GuideConfiguration config) {
        super.setup(config);
        this.shouldShow = config.getShowPrevButton();
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        setVisibility(shouldShow && pageIndex != firstPageIndex);
    }

}
