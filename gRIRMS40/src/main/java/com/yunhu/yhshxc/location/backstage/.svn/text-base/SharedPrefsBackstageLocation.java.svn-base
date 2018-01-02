package com.yunhu.yhshxc.location.backstage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于存取后台定位相关状态属性的SharedPreferences
 * 
 * @version 2013.5.21
 * @author wangchao
 *
 */
public class SharedPrefsBackstageLocation {

	/**
	 * 用于保存SharedPreferences
	 */
	private static Editor saveEditor;
	

	/**
	 * 用于获取SharedPreferences
	 */
	private static SharedPreferences saveInfo;
	
	/**
	 * 开始定位时间常量
	 */
	private final String LOCATION_START_TIME = "location_start_time";
	
	/**
	 * 定位停止时间常量
	 */
	private final String LOCATION_STOP_TIME = "location_stop_time";
	
	/**
	 * 每次定位时间间隔常量
	 */
	private final String LOCATION_EACH_INTERVAL = "location_each_interval";
	
	/**
	 * 一周期哪几天需要定位的规则常量
	 */
	private final String LOCATION_PERIOD_RULE = "location_period_rule";
	
	/**
	 * 定位是否启用常量
	 */
	private final String LOCATION_IS_AVAILABLE = "location_is_available";
	
	/**
	 * 下次定位时间常量
	 */
	private final String LOCATION_NEXT_TIME = "location_next_time";
	
	/**
	 * 初始化日期常量
	 */
	private final String LOCATION_INITIALIZED_DATE = "Location_init_date";
	
	/**
	 * 是否确认定位规则常量
	 */
	private final String LOCATION_RULE_IS_CONFIRM = "is_confirm_location_rule";
	
	/**
	 * 上次定位的位置常量
	 */
	private final String LOCATION_LAST = "last";
	
	/**
	 * 轨迹定位的允许栅栏值的常量
	 */
	private final String TRACE_FENCE = "trace_fence";
	
	/**
	 * 手机被动定位前是否提示用户
	 */
	private  final String LOCATION_TIP_TYPE = "loc_tips";
	

	/**
	 * 单例模式所持有的唯一实例化对象
	 */
	private static SharedPrefsBackstageLocation spUtil = new SharedPrefsBackstageLocation();
	private static Context mContext;

