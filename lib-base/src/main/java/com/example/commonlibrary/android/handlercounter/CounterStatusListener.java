package com.example.commonlibrary.android.handlercounter;

/**
 * description : 计数器状态监听器
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/10 11:30
 */
@SuppressWarnings("WeakerAccess")
public interface CounterStatusListener {
    /**
     * 计数器状态回调
     *
     * @param status 切换的新状态
     */
    void onNewStatus(CounterStatus status);
}
