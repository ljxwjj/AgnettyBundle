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

package com.android.agnetty.future.upload.multi;


/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 上传的多媒体文件
 */
public class MultiUploadFile {   
	//上传文件的路径
	private String mPath;  
	//请求参数名称
	private String mField;  
	//文件名称 
	private String mFileName;  
	//内容类型
	private String mContentType = "application/octet-stream"; 

	public MultiUploadFile() {
		
	}
	
	public MultiUploadFile(String field, String fileName, String path) {
		this.mField = field;
		this.mFileName = fileName;
		this.mPath = path;
	}
	
	public MultiUploadFile(String field, String fileName, String path, String contentType) {
		this.mField = field;
		this.mFileName = fileName;
		this.mPath = path;
		this.mContentType = contentType;
	}
	
	/**
	 * 设置上传文件的路径
	 * @param path
	 */
	public void setPath(String path) {
		this.mPath = path;
	}
	
	/**
	 * 获取上传文件的路径
	 * @return
	 */
	public String getPath() {
		return this.mPath;
	}
	
	/**
	 * 设置文件名称 
	 * @param fileName
	 */
	public void setName(String fileName) {
		this.mFileName = fileName;
	}
	
	/**
	 * 获取文件名称 
	 * @return
	 */
	public String getName() {
		return this.mFileName;
	}
	
	/**
	 * 设置请求参数名字
	 * @param field
	 */
	public void setFiled(String field) {
		this.mField = field;
	}
	
	/**
	 * 获取请求参数名字
	 * @return
	 */
	public String getField() {
		return this.mField;
	}
	
	/**
	 * 设置文件内容类型
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.mContentType = contentType;
	}
	
	/**
	 * 设置文获取件内容类型
	 * @return
	 */
	public String getContentType() {
		return this.mContentType;
	}
}
