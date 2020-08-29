package com.example.commonlibrary.android.base;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2019/2/21 20:34
 */
public enum LifeCycle {
    /**
     * 初始状态
     */
    INIT,

    /**
     * 已创建
     */
    CREATE,

    /**
     * 已开始
     */
    START,

    /**
     * 开始交互
     */
    RESUME,

    /**
     * 暂停
     */
    PAUSE,

    /**
     * 停止
     */
    STOP,

    /**
     * 未构建或者已销毁
     */
    DESTROY
}
