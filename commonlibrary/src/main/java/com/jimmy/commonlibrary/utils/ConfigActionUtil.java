package com.agile.merchant.utils;

import android.content.Context;
import android.text.TextUtils;

import com.agile.merchant.common.imageloader.YJLUrlFilter;
import com.agile.merchant.threadpool.ThreadPoolFactory;
import com.agile.merchant.threadpool.interfaces.IThreadPoolManager;
import com.agile.merchant.threadpool.manager.ThreadTaskObject;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;

/***
 * 处理可配置化点击动作的工具类
 *
 * @author fuxinrong
 */
public class ConfigActionUtil {

    /***
     * 下载并保存应用启动页
     */
    public static void downloadAndSaveStartPagePic(Context context, String imgUrl) {
        File targetDir = FileUtils.picSaveDir(context);
        if (targetDir == null)
            return;
        if (!TextUtils.isEmpty(imgUrl)) {
            String fileName = YJLUrlFilter.createYjlUrl(imgUrl);
            fileName = new HashCodeFileNameGenerator().generate(fileName);
            File picFile = new File(targetDir, fileName);
            if (!FileUtils.isFileExist(picFile) && picFile.length() <= 0) {
                try {
                    picFile.createNewFile();
                    FileUtils.deleteFileExceptSpcialFile(targetDir.getAbsolutePath(), picFile.getAbsolutePath());
                    FileOutputStream outputStream = new FileOutputStream(picFile);

                    // 获取到线程池管理者
                    IThreadPoolManager tpm = ThreadPoolFactory.getThreadPoolManager();
                    // 往线程池添加任务
                    tpm.addTask(new DownloadPicTask(context, picFile, imgUrl, outputStream));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 下载图片任务
     */
    private static class DownloadPicTask extends ThreadTaskObject {
        private SoftReference<Context> srf;

        private File mPicFile;
        private String mImgUrl;
        private OutputStream mOutputStream;

        public DownloadPicTask(Context context, File picFile, String imgUrl, OutputStream outputStream) {
            this.mPicFile = picFile;
            this.mImgUrl = imgUrl;
            this.mOutputStream = outputStream;

            srf = new SoftReference<>(context);
        }

        @Override
        public void run() {
            Context context = srf.get();
            if (context == null)
                return;
            boolean download = ImageUtils.downSavePic(context, mImgUrl, mOutputStream);
            if (!download) {
                // 下载失败,删除残留
                mPicFile.delete();
            }
        }
    }
}
