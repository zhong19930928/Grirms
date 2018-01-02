package com.yunhu.yhshxc.location.backstage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.HomePageActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gcg.org.debug.JLog;

import static com.yunhu.yhshxc.application.SoftApplication.context;

/**
 * 
 * 守护进程1,与守护进程2互相守护并同时守护后台定位服务的开启
 *
 */
public class WatchLocationService1 extends Service {
	private WatchOneReceiver receiver;// 用于工作时间结束后收听结束指令关闭此服务
	private static boolean isNormalClose = false;// 是否是到了下班时间正常关闭
	private String TAG = getClass().getName();
	// 用于判断服务是否运行
	private String ServiceName = "com.gcg.grirms.location.backstage.WatchLocationService2";
	// 用于判断后台定位服务是否运行
	private String BackLocationService = "com.gcg.grirms.location.backstage.BackstageLocationService";

	private ExecutorService executorService = Executors.newSingleThreadExecutor();// 单线程线程池
	public static boolean isStartLocation = false;
	private WatchLocation1 service_1 = new WatchLocation1.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(WatchLocationService1.this, WatchLocationService2.class);
			WatchLocationService1.this.stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(WatchLocationService1.this, WatchLocationService2.class);
			WatchLocationService1.this.startService(i);
		}
	};
	
	/**
	 *@method 修改notification
	 *@author suhu
	 *@time 2017/4/18 14:25
	 *
	*/
	public void onCreate() {
		super.onCreate();

//		Notification notification=null;
//
//		    String tipName = getResources().getString(R.string.app_normalname);
//			 notification = new Notification(R.drawable.icon_main, "", System.currentTimeMillis());
//				Intent intentN = new Intent(getApplicationContext(), HomePageActivity.class);
//				PendingIntent pendingIntentN = PendingIntent.getActivity(getApplicationContext(), 0, intentN, 0);
//				notification.setLatestEventInfo(getApplicationContext(), tipName, "后台服务运行中", pendingIntentN);
//
//		startForeground(11, notification);

		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(R.drawable.icon_main);
		String tipName = getResources().getString(R.string.app_normalname);
		builder.setContentTitle(tipName);
		builder.setContentText(setString(R.string.backstage_service_11));

		Intent intentN = new Intent(getApplicationContext(), HomePageActivity.class);
		PendingIntent pendingIntentN = PendingIntent.getActivity(getApplicationContext(), 0, intentN, 0);
		builder.setContentIntent(pendingIntentN);
		
		Notification notification = builder.getNotification();
		manager.notify(11, notification);



		JLog.d(TAG,"开启后台定位Notification");
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startService(new Intent(WatchLocationService1.this, BackstageLocationService.class));
			    JLog.d(TAG, "定时开启后台定位服务,当前网络是否可用: "+isNetworkAvailable(getApplicationContext())+" GPS是够开启: "+gPSIsOPen(getApplicationContext()));
			}
		};
		int scheduleTime = 3 * 60 * 1000;// 启动间隔3分钟
		timer.schedule(task, 1000, scheduleTime);
		// 动态注册关闭服务广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("close.background.location");
		receiver = new WatchOneReceiver();
		this.registerReceiver(receiver, filter);

		// executorService.execute(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// while (true) {
		//
		// boolean isRun =
		// isServiceRunning(WatchLocationService1.this,ServiceName);
		//// boolean isRun = isProessRunning(WatchLocationService1.this,
		// Process_Name);
		// boolean
		// isRunLocationService=isServiceRunning(WatchLocationService1.this,BackLocationService);
		// if (isRun==false) {
		// try {
		//
		//// Log.d(TAG, "重新启动服务2");
		// service_1.startService();
		// } catch (RemoteException e) {
		// e.printStackTrace();
		// }
		//
		// }else{
		//// Log.d(TAG, "服务2在运行,不启动");
		// }
		// if (!isRunLocationService) {
		//// Log.d(TAG, "守护线程:后台定位未运行,进行开启");
		// startBackLocationService();
		// }else{
		//// Log.d(TAG, "守护线程:后台定位正在运行");
		// }
		// }
		//
		// }
		// });

	}

	// private void startDingwei(){
	// boolean
	// isRunLocationService=isServiceRunning(WatchLocationService1.this,BackLocationService);
	// if (!isRunLocationService) {
	// startBackLocationService();
	// }
	// }
 
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (!isNormalClose) {
				service_1.startService();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) service_1;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (!isNormalClose) { // 如果不是工作时间结束的销毁就重启服务
			sendBroadcast(new Intent("watchlocationservice1_destroy"));
			JLog.d(TAG,"异常终结守护进程watchlocationservice1,重新开启");
		} else { // 工作时间结束,关闭守护进程
			this.stopForeground(true);// 关闭通知栏
			JLog.d(TAG,"工作时间结束,关闭后台定位Notification");
		}
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	// //服务是否运行
	// public boolean isServiceRunning(Context context, String serviceName) {
	// boolean isRunning = false;
	// ActivityManager am = (ActivityManager) context
	// .getSystemService(Context.ACTIVITY_SERVICE);
	// List<RunningServiceInfo> lists = am.getRunningServices(30);
	//
	// for (RunningServiceInfo info : lists) {// 获取运行服务再启动
	// if (info.service.getClassName().toString().equals(serviceName)) {
	//// Log.i("Service1进程", "" + info.service.getClassName());
	// isRunning = true;
	// if (info.service.getClassName().toString().equals(BackLocationService)) {
	// isStartLocation=true;
	// }
	// }
	// }
	// return isRunning;
	//
	// }

	// private void startBackLocationService(){
	// Intent intent = new
	// Intent(WatchLocationService1.this,BackstageLocationService.class);
	//
	// WatchLocationService1.this.startService(intent);
	// }

	class WatchOneReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			isNormalClose = true;
			context.stopService(new Intent(context, WatchLocationService1.class));
			JLog.d(TAG,"工作时间结束,关闭守护进程watchlocationservice1");
		}

	}
	
	/*
	    * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	    * @param context
	    * @return true 表示开启
	    */
	   public boolean gPSIsOPen(Context context) {
	       LocationManager locationManager
	               = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	       // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
	       boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//	       // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//	       boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//	       if (gps || network) {
//	           return true;
//	       }
	       if (gps) {
			return true;
		}
	       return false;
	   }
		
		  /**
	     * 网络是否可用
	     * 
	     * @param context
	     * @return
	     */
	    public boolean isNetworkAvailable(Context context)
	    {
	        // 获取网络manager
	        ConnectivityManager mgr = (ConnectivityManager) context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo[] info = mgr.getAllNetworkInfo();
	 
	        // 遍历所有可以连接的网络
	        if (info != null)
	        {
	            for (int i = 0; i < info.length; i++)
	            {
	                if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                {
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
