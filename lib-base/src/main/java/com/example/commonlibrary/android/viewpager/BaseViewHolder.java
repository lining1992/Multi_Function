package com.example.commonlibrary.android.viewpager;

import android.view.View;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/13 14:33
 */
public abstract class BaseViewHolder<T> {

    private final View mItemView;

    public BaseViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        mItemView = itemView;
    }

    public View getItemView() {
        return mItemView;
    }

    protected abstract void onBindItemView(View view, T t, int position);

    protected void onRefreshItemView(Object payload) {

    }
}
