package com.example.commonlibrary.mvp;

/**
 * @author : leixing
 * @date : 2017-05-31
 * Email       : leixing@baidu.com
 * Version     : 0.0.1
 * <p>
 * Description : 生命周期提供者，一般是{@code Activity} 或者是 {@code Fragment}
 */

public interface LifeCycleProvider {
    /**
     * 添加生命周期观察者
     *
     * @param observer 生命周期观察者
     */
    void addLifeCycleObserver(LifeCycleObserver observer);

    /**
     * 删除生命周期观察者
     *
     * @param observer 生命周期观察者
     */
    void removeLifeCycleObserver(LifeCycleObserver observer);
}
