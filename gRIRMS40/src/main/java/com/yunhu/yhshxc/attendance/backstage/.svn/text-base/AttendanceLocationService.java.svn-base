package com.yunhu.yhshxc.attendance.backstage;

import gcg.org.debug.JLog;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import com.yunhu.yhshxc.attendance.AttendanceUtil;
import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location.bo.LocationInfo;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

public class AttendanceLocationService extends Service implements
		ReceiveLocationListener {

	public final String TAG = this.getClass().getSimpleName();
	private AlarmManager am = null; // 定时器
	private PendingIntent pi = null; // 定时执行意图

	public static final String WORK_STATUS = "workStatus";
	public static final int WORK_START = 1;
	public static final int WORK_END = 2;
	public static final int WORK_PHONE_RESTART = 3;
	public static final int WORK_APP_RESTART = 4; //防止本服务意外停止无法启动时，
	private final String ISEXP_SUCCESS = "1";
	private final String ISEXP_FAIL = "2";
	private  String REMARK_UP = "";
	private  String REMARK_DOWN = "";
	private  String REMARK_FAIL = "";

	protected int workStatus = 0; // 开启定位时，告诉服务是上班，还是下班定位
	protected int serviceFlag = 1;//1：考勤上班打卡启动服务、2：固定时间开始服务
	
	int loopTime = 40 * 1000;
	int intervalTime = 0; //定位时间间隔（min）
	boolean isRunning = false;

	/**
	 * 定时启动服务
	 */
	private void startAlarm() {
		if (am == null) {
			am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
		}
		pi = PendingIntent.getService(this, 0,
				new Intent(this, this.getClass()),
				PendingIntent.FLAG_UPDATE_CURRENT);
		closeAlarm();
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ loopTime, loopTime, pi);
	}

	private void closeAlarm() {
		if (am != null) {
			am.cancel(pi);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		REMARK_UP = PublicUtils.getResourceString(getApplicationContext(),R.string.dateselectview2);
		REMARK_DOWN = PublicUtils.getResourceString(getApplicationContext(),R.string.end_service);
		REMARK_FAIL = PublicUtils.getResourceString(getApplicationContext(),R.string.end_service1);
		this.isRunning = false;
		startAlarm();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		this.serviceFlag = SharedPrefsAttendanceUtil.getInstance(getApplicationContext()).getAttendanceBackstageLocationServiceFlg();
		if(this.serviceFlag == 2){
			JLog.d(TAG, "serviceFlag=" + serviceFlag);
			AttendanceUtil.startAttendanceLocationService(this, 0);//手动改自动
			this.release();
			return Service.START_STICKY;
		}
		
		// 如果定位间隔为0的话，说明没有配置定位，不需要此定位
		this.intervalTime = SharedPrefsAttendanceUtil.getInstance(
				getApplicationContext())
				.getAttendanceBackstageLocationIntervalTime();
		if (this.intervalTime == 0) {
			JLog.d(TAG, "定位间隔=" + intervalTime);
			this.release();// 关闭服务
			return Service.START_STICKY;
		}
		
		this.workStatus = 0;
		if (intent != null) {
			this.workStatus = intent.getIntExtra(WORK_STATUS, 0);
		}
		
		this.exeLocation();
		
		return Service.START_STICKY;
	}

	/**
	 * 满足条件后开始定位 条件：1. 到达开始定位的时间，2.例外时间内不查岗(可以后台设置不查岗的时间段)
	 * 
	 */
	public void exeLocation() {

		if (workStatus != WORK_START && workStatus != WORK_END) { // 为的是上班，下班直接提交定位，无需验证
			
			if(!isAllowStartServiceByPeriod()){ //考勤周期性，服务意外终止都需要验证
				release();
				return;
			}
			
			if (!isArrivalLocationTime()) {
				return;
			}
			phoneRestart();
			
			if (isCannotLocation()) {
				return;
			}
		}else{
			JLog.d(TAG, (workStatus == WORK_START ? "上班": "下班")+"后定位");
		}
		
		location();
	}
		
	private void location(){
		if(!this.isRunning || this.workStatus>0){
			this.isRunning = true;
			// 执行定位
//			new LocationFactory(this).startBackstageLocation(this);
			new LocationFactoy(this, this).startLocationHH();
			JLog.d(TAG, "执行一次定位!");
		}else{
			JLog.d(TAG, "正在定位...");
		}
	}
	
	/**
	 * 因考勤存在周期性（如果用户不打下班卡，第二天复位），
	 * 所以每次执行都需要判断服务是否要关闭
	 * @return
	 */
	protected boolean isAllowStartServiceByPeriod(){
		
		setNewAttendTime();
		boolean start = SharedPreferencesUtil.getInstance(getApplicationContext()).getNewAttendanceStartDoCard();
		if(!start){
			return false;
		}
		boolean end	= SharedPreferencesUtil.getInstance(getApplicationContext()).getNewAttendanceEndDoCard();
		if(end){
			return false;
		}else{
			return true;
		}
	}
	private  void setNewAttendTime(){
		flagTime = true;
		timerT = new Timer(true);
		if(taskT.cancel()){
			timerT.schedule(taskT,3000, 100000000); //延时3000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Date date = PublicUtils.getNetDate();;
				  Message msg = Message.obtain();
				  msg.obj = date;
				  mHanlderTime.sendMessage(msg);
			}
		}).start();
	}
	 Handler mHanlderTime = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(flagTime){
				flagTime = false;
				if (taskT != null){
					taskT.cancel();
				 }
				Date date = (Date) msg.obj;
				AttendanceUtil.resetAttendance(getApplicationContext(),date);
			}
		};
	};
	private  boolean flagTime = true;
	 Timer timerT;
	 TimerTask taskT = new TimerTask(){  
        public void run() {  
        	mHanlderTime.sendEmptyMessage(1);   
        }  
	};
	/**
	 * 是否到达下次定位时间
	 * 
	 * @return
	 */
	private boolean isArrivalLocationTime() {
		String lastTime = SharedPrefsAttendanceUtil.getInstance(
				this.getApplicationContext())
				.getAttendanceBackstageLocationLastTime();
		if (TextUtils.isEmpty(lastTime)) {
			return true;
		}
		Date runDate = DateUtil.getDate(DateUtil.addDate(
				DateUtil.getDate(lastTime), Calendar.MINUTE, intervalTime));
		Date nowDate = DateUtil.getCurrentDate();

		if (nowDate.after(runDate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断手机是否关机、不在服务区没信号的逻辑 a、如果本次定位失败，上报 “手机故障”的定位数据。
	 * b、如果手机关机（扣电池）后离岗，没有定位服务就不能知道他是否离岗，只能告知管理者“手机故障”。
	 * 上班打卡提交后就设定“最后定位时间”，之后每成功定位一次，刷新“最后定位时间”。 本日内有不在服务的判断 If 本次成功定位时间 -
	 * “最后定位时间” > 查岗定位时间间隔 * 1.5 { 发送一条“手机故障”数据（备注 = “最后定位时间” + 定位时间间隔 / 2 ~
	 * 本次成功定位时间 – 定位时间间隔 / 2）给后台。 }
	 */
	private void phoneRestart() {
		String lastTime = SharedPrefsAttendanceUtil.getInstance(
				this.getApplicationContext())
				.getAttendanceBackstageLocationLastTime();
		JLog.d("上次定位："+lastTime+" /"+this.intervalTime);
		if (TextUtils.isEmpty(lastTime)) {
			return;
		}
		double l = DateUtil.compareCurrentDateStr(lastTime);
		double min = l / 60000;
		if (min > this.intervalTime * 1.5) {
			Date nowDate = DateUtil.getCurrentDate();
			String start = DateUtil.addDate(DateUtil.getDate(lastTime),
					Calendar.MINUTE, this.intervalTime / 2);
			String end = DateUtil.addDate(nowDate, Calendar.MINUTE,
					-(this.intervalTime / 2));
			String remark = new StringBuffer().append(REMARK_FAIL).append("(")
					.append(start).append("~").append(end).append(")")
					.toString();
			savePassiveLocation("", "", "", DateUtil.dateToDateString(nowDate),
					"", "", "", remark, ISEXP_FAIL);
			JLog.d(TAG, remark);
		}
	}

	/**
	 * 例外时间内不查岗(可以后台设置不查岗的时间段)
	 * @return
	 */
	private boolean isCannotLocation() {
		// 12:00,1.0|14:00,0.5
		try {
			String notExeTime = SharedPrefsAttendanceUtil.getInstance(
					getApplicationContext())
					.getAttendanceBackstageLocationExpTime();
			//notExeTime = "12:00,1.0|16:00,0.1";
			if (TextUtils.isEmpty(notExeTime)) {
				return false;
			}
			String []arr = notExeTime.split("\\|");
			Date nowDate = DateUtil.getCurrentDate();
			Date start;
			Date end;
			for(String s : arr){
				start = DateUtil.getDate(DateUtil.getCurDate() + " " + s.split(",")[0]+":00");
				end = getEndDateInNotCheckWork(start,s.split(",")[1]);
				if(nowDate.after(start) && nowDate.before(end)){
					JLog.d(TAG, "不查岗:"+s);
					SharedPrefsAttendanceUtil.getInstance(this.getApplicationContext())
					.setAttendanceBackstageLocationLastTime(DateUtil.dateToDateString(nowDate));
					return true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			JLog.e(e);
		}

		return false;
	}
	
	private Date getEndDateInNotCheckWork(Date startDate,String interval){
		Float intervalMin = Float.valueOf(interval) * 60;
		String end = DateUtil.addDate(startDate, Calendar.MINUTE, intervalMin.intValue());
		return DateUtil.getDate(end);
		 
	}

	@Override
	public void onReceiveResult(LocationResult result) {
		
		String lon = "", lat = "", acc = "", address = "", time = "", type = "", postype = "";
		String remark = null, inexp = null;

		if (result != null && result.isStatus()) {
			lon = String.valueOf(result.getLongitude());
			lat = String.valueOf(result.getLatitude());
			address = result.getAddress();
			time = result.getLcationTime();
			type = PublicUtils.receiveLocationTypeByType(result.getLocType());
			acc = result.getRadius() + "";
			postype = result.getPosType() + "";

			remark = null;
			inexp = ISEXP_SUCCESS;
		} else {
			inexp = ISEXP_FAIL;
			time = DateUtil.getCurDateTime();
			remark = REMARK_FAIL;
			JLog.d(TAG, "本次定位失败");
		}
		if (workStatus == WORK_START) {
			remark = REMARK_UP;
		} else if (workStatus == WORK_END) {
			closeAlarm();
			remark = REMARK_DOWN;
		}
		LocationInfo info = savePassiveLocation(lon, lat, address, time, type,
				acc, postype, remark, inexp);
		locationFinish(info);
		this.isRunning = false;
	}

	private void locationFinish(LocationInfo info) {
		JLog.d(TAG, "经度：" + info.getLon());
		JLog.d(TAG, "纬度：" + info.getLat());
		JLog.d(TAG, "地址:" + info.getAddr());
		JLog.d(TAG, "定位时间:" + info.getLocationTime());
		JLog.d(TAG, "定位类型：" + info.getPosType());
		JLog.d(TAG, "备注：" + info.getRemark());
		JLog.d(TAG, "定位状态：" + info.getIsExp());
		JLog.d(TAG, "-------------后台定位结束--------------("
				+ Thread.currentThread().getName() + ")");
		if (workStatus == WORK_END) {
			release();
		}
		SharedPrefsAttendanceUtil.getInstance(this.getApplicationContext())
				.setAttendanceBackstageLocationLastTime(info.getLocationTime());
	}

	/**
	 * 被动定位后台提交数据
	 * 
	 * @param lon
	 *            经度
	 * @param lat
	 *            纬度
	 * @param address
	 *            地址
	 * @param time
	 *            定位时间
	 * @param type
	 *            定位方式
	 */
	private LocationInfo savePassiveLocation(String lon, String lat,
			String address, String time, String type, String acc,
			String postype, String remark, String isExp) {

		LocationInfo info = new LocationInfo();
		info.setLon(lon);
		info.setLat(lat);
		info.setAddr(address);
		info.setLocationType(type);
		info.setLocationTime(time);
		info.setAcc(acc);
		info.setAction(LocationMaster.ACTION);
		info.setVersion(LocationMaster.VERSION);
		info.setPosType(postype);
		info.setRemark(remark);
		info.setIsExp(isExp);
		new CoreHttpHelper().performAttendanceLocationSubmit(this, info);
		return info;
	}

	protected void release() {
		closeAlarm();
		SharedPrefsAttendanceUtil.getInstance(this.getApplicationContext())
		.setAttendanceBackstageLocationLastTime("");
		this.stopSelf();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		JLog.d(TAG, "守店定位服务关闭");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
