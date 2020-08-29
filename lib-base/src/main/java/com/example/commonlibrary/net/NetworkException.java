/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.net;

/**
 * 网络异常参数
 *
 * @author lishicong
 * @date 2016/12/13.
 */

public class NetworkException extends Exception {
    /**
     * 请求已过期
     */
    private static final int REQUEST_EXPIRES_ERROR = 1004;

    private int errorCode;

    public NetworkException(String message) {
        this(message, ExceptionHandle.SERVER_ERROR_CODE);
    }

    public NetworkException(int errorCode) {
        this(ExceptionHandle.UN_KNOW_ERROR, errorCode);
    }

    public NetworkException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        switch (getErrorCode()) {
            case NetworkException.REQUEST_EXPIRES_ERROR:
                return "你的请求已过期,请检查设备时间";
            default:
                return super.getMessage();
        }
    }

    @Override
    public String toString() {
        return "NetworkException code:" + errorCode + ", message:" + getMessage();
    }

}
