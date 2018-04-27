/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide.button;

import com.fast.framework.style.guide.GuideConfiguration;
import com.fast.framework.style.guide.GuideUtils;

import android.view.View;
import android.widget.TextView;

public class SkipButton extends GuideViewWrapper {

    private boolean enabled = true;
    private boolean onlyShowOnFirstPage = false;

    public SkipButton(View button) {
        super(button);
    }

    @Override
    public void setup(GuideConfiguration config) {
        super.setup(config);
        onlyShowOnFirstPage = config.getShowPrevButton();
        this.enabled = config.getCanSkip();
        setVisibility(enabled, false);
        if (getView() instanceof TextView) {
            GuideUtils.setTypeface((TextView) this.getView(), config.getSkipButtonTypefacePath(), config.getContext());
        }
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        if (onlyShowOnFirstPage) {
            setVisibility(enabled && pageIndex == firstPageIndex);
        } else {
            setVisibility(enabled && GuideUtils.isIndexBeforeLastPage(pageIndex, lastPageIndex));
        }
    }

}
