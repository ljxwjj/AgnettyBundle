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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.constant.CharsetCst;
import com.android.agnetty.constant.FileCst;
import com.android.agnetty.constant.HttpCst;
import com.android.agnetty.constant.SizeCst;
import android.util.Log;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 日志工具类，支持日志的显示、日志文件的保存以及日志文件的上传
 */
public class LogUtil {
	//日志文件最大值
	private static final long LOG_MAX_SIZE = 100 * SizeCst.KB; //100K
	
	private static final String BOUNDARY = "---------------------------ae0Ij5GI3KM7gL6Ef1cH2GI3Ef1gL6";
	
	private static final ExecutorService mLogCachedThreadPool = ThreadPoolUtil.getCachedThreadPool();
	
	/**
	 * Verbose Log, 输出大于或等于Verbose日志级别的信息
	 * @param msg
	 */
	public static void v(String msg) {
		if(AgnettyCst.DEBUG) {
			Log.v(AgnettyCst.TAG, StringUtil.nullToString(msg));
			saveLog(msg);
		}
	}
	
	/**
	 * Debug Log, 输出大于或等于Debug日志级别的信息
	 * @param msg
	 */
	public static void d(String msg) {
		if(AgnettyCst.DEBUG) {
			Log.d(AgnettyCst.TAG, StringUtil.nullToString(msg));
			saveLog(msg);
		}
	}
	
	/**
	 * Info Log,输出大于或等于Info日志级别的信息
	 * @param msg
	 */
	public static void i(String msg) {
		if(AgnettyCst.DEBUG) {
			Log.i(AgnettyCst.TAG, StringUtil.nullToString(msg));
			saveLog(msg);
		}
	}
	
	/**
	 * Warn Log,输出大于或等于Warn日志级别的信息
	 * @param msg
	 */
	public static void w(String msg) {
		if(AgnettyCst.DEBUG) {
			Log.w(AgnettyCst.TAG, StringUtil.nullToString(msg));
			saveLog(msg);
		}
	}
	
