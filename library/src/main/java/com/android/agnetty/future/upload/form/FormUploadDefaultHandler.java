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

package com.android.agnetty.future.upload.form;

import com.android.agnetty.core.event.ExceptionEvent;
import com.android.agnetty.core.event.MessageEvent;

import android.content.Context;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class FormUploadDefaultHandler extends FormUploadHandler{

	public FormUploadDefaultHandler(Context context) {
		super(context);
		
	}
	
	@Override
	public boolean onStart(MessageEvent evt) throws Exception {
		return false;
	}

	@Override
	public boolean onDecompress(MessageEvent evt) throws Exception {
		return false;
	}

	@Override
	public boolean onDecode(MessageEvent evt) throws Exception {
		
		return false;
	}

	@Override
	public void onHandle(MessageEvent evt) throws Exception {
		evt.getFuture().commitComplete(evt.getData());
	}

	@Override
	public void onException(ExceptionEvent evt) {
		evt.getFuture().commitException(null, evt.getException());
	}

	@Override
	public void onDispose() {
		
	}

}
