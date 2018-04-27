/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * Created by lishicong on 2016/12/16.
 */

public class DataCheckUtil {

    private DataCheckUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查字符串是否是整型数据
     *
     * @param str
     *
     * @return
     */
    public static boolean isNumeric(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
