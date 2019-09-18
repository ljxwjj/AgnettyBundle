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

import android.content.Context;
import com.android.agnetty.core.event.ExceptionEvent;
import com.android.agnetty.core.event.MessageEvent;
import com.android.agnetty.future.local.LocalHandler;
import com.android.agnetty.utils.FileUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class DownloadProgressHandler extends LocalHandler{

	public DownloadProgressHandler(Context context) {
		super(context);
		
	}

	@Override
	public void onHandle(MessageEvent evt) throws Exception {
		DownloadItem item = (DownloadItem)evt.getData();
		long fileSize = FileUtil.getFileSize(item.mPath);
		int progress = fileSize==-1 ? 0 : (int)(fileSize * 100f / item.mTotal);
		evt.getFuture().commitComplete(progress);
	}

	@Override
	public void onException(ExceptionEvent evt) {
		
	}

	@Override
	public void onDispose() {
		
	}

}
