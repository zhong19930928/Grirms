package com.yunhu.yhshxc.location.backstage;

import gcg.org.debug.JLog;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 后台定位管理器
 * 用于BackstageLocationService存取、判断与后台定位相关的各种状态属性
 * 
 * @version 2013.5.21
 *
 */
public class BackstageLocationManager {
	private final String TAG = "BackstageLocationManager";
	private Context context;
	
	/**
	 * 在定位规则提示框中点击稍后按钮时存储的延迟时间。
	 * 需要注意的是，当需要向用户提示后台定位时，因为种种原因提示框弹出后用户可能长时间不对其进
	 * 行操作，极端情况下可能出现提示框在23:50弹出，而用户点击确定或稍后时已经是次日00:05的情况，
	 * 这种情况下如果按照用户点击按钮的时间来计算，那么实际上用户所操作的就成了第二天的定位规则。
	 * 为了解决这种情况，就需要保存提示框弹出的那个时间，并用这个时间来计算用户的操作。
	 * （弹出窗口前设置时间，关闭窗口时清空）
	 */
	public static Calendar delay;
	
	public BackstageLocationManager(Context context) {
		this.context = context;
	}
	
	/**
	 * 是否马上执行定位。
	 * 如果用户同意今天定位，并且当前时间大于下次定位时间，那么就认为应该执行一次定位。
	 * 
	 * @return 如果应该马上执行一次定位，则返回true，否则返回false
	 */
	public synchronized boolean isStartPosition(){
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext());
		boolean isConfirmLocationRule = prefs.getIsConfirmLocationRule();//获取用户是否已经同意今天定位
		String nextTimeStr = prefs.getLocationNextTime();//获取下次定位时间
		JLog.d(TAG,"BackstageLocationManager.isStartPosition() -> isConfirm:" + isConfirmLocationRule + " 下次定位时间:" + nextTimeStr);
		if(!isConfirmLocationRule){
			 return false;
		}
		
