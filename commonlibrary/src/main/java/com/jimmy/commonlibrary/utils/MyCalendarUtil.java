package com.jimmy.commonlibrary.utils;


import java.util.Calendar;

public class MyCalendarUtil {


	/**
	 * 当前日比较
	 * 
	 * @return
	 */
	private  static  boolean compareTo(Calendar beginDate,Calendar endDate) {
		return beginDate.get(Calendar.DAY_OF_MONTH) > endDate
				.get(Calendar.DAY_OF_MONTH);
	}

	public static int calculatorYear(Calendar beginDate,Calendar endDate) {
		int year1 = beginDate.get(Calendar.YEAR);
		int year2 = endDate.get(Calendar.YEAR);
		int month1 = beginDate.get(Calendar.MONTH);
		int month2 = endDate.get(Calendar.MONTH);
		int year = year2 - year1;
		if (compareTo(beginDate,endDate)) // 计算天时向月借了一个月
			month2 -= 1;
		if (month1 > month2)
			year -= 1;
		return year;
	}

	public  static int calculatorMonth(Calendar beginDate,Calendar endDate) {

		int month1 = beginDate.get(Calendar.MONTH);
		int month2 = endDate.get(Calendar.MONTH);
		int month = 0;
		if (compareTo(beginDate,endDate)) // 计算天时向月借了一个月
			month2 -= 1;
		if (month2 >= month1)
			month = month2 - month1;
		else if (month2 < month1) // 借一整年
			month = 12 + month2 - month1;
		return month;

	}

	public static int calculatorDay(Calendar beginDate,Calendar endDate) {

		int day11 = beginDate.get(Calendar.DAY_OF_MONTH);
		int day21 = endDate.get(Calendar.DAY_OF_MONTH);

		if (day21 >= day11) {
			return day21 - day11;
		} else {// 借一整月
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate.getTime());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(endDate.DAY_OF_MONTH, -1);
			return cal.getActualMaximum(Calendar.DATE) + day21 - day11;
		}
	}

	/**
	 * 返回两个时间相隔的多少年
	 * 
	 * @return
	 */
	public static int getYear(Calendar beginDate,Calendar endDate) {
		return calculatorYear(beginDate,endDate) > 0 ? calculatorYear(beginDate,endDate) : 0;
	}

	/**
	 * 返回除去整数年后的月数
	 * 
	 * @return
	 */
	public static int getMonth(Calendar beginDate,Calendar endDate) {
		int month = calculatorMonth(beginDate,endDate) % 12;
		return (month > 0 ? month : 0);
	}

	/**
	 * 返回除去整月后的天数
	 * 
	 * @return
	 */
	private static int getDay(Calendar beginDate,Calendar endDate) {
		int day = calculatorDay(beginDate,endDate);
		return day;
	}

	/**
	 * 返回现个日期间相差的年月天以@分隔
	 * 
	 * @return
	 */
	public static String getDate(Calendar beginDate,Calendar endDate) {
		return getYear(beginDate,endDate) + "@" + getMonth(beginDate,endDate) + "@" + getDay(beginDate,endDate);
	}
	public static int calculatorYear(long beginDateMills,long endDateMills) {
		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return calculatorYear(beginDate, endDate);
	}

	
	public  static int calculatorMonth(long beginDateMills,long endDateMills) {

		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return calculatorMonth(beginDate, endDate);

	}

	
	public static int calculatorDay(long beginDateMills,long endDateMills) {
		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return calculatorDay(beginDate, endDate);
	}

	/***
	 *  计算2个日期之间相差多少个月
	 * @param beginDateMills
	 * @param endDateMills
	 * @return
	 */
	public static int calculatorTwoDateMonthDistance(long beginDateMills,long endDateMills) {
		int calculatorYear = calculatorYear(beginDateMills, endDateMills);
		int calculatorMonth = calculatorMonth(beginDateMills, endDateMills);
		
		return calculatorYear*12+calculatorMonth;
	}
	/**
	 * 返回两个时间相隔的多少年
	 * 
	 * @return
	 */
	public static int getYear(long beginDateMills,long endDateMills) {
		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return getYear(beginDate, endDate);
	}

	/**
	 * 返回除去整数年后的月数
	 * 
	 * @return
	 */
	public static int getMonth(long beginDateMills,long endDateMills) {
		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return getMonth(beginDate, endDate);
	}

	/**
	 * 返回除去整月后的天数
	 * 
	 * @return
	 */
	public static int getDay(long beginDateMills,long endDateMills) {
		Calendar beginDate = getCalendarByMills(beginDateMills);
		Calendar endDate = getCalendarByMills(endDateMills);
		return getDay(beginDate, endDate);
	}

	/**
	 * 返回现个日期间相差的年月天以@分隔
	 * 
	 * @return
	 */
	public static String getDate(long beginDateMills,long endDateMills) {
		return getYear(beginDateMills,endDateMills) 
				+ "@" + getMonth(beginDateMills,endDateMills) 
				+ "@" + getDay(beginDateMills,endDateMills);
	}
	
	private static Calendar getCalendarByMills(long mills){
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTimeInMillis(mills);
		return dateCalendar;
	}
}