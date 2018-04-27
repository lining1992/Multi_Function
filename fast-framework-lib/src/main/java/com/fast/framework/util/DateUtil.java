/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 * <p>
 * Created by lishicong on 2016/12/5.
 */

public class DateUtil {

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHARE = "yyyy.MM.dd";
    public static final String DATE_FORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_MMDDHHMMSS = "MMddHHmmss";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_HHMMSS = "HH:mm:ss";
    public static final String DATE_FORMAT_HHMM = "HH:mm";
    public static final String DATE_FORMAT_CHINESE_SHORT = "yyyy年M月d日";

    private DateUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param time      时间戳
     * @param formatter
     *
     * @return
     */
    public static String formatter(long time, String formatter) {

        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.US);
        return format.format(new Date(time));
    }

    /**
     * @param date
     * @param formatter
     *
     * @return
     */
    public static String formatter(Date date, String formatter) {

        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.US);
        return format.format(date);
    }

}
