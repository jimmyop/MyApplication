package com.agile.merchant.utils;

import android.app.FragmentManager;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择工具类
 * Created by zengxiangbin on 2016/5/16.
 */
public class DateTimePickerUtil {
    private DateTimePickerUtil() {
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSet(long time);
    }

    /**
     * 选择日期
     *
     * @param l
     * @param manager
     */
    public static void datePicker(final OnDateTimeSetListener l, FragmentManager manager) {
        datePicker(l, manager, "date_picker");
    }

    /**
     * 选择日期
     *
     * @param l
     * @param manager
     * @param year       默认选中年份
     * @param month      默认选中月份
     * @param dayOfMonth 默认选中日
     */
    public static void datePicker(final OnDateTimeSetListener l, FragmentManager manager,
                                  int year, int month, int dayOfMonth) {
        datePicker(l, manager, "date_picker", year, month, dayOfMonth);
    }

    /**
     * 选择日期
     *
     * @param l
     * @param manager
     * @param tag
     */
    public static void datePicker(final OnDateTimeSetListener l, FragmentManager manager, final String tag) {
        Calendar now = Calendar.getInstance();
        datePicker(l, manager, tag, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 选择日期
     *
     * @param l
     * @param manager
     * @param tag
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public static void datePicker(final OnDateTimeSetListener l, FragmentManager manager,
                                  final String tag, int year, int month, int dayOfMonth) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        if (l != null) {
                            Calendar dateSet = Calendar.getInstance();
                            dateSet.set(Calendar.YEAR, year);
                            dateSet.set(Calendar.MONTH, monthOfYear);
                            dateSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            dateSet.set(Calendar.HOUR_OF_DAY, 0);
                            dateSet.set(Calendar.MINUTE, 0);
                            dateSet.set(Calendar.SECOND, 0);
                            dateSet.set(Calendar.MILLISECOND, 0);

                            l.onDateTimeSet(dateSet.getTime().getTime());
                        }
                    }
                }, year, month, dayOfMonth);

        dpd.setThemeDark(false);
        dpd.show(manager, tag);
    }

    /**
     * 选择当天的时间
     *
     * @param l
     * @param manager
     */
    public static void timePicker(final OnDateTimeSetListener l, FragmentManager manager) {
        timePicker(l, manager, "time_picker");
    }

    /**
     * 选择当天的时间
     *
     * @param l
     * @param manager
     * @param tag
     */
    public static void timePicker(final OnDateTimeSetListener l, FragmentManager manager, final String tag) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        if (l != null) {
                            Calendar dateSet = Calendar.getInstance();
                            dateSet.setTime(new Date());
                            dateSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            dateSet.set(Calendar.MINUTE, minute);
                            dateSet.set(Calendar.SECOND, second);
                            dateSet.set(Calendar.MILLISECOND, 0);

                            l.onDateTimeSet(dateSet.getTime().getTime());
                        }
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.show(manager, tag);
    }
}
