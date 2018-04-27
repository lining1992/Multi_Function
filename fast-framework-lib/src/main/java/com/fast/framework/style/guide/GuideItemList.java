/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import java.util.ArrayList;
import java.util.Arrays;

public class GuideItemList extends ArrayList<OnGuideScreenPageChangeListener>
        implements OnGuideScreenPageChangeListener {

    public void addAll(OnGuideScreenPageChangeListener... items) {
        super.addAll(Arrays.asList(items));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (OnGuideScreenPageChangeListener changeListener : this) {
            changeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (OnGuideScreenPageChangeListener changeListener : this) {
            changeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        for (OnGuideScreenPageChangeListener changeListener : this) {
            changeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void setup(GuideConfiguration config) {
        for (OnGuideScreenPageChangeListener changeListener : this) {
            changeListener.setup(config);
        }
    }
}
