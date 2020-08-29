package com.example.commonlibrary.android.viewpager.table;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/23 20:17
 */
public interface TableMultiTypeSupport {
    int getViewType(int pageIndex, int row, int column);

    int getLayoutId(int viewType);
}
