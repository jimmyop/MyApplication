package com.jimmy.commonlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/***
 *  图片image的一些工具方法
 * @author fuxinrong
 *
 */
public class ImageUtils {
	 /** 
     * Bitmap转换到Byte[] 
     * @param bm 
     * @return  、
     * 
     * 注：压缩过的bitmap传入这个方法后又没有压缩效果了。
     */  
    public static byte[] bitmap2Bytes(Bitmap bm){     
        ByteArrayOutputStream bas = new ByteArrayOutputStream();       
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bas); // 100 不进行压缩了      
        LogUtils.d("bitmap2Bytes bas.toByteArray size "+bas.toByteArray().length);
        return bas.toByteArray();     
      }
    /***
     *  从文件地址读出图片，然后进行尺寸压缩，最后进行质量压缩。
     *  最后的大小不超过400kb
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inPreferredConfig = Config.RGB_565;// 图片配置系数用16 位RGB_565, 默认是24位的ARGB_888
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 1280f;//这里设置高度为1080f  
        float ww = 720f;//这里设置宽度为720f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        LogUtils.d(String.format("original: [%d,%d], now: [%d, %d]", w, h, newOpts.outWidth, newOpts.outHeight));

        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }  
    /***
     *  对图片进行质量压缩
     * @param image
     * @return Bitmap
     */
    public static Bitmap compressImage(Bitmap image) {  
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            options -= 10;//每次都减少10  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        LogUtils.d("compressImage baos.toByteArray() size "+ baos.toByteArray().length);
        return bitmap;  
    }  
    
    /***
     *  从文件地址读出图片，然后进行尺寸压缩，最后进行质量压缩。
     *  最后的大小不超过200kb
     * @param srcPath
     * @return byte[]图片字节数组
     */
    public static byte[] getimageByte(String srcPath) {  
    	Bitmap bitmap = getimage(srcPath); 
        return compressImage2ByteArray(bitmap);//压缩好比例大小后再进行质量压缩  
    }  
    
    /***
     *  对图片进行质量压缩
     * @param image
     * @return byte[]图片字节数组
     */
    public static byte[] compressImage2ByteArray(Bitmap image) {  
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>400) {  //循环判断如果压缩后图片是否大于400kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            options -= 10;//每次都减少10  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
        }  
        LogUtils.d("compressImage baos.toByteArray() size "+ baos.toByteArray().length);
        return baos.toByteArray();  
    }  
    
    /***
     *  通过base64Pic取得图片流
     * @param base64Pic
     * @return
     */
    public static Bitmap decodeBase64Pic(String base64Pic){

    	InputStream input = null;
    	Bitmap bitmap = null;
    	try{
    	byte[] imgByte = Base64.decode(base64Pic, Base64.DEFAULT);
    	 input = new ByteArrayInputStream(imgByte);
    	 bitmap= BitmapFactory.decodeStream(input);
    	 if(input != null){
    		 input.close();
    	 }
    	}catch(Exception exception){
    		exception.printStackTrace();
    	}
    	return bitmap;
    }

    public static boolean downSavePic(Context context,String urlString,OutputStream outputStream){
    	if (!NetworkUtils.isNetworkConnected(context) || outputStream == null) {
			return false;
		}

		HttpURLConnection urlConnection = null;
		InputStream in = null;
		URL url;
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(30000);  
			urlConnection.setReadTimeout(30000);  
			if (urlConnection != null
					&& urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				in = urlConnection.getInputStream();
				int buffSize = 8192;
				byte[] data = new byte[buffSize];
				int count = -1;
				while ((count = in.read(data, 0, buffSize)) != -1)
					outputStream.write(data, 0, count);
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
			}
		}
    	return true;
    }
    
    /**
     * 获取文件对应的宽高
     * @param path
     * @return 返回数组长度为2，第0个为width,第1个为height
     */
    public static int[] getImageBounds(String path){
    	int[] bounds = new int[2];
    	try{
    		BitmapFactory.Options opts = new BitmapFactory.Options();
    		opts.inJustDecodeBounds = true;
    		BitmapFactory.decodeFile(path, opts);
    		bounds[0] = opts.outWidth;
    		bounds[1] = opts.outHeight;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return bounds;
    }
}
