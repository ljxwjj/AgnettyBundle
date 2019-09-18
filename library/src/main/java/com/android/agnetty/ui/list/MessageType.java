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

package com.android.agnetty.ui.list;


/**
 * @author : wangjianjie
 * @date : 2013-11-11
 * @desc :
 */
@SuppressWarnings("rawtypes")
public interface MessageType {

	/**
	 * 获取类型
	 */
	public int getTypeId();

	public int getTypeIndex();

	/**
	 * 获取消息绑定后的视图对象
	 */
	public int getResource();

	/**
	 * 获取该类型对应的组装对象
	 */
	public ItemHelper getHelper();
}