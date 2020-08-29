package com.example.commonlibrary.android.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/2 14:19
 * <p>
 * description : 条目点击事件监听器
 */
public interface OnItemClickListener<T> {
    /**
     * 列表条目的点击事件
     *
     * @param parent   条目所在的父布局
     * @param view     被点击的控件
     * @param position 点击的Item的位置
     * @param t        点击的item对应的数据
     */
    void onItemClick(ViewGroup parent, View view, int position, T t);
}
