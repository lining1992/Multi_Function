/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import android.content.Context;
import android.util.AttributeSet;

/**
 * An extension of {@link SimpleViewPagerIndicator SimpleViewPagerIndicator} that implements
 * the setup method of {@link OnGuideScreenPageChangeListener OnWelcomeScreenPageChangeListener}
 */
public class GuideViewPagerIndicator extends SimpleViewPagerIndicator implements OnGuideScreenPageChangeListener {

    public GuideViewPagerIndicator(Context context) {
        super(context);
    }

    public GuideViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setup(GuideConfiguration config) {
        setTotalPages(config.viewablePageCount());
    }
}
