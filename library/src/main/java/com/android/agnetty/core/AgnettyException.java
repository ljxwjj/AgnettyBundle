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
package com.android.agnetty.core;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-12-26
 * @desc   : 
 */
public class AgnettyException extends Exception{
	//网络不可用
	public static final int NETWORK_UNAVAILABLE  = 100; 
	//网络异常
	public static final int NETWORK_EXCEPTION  	 = 101; 
	
	//错误编码
	private int mCode;
	

	public AgnettyException(String msg, int code) {
		super(msg);
		this.mCode = code;
	}
	
	/**
	 * 设置错误编码
	 * @param code
	 */
	public void setCode(int code) {
		this.mCode = code;
	}
	
	/**
	 * 获取错误编码
	 * @return
	 */
	public int getCode() {
		return this.mCode;
	}
}
