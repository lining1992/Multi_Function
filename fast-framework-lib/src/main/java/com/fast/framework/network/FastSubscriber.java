/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

import android.content.Context;
import rx.Subscriber;

/**
 * Created by lishicong on 2016/12/16.
 */

public abstract class FastSubscriber<T> extends Subscriber<T> {

    protected Context mContext;

    public FastSubscriber(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onError(Throwable e) {

        onFailure(ExceptionHandle.handleException(e));

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure(NetworkException networkException);

}