package com.jimmy.commonlibrary.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by zengxiangbin on 2016/7/20.
 */
public class DoubleClickExitHelper {

    private static final String DEFAULT_TOAST = "再按一次退出应用";

    private Activity mActivity;
    private int mTimeInterval = 2000;
    private boolean isOnBacking = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Toast mToast;

    public DoubleClickExitHelper(Activity activity){
        mActivity = activity;
        mToast = Toast.makeText(mActivity, DEFAULT_TOAST, Toast.LENGTH_LONG);
    }

    public DoubleClickExitHelper setToastContent(String content){
        mToast = Toast.makeText(mActivity, content, Toast.LENGTH_LONG);
        return this;
    }


    public DoubleClickExitHelper setToastContent(int resid){
        mToast = Toast.makeText(mActivity, resid, Toast.LENGTH_LONG);
        return this;
    }

    public DoubleClickExitHelper setTimeInterval(int timeInterval){
        this.mTimeInterval = timeInterval;
        return this;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode != KeyEvent.KEYCODE_BACK){
            return false;
        }

        if (isOnBacking){
            mHandler.removeCallbacks(mBackingRunnable);
            mToast.cancel();
            mActivity.finish();
        }else {
            isOnBacking = true;
            mToast.show();
            mHandler.postDelayed(mBackingRunnable, mTimeInterval);
        }

        return true;
    }

    private Runnable mBackingRunnable = new Runnable() {
        @Override
        public void run() {
            isOnBacking = false;
            mToast.cancel();
        }
    };
}
