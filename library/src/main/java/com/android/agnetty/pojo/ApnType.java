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

import com.android.agnetty.utils.StringUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-3-17
 * @desc   : 网络接入类型
 */
public enum ApnType {
	//未知接入类型
	APN_UNKNOWN("UNKNOWN"), 
	//中国电信APN 2G
	APN_CTWAP("CTWAP"),
	//中国电信APN 3G
	APN_CTNET("CTNET"),  
	//中国联通APN 2G手机
	APN_UNIWAP("UNIWAP"),
	//2G手机  3G手机
	APN_3GWAP("3GWAP"),
	//2G手机 2G电脑
	APN_UNINET("UNINET"),
	//2G手机  3G电脑
	APN_3GNET("3GNET"),
	//中国移动APN 2G
	APN_CMWAP("CMWAP"),
	//中国移动APN 3G
	APN_CMNET("CMNET");
	
	private String mValue;
	
	ApnType(String value) {
		this.mValue = value;
	}
	
	public String getValue() {
		return this.mValue;
	}
	
	public static ApnType valueof(String value) {
		ApnType apnType = ApnType.APN_UNKNOWN;
		
		if(StringUtil.isNotEmpty(value)) {
			String info = value.toUpperCase();
			
			if (info.contains(ApnType.APN_CTNET.getValue())) {
				apnType = ApnType.APN_CTNET;
			} else if (info.contains(ApnType.APN_CTWAP.getValue())) {
				apnType = ApnType.APN_CTWAP;
			} else if (info.contains(ApnType.APN_CMNET.getValue())) {
				apnType = ApnType.APN_CMNET;
			} else if (info.contains(ApnType.APN_CMWAP.getValue())) {
				apnType = ApnType.APN_CMWAP;
			} else if (info.contains(ApnType.APN_UNIWAP.getValue())) {
				apnType = ApnType.APN_UNIWAP;
			} else if (info.contains(ApnType.APN_3GWAP.getValue())) {
				apnType = ApnType.APN_3GWAP;
			} else if (info.contains(ApnType.APN_UNINET.getValue())) {
				apnType = ApnType.APN_UNINET;
			} else if (info.contains(ApnType.APN_3GNET.getValue())) {
				apnType = ApnType.APN_3GNET;
			} 
		}
		
		return apnType;
	}
}
