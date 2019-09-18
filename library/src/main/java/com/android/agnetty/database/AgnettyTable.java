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

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public abstract class AgnettyTable {
	protected String mTableName;
	protected int    mPathIndex;
	
	public AgnettyTable(AgnettyDBAdapter adapter) {
		this.mPathIndex = adapter.getCurPathIndex();
	}
	
	/**
	 * 创建表
	 * @param db
	 * @return
	 */
	public abstract boolean createTable(SQLiteDatabase db);
	
	/**
	 * 初始化表数据
	 * @param db
	 * @return
	 */
	public abstract boolean initTable(SQLiteDatabase db);
	
	/**
	 * 创建表索引
	 * @param db
	 * @return
	 */
	public abstract boolean createIndex(SQLiteDatabase db);
	
	/**
	 * 创建触发器
	 * @param db
	 * @return
	 */
	public abstract boolean createTrigger(SQLiteDatabase db);
	
	/**
	 * 删除表
	 * @param db
	 * @return
	 */
	public boolean dropTable(SQLiteDatabase db) {
		if (mTableName != null) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + mTableName);
			} catch (SQLException ex) {
				throw ex;
			}
		}
			
		return true;
	}
	
	/**
	 * 修改表
	 * @param db
	 * @return
	 */
	public boolean alertTable(SQLiteDatabase db) {
		return dropTable(db) 
				&& createTable(db) 
				&& createIndex(db) 
				&& createTrigger(db)
				&& initTable(db);
	}
}
