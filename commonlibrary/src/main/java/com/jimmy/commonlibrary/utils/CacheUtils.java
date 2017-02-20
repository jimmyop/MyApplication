package com.agile.merchant.utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.agile.merchant.common.imageloader.YjlImageLoader;

import java.io.File;

/**
 * 清理缓存工具类
 * Created by lixinyu on 2016/12/6.
 */

public class CacheUtils {

	public static long getSystemCacheSize(Context context) {
		File file = context.getCacheDir();
		if (file != null && file.exists()) {
			return FileUtils.getTotalSizeOfFilesInDir(file);
		}
		return 0;
	}
	
	public static boolean clearSystemCache(Context context) {
		boolean bl = false;
		
		//File file = CacheManager.getCacheFileBaseDir();
		File file = context.getCacheDir();
		if (file != null && file.exists()) bl = FileUtils.deleteFile(file);
		bl &= context.deleteDatabase("webview.db");
		bl &= context.deleteDatabase("webviewCache.db");
		
		return bl;
	}

	public static boolean clearCookies(Context context) {
		// Edge case: an illegal state exception is thrown if an instance of
		// CookieSyncManager has not be created.  CookieSyncManager is normally
		// created by a WebKit view, but this might happen if you start the
		// app, restore saved state, and click logout before running a UI
		// dialog in a WebView -- in which case the app crashes
		try {
			@SuppressWarnings("unused")
			CookieSyncManager cookieSyncMngr =
					CookieSyncManager.createInstance(context);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean clearImageCache() {
		boolean bl = false;
		File directory = YjlImageLoader.getInstance().getDiskCache().getDirectory();
		if (directory != null && directory.exists()) bl = FileUtils.deleteFile(directory);
		try {
			YjlImageLoader.getInstance().getMemoryCache().clear();
			bl &= true;
		} catch (Exception e) {
			e.printStackTrace();
			bl = false;
		}

		return bl;
	}
	
	public static long getImageCacheSize() {
		File directory = YjlImageLoader.getInstance().getDiskCache().getDirectory();
		if (directory != null && directory.exists()) return FileUtils.getTotalSizeOfFilesInDir(directory);
		
		return 0;
	}
	
	public static long getTotalCacheSize(Context context) {
		return getImageCacheSize() + getSystemCacheSize(context);
	}
	
	public static boolean clearAllCache(Context context) {
		boolean bl = true;
		
		bl &= clearImageCache();
		bl &= clearSystemCache(context);
		bl &= clearCookies(context);
		
		return bl;
	}
}
