package com.example.commonlibrary.android.viewpager.table;

import android.view.View;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/13 14:33
 */
public abstract class BaseTableViewHolder<T> {

    private final View mItemView;

    public BaseTableViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        mItemView = itemView;
    }

    public View getItemView() {
        return mItemView;
    }

    protected abstract void onBindItemView(View view, T t, int pageIndex, int row, int column);

    protected void onRefreshItemView(Object payload) {

    }
}
