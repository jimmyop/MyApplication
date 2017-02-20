package com.agile.merchant.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zengxiangbin on 2016/6/28.
 */
public class GsonUtils {
    private GsonUtils(){}

    public static <T> T fromJson(String json, Class<T> clazz){
        return new Gson().fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonArray(String jsonArr){
        Type arrType = new TypeToken<List<T>>(){}.getType();
        return new Gson().fromJson(jsonArr, arrType);
    }

    public static <T> T fromJson(String json){
        Type type = new TypeToken<T>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    public static String toJson(Object obj){
        return new Gson().toJson(obj);
    }
}
