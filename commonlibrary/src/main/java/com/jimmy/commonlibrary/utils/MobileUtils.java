package com.jimmy.commonlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机相关工具类
 * Created by zengxiangbin on 2016/6/21.
 */
public class MobileUtils {
    private MobileUtils(){}

    /**
     * 手机号中间加密
     * @param mobile
     * @return
     */
    public static String getScretMobile(String mobile){
        if (TextUtils.isEmpty(mobile)){
            return "";
        }
        if (mobile.length() <= 7){
            return mobile;
        }

        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 是否是一个合法的手机号
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^(1[3-9])\\d{9}|((5[1-69]|9[0-8])\\d{6}|6\\d{7})$");// 支持大陆、香港手机号
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 是否是一个合法的电话号码
     * @param telephone
     * @return
     */
    public static boolean isTelephone(String telephone) {
        Pattern p = Pattern.compile("(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,8})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,8})+(\\-[0-9]{1,4})?)");// 区号+座机号码+分机号码
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    /**
     * 打电话
     * @param context
     * @param phone
     */
    public static void call(Context context, String phone){
        if(context == null || TextUtils.isEmpty(phone)){
            return ;
        }

        call(context, Uri.parse("tel:"+phone));
    }

    /**
     * 打电话
     * @param context
     * @param uri
     */
    public static void call(Context context, Uri uri){
        if(context == null || uri == null){
            return ;
        }
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if(CollectionUtils.isNullOrEmpty(resolveInfo)){
            return ;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断url是否是一个电话类型的url
     * @param url
     * @return
     */
    public static boolean isPhoneUrl(String url){
        if(TextUtils.isEmpty(url)){
            return false;
        }
        return url.startsWith("tel:");
    }
}
