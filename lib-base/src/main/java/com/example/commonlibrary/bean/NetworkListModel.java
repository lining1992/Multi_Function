/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 网络数据封装类
 * <p>
 * Created by lishicong on 2016/12/13.
 */

public class NetworkListModel<T> {

    // 消息码
    @JSONField(name = "errno")
    private int code;
    // 消息内容
    @JSONField(name = "errmsg")
    private String msg;
    // 实体数据
    @JSONField(name = "data")
    private Data<T> result;

    @JSONField(name = "log_id")
    private String logId;

    @JSONField(name = "timestamp")
    private int timestamp;
    @JSONField(name = "query")
    private String query;

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

    public Data<T> getResult() {
        return result;
    }

    public NetworkListModel setResult(Data<T> result) {
        this.result = result;
        return this;
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
        return "\"NetworkListModel\": {"
                + "\"code\": \"" + code
                + ", \"msg\": \"" + msg + '\"'
                + ", \"result\": \"" + result
                + ", \"logId\": \"" + logId + '\"'
                + ", \"timestamp\": \"" + timestamp
                + '}';
    }

    public static class Data<T> {
        private int number;
        private int page;
        private int psize;
        @JSONField(name = "total_page")
        private int totalPage;
        private List<T> list;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public List<T> getList() {
            return list;
        }

        public Data setList(List<T> list) {
            this.list = list;
            return this;
        }

        @Override
        public String toString() {
            return "\"Data\": {"
                    + "\"number\": \"" + number
                    + ", \"list\": \"" + list
                    + '}';
        }
    }
}
