package com.example.commonlibrary.net;

/**
 * 可取消的
 */
public interface Cancelable {
    /**
     * 取消
     */
    void cancel();

    /**
     * 判断是否已取消
     *
     * @return 是否已取消
     */
    boolean isCancelled();
}
