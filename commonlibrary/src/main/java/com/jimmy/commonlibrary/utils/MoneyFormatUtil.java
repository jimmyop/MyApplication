package com.jimmy.commonlibrary.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyFormatUtil {

	/***
	 *  格式化为2位小数点,必须是带2位小数
	 * @param money
	 * @return
	 */
	public static String format2Bit(double money){
	    return format2Bit(Double.toString(money));
	}
	
	public static double parser2Double(String input){
		double result = 0;
		try {
			if (!TextUtils.isEmpty(input) && !TextUtils.isEmpty(input.trim())) {
				BigDecimal moneyDecimal = new BigDecimal(input);
				result = moneyDecimal.doubleValue();
				//result = Double.parseDouble(input);
			}
		} catch (Exception e) {
			LogUtils.e("parser2double error! please check input data input = "+input );
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 4舍5入 保留2位小数的方法
	 * @param input
	 * @return
	 */
	public static double parser2Double2Bit(Double input){
		double result = 0.00;
		try {

			BigDecimal  b   =  new   BigDecimal(Double.toString(input));
			result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 把分为单位的金额转化为以元为单位的金额
	 * 保留2为小数
	 * @param input
	 * @return
	 */
	public static double changeIntPenney2DoubleYuan(int input){
		double result = 0.00;
		try {

			BigDecimal  b   =  new   BigDecimal(Double.toString(input/100.0));
			result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 4舍5入 不保留小数的方法
	 * @param input
	 * @return
	 */
	public static int parser2Double2Int(Double input){
		int result = 0;
		try {
			BigDecimal  b   =  new   BigDecimal(Double.toString(input));
			result  =  b.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 把以元为单位的金额变为以分为单位
	 * 4舍5入 不保留小数的方法
	 * @param input
	 * @return
	 */
	public static int parserDouble2MoneyInt(Double input){

		BigDecimal  b   =  new   BigDecimal(Double.toString(input));
		BigDecimal l = b.multiply(new BigDecimal(String.valueOf(100)));
		int  f1  =  l.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); 
		return f1;
	}
	
	/***
	 * 元为单位的double类型解析成分为单位的数值
	 * @param input
	 * @return
	 */
	public static Long parserDouble2MoneyLong(Double input){
		BigDecimal  b   =  new   BigDecimal(Double.toString(input));
		BigDecimal l = b.multiply(new BigDecimal(String.valueOf(100)));
		Long resutl = l.setScale(0,BigDecimal.ROUND_HALF_UP).longValue();
		return  resutl;
	}
	/***
	 * 元为单位的double类型解析成分为单位的数值
	 * @param input
	 * @return
	 */
	public static Long parserDoubleStr2MoneyLong(String input){
		double d1 = parser2Double(input);
		return  parserDouble2MoneyLong(d1);
	}

	/***
	 * 4舍5入 保留2位小数的方法
	 * @param input
	 * @return
	 */
	public static double parser2Double2Bit(String input){
		double d1 = parser2Double(input);
		BigDecimal  b   =  new   BigDecimal(d1); 
		double  f1  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		return f1;
	}
	public static float parser2Float(String input){
		float result = 0;
		try {
			if (!TextUtils.isEmpty(input) && !TextUtils.isEmpty(input.trim())) {
				result = Float.parseFloat(input);
			}
		} catch (Exception e) {
			LogUtils.e("parser2double error! please check input data input = "+input );
			e.printStackTrace();
		}
		return result;
	}
	/***
	 *  格式化为2位小数点,必须是带2位小数
	 * @param money
	 * @return
	 */
	public static String format2Bit(String money){
		String moneyString = "0.00";
		if (TextUtils.isEmpty(money)||TextUtils.isEmpty(money.trim())){
			return moneyString;
		}
		BigDecimal  b   =  new   BigDecimal((money));
		double moneyDouble = b.doubleValue();
	   	DecimalFormat decimalFormat = new DecimalFormat("###0.00");
	    moneyString = decimalFormat.format(moneyDouble);
	    return moneyString;
	}
	/***
	 *  转换为只带2为小数，如果小数点为00则不显示
	 * @param money
	 * @return
	 */
	public static String format2BitIfNeed(String money){
		String moneyString = "0";
		try {
			double moneyDouble = Double.parseDouble(money);
	 
	    	moneyString = format2BitIfNeed(moneyDouble);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return moneyString;
	}
	/***
	 *  转换为只带2为小数，如果小数点为00则不显示
	 * @param money
	 * @return
	 */
	public static String format2BitIfNeed(Double money){
		String moneyString = "0";
		try {
		double moneyDouble = money;
	    DecimalFormat decimalFormat = new DecimalFormat("###0.##");  
	 
	    moneyString = decimalFormat.format(moneyDouble);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return moneyString;
	}
	/**
	 *  格式化为2为小数，且小数点第三位不进行四舍五入
	 * @param money
	 * @return
	 */
	public static String formatFloor2Bit(double money){
		String moneyString = "0.00";
		try {
		    DecimalFormat decimalFormat = new DecimalFormat("###0.00");  
		    DecimalFormat formater = new DecimalFormat();
		    formater.setMaximumFractionDigits(2);
		    formater.setGroupingSize(0);
		    formater.setRoundingMode(RoundingMode.FLOOR);
		    moneyString = formater.format(money);  
		    moneyString = decimalFormat.format(Double.parseDouble(moneyString));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return moneyString;
	}
	/***
	 *  不使用科学计数法显示
	 * @param money
	 * @return
	 */
	public static String formatGroupingUsed(double money){
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);  
		return nf.format(money);
	}

	/***
	 *  获取超过万以后格式化的金额
	 * @param money
	 * @param keepZero "0.00" or "0.##"
	 * @return
	 */
	public static String getMoneyTenThousandString(double money,boolean keepZero){
		StringBuffer resultBuffer = new StringBuffer();
		String pattern = "0.##";
		if (keepZero){
			pattern = "0.00";
		}
		DecimalFormat   df   =new   DecimalFormat(pattern);

		resultBuffer.append("");
		if (money < 10000) {
			String moneyStr = df.format(money);
			resultBuffer.append(moneyStr);

		} else {
			BigDecimal bigDecimal = new BigDecimal(Double.toString(money));
			BigDecimal moneyW = bigDecimal.divide(new BigDecimal(10000));
			//double   moneyResult   =   moneyW.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
			double   moneyResult   =   moneyW.doubleValue();
			df = new DecimalFormat("0.###");// 保留3位小数
			df.setMaximumFractionDigits(3);
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.FLOOR);
			String moneyStr = df.format(moneyResult);
			resultBuffer.append(moneyStr);
			resultBuffer.append("万");
		}
		return resultBuffer.toString();
	}
}
