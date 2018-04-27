/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import android.support.v4.view.ViewPager;

/**
 * Implemented by library components to respond to page scroll events
 * and initial setup
 */
public interface OnGuideScreenPageChangeListener extends ViewPager.OnPageChangeListener {

    void setup(GuideConfiguration config);

}

