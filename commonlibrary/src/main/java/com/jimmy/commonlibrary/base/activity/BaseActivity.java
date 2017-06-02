package com.jimmy.commonlibrary.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jimmy.commonlibrary.R;
import com.jimmy.commonlibrary.widget.TitleHeadLayout;

import butterknife.ButterKnife;

/**
 * Created by chenjiaming1 on 2017/2/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private LinearLayout rootLayout;
    private TitleHeadLayout mTitlelayout;
    private ViewGroup mContainerLayout;
    private View contentView;

    @LayoutRes
    protected int getRootLayout() {
        return R.layout.base_toolbar_container;
    }

    @LayoutRes
    protected abstract int getContentLayout();

    /**
     * 是否有标题拦
     *
     * @return
     */
    protected boolean hasTitleLayout() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getRootLayout());

        rootLayout = ButterKnife.findById(this, R.id.root_layout);
        mTitlelayout = ButterKnife.findById(this, R.id.title_head_layout);
        mContainerLayout = ButterKnife.findById(this, R.id.container_layout);

        LayoutInflater inflater = LayoutInflater.from(this);
        contentView = inflater.inflate(getContentLayout(), mContainerLayout, false);
        mContainerLayout.addView(contentView);

        ButterKnife.bind(this);
        initView();

        // 判断有没有设置TitleLayout
        if (hasTitleLayout()) {
            initHeaderView(mTitlelayout);
        } else {
            rootLayout.removeView(mTitlelayout);
        }

        initData(savedInstanceState);
    }

    /**
     * 初始化Title布局
     *
     * @param headLayout
     */
    protected void initHeaderView(TitleHeadLayout headLayout) {}

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public TitleHeadLayout getTitlelayout() {
        return mTitlelayout;
    }
}
