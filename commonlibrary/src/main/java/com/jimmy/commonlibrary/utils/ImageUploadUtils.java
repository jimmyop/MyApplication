package com.agile.merchant.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;

import com.mobile.qiniu.QiNieImageUpload;
import com.mobile.qiniu.QiNieImageUpload.OnImageUploadRefreshListener;
import com.qiniu.android.http.ResponseInfo;

/**
 * 上传图片工具类
 * 
 * @author xuanweiqing
 * 
 */
public class ImageUploadUtils implements OnImageUploadRefreshListener {

	/**
	 * 图片上传结果回调
	 * 
	 * @author xuanweiqing
	 * 
	 */
	public interface OnImageUploadListener {
		/**
		 * 图片上传完成回调
		 * 
		 * @param imageNames
		 */
		void onImageUploadDone(List<String> imageNames);

		/**
		 * 图片上传失败回调
		 */
		void onImageUploadFailure(ResponseInfo info);
	}

	/**
	 * 图片上传结果监听
	 */
	private OnImageUploadListener mOnImageUploadListener = null;

	/**
	 * 7牛返回图片名字
	 */
	private List<String> mImageNames = new ArrayList<String>();
	/**
	 * 7牛已经上传的图片张数
	 */
	private int mImageCount = 0;

	private String mToken;
	private boolean isError = false;
	private List<String> mImagePaths = null;
	private int mUploadImageCount;
	
	private String[] mImageUrls;

	public void upLoadImage(@NonNull final List<String> imagePaths,
			@NonNull String token, OnImageUploadListener onImageUploadListener) {

		mUploadImageCount = imagePaths.size();
		mImagePaths = imagePaths;
		mToken = token;
		
		setOnImageUploadListener(onImageUploadListener);
		
		QiNieImageUpload.getInstance().setConfiguration();
		QiNieImageUpload.getInstance().setOnImageUploadRefreshListener(this);
		mImageUrls = new String[imagePaths.size()];
//		for(int i=0; i < imagePaths.size(); i++){
//			byte[] imageByte = ImageUtils.getimageByte(imagePaths.get(i));
//			LogUtils.d("upload imagebyte size "+imageByte.length);
//			QiNieImageUpload.getInstance().uploadImage(imageByte, null, token);
//		}
		upload();
	
	}
	
	private void upload(){
		if(mImagePaths.size()>0){
			byte[] imageByte = ImageUtils.getimageByte(mImagePaths.remove(0));
			LogUtils.d("upload imagebyte size "+imageByte.length);
			QiNieImageUpload.getInstance().uploadImage(imageByte, null, mToken);
		}
	}

	public OnImageUploadListener getOnImageUploadListener() {
		return mOnImageUploadListener;
	}

	public void setOnImageUploadListener(
			OnImageUploadListener onImageUploadListener) {
		this.mOnImageUploadListener = onImageUploadListener;
	}

	@Override
	public void onImageUploadRefresh(String key, ResponseInfo info,
			JSONObject res) {
		
			if(null != res && info.isOK()) {
				mImageCount++;
				String imageName;
				try {
					LogUtils.d(""+res.toString());
					imageName = res.getString("hash");
					mImageNames.add(imageName);
					LogUtils.d("upload one pic: "+imageName);
				} catch (JSONException e) {
					if(null != mOnImageUploadListener)
					{
						mOnImageUploadListener.onImageUploadFailure(info);
					}
					e.printStackTrace();
					return;
					
				}
			
				if(mImageCount >= mUploadImageCount)
				{
					if(null != mOnImageUploadListener)
					{
						mOnImageUploadListener.onImageUploadDone(mImageNames);
					}
				}else{
					upload();
				}
			}
			else {
			
				if(null != mOnImageUploadListener)
				{
					mOnImageUploadListener.onImageUploadFailure(info);
				}
			}
		}
	
}
