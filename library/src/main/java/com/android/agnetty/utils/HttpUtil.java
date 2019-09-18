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

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.android.agnetty.constant.AgnettyCst;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class HttpUtil {
	public static final String URL_AND_PARA_SEPARATOR = "?";
	public static final String PARAMETERS_SEPARATOR   = "&";
	public static final String PATHS_SEPARATOR        = "/";
	public static final String EQUAL_SIGN             = "=";

	/**
	 * 获取OkHttpClient实例
	 * @param context
	 * 				上下文
	 * @param connectionTimeout
	 * 				连接超时时间
	 * @param readTimeout
	 * 				数据读取超时时间
	 * @return
	 */
	public static OkHttpClient getHttpClient(Context context,
											 int connectionTimeout,
											 int readTimeout) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
				.writeTimeout(readTimeout, TimeUnit.MILLISECONDS)
				.readTimeout(readTimeout, TimeUnit.MILLISECONDS);

		Proxy proxy = NetworkUtil.getNetworkProxy(context);
		if(proxy != null){
			builder.proxy(proxy);
		} else {
			if (!AgnettyCst.DEBUG) {
				System.getProperties().remove("proxySet");
				System.getProperties().remove("http.proxyHost");
				System.getProperties().remove("http.proxyPort");
			}
		}
		return builder.build();
	}

	/**
	 * 获取HttpURLConnection实例，如果框架支持OKHttp api，则使用OKHttp获取其实例，否则使用Sun内置api获取实例
	 * @param context
	 * @param url
	 * @return
	 */
	public static HttpURLConnection getHttpURLConnection(Context context, String url) {
		if(context==null || StringUtil.isEmpty(url)) return null;

		HttpURLConnection conn = null;
		try {
			URL httpUrl = new URL(url);
			Proxy proxy = NetworkUtil.getNetworkProxy(context);

			if(proxy != null){
				conn = (HttpURLConnection) httpUrl.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) httpUrl.openConnection();
				if (!AgnettyCst.DEBUG) {
					System.getProperties().remove("proxySet");
					System.getProperties().remove("http.proxyHost");
					System.getProperties().remove("http.proxyPort");
				}
			}
		} catch (Exception ex) {
		}

		return conn;
	}

	/**
	 * 创建Get请求HttpURLConnection实例，并设置相关属性
	 * @param context
	 * 				上下文
	 * @param url
	 * 				请求地址
	 * @param connectionTimeout
	 * 				连接超时时间
	 * @param readTimeout
	 * 				数据读取超时时间
	 * @param properties
	 * 				Http请求头属性
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection createGetHttpURLConnection(Context context,
															   String url,
															   int connectionTimeout,
															   int readTimeout,
															   HashMap<String, String> properties) throws Exception{
		HttpURLConnection conn = HttpUtil.getHttpURLConnection(context, url);
		//设置是否从httpUrlConnection读入，默认情况下是true;
		conn.setDoInput(true);
		//请求方式
		conn.setRequestMethod("GET");
		//设置请求连接超时时间
		conn.setConnectTimeout(connectionTimeout);
		//设置数据读取超时时间
		conn.setReadTimeout(readTimeout);
		//设置属性值
		for(String key : properties.keySet()) {
			conn.setRequestProperty(key, properties.get(key));
		}

		return conn;
	}

	/**
	 * 创建Post请求HttpURLConnection实例，并设置相关属性
	 * @param context
	 * 				上下文
	 * @param url
	 * 				请求地址
	 * @param connectionTimeout
	 * 				连接超时时间
	 * @param readTimeout
	 * 				数据读取超时时间
	 * @param properties
	 * 				Http请求头属性
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection createPostHttpURLConnection(Context context,
																String url,
																int connectionTimeout,
																int readTimeout,
																HashMap<String, String> properties) throws Exception{
		HttpURLConnection conn = HttpUtil.getHttpURLConnection(context, url);
		//设置是否从httpUrlConnection读入，默认情况下是true;
		conn.setDoInput(true);
		//输出数据
		conn.setDoOutput(true);
		//Post 请求不能使用缓存
		conn.setUseCaches(false);
		//请求方式
		conn.setRequestMethod("POST");
		//设置请求连接超时时间
		conn.setConnectTimeout(connectionTimeout);
		//设置数据读取超时时间
		conn.setReadTimeout(readTimeout);
		//设置属性值
		for(String key : properties.keySet()) {
			conn.setRequestProperty(key, properties.get(key));
		}

		return conn;
	}

	/**
	 *
	 * @param paramsMap
	 * @return
	 */
	public static String joinParams(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}

		StringBuilder paras = new StringBuilder();
		Iterator<Map.Entry<String, String>> ite = paramsMap.entrySet().iterator();
		while (ite.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)ite.next();
			paras.append(entry.getKey()).append(EQUAL_SIGN).append(entry.getValue());
			if (ite.hasNext()) {
				paras.append(PARAMETERS_SEPARATOR);
			}
		}
		return paras.toString();
	}

	/**
	 *
	 * @param paramsMap
	 * @return
	 */
	public static String joinParamsWithEncode(Map<String, String> paramsMap, String encoder) {
		StringBuilder paras = new StringBuilder("");
		if (paramsMap != null && paramsMap.size() > 0) {
			Iterator<Map.Entry<String, String>> ite = paramsMap.entrySet().iterator();
			try {
				while (ite.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>)ite.next();
					paras.append(entry.getKey()).append(EQUAL_SIGN).append(URLEncoder.encode(entry.getValue(), encoder));
					if (ite.hasNext()) {
						paras.append(PARAMETERS_SEPARATOR);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return paras.toString();
	}
}
