package com.jimmy.commonlibrary.widget.loadingmanager;

/**
 * Author: chenjiaming1
 * Created on: 2017/5/31.
 * Description:
 */

public enum LoadingUiType {
    /***
     * 背景的加载形式
     */
    BACKGROUND,
    /***
     * 背景Toast方式
     */
    BACKGROUNDTOAST,
    /***
     * 对话框的加载形式
     */
    DIALOGTOAST,
    /***
     * 上下拉
     */
    PULLUPDOWN,
    /**
     * 无提示
     */
    SILENCE,
    /***
     * 对话框的加载形式有空UI提示
     */
    DIALOG_EMPTYUI,
    /***
     * 所有的提示都是对话框的形式
     */
    DIALOG_ALL,
    /**
     * 只显示Toast
     */
    TOAST

}
