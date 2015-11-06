package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class AppInstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
	           // String packageName = intent.getData().getSchemeSpecificPart();
	            Toast.makeText(context, "安装成功",1).show();
	            MainActivity.installSuccess();
	           ceshiLog.PrintLog("success");
	        }
		
	}

}
