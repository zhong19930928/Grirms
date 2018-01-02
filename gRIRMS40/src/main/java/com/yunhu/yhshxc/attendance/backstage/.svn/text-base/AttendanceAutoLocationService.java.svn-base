package com.yunhu.yhshxc.attendance.backstage;

import gcg.org.debug.JLog;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Service;
import android.content.Intent;
import android.text.TextUtils;

import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.utility.DateUtil;

public class AttendanceAutoLocationService extends AttendanceLocationService implements
		ReceiveLocationListener {

	private Date startDate;
	private Date stopDate;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		JLog.d("守店进程还在...");
		serviceFlag = SharedPrefsAttendanceUtil.getInstance(getApplicationContext()).getAttendanceBackstageLocationServiceFlg();
		if(serviceFlag == 1){
			JLog.d(TAG, "serviceFlag=" + serviceFlag);
			release();
			return Service.START_STICKY;
		}
		
		// 如果定位间隔为0的话，说明没有配置定位，不需要此定位
		intervalTime = SharedPrefsAttendanceUtil.getInstance(getApplicationContext()).getAttendanceBackstageLocationIntervalTime();
		if (intervalTime == 0) {
			JLog.d(TAG, "定位间隔=" + intervalTime);
			release();// 关闭服务
			return Service.START_STICKY;
		}
		
		if(isTodayLocationByWeeks() && isOpenAutoLocation()){
			if(isFirstPointForStartWork()){
				//如果是定位规则中的第一个定位点,则对LocationLastTime进行设置
				setLastTimeForFirstPoint();
			}
			exeLocation();
		}else{
			JLog.d(TAG, "还不到定位的时候...");
		}
		
		return Service.START_STICKY;
	}
	
	/**
	 * 判断此时是否为定位规则中的第一个定位点
	 * 上一次定位时间小于开始定位时间，说明是第一个定位点
	 * 
	 * @return
	 */
	private boolean isFirstPointForStartWork(){
		if (startDate == null){
			return false;
		}
		String lastTime = SharedPrefsAttendanceUtil.getInstance(
				this.getApplicationContext())
				.getAttendanceBackstageLocationLastTime();
		if (TextUtils.isEmpty(lastTime) || DateUtil.getDate(lastTime).before(startDate)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 设置LocationLastTime
	 * 如果开始时间与当前时间的间隔 > 规则间隔 * 1.5
	 * 则说明手机存在故障，之前未定位，将lastTime = 开始定位时间
	 * 否则说明手机正常定位，将lastTime=""
	 * 这样在后面的逻辑中就会顺理成章
	 */
	private void setLastTimeForFirstPoint(){
		String start = DateUtil.dateToDateString(startDate);
		double l = DateUtil.compareCurrentDateStr(start);
		double min = l / 60000;
		if (min > intervalTime * 1.5) {
			SharedPrefsAttendanceUtil.getInstance(
					this.getApplicationContext())
					.setAttendanceBackstageLocationLastTime(start);
		}else{
			SharedPrefsAttendanceUtil.getInstance(
					this.getApplicationContext())
					.setAttendanceBackstageLocationLastTime("");
		}
	}
	
	/**
	 * 设置自动查岗定位;
	 * 查岗服务启动方式 = 2时，和被动定位相同，以 『周几启动服务 + 查岗时间段』来处理
	 * 
	 * @return
	 */
	private boolean isOpenAutoLocation(){
		String attendTime = SharedPrefsAttendanceUtil.getInstance(getApplicationContext()).getAttendanceBackstageLocationAttendTime();
		if(TextUtils.isEmpty(attendTime)){
			return false;
		}
		String startTime = attendTime.split(",")[0];
		Float workTimeLong = Float.valueOf(attendTime.split(",")[1]) * 60;
		startDate = DateUtil.getDate(DateUtil.getCurDate()+" "+startTime, DateUtil.YYYY_MM_DD_HH_MM);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MINUTE, workTimeLong.intValue());
		stopDate = cal.getTime();
		Date nowDate = DateUtil.getCurrentDate();
		if(nowDate.before(startDate) || nowDate.after(stopDate)){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一周中的今天是不是能够执行
	 * 格式：weeks: "1,2,3,4,5,6,7"
	 * @return
	 */
	private boolean isTodayLocationByWeeks(){
		String weeks = SharedPrefsAttendanceUtil.getInstance(getApplicationContext()).getAttendanceBackstageLocationWeeks();
		if(TextUtils.isEmpty(weeks)){
			return false;
		}
		String []weekArr = weeks.split(",");
		int w = getDaySpacing(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DAY_OF_WEEK));
		for(String s : weekArr){
			if(w == Integer.valueOf(s).intValue()){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 转换成中国星期制
	 */
	private int getDaySpacing(int dayOfWeek) {
		if (Calendar.SUNDAY == dayOfWeek)
			return 7;
		else
			return dayOfWeek - 1;
	}

	@Override
	protected boolean isAllowStartServiceByPeriod() {
		return true;
	}
	
}
