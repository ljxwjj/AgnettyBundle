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

package com.android.agnetty.external.helper.system;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 插件activity启动的真实activity，不过不会被真正启动， 真正启动的是stub activity，
 * 			   宿主模块的清单中应声明该activity，如果该activity被启动，则说明启动插件
 * 			   的activity失败。
 */
public class CoreActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView errorTxt = new TextView(this);
		errorTxt.setText("Stub activity start failed!!!");
		setContentView(errorTxt);
	}

}
