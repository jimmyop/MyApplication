package com.jimmy.commonlibrary.utils;

import android.content.Intent;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * 获取可配置化 的参数
 * @author zengxiangbin
 *
 */
public class IntentExtraUtils {
	private IntentExtraUtils(){
		
	}
	
	public static String getString(Intent intent, String key){
		return getString(intent, key, "");
	}
	
	public static String getString(Intent intent, String key, String defaultValue){
		Serializable serializable = intent.getSerializableExtra(key);
		if(serializable == null || TextUtils.isEmpty(serializable.toString())){
			return defaultValue;
		}
		return serializable.toString();
	}
	
	public static int getInt(Intent intent, String key){
		return getInt(intent, key, 0);
	}
	
	public static int getInt(Intent intent, String key, int defaultValue){
		Serializable serializable = intent.getSerializableExtra(key);
		if(serializable == null || TextUtils.isEmpty(serializable.toString())){
			return defaultValue;
		}
		try {
			return Integer.parseInt(serializable.toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			if(LogUtils.DEBUG){
				throw new IllegalArgumentException("参数错误："+e.getMessage());
			}
		}
		return defaultValue;
	}
}
