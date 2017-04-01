package com.jimmy.commonlibrary.request;

/**
 * Created by chenjiaming1 on 2017/4/1.
 */

public abstract class HttpCallBack<T> {

    public abstract void onRequestSuccess(T data);

    public abstract void onRequestFail(int code, String msg);

    public abstract void onRequestError(String msg);

}
