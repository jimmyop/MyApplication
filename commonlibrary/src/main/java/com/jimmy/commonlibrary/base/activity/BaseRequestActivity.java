package com.jimmy.commonlibrary.base.activity;

import com.jimmy.commonlibrary.net.HttpRequest;
import com.jimmy.commonlibrary.net.RequestHelper;
import com.jimmy.commonlibrary.net.RequestParams;

/**
 * Created by chenjiaming on 2017/2/20.
 */

public abstract class BaseRequestActivity extends BaseActivity implements HttpRequest {

    protected String requestDate(Class clazz, RequestParams mRequestParams) {
       return RequestHelper.getInstance().requestData(clazz, this, mRequestParams);
    }
}
