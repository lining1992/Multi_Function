/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide.button;

import com.fast.framework.style.guide.GuideConfiguration;
import com.fast.framework.style.guide.GuideUtils;

import android.view.View;
import android.widget.TextView;

public class DoneButton extends GuideViewWrapper {

    public DoneButton(View button) {
        super(button);
        if (button != null) {
            hideImmediately();
        }
    }

    @Override
    public void setup(GuideConfiguration config) {
        super.setup(config);
        if (this.getView() instanceof TextView) {
            GuideUtils.setTypeface((TextView) this.getView(), config.getDoneButtonTypefacePath(), config.getContext());
        }
    }

    @Override
    public void onPageSelected(int pageIndex, int firstPageIndex, int lastPageIndex) {
        setVisibility(!GuideUtils.isIndexBeforeLastPage(pageIndex, lastPageIndex));
    }

}