		if(TextUtils.isEmpty(nextTimeStr)){
			JLog.d(TAG,"nextTime =>null");
			return false;
		}
		Date startTime = DateUtil.getDate(nextTimeStr);
		Date now = new Date();
		if(now.after(getTodayLocationStartDate(null)) && now.after(startTime)) { //当前时间大于启动时间，则返回true，开启定位
			return true;
		}else{
			return false;  //定位时间还没到
		}
	}
	
	/**
	 * 判断今天的定位任务是否已经结束。
	 * 根据每日定位时间段来判断，如果当前时间大于今天结束定位的时间，则认为今天的定位任务可以结束了。
	 * 
	 * @return 如果今天不再需要定位，则返回true，否则返回false
	 */
	public boolean isStopLocation(){
		Date stopDate = getTodayLocationStopDate();
		if(new Date().after(stopDate)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否关闭Service。
	 * 如果开始定位时间或结束定位时间二者中有一个为空，或标记是否定位的参数为false则关闭定位。
	 * 
	 * @return 如果关闭定位，则返回true，否则返回false
	 */
	public boolean isCloseLocation(){
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext());
		String startOnlyTime = prefs.getLocationStartTime();
		String stopOnlyTime = prefs.getLocationStopTime();
		boolean isEmptyByTime = TextUtils.isEmpty(startOnlyTime) || TextUtils.isEmpty(stopOnlyTime);
		boolean isAvailable = prefs.getLocationIsAvailable();
		
		return isEmptyByTime || !isAvailable;
	}
	
	/**
	 * 判断是否向用户弹出定位规则的提示框
	 * 定位规则的提示框原则上只是让用户知道在某个时间段内系统会对他进行跟踪（后台定位并提交位置信息到服务器上），
	 * 用户只能选择接受或延迟接受，而不能选择拒绝。
	 * 
	 * 一般情况：定位规则会在当日开始定位时间前一小时弹出，也就是说如果当日定位时间从09:00开始，那么用户只要在
	 * 08:00以后登录系统且当日尚未确认过定位规则，就应该弹出提示框。
	 * 特殊情况：如果当日定位开始时间早于凌晨01:00，那么只要在当日00:00以后登录系统就会向用户弹出定位规则提示框，
	 * 而不会把提示框弹出时间推前一小时。
	 * 
	 * @return 如果需要提示定位规则，则返回true，否则返回false
	 */
	private boolean isShowLocationRule(){
		boolean isReadedLaw = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getIsReadedLaw();
		if(!isReadedLaw){  //如果法律条文没有同意，那么就不提示用户定位，同时也不采取被动定位
			return false;
		}
		
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext());
		boolean isConfirm = prefs.getIsConfirmLocationRule();
		if (isConfirm)//如果今天用户已经确认过定位规则，就不再需要弹出提示框
			return false;
		
		long current = System.currentTimeMillis();
		if (delay != null) {
			if (current < delay.getTimeInMillis()) {
				return false;
			}
			else {
				delay = null;
			}
		}
		
		//如果当前时间落在当日定位时间段的区间内，且用户当日尚未确认过定位规则，那么就返回true
		String[] tmp = prefs.getLocationStartTime().split(":");
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmp[0]));
		start.set(Calendar.MINUTE, Integer.parseInt(tmp[1]));
		start.set(Calendar.SECOND, 0);
		
		String stopTime = DateUtil.getCurDate() + " " + prefs.getLocationStopTime()+":00";
		Date stop = DateUtil.getDate(stopTime);
		
		if (start.get(Calendar.HOUR_OF_DAY) >= 1) {
			//一般情况：如果开始定位时间大于01:00，那么定位规则提示框的弹出时间就应早于开始定位时间一小时
			long t = start.getTimeInMillis() - 1 * 60 * 60 * 1000;
			if (current >= t && current < stop.getTime()) {
				return true;
			}
		}
		else {
			//特殊情况：如果开始定位时间早于01:00，那么只要在当日00:00以后就应弹出定位规则提示框
			start.set(Calendar.MINUTE, 0);
			if (current >= start.getTimeInMillis() && current < stop.getTime()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 根据类型判断是否要提示用户被动定位规则
	 * @return true 提示用户 false不提示用户
	 */
	public boolean isTipUser(){
		boolean flag  = true;
		int tipType = SharedPrefsBackstageLocation.getInstance(context).getLocationTipType();
		switch (tipType) {
		case 0:
			flag = isShowLocationRule();
			break;
		case 1://只提示一次 等于1的时候是有新规则下发的时候也要提示一次		
		case 2://只提示一次，有新规则下发也不再提示
			flag = isShowLocationRule();
			boolean isTip = SharedPreferencesUtil.getInstance(context).isTipUser();
			if (isTip) {
				flag = false;
			}
			if(!flag){ //不提示的话，此值改为true，以防止每日initialize更改
				SharedPrefsBackstageLocation.getInstance(context).setIsConfirmLocationRule(true);
			}
			break;
		case 3://不提示
			SharedPrefsBackstageLocation.getInstance(context).setIsConfirmLocationRule(true);
			flag = false;
			break;
		default:
			break;
		}
		return flag;
	}
	
	/**
	 * 根据本周定位规则判断今天是否定位
	 * 
	 * @return 如果今天应该执行定位任务，则返回true，否则返回false
	 */
	public boolean isTodayLocationByWeek(){
		Calendar current = Calendar.getInstance();
		int day = current.get(Calendar.DAY_OF_WEEK) - 1;// 获取今天是本周的第几天 从星期一开始
		day = day == 0 ? 7 : day;
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext());
		String rule = prefs.getLocationPeriodRule();
		int n = Integer.valueOf(rule,2)>>(7-day);
		if (n % 2 == 1) {
			JLog.d(TAG, "BackstageLocationManager.isTodayLocationByWeek() -> true");
			return true;
		}else{
			JLog.d(TAG, "BackstageLocationManager.isTodayLocationByWeek() -> false");
			return false;
		}
	}
	
	/**
	 * 本次定位完成后，更新下次定位时间
	 */
	public void updateNextLocation(){
		int interval = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).getLocationEachInterval();
		String nextTimeStr = DateUtil.addDate(new Date(), Calendar.MINUTE, interval);
		SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).setLocationNextTime(nextTimeStr);
	}
	
	/**
	 * 今天定位停止后，更新数据
	 */
	public void updateStopLocation(){
		Date startDate = getTodayLocationStartDate(null);
		if(startDate == null){
			return;
		}
		SharedPrefsBackstageLocation prefs = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext());
		
		String startDateStr = DateUtil.addDate(startDate, Calendar.DAY_OF_MONTH, 1);
		prefs.setLocationNextTime(startDateStr);
		prefs.setIsConfirmLocationRule(false);
		JLog.d(TAG, "stop更改下次定位时间："+startDateStr);
	}
	
	/**
	 * 初始化后台定位任务的相关属性，将下次定位时间设置为当前时间，并将用户是否已确认定位规则设置为false。
	 * 
	 * @param isResetConfirm 是否提示定位规则
	 */
	public void updateLocationRuleForInit(boolean isResetConfirm) {
		Date startDate = getTodayLocationStartDate(null);
		if(startDate == null){
			JLog.d(TAG, "init 中startDate=null");
			return;
		}
		SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).setLocationNextTime(DateUtil.getCurDateTime());
		
		if (isResetConfirm)
			SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).setIsConfirmLocationRule(false);
	}
	
	/**
	 * 用户点击定位提示对话框的Delay时，设置delay时间
	 */
	public void updateLocationRulePromptDateByDelay(){
		int interval = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).getLocationEachInterval();
		delay = Calendar.getInstance();
		delay.add(Calendar.MINUTE, interval);
	}
	
	/**
	 * 今天开始定位时间
	 * 
	 * @param current 当前时间
	 * @return 返回今天开始定位时间的Date对象
	 */
	public Date getTodayLocationStartDate(Calendar current){
		String startOnlyTime = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).getLocationStartTime();
		Date startDate = null;
		if(!TextUtils.isEmpty(startOnlyTime)){
			String []arr1 = startOnlyTime.split(":");
			startDate = getDate(arr1[0],arr1[1], current);
		}
		return startDate;
	}
	
	/**
	 * 今天停止定位时间
	 * 
	 * @return 返回今天结束定位时间的Date对象
	 */
	public Date getTodayLocationStopDate(){
		String stopOnlyTime = SharedPrefsBackstageLocation.getInstance(context.getApplicationContext()).getLocationStopTime();
		String []arr1 = stopOnlyTime.split(":");
		Date stopDate = getDate(arr1[0],arr1[1], null);
		return stopDate;
	}

	/**
	 * 根据小时和分钟参数获取当天自动定位开始时间的Date对象
	 * 
	 * @param hour 小时
	 * @param min 分钟
	 * @param current 如果为null，就用Calendar.getInstance()代替
	 * @return 返回自动定位开始时间的Date对象
	 */
	private Date getDate(String hour,String min, Calendar current){
		if (current == null)
			current = Calendar.getInstance();
		int year = current.get(Calendar.YEAR);
		int month = current.get(Calendar.MONTH);
		int day = current.get(Calendar.DAY_OF_MONTH);
		
		current.set(year, month, day, Integer.parseInt(hour), Integer.parseInt(min), 0);
				
		return current.getTime();
	}
}
