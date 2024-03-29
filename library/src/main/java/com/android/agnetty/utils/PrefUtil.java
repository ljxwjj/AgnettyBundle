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

package com.android.agnetty.utils;

import com.android.agnetty.constant.AgnettyCst;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author : Zhenshui.Xia
 * @date   : 2014-2-28
 * @desc   : SharedPreferences工具类
 * 
 * 	public method
 * 	<li>putString(Context, String, String)			保存字符串数据 </li>
 * 	<li>getString(Context, String, String)			获取字符串数据 </li>
 * 	<li>putInt(Context, String, int)				保存整型数据 </li>
 * 	<li>getInt(Context, String, int)				获取整型数据 </li>
 * 	<li>putLong(Context, String, long)				获取长整型数据 </li>
 * 	<li>getLong(Context, String, long)				获取长整型数据 </li>
 * 	<li>putBoolean(Context, String, boolean)		获取布尔值数据 </li>
 * 	<li>getBoolean(Context, String, boolean)		获取布尔值数据 </li>
 * 	<li>putFloat(Context, String, float)			获取浮点型数据 </li>
 * 	<li>getFloat(Context, String, float)			获取浮点型数据 </li>
 * 
 */
public class PrefUtil {
	/**
	 * 保存字符串数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 获取字符串数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		return pref.getString(key, defValue);
	}
	
	
	/**
	 * 保存整型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context, String key, int value) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * 保存整型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		return pref.getInt(key, defValue);
	}
	
	
	/**
	 * 保存长整型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putLong(Context context, String key, long value) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	/**
	 * 保存长整型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		return pref.getLong(key, defValue);
	}
	
	/**
	 * 保存布尔值数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 保存布尔值数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		return pref.getBoolean(key, defValue);
	}
	
	
	/**
	 * 保存浮点型数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putFloat(Context context, String key, float value) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	/**
	 * 保存浮点型数据
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		SharedPreferences pref = context
				.getSharedPreferences(AgnettyCst.AGNETTY, Context.MODE_PRIVATE);
		return pref.getFloat(key, defValue);
	}
}
