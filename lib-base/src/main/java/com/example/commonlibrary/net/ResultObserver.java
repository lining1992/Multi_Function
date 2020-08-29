package com.example.commonlibrary.net;


import com.example.commonlibrary.bean.Result;

import io.reactivex.disposables.Disposable;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/9/15 12:39
 */
public class ResultObserver<T> extends BaseObserver<T> {
    private final Result<T> result;
    private final Canceller canceller;

    public ResultObserver(Result<T> result) {
        this(result, null);
    }

    public ResultObserver(Result<T> result, Canceller canceller) {
        this.result = result;
        this.canceller = canceller;
    }

    @Override
    public void onSubscribe(final Disposable d) {

        if (canceller == null) {
            return;
        }
        canceller.setCancelable(new Cancelable() {
            @Override
            public void cancel() {
                d.dispose();
                result.setSucceed(false);
                result.setErrorCode(ErrorCode.CANCEL);
                result.setErrorMsg("已取消");
            }

            @Override
            public boolean isCancelled() {
                return d.isDisposed();
            }
        });
    }

    @Override
    protected void onSuccess(T t) {
        if (t == null) {
            result.setSucceed(false);
            result.setErrorCode(ErrorCode.NO_RESULT);
            result.setErrorMsg("无法获取数据");
            return;
        }

        result.setSucceed(true);
        result.setData(t);
    }

    @Override
    protected void onFailure(int errorCode, String errorMsg, Throwable throwable) {
        result.setSucceed(false);
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        result.setThrowable(throwable);

    }
}
