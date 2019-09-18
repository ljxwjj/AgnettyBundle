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

import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.agnetty.external.helper.pojo.ApkInfo;
import com.android.agnetty.utils.FileUtil;
import com.android.agnetty.utils.ImageUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 解析apk包的信息
 */
public class ApkParser {
	
	/**
	 * 解析指定apk包中的manifest，获取清单其中的:
	 * 					包名-->package="?"
	 * 					应用名-->android:name="@string/name"
	 * 					描述-->android:description="@string/desc"
	 * 					图标-->android:icon="@drawable/icon"
	 * @param apkPath  apk包路径
	 * @return
	 */
	public static ApkInfo parse(String apkPath) {
		ApkInfo apkInfo = new ApkInfo();
		
		if(!FileUtil.isFileExist(apkPath)) return apkInfo;
		
		try {
			//PackageParser parser = new PackageParser(String apkPath)
			Class pkgParserCls = Class.forName("android.content.pm.PackageParser");
			Constructor pkgParserCt = pkgParserCls.getConstructor(new Class[]{String.class});
			Object pkgParserObj = pkgParserCt.newInstance(new Object[]{apkPath});
			
			//parser.parsePackage(File file, String path, DisplayMetrics metrics, Configuration config)
			Method parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
					new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE});	
			parsePackageMtd.setAccessible(true);
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			
			Object parsePackageObj = parsePackageMtd.invoke(pkgParserObj, 
					new Object[]{new File(apkPath), apkPath, metrics, 0});
			
			//ApplicationInfo info = PackageParser.applicationInfo
			Field appInfoFld = parsePackageObj.getClass().getDeclaredField("applicationInfo");
			appInfoFld.setAccessible(true);
			ApplicationInfo info = (ApplicationInfo) appInfoFld.get(parsePackageObj);
		
			// AssetManager assetManagerObj = new AssetManager();
			// assetManagerObj.addAssetPath(apkPath);
			// Resources res = new Resources(assetManagerObj, null, null);
			AssetManager assetManagerObj = AssetManager.class
					.getConstructor().newInstance();
            Method assetManagerMtd = assetManagerObj.getClass()
            		.getDeclaredMethod("addAssetPath", new Class[]{String.class});
            assetManagerMtd.setAccessible(true);
            assetManagerMtd.invoke(assetManagerObj, apkPath);
           
            Resources res = new Resources(assetManagerObj, null, null);
            
            //apk info
            //package name
            apkInfo.mPackageName = info.packageName;
            
            //name
			if (info.labelRes != 0) {
				apkInfo.mName = res.getText(info.labelRes).toString();
			}
			
			//desc
			if(info.descriptionRes != 0) {
				apkInfo.mDesc = res.getText(info.descriptionRes).toString();
			}
			
			//icon
			if (info.icon != 0) {
				apkInfo.mIcon = ImageUtil.drawableToBitmap(res.getDrawable(info.icon));
			}
			
			//stub class
			Properties property = new Properties();
            property.load(assetManagerObj.open("plugin.properties"));
            apkInfo.mStubClass = property.getProperty("Bundle-StubClass");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return apkInfo;
	}
}
