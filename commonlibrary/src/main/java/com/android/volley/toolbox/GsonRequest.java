package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.jimmy.commonlibrary.net.BaseResponse;
import com.jimmy.commonlibrary.net.RequestParams;
import com.jimmy.commonlibrary.net.RequestUtil;
import com.jimmy.commonlibrary.utils.JsonUtils;
import com.jimmy.commonlibrary.utils.LogUtils;
import com.jimmy.commonlibrary.utils.StringUtils;

/**
 * 自定义GsonRequest是为了能在子线程里将json转成具体的类
 * <p>
 * Created by asdzheng on 2015/10/31.
 */
public class GsonRequest<T> extends Request<T> {
    private final String TAG = this.getClass().getSimpleName();

    private final Response.Listener<T> mListener;

    private Gson mGson;

    private RequestParams params;

    private Class<T> mClass;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener, RequestParams params) {
        super(method, url, errorListener);
        mListener = listener;
        mGson = new Gson();
        mClass = clazz;
        this.params = params;
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener, RequestParams params) {
        this(Method.GET, url, clazz, listener, errorListener, params);
    }

    /**
     * 处理网络接口的返回结果，把json转成model后再传回主线程处理
     *
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            //数据是以字节的形式存放在NetworkResponse的data变量中的，这里将数据取出然后组装成一个String，并传入Response的success()方法中即可
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if(StringUtils.isEmpty(params.getString("action"))){
                LogUtils.i(TAG,  "jsonString: " + jsonString);
            }else{
                LogUtils.i(TAG, params.getString("action") + ": " + jsonString);
            }

//            LogUtil.e("TrafficStats-total-",""+ TrafficStats.getTotalRxBytes());

//            if (response.headers.containsKey("response-sign")) {
//                String responseSign = response.headers.get("response-sign");
////                String apiSign = Catch.getInstance().catchA(jsonString);
//                if (!responseSign.trim().equals(apiSign.trim())) {
//                    BaseResponse baseResponse = new BaseResponse();
//                    baseResponse.setStatus(JsonUtils.JsonCode.EditedJson);
//                    baseResponse.setMsg(JsonUtils.JsonMessage.EditedJson);
//                    return Response.success((T) baseResponse, HttpHeaderParser.parseCacheHeaders(response));
//                }
//            }

            return Response.success((T) RequestUtil.processResponse(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {//UnsupportedEncodingException
            LogUtils.e(TAG, e.toString());
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setStatus(JsonUtils.JsonCode.BadJson);
            baseResponse.setMsg(JsonUtils.JsonMessage.BadJson);
            return Response.success((T) baseResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
