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

package com.android.agnetty.future.upload.form;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import android.content.Context;

import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.core.AgnettyException;
import com.android.agnetty.core.AgnettyHandler;
import com.android.agnetty.core.AgnettyStatus;
import com.android.agnetty.core.event.MessageEvent;
import com.android.agnetty.utils.FileUtil;
import com.android.agnetty.utils.HttpUtil;
import com.android.agnetty.utils.LogUtil;
import com.android.agnetty.utils.NetworkUtil;
import com.android.agnetty.utils.StreamUtil;
import com.android.agnetty.utils.StringUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class FormUploadHandler extends AgnettyHandler{
	//数据分隔线
	private static final String BOUNDARY = "---------------------------7da2137580612"; 
	
	public FormUploadHandler(Context context) {
		super(context);
	}
	
	/**
	 * 开始（上传前）
	 * @param evt
	 * @return
	 */
	public abstract boolean onStart(MessageEvent evt) throws Exception;

	/**
	 * 解压缩
	 * @param evt
	 * @return
	 * @throws Exception
	 */
	public abstract boolean onDecompress(MessageEvent evt) throws Exception;
	
	/**
	 * 解码
	 * @param evt
	 * @return
	 */
	public abstract boolean onDecode(MessageEvent evt) throws Exception;
	
	/**
	 * 业务处理
	 * @param evt
	 * @return
	 */
	public abstract void onHandle(MessageEvent evt) throws Exception;
	
	@Override
	public void onExecute(MessageEvent evt) throws Exception {
		
		FormUploadFuture future = (FormUploadFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		future.commitStart(evt.getData());
		
		//网络没连上
		if(!NetworkUtil.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.NETWORK_UNAVAILABLE);
			throw ex;
		}
						
		HttpURLConnection conn = null;
		DataOutputStream dataOut = null;
		InputStream in = null;
		
		if(onStart(evt)) {
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}
		
		int retry = future.getRetry();
		while(retry>=0) {
			try {
				conn = HttpUtil.createPostHttpURLConnection(mContext, 
						 future.getUrl(), 
						 future.getConnectionTimeout(), 
						 future.getReadTimeout(), 
						 future.getProperties()); 
				
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
				
				StringBuilder sb = new StringBuilder();
				// 上传的表单参数部分，格式请参考文章
				for (Map.Entry<String, String> entry : future.getUploadFields().entrySet()) {// 构建表单字段内容
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
				}
				
				dataOut = new DataOutputStream(conn.getOutputStream());
				byte[] data = sb.toString().getBytes();
				dataOut.write(data);// 发送表单字段数据
				
				// 上传的文件部分，格式请参考文章
				for (FormUploadFile formFile : future.getUploadFiles()){
					StringBuilder split = new StringBuilder();
					split.append("--");
					split.append(BOUNDARY);
					split.append("\r\n");
					split.append("Content-Disposition: form-data;name=\"" + formFile.getField() + "\";filename=\"" + formFile.getName() + "\"\r\n");
					split.append("Content-Type: " + formFile.getContentType() + "\r\n\r\n");
					dataOut.write(split.toString().getBytes());
					if(formFile.getData() != null) { 
						dataOut.write(formFile.getData());
					} else if(FileUtil.isFileExist(formFile.getPath())) {
						FileInputStream fileIn = new FileInputStream(formFile.getPath());  
						byte[] buffer = StreamUtil.toByteArray(fileIn);
						dataOut.write(buffer);
						fileIn.close();
					}
					dataOut.write("\r\n".getBytes());
				}
				
				byte[] end = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
				dataOut.write(end);
				dataOut.flush();
				
				int code = conn.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					in = conn.getInputStream();
					byte[] result = StreamUtil.toByteArray(in);
					evt.setData(result);
					if(onDecompress(evt)) {
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					if(onDecode(evt)) {
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				}  else { //请求失败，下载失败
					throw new Exception("HTTP RESPONSE ERROR:"+code+"!!!");
				}
				
				retry = -1;
			} catch (Exception ex) {
				if(retry == 0) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.NETWORK_EXCEPTION);
				} else {
					retry--;
				}
			} finally {
				//释放资源
				try {
					if(conn != null) conn.disconnect();
					if(dataOut != null) dataOut.close();
					if(in != null) in.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
}
