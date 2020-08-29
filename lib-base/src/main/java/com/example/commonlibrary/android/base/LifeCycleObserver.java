package com.example.commonlibrary.android.base;

/**
 * @author : leixing
 * @date : 2017-05-26
 * Email       : leixing@baidu.com
 * Version     : 0.0.1
 * <p>
 * Description : interface of view life cycle
 */

public interface LifeCycleObserver {
    /**
     * onCreate回调
     */
    void onCreate();

    /**
     * onStart回调
     */
    void onStart();

    /**
     * onResume回调
     */
    void onResume();

    /**
     * onPause回调
     */
    void onPause();

    /**
     * onStop回调
     */
    void onStop();

    /**
     * onDestroy回调
     */
    void onDestroy();
}
