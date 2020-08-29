package com.example.commonlibrary.net;

import com.example.commonlibrary.utils.LogUtil;

/**
 * 取消器，用于耗时任务的取消
 *
 * @author : leixing
 * @date : 2019/03/25
 * Version : 0.0.1
 */
public class Canceller implements Cancelable {

    protected Cancelable cancelable;

    public Canceller() {
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * 取消
     */
    @Override
    public void cancel() {
        if (cancelable != null) {
            LogUtil.e("TRACE LOG" + "取消");
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelable != null && cancelable.isCancelled();
    }
}