package com.jimmy.commonlibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 屏幕显示工具类
 */
public class DisplayUtil {

    public static int mScreenWidth;
    public static int mScreenHeight;

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 返回屏幕密度
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dip     dp单位值
     * @return 返回px单位值
     */
    public static int dip2px(Context context, double dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dip dp单位值
     * @return 返回px单位值
     */
    public static int dip2px(double dip) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue px单位值
     * @return 返回dip单位值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue px单位值
     * @return 返回dip单位值
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spVal   需转换的sp值
     * @return 转后的px值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param spVal 需转换的sp值
     * @return 转后的px值
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽
     *
     * @return 返回屏幕宽
     */
    public static int getScreenWidth() {
        if (mScreenWidth != 0) {
            return mScreenWidth;
        }
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return mScreenWidth;
    }

    /**
     * 获取屏幕高
     *
     * @return 返回屏幕高
     */
    public static int getScreenHeight() {
        if (mScreenHeight != 0) {
            return mScreenHeight;
        }
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        return mScreenHeight;
    }

    /**
     * 获得状态栏的高度
     *
     * @return 返回状态栏的高度
     */
    public static int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = Resources.getSystem().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取手机的分辨率 格式为1280x720
     *
     * @return 返回手机的分辨率
     */
    public static String getPhoneResolution() {
        return getScreenWidth() + "x" + getScreenHeight();
    }
}
