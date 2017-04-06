package com.jimmy.commonlibrary.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by chenjiaming1 on 2017/3/30.
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;

    public static BaseApplication getApplicationInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

}
