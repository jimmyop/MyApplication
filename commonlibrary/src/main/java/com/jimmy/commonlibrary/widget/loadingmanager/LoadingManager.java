package com.jimmy.commonlibrary.widget.loadingmanager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public interface LoadingManager{
	/***
	 *  当请求失败时，是否需要把该请求移除
	 *  不同的加载方式会影响到要不要移除失败的请求
	 * @return
	 */
	public boolean needRemoveRequest();
	/***
	 * 显示初始加载显示的View
	 * @return
	 */
	public  View showBeginLoaingView(ViewGroup parentView, ViewGroup.LayoutParams params);
	/***
	 * 显示网络错误的提示View
	 * @return
	 */
	public  View showNetErrorView(ViewGroup parentView, ViewGroup.LayoutParams params);
	/***
	 * 显示数据为空
	 * @param parentView
	 * @param msg 数据为空的提示语(为空表示使用默认的空提示)
	 * @return
	 */
	public  View showEmptyDataView(ViewGroup parentView, String msg, ViewGroup.LayoutParams params);
	/***
	 * 显示服务器返回数据错误的提示view
	 * @param parentView
	 * @return
	 */
	public  View showResponseErrorView(ViewGroup parentView, int code, String msg, ViewGroup.LayoutParams params);
	/***
	 * 请求成功了
	 * @param parentView
	 * @return
	 */
	public  View showSuccessView(ViewGroup parentView);

	/***
	 * 设置重新加载数据的点击回调
	 * @param onBgViewClickListener
	 */
	public void setReloadDataWhenClick(OnClickListener onBgViewClickListener) ;
	/***
	 *  清楚view
	 */
	public void clearViews();
	/***
	 *  获得自身UI的type
	 * @return
	 */
	public LoadingUiType uiType();
	
	public void setCancleClickListener(OnClickListener cancleClickListener) ;
}
