package com.agile.merchant.utils;

import android.content.Context;

import com.agile.merchant.data.bean.address.Province;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zengxiangbin on 2016/5/20.
 */
public class AddressUtils {
    private AddressUtils(){}

    /**
     * 获取省市区列表
     * @param context
     * @return
     */
    public static List<Province> getProvinceList(Context context){
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open("address.txt");
            Gson gson = new Gson();
            final Province[] provices = gson.fromJson(new InputStreamReader(is), Province[].class);
            return Arrays.asList(provices);

        }catch (final Exception e){
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }
}
