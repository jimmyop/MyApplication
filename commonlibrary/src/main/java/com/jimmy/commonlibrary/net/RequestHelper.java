
package com.jimmy.commonlibrary.net;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.Volley;
import com.jimmy.commonlibrary.base.BaseApplication;
import com.jimmy.commonlibrary.utils.JsonUtils;
import com.jimmy.commonlibrary.utils.LogUtils;
import com.jimmy.commonlibrary.utils.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RequestHelper {

    // 默认请求接口的超时时间30s
    private static final int DEFAULT_TIMEOUT_MS = 30 * 1000;

    /**
     * 后台接口请求成功标志
     */
    public static final String SUCCESS_STATUS = "0000";
    /**
     * 如果需要下拉刷新延迟显示的，默认延迟800ms
     */
    private static final long delayTime = 800;

    private final String TAG = this.getClass().getSimpleName();

    /**
     * 请求接口的唯一队列，
     */
    private RequestQueue queue;

    /**
     * 单例模式
     */
    private static RequestHelper helper;

    private Context appcontext;

    LinkedHashMap<String, String> requestMap = new LinkedHashMap<>();//请求的队列
    LinkedHashMap<String, BlockEntity> blockingRequestMap = new LinkedHashMap<>();//等待的队列

    private RequestHelper() {
        appcontext = BaseApplication.getApplicationInstance().getApplicationContext();
        //初始化队列
        queue = Volley.newRequestQueue(appcontext);
    }

    public static synchronized RequestHelper getInstance() {
        if (helper == null) {
            helper = new RequestHelper();
        }
        return helper;
    }

    public String requestData(Object tag, HttpRequest http, RequestParams param) {

        String taskid = RequestUtil.getUrl(param);
        request(param.getMethod(), tag, taskid, http, param);
        return taskid;
    }

    /**
     * @param method        请求的方法，现在都为GET，POST还未做处理
     * @param tag           为每个请求打的标签，退出界面时可取消请求
     * @param http          请求的回调接口
     * @param requestParams 请求需要的参数和返回的的类
     */
    @SuppressWarnings("unchecked")
    private void request(int method, Object tag, final String taskid, final HttpRequest http,
                         final RequestParams requestParams) {
        final String tagName = tag.getClass().getSimpleName();

        queue.add(getGsonRequest(tag, taskid, http, requestParams));
    }

    @SuppressWarnings("unchecked")
    private GsonRequest getGsonRequest(Object tag, final String taskid, final HttpRequest http,
                                       final RequestParams requestParams) {
        final String tagName = tag.getClass().getSimpleName();
        final long startRequestTime = System.currentTimeMillis();

        requestMap.put(taskid, requestParams.get("action") + "");//添加请求到队列中

        final String url = RequestUtil.getUrl(requestParams);

        GsonRequest request = new GsonRequest(url, requestParams.getResponseClass(), new Response.Listener<BaseResponse>() {

            @Override
            public void onResponse(final BaseResponse baseResponse) {

                if (SUCCESS_STATUS.equals(baseResponse.getStatus())) {

                    //下拉刷新的接口如果刷新太快，显示加载控件就会闪一下，不太好，这里做一下数据刷新延迟，让控件加载一段时间再消失
                    if (requestParams.isDelayResponse()) {
                        long requestTime = System.currentTimeMillis() - startRequestTime;

                        if (requestTime < delayTime) {

                            LogUtils.i(tagName, "requestTime = " + requestTime + " || delayTime = " + (delayTime - requestTime));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    http.requestSuccese(taskid, baseResponse);
                                }
                            }, delayTime - requestTime);

                        } else {
                            http.requestSuccese(taskid, baseResponse);
                        }

                    } else {
                        http.requestSuccese(taskid, baseResponse);
                    }

                } else {
                    if (requestParams.isToastErrorMsg()) {
                        Toast.makeText(appcontext, baseResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    http.requestException(taskid, baseResponse.getStatus(), baseResponse);

                }
                requestParams.clearUrlArrayParams();

                requestMap.remove(taskid);

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                LogUtils.e(tagName, error.toString());

                if (requestParams.isToastErrorMsg()) {
                    Toast.makeText(appcontext, "网络连接错误", Toast.LENGTH_SHORT).show();
                }

                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStatus(JsonUtils.JsonCode.Http);
                baseResponse.setMsg(JsonUtils.JsonMessage.Http);
                http.requestError(taskid, JsonUtils.JsonCode.Http, baseResponse);

                requestParams.clearUrlArrayParams();

                requestMap.remove(taskid);
            }
        }, requestParams);

        //tag是作为取消请求的标志，一般为Activity
        request.setTag(tag);
        // 现在的接口数据都不需要Volley做缓存
        request.setShouldCache(false);
        // 重试策略的设置，Volley默认超时时间为2.5s，现改为30s
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        String action = requestParams.get("action") + "";

        if (StringUtils.isEmpty(action)) {
            LogUtils.w(tagName, "Sending  request to " + url);

        } else {
            LogUtils.w(tagName, "Sending action " + action + " request to " + url);
        }
        return request;
    }

    /**
     * 取消掉某个Activity的所有请求
     *
     * @param tag 具体的Activity
     */
    public void cancelAll(Object tag) {

        requestMap.clear();
        blockingRequestMap.clear();
        queue.cancelAll(tag);
    }


    private class BlockEntity implements Serializable {

        private int method;
        private Object tag;
        private String taskid;
        private HttpRequest http;
        private RequestParams params;

        public int getMethod() {
            return method;
        }

        public void setMethod(int method) {
            this.method = method;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public HttpRequest getHttp() {
            return http;
        }

        public void setHttp(HttpRequest http) {
            this.http = http;
        }

        public RequestParams getParams() {
            return params;
        }

        public void setParams(RequestParams params) {
            this.params = params;
        }
    }

}
