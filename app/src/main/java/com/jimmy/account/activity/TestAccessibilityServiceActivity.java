package com.jimmy.account.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jimmy.account.R;

/**
 * Created by chenjiaming1 on 2017/3/1.
 */

public class TestAccessibilityServiceActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ListView mLis = (ListView) findViewById(R.id.list);

        LinearLayout header = (LinearLayout)getLayoutInflater().inflate(R.layout.header,null);
        mLis.addHeaderView(header);
        String[] aa = {"1dasdas","2asdasda","3dasdasda"};
        mLis.setAdapter(new ArrayAdapter<String>(TestAccessibilityServiceActivity.this,
                android.R.layout.simple_list_item_1, aa));

    }
}
