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

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.external.helper.pojo.ApkInfo;
import com.android.agnetty.external.helper.pojo.PluginInfo;
import com.android.agnetty.external.helper.system.CoreCst;
import com.android.agnetty.external.helper.system.CoreDBHelper;
import com.android.agnetty.external.helper.system.CoreDBHelper.PluginColumns;
import com.android.agnetty.utils.ImageUtil;
import com.android.agnetty.utils.LogUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 插件包数据库业务逻辑
 */
public class PluginDao{
	private CoreDBHelper mOpenHelper;

    public PluginDao(Context context) {
    	mOpenHelper = new CoreDBHelper(context);
    }
    
	/**
	 * 插件表中是否有首次加载的插件信息
	 * @return
	 */
	public boolean hasPluginInfo() {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		boolean flag = false;
		Cursor cursor = null;
		
		try {
			cursor = db.query(CoreDBHelper.PLUGIN_TABLE, 
					null, null, null, null, null, null);
			if(cursor != null) flag = cursor.getCount()>0;
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(cursor != null) cursor.close();
		}
		
		db.close();
		return flag;
	}
	
	/**
	 * 根据序列号获取当前所有的插件系列
	 * @param serialID
	 * @return
	 */
	public ArrayList<PluginInfo> getPluginInfos(int serialID) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		ArrayList<PluginInfo> infos = null;
		Cursor cursor = null;
		
		try {
			cursor = db.query(CoreDBHelper.PLUGIN_TABLE, new String[]{
					PluginColumns.TYPE,
					PluginColumns.APK_URL,
					PluginColumns.APK_PATH,
					PluginColumns.MD5,
					PluginColumns.ENABLED,
					PluginColumns.NAME,
					PluginColumns.PACKAGE_NAME,
					PluginColumns.DESC,
					PluginColumns.ICON,
					PluginColumns.STUB_CLASS
				}, PluginColumns.SERIAL_ID+"=?", new String[]{String.valueOf(serialID)}, null, null, null);
			
			if(cursor != null) {
				infos = new ArrayList<PluginInfo>(cursor.getCount());
				while(cursor.moveToNext()) {
					PluginInfo info = new PluginInfo(serialID, 
							cursor.getInt(0), 
							cursor.getString(1),
							cursor.getString(2), 
							cursor.getString(3),
							cursor.getInt(4)==1, 
							new ApkInfo(cursor.getString(5), 
										cursor.getString(6),
										cursor.getString(7),
										ImageUtil.byteToBitmap(cursor.getBlob(8)),
										cursor.getString(9)));
					infos.add(info);
				}
			}
		} catch(Exception ex) {
			infos = null;
			ex.printStackTrace();
		} finally {
			if(cursor != null) cursor.close();
		}
		
		db.close();
		return infos;
	}
	

	/**
	 * 添加新版本插件系列到插件表,如果以前的插件表存在相同的序列号，则将被删除
	 * @param infos
	 */
	public void addPluginInfos(ArrayList<PluginInfo> infos) {
		if(infos != null && infos.size() != 0) {
			SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			
			db.delete(CoreDBHelper.PLUGIN_TABLE, 
					  PluginColumns.SERIAL_ID+"=?", 
					  new String[]{String.valueOf(infos.get(0).getSericalID())});
			
			for(PluginInfo info : infos) {
				ContentValues values = new ContentValues();
				values.put(PluginColumns.SERIAL_ID,    info.getSericalID());
				values.put(PluginColumns.TYPE, 	 	   info.getType());
				values.put(PluginColumns.APK_URL, 	   info.getApkUrl());
				values.put(PluginColumns.APK_PATH, 	   info.getApkPath());
				values.put(PluginColumns.MD5, 		   info.getMD5());
				values.put(PluginColumns.ENABLED,      info.getEnabled());
				values.put(PluginColumns.NAME, 		   info.getApkInfo().getName());
				values.put(PluginColumns.PACKAGE_NAME, info.getApkInfo().getPackageName());
				values.put(PluginColumns.DESC, 		   info.getApkInfo().getDesc());
				values.put(PluginColumns.ICON, 	 	   ImageUtil.bitmapToByte(info.getApkInfo().getIcon()));
				values.put(PluginColumns.STUB_CLASS,   info.getApkInfo().getStubClass());
				db.insert(CoreDBHelper.PLUGIN_TABLE, null, values);
			}
			
			db.close();
		}
	}
}
