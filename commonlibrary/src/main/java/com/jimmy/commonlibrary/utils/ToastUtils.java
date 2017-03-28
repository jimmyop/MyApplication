package com.jimmy.commonlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtils {
    public static void toast(Context context, int resId) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String text) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, int resId) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(Context context, String text) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
