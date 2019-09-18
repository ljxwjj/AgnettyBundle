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

import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.utils.LogUtil;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 插件系统中Service组件的抽象基类
 */
public abstract class StubService extends Service{
	private AssetManager mAssetManager;
    private Resources mResources;
//  private Theme mTheme;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(newBase);
		
		try {
			//To get new AssetManager
			mAssetManager = (AssetManager) AssetManager.class.newInstance();
			mAssetManager.getClass().getMethod("addAssetPath", String.class)
                    .invoke(mAssetManager, StubClassLoader.getStubRes());
			
			//To get new Resources
            Resources superRes = super.getResources();
            mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
            
            //To get new Theme
//          mTheme = mResources.newTheme();
//          mTheme.setTo(super.getTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	
	@Override
	public void startActivity(Intent intent) {
		try {
			StubClassLoader.setStubInfo(intent);
			Intent stubIntent = new Intent();
			stubIntent.setClassName(getBaseContext(), CoreCst.CORE_ACTIVITY);
			super.startActivity(stubIntent);
		}catch(Exception ex) {
			super.startActivity(intent);
		}
	}
	
	@Override
	public ComponentName startService(Intent service) {
		try {
			StubClassLoader.setStubInfo(service);
			Intent stubIntent = new Intent();
			stubIntent.setClassName(getBaseContext(), CoreCst.CORE_SERVICE);
			return super.startService(service);
		}catch(Exception ex) {
			return super.startService(service);
		}
	}

	@Override
	public boolean stopService(Intent name) {
		try {
			StubClassLoader.setStubInfo(name);
			Intent stubIntent = new Intent();
			stubIntent.setClassName(getBaseContext(), CoreCst.CORE_SERVICE);
			return super.stopService(name);
		}catch(Exception ex) {
			return super.stopService(name);
		}
	}
	
	@Override
    public AssetManager getAssets() {	        
        return mAssetManager == null 
        		? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null 
        		? super.getResources() : mResources;
    }

//    @Override
//    public Theme getTheme() {
//        return mTheme == null 
//        		? super.getTheme() : mTheme;
//    }
}
