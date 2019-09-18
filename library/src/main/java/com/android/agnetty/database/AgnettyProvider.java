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
import java.util.Collection;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 
 */
public abstract class AgnettyProvider extends ContentProvider{
	private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	   
	private DatabaseHelper mDBHelper;  
	private AgnettyDBAdapter mAdapter;
	
	public abstract void configDatabase(AgnettyDBAdapter adapter);
	
	public abstract void configTable(AgnettyDBAdapter adapter);
	
	private void configProvider(AgnettyDBAdapter adapter) {
		configDatabase(adapter);
		configTable(adapter);
	}
	
	@Override
	public boolean onCreate() {
		mAdapter = new AgnettyDBAdapter(mUriMatcher);
		configProvider(mAdapter); 
		mDBHelper = new DatabaseHelper(getContext(), mAdapter.getDBName(), mAdapter.getDBVersion());  
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		int code = mUriMatcher.match(uri);
		
		if (code != UriMatcher.NO_MATCH) {
			AgnettyTable table = mAdapter.getTable(code);
			if(code % 2 == 0) {
				return db.query(table.mTableName, projection, selection, selectionArgs,  
					                null, null, sortOrder); 
			} else {
				long id = ContentUris.parseId(uri);
				StringBuilder where = new StringBuilder(); 
				where.append("_id=").append(id).append(" AND ");
				
				if (!TextUtils.isEmpty(selection)) where.append(selection);
				
				return db.query(table.mTableName, projection, where.toString(), selectionArgs, 
									null, null, sortOrder);  
			}
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		int code = mUriMatcher.match(uri);
		
		if (code != UriMatcher.NO_MATCH) {
			AgnettyTable table = mAdapter.getTable(code);
			if(code % 2 == 0) {
				return db.delete(table.mTableName, selection, selectionArgs);
			} else {
				long id = ContentUris.parseId(uri);
				StringBuilder where = new StringBuilder(); 
				where.append("_id=").append(id).append(" AND ");
				
				if (!TextUtils.isEmpty(selection)) where.append(selection);
				
				return db.delete(table.mTableName, where.toString(), selectionArgs);  
			}
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

	}

	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		int code = mUriMatcher.match(uri);
		
		if (code != UriMatcher.NO_MATCH) {
			AgnettyTable table = mAdapter.getTable(code);
			if(code % 2 == 0) {
				long rowid = db.insert(table.mTableName, null, values);  
				return ContentUris.withAppendedId(uri, rowid);  
			} else {
				throw new IllegalArgumentException("Unknown URI: " + uri);
			}
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		int code = mUriMatcher.match(uri);
		
		if (code != UriMatcher.NO_MATCH) {
			AgnettyTable table = mAdapter.getTable(code);
			if(code % 2 == 0) {
				return db.update(table.mTableName, values, selection, selectionArgs);  
			} else {
				long id = ContentUris.parseId(uri);
				StringBuilder where = new StringBuilder(); 
				where.append("_id=").append(id).append(" AND ");
				
				if (!TextUtils.isEmpty(selection)) where.append(selection);
				
				return  db.update(table.mTableName, values, where.toString(), selectionArgs);  
			}
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

	}
	
	@Override
	public String getType(Uri uri) {
		int code = mUriMatcher.match(uri);
		
		if (code != UriMatcher.NO_MATCH) {
			AgnettyTable table = mAdapter.getTable(code);
			if(code % 2 == 0) {
				return "vnd.android.cursor.dir/vnd.agnetty."+table.mTableName;
			} else {
				return "vnd.android.cursor.item/vnd.agnetty."+table.mTableName;
			}
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	/**
	 * 重新批量操作，支持事务处理
	 */
	@Override
	public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
			throws OperationApplicationException {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		
        try{ 
        	//开始事务
            db.beginTransaction(); 
            ContentProviderResult[] results = super.applyBatch(operations); 
            //设置事务标记为成功 
            db.setTransactionSuccessful();
            return results; 
        } finally { 
        	//结束事务 
            db.endTransaction();
        }
	}
	
	
	/**
	 * 
	 * @author Zhenshui.Xia
	 * 
	 */
	private class DatabaseHelper extends SQLiteOpenHelper {  
		DatabaseHelper(Context context, String name, int version) {            
			super(context, name, null, version);        
		}        
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Collection<AgnettyTable> tables = mAdapter.getTables();
			
			if (tables != null) {
				for (AgnettyTable table : tables) {
					table.createTable(db);   //创建表
					table.createIndex(db);   //创建索引
					table.createTrigger(db); //创建触发器
					table.initTable(db);     //初始化表数据
				}
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Collection<AgnettyTable> tables = mAdapter.getTables();
			
			if (tables != null) {
				for (AgnettyTable table : tables) {
					table.alertTable(db);  //修改表
				}
			}
		}   
	}

}
