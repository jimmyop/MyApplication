package com.jimmy.account.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jimmy.account.R;
import com.jimmy.commonlibrary.base.activity.BaseRequestActivity;
import com.jimmy.commonlibrary.net.BaseResponse;
import com.jimmy.commonlibrary.net.RequestParams;

/**
 * Created by chenjiaming1 on 2017/3/1.
 */

public class TestAccessibilityServiceActivity extends BaseRequestActivity {

    SimpleDraweeView mSimpleDraweeView;
    ListView mLis;

    @Override
    protected int getContentLayout() {
        return R.layout.test;
    }

    @Override
    protected void initView() {

        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv);
        mLis = (ListView) findViewById(R.id.list);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Uri uri = Uri.parse("http://img.huofar.com/data/jiankangrenwu/shizi.gif");
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mSimpleDraweeView.setController(draweeController);


        LinearLayout header = (LinearLayout) getLayoutInflater().inflate(R.layout.header, null);
        mLis.addHeaderView(header);
        String[] aa = {"1dasdas", "2asdasda", "3dasdasda"};
        mLis.setAdapter(new ArrayAdapter<String>(TestAccessibilityServiceActivity.this,
                android.R.layout.simple_list_item_1, aa));

        req();

    }

    @Override
    public void requestSuccese(String taskid, BaseResponse resp) {

        if (taskid.equals(taskId)) {


        }
    }

    @Override
    public void requestError(String taskid, String status, BaseResponse resp) {

    }

    @Override
    public void requestException(String taskid, String status, BaseResponse resp) {

    }

    String taskId;

    private void req() {
        RequestParams requestParams = new RequestParams();
        requestParams.setUrlType("http://gank.io/api/data/Android/10/1");

        taskId = requestDate(getClass(), requestParams);
    }
}
