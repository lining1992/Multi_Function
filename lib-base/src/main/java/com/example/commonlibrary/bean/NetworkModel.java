/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 网络数据封装类
 * <p>
 * Created by lishicong on 2016/12/13.
 */

public class NetworkModel<T> {

    // 消息码
    @JSONField(name = "code")
    private int code;
    // 消息内容
    @JSONField(name = "msg")
    private String msg;
    // 实体数据
    @JSONField(name = "data")
    private T result;

    @JSONField(name = "log_id")
    private String logId;

    @JSONField(name = "timestamp")
    private int timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "\"NetworkModel\": {"
                + "\"code\": \"" + code
                + ", \"msg\": \"" + msg + '\"'
                + ", \"logId\": \"" + logId + '\"'
                + ", \"timestamp\": \"" + timestamp
                + '}';
    }
}
