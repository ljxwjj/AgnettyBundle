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

package com.android.agnetty.core;


/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 处理器Handler执行后提交的结果， 该结果将被提交给任务注册的监听器处理
 * 
 * 			 example:
 * 			//------------------------------------------
 * 			 	AgnettyResult result = new AgnettyResult();
 * 			 	result.setException(ex);
 * 			 	//setException(result) or
 * 			 	setResult(result, AgnettyStatus.EXCEPTION); 
 * 			--------------------------------------------//
 */
public class AgnettyResult {	
	private Object mAttach;
	private Exception mException;
	private int mProgress;
	
	public AgnettyResult() {
		
	}
	
	/**
	 * 设置Handler提交的附件
	 * @param attach
	 */
	public void setAttach(Object attach) {
		this.mAttach = attach;
	}
	
	/**
	 * 获取Handler提交的附件
	 * @return
	 */
	public Object getAttach() {
		return this.mAttach;
	}
	
	/**
	 * 设置Handler异常执行的异常，一般用在Handler的onException事件
	 * 时提交的异常结果
	 * @param ex
	 */
	public void setException(Exception ex) {
		this.mException = ex;
	}
	
	/**
	 * 获取Handler执行的异常
	 * @return
	 */
	public Exception getException() {
		return this.mException;
	}
	
	/**
	 * 设置Handler的处理进度
	 * @param progress
	 */
	public void setProgress(int progress) {
		this.mProgress = progress;
	}
	
	/**
	 * 获取Handler的处理进度
	 * @return
	 */
	public int getProgress() {
		return this.mProgress;
	}
}
