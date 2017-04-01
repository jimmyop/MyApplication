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

/**
 * Created by chenjiaming1 on 2017/3/1.
 */

public class TestAccessibilityServiceActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        String gif = "http://ww2.sinaimg.cn/large/85cccab3gw1ete77tv9atg20b40684qq.gif";

        SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv);

        Uri uri = Uri.parse("http://img.huofar.com/data/jiankangrenwu/shizi.gif");
//        Uri uri = Uri.parse("res://drawable-hdpi/"+R.drawable.gif1);
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        mSimpleDraweeView.setController(draweeController);

        ListView mLis = (ListView) findViewById(R.id.list);

        LinearLayout header = (LinearLayout) getLayoutInflater().inflate(R.layout.header, null);
        mLis.addHeaderView(header);
        String[] aa = {"1dasdas", "2asdasda", "3dasdasda"};
        mLis.setAdapter(new ArrayAdapter<String>(TestAccessibilityServiceActivity.this,
                android.R.layout.simple_list_item_1, aa));

    }
}
