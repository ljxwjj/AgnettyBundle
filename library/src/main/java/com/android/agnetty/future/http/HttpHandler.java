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

package com.android.agnetty.future.http;

import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;

import com.android.agnetty.constant.HttpCst;
import com.android.agnetty.core.AgnettyException;
import com.android.agnetty.core.AgnettyHandler;
import com.android.agnetty.core.AgnettyStatus;
import com.android.agnetty.core.event.MessageEvent;
import com.android.agnetty.utils.HttpUtil;
import com.android.agnetty.utils.LogUtil;
import com.android.agnetty.utils.NetworkUtil;
import com.android.agnetty.utils.StreamUtil;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class HttpHandler extends AgnettyHandler{
	public HttpHandler(Context context) {
		super(context);
	}

	/**
	 * 编码
	 * @param evt
	 * @return
	 */
	public abstract boolean onEncode(MessageEvent evt) throws Exception;

	/**
	 * 压缩
	 * @param evt
	 * @return
	 * @throws Exception
	 */
	public abstract boolean onCompress(MessageEvent evt) throws Exception;

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
		HttpFuture future = (HttpFuture) evt.getFuture();
		evt.setStatus(AgnettyStatus.START);
		future.commitStart(evt.getData());
		LogUtil.d("URL:" + future.getUrl());

		//网络没连上
		if(!NetworkUtil.isNetAvailable(mContext)) {
			AgnettyException ex = new AgnettyException("Network isn't avaiable", AgnettyException.NETWORK_UNAVAILABLE);
			throw ex;
		}

		String method = future.getRequestMothod();
		if(method.equals(HttpCst.GET)) {
			getRequest(future, evt);
		} else if(method.equals(HttpCst.POST)) {
			postRequest(future, evt);
		}
	}

	/**
	 * 执行Get请求
	 * @param future
	 * @param evt
	 * @throws Exception
	 */
	private void getRequest(HttpFuture future, MessageEvent evt) throws Exception{
		if(onEncode(evt)) {
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}
		if(onCompress(evt)) {
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}

		OkHttpClient client;
		InputStream in = null;

		int retry = future.getRetry();
		while(retry>=0) {
			try {
				Headers.Builder builder = new Headers.Builder();
				HashMap<String, String> properties = future.getProperties();
				for(String key : properties.keySet()) {
					builder.add(key, properties.get(key));
				}
				Headers headers = builder.build();

				client = HttpUtil.getHttpClient(mContext, future.getConnectionTimeout(), future.getReadTimeout());
				// Create request for remote resource.
				Request request = new Request.Builder()
						.url(future.getUrl())
						.headers(headers)
						.build();

				// Execute the request and retrieve the response.
				Response response = client.newCall(request).execute();
				if (response.isSuccessful()) {
					in = response.body().byteStream();
					byte[] data = StreamUtil.toHttpByteArray(in);
					evt.setData(data);
					if(onDecompress(evt)){
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					if(onDecode(evt)){
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				} else { //请求失败
					throw new Exception("HTTP RESPONSE ERROR:"+response.code()+"!!!");
				}

				retry = -1;
			} catch(Exception ex) {
				if(retry == 0) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.NETWORK_EXCEPTION);
				} else {
					retry--;
				}
			} finally {
				//释放资源
				try {
					if(in != null) in.close();
				} catch (Exception ex) {
					throw ex;
				}
			}
		}
	}

	/**
	 * 执行Post请求
	 * @param future
	 * @param evt
	 * @throws Exception
	 */
	private void postRequest(HttpFuture future, MessageEvent evt) throws Exception{
		if(onEncode(evt)) {
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}
		if(onCompress(evt)){
			if(!future.isScheduleFuture()) future.cancel();
			return;
		}

		OkHttpClient client;

		int retry = future.getRetry();
		while(retry>=0) {
			try {
				Headers.Builder builder = new Headers.Builder();
				HashMap<String, String> properties = future.getProperties();
				for(String key : properties.keySet()) {
					builder.add(key, properties.get(key));
				}
				Headers headers = builder.build();
				RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (byte[])evt.getData());

				client = HttpUtil.getHttpClient(mContext, future.getConnectionTimeout(), future.getReadTimeout());
				// Create request for remote resource.
				Request request = new Request.Builder()
						.url(future.getUrl())
						.headers(headers)
						.post(requestBody)
						.build();

				// Execute the request and retrieve the response.
				Response response = client.newCall(request).execute();
				if (response.isSuccessful()) {
					evt.setData(response.body().bytes());
					if(onDecompress(evt)){
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					if(onDecode(evt)){
						if(!future.isScheduleFuture()) future.cancel();
						return;
					}
					evt.setStatus(AgnettyStatus.COMPLETED);
					onHandle(evt);
				} else { //请求失败
					throw new Exception("HTTP RESPONSE ERROR:"+response.code()+"!!!");
				}

				retry = -1;
			} catch(Exception ex) {
				if(retry == 0) {
					throw new AgnettyException(ex.getMessage(), AgnettyException.NETWORK_EXCEPTION);
				} else {
					retry--;
				}
			} finally {
				//释放资源
			}
		}
	}
}
