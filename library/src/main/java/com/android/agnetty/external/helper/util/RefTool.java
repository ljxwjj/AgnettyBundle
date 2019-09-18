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

package com.android.agnetty.external.helper.util;

import java.lang.reflect.Field;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 设置/获取类的属性值
 */
public class RefTool<T> {
	private Object mObj;
	private String mFieldName;

	private boolean mInited;
	private Field mField;

	public RefTool(Object obj, String fieldName) {
		if (obj == null) {
			throw new IllegalArgumentException("obj cannot be null");
		}
		
		this.mObj = obj;
		this.mFieldName = fieldName;
	}

	private void prepare() {
		if (mInited) return;
		mInited = true;

		Class<?> clazz = mObj.getClass();
		while (clazz != null) {
			try {
				Field filed = clazz.getDeclaredField(mFieldName);
				filed.setAccessible(true);
				mField = filed;
				return;
			} catch (Exception e) {
				
			} finally {
				clazz = clazz.getSuperclass();
			}
		}
	}

	/**
	 * 获取类属性的值
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public T get() throws NoSuchFieldException, IllegalAccessException,
			IllegalArgumentException {
		prepare();

		if (mField == null)
			throw new NoSuchFieldException();

		try {
			@SuppressWarnings("unchecked")
			T obj = (T) mField.get(mObj);
			return obj;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("unable to cast object");
		}
	}

	/**
	 * 设置类属性的值
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void set(T value) throws NoSuchFieldException, IllegalAccessException,
			IllegalArgumentException {
		prepare();

		if (mField == null)
			throw new NoSuchFieldException();

		mField.set(mObj, value);
	}
}
