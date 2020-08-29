package com.example.commonlibrary.android.viewpager.table;

import android.view.View;

/**
 * description : {@code ViewHolder}的工厂类，用于生成{@code ViewHolder}
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/2 14:26
 */
public abstract class BaseTableViewHolderFactory<T> {
    /**
     * 构造{@code ViewHolder}
     *
     * @param itemView 条目的视图
     * @param viewType 条目的布局类型
     * @return {@code ViewHolder}
     */
    public abstract BaseTableViewHolder<T> createViewHolder(View itemView, int viewType);
}
