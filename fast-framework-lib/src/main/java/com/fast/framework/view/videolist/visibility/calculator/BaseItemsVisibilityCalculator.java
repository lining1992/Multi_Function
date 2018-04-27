/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.visibility.calculator;

import com.fast.framework.view.videolist.visibility.scroll.ItemsPositionGetter;
import com.fast.framework.view.videolist.visibility.scroll.ScrollDirectionDetector;
import com.fast.framework.view.videolist.visibility.utils.Config;
import com.fast.framework.view.videolist.visibility.utils.Logger;

import android.widget.AbsListView;

/**
 * This class encapsulates some basic logic of Visibility calculator.
 * In onScroll event it calculates Scroll direction using
 * and then calls appropriate methods
 */
public abstract class BaseItemsVisibilityCalculator implements ListItemsVisibilityCalculator {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = BaseItemsVisibilityCalculator.class.getSimpleName();

    /**
     * Initial scroll direction should be UP in order to set as active most top item if no active item yet
     */
    protected ScrollDirectionDetector.ScrollDirection mScrollDirection = ScrollDirectionDetector.ScrollDirection.UP;

    private final ScrollDirectionDetector mScrollDirectionDetector = new ScrollDirectionDetector(
            new ScrollDirectionDetector.OnDetectScrollListener() {
                @Override
                public void onScrollDirectionChanged(ScrollDirectionDetector.ScrollDirection scrollDirection) {
                    if (SHOW_LOGS) {
                        Logger.v(TAG, "onScrollDirectionChanged, scrollDirection " + scrollDirection);
                    }
                    mScrollDirection = scrollDirection;
                }
            });

    @Override
    public void onScrolled(ItemsPositionGetter itemsPositionGetter, int scrollState) {
        int firstVisiblePosition = itemsPositionGetter.getFirstVisiblePosition();

        if (SHOW_LOGS) {
            Logger.v(TAG, "onScroll, firstVisibleItem " + firstVisiblePosition + "scrollState " + scrollState);
        }

        mScrollDirectionDetector.onDetectedListScroll(itemsPositionGetter, firstVisiblePosition);

        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                onStateTouchScroll(itemsPositionGetter);
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                onStateFling(itemsPositionGetter);
                break;

            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                onScrollStateIdle(itemsPositionGetter);
                break;
            default:
                break;
        }
    }

    protected abstract void onStateFling(ItemsPositionGetter itemsPositionGetter);

    protected abstract void onStateTouchScroll(ItemsPositionGetter itemsPositionGetter);

}
