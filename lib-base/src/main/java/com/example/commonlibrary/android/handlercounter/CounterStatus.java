package com.example.commonlibrary.android.handlercounter;

/**
 * description : 计数器状态
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/10 11:09
 */
public enum CounterStatus {
    /**
     * 空闲状态
     */
    IDLE,

    /**
     * 运行状态
     */
    RUNNING,

    /**
     * 暂停状态
     */
    PAUSE;

    public static CounterStatus fromOrdinal(int ordinal) {
        for (CounterStatus status : CounterStatus.values()) {
            if (status.ordinal() == ordinal) {
                return status;
            }
        }
        return null;
    }
}
