package com.yunhu.yhshxc.location.backstage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.dialog.DialogLocationRuleActivity;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.location.LocationFactory;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location.bo.LocationInfo;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gcg.org.debug.JLog;

/**
 * 后台定位服务。
 * 为了节省资源，该服务不会始终挂在后台运行，而是每次执行一次定位服务后便会关闭服务，然后每隔一分钟后再重启这个服务(通过AlarmManager定时实现)。
 * 这个服务只是提供一个后台定位的执行环境，是否执行定位的逻辑判断和定位任务会委派给其他类处理
 * 
 * @see BackstageLocationManager 后台定位相关的逻辑判断类
 * @see LocationFactory 执行定位的类
 * 
 * @version: 2013.5.21
 * @author wangchao
 *
 */
public class BackstageLocationService extends Service implements ReceiveLocationListener {

	 private final String TAG = "BackstageLocationService";
	
	/**
	 * 关闭定位常量
	 */
	private final int CLOSELOCATION = 1;
	
	/**
	 * 提示定位规则常量
	 */
	private final int SHOWLOCATIONRULE = 2;
	
	/**
	 * 开始定位常量
	 */
	private final int STARTPOSITION = 3;
	
	/**
	 * 为了节约资源，只需要初始化一个单例线程池即可，所有需要线程运行的Runnable都可以放入
	 * 此线程池中运行，且不需要反复执行耗费资源的线程创建/销毁工作
	 */
	ExecutorService threadPool = Executors.newSingleThreadExecutor();

	/**
	 * 后台定位用的定时器
	 */
	private AlarmManager am;
	
	/**
	 * 用于配合AlarmManager在指定时间重启当前Service以完成后台定位
	 */
	private PendingIntent pi;
	
	/**
	 * 后台定位相关属性的管理器
	 */
	private BackstageLocationManager manager;
	
	/**
	 * 判断后台定位线程是否正在运行中
	 */
	private boolean isRun = false;
	/**
	 * 判定是否开启守护线程
	 */
     private boolean isStartWatchLocation=true;
	@Override
	public void onCreate() {
		JLog.d(TAG, "BackstageLocationService.onCreate()");
		
//		clearDataForTest();
		
		initialize();
		
		printLog();

		super.onCreate();
		this.am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
		manager = new BackstageLocationManager(this);
//		if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 10) {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//					.detectDiskReads().detectDiskWrites().detectNetwork() // or .detectAll() for all detectable problems
//					.penaltyLog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
//		}
		startAlarm();
	}


