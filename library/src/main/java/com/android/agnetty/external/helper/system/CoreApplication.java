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

import com.android.agnetty.external.helper.util.RefTool;
import android.app.Application;
import android.content.Context;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 插件Application，使用插件系统时，必须继续该Application。
 */
public abstract class CoreApplication extends Application{

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(newBase);
		
		//应用在启动activity、service等功能时,都会使用context的classloader加载类，
		//通过修改context的classloader，重定向加载的类型，实现插件功能
		try {
			Context base = new RefTool<Context>(this, 
					"mBase").get();
			Object packageInfo = new RefTool<Object>(base, 
					"mPackageInfo").get();
			
			RefTool<ClassLoader> fieldClassLoader = new RefTool<ClassLoader>(packageInfo, 
					"mClassLoader");
			ClassLoader classLoader= fieldClassLoader.get();
			
			StubClassLoader.setDefaultLoader(classLoader);
			StubClassLoader stubClassLoader = new StubClassLoader(classLoader);
			fieldClassLoader.set(stubClassLoader);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
				
	}
}