	/**
	 * 以单例模式获取当前类的实例化对象
	 * 
	 * @param context 上下文对象
	 * @return 当前类的实例化对象
	 */
	public static SharedPrefsBackstageLocation getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_bsl",
					Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	/**
	 * 构造方法
	 */
	private SharedPrefsBackstageLocation() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_bsl",
					Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}
	
	/**
	 * 清除所有与后台定位相关的状态属性
	 */
	public void clearAll(){
		saveEditor.clear();
		saveEditor.commit();
	}

	/*---------------------定位规则-------------------------------------*/
	
	/**
	 * 保存开始定位时间
	 * 
	 * @param startTime 开始定位时间
	 */
	public void setLocationStartTime(String startTime) {
		saveEditor.putString(LOCATION_START_TIME, startTime);
		saveEditor.commit();
	}

	/**
	 * 获取开始定位时间
	 * 
	 * @return 返回开始定位时间
	 */
	public String getLocationStartTime() {
		return saveInfo.getString(LOCATION_START_TIME, "");
	}
	
	/**
	 * 保存定位停止时间
	 * 
	 * @param stopTime 定位停止时间
	 */
	public void setLocationStopTime(String stopTime) {
		saveEditor.putString(LOCATION_STOP_TIME, stopTime);
		saveEditor.commit();
	}

	/**
	 * 获取定位停止时间
	 * 
	 * @return 返回定位停止时间
	 */
	public String getLocationStopTime() {
		return saveInfo.getString(LOCATION_STOP_TIME, "");
	}
	
	/**
	 * 保存下次定位时间
	 * 
	 * @param nextTime 下次定位时间
	 */
	public void setLocationNextTime(String nextTime) {
		saveEditor.putString(LOCATION_NEXT_TIME, nextTime);
		saveEditor.commit();
	}

	/**
	 * 获取下次定位时间
	 * 
	 * @return 返回下次定位时间
	 */
	public String getLocationNextTime() {
		return saveInfo.getString(LOCATION_NEXT_TIME, "");
	}
	
	/**
	 * 保存每次定位时间间隔
	 * 
	 * @param interval 每次定位时间间隔
	 */
	public void setLocationEachInterval(int interval) {
		saveEditor.putInt(LOCATION_EACH_INTERVAL, interval);
		saveEditor.commit();
	}

	/**
	 * 获取每次定位时间间隔
	 * 
	 * @return 返回每次定位时间间隔
	 */
	public int getLocationEachInterval() {
		return saveInfo.getInt(LOCATION_EACH_INTERVAL,10);
	}
	
	/**
	 * 保存一周期哪几天需要定位的规则
	 * 
	 * @param rule 一周期哪几天需要定位的规则
	 */
	public void setLocationPeriodRule(String rule) {
		saveEditor.putString(LOCATION_PERIOD_RULE, rule);
		saveEditor.commit();
	}

	/**
	 * 获取一周期哪几天需要定位的规则
	 * 
	 * @return 返回一周期哪几天需要定位的规则
	 */
	public String getLocationPeriodRule() {
		return saveInfo.getString(LOCATION_PERIOD_RULE, "");
	}
	
	/**
	 * 获取定位是否启用/禁用
	 * 
	 * @return true 启用   false 禁用
	 */
	public Boolean getLocationIsAvailable() {
		return saveInfo.getBoolean(LOCATION_IS_AVAILABLE, false);
	}

	/**
	 * 保存定位是否启用/禁用
	 * 
	 * @param flag true 启用   false 禁用
	 */
	public void setLocationIsAvailable(Boolean flag) {
		saveEditor.putBoolean(LOCATION_IS_AVAILABLE, flag);
		saveEditor.commit();
	}

	/**
	 * 每天第一次启动程序时须执行一次初始化任务，初始化任务负责把LOCATION_RULE_IS_CONFIRM字段重置为false，
	 * 否则前一日如果因为各种原因未执行BackstageLocationManager.updateStopLocation()的话，LOCATION_RULE_IS_CONFIRM字段可能始终为true，导致无法弹出规则提示窗口
	 * 
	 * @return 上次执行初始化的日期（YYYYMMDD格式）
	 */
	public int getLocationInitializedDate() {
		return saveInfo.getInt(LOCATION_INITIALIZED_DATE, 0);
	}

	/**
	 * 保存上次执行初始化的日期
	 * 
	 * @param date 上次执行初始化的日期（YYYYMMDD格式）
	 */
	public void setLocationInitializedDate(int date) {
		saveEditor.putInt(LOCATION_INITIALIZED_DATE, date);
		saveEditor.commit();
	}
	
	/**
	 * 保存是否确认定位规则
	 * 
	 * @param isConfirm 如果已经确认过，则为true，否则为false
	 */
	public void setIsConfirmLocationRule(boolean isConfirm) {
		saveEditor.putBoolean(LOCATION_RULE_IS_CONFIRM, isConfirm);
		saveEditor.commit();
	}
	
	/**
	 * 获取是否确认定位规则
	 * 
	 * @return 如果已经确认过，则返回true，否则返回false
	 */
	public boolean getIsConfirmLocationRule() {
		return saveInfo.getBoolean(LOCATION_RULE_IS_CONFIRM, false);
	}

//	/**
//	 * 获取上次定位的位置
//	 * 
//	 * @return 返回上次定位的位置
//	 */
//	public String getLastLocation() {
//		return saveInfo.getString(LOCATION_LAST,  "");
//	}
//	
//	/**
//	 * 保存上次定位的位置
//	 * 
//	 * @param last 上次定位的位置
//	 */
//	public void saveLastLocation(String last) {
//		saveEditor.putString(LOCATION_LAST, last);
//		saveEditor.commit();
//	}
	
	/*----------------------------------------------------------*/
	
	/**
	 * 获取轨迹定位的允许栅栏值
	 * 
	 * @return 返回允许栅栏值
	 */
	public Integer getTraceFence() {
		return saveInfo.getInt(TRACE_FENCE,  100);
	}
	
	/**
	 * 保存轨迹定位的允许栅栏值
	 * 
	 * @param traceFence 允许栅栏值
	 */
	public void saveTraceFence(Integer traceFence) {
		saveEditor.putInt(TRACE_FENCE, traceFence);
		saveEditor.commit();
	}
	
	/**
	 * 获取被动定位是否提示用户类型
	 * @return
	 */
	public int getLocationTipType(){
		return saveInfo.getInt(LOCATION_TIP_TYPE, 0);
	}
	
	/**
	 * 存储被动定位提示用户类型
	 */
	public void saveLocationTipType(int type){
		saveEditor.putInt(LOCATION_TIP_TYPE, type);
		saveEditor.commit();
	}
	
}
