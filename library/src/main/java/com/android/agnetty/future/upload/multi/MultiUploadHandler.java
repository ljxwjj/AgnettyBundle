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

package com.android.agnetty.future.upload.multi;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class MultiUploadHandler extends AgnettyHandler{
	//数据分隔线
	private static final String BOUNDARY = "---------------------------7da2137580612"; 
	
	public MultiUploadHandler(Context context) {
		super(context);
	}
	
	/**
	 * 开始（上传前）
	 * @param evt
	 * @return
	 */
	public abstract boolean onStart(MessageEvent evt) throws Exception;
	
	/**
	 * 上传业务处理
	 * @param evt
	 * @return
	 */
	public abstract void onHandle(MessageEvent evt) throws Exception;
	
	@Override
	public void onExecute(MessageEvent evt) throws Exception {	
		//上传任务开始		
		MultiUploadFuture future = (MultiUploadFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		onHandle(evt);
		
		//网络没连上
		if(!NetworkUtil.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.NETWORK_UNAVAILABLE);
			throw ex;
		}
			
		if(onStart(evt)) {
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}
		
		int uploadMode = future.getUploadMode();
		if(uploadMode == MultiUploadFuture.DIRECT_MODE) {//直接上传
			directUpload(future, evt);
		} else if(uploadMode == MultiUploadFuture.REGET_MODE) {//断点续传
			regetUpload(future, evt);
		}
	}	
	
	/**
	 * 直接上传
	 * @param evt
	 * @throws Exception
	 */
	private void directUpload(MultiUploadFuture future, MessageEvent evt) throws Exception{
		HttpURLConnection conn = null;
		DataOutputStream dataOut = null;
		
		int retry = future.getRetry();
		while(retry>=0) {
			try {
				conn = HttpUtil.createPostHttpURLConnection(mContext, 
						   future.getUrl(), 
						   future.getConnectionTimeout(), 
						   future.getReadTimeout(), 
						   future.getProperties()); 
				
				//上传数据块大小，保证每次文件流达到指定大小就发送一次，解决只能上传小文件问题（大文件将导致dvm oom）
				conn.setChunkedStreamingMode(56 * 1024);// 56KB 
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
				 
				MultiUploadFile multiFile = future.getUploadFile();
				//请求参数名、上传文件不存在
				if(multiFile==null || !FileUtil.isFileExist(multiFile.getPath())) {
					throw new Exception("FILE NOT FOUND ERROR!!!");
				}
				
				File file = new File(multiFile.getPath());
				dataOut = new DataOutputStream(conn.getOutputStream()); 
				
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
				
				byte[] data = sb.toString().getBytes();
				dataOut.write(data);// 发送表单字段数据
				
				//文件开始部分
				StringBuilder start = new StringBuilder();
				start.append("--")
					.append(BOUNDARY)
					.append("\r\n")
					.append("Content-Disposition: form-data;name=\"" + multiFile.getField() + "\";filename=\"" + multiFile.getName() + "\"\r\n")
					.append("Content-Type: " + multiFile.getContentType() + "\r\n\r\n");
				dataOut.write(start.toString().getBytes());
				dataOut.flush();
				
				//上传文件数据
				FileInputStream fileIn = new FileInputStream(file);  
				int maxBufferSize = 56 * 1024; //56KB  
				long uploadlen = 0;
				long fileSize = file.length();
				int preProgress = 0; 
				int curProgress = 0; 
				int bytesAvailable = fileIn.available();  
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);  
				byte[] buffer = new byte[bufferSize];  
				int bytesRead = fileIn.read(buffer, 0, bufferSize);  
	
	            while (bytesRead > 0) {  
	            	dataOut.write(buffer, 0, bufferSize);  
	            	uploadlen += bufferSize;  
					curProgress = (int)(uploadlen * 100.0f / fileSize ); 
					
					//通知上传进度更新
					if(curProgress > preProgress) { 
						preProgress = curProgress;
						evt.setData(curProgress);
						evt.setStatus(AgnettyStatus.PROGRESSING);
						onHandle(evt);
					}
	 
	                bytesAvailable = fileIn.available();  
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);  
	                bytesRead = fileIn.read(buffer, 0, bufferSize);  
	            }  
	            
	            dataOut.flush();
	            fileIn.close();
				
	            //上传接收部分
				StringBuilder end = new StringBuilder();
				end.append("\r\n--").append(BOUNDARY).append("--\r\n");
				dataOut.write(end.toString().getBytes());
				dataOut.flush();
				
				//文件上传成功
				int code = conn.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				}  else { 
					//请求失败，上传失败
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
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}
	
	/**
	 * 断点续传
	 * @param evt
	 * @throws Exception
	 */
	private void regetUpload(MultiUploadFuture future, MessageEvent evt) throws Exception {
		//暂不支持
		directUpload(future, evt);
	}
}
