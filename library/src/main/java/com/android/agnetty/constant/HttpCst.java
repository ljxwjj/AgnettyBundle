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

package com.android.agnetty.constant;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-2-27
 * @desc   : HTTP请求相关常量
 */
public class HttpCst {
	//Http请求默认连接超时时间, 10s
	public static final int CONNECTION_TIMEOUT 		= 10000; 	
	//Http请求默认数据读取超时时间, 10s
	public static final int READ_TIMEOUT       		= 10000; 
	
	//GET请求
	public static final String GET                  = "GET";
	//POST请求
	public static final String POST                 = "POST";
	
	public static final String ACCEPT_ENCODING		= "Accept-Encoding";
	
	private HttpCst() {
		super();
	}
}
