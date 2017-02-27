package com.jimmy.commonlibrary.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zengxiangbin on 2016/5/13.
 */
public class DateUtils {

    private DateUtils() {
    }

    public static final String DEFAULT_TIME_MM_DD_TIME = "MM-dd HH:mm";
    public static final String YYYY_MM_DD_OF_DATE = "yyyy年MM月dd日";

    /**
     * 获取当天日期
     *
     * @return
     */
    public static long getCurrDate() {
        Calendar currCal = Calendar.getInstance();

        return currCal.getTime().getTime();
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static long getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static long getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return long
     */
    public static long getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime().getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return long
     */
    public static long getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);

        return calendar.getTime().getTime();
    }

    /**
     * 获取某月往后加上一个月的日期
     *
     * @param time
     * @return
     */
    public static long getNextMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, 1);

        return calendar.getTimeInMillis();
    }

    public static String format(@NonNull String pattern, long time) {
        if (String.valueOf(time).length() == 10) {
            time *= 1000L;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }

    /**
     * 月日时分
     *
     * @param time
     * @return
     */
    public static String formatMMDDHHMM(long time) {
        if (String.valueOf(time).length() == 10) {
            time *= 1000L;
        }
        return format(DEFAULT_TIME_MM_DD_TIME, time);
    }

    /**
     * yyyy年MM月dd日
     *
     * @param time
     * @return
     */
    public static String formatYYYYMMDD(long time) {
        if (String.valueOf(time).length() == 10) {
            time *= 1000L;
        }
        return format(YYYY_MM_DD_OF_DATE, time);
    }

    /**
     * 中文的年月
     *
     * @param time
     * @return
     */
    public static String formatYYYYMMChina(long time) {
        if (String.valueOf(time).length() == 10) {
            time *= 1000L;
        }
        return format("yyyy年MM月", time);
    }

    /**
     * 替换日期字符串中的 “yyyy年MM月dd日” 为 “yyyy-MM-dd”
     *
     * @param dataStr
     * @return
     */
    public static String replaceWithChinaChar(String dataStr) {
        if (!TextUtils.isEmpty(dataStr)) {
            return dataStr.replace("年", "-").replace("月", "-").replace("日", "");
        }
        return "";
    }

    /**
     * 解析日期
     *
     * @param format  要解析的日期格式
     * @param dateStr 日期字符串
     * @return
     */
    public static Date parseDate(@NonNull String format, @NonNull String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            LogUtils.e("Parse Date Exception:" + e.getMessage());
        }
        return null;
    }

    /**
     * 解析日期 格式：yyyy年MM月dd日
     *
     * @param dateStr 日期
     * @return
     */
    public static Date parseWithChinaCharDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_OF_DATE);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            LogUtils.e("Parse Date Exception:" + e.getMessage());
        }
        return null;
    }
}
