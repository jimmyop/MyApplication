package com.jimmy.commonlibrary.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SdcardUtils {

    public static boolean isSdcardAvaliable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获得系统中剩余的空间大小
     *
     * @return
     */
    public static long getPhoneStorageLeftMemory() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long storageLeftMemory = stat.getAvailableBlocks() * stat.getBlockSize(); // return value is in bytes

        return storageLeftMemory;
    }

    /**
     * 获得系统中已使用的空间大小
     *
     * @return
     */
    public static long getPhoneStorageUsedMemory() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long storageUsedMemory = (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize(); // return value is in bytes
        return storageUsedMemory;
    }

    /**
     * 获得系统中所有的空间大小
     *
     * @return
     */
    public static long getPhoneStorageTotalMemory() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long storageTotalMemory = stat.getBlockCount() * stat.getBlockSize(); // return value is in bytes

        return storageTotalMemory;
    }

    /**
     * 获得sdcard中剩余的空间大小
     *
     * @return
     */
    public static long getSdCardLeftMemory() {

        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long leftMemory = stat.getAvailableBlocks() * stat.getBlockSize(); // return value is in bytes

        return leftMemory;
    }

    /**
     * 获得sdcard中已使用的空间大小
     *
     * @return
     */
    public static long getSdCardUsedMemory() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long usedMemory = (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize(); // return value is in bytes

        return usedMemory;
    }

    /**
     * 获得sdcard中总共的空间大小
     *
     * @return
     */
    public static long getSdCardTotalMemory() {

        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long totalMemory = stat.getBlockCount() * stat.getBlockSize(); // return value is in bytes

        return totalMemory;
    }
}
