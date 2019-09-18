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

package com.android.agnetty.future.local;

import android.content.Context;

import com.android.agnetty.core.AgnettyFuture;
import com.android.agnetty.core.AgnettyHandler;
import com.android.agnetty.core.event.MessageEvent;

/**
 * @author : Zhenshui.Xia
 * @date   : 20void13-9-19
 * @desc   : 本地业务逻辑处理器，一般指非网络的业务逻辑
 */
public abstract class LocalHandler extends AgnettyHandler{

	public LocalHandler(Context context) {
		super(context);
		
	}

	/**
	 * 处理本地业务逻辑
	 * @param evt
	 * @return 默认为false，意指如果有后续业务逻辑回调事件，则继续处理； true，不在
	 * 		   继续处理后续的回调事件
	 * @throws Exception
	 */
	public abstract void onHandle(MessageEvent evt) throws Exception;
	
	
	@Override
	public void onExecute(MessageEvent evt) throws Exception {
		evt.getFuture().commitStart(evt.getData());
		onHandle(evt);
	}
}
