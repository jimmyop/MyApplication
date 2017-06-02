package com.jimmy.commonlibrary.base.activity;

import android.os.Bundle;

import com.jimmy.commonlibrary.net.BaseResponse;
import com.jimmy.commonlibrary.net.HttpRequest;
import com.jimmy.commonlibrary.net.RequestHelper;
import com.jimmy.commonlibrary.net.RequestParams;

/**
 * Created by chenjiaming on 2017/2/20.
 */

public class BaseRequestActivity extends BaseActivity implements HttpRequest {

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void requestSuccese(String taskid, BaseResponse resp) {

    }

    @Override
    public void requestError(String taskid, String status, BaseResponse resp) {

    }

    @Override
    public void requestException(String taskid, String status, BaseResponse resp) {

    }

    protected String requestDate(Class clazz, RequestParams mRequestParams) {
       return RequestHelper.getInstance().requestData(clazz, this, mRequestParams);
    }
}
