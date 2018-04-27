package com.example.mytest.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static final String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SIMPLE_POINT = "yyyy.MM.dd";
    public static final String DATE_FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss"; // 24h
    public static final String DATE_FORMAT_DETAIL_2 = "yyyy-MM-dd hh:mm:ss"; // 12h
    public static final String DATE_FORMAT_HOUR = "HH:mm"; // 12h
    public static final String DATE_FORMAT_DETAIL_NO_SECOND = "yyyy年MM月dd日 HH:mm"; // 24h
    public static final String DATE_FORMAT_DETAIL_CHINESE = "yyyy年MM月dd日"; // 24h
    public static final String DATE_FORMAT_MDW = "MM月dd日";
    public static final String[] WEEK_DAYS_NAME = {"日", "一", "二", "三", "四", "五", "六"};
    public static final String DATE_FORMAT_RFC822 = "EEE, d MMM yyyy HH:mm:ss z";

    /**
     * 获取yyyy-MM-dd格式的formater
     *
     * @return
     */
    public static String getSimpleTypeText(long millis) {

        return new SimpleDateFormat(DATE_FORMAT_SIMPLE).format(millis);
    }

    public static String getSimpleTypeChineseText(long millis) {

        return new SimpleDateFormat(DATE_FORMAT_DETAIL_CHINESE).format(millis);
    }

    public static String getSimpleDataText(long millis) {

        return new SimpleDateFormat(DATE_FORMAT_SIMPLE_POINT).format(millis);
    }

    public static String getFormatMonthDay(long timeMillis) {

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MDW);
        return formatter.format(timeMillis);
    }

    public static String getWeekTip(long timeMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeMillis));
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return WEEK_DAYS_NAME[intWeek];
    }

    public static String getFormatTime(long timeInMillis, SimpleDateFormat dateFormat) {

        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getSimpleTime(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_SIMPLE, timeInMillis).toString();
    }

    public static CharSequence getSimpleTimeCharSeq(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_SIMPLE, timeInMillis).toString();
    }

    public static String getDetailTime(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_DETAIL, timeInMillis).toString();
    }

    public static String getDetailTime2(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_DETAIL_2, timeInMillis).toString();
    }

    public static String getDetailTimeHour(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_HOUR, timeInMillis).toString();
    }

    public static String getDetailTimeNoSecond(long timeInMillis) {

        return new SimpleDateFormat(DATE_FORMAT_DETAIL_NO_SECOND).format(new Date(timeInMillis));
    }

    public static CharSequence getDetailTimeCharSeq(long timeInMillis) {

        return DateFormat.format(DATE_FORMAT_DETAIL, timeInMillis).toString();
    }

    public static long getCurrentTimeInLong() {

        return System.currentTimeMillis();
    }

    public static String getCurrentSimeTime() {

        return getSimpleTime(System.currentTimeMillis());
    }

    public static String getCurrentDetailTime() {

        return getDetailTime(System.currentTimeMillis());
    }

    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {

        return getFormatTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取自定义格式的日期
     *
     * @param mark eg：yyyyMMddHHmmss yyyy年MM月dd日 HH时mm分ss秒
     */
    @SuppressLint("SimpleDateFormat")
    public static String getMyFormatDate(String mark) throws Exception {

        return new SimpleDateFormat(mark).format(new Date());
    }

    /**
     * 获取自定义格式的日期
     *
     * @param mark  自定义的格式 ：yyyy/MM/dd
     * @param uTime Unix 时间戳
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatDateByCustomMarkAndUnixTime(String mark, long uTime) {

        return new SimpleDateFormat(mark).format(new Date(uTime * 1000));
    }

    /**
     * for IM module
     *
     * @param timeInMillis
     * @return
     */
    public static String getFormatTime(long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Calendar currCalendar = Calendar.getInstance();

        if (calendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {

            if (calendar.get(Calendar.DAY_OF_YEAR) == currCalendar.get(Calendar.DAY_OF_YEAR)) {

                return formatTimeTypeToday(calendar);
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == currCalendar.get(Calendar.DAY_OF_YEAR) - 1) {

                return formatTimeTypeYestorday(calendar);
            } else {

                return formatTimeTypeThisYear(calendar);
            }
        }
        return formatTimeTypeOther(calendar);
    }

    private static String formatTimeTypeToday(Calendar calendar) {

        return DateFormat.format("kk:mm", calendar).toString();
    }

    private static String formatTimeTypeYestorday(Calendar calendar) {

        return new StringBuilder("昨天  ").append(DateFormat.format("kk:mm", calendar)).toString();
    }

    private static String formatTimeTypeThisYear(Calendar calendar) {

        return DateFormat.format("MM-dd kk:mm", calendar).toString();
    }

    private static String formatTimeTypeOther(Calendar calendar) {

        return DateFormat.format("yyyy-MM-dd kk:mm", calendar).toString();
    }

    public static boolean isAddSysTime(long currentTime, long prevTime) {

        return (float) (currentTime - prevTime) / 1000 > 5 * 60;
    }

    /**
     * @param date 字符串时间,出入的格式为+0800
     * @return
     */
    public static long getRFC822(String date) {

        java.text.DateFormat rfc822 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            return rfc822.parse(date).getTime();
        } catch (ParseException e) {
        }
        return 0;
    }

    /**
     * 根据传入的便宜量,根据手机时区,再换算为当地时区,只初略计算"时"的偏移,不计算分
     *
     * @param timezone 格式必须是 +0800
     * @return
     */
    public static long getLocalTime(String timezone) {

        if (TextUtil.isNotEmpty(timezone) && timezone.length() > 3) {

            int cityTimeZone = MathUtil.parseInt(timezone.substring(0, 2), -1);
            if (cityTimeZone > -13 && cityTimeZone < 13) {

                int timeZone = calcTimeZoneOffset(cityTimeZone, getSystemTimeZone());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.HOUR_OF_DAY, timeZone);
                return calendar.getTimeInMillis();
            }
        }
        return 0;
    }

    /**
     * 根据两个时区的差值换算出对应的差值
     *
     * @param cityTimeZone   城市对应的时区
     * @param systemTimeZone 当前系统对应的时区
     * @return
     */
    private static int calcTimeZoneOffset(int cityTimeZone, int systemTimeZone) {

        if (cityTimeZone >= 0 && systemTimeZone >= 0) {

            return cityTimeZone - systemTimeZone;
        } else if (cityTimeZone <= 0 && systemTimeZone <= 0) {

            return cityTimeZone - systemTimeZone;
        } else if (cityTimeZone >= 0 && systemTimeZone <= 0) {

            return cityTimeZone + Math.abs(systemTimeZone);
        } else if (cityTimeZone <= 0 && systemTimeZone >= 0) {

            return -(Math.abs(cityTimeZone) + systemTimeZone);
        }
        return 0;
    }

    /**
     * 获取当前系统时间的时区 ,
     *
     * @return
     */
    public static int getSystemTimeZone() {

        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeZone().getOffset(System.currentTimeMillis()) / 1000 / 60 / 60;//系统返回的是偏移的毫秒数,除得的时间为"时"数
    }
}