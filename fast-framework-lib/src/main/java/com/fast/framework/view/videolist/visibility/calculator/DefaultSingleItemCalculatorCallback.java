/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.visibility.calculator;

import com.fast.framework.view.videolist.visibility.items.ListItem;
import com.fast.framework.view.videolist.visibility.utils.Config;
import com.fast.framework.view.videolist.visibility.utils.Logger;

import android.view.View;

/**
 * Default implementation. You can override it and intercept switching between active items
 */
public class DefaultSingleItemCalculatorCallback implements SingleListViewItemActiveCalculator.Callback<ListItem> {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = DefaultSingleItemCalculatorCallback.class.getSimpleName();

    @Override
    public void activateNewCurrentItem(ListItem newListItem, View newView, int newViewPosition) {
        if (SHOW_LOGS) {
            Logger.v(TAG, "activateNewCurrentItem, newListItem " + newListItem);
            Logger.v(TAG, "activateNewCurrentItem, newViewPosition " + newViewPosition);
        }
        /**
         * Here you can do whatever you need with a newly "active" ListItem.
         */
        newListItem.setActive(newView, newViewPosition);
    }

    @Override
    public void deactivateCurrentItem(ListItem listItemToDeactivate, View view, int position) {
        if (SHOW_LOGS) {
            Logger.v(TAG, "deactivateCurrentItem, listItemToDeactivate " + listItemToDeactivate);
        }
        /**
         * When view need to stop being active we call deactivate.
         */
        listItemToDeactivate.deactivate(view, position);
    }
}
