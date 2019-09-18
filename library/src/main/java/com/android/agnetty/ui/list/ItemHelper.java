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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

/**
 * @author : wangjianjie
 * @date   : 2013-11-11
 * @desc   : 组装Item视图与数据的逻辑类，实现该接口的类以单例模式运行
 */
public abstract class ItemHelper<T extends MessageHistory> {

	/**
	 * 数据与视图绑定
	 */
	public void bindData(Context context, T message, View view, Object object) {
		IViewHolder holder = (IViewHolder) view.getTag();
		if (message.showTime != null) {
			holder.onlyTime.setText(message.showTime);
			holder.onlyTime.setVisibility(View.VISIBLE);
		} else {
			holder.onlyTime.setVisibility(View.GONE);
		}
	}
	
	public abstract IViewHolder createHolder(View view);
	
	public abstract T loadByCursor(Cursor c);
	public abstract T loadByJSON(Context context, JSONObject body) throws JSONException;
	
	public class IViewHolder {
		public TextView onlyTime;
	}
}

