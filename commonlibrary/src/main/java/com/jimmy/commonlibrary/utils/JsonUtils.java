package com.jimmy.commonlibrary.utils;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONObject;

/**
 * Created by chenjiaming1 on 2017/4/6.
 */

public class JsonUtils {

    /***
     * 将JSON对象转换为传入类型的对象
     * @param
     * @param jsonString
     * @param beanClass
     * @return
     */
    public static <T> T toBean(String jsonString, Class<T> beanClass) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonString, beanClass);
        return result;
    }


    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static class JsonCode {
        public static String Exception = "Exception";
        public static String BadJson = "BadJson";
        public static String Http = "Http";
    }

    public static class JsonMessage {
        public static String Exception = "";
        public static String BadJson = "";
        public static String Http = "";
    }
}
