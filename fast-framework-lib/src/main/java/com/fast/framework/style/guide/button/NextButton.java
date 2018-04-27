/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide.button;

import com.fast.framework.style.guide.GuideConfiguration;
import com.fast.framework.style.guide.GuideUtils;

import android.view.View;

public class NextButton extends GuideViewWrapper {

    private boolean shouldShow = true;

    public NextButton(View button) {
        super(button);
    }

    @Override
    public void setup(GuideConfiguration config) {
        super.setup(config);
        this.shouldShow = config.getShowNextButton();
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        setVisibility(shouldShow && GuideUtils.isIndexBeforeLastPage(pageIndex, lastPageIndex));
    }
}
