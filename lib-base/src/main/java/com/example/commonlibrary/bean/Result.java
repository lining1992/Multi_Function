package com.example.commonlibrary.bean;

import com.example.commonlibrary.net.ErrorCode;

import java.util.List;

/**
 * @author v_lining05
 * @date 2019-12-07
 */
public class Result<T> {

    private boolean isSucceed;
    private T data;
    private int errorCode;
    private String errorMsg;
    private Throwable throwable;

    public boolean isSucceed() {
        return isSucceed;
    }

    public Result<T> setSucceed(boolean succeed) {
        isSucceed = succeed;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Result<T> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Result<T> setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Result<T> setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public String toString() {
        return "\"Result\": {"
                + "\"isSucceed\": \"" + isSucceed
                + ", \"data\": \"" + data
                + ", \"errorCode\": \"" + errorCode
                + ", \"errorMsg\": \"" + errorMsg + '\"'
                + ", \"throwable\": \"" + throwable
                + '}';
    }

    public Result<T> merge(Result<?> result) {
        isSucceed = result.isSucceed();
        errorCode = result.getErrorCode();
        errorMsg = result.getErrorMsg();
        throwable = result.getThrowable();
        return this;
    }

    public boolean isCancelled() {
        return errorCode == ErrorCode.CANCEL;
    }
}
