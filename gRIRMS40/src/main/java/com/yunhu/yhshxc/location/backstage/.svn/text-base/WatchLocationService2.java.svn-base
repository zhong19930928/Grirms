package com.yunhu.yhshxc.location.backstage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;

import gcg.org.debug.JLog;
/**
 * 
 * 守护进程2,与守护进程1互相守护后台定位服务
 *
 */
public class WatchLocationService2 extends Service {
	private WatchTwoReceiver receiver;//用于接收关闭此服务的广播
	   private static boolean isNormalClose=false;//是否是到了下班时间正常关闭
	private String TAG = getClass().getName();
	private String ServiceName = "com.gcg.grirms.location.backstage.WatchLocationService1";

	private ExecutorService executorService = Executors.newFixedThreadPool(1);//线程池管理线程
   public static boolean isStartLocation=false;
	private WatchLocation1 service_2 = new WatchLocation1.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(WatchLocationService2.this, WatchLocationService1.class);
			WatchLocationService2.this.stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(WatchLocationService2.this, WatchLocationService1.class);
			WatchLocationService2.this.startService(i);
			
		}
	};


	public void onCreate() {
		super.onCreate();
		//动态注册接收关闭此服务的接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("close.background.location");
		 receiver = new WatchTwoReceiver();
		this.registerReceiver(receiver, filter);
		
//		executorService.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				while (true) {
////					try {
////						Thread.currentThread().sleep(3000);
////					} catch (InterruptedException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//					boolean isRun = isServiceRunning(WatchLocationService2.this,ServiceName);
////					boolean isRun = isProessRunning(WatchLocationService2.this, Process_Name);
//					if(isRun==false){
//						try {
////							Log.i(TAG, "重新启动服务: "+service_2);
//							service_2.startService();
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}	
////						if (!isStartLocation) {
////							startBackLocationService();
////						}
//					}
//				}
//			
//			}
//		});
//		  Timer timer = new Timer();
//			
//			
//			
//			TimerTask task = new TimerTask() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					startService(new Intent(WatchLocationService2.this,WatchLocationService1.class));
//				}
//			};
//			int scheduleTime=2*60*1000;//启动间隔
//			timer.schedule(task , 1000, scheduleTime);
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (!isNormalClose) {				
				service_2.startService();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) service_2;
	}
  @Override
public void onDestroy() {
	// TODO Auto-generated method stub
	  if (!isNormalClose) {	//如果不是工作时间结束的销毁就重启服务			
		  sendBroadcast(new Intent("watchlocationservice2_destroy"));
	}
	  unregisterReceiver(receiver);
	super.onDestroy();
}
//	//服务是否运行
//	public static boolean isServiceRunning(Context context, String serviceName) {
//		
//		boolean isRunning = false;
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningServiceInfo> lists = am.getRunningServices(30);
//		
//		for (RunningServiceInfo info : lists) {//判断服务
//				if(info.service.getClassName().toString().equals(serviceName)){
////				Log.i("Service1进程", ""+info.service.getClassName());
//				isRunning = true;
////				if (info.service.getClassName().equals(backLocationService)) {
////					isStartLocation=true;
////				}
//			}
//		}
//		
//		
//		return isRunning;
//	}
	
//	//进程是否运行
//	public static boolean isProessRunning(Context context, String proessName) {
//		
//		boolean isRunning = false;
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//
//		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
//		for(RunningAppProcessInfo info : lists){
//			if(info.processName.equals(proessName)){
//				//Log.i("Service2进程", ""+info.processName);
//				isRunning = true;
//			}
//		}
//		
//		return isRunning;
//	}
//    private void startBackLocationService(){
////    	Intent intent = new Intent();
////    	intent.setClassName("com.gcg.grirms.location.backstage","BackstageLocationService");
//    	Intent intent = new Intent(WatchLocationService2.this,BackstageLocationService.class);
//    	WatchLocationService2.this.startService(intent);
//    }
  
  class WatchTwoReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		isNormalClose=true;
		 context.stopService(new Intent(context,WatchLocationService2.class));
		 JLog.d("工作时间结束,关闭守护进程watchlocationservice2");
	}
	  
  }
}
