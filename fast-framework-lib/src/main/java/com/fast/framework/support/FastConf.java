/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.support;

/**
 * Fast framework setting config class.
 * <p>
 * Created by lishicong on 2016/12/6.
 */

public class FastConf {

    private FastConf() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 引导页显示状态
     */
    public static String FAST_GUIDE_STATUS = "fast_guide_status";
    /**
     * 夜间模式
     */
    public static String FAST_THEME_KEY = "fast_theme_mode";

}
