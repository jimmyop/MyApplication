
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

//        String requestAction = requestParams.get("action") + "";
//        if (isAboutTokenRequest(requestAction)) {//是否是和Token相关的接口
//            isChangingQyd = true;//true为和Token相关的接口
//
//            if (requestMap.size() > 0) {//之前的队列中是否有正在请求的接口
//
//                isQydWaiting = true;//若之前的队列中有正在请求的接口，则等待前面的接口执行完成，若不等待有可能新的请求后台更新Token，导致之前的请求失败
//
//                // qyd 相关的接口进来之前,有其他接口进行中，qyd相关的进行等待，待前面的接口完成后，再执行
//                BlockEntity entity = new BlockEntity();
//                entity.setMethod(method);
//                entity.setTag(tag);
//                entity.setTaskid(taskid);
//                entity.setHttp(http);
//                entity.setParams(requestParams);
//                blockingRequestMap.put(requestParams.get("action") + "", entity);
//            } else {
//                //直接加入请求队列
//                queue.add(getGsonRequest(tag, taskid, http, requestParams));
//            }
//
//        } else {
//            // 判断 影响qyd_token的接口是否在等待，若等待则将请求加入缓存队列blockingRequestMap
//            if (isQydWaiting || isChangingQyd) {
//                BlockEntity entity = new BlockEntity();
//                entity.setMethod(method);
//                entity.setTag(tag);
//                entity.setTaskid(taskid);
//                entity.setHttp(http);
//                entity.setParams(requestParams);
//
//                blockingRequestMap.put(requestParams.get("action") + "", entity);
//
//            } else {
//                // 正常加入到消息队列
//                queue.add(getGsonRequest(tag, taskid, http, requestParams));
//            }
//        }
    }

    private boolean isChangingQyd = false;
    private boolean isQydWaiting = false;


    //判断 是否是影响 token的接口
    private boolean isAboutTokenRequest(String ation) {

        if (ation.equals("get_qyd_token") || ation.equals("union_login") || ation.equals("union_phone")
                || ation.equals("login") || ation.equals("quick_login") || ation.equals("update_password")
                || ation.equals("set_password") || ation.equals("reset_password") || ation.equals("logout"))
            return true;
        return false;
    }

    /*放开阻塞队列*/
    private void startBlockingRequest() {

//        LogUtil.e("Do block----", "start--" + System.currentTimeMillis());

        java.util.Iterator it = blockingRequestMap.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            BlockEntity entity = (BlockEntity) entry.getValue();

            if (isAboutTokenRequest(entry.getKey() + "")) {//此条件是为了避免此时在发起请求如果和Token无关会直接进入队列请求数据，Token校验失败
                isChangingQyd = true;
                if (requestMap.size() == 0) {
                    isQydWaiting = true;
                    queue.add(getGsonRequest(entity.getTag(), entity.getTaskid(), entity.getHttp(), entity.getParams()));
//                    LogUtil.e("before remove--1-",""+blockingRequestMap.size());
                    it.remove();
//                    LogUtil.e("after remove--1-",""+blockingRequestMap.size());
                } else {


//                    LogUtil.e("Do disapper----", entry.getKey() + "");

                }
                break;
            } else {
                queue.add(getGsonRequest(entity.getTag(), entity.getTaskid(), entity.getHttp(), entity.getParams()));
//                LogUtil.e("before remove--2-",""+blockingRequestMap.size());
                it.remove();
//                LogUtil.e("after remove--2-",""+blockingRequestMap.size());
            }

//            LogUtil.e("Do block----", "doing--" + System.currentTimeMillis());
        }

//        LogUtil.e("Do block----", "end--" + System.currentTimeMillis());

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

//                    if (baseResponse.getStatus().equals("-15")) { //更新qyd_token
//                        EventBusUtil.postUpdateQYDToken();
//                    }

                    http.requestException(taskid, baseResponse.getStatus(), baseResponse);

                }
                requestParams.clearUrlArrayParams();

//                LogUtil.e("requestMap  remove  action--1--", "" + requestMap.get(taskid));
                requestMap.remove(taskid);
//                doAfterRequest();

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

//                LogUtil.e("requestMap  remove  action--2--", "" + requestMap.get(taskid));
                requestMap.remove(taskid);
//                doAfterRequest();
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

        LogUtils.w(tagName, "Sending action " + requestParams.get("action") + " request to " + url);

        return request;
    }

    private void doAfterRequest() {
//        LogUtil.e("requestMap  size--1--", "" + requestMap.size());
        if (requestMap.size() == 0) {
            isChangingQyd = false;
            isQydWaiting = false;

            if (blockingRequestMap.size() > 0)
                startBlockingRequest();
        }

//        LogUtil.e("requestMap  size--2--", "" + requestMap.size());
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
