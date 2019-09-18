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
 * @date   : 2013-9-19
 * @desc   : 框架系统常量
 */
public class AgnettyCst {
	//项目名
	public static final String AGNETTY 					= "ELvShi";
	//框架是否处于调试模式
	public static boolean DEBUG 						= true;		
	//框架调试标记
	public static final String TAG 						=  AGNETTY;	
	
	
	
	//Bundle key:任务ID
	public static final String FUTURE_ID 					= "futureID";	
	//Bundle key:任务使用的线程池
	public static final String FUTURE_POOL 					= "futurePool";	
	public static final String FUTURE_HASHCODE 				= "futureHashcode";	
	//Bundle key:是否取消定时任务
	public static final String CANCEL_SCHEDULED 			= "cancelScheduled";	
	//定时/延时任务广播Intent-ACTION
	public static final String ALARM_ACTION  				= "com.android.agnetty.ALARM_ACTION";
	
	
	
		
	
	private AgnettyCst() {
		super();
	}
}
