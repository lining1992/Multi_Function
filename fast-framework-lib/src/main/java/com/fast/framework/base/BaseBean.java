/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.base;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 实体的基类.
 * <p>
 * Created by lishicong on 2016/12/13.
 */

public class BaseBean implements Serializable {

    @JSONField(name = "errno")
    public int code;
    @JSONField(name = "errmsg")
    public String msg;
}
