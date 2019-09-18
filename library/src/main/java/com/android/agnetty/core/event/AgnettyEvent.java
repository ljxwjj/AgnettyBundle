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

package com.android.agnetty.core.event;

import com.android.agnetty.core.AgnettyFuture;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class AgnettyEvent {
	private AgnettyFuture mFuture;
	
	public void setFuture(AgnettyFuture future) {
		this.mFuture = future;
	}
	
	public AgnettyFuture getFuture() {
		return this.mFuture;
	}
}
