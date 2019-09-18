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

package com.android.agnetty.external.helper.system;


import com.android.agnetty.utils.LogUtil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * 
 * @author Zhenshui.Xia
 *
 */
public class CoreDBHelper extends SQLiteOpenHelper {

    public static final String CORE_DB    = "core.db";
    
    public static final int CORE_VERSION  = 1;
    
    //插件数据表
    public static final String PLUGIN_TABLE = "pluginTable";
    
    //插件数据表
    public static final String USER_TABLE 	= "userTable";
    
    public CoreDBHelper(Context context) {
        super(context, CORE_DB, null, CORE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	createUserTable(db);
    	createPluginTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	updateUserTable(db);
        updatePluginTable(db);
        onCreate(db);
    }
    
    /**
     * 
     * @param db
     */
    private void createPluginTable(SQLiteDatabase db) {
    	StringBuilder sb = new StringBuilder()
			.append("CREATE TABLE IF NOT EXISTS ").append(PLUGIN_TABLE)
			.append("(")
			.append(PluginColumns._ID )             .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
			.append(PluginColumns.SERIAL_ID )       .append(" INTEGER,")
			.append(PluginColumns.TYPE )         	.append(" INTEGER,")
			.append(PluginColumns.APK_URL )         .append(" VARCHAR(100),")
			.append(PluginColumns.APK_PATH )        .append(" VARCHAR(100),")
			.append(PluginColumns.MD5 )             .append(" VARCHAR(50),")
			.append(PluginColumns.ENABLED )         .append(" INTEGER,")
			.append(PluginColumns.NAME )            .append(" VARCHAR(50),")
			.append(PluginColumns.PACKAGE_NAME )    .append(" VARCHAR(50),")
			.append(PluginColumns.DESC )            .append(" VARCHAR(100),")
			.append(PluginColumns.ICON )        	.append(" BLOB,")
			.append(PluginColumns.STUB_CLASS )      .append(" VARCHAR(50)")
			.append(");");
    	LogUtil.i(sb.toString());
    	db.execSQL(sb.toString());
    }
    
    /**
     * 
     * @param db
     */
    private void updatePluginTable(SQLiteDatabase db) {
    	StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS " + PLUGIN_TABLE);
    	LogUtil.i(sb.toString());
    	db.execSQL(sb.toString());
    }
    
    
    /**
     * 
     * @param db
     */
    private void createUserTable(SQLiteDatabase db) {
    	StringBuilder sb = new StringBuilder()
			.append("CREATE TABLE IF NOT EXISTS ").append(USER_TABLE)
			.append("(")
			.append(UserColumns._ID )             .append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
			.append(UserColumns.PHONE )       	  .append(" VARCHAR(20),")
			.append(UserColumns.UUID )       	  .append(" VARCHAR(100)")
			.append(");");
    	LogUtil.i(sb.toString());
    	db.execSQL(sb.toString());
    }
    
    /**
     * 
     * @param db
     */
    private void updateUserTable(SQLiteDatabase db) {
    	StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS " + USER_TABLE);
    	LogUtil.i(sb.toString());
    	db.execSQL(sb.toString());
    }

    /**
     * 插件表字段
     * @author Zhenshui.Xia
     *
     */
    public static final class PluginColumns implements BaseColumns {
		//所属插件序列号
		public static final String SERIAL_ID    = "_sericalID";		
		//插件包类型
		public static final String TYPE  		= "_type"; 		
		//插件包url
		public static final String APK_URL  	= "_apkUrl"; 		
		//插件包存储路径
		public static final String APK_PATH 	= "_apkPath"; 		
		//当前插件包校验值（md5）
		public static final String MD5  		= "_md5"; 		
		//当前插件是否可用
		public static final String ENABLED  	= "_enabled"; 		
		//插件功能名
		public static final String NAME  		= "_name"; 	
		//插件包名
		public static final String PACKAGE_NAME = "_packageName"; 
		//插件功能图标url
		public static final String ICON  		= "_icon"; 		
		//插件功能描述
		public static final String DESC  		= "_desc"; 		
		//插件启动类名
		public static final String STUB_CLASS  	= "_stubClass"; 
	}
    
    /**
     * 用户表字段
     * @author Zhenshui.Xia
     *
     */
    public static final class UserColumns implements BaseColumns {
		//用户手机号码
		public static final String PHONE    = "_phone";	
		
		//该号码对 应的唯一设备号
		public static final String UUID		= "_uuID";
	}
}