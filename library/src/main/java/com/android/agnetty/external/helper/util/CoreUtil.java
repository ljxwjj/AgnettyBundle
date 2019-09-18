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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.agnetty.external.helper.system.CoreCst;
import com.android.agnetty.utils.BaseUtil;
import com.android.agnetty.utils.DeviceUtil;
import com.android.agnetty.utils.StringUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-12-19
 * @desc   : 工具类
 */
public class CoreUtil {
	/**
	 * DES加密数据
	 * @param data
	 * @return
	 */
	public static String encode(String data) {
		DES des = new DES(CoreCst.DES_KEY);
		String encrptyData = data;
		try {
			encrptyData = des.encrypt(data);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return encrptyData;
	}
	
	/**
	 * DES解密数据
	 * @param data
	 * @return
	 */
	public static String decode(String data) {
		DES des = new DES(CoreCst.DES_KEY);
		String encrptyData = data;
		try {
			encrptyData = des.decrypt(encrptyData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return encrptyData;
	} 
	
	/**
	 * 发送短信
	 * @param context
	 * @param phone
	 * @param content
	 */
	public static void sendSms(Context context, String phone, String content) {
		SmsManager sms = SmsManager.getDefault();
		Intent intent = new Intent();
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		sms.sendTextMessage(phone, null, content, pIntent, null);
	}
	
	/**
	 * 获取手机设备唯一号，如果手机换卡，该设备号会改变
	 * @param context
	 * @return
	 */
	public static String getUUID(Context context) {
		String result = "";
		StringBuilder sb = new StringBuilder();
		//subscriberId & deviceId
		TelephonyManager tManager = (TelephonyManager) context
                		.getSystemService(Context.TELEPHONY_SERVICE);
		
		if(tManager != null) {
			sb.append(tManager.getSubscriberId());
			sb.append(tManager.getDeviceId());
		}
		
		//pseudo-unique ID
		sb.append(Build.MANUFACTURER + Build.MODEL);
		
		//android ID
		sb.append(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(sb.toString().getBytes());
			result = BaseUtil.bytesToHexString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 判断当前卡是否是非电信卡，如果获取不到SubscriberId，结果也是false
	 * @param context
	 * @return
	 */
	public static boolean isNotCTC(Context context) {
		String imsi = DeviceUtil.getImsi(context);
		
		if(StringUtil.isNotBlank(imsi)) {
	    	if(imsi.startsWith("46001") // 联通卡
	    			|| imsi.startsWith("46000") //移动卡
	    			|| imsi.startsWith("46002") //移动卡
	    			) {
	    		return true;
	    	}
		}
		
		return false;
	}
	
	/**
	 * 显示Toast
	 * @param context
	 * @param resId
	 */
	public static void showToast(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示Toast
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
