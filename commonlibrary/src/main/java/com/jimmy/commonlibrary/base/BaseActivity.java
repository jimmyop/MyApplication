package com.jimmy.commonlibrary.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chenjiaming1 on 2017/2/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData(savedInstanceState);
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

}
