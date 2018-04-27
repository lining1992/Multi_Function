/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.style.guide;

import java.util.ArrayList;
import java.util.Arrays;

import com.fast.framework.style.guide.color.BackgroundColor;

import android.content.Context;

public class GuidePageList extends ArrayList<GuidePage> implements OnGuideScreenPageChangeListener {

    public GuidePageList(GuidePage... pages) {
        super(Arrays.asList(pages));
    }

    public BackgroundColor getBackgroundColor(Context context, int index) {
        return get(index).getBackground(context);
    }

    public BackgroundColor[] getBackgroundColors(Context context) {
        ArrayList<BackgroundColor> colors = new ArrayList<>();
        for (GuidePage page : this) {
            colors.add(page.getBackground(context));
        }
        return colors.toArray(new BackgroundColor[1]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (GuidePage page : this) {
            page.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (GuidePage page : this) {
            page.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        for (GuidePage page : this) {
            page.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void setup(GuideConfiguration config) {
        for (GuidePage page : this) {
            page.setup(config);
        }
    }
}
