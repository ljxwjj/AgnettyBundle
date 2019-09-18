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
package com.android.agnetty.external.helper.pojo;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-12-20
 * @desc   : 用户信息
 */
public class User {
	//手机号码
	private String mPhone;
	//对应手机号码的唯一ID
	private String mUUID;
	
	public User() {
		
	}
	
	public User(String phone, String uuID) {
		this.mPhone = phone;
		this.mUUID = uuID;
	}
	
	/**
	 * 设置手机号码
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.mPhone = phone;
	}
	
	/**
	 * 获取手机号码
	 * @return
	 */
	public String getPhone() {
		return this.mPhone;
	}
	
	/**
	 * 设置唯一ID
	 * @param uuID
	 */
	public void setUUID(String uuID) {
		this.mUUID = uuID;
	}
	
	/**
	 * 获取唯一ID
	 * @return
	 */
	public String getUUID() {
		return this.mUUID;
	}
}
