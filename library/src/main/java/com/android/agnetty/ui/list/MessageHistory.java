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

package com.android.agnetty.ui.list;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.agnetty.ui.list.ItemHelper.IViewHolder;
/**
 * @author : wangjianjie
 * @date   : 2013-11-11
 * @desc   : 消息实体Bean的父类，实现了Bean与Helper的整合
 */
public abstract class MessageHistory {
	/**
	 * 消息id
	 */
	public int id = 0;

	public String account;
	
	public Date createTime = new Date();
	
	public IMessageStatus status;
	
	public String showTime;
	
	public String content;

	/**
	 * 获取消息实体的类型
	 */
	public abstract MessageType getType();
	
	public abstract void save(IMessageDao messageDao);
	
	/**
	 * 获取消息绑定后的视图对象
	 * @param context
	 * @param view 
	 * 可能为空，如果不为空，则把当前bean数据绑定到该view上，否则内部创建一个view对象
	 * @return 返回消息绑定后的视图对角
	 */
	@SuppressWarnings("unchecked")
	public View getView(Context context, View view, Object object) {
		MessageType type = getType();
		if (view == null) {
			view = LinearLayout.inflate(context, type.getResource(), null);
			IViewHolder holder = type.getHelper().createHolder(view);
			holder.onlyTime = (TextView) view.findViewById(android.R.id.text2);
			view.setTag(holder);
		}
		type.getHelper().bindData(context, this, view, object);
		return view;
	}
	
}
