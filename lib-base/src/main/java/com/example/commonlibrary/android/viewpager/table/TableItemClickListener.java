package com.example.commonlibrary.android.viewpager.table;

import android.view.View;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/14 13:55
 */
public interface TableItemClickListener<T> {
    void onItemClick(View view, T t, int pageIndex, int row, int column);
}
