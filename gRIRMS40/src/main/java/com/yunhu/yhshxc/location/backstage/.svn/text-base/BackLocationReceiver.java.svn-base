package com.yunhu.yhshxc.location.backstage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听自定义广播,启动后台定位守护
 * @author qingli
 *
 */
public class BackLocationReceiver extends BroadcastReceiver {
	
	
  private String backLoacation="com.gcg.grirms.location.backstage.BackstageLocationService";
	@Override
	public void onReceive(Context context, Intent intent) {	
		// TODO Auto-generated method stub
//		boolean isStartBackLocation=isServiceRunning(context, backLoacation);
//		if (!isStartBackLocation) {
//			Intent intentS = new Intent(SoftApplication.context,BackstageLocationService.class);
//			context.startService(intentS);
//		}
		
		if ("watchlocationservice2_destroy".equals(intent.getAction())) {
			context.startService(new Intent(context,WatchLocationService1.class));
		}else if ("watchlocationservice1_destroy".equals(intent.getAction())) {
			context.startService(new Intent(context,WatchLocationService2.class));
		}
		

	}

//public static boolean isServiceRunning(Context context, String serviceName) {
//		
//		boolean isRunning = false;
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningServiceInfo> lists = am.getRunningServices(30);
//		
//		for (RunningServiceInfo info : lists) {//判断服务
//				if(info.service.getClassName().toString().equals(serviceName)){
//					isRunning=true;
//				}
//		} 
//		
//		
//		return isRunning;
//	}
}
