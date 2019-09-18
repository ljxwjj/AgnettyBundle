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

package com.android.agnetty.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.android.agnetty.constant.AgnettyCst;
import com.android.agnetty.external.helper.system.CoreCst;
import com.android.agnetty.utils.LogUtil;
import com.android.agnetty.utils.StringUtil;
import com.android.agnetty.utils.ThreadPoolUtil;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 任务管理器, 单例，确保首次getInstance()实例化是在UI线程
 */
public class AgnettyManager {
	private static AgnettyManager mInstance;
		
	private Context mContext;
	
	//单例线程池
	private ExecutorService mSingleExecutor;  
	//缓存线程池
	private ExecutorService mCachedExecutor; 
	//固定线程池，默认创建个数为手机处理器个数
	private ExecutorService mFixedExecutor;   
	//定时线程池
	private ScheduledExecutorService mScheduleExecutor;  
	
	//UI线程任务监听事件处理器(确保它在UI线程下初始化)
	private Handler mUIHandler;   
	//当前正在执行的所有ID-future
	private ConcurrentHashMap<String, AgnettyFuture> mIDToFutures;  
	private ConcurrentHashMap<String, ScheduledFuture> mIDToSchedules;  
	//当前正在执行的所有tag-future
	private ConcurrentHashMap<Object, AgnettyFuture> mTagToFutures;	 
	//广播接收器，接收AlarmManagr发送的广播，处理所有延时、重复任务
//	private AlarmReceiver mAlarmReceiver; 
//	private AlarmManager mAlarmManager;
	
	private AgnettyManager(Context context) {
		mContext = context;
		
		mSingleExecutor = Executors.newSingleThreadExecutor();
		mCachedExecutor =  Executors.newCachedThreadPool();
		mFixedExecutor = Executors.newFixedThreadPool(ThreadPoolUtil.DEFAULT_THREAD_POOL_SIZE);
		mScheduleExecutor = Executors.newScheduledThreadPool(ThreadPoolUtil.DEFAULT_THREAD_POOL_SIZE);
		
		mUIHandler = new UIHandler();
		mIDToFutures = new ConcurrentHashMap<String, AgnettyFuture>();
		mIDToSchedules = new ConcurrentHashMap<String, ScheduledFuture>();
		mTagToFutures = new ConcurrentHashMap<Object, AgnettyFuture>();
		
//		mAlarmReceiver = new AlarmReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(AgnettyCst.ALARM_ACTION);
//		context.getApplicationContext().registerReceiver(mAlarmReceiver, filter);
//		
//		mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
	}
	
	/**
	 * 实例化任务管理器
	 * @param context
	 * @return
	 */
	public synchronized static AgnettyManager getInstance(Context context) {
		if(mInstance == null) {
			mInstance = new AgnettyManager(context);
		}
		
		return mInstance;
	}
	
	
	
	/**
	 * 关闭任务管理器，清除数据
	 */
	public void release() {
		if(mInstance != null) {
//			mContext.getApplicationContext().unregisterReceiver(mAlarmReceiver);
//			for(AgnettyFuture future : mIDToFutures.values()) {
//				if(future.mFutureIntent != null) mAlarmManager.cancel(future.mFutureIntent);
//			}
			
			mIDToFutures.clear();
			mIDToSchedules.clear();
			mTagToFutures.clear();
			mCachedExecutor.shutdown();
			mFixedExecutor.shutdown();
			mSingleExecutor.shutdown();
			mScheduleExecutor.shutdown();
			mInstance = null;
		}
	}
	
