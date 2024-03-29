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

package com.android.agnetty.future.local;

import java.lang.reflect.Constructor;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.android.agnetty.core.AgnettyFuture;
import com.android.agnetty.core.AgnettyFutureListener;
import com.android.agnetty.core.AgnettyHandler;
import com.android.agnetty.core.AgnettyManager;
import com.android.agnetty.core.event.ExceptionEvent;
import com.android.agnetty.core.event.MessageEvent;
import com.android.agnetty.future.http.HttpHandler;
import com.android.agnetty.utils.LogUtil;
import com.android.agnetty.utils.RefUtil;

/**
 * @author : Zhenshui.Xia
 * @date   : 2013-9-19
 * @desc   : 
 */
public class LocalFuture extends AgnettyFuture{
	private LocalHandler mHandler;
	
	public LocalFuture(Context context) {
		super(context);	
	}
	
	@Override
	public void run() {
		//首次实例化任务处理器
		if(mHandler == null) {
			//没有设置任务处理器,使用默认的本地任务处理器，DefaultLocalHandler
			if(mHandlerCls == null) {
				mHandler = new LocalDefaultHandler(mContext);
			} else {
				try {
					Constructor<? extends AgnettyHandler> constructor 
													= mHandlerCls.getConstructor(Context.class);
					mHandler = (LocalHandler)constructor.newInstance(mContext);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				} 
			}
		}
		
		if(mListener != null) mListener.setFuture(this);
		
		//执行任务
		try {
			MessageEvent evt = new MessageEvent();
			evt.setFuture(this);
			evt.setData(mData);
			mHandler.onExecute(evt);
			if(!mFinished) commitComplete(null, false);
		} catch(Exception ex) {
			ExceptionEvent evt = new ExceptionEvent();
			evt.setFuture(this);
			evt.setException(ex);
			mHandler.onException(evt);
			if(!mFinished) commitException(null, ex, false);
		} finally {
			mHandler.onDispose();
		}
	}
	

	@Override
	public String getName() {
		String futureName = mHandlerCls==null ? LocalDefaultHandler.class.getName() : mHandlerCls.getName();
		return futureName;
	}



	public static class Builder {
		private Context mContext;
		
		private Class<? extends AgnettyHandler> mHandlerCls;
		private Object mData;
		private AgnettyFutureListener mListener; 
		private Object mTag; 
		private int mPool;
		
		private int mDelayType ; 
		private int mDelayTime; 
		private boolean mIsDelay;
		
		private int mScheduleType;
		private int mScheduleTrigger; 
		private int mScheduleInterval; 
		private int mScheduleTimes;
		private boolean mIsSchedule;
		
		public Builder(Context context) {
			this.mContext = context;
			this.mPool = CACHED_POOL;
		}
		
		/**
		 * 设置任务处理器
		 * @param handler
		 * @return
		 */
		public Builder setHandler(Class<? extends AgnettyHandler> handler) {
			this.mHandlerCls = handler;
			return this;
		}
		
		/**
		 * 设置任务处理器执行数据
		 * @param data
		 * @return
		 */
		public Builder setData(Object data) {
			this.mData = data;
			return this;
		}
		
		/**
		 * 设置任务监听器
		 * @param listener
		 * @return
		 */
		public Builder setListener(AgnettyFutureListener listener) {
			this.mListener = listener;
			return this;
		}
		
		/**
		 * 设置任务标记
		 * @param tag
		 * @return
		 */
		public Builder setTag(Object tag) {
			this.mTag = tag;
			return this;
		}
		
		/**
		 * 设置任务执行动作方式
		 * @param action
		 */
		public Builder setPool(int pool) {
			this.mPool = pool;
			return this;
		}
		
		/**
		 * 设置定时执行的任务
		 * @param startTime 	
		 * @param intervalTime  
		 * @return
		 */
		public Builder setSchedule(int startTime, int intervalTime, int maxTimes) {
			setSchedule(RTC_WAKEUP, startTime, intervalTime, maxTimes);
			return this;
		}
		
		/**
		 * 设置定时执行的任务
		 * @param type   
		 * @param startTime     
		 * @param intervalTime 
		 * @return
		 */
		public Builder setSchedule(int type, int startTime, int intervalTime, int maxTimes) {
			mIsDelay = false;
			
			if(startTime < 0
					|| intervalTime <= 0
					|| !(type == RTC_WAKEUP
					|| type == RTC
					|| type == ELAPSED_REALTIME_WAKEUP
					|| type == ELAPSED_REALTIME)) {
				mIsSchedule = false;
			} else {
				mScheduleType = type;
				mScheduleTrigger = startTime;
				mScheduleInterval = intervalTime;
				mScheduleTimes = maxTimes;
				mIsSchedule = true;
			}
			
			return this;
		}
		
		
		/**
		 * 设置任务执行的延迟时间
		 * @param delay 
		 * @return
		 */
		public Builder setDelay(int delayTime) {   
			setDelay(RTC_WAKEUP, delayTime);
			return this;
		}
		
		/**
		 * 设置任务执行的延迟时间
		 * @param type  								  
		 * @param delayTime  
		 * @return
		 */
		public Builder setDelay(int type, int delayTime) { 
			mIsSchedule = false;
			
			if(delayTime < 0
					|| !(type == RTC_WAKEUP
					|| type == RTC
					|| type == ELAPSED_REALTIME_WAKEUP
					|| type == ELAPSED_REALTIME)) {
				mIsDelay = false;
			} else {
				mDelayType = type;
				mDelayTime = delayTime;
				mIsDelay = true;
			}
			
			return this;
		}
		
		/**
		 * 创建一个本地任务
		 * @return
		 */
		public LocalFuture create() {
			final LocalFuture future = new LocalFuture(mContext);
			
			future.mHandlerCls = mHandlerCls;
			future.mData = mData;
			future.mListener = mListener;
			future.mTag = mTag;
			future.mPool = mPool;
			
			future.mDelayType = mDelayType;
			future.mDelayTime = mDelayTime;
			future.mIsDelay = mIsDelay;
			
			future.mScheduleType = mScheduleType;
			future.mScheduleTrigger = mScheduleTrigger;
			future.mScheduleInterval = mScheduleInterval;
			future.mScheduleTimes = mScheduleTimes;
			future.mIsSchedule = mIsSchedule;
			
			return future;
		}
		
		/**
		 * 异步执行任务
		 * @return
		 */
		public LocalFuture execute() {
			LocalFuture future = create();
			future.execute();
			return future;
		}
	}
}
