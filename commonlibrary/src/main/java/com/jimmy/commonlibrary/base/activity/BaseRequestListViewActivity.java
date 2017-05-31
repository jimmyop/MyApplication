package com.jimmy.commonlibrary.base.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.jimmy.commonlibrary.R;
import com.jimmy.commonlibrary.net.BaseResponse;
import com.jimmy.commonlibrary.widget.meituan.MeiTuanPullRefreshLayout;

import butterknife.ButterKnife;

/**
 * Created by chenjiaming1 on 2017/3/28.
 */

public abstract class BaseRequestListViewActivity extends BaseRequestActivity {

    protected static final int PAGE_SIZE = 10;
    protected static final String PARAM_PAGE_INDEX = "pageIndex";
    protected static final String PARAM_PAGE_SIZE = "pageSize";

    MeiTuanPullRefreshLayout mPtrLayout;
    ListView mListView;

    protected ListView.LayoutParams pl_show;
    protected ListView.LayoutParams pl_hide;

    private boolean isLoadOver = true;// 是否已加载完成的开关

    @Override
    protected int getContentLayout() {
        return R.layout.base_ptr_listview_layout;
    }

    @Override
    protected void initView() {
        mListView = ButterKnife.findById(this, R.id.list_view);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    protected ListView getListView() {
        return mListView;
    }

    protected MeiTuanPullRefreshLayout getPtrLayout() {
        return mPtrLayout;
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


    /***
     *
     * @param response
     * @param tag
     * @param isLoadMore 是否是加载更多
     */
    public abstract void onBindData(Object response, Object tag,boolean isLoadMore);
}
