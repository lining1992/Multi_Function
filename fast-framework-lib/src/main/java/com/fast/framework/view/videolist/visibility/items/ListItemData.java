/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.visibility.items;

import java.util.List;

import android.view.View;

public class ListItemData {

    private Integer mIndexInAdapter;
    private View mView;

    private boolean mIsVisibleItemChanged;

    public int getIndex() {
        return mIndexInAdapter;
    }

    public View getView() {
        return mView;
    }

    public ListItemData fillWithData(int indexInAdapter, View view) {
        mIndexInAdapter = indexInAdapter;
        mView = view;
        return this;
    }

    public boolean isAvailable() {
        return mIndexInAdapter != null && mView != null;
    }

    public int getVisibilityPercents(List<? extends ListItem> listItems) {
        return listItems.get(getIndex()).getVisibilityPercents(getView());
    }

    public void setVisibleItemChanged(boolean isDataChanged) {
        mIsVisibleItemChanged = isDataChanged;
    }

    public boolean isVisibleItemChanged() {
        return mIsVisibleItemChanged;
    }

    @Override
    public String toString() {
        return "ListItemData{" + "mIndexInAdapter=" + mIndexInAdapter + ", mView=" + mView + ", mIsVisibleItemChanged="
                + mIsVisibleItemChanged + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListItemData that = (ListItemData) o;

        if (mIndexInAdapter != null ? !mIndexInAdapter.equals(that.mIndexInAdapter) : that.mIndexInAdapter != null) {
            return false;
        }
        return mView != null ? mView.equals(that.mView) : that.mView == null;

    }

    @Override
    public int hashCode() {
        int result = mIndexInAdapter != null ? mIndexInAdapter.hashCode() : 0;
        result = 31 * result + (mView != null ? mView.hashCode() : 0);
        return result;
    }
}
