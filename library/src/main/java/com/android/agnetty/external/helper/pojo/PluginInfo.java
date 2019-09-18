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

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-11-20
 * @desc   : 插件信息描述
 */
public class PluginInfo {
	//所属插件序列号
	private int mSerialID;
	//设置插件的类型
	private int mType;
	//插件包url
	private String mApkUrl;
	//插件包存储路径
	private String mApkPath;
	//当前插件包校验值（md5）
	private String mMD5;
	//插件包是否可用，
	private boolean mEnabled = true;
	//插件包本身信息
	private ApkInfo mApkInfo;
	
	public PluginInfo() {
		
	}
	
	public PluginInfo(int serialID, int type, String apkUrl, 
			String apkPath,String md5, boolean enabled, ApkInfo apkInfo) {
		this.mSerialID = serialID;
		this.mType = type;
		this.mApkUrl = apkUrl;
		this.mApkPath = apkPath;
		this.mMD5 = md5;
		this.mEnabled = enabled;
		this.mApkInfo = apkInfo;
	}
	
	/**
	 * 设置插件序列号, 任何一个插件包的修改,都必须增加该应用的插件序列号
	 * @param sericalID
	 */
	public void setSerialID(int sericalID) {
		this.mSerialID = sericalID;
	}
	
	/**
	 * 获取插件序列号
	 * @return
	 */
	public int getSericalID() {
		return this.mSerialID;
	}
	
	/**
	 * 设置插件的类型
	 * @param type
	 */
	public void setType(int type) {
		this.mType = type;
	}
	
	/**
	 * 获取插件的类型
	 * @return
	 */
	public int getType() {
		return this.mType;
	}
	
	/**
	 * 设置插件包url
	 * @param apkUrl
	 */
	public void setApkUrl(String apkUrl) {
		this.mApkUrl = apkUrl;
	}
	
	/**
	 * 获取插件包url
	 * @return
	 */
	public String getApkUrl() {
		return this.mApkUrl;
	}
	
	/**
	 * 设置插件包保存路径
	 * @param apkPath
	 */
	public void setApkPath(String apkPath) {
		this.mApkPath = apkPath;
	}
	
	/**
	 * 获取插件包保存路径
	 * @return
	 */
	public String getApkPath() {
		return this.mApkPath;
	}
	
	/**
	 * 设置当前插件包校验值（md5），如果客户端对插件包修改，则该插件将不可用
	 * @param md5
	 */
	public void setMD5(String md5) {
		this.mMD5 = md5;
	}
	
	/**
	 * 获取当前插件包校验值（md5）
	 * @return
	 */
	public String getMD5() {
		return this.mMD5;
	}
	
	/**
	 * 设置插件是否可用，被禁用的插件包将不会显示，默认为true
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.mEnabled = enabled;
	}
	
	/**
	 * 获取插件是否可用
	 * @return
	 */
	public boolean getEnabled() {
		return this.mEnabled;
	}
	
	/**
	 * 设置插件包本身信息，如插件名、插件名描述、插件图标
	 * @param apkInfo
	 */
	public void setApkInfo(ApkInfo apkInfo) {
		this.mApkInfo = apkInfo	;
	}
	
	/**
	 * 获取插件包本身信息
	 * @return
	 */
	public ApkInfo getApkInfo() {
		return this.mApkInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PluginInfo[sericalID:").append(mSerialID)
			   .append(" type:").append(mType)
			   .append(" apkUrl:").append(mApkUrl)
			   .append(" apkPath:").append(mApkPath)
			   .append(" md5:").append(mMD5)
			   .append(" enabled:").append(mEnabled)	   
			   .append(" name:").append(mApkInfo!=null?mApkInfo.mName : null)
			   .append(" packageName:").append(mApkInfo!=null?mApkInfo.mPackageName : null)
			   .append(" desc:").append(mApkInfo!=null?mApkInfo.mDesc : null)
			   .append(" icon:").append(mApkInfo!=null?mApkInfo.mIcon : null)
			   .append(" stubclass:").append(mApkInfo!=null?mApkInfo.mStubClass : null)
			   .append("]");
			   
		return builder.toString();
	}
}
