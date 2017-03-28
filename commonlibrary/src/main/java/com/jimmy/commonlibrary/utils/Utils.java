package com.jimmy.commonlibrary.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;

/**
 * Class containing some static utility methods.
 */
public class Utils {

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                // vmPolicyBuilder
                // .setClassInstanceLimit(ImageGridActivity.class, 1)
                // .setClassInstanceLimit(ImageDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * 2.2
     *
     * @return
     */
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    /**
     * 2.3
     *
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    /**
     * 3.0
     *
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    /**
     * 3.1
     *
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 3.2
     *
     * @return
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * 4.0
     *
     * @return
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * 4.0.3
     *
     * @return
     */
    public static boolean hasIceCreamSandwichMr1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * 4.1
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 4.2
     *
     * @return
     */
    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * 4.3
     *
     * @return
     */
    public static boolean hasJellyBeanMr2() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * 4.4
     *
     * @return
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        File file = getExternalCacheDir(context);
        final String cachePath;
        if (file != null) {
            cachePath =
                    Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                            || !isExternalStorageRemovable() ? file.getPath() : context
                            .getCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false otherwise.
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

}
