package com.jimmy.commonlibrary.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jimmy.commonlibrary.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenjiaming1 on 2017/4/1.
 */

public class RequestHandler {

    public static final int NET_ERROR_VOLLEY = -2;

    private static void addRequest(
            int method,
            final Handler handler, final int what,
            final Bundle bundle, String url, final Map<String, String> params, final Map<String, String> header,
            final NetWorkRequestListener listener) {
        if (method == Request.Method.GET) {
            url = NetworkHelper.getUrlWithParams(url, params);
        }
        listener.onPreRequest();
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                onVolleyResponse(response, handler, what, bundle);
                listener.onResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onVolleyErrorResponse(volleyError, listener, handler, bundle);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = header;
                if (map == null) {
                    map = new HashMap<>();
                }
                // 在此统一添加header
                map.put("versionName", BuildConfig.VERSION_NAME);
                return map;
            }

            /**
             * Volley仅在post的情况下会回调该方法，获取form表单参数
             * @return
             * @throws AuthFailureError
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        MyApplication.getRequestQueue().add(request);
    }

    private static void onVolleyErrorResponse(VolleyError volleyError, NetWorkRequestListener listener, Handler handler, Bundle bundle) {
        if (listener.retry()) {
            listener.onFailed();
            return;
        }
        Message msg = handler.obtainMessage(NET_ERROR_VOLLEY);
        msg.setData(bundle);
        handler.sendMessage(msg);
        listener.onFailed();
    }

    private static void onVolleyResponse(String response, Handler handler, int what, Bundle bundle) {
        Message msg = handler.obtainMessage(what, response);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * @param method  Request.Method.GET 或 Request.Method.POST
     * @param handler 请求结束后将结果作为Message.obj发送到该Handler
     * @param what    请求结束后发送的Message.what
     * @param bundle  不参与网络请求，仅携带参数
     *                （请求结束后，通过Message.setData设置到Message对象，数据原样返回）
     * @param url     请求地址
     * @param params  请求参数
     * @param header  请求头
     */
    public static void addRequest(
            final int method, final Handler handler, final int what, final Bundle bundle,
            final String url, final Map<String, String> params, final Map<String, String> header) {
        addRequest(method, handler, what, bundle, url, params, header, new DefaultRequestListener() {
            @Override
            public boolean retry() {
                addRequest(method, handler, what, bundle, url, params, header,
                        retryTimer++ >= MAX_RETRY_TIME ? new DefaultRequestListener() : this);
                return true;
            }
        });
    }

    public static void addRequestWithDialog(
            final int method, Context context, final Handler handler, final int what, final Bundle bundle,
            final String url, final Map<String, String> params, final Map<String, String> header) {
        addRequest(method, handler, what, bundle, url, params, header, new DefaultDialogRequestListener(context) {
            @Override
            public boolean retry() {
                addRequest(method, handler, what, bundle, url, params, header,
                        retryTimer++ >= MAX_RETRY_TIME ? new DefaultDialogRequestListener(context) : this);
                return true;
            }
        });
    }


    /**
     * 请求过程中显示加载对话框，且自动处理其生命周期
     */
    private static class DefaultDialogRequestListener extends DefaultRequestListener {

        Context context;
        ProgressDialog dialog;

        public DefaultDialogRequestListener(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        public void onPreRequest() {
            dialog.show();
        }

        @Override
        public void onResponse() {
            dialog.dismiss();
        }

        @Override
        public void onFailed() {
            dialog.dismiss();
        }
    }

    private static class DefaultRequestListener implements NetWorkRequestListener {

        int retryTimer;

        static final int MAX_RETRY_TIME = 3;

        @Override
        public void onPreRequest() {

        }

        @Override
        public void onResponse() {

        }

        @Override
        public void onFailed() {

        }

        @Override
        public boolean retry() {
            return false;
        }
    }

    /**
     * 用于所有网络请求，在不同时机回调的接口
     */
    private static interface NetWorkRequestListener {
        void onPreRequest();

        void onResponse();

        void onFailed();

        boolean retry();
    }
}
