package com.jimmy.commonlibrary.widget.loadingmanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.jimmy.commonlibrary.utils.ToastUtils;

/**
 * 只显示Toast
 * Created by lixinyu on 2016/12/13.
 */

public class ToastLoadingManager implements LoadingManager {
    private Context mContext;


    public ToastLoadingManager(Context context) {
        this.mContext = context;
    }


    /***
     * 当请求失败时，是否需要把该请求移除
     * 不同的加载方式会影响到要不要移除失败的请求
     *
     * @return
     */
    @Override
    public boolean needRemoveRequest() {
        return true;
    }

    /***
     * 显示初始加载显示的View
     *
     * @param parentView
     * @param params
     * @return
     */
    @Override
    public View showBeginLoaingView(ViewGroup parentView, ViewGroup.LayoutParams params) {
        return null;
    }

    /***
     * 显示网络错误的提示View
     *
     * @param parentView
     * @param params
     * @return
     */
    @Override
    public View showNetErrorView(ViewGroup parentView, ViewGroup.LayoutParams params) {
        ToastUtils.toast(mContext, "网络不佳，请稍后再试");
        return null;
    }

    /***
     * 显示数据为空
     *
     * @param parentView
     * @param msg        数据为空的提示语(为空表示使用默认的空提示)
     * @param params
     * @return
     */
    @Override
    public View showEmptyDataView(ViewGroup parentView, String msg, ViewGroup.LayoutParams params) {
        if (!TextUtils.isEmpty(msg)) {

            ToastUtils.toast(mContext, msg);
        } else {
            ToastUtils.toast(mContext, "这里什么也没有，请稍后再试");
        }
        return null;
    }

    /***
     * 显示服务器返回数据错误的提示view
     *
     * @param parentView
     * @param code
     * @param msg
     * @param params     @return
     */
    @Override
    public View showResponseErrorView(ViewGroup parentView, int code, String msg, ViewGroup.LayoutParams params) {
        ToastUtils.toast(mContext, msg);
        return null;
    }

    /***
     * 请求成功了
     *
     * @param parentView
     * @return
     */
    @Override
    public View showSuccessView(ViewGroup parentView) {
        return null;
    }

    /***
     * 设置重新加载数据的点击回调
     *
     * @param onBgViewClickListener
     */
    @Override
    public void setReloadDataWhenClick(View.OnClickListener onBgViewClickListener) {

    }

    /***
     * 清楚view
     */
    @Override
    public void clearViews() {

    }

    /***
     * 获得自身UI的type
     *
     * @return
     */
    @Override
    public LoadingUiType uiType() {
        return LoadingUiType.TOAST;
    }

    @Override
    public void setCancleClickListener(View.OnClickListener cancleClickListener) {

    }
}
