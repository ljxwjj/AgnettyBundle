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

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.UriMatcher;
import android.util.SparseArray;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class AgnettyDBAdapter {
	private AtomicInteger mPathIndex = new AtomicInteger(0);
	private LinkedList<AgnettyTable> mTables  = new LinkedList<AgnettyTable>();
	private SparseArray<AgnettyTable> mCode2Tables   = new SparseArray<AgnettyTable>();
	private final UriMatcher mUriMatcher;
    
	private String mAuthority; //authority
	private String mDBName;    //数据库名称
	private int mDBVersion; //数据库版本
	
	public AgnettyDBAdapter(UriMatcher uriMatcher) {
		this.mUriMatcher = uriMatcher;
	}
	
	public AgnettyDBAdapter addTable(AgnettyTable table) {
		if (table != null) {
			mTables.add(table);
			
			if (table.mTableName != null) { 
				mCode2Tables.append(mPathIndex.get(), table);
				mUriMatcher.addURI(mAuthority, table.mTableName,      mPathIndex.getAndIncrement());
				mCode2Tables.append(mPathIndex.get(), table);
				mUriMatcher.addURI(mAuthority, table.mTableName+"/#", mPathIndex.getAndIncrement());
			} else {
				throw new IllegalArgumentException("path is NULL");
			}
		} else {
			throw new IllegalArgumentException("table is NULL");
		}
		
		return this;
	}
	
	public AgnettyTable getTable(Integer code) {
		return mCode2Tables.get(code);
	}
	
	public int getCurPathIndex() {
		return mPathIndex.get();
	}
	
	public Collection<AgnettyTable> getTables() {
		return mTables;
	}
	
	public void setAuthority(String authority) {
		this.mAuthority = authority;
	}
	
	public String getAuthority() {
		return this.mAuthority;
	}
	
	public void setDBName(String name) {
		this.mDBName = name;
	}
	
	public String getDBName() {
		return this.mDBName;
	}
	
	public void setDBVersion(int version) {
		this.mDBVersion = version;
	}
	
	public int getDBVersion() {
		return this.mDBVersion;
	}
}
