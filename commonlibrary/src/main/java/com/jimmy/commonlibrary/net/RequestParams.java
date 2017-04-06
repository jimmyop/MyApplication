
package com.jimmy.commonlibrary.net;

import android.support.v4.util.ArrayMap;

import com.android.volley.Request.Method;

import java.util.Map;

/**
 * 请求参数设置，如果是请求接口请求参数的话，用put(key, value)方法设值 如果是请求连接的设置，用对应的方法设置，如responseClass,
 * method, commonUrl
 *
 * @author asdzheng
 */
public class RequestParams {

    private boolean isToastErrorMsg = true;
    /**
     * 延迟回调接口，主要用于延迟刷新页面功能
     */
    private boolean isDelayResponse = false;

    private ArrayMap<String, String> urlArrayParams = null;
    /**
     * 接口返回来的具体model
     */
    private Class<?> clz;

    // 不同的url请求连接，默认为interface类型
    private String urlType = "";
    private int method = Method.GET;

    private HttpRequest http;
    private Object tag;
    private String task_id;

    /**
     * 初始化请求参数，会将一些基本参数先初始化
     */
    public RequestParams() {
        urlArrayParams = new ArrayMap<>();
        // 版本号没有特别说明的话是必传的，且是1.0
//        urlArrayParams.put("ver", "1.0");
//        urlArrayParams.put("system_version", android.os.Build.VERSION.RELEASE + "");
//        urlArrayParams.put("utm_medium", "android");
//        urlArrayParams.put("city_id", PreferencesUtil.getInstance().getCityId());
//        urlArrayParams.put("device_id", PreferencesUtil.getInstance().getDeviceId());
//        urlArrayParams.put("qyd_token", DexUtil.getInstance().decryptDES(PreferencesUtil.getInstance().getQydToken()));
        //一些公共参数
//        urlArrayParams.put("utm_source",
//                Config.CHANNEL);

//        if (DisplayUtil.getDeviceHeight() > 0 && DisplayUtil.getDisplayWidth() > 0) {
//            urlArrayParams.put("display_width", DisplayUtil.getDisplayWidth() + "");
//            urlArrayParams.put("display_height", DisplayUtil.getDeviceHeight() + "");
//        }

//        urlArrayParams.put("app_version",
//                NingMiApplication.getApplicationInstance().getString(R.string.version));
//        urlArrayParams.put("client_time", DateUtil.getNowNetTime() + "");

    }

    public RequestParams(Map<String, String> map) {
        this();
        urlArrayParams.putAll(map);
    }

    public void put(String key, String value) {
        urlArrayParams.put(key, value);
    }

    public Object get(String key) {
        return urlArrayParams.get(key);
    }

    public String getString(String key) {
        Object object = get(key);
        return object == null ? null : object.toString();
    }

    public boolean has(String key) {
        return this.urlArrayParams.containsKey(key);
    }

    public String removeKey(String key) {
        return (String) urlArrayParams.remove(key);
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Class<?> getResponseClass() {
        return clz;
    }

    public void setResponseClass(Class<?> clz) {
        this.clz = clz;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    /**
     * 有些接口比较特殊，CommonUrl没有对应的url类型，需要url加key来拼接
     *
     * @param urlType
     * @param key
     * @如： set_pay_password接口，为： CommonUrl.INTERFACE_APP + "paypassword?"
     */
    public void setUrlType(String urlType, String key) {
        this.urlType = urlType + key;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public HttpRequest getHttp() {
        return http;
    }

    public void setHttp(HttpRequest http) {
        this.http = http;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 是否在出现接口异常的时候，toast出错误信息，默认会弹
     */
    public boolean isToastErrorMsg() {
        return isToastErrorMsg;
    }

    /**
     * 是否在出现接口异常的时候，toast出错误信息，默认会弹
     */
    public void setToastErrorMsg(boolean isToastErrorMsg) {
        this.isToastErrorMsg = isToastErrorMsg;
    }


    public boolean isDelayResponse() {
        return isDelayResponse;
    }

    /**
     * 设置是否延迟回掉response，此设置针对的是为了延迟消失下拉框架里的加载框
     *
     * @param isDelayResponse
     */
    public void setDelayResponse(boolean isDelayResponse) {
        this.isDelayResponse = isDelayResponse;
    }

    /**
     * 获取请求的所有参数
     */
    public void clearUrlArrayParams() {
        urlArrayParams.clear();
    }

    public ArrayMap<String, String> getUrlArrayParams() {
        return urlArrayParams;
    }
}
