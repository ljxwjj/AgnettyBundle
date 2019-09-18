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

package com.android.agnetty.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import com.android.agnetty.pojo.ApnType;
import com.android.agnetty.pojo.NetworkType;
import com.android.agnetty.pojo.OperatorType;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 网络相关工具类
 */
public class NetworkUtil {
	private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	
	/**
	 * 获取可用的网络信息 
	 * Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static NetworkInfo getActiveNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
	/**
	 * 判断是否有网络可用
	 *  Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static boolean isNetAvailable(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		return networkInfo != null ? networkInfo.isAvailable() : false;
	}
	
	
	/**
	 * 获取网络接入类型, 请查看ApnType定义
	 * Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static ApnType getApnType(Context context) {
		ApnType apnType = ApnType.APN_UNKNOWN;
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		
		if(networkInfo != null 
				&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			//apnType = ApnType.valueof(networkInfo.getExtraInfo());
			
			if(apnType == ApnType.APN_UNKNOWN) {
				Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI, null,
						null, null, null);
				try {
					if (cursor != null && cursor.moveToFirst()) {
						apnType = ApnType.valueof(cursor.getString(cursor.getColumnIndex("user")));
					}
				} catch(Exception ex) {
					
				} finally {
					if(cursor != null) cursor.close();
				}
			}
		}
		
		return apnType;
	}
	
	/**
	 * 获取网络运营商类型, 请查看OperatorType定义
	 * Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static OperatorType getOperatorType(Context context) {
		OperatorType operatorType = OperatorType.OPERATOR_UNKNOWN;
		
		switch(getApnType(context)) {
			case APN_CTNET:
			case APN_CTWAP:
				operatorType = OperatorType.OPERATOR_CMCC;
				break;
				
			case APN_CMNET:
			case APN_CMWAP:
				operatorType = OperatorType.OPERATOR_CTC;
				break;
				
			case APN_UNIWAP:
			case APN_3GWAP:
			case APN_UNINET:
			case APN_3GNET:
				operatorType = OperatorType.OPERATOR_CUC;
				break;
				
			default:
				break;
		}
		
		return operatorType;
	}
	
	
	/**
	 * 获取当前的网络类型 2G、3G、4G、WIFI、UNKNOWN, 请查看NetworkType定义
	 * Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static NetworkType getNetworkType(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context); 
		NetworkType networkType = NetworkType.NETWORK_UNKNOWN;
		
		if(networkInfo != null) {
			int type = networkInfo.getType();
			if (type == ConnectivityManager.TYPE_WIFI) {   
				networkType = NetworkType.NETWORK_WIFI;
			} else if(type == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				switch (telephonyManager.getNetworkType()) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
			        	networkType = NetworkType.NETWORK_2G; // ~ 100 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_EDGE:
			        	networkType = NetworkType.NETWORK_2G; // ~ 50-100 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_UMTS:
			        	networkType = NetworkType.NETWORK_3G; // ~ 400-7000 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_CDMA:
			        	networkType = NetworkType.NETWORK_2G; // ~ 14-64 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_EVDO_0:
			        	networkType = NetworkType.NETWORK_3G; // ~ 400-1000 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_EVDO_A:
			        	networkType = NetworkType.NETWORK_3G; // ~ 600-1400 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_1xRTT:
			        	networkType = NetworkType.NETWORK_2G; // ~ 50-100 kbps
			        	break;
			        case TelephonyManager.NETWORK_TYPE_HSDPA:
			        	networkType = NetworkType.NETWORK_3G; // ~ 2-14 Mbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_HSUPA:
			        	networkType = NetworkType.NETWORK_3G; // ~ 1-23 Mbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_HSPA:
			        	networkType = NetworkType.NETWORK_3G; // ~ 700-1700 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_IDEN:
			        	networkType = NetworkType.NETWORK_2G; // ~25 kbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_EVDO_B:
			        	networkType = NetworkType.NETWORK_3G; // ~ 5 Mbps
			            break;
			        case 13://TelephonyManager.NETWORK_TYPE_LTE
			        	networkType = NetworkType.NETWORK_4G; // ~ 10+ Mbps
			            break;
			        case 14://TelephonyManager.NETWORK_TYPE_EHRPD
			        	networkType = NetworkType.NETWORK_3G; // ~ 1-2 Mbps
			            break;   
			        case 15://TelephonyManager.NETWORK_TYPE_HSPAP
			        	networkType = NetworkType.NETWORK_3G; // ~ 10-20 Mbps
			            break;
			        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			        default:
			        	networkType = NetworkType.NETWORK_2G;
			        	break;
				}
			}
		} 
  
       return networkType;  
	}
	
	
	/**
	 * 获取当前网络的代理，断网或wifi下返回为NULL
	 * Permission:android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static Proxy getNetworkProxy(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if(networkInfo == null) return null;
		
		int type = networkInfo.getType();
		if (type != ConnectivityManager.TYPE_WIFI) {  
			String host = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			
			if(StringUtil.isNotEmpty(host) && port != -1) {
				return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
			} 
		}
		
		return null;
	}
}
