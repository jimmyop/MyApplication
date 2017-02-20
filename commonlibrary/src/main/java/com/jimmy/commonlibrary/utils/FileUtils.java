package com.agile.merchant.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.agile.merchant.common.Constants;


public class FileUtils {

	// 写在/mnt/sdcard/目录下面的文件
	public static void writeFileSdcard(String pathName, String message) {
		try {
			File path = new File(pathName);
			File file = new File(pathName + File.separator + Constants.BACKUP_FILE_NAME);
			if (!path.exists()) {
				path.mkdir();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(file);
			byte[] buf = message.getBytes();
			stream.write(buf);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeLogFileSdcard(String fileName, String conent) {
		if (!SdcardUtils.isSdcardAvaliable()) {
			return;
		}
		String path = Environment.getExternalStorageDirectory() + File.separator + fileName;
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static public boolean writeSerializeFile(String filename, Context context, Object obj) {
		FileOutputStream ostream = null;
		try {
			ostream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(obj);
			p.flush();
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	static public Object readSerializeFile(String filename, Context context) {
		FileInputStream istream = null;
		try {
			istream = context.openFileInput(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		ObjectInputStream q;
		try {
			q = new ObjectInputStream(istream);
			return q.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除目录
	 * 
	 * @param dir
	 */
	public static void deleteFolder(File dir) {
		File filelist[] = dir.listFiles();
		int listlen = filelist.length;
		for (int i = 0; i < listlen; i++) {
			if (filelist[i].isDirectory()) {
				deleteFolder(filelist[i]);
			} else if (filelist[i].isFile()) {
				filelist[i].delete();
			}
		}
	}

	/**
	 * 从文件路径里获取去除掉后缀名的文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameWithoutExtension(String filePath) {
		return getFilePathWithoutExtension(getFileName(filePath));
	}

	/**
	 * 获取去除掉文件名后缀的文件路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFilePathWithoutExtension(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return filePath;

		int extenPosi = filePath.lastIndexOf(".");
		if (extenPosi == -1)
			return filePath;
		return filePath.substring(0, extenPosi);
	}

	/**
	 * 从文件路径里获取文件名 ，含文件后缀名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return filePath;

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	}

	/**
	 * 获取该文件的所属上级文件夹
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFolderName(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return filePath;

		File file = new File(filePath);
		if (file.exists() && file.isDirectory())
			return filePath;

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * 获取该文件路径的文件后缀名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileExtension(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return filePath;

		int extenPosi = filePath.lastIndexOf(".");
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1)
			return "";
		return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	}

	/**
	 * 创建文件夹
	 */
	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);

		if (TextUtils.isEmpty(folderName))
			return false;

		File folder = new File(folderName);
		if (folder.exists() && folder.isDirectory())
			return true;

		return folder.mkdirs();
	}

	/**
	 * 判断制定文件路径的文件或文件夹是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}

		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 判断制定文件路径的文件或文件夹是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFileExist(File file) {
		if (file != null)
			return isFileExist(file.getAbsolutePath());
		return false;
	}

	/***
	 *  APP 启动页的目录
	 *  目录：1context.getExternalFilesDir 或者2 context.getFilesDir()
	 * @param context
	 * @return
	 */
	public static File picSaveDir(Context context){
		File targetDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			targetDir = context.getExternalFilesDir(null);
			
		} else {
			targetDir = context.getFilesDir();
		}
		if (targetDir != null&&!targetDir.exists()) {
			targetDir.mkdirs();
		}
		return targetDir;
	}
	
	/**
	 * 删除文件或文件夹
	 *
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		File file = new File(path);
		return deleteFile(file);
	}

	/**
	 * 删除文件或文件夹
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if (file == null)
			return false;
		if (!file.exists())
			return true;

		if (file.isFile()) {
			//LogUtils.e("lxy", "删除文件--->" + file.getAbsolutePath());
			return file.delete();
		}

		for (File f : file.listFiles()) {
			if (!deleteFile(f))
				return false;
		}
		return file.delete();
	}
	
	/**
	 * 删除某个目录下除某个特殊文件外的其他任何文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFileExceptSpcialFile(String dir,String spcialAbsolutePath) {
		if (TextUtils.isEmpty(dir))
			return false;
		File file = new File(dir);
		if (!file.exists())
			return true;

		if (!file.isDirectory())
			return false;

		for (File f : file.listFiles()) {
			if (f.getAbsolutePath().equals(spcialAbsolutePath)) {
				continue;
			}
			f.delete();
		}
		return true;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath 准备复制的文件源
	 * @param newPath 拷贝到新绝对路径带文件名
	 * @return
	 */
	private static boolean copyFile(String oldPath, String newPath) {
		InputStream is = null;
		FileOutputStream fs = null;
		try {
			is = new FileInputStream(oldPath);
			fs = new FileOutputStream(newPath);

			byte[] buffer = new byte[1024 * 8];
			int byteread = 0;
			while ((byteread = is.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			new File(newPath).delete();
		} finally {
			try {
				if (fs != null)
					fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath 准备拷贝的目录或文件的绝对路径
	 * @param newPath 指定新目录或文件的绝对路径
	 * @return
	 */
	public static boolean copyFileOrDir(String oldPath, String newPath) {
		if (!isFileExist(oldPath))
			return false;

		if (!makeDirs(newPath))
			return false;

		File oldFile = new File(oldPath);
		if (oldFile.isFile())
			return copyFile(oldPath, newPath);

		File[] files = oldFile.listFiles();
		for (File file : files) {
			if (!copyFileOrDir(oldPath + File.separator + file.getName(), newPath + File.separator
					+ file.getName()))
				return false;
		}
		return true;
	}

	/**
	 * 移动文件或目录 重命名文件或目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean renameFileOrDir(String oldPath, String newPath) {
		try {
			File oldFile = new File(oldPath);
			if (!oldFile.exists())
				return false;
			File newFile = new File(newPath);
			if (newFile.exists())
				newFile.delete();
			return makeDirs(newPath) && oldFile.renameTo(newFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取真实文件大小
	 * @param file
	 * @return
	 */
	public static long getTotalSizeOfFilesInDir(File file) {
		if (file.isFile()) {
			//LogUtils.i("lxy", "查询文件大小--->" + file.getAbsolutePath());
			return file.length();
		}
		final File[] children = file.listFiles();
		long total = 0;
		if (children != null)
			for (final File child : children)
				total += getTotalSizeOfFilesInDir(child);
		return total;
	}

	/**
	 * 转换文件大小
	 * @param fileSize
	 * @return
	 */
	public static String formatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize <= 0) {
			fileSizeString = "0 MB";
		} else if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "KB";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) +"GB";
		}
		return fileSizeString;
	}
}
