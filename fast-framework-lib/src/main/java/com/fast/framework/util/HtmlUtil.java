/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

/**
 * Created by lishicong on 2017/7/19.
 */

public class HtmlUtil {

    /**
     * 按指定长度截串（中英文混合）
     *
     * @param str
     * @param len
     *
     * @return
     */
    public static String subTextString(String str, int len) {
        if (str.length() < len / 2) {
            return str;
        }

        int count = 0;
        StringBuffer sb = new StringBuffer();
        String[] ss = str.split("");
        for (int i = 0; i < ss.length; i++) {
            count += ss[i].getBytes().length > 1 ? 2 : 1;
            sb.append(ss[i]);
            if (count >= len) {
                break;
            }
        }
        return (sb.toString().length() < str.length()) ? sb.append("...").toString() : str;
    }
}
