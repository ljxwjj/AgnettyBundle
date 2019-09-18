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
package com.android.agnetty.external.helper.pojo;

import android.graphics.Bitmap;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-11-20
 * @desc   : apk插件包的信息
 */
public class ApkInfo {
	//插件名称
	public String mName;
	//插件包名
	public String mPackageName;
	//插件描述
	public String mDesc;
	//插件图标
	public Bitmap mIcon;
	//插件启动类名
	public String mStubClass;
	
	public ApkInfo() {
		
	}
	
	public ApkInfo(String name, String packageName, String desc, 
			Bitmap icon, String stubClass) {
		this.mName = name;
		this.mPackageName = packageName;
		this.mDesc = desc;
		this.mIcon = icon;
		this.mStubClass = stubClass;
	}
	
	/**
	 * 设置插件的名称
	 * @param name
	 */
	public void setName(String name) {
		this.mName = name;
	}
	
	/**
	 * 获取插件的名称
	 * @return
	 */
	public String getName() {
		return this.mName;
	}
	
	/**
	 * 设置插件的包名
	 * @param packageName
	 */
	public void setPackageName(String packageName) {
		this.mPackageName = packageName;
	}
	
	/**
	 * 获取插件的包名
	 * @return
	 */
	public String getPackageName() {
		return this.mPackageName;
	}
	
	/**
	 * 设置插件的描述
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.mDesc = desc;
	}
	
	/**
	 * 获取插件的描述
	 * @return
	 */
	public String getDesc() {
		return this.mDesc;
	}
	
	/**
	 * 设置插件的图标
	 * @param icon
	 */
	public void setIcon(Bitmap icon) {
		this.mIcon = icon;
	}
	
	/**
	 * 获取插件的图标
	 * @return
	 */
	public Bitmap getIcon() {
		return this.mIcon;
	}
	
	/**
	 * 设置插件启动类名,需用全名,如果启动一个Activity: com.android.agnetty.test.TestActivity
	 * @param stubClass
	 */
	public void setStubClass(String stubClass) {
		this.mStubClass = stubClass;
	}
	
	/**
	 * 获取插件启动类名
	 * @return
	 */
	public String getStubClass() {
		return this.mStubClass;
	}
		
}
