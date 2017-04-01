package com.jimmy.commonlibrary.base.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jimmy.commonlibrary.R;
import com.jimmy.commonlibrary.widget.meituan.MeiTuanPullRefreshLayout;

import butterknife.ButterKnife;

/**
 * Created by chenjiaming1 on 2017/3/28.
 */

public class BaseRequestRecyclerViewActivity extends BaseRequestActivity {

    protected static final int PAGE_SIZE = 10;
    protected static final String PARAM_PAGE_INDEX = "pageIndex";
    protected static final String PARAM_PAGE_SIZE = "pageSize";

    MeiTuanPullRefreshLayout mPtrLayout;
    RecyclerView mRecyclerView;

    protected RecyclerView.LayoutParams pl_show;
    protected RecyclerView.LayoutParams pl_hide;

    private boolean isLoadOver = true;// 是否已加载完成的开关

    @Override
    protected int getContentLayout() {
        return R.layout.base_ptr_recyclerview_layout;
    }

    @Override
    protected void initView() {
        mRecyclerView = ButterKnife.findById(this, R.id.recycler_view);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected MeiTuanPullRefreshLayout getPtrLayout() {
        return mPtrLayout;
    }
}
