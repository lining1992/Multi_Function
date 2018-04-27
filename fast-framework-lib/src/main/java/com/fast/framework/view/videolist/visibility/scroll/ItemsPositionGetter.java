/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.visibility.scroll;

import android.view.View;

/**
 * This class is an API for
 * Using this class is can access all the data from RecyclerView / ListView
 * <p>
 * There is two different implementations for ListView and for RecyclerView.
 * RecyclerView introduced LayoutManager that's why some of data moved there
 */
public interface ItemsPositionGetter {
    View getChildAt(int position);

    int indexOfChild(View view);

    int getChildCount();

    int getLastVisiblePosition();

    int getFirstVisiblePosition();
}
