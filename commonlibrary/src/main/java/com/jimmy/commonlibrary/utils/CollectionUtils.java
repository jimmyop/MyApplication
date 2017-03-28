package com.jimmy.commonlibrary.utils;


import java.util.Collection;

/**
 * 集合工具类
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return 返回为空状态
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 获取集合的长度
     *
     * @param collection 集合
     * @return 返回集合的长度
     */
    public static int getSize(Collection<?> collection) {
        if (isNullOrEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    /**
     * 判断集合长度是否与目标值相等
     *
     * @param collection 集合
     * @param targetSize 集合大小
     * @return 返回长度比较状态
     */
    public static boolean isEqual(Collection<?> collection, int targetSize) {
        return getSize(collection) == targetSize;
    }

    /**
     * 判断集合长度是否比目标值小
     *
     * @param collection 集合
     * @param targetSize 集合大小
     * @return 返回长度小比较状态
     */
    public static boolean isSmallThan(Collection<?> collection, int targetSize) {
        return getSize(collection) < targetSize;
    }

    /**
     * 判断集合长度是否比目标值小或者相等
     *
     * @param collection 集合
     * @param targetSize 集合大小
     * @return 返回长度小或者相等比较状态
     */
    public static boolean isSmallThanOrEqual(Collection<?> collection, int targetSize) {
        return getSize(collection) <= targetSize;
    }

    /**
     * 判断集合长度是否比目标值大
     *
     * @param collection 集合
     * @param targetSize 集合大小
     * @return 返回长度大比较状态
     */
    public static boolean isBigThan(Collection<?> collection, int targetSize) {
        return getSize(collection) > targetSize;
    }

    /**
     * 判断集合长度是否比目标值大或者相等
     *
     * @param collection 集合
     * @param targetSize 集合大小
     * @return 返回长度大或者相等比较状态
     */
    public static boolean isBigThanOrEqual(Collection<?> collection, int targetSize) {
        return getSize(collection) >= targetSize;
    }
}
