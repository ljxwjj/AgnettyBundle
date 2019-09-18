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

package com.android.agnetty.external.helper.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.agnetty.external.helper.pojo.User;
import com.android.agnetty.external.helper.system.CoreCst;
import com.android.agnetty.external.helper.system.CoreDBHelper;
import com.android.agnetty.external.helper.system.CoreDBHelper.UserColumns;
import com.android.agnetty.utils.LogUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 用户信息数据库业务逻辑
 */
public class UserDao{
	private CoreDBHelper mOpenHelper;

    public UserDao(Context context) {
    	mOpenHelper = new CoreDBHelper(context);
    }
    
    /**
     * 获取用户信息
     * @return
     */
    public User getUser() {
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		User user = null;
		Cursor cursor = null;
		
		try {
			cursor = db.query(CoreDBHelper.USER_TABLE, new String[]{
						UserColumns.PHONE,
						UserColumns.UUID
					}, null, null, null, null, null);
			
			if(cursor != null && cursor.moveToNext()) {
				user = new User(cursor.getString(0),
						cursor.getString(1));
			}
		} catch(Exception ex) {
			user = null;
			ex.printStackTrace();
		} finally {
			if(cursor != null) cursor.close();
		}
		
		db.close();
		return user;
    }
    
    
    /**
     * 每张手机卡都对应一个唯一的ID,通过该ID获取用户信息
     * @param uuID
     * @return
     */
    public User getUser(String uuID) {
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		User user = null;
		Cursor cursor = null;
	
		try {
			cursor = db.query(CoreDBHelper.USER_TABLE, new String[]{
						UserColumns.PHONE
					}, UserColumns.UUID +"=?", new String[]{uuID}, null, null, null);
			
			if(cursor != null && cursor.moveToNext()) {
				user = new User(cursor.getString(0),
						uuID);
			}
		} catch(Exception ex) {
			user = null;
			ex.printStackTrace();
		} finally {
			if(cursor != null) cursor.close();
		}
		
		db.close();
		return user;
    }
    
    /**
     * 保存用户信息，用户信息表只能保存当前最新的用户信息，
     * 如果以前已经有用户信息，则删除老信息
     * @param user
     */
    public void addUser(User user) {
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
    	User oldUser = getUser();
    	if(oldUser != null) {
    		db.delete(CoreDBHelper.USER_TABLE, 
    				UserColumns.PHONE + "=?", 
    				new String[]{oldUser.getPhone()});
    	}
    	
		ContentValues values = new ContentValues();
		values.put(UserColumns.PHONE,    		user.getPhone());
		values.put(UserColumns.UUID,    		user.getUUID());
		db.insert(CoreDBHelper.USER_TABLE, null, values);
		
		db.close();
    }
}