	/**
	 * Error Log, 仅输出Error日志级别的信息.
	 * @param msg
	 */
	public static void e(String msg) {
		if(AgnettyCst.DEBUG) {
			Log.e(AgnettyCst.TAG, StringUtil.nullToString(msg));
			saveLog(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	private static void saveLog(final String msg) {
		/*mLogCachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
			
				String fileName = new SimpleDateFormat("yyyyMMdd_HH")
										.format(new Date())+ FileCst.SUFFIX_LOG;
				String filePath = StorageUtil.createFilePath(null, fileName);
				if(StringUtil.isNotBlank(filePath)) {
					File file = new File(filePath);
					writeLogFile(file, msg);
//					uploadLogFile(file, msg);
				}
			}
		});*/
	}
	
	/**
	 * 将日志写入文件
	 * @param file
	 * @param msg
	 */
	private static void writeLogFile(File file, String msg) {
		if(file == null|| !file.exists()) return;
		
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			//日志内容
			StringBuilder builder = new StringBuilder()
				.append(new SimpleDateFormat("yyyyMMdd-hh:mm:ss:SSS").format(new Date()))
				.append(":")
				.append(AgnettyCst.TAG)
				.append(":")
				.append(msg)
				.append("\n");
			bufferedWriter.append(builder.toString());
			bufferedWriter.flush();
		}catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(bufferedWriter != null) bufferedWriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 上传日志文件到服务器
	 * @param file
	 * @param msg
	 */
	private static void uploadLogFile(File file, String msg) {
		if(file == null 
				|| !file.exists() 
				|| file.length()< LOG_MAX_SIZE) {
			return;
		}
		
		try {
			String url = "http://upload9.163disk.com/?ac=upload&temp=0.8839586193207651";
			
			HashMap<String, String> contents = new HashMap<String, String>();
	        contents.put("Filename",  file.getName());
	        contents.put("up_folder", "0");
	        contents.put("up_server", "9");
	        contents.put("up_key", 	  "bbb127a4d091297f7de2b3839e2d0740");
	        contents.put("up_list",   "8");
	        contents.put("up_share",  "0");
	        contents.put("up_ip",     "202.101.166.113");
	        contents.put("up_user",   "395871");
	        contents.put("Upload",    "Submit Query");
	        	
	        FileInputStream in = new FileInputStream(file);
	        byte[] data = StreamUtil.toByteArray(in);
	        FormUploadFile uploadFile = new FormUploadFile("Filedata", file.getName(), data);
	        FormUploadFile[] uploadFiles = {uploadFile};
	        httpUploadFile(url, contents, uploadFiles);
	        in.close();
		}catch (Exception e) {
			
		}
	}
	
	/**
	 * 基于http的文件上传
	 * @param url
	 * @param contents
	 * @param files
	 */
	private static void httpUploadFile(String url, HashMap<String, String> contents, FormUploadFile[] files) {
		HttpURLConnection conn = null;
		DataOutputStream dataOutStream = null;
		BufferedInputStream bufferInStream = null;
		ByteArrayOutputStream arrayOutStream = null;
		
		try {
			URL httpUrl = new URL(url);
			
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setConnectTimeout(HttpCst.CONNECTION_TIMEOUT);
			conn.setReadTimeout(HttpCst.READ_TIMEOUT);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);// 不使用Cache
			conn.setRequestMethod(HttpCst.POST);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", CharsetCst.UTF_8);
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			
			
				
			StringBuilder sb = new StringBuilder();
			// 上传的表单参数部分，格式请参考文章
			for (Map.Entry<String, String> entry : contents.entrySet()) {// 构建表单字段内容
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				sb.append(entry.getValue());
				sb.append("\r\n");
			}
			
			dataOutStream = new DataOutputStream(conn.getOutputStream());
			byte[] data = sb.toString().getBytes();
			dataOutStream.write(data);// 发送表单字段数据
			
			// 上传的文件部分，格式请参考文章
			for (FormUploadFile file : files){
				StringBuilder split = new StringBuilder();
				split.append("--");
				split.append(BOUNDARY);
				split.append("\r\n");
				split.append("Content-Disposition: form-data;name=\"" + file.mField + "\";filename=\"" + file.mFileName + "\"\r\n");
				split.append("Content-Type: " + file.mContentType + "\r\n\r\n");
				dataOutStream.write(split.toString().getBytes());
				dataOutStream.write(file.mData, 0, file.mData.length);
				dataOutStream.write("\r\n".getBytes());
			}
			
			byte[] end = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
			dataOutStream.write(end);
			dataOutStream.flush();
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				byte[] buffer = new byte[8192];
				int length = -1;	 //每次读取数据的长度
				
				bufferInStream = new BufferedInputStream(conn.getInputStream());
				arrayOutStream = new ByteArrayOutputStream();
				
				while((length = bufferInStream.read(buffer)) != -1){
					arrayOutStream.write(buffer, 0, length);
				}
				
			
			}  else { //请求失败，下载失败
				throw new IllegalStateException("HTTP RESPONSE ERROR!!!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//-------------释放资源-----------------
			if(conn != null) conn.disconnect();
			
			try {
				if(dataOutStream != null) dataOutStream.close();
				if(bufferInStream != null) bufferInStream.close();
				if(arrayOutStream != null) arrayOutStream.close();
			} catch (Exception e) {
				
			}
		}
 	}
	
	
	/**
	 * @author : Zhenshui.Xia
	 * @date   : 2013-9-19
	 * @desc   :
	 */
	private static class FormUploadFile {
		//上传文件的数据 
		public byte[] mData;    
		//文件名称 
		public String mFileName;  
		//请求参数名称
		public String mField;  
		//内容类型
		public String mContentType = "application/octet-stream"; 

		public FormUploadFile() {
			
		}
		
		public FormUploadFile(String field, String fileName, byte[] data) {
			this.mField = field;
			this.mFileName = fileName;
			this.mData = data;
		}
		
		public FormUploadFile(String field, String fileName, byte[] data, String contentType) {
			this.mField = field;
			this.mFileName = fileName;
			this.mData = data;
			this.mContentType = contentType;
		}
	}  
	
}
