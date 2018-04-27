/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide.color;

/**
 * Created by lishicong on 2016/12/7.
 */

import com.fast.framework.style.guide.GuideConfiguration;
import com.fast.framework.style.guide.OnGuideScreenPageChangeListener;

import android.content.Context;
import android.util.AttributeSet;

public class GuideBackgroundView extends ColorChangingBackgroundView implements OnGuideScreenPageChangeListener {

    public GuideBackgroundView(Context context) {
        super(context);
    }

    public GuideBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setup(GuideConfiguration config) {
        setColors(config.getBackgroundColors());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setPosition(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        // Not used
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Not used
    }

}
