
package com.jimmy.commonlibrary.net;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.jimmy.commonlibrary.utils.JsonUtils;
import com.jimmy.commonlibrary.utils.LogUtils;
import com.jimmy.commonlibrary.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 请求参数的工具类
 */
public class RequestUtil {

    /**
     * 拼装URL
     *
     * @param param
     * @return
     */
    public static String getUrl(RequestParams param) {
        //这个builder是拼接请求的键值，每个value都需要encode一下
        StringBuilder urlEncodeBuilder = new StringBuilder(param.getUrlType());
        //这个builder是用来生成api_sign标识的，不需要encode，最后需要加上api_key
        StringBuilder signBuilder = new StringBuilder();
        //ArrayMap相比于HashMap更省内存
        ArrayMap<String, String> urlParams = param.getUrlArrayParams();

        //api_sign的时候需要给键值按首字母排序
        List<String> keys = new ArrayList<String>();
        keys.addAll(urlParams.keySet());
        Collections.sort(keys);

        for (int i = 0; i < urlParams.size(); i++) {
            String key = keys.get(i);
            String value = urlParams.get(key);

            if (value != null) {
                signBuilder.append(key).append("=").append(value).append("&");
                try {
                    urlEncodeBuilder.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    LogUtils.e("RequestUtil", e.toString());
                }

            } else {
                //此处打印需要一直暴露出来，所以不用LogUtil
                if(StringUtils.isEmpty(param.get("action") +"")){
                    Log.e("RequestUtil", "requets  key = " + key + " is null");
                }else{
                    Log.e("RequestUtil", "requets action =  " + param.get("action") + " | key = " + key + " is null");
                }
            }
        }
//        LogUtil.e("signBuilder-->",""+signBuilder.toString());
//        String apiSign = Catch.getInstance().catchA(signBuilder.toString());
//        LogUtil.e("apiSign-->",""+apiSign);
//        urlEncodeBuilder.append("api_sign=").append(apiSign);
        return urlEncodeBuilder.toString();
    }

    /**
     * 处理接口返回来的数据
     *
     * @param respString    返回数据的string
     * @param responseClass 目标Model
     * @return
     */
    public static BaseResponse processResponse(String respString, Class<?> responseClass) {//GsonRequest的parseNetworkResponse调用
        BaseResponse response = null;
        try {
            response = (BaseResponse) responseClass.newInstance();
            if (!StringUtils.isEmpty(respString)) {
                response = (BaseResponse) JsonUtils.toBean(respString, responseClass);
            } else {
                response.setStatus(JsonUtils.JsonCode.Exception);
                response.setMsg(JsonUtils.JsonMessage.Exception);
                LogUtils.e("RequestUtil", "返回报文为空");
            }
        } catch (IllegalStateException e) {
            LogUtils.e("RequestUtil", e.toString());

            response.setStatus(JsonUtils.JsonCode.Exception);
            response.setMsg(JsonUtils.JsonMessage.Exception);

            if (JsonUtils.isGoodJson(respString)) {
                try {
                    JSONObject json = new JSONObject(respString);
                    if (json.has("status")) {
                        response.setStatus(json.getString("status"));
                    }
                    if (json.has("msg")) {
                        response.setMsg(json.getString("msg"));
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

        } catch (Exception e) {
            LogUtils.e("RequestUtil", e.toString());

            response.setStatus(JsonUtils.JsonCode.BadJson);
            response.setMsg(JsonUtils.JsonMessage.BadJson);

            if (JsonUtils.isGoodJson(respString)) {
                try {
                    JSONObject json = new JSONObject(respString);
                    if (json.has("status")) {
                        response.setStatus(json.getString("status"));
                    }
                    if (json.has("msg")) {
                        response.setMsg(json.getString("msg"));
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return response;
    }
}
