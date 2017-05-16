package com.jimmy.account.ui.activities;

import android.os.Bundle;

import com.jimmy.account.R;
import com.jimmy.commonlibrary.base.activity.BaseActivity;
import com.jimmy.commonlibrary.utils.LogUtils;
import com.jimmy.commonlibrary.widget.TitleHeadLayout;

/**
 * Author: chenjiaming1
 * Created on: 2017/5/15.
 * Description:
 */

public class RxJavaTestActivity extends BaseActivity {

    @Override
    protected void initHeaderView(TitleHeadLayout headLayout) {
        super.initHeaderView(headLayout);

        headLayout.setTitleText("RxJavaTestRxJavaTestRxJavaTest");

        headLayout.setRightFirstImageResource(R.drawable.ic_menu_camera);

        headLayout.setRightSecondImageResource(R.drawable.ic_menu_manage);


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_rxjava_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