	/**
	 * 处理非UI线程的任务回调事件
	 * @param futureID
	 * @param result
	 * @param status
	 */
	public void handleEvt(String futureID, AgnettyResult result, boolean cancelScheduled, int status) {
		AgnettyFuture future = mIDToFutures.get(futureID);
		if(future == null) return;
		
		switch(status) {
			case AgnettyStatus.START: //任务开始
				if(future.mListener != null) future.mListener.onStart(result);
				break;
				
			case AgnettyStatus.PROGRESSING: //任务进度更新
				if(future.mListener != null)future.mListener.onProgress(result);
				break;
				
			case AgnettyStatus.COMPLETED:   //任务完成
				if(future.mListener != null)future.mListener.onComplete(result);
				if(!future.isScheduleFuture()) {
					cancelFutureByID(futureID);
				} else {
					if(cancelScheduled) {
						cancelFutureByID(futureID);
					}
				}
				break;
				
			case AgnettyStatus.EXCEPTION:   //任务发生异常
				if(future.mListener != null)future.mListener.onException(result);
				if(!future.isScheduleFuture()) {
					cancelFutureByID(futureID);
				} else {
					if(cancelScheduled) {
						cancelFutureByID(futureID);
					}
				}
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * 分发处理器事件给UI线程监听器执行
	 * @param futureID
	 * @param result   
	 * @param status
	 */
	public void handleUIEvt(String futureID, AgnettyResult result, boolean cancelScheduled, int status) {
		AgnettyFuture future = mIDToFutures.get(futureID);
		if(future == null) return;
		
		Message msg = Message.obtain();
		msg.what = status;
		msg.obj = result;  
		Bundle data = new Bundle();
		data.putString(AgnettyCst.FUTURE_ID, futureID);
		data.putBoolean(AgnettyCst.CANCEL_SCHEDULED, cancelScheduled);
		msg.setData(data);
		mUIHandler.sendMessage(msg);
	}
	
	/**
	 * 通过单例线程池执行任务
	 * @param future
	 */
	public void execSingleFuture(AgnettyFuture future) {
		if(future == null || mSingleExecutor.isShutdown()) return;
		mSingleExecutor.submit(future); 
	}
	
	/**
	 * 通过缓存池执行任务
	 * @param future
	 */
	public void execCachedFuture(AgnettyFuture future) {
		if(future == null || mCachedExecutor.isShutdown()) return;
		mCachedExecutor.submit(future);
	}
	
	/**
	 * 通过固定线程池执行任务
	 * @param future
	 */
	public void execFixedFuture(AgnettyFuture future) {
		if(future == null || mFixedExecutor.isShutdown()) return;
		mFixedExecutor.submit(future);
	}
	
	
	/**
	 * 通过定时线程池执行定时任务
	 * @param future
	 */
	public void execScheduleFuture(final AgnettyFuture future) {
		if(future == null || mScheduleExecutor.isShutdown()) return;
		
		
		ScheduledFuture f = mScheduleExecutor.scheduleAtFixedRate(
				new Runnable() {

					@Override
					public void run() {
						if(future.mScheduleTimes>-1 
								&& future.mScheduleTimes--<=0) {
							future.mFutureManager.cancelFutureByID(future.mFutureID);
							return;
						}
						
						future.run();
					}
					
				}, 
				future.mScheduleTrigger, 
				future.mScheduleInterval, 
				TimeUnit.MILLISECONDS);
		mIDToSchedules.put(future.mFutureID, f);
	}
	
	/**
	 * 通过定时线程池执行延时任务
	 * @param future
	 */
	public void execDelayFuture(AgnettyFuture future) {
		if(future == null || mScheduleExecutor.isShutdown()) return;
		ScheduledFuture f = mScheduleExecutor.schedule(future, future.mDelayTime, TimeUnit.MILLISECONDS);
		mIDToSchedules.put(future.mFutureID, f);
	}
	
	/**
	 * 向任务管理器添加执行的任务
	 * @param future
	 */
	public void addFuture(AgnettyFuture future) {
		if(future == null) return;
		//添加任务
		mIDToFutures.put(future.mFutureID, future);
		//通过tag找到指定的future，see：getFutureByTag(tag)
		if(future.mTag != null) mTagToFutures.put(future.mTag, future);
	}
	
	/**
	 * 通过任务标记找到正在执行的任务
	 * @param tag
	 * @return
	 */
	public AgnettyFuture getFutureByTag(Object tag) {
		return tag==null? null : mTagToFutures.get(tag);
	}
	
	/**
	 * 通过任务ID找到正在执行的任务
	 * @param futureID
	 * @return
	 */
	public AgnettyFuture getFutureByID(String futureID) {
		return mIDToFutures.get(futureID);
	}
	
	/**
	 * 根据任务标记取消当前正在执行的任务
	 * @param tag
	 */
	public void cancelFutureByTag(Object tag) {
		AgnettyFuture future = mTagToFutures.get(tag);
		
		if(future != null) {
			mTagToFutures.remove(tag);
			mIDToFutures.remove(future.mFutureID);
//			if(future.mFutureIntent != null) mAlarmManager.cancel(future.mFutureIntent);
			ScheduledFuture f = mIDToSchedules.get(future.mFutureID);
			if(f != null) {
				mIDToSchedules.remove(future.mFutureID);
				f.cancel(true);
			}
		}
	}
	
	/**
	 * 根据任务ID取消当前正在执行的任务
	 * @param futureID
	 */
	public void cancelFutureByID(String futureID) {
		AgnettyFuture future = mIDToFutures.get(futureID);
		
		if(future != null )  {
			mIDToFutures.remove(futureID);
			
//			if(future.mFutureIntent != null) mAlarmManager.cancel(future.mFutureIntent);
			ScheduledFuture f = mIDToSchedules.get(future.mFutureID);
			if(f != null) {
				mIDToSchedules.remove(future.mFutureID);
				f.cancel(true);
			}
			
			Object tag = future.getTag();
			if(tag != null) mTagToFutures.remove(tag);
		}
	}
	
	/**
	 * 延时、重复任务接收器
	 * @author Zhenshui.Xia
	 *
	 */
//    public class AlarmReceiver extends BroadcastReceiver{
//    	
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			AgnettyManager manager = AgnettyManager.getInstance(mContext);
//			String futureID = intent.getStringExtra(AgnettyCst.FUTURE_ID);
//			if(StringUtil.isEmpty(futureID)) return;
//			int pool = intent.getIntExtra(AgnettyCst.FUTURE_POOL, AgnettyFuture.CACHED_POOL);
//			AgnettyFuture future = manager.getFutureByID(futureID);
//			
//			if(future == null) {
//				int hashcode = intent.getIntExtra(AgnettyCst.FUTURE_HASHCODE, 0);
//				Intent cancelIntent = new Intent(AgnettyCst.ALARM_ACTION);  
//				cancelIntent.putExtra(AgnettyCst.FUTURE_ID, futureID);
//				cancelIntent.putExtra(AgnettyCst.FUTURE_POOL, pool);
//				PendingIntent pcancelIntent = PendingIntent
//						.getBroadcast(mContext, hashcode, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//				mAlarmManager.cancel(pcancelIntent);
//				return;
//			}
//			
//			if(future.mScheduleTimes>-1 && future.mScheduleTimes--<=0) {
//				manager.cancelFutureByID(futureID);
//				return;
//			}
//			
//			if(pool == AgnettyFuture.CACHED_POOL) {
//				manager.execCachedFuture(future);
//			} else if(pool == AgnettyFuture.FIXED_POOL) {
//				manager.execFixedFuture(future);
//			} else {
//				manager.execSingleFuture(future);
//			}
//		}
//    }
    
    /**
     * 处理UI线程的回调事件，任务进度更新、任务完成、任务异常
     * @author Zhenshui.Xia
     *
     */
    private class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String futureID = msg.getData().getString(AgnettyCst.FUTURE_ID);
			boolean cancelScheduled = msg.getData().getBoolean(AgnettyCst.CANCEL_SCHEDULED);
			AgnettyFuture future = mIDToFutures.get(futureID);
			if(future == null) return;
			AgnettyResult result = (AgnettyResult)msg.obj;
		
			switch(msg.what) {
				case AgnettyStatus.START: //任务开始
					if(future.mListener != null) future.mListener.onStart(result);
					break;
					
				case AgnettyStatus.PROGRESSING: //任务进度更新
					if(future.mListener != null) future.mListener.onProgress(result);
					break;
					
				case AgnettyStatus.COMPLETED:   //任务完成
					if(future.mListener != null) future.mListener.onComplete(result);
					if(!future.isScheduleFuture()) {
						cancelFutureByID(futureID);
					} else {
						if(cancelScheduled) {
							cancelFutureByID(futureID);
						}
					}
					break;
					
				case AgnettyStatus.EXCEPTION:   //任务发生异常
					if(future.mListener != null) future.mListener.onException(result);
					if(!future.isScheduleFuture()) {
						cancelFutureByID(futureID);
					} else {
						if(cancelScheduled) {
							cancelFutureByID(futureID);
						}
					}
					break;
					
				default:
					break;
			}
		}
	}
  
	
}
