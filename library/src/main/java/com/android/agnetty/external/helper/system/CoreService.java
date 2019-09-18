package com.android.agnetty.external.helper.system;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CoreService extends Service{

	@Override
	public void onCreate() {
		super.onCreate();
		if(CoreCst.DEBUG) {
			Toast.makeText(this, "Stub service start failed!!!", Toast.LENGTH_SHORT).show();
		}
		//core service不该被启动
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
