package com.yunhu.yhshxc.attendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于存取后台定位相关状态属性的SharedPreferences
 * 
 * @version 2014.8.21
 * @author 侯宇
 *
 */
public class SharedPrefsAttendanceUtil {
	
	private final String ATTENDANCE_BACKSTAGE_LOCATION_INTERVAL_TIME = "attendance_backstage_location_interval_time"; // 查岗间隔时间
	private final String ATTENDANCE_BACKSTAGE_LOCATION_EXP_TIME = "attendance_backstage_location_exp_time"; // 例外时间段（格式 12:00,1.0|14:00,0.5）
	private final String ATTENDANCE_BACKSTAGE_LOCATION_LAST_TIME = "attendance_backstage_location_last_time";//后台最后一次定位时间

	/**1：考勤上班打卡启动服务、2：固定时间开始服务*/
	private final String ATTENDANCE_BACKSTAGE_LOCATION_SERVICE_FLG = "attendance_backstage_location_service_flg";
	
	/** 周几启动服务（格式：周1、2、3、5启动查岗服务时，存储为 『1,2,3,5』）、*/
	private final String ATTENDANCE_BACKSTAGE_LOCATION_WEEKS = "attendance_backstage_location_weeks"; //
	
	/**查岗时间段（格式：9点开始9.5个小时，『09:00,9.5）*/
	private final String ATTENDANCE_BACKSTAGE_LOCATION_ATTEND_TIME = "attendance_backstage_location_attend_time";
	
	
	private final String ATTEND_FUNC = "attend_func";//（0：不要排班、1：要排班）
	private final String PAI_FLG = "pai_flg";//当排班时间小于等于当前时间 1、无修改权限 2、web端可修改 3、phone可修改 4、都可修改
	/**
	 * 用于保存SharedPreferences
	 */
	private static Editor saveEditor;

	/**
	 * 用于获取SharedPreferences
	 */
	private static SharedPreferences saveInfo;
	

	/**
	 * 单例模式所持有的唯一实例化对象
	 */
	private static SharedPrefsAttendanceUtil spUtil = new SharedPrefsAttendanceUtil();
	private static Context mContext;

	/**
	 * 以单例模式获取当前类的实例化对象
	 * 
	 * @param context 上下文对象
	 * @return 当前类的实例化对象
	 */
	public static SharedPrefsAttendanceUtil getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_attendance",
					Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	/**
	 * 构造方法
	 */
	private SharedPrefsAttendanceUtil() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_attendance",
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

	/**
	 * 查岗间隔时间
	 * @param intervalTime
	 */
	public void setAttendanceBackstageLocationIntervalTime(int intervalTime){
		saveEditor.putInt(ATTENDANCE_BACKSTAGE_LOCATION_INTERVAL_TIME, intervalTime);
		saveEditor.commit();
	}
	
	public int getAttendanceBackstageLocationIntervalTime(){
		return saveInfo.getInt(ATTENDANCE_BACKSTAGE_LOCATION_INTERVAL_TIME, 0);
	}
	
	/**
	 * 例外时间段（格式 12:00,1.0|14:00,0.5）
	 * @param wait
	 */
	public void setAttendanceBackstageLocationExpTime(String expTime){
		saveEditor.putString(ATTENDANCE_BACKSTAGE_LOCATION_EXP_TIME, expTime);
		saveEditor.commit();
	}
	
	public String getAttendanceBackstageLocationExpTime(){
		return saveInfo.getString(ATTENDANCE_BACKSTAGE_LOCATION_EXP_TIME,null);
	}
	
	/**
	 * 后台定位最后一次定位时间
	 * @param wait
	 */
	public void setAttendanceBackstageLocationLastTime(String lastTime){
		saveEditor.putString(ATTENDANCE_BACKSTAGE_LOCATION_LAST_TIME,lastTime);
		saveEditor.commit();
	}
	
	public String getAttendanceBackstageLocationLastTime(){
		return saveInfo.getString(ATTENDANCE_BACKSTAGE_LOCATION_LAST_TIME, null);
	}
	
	public void setAttendanceBackstageLocationServiceFlg(int serviceFlg){
		saveEditor.putInt(ATTENDANCE_BACKSTAGE_LOCATION_SERVICE_FLG,serviceFlg);
		saveEditor.commit();
	}
	
	public int getAttendanceBackstageLocationServiceFlg(){
		return saveInfo.getInt(ATTENDANCE_BACKSTAGE_LOCATION_SERVICE_FLG,1);
	}
	
	public void setAttendanceBackstageLocationWeeks(String weeks){
		saveEditor.putString(ATTENDANCE_BACKSTAGE_LOCATION_WEEKS,weeks);
		saveEditor.commit();
	}
	
	public String getAttendanceBackstageLocationWeeks(){
		return saveInfo.getString(ATTENDANCE_BACKSTAGE_LOCATION_WEEKS, null);
	}
	
	public void setAttendanceBackstageLocationAttendTime(String attendTime){
		saveEditor.putString(ATTENDANCE_BACKSTAGE_LOCATION_ATTEND_TIME,attendTime);
		saveEditor.commit();
	}
	
	public String getAttendanceBackstageLocationAttendTime(){
		return saveInfo.getString(ATTENDANCE_BACKSTAGE_LOCATION_ATTEND_TIME, null);
	}
	
	public void setAttendFunc(String value){
		saveEditor.putString(ATTEND_FUNC,value);
		saveEditor.commit();
	}
	
	public String getAttendFunc(){
		return saveInfo.getString(ATTEND_FUNC, "0");
	}
	
	public void setPaiFlg(String value){
		saveEditor.putString(PAI_FLG,value);
		saveEditor.commit();
	}
	
	public String getPaiFlg(){
		return saveInfo.getString(PAI_FLG, "1");
	}
}
