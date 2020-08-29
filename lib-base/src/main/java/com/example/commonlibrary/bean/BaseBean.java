/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.commonlibrary.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 实体的基类.
 * <p>
 * Created by lishicong on 2016/12/13.
 */

public class BaseBean implements Serializable {

    @SerializedName("errno")
    @JSONField(name = "errno")
    public int code;

    @SerializedName("errmsg")
    @JSONField(name = "errmsg")
    public String msg;
}
