package com.jimmy.commonlibrary.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by zengxiangbin on 2016/5/17.
 */
public class MoneyUtils {

    private static DecimalFormat sDecimalFormat = new DecimalFormat("00");

    private MoneyUtils(){}

    /**
     * 转成分
     * @param text
     * @return
     */
    public static long parseToPenny(String text){
        if (TextUtils.isEmpty(text)){
            return 0;
        }

        String[] tempArray = text.split("\\.");
        if(tempArray.length == 1 || TextUtils.isEmpty(tempArray[1])){
            return Long.parseLong(tempArray[0]) * 100;
        }else {
            if (tempArray[1].length() == 1){
                return Long.parseLong(tempArray[0]) * 100 + Long.parseLong(tempArray[1])*10;
            }
            return Long.parseLong(tempArray[0]) * 100 + Long.parseLong(tempArray[1].substring(0,2));
        }
    }

    /**
     * 份转成字符串
     * @param penney
     * @return
     */
    public static String pennyToString(long penney){
        if (penney == 0){
            return "0.00";
        }

        if (penney < 100){
            return "0."+sDecimalFormat.format(penney);
        }

        if (penney % 100 == 0){
            return penney / 100 + ".00";
        }

        return penney / 100 + "." + sDecimalFormat.format(penney % 100);
    }

    /**
     * 份转成字符串
     * @param penney
     * @return
     */
    public static String pennyToStringForInput(long penney){
        if (penney == 0){
            return "0";
        }

        if (penney < 100){
            return "0."+sDecimalFormat.format(penney);
        }

        if (penney % 100 == 0){
            return penney / 100 + "";
        }

        return penney / 100 + "." + sDecimalFormat.format(penney % 100);
    }
	
}
