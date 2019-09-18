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
 * @date   : 2013-9-19
 * @desc   : 处理器Handler执行状态
 */
public class AgnettyStatus {
	//Handler状态未知
	public static final int	UNKNOWN        = 0;
	
	//Handler开始执行
	public static final int	START          = 1;
	
	//Handler发送执行进度
	public static final int	PROGRESSING    = 2;

	//Handler执行完成
	public static final int COMPLETED      = 3;

	//Handler执行发生异常
	public static final int EXCEPTION      = 4;

	private AgnettyStatus() {
		super();
	}
}
