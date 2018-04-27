/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

/**
 * Created by lishicong on 2016/12/13.
 */

public class NetworkException extends Exception {
    // 网络异常，请稍后重试
    public static final int NERWORK_ERROR = 1000;
    // 参数错误
    public static final int PARAMETER_ERROR = 1001;
    // 未登录
    public static final int UNLOGIN_ERROR = 1002;
    // 签名验证失败
    public static final int SIGN_VERIFICATION_ERROR = 1003;
    // 请求已过期
    public static final int REQUEST_EXPIRES_ERROR = 1004;
    private int errorCode;

    public NetworkException(String message) {
        this(message, ExceptionHandle.SERVER_ERROR_CODE);
    }

    public NetworkException(int errorCode) {
        this(ExceptionHandle.UNKOWN_ERROR, errorCode);
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
