/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.network;

/**
 * Created by lishicong on 2017/5/31.
 */
public class ExceptionHandle {

    public static final int SUCCESS_CODE = 0; // 请求成功
    public static final int SERVER_ERROR_CODE = -1;
    public static final String UNKOWN_ERROR = "未知错误";

    public static NetworkException handleException(Throwable e) {

        e.printStackTrace();

        if (e instanceof java.net.UnknownHostException) {
            return new NetworkException("服务器异常", SERVER_ERROR_CODE);
        } else if (e instanceof java.net.SocketTimeoutException) {
            return new NetworkException("服务器连接超时异常", SERVER_ERROR_CODE);
        } else if (e instanceof org.json.JSONException || e instanceof android.net.ParseException) {
            return new NetworkException("服务器数据解析错误异常", SERVER_ERROR_CODE);
        } else if (e instanceof NetworkException) {
            return (NetworkException) e;
        }
        return new NetworkException(UNKOWN_ERROR, SERVER_ERROR_CODE);
    }

    public static NetworkException handleException(int code) {
        if (code == ERROR.NETWORK) {
            return new NetworkException("网络连接异常", SERVER_ERROR_CODE);
        }
        return new NetworkException(UNKOWN_ERROR, SERVER_ERROR_CODE);
    }

    class ERROR {
        public static final int NETWORK = -1001;
    }

}
