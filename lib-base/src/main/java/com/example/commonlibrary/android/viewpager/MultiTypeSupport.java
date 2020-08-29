package com.example.commonlibrary.android.viewpager;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/23 20:17
 */
public interface MultiTypeSupport {
    int getViewType(int position);

    int getLayoutId(int viewType);
}
