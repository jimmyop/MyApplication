package com.jimmy.commonlibrary.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimmy.commonlibrary.R;

import butterknife.ButterKnife;

/**
 * Created by chenjiaming1 on 2017/2/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewGroup mContainerLayout;

    @LayoutRes
    protected int getRootLayout() {
        if (hasToolbar()) {
            return R.layout.base_toolbar_container;
        } else {
            return R.layout.base_container;
        }
    }

    @LayoutRes
    protected abstract int getContentLayout();

    /**
     * 是否显示导航栏
     *
     * @return
     */
    protected boolean hasToolbar() {
        return true;
    }

    /**
     * 标题是否有返回按钮
     *
     * @return
     */
    protected boolean navigationUp() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getRootLayout());

        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        mContainerLayout = ButterKnife.findById(this, R.id.container_layout);

        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(getContentLayout(), mContainerLayout, false);
        mContainerLayout.addView(contentView);

        ButterKnife.bind(this);
        setupToolbar();
        initView();
        initData(savedInstanceState);
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (navigationUp()) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_icon);
            }
        }
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
