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

package com.android.agnetty.database;

import java.util.ArrayList;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 数据库业务处理基类
 */
public class AgnettyDao {
	private Context mContext;
	private ContentResolver mContentResolver;
	
	public AgnettyDao(Context context) {
		this.mContext = context;
		this.mContentResolver = context.getContentResolver();
	}
	
	/**
	 * 获取上下文
	 * @return
	 */
	public Context getContext() {
		return this.mContext;
	}
	
	/**
	 * 添加数据
	 * @param uri
	 * @param values
	 * @return
	 */
	public Uri insert(Uri uri, ContentValues values) {
		return mContentResolver.insert(uri, values);
	}
	
	/**
	 * 查询数据
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return mContentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
	}
	

	/**
	 * 更新数据
	 * @param uri
	 * @param values
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public int update(Uri uri, ContentValues values, final String selection, String[] selectionArgs) {
		return mContentResolver.update(uri, values, selection, selectionArgs);
	}
	
	/**
	 * 删除数据
	 * @param uri
	 * @param where
	 * @param selectionArgs
	 * @return
	 */
	public int delete(Uri uri, String where, String[] selectionArgs) {
		return mContentResolver.delete(uri, where, selectionArgs);
	}
	
	/**
	 * 重新批量操作，支持事务处理
	 * @param operations
	 * @param authority
	 * @return
	 */
	public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations){
		ContentProviderResult[] results = null;
		
		try {
			results = mContentResolver.applyBatch(authority, operations);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
		
		return  results;
	}
}
