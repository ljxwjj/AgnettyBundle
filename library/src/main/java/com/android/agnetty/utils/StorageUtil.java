/*
 * ========================================================
 * Copyright(c) 2012 杭州龙骞科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.hzdracom.com/
 * 
 * ========================================================
 */

package com.android.agnetty.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import com.android.agnetty.constant.FileCst;
import java.io.File;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 
 * 
 * 	public method
 *  <li>getRootDir(Context)				获取可用存储空间的根目录 </li>
 *  <li>getFileType(String) 			获取文件类型</li>
 *  <li>getFileDir(Context, int) 		根据文件类型获取文件目录名</li>
 *  <li>getFileDir(Context, String) 	根据文件名获取文件目录名</li>
 *  <li>createFileDir(Context, int) 	根据文件名创建文件目录</li>
 *  <li>createFileDir(Context, String) 	根据文件名创建文件目录</li>
 *  <li>getFilePath(Context, String) 	获取文件的绝对路径</li>
 *  <li>createFilePath(Context, String) 创建文件</li>
 *  <li>getLeftSpace(String) 			获取指定目录剩余存储空间</li>
 *  <li>getTotalSpace(String) 			获取指定目录所有存储空间</li>
 *  
 *  private method
 *  
 */
public class StorageUtil {
	
	/**
	 * 获取可用存储空间的根目录，一般先获取外置存储空间，如果没有，再去获取内存存储空间，
	 * 如果都没有，则获取本地应用的可用目录
	 * @param context
	 * @return
	 */
	public static String getRootDir(Context context) {
		//先找外置存储路径
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		//再找本地应用内存路径
		if(context != null) {
			return context.getFilesDir().getAbsolutePath();
		}
		if (context != null) {
			return context.getCacheDir().getAbsolutePath();
		}
		
		return null;
	}

	/**
	 * 获取文件类型，文件类型请查看FileCst定义
	 * @param fileName 文件名
	 * @return
	 */
	public static int getFileType(String fileName) {
		if(StringUtil.isBlank(fileName)) return FileCst.TYPE_ERROR;
		fileName = fileName.toLowerCase();
		
		if (fileName.endsWith(FileCst.SUFFIX_PNG) 	
				|| fileName.endsWith(FileCst.SUFFIX_JPG) 
				|| fileName.endsWith(FileCst.SUFFIX_JPE) 
				|| fileName.endsWith(FileCst.SUFFIX_JPEG) 
				|| fileName.endsWith(FileCst.SUFFIX_BMP) 
				|| fileName.endsWith(FileCst.SUFFIX_GIF) ) { //图片类型
			return FileCst.TYPE_IMAGE;
		} else if (fileName.endsWith(FileCst.SUFFIX_MP4) 
				|| fileName.endsWith(FileCst.SUFFIX_3GPP) 
				|| fileName.endsWith(FileCst.SUFFIX_M4A)) { //音频类型
			return FileCst.TYPE_AUDIO;
		} else if (fileName.endsWith(FileCst.SUFFIX_VID)) { //视频类型
			return FileCst.TYPE_VIDEO;
		} else if (fileName.endsWith(FileCst.SUFFIX_APK) 
				|| fileName.endsWith(FileCst.SUFFIX_DEX)) { //安装包类型
			return FileCst.TYPE_APK;
		} else if (fileName.endsWith(FileCst.SUFFIX_TXT)) { //文本类型
			return FileCst.TYPE_TXT;
		} else if (fileName.endsWith(FileCst.SUFFIX_LOG)){ //日志类型
			return FileCst.TYPE_LOG;
		} else if (fileName.endsWith(FileCst.SUFFIX_RAR)
				|| fileName.endsWith(FileCst.SUFFIX_ZIP)) { //压缩包类型
			return FileCst.TYPE_ZIP;
		} else if(fileName.endsWith(FileCst.SUFFIX_DB)) { //数据存储类型
			return FileCst.TYPE_DATA;
		} else if (fileName.endsWith(FileCst.SUFFIX_PDF)) {
			return FileCst.TYPE_PDF;
		} else { //未知
			return FileCst.TYPE_UNKNOWN;
		}
	}
	
	
	/**
	 * 获取文件目录名, 相对目录请查看FileCst定义
	 * @param fileType
	 * @return
	 */
	public static String getFileDir(Context context, int fileType) {
		String rootDir = getRootDir(context);
		if(StringUtil.isEmpty(rootDir)) return null;
		
		String directory = FileCst.DIR_UNKNOWN;
		switch (fileType) {
			case FileCst.TYPE_IMAGE:
				directory = FileCst.DIR_IMAGE;
				break;
			case FileCst.TYPE_AUDIO:
				directory = FileCst.DIR_AUDIO;
				break;
			case FileCst.TYPE_VIDEO:
				directory = FileCst.DIR_VIDEO;
				break;
			case FileCst.TYPE_APK:
				directory = FileCst.DIR_APK;
				break;
			case FileCst.TYPE_TXT:
				directory = FileCst.DIR_TXT;
				break;
			case FileCst.TYPE_LOG:
				directory = FileCst.DIR_LOG;
				break;
			case FileCst.TYPE_ZIP:
				directory = FileCst.DIR_ZIP;
				break;
			case FileCst.TYPE_DATA:
				directory = FileCst.DIR_DATA;
				break;
			case FileCst.TYPE_UNKNOWN:
				directory = FileCst.DIR_UNKNOWN;
				break;
			case FileCst.TYPE_PDF:
				directory = FileCst.DIR_PDF;
				break;
			case FileCst.TYPE_ERROR:
			default:
				directory = null;
				break;
		}
		
		return directory == null 
				? null : rootDir + File.separator +directory;
	}

