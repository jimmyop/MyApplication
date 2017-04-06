
package com.jimmy.commonlibrary.net;

public interface HttpRequest {
    /**
     * 接口请求成功的回调，只有status为"0000"才回回调此函数
     *
     * @param taskid 请求的唯一标识
     * @param resp   接口返回的具体Model
     */
    public void requestSuccese(String taskid, BaseResponse resp);

    /**
     * 网络异常的回调，如没有网络，网络超时会回调此函数
     * @param taskid 请求的唯一标识
     * @param status 具体的错误码标识
     * @param resp 接口返回的具体Model
     */
    public void requestError(String taskid, String status, BaseResponse resp);

    /**
     * 接口请求异常回调，一般是业务错误，并带有错误msg提示
     * @param taskid 请求的唯一标识
     * @param status 具体的错误码标识
     * @param resp 接口返回的具体Model
     */
    public void requestException(String taskid, String status, BaseResponse resp);

}