	/**
	 * 测试用
	 */
	private void clearDataForTest() {
		JLog.d(TAG, "BackstageLocationService.clearDataForTest()");
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this);
		prefs.setLocationNextTime("2013-03-12 23:00:00");
		prefs.setLocationInitializedDate(0);
		prefs.setIsConfirmLocationRule(false);
		prefs.setLocationStartTime("03:10");//13:55
		prefs.setLocationStopTime("23:59");
	}
	
	/**
	 * 测试用
	 */
	private void printLog() {
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this);
		JLog.d(TAG,
				setString(R.string.backstage_service_05) + prefs.getLocationInitializedDate() +
						setString(R.string.backstage_service_06) + prefs.getIsConfirmLocationRule() +
						setString(R.string.backstage_service_07)+ prefs.getLocationIsAvailable() +
						setString(R.string.backstage_service_08) + prefs.getLocationStartTime() +
						setString(R.string.backstage_service_09) + prefs.getLocationStopTime() +
						setString(R.string.backstage_service_10) + prefs.getLocationNextTime());
	}
	
	/**
	 * 每天第一次启动程序时须执行一次初始化任务，初始化任务负责把LOCATION_RULE_IS_CONFIRM字段重置为false，
	 * 否则前一日如果因为各种原因未执行BackstageLocationManager.updateStopLocation()的话，
	 * LOCATION_RULE_IS_CONFIRM字段可能始终为true，导致无法弹出规则提示窗口。
	 * 如果当天执行过一次初始化，那么将不在执行。
	 */
	private void initialize() {
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this);
		int initDate = prefs.getLocationInitializedDate();
		Calendar current = Calendar.getInstance();
		//将当前日期转换成YYYYMMDD格式的int变量
		int today = current.get(Calendar.YEAR) * 10000 + (current.get(Calendar.MONTH) + 1) * 100 + current.get(Calendar.DAY_OF_MONTH);
		//如果当前日期大于上次初始化日期，则需要重置IsConfirm状态，并将初始化日期设置为当前日期
		if (today > initDate) {
			JLog.d(TAG, "BackstageLocationService.onCreate() -> initialized");
			prefs.setIsConfirmLocationRule(false);
			prefs.setLocationInitializedDate(today);
		}
	}

	/**
	 * 系统方法，每次启动服务时调用
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		JLog.d(TAG, "BackstageLocationService.onStartCommand()");
		
//		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(this);
//		String last = prefs.getLastLocation();
//		if (last.length() > 0) {
//			lastLocation = new LastLocation();
//			lastLocation.update(last);
//			boolean isNewDay = isNewDay();
//			if (isNewDay) {
//				//如果是新的一天，则清空上次定位的数据
//				prefs.saveLastLocation("");
//				lastLocation = null;
//			}
//		}
		//如果获得停止当前Service的标记，就终止当前Service，否则就执行后台定位逻辑。
		if (intent != null && intent.getBooleanExtra("IS_STOP", false)) {
			JLog.d(TAG, "BackstageLocationService.onStartCommand.stopSelf()");
			this.stopSelf();
		}
		else {
			//如果isRun是true，说明后台定位程序正在执行，此时不必再次执行，忽略即可。
			//反之则需要启动线程来执行后台定位任务
			if (!isRun) {
				threadPool.execute(executeRunnable);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 需要放在线程中执行的后台定位任务
	 */
	private final Runnable executeRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				isRun = true;// 线程正在运行
				execute();//执行线程任务
			}
			catch (Exception e) {
				JLog.e(e);
			}
			finally {
				isRun = false;//线程即将结束时把标记设为false
			}
		}
	};

	/**
	 * 执行后台定位任务，因为这个方法是在线程中执行的，所以这个方法涉及到界面的逻辑需要在Handler中执行
	 */
	private void execute() {
		JLog.d(TAG, "BackstageLocationService.execute()");
	
		
		initialize();

		if (manager.isCloseLocation()) { // 是否停止定位，必须放在首位
			
			JLog.d(TAG, "BackstageLocationService.execute() -> isCloseLocation = true");
		
			Message msg = handler.obtainMessage(CLOSELOCATION);
			handler.sendMessage(msg);
		}
		else if (manager.isTodayLocationByWeek()) {// 今天是否定位
			if (isStartWatchLocation) {
				startService(new Intent(BackstageLocationService.this,WatchLocationService1.class));//开启后台定位守护线程
				JLog.d("今天允许定位,开启后台定位守护线程");
				isStartWatchLocation=false;//开启后不再重复开启
			}
			
			boolean isShowLocationRule = manager.isTipUser();  
			
			if (isShowLocationRule) { // 判断是否提示定位规则
				handler.sendEmptyMessage(SHOWLOCATIONRULE);
			}
			boolean isStopLocation = manager.isStopLocation();
		
			if (isStopLocation) {// 今天定位任务是否已经结束
				JLog.d(TAG, "BackstageLocationService.execute() -> 今天定位结束");
			
				sendBroadcast(new Intent("close.background.location"));//发送广播关闭守护进程等服务
				manager.updateStopLocation();// 今天定位结束
			}
			boolean isStartPosition = manager.isStartPosition();
			if (isStartPosition) { // 是否执行定位
				handler.sendEmptyMessage(STARTPOSITION);
				manager.updateNextLocation(); // 本次定位执行完毕，设置下次定位时间
			}
			JLog.d(TAG, "BackstageLocationService.execute() -> 提示定位规则:" + isShowLocationRule +  " 今天定位结束:" + isStopLocation + " 是否执行定位:" + isStartPosition);
		}
		else {
			JLog.d(TAG, "BackstageLocationService.execute() -> 今天不定位，将下次定位时间更改成明天的");
			
			manager.updateStopLocation(); // 如果今天不定位，将下次定位时间更改成明天的
		}
		
	}

	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			isRun = false;
			int key = msg.what;
			switch (key) {
				case CLOSELOCATION:// 关闭定位
					BackstageLocationService.this.stopSelf();
					break;
				case SHOWLOCATIONRULE:
					promptLocationRule();// 显示定位规则
					break;
				case STARTPOSITION:
					// 执行定位
//					LocationFactory location = new LocationFactory(BackstageLocationService.this);
//					location.startBackstageLocation(BackstageLocationService.this);
			
					LocationFactoy location = new LocationFactoy(BackstageLocationService.this, BackstageLocationService.this);
					
					String level = SharedPreferencesUtil.getInstance(getApplicationContext()).getLocLevel("");
//					String level = "1";
					String lev = TextUtils.isEmpty(level)?"":level;
					if(lev.equalsIgnoreCase("1")){ //GPS优先
						location.startLoctionGPS();
					}else if(lev.equalsIgnoreCase("2")){//WIFI优先
						location.sartLocationWiFi();
					}else {//混合
						location.startLocationHunHe();
					}
					JLog.d(TAG, "执行一次定位!");
					
					
				
					break;
				default:
					break;
			}
			return true;
		}
	});

	/**
	 * 进入DialogActivity显示定位规则
	 */
	private void promptLocationRule() {
		Intent intent = new Intent(this, DialogLocationRuleActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}

	/**
	 * 必须实现的系统方法，没用
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 定时，每分钟启动一次当前服务以判断一次是否需要执行后台定位
	 */
	public void startAlarm() {
//		JLog.d(TAG, "BackstageLocationService.startAlarm()");
		cancelAlarm();
		// int interval = SharedPrefsBackstageLocation.getInstance(this.getApplicationContext()).getLocationEachInterval();
		int definiteTime = 1 * 60 * 1000; // 定时
		Intent intent = new Intent(this, this.getClass());
		this.pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		if (this.pi==null) {
			this.pi = PendingIntent.getService(this, 0, intent, 0);
		}
		if (this.am!=null && this.pi!=null) {
			this.am.setRepeating(0, Calendar.getInstance().getTimeInMillis() + definiteTime, definiteTime, this.pi);
		}
	}

	/**
	 * 取消定时
	 */
	public void cancelAlarm() {
//		JLog.d(TAG, "BackstageLocationService.cancelAlarm()");
		if (this.am!=null && this.pi!=null) {
			this.am.cancel(this.pi);
		}
	}

	/**
	 * 后台定位任务结果的回调方法
	 * @param result 接受到的定位数据
	 */
	@Override
	public void onReceiveResult(LocationResult result) {
		if (result != null && result.isStatus() && result.isInAcc()) {
			String lon = String.valueOf(result.getLongitude());
			String lat = String.valueOf(result.getLatitude());
			
			String address = result.getAddress();
			String time = result.getLcationTime();
			String type = PublicUtils.receiveLocationTypeByType(result.getLocType());
			float acc = result.getRadius();
			int postype = result.getPosType();
			savePassiveLocation(lon, lat, address, time, type,acc,postype);
			
			JLog.d(TAG, "经度：" + lon);
			JLog.d(TAG, "纬度：" + lat);
			JLog.d(TAG, "地址:" + (address == null ? "" : address));
			JLog.d(TAG, "定位时间:" + time);
			JLog.d(TAG, "定位类型：" + type);
			JLog.d(TAG, "-------------后台定位成功结束-----------------");
		}else {
			String info = result==null?
					setString(R.string.backstage_service_01):setString(R.string.backstage_service_02)
					+setString(R.string.backstage_service_03)+result.isStatus()+setString(R.string.backstage_service_04)+result.isInAcc();
			JLog.d("本次定位失败" + info);
			JLog.d(TAG, "-------------后台定位失败结束-----------------\n"+info);
		}
	}

	@Override
	public void onDestroy() {
		this.cancelAlarm();
		
		//如果是否关闭当天定位任务的方法返回false，则即便当前服务被关闭也必须重启。
		if (!manager.isCloseLocation()) {
			startService(new Intent(this, this.getClass()));
		}
		JLog.d(TAG, "BackstageLocationService.onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		onDestroy();//系统因资源问题需要终止当前服务时也需要重启.
	}

	/**
	 * 被动定位后台提交数据
	 * 
	 * @param lon 经度
	 * @param lat 纬度
	 * @param address 地址
	 * @param time 定位时间
	 * @param type 定位方式
	 */
	private void savePassiveLocation(String lon, String lat, String address, String time, String type,float acc,int postype) {
		
		LocationInfo info = new LocationInfo();
		info.setLon(lon);
		info.setLat(lat);
		info.setAddr(address);
		info.setLocationType(type);
		info.setLocationTime(time);
		info.setAcc(String.valueOf(acc));
		info.setAction(LocationMaster.ACTION);
		info.setVersion(LocationMaster.VERSION);
		info.setPosType(String.valueOf(postype));

		new CoreHttpHelper().performLocationSubmit(this,info);
	}

	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
