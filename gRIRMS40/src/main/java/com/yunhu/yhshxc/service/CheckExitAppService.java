package com.yunhu.yhshxc.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class CheckExitAppService extends Service {

	
	private AlarmManager am = null;  //定时器
	private PendingIntent pi = null; //定时执行意图
	private final int INIT_INTERVAL = 6*60*60;//初始化间隔时间秒
//	private final int INIT_INTERVAL = 1*60;//初始化间隔时间秒
	public final static String GCG_ANDROID_GRIRMS_INIT_ACTION = "gcg_android_grirms_init_action";//初始化系统广播
	
	
	@Override
	public void onCreate() {
		super.onCreate();
//		JLog.d("***********onCreate**************CheckInitService**************onCreate***********");
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		String lastRunTiem = SharedPreferencesUtil.getInstance(this).getLastRunTime();
//		JLog.d("***********onStartCommand**************CheckInitService**************onStartCommand***********"+lastRunTiem);
		if (TextUtils.isEmpty(lastRunTiem)) {//lastRunTiem 是空说明应用在前端运行
			stopSelf();
		}else{
			startAlarm();
			checkBackgroundTime(lastRunTiem);
		}
		return Service.START_STICKY;
	};
	
	
	/**
	 * 检测程序在后台运行的时长
	 */
	private void checkBackgroundTime(String lastRunTiem){
		long intervalTime = DateUtil.compareCurrentDateStr(lastRunTiem);
		if (intervalTime/1000 >= INIT_INTERVAL) {//后台运行时长大于设定时长就初始化
			Intent intent = new Intent();
			intent.setAction(GCG_ANDROID_GRIRMS_INIT_ACTION);
			sendBroadcast(intent);
		}
	}
	
	/**
	 * 定时启动服务
	 */
	private void startAlarm() {
		int intervalMillis = 5*60*1000;
		if(am == null){
			am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
			pi = PendingIntent.getService(this, 0, new Intent(this, this.getClass()), PendingIntent.FLAG_UPDATE_CURRENT);
		}
		am.cancel(pi);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis,intervalMillis, pi);
	}
	
	/**
	 * 销毁服务，并定时下次启动时间
	 */
	public void onDestroy() {
		String lastRunTiem = SharedPreferencesUtil.getInstance(this).getLastRunTime();
//		JLog.d("***********onDestroy**************CheckInitService**************onDestroy***********"+lastRunTiem);
		if (!TextUtils.isEmpty(lastRunTiem)) {//如果是空的话说明程序在前端运行，不需要开启服务
			startAlarm();
		}
		super.onDestroy();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
