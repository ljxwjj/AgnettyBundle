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

package com.android.agnetty.pojo;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-3-17
 * @desc   : 网络运营商类型
 */
public enum OperatorType {
	//未知运营商
	OPERATOR_UNKNOWN("UNKNOWN"),
	//移动
	OPERATOR_CMCC("CMCC"),
	//电信
	OPERATOR_CTC("CTC"),
	//联通
	OPERATOR_CUC("CUC");
	
	private String mValue;
	
	OperatorType(String value) {
		this.mValue = value;
	}
	
	public String getValue() {
		return this.mValue;
	}
}