	public static String getCacheDir(Context context, int fileType) {
		String rootDir = context.getCacheDir().getAbsolutePath();

		String directory = FileCst.DIR_UNKNOWN;
		switch (fileType) {
			case FileCst.TYPE_IMAGE:
				directory = FileCst.DIR_IMAGE;
				break;
			case FileCst.TYPE_AUDIO:
				directory = FileCst.DIR_AUDIO;
				break;
			case FileCst.TYPE_VIDEO:
				directory = FileCst.DIR_VIDEO;
				break;
			case FileCst.TYPE_APK:
				directory = FileCst.DIR_APK;
				break;
			case FileCst.TYPE_TXT:
				directory = FileCst.DIR_TXT;
				break;
			case FileCst.TYPE_LOG:
				directory = FileCst.DIR_LOG;
				break;
			case FileCst.TYPE_ZIP:
				directory = FileCst.DIR_ZIP;
				break;
			case FileCst.TYPE_DATA:
				directory = FileCst.DIR_DATA;
				break;
			case FileCst.TYPE_UNKNOWN:
				directory = FileCst.DIR_UNKNOWN;
				break;
			case FileCst.TYPE_PDF:
				directory = FileCst.DIR_PDF;
				break;
			case FileCst.TYPE_ERROR:
			default:
				directory = null;
				break;
		}

		return directory == null
				? null : rootDir + File.separator +directory;
	}
	
	/**
	 * 根据文件名获取文件目录名, 相对目录请查看FileCst定义
	 * @param context
	 * @param fileName
	 * @see #getFileDir(Context, int)
	 * @return
	 */
	public static String getFileDir(Context context, String fileName) {
		int fileType = getFileType(fileName);
		return getFileDir(context, fileType);
	}

	public static String getCacheDir(Context context, String fileName) {
		int fileType = getFileType(fileName);
		return getCacheDir(context, fileType);
	}
	
	/**
	 * 根据文件类型创建文件的目录
	 * @param context
	 * @param fileType
	 * @return
	 */
	public static String createFileDir(Context context, int fileType) {
		String fileDir = getFileDir(context, fileType);
		return FileUtil.makeFolders(fileDir) ? fileDir : null;
	}
	
	
	/**
	 * 根据文件名创建文件的目录
	 * @param context
	 * @param fileName
	 * @see #createFileDir(Context, int)
	 * @return
	 */
	public static String createFileDir(Context context, String fileName) {
		int fileType = getFileType(fileName);
		return createFileDir(context, fileType);
	}
	

	/**
	 * 获取文件的绝对路径
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(Context context, String fileName) {
		String fileDir = getFileDir(context, fileName);
		return StringUtil.isEmpty(fileDir) 
				? null : fileDir + File.separator + fileName;
	}

	public static String getCacheFilePath(Context context, String fileName) {
		String fileDir = getCacheDir(context, fileName);
		return StringUtil.isEmpty(fileDir)
				? null : fileDir + File.separator + fileName;
	}
	
	
	/**
	 * 创建文件
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String createFilePath(Context context, String fileName) {
		String fileDir = createFileDir(context, fileName);
		
		if(StringUtil.isEmpty(fileDir)) {
			return null;
		} else {
			String filePath = fileDir + File.separator + fileName;
			return FileUtil.createFile(filePath) ? filePath : null;
		}
		
	}
	
	/**
	 * 获取指定目录剩余存储空间，返回单位为字节
	 * @param directory
	 * @return
	 */
	public static long getLeftSpace(String directory) {
		if(StringUtil.isBlank(directory)) return 0;
		
		long space = 0;
		try {
			StatFs sf = new StatFs(directory);
			space = (long)sf.getBlockSize() * sf.getAvailableBlocks();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		
		return space;
	}
	
	/**
	 * 获取指定目录所有存储空间, 返回单位为字节
	 * @param directory
	 * @return
	 */
	public static long getTotalSpace(String directory) {
		if(StringUtil.isBlank(directory)) return 0;
		
		long space = 0;
		try {
			StatFs sf = new StatFs(directory);
			space = (long)sf.getBlockSize() * sf.getBlockCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		
		return space;
	}
}
