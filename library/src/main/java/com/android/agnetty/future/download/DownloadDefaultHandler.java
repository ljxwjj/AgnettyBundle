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

package com.android.agnetty.future.download;

import com.android.agnetty.core.AgnettyStatus;
import com.android.agnetty.core.event.ExceptionEvent;
import com.android.agnetty.core.event.MessageEvent;
import com.android.agnetty.utils.LogUtil;

import android.content.Context;
import android.util.Log;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class DownloadDefaultHandler extends DownloadHandler{

	public DownloadDefaultHandler(Context context) {
		super(context);
	}

	@Override
	public void onHandle(MessageEvent evt) throws Exception {
		switch(evt.getStatus()) {
			case AgnettyStatus.START:
				evt.getFuture().commitStart(null);
				break;
				
			case AgnettyStatus.PROGRESSING:
				evt.getFuture().commitProgress(null, (Integer)evt.getData());
				break;
			
			case AgnettyStatus.COMPLETED:
				evt.getFuture().commitComplete(null);
				break;
			
			default:
				break;
		}
	}

	@Override
	public void onException(ExceptionEvent evt) {
		evt.getFuture().commitException(null, evt.getException());
	}

	@Override
	public void onDispose() {
		
	}

}
