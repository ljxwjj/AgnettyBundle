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


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.external.helper.pojo.PluginInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;


/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   :
 */
public class StubClassLoader extends ClassLoader {
	//宿主的classloader--Launcher Bundle
	private static ClassLoader mDefaultLoader;
	
	//插件classloader集
	private static Map<String, ClassLoader> mClassLoaders = new LinkedHashMap<String, ClassLoader>();
	
	//插件集
	private static Map<String, PluginInfo> mPluginInfos = new LinkedHashMap<String, PluginInfo>();
	
	//
	private static String mStubPackageName;
	//
	private static String mStubClass;
	//
	private static String mStubRes;
	
	public StubClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	/**
	 * 
	 * @param classLoader
	 */
	public static void setDefaultLoader(ClassLoader classLoader) {
		mDefaultLoader = classLoader;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultLoader() {
		return mDefaultLoader;
	}
	
	/**
	 * 
	 * @param packageName
	 * @param classLoader
	 */
	public static void addClassLoader(String packageName, ClassLoader classLoader) {
		mClassLoaders.put(packageName, classLoader);
	}
	
	/**
	 * 
	 * @param packageName
	 * @return
	 */
	public static ClassLoader getClassLoader(String packageName) {
		return mClassLoaders.get(packageName);
	}
	
	/**
	 * 
	 * @param packageName
	 * @param info
	 */
	public static void addPluginInfo(String packageName, PluginInfo info) {
		mPluginInfos.put(packageName, info);
	}
	
	/**
	 * 
	 * @param packageName
	 * @return
	 */
	public static PluginInfo getPluginInfo(String packageName) {
		return mPluginInfos.get(packageName);
	}
	
	/**
	 * 
	 * @param pluginType
	 * @return
	 */
	public static ArrayList<PluginInfo> getPluginInfos(int pluginType) {
		ArrayList<PluginInfo> infos = new ArrayList<PluginInfo>();
		
		for(PluginInfo info : mPluginInfos.values()) {
			if(info.getType() == pluginType) {
				infos.add(info);
			}
		}
		
		return infos;
	}
	
	/**
	 * 
	 * @param intent
	 */
	public static void setStubInfo(Intent intent) {
		ComponentName component = intent.getComponent();
		StubClassLoader.mStubPackageName = component.getPackageName();
		StubClassLoader.mStubClass = component.getClassName();
		StubClassLoader.mStubRes = mPluginInfos.get(StubClassLoader.mStubPackageName).getApkPath();
	}
	
	/**
	 * 
	 * @param pluginType
	 */
	public static void setStubInfo(int pluginType) {
		for(PluginInfo info : mPluginInfos.values()) {
			if(info.getType() == pluginType) {
				StubClassLoader.mStubClass = info.getApkInfo().mStubClass;
				StubClassLoader.mStubRes = info.getApkPath();
				StubClassLoader.mStubPackageName = info.getApkInfo().mPackageName;
				break;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getStubRes() {
		return mStubRes;
	}
	
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		ClassLoader classLoader = mClassLoaders.get(mStubPackageName);
		
		if(classLoader != null) {
			try {
				Class<?> clazz = null;
				if(className.equals(CoreCst.CORE_SERVICE)
						|| className.equals(CoreCst.CORE_ACTIVITY)) {
					clazz = classLoader.loadClass(mStubClass);
				} else {
					clazz = classLoader.loadClass(className);
				}
				if (clazz != null) return clazz;
			} catch (Exception ex) {
				
			}
		}
		
		return super.loadClass(className);
	}

}
