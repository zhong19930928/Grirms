package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil2 {

	private static Editor saveEditor;

	private static SharedPreferences saveInfo;
	private final String IS_SHOW_PHONE_REPORT = "IS_SHOW_PHONE_REPORT";// 是否显示主菜单底部的报表选项
	private final String REPORT_WHERE = "REPORT_WHERE";
	private final String REPORT_WHERE2 = "REPORT_WHERE2";
	private final String IS_UPLOAD_LOG = "IS_UPLOAD_LOG";// 上传log开关,默认false
	private final String STORE_INFO_ID = "store_info_id";// 店面信息id(店面信息的模块id)
	// private final String STORE_INFO_OPERATION =
	// "store_info_operation";//店面信息操作权限(0没有,1查看,2修改)
	private final String IS_ANOMALY = "is_anomaly";// 是否异常退出的标识
	private final String MENU_ID = "menu_id";// 存储异常退出时所在的模块的MenuId;
	private final String WORKPLAN_TIP_TIME = "workplan_tiptime";// 上一次工作计划提醒的时间
	private final String WORKPLAN_TIP_RULE = "workplan_tip_rule";// 工作计划提醒时间段
	private final String SLIDE_IMAGE_DOWN = "slide_image_down";// 首页轮播图是否下载成功

	private static SharedPreferencesUtil2 spUtil = new SharedPreferencesUtil2();
	private static Context mContext;

	public static SharedPreferencesUtil2 getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms2", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	private SharedPreferencesUtil2() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms2", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public void clearAll() {
		saveEditor.clear();
		saveEditor.commit();
	}

	public int getMenuId() {
		return saveInfo.getInt(MENU_ID, -1);
	}

	public void saveMenuId(int value) {
		saveEditor.putInt(MENU_ID, value);
		saveEditor.commit();
	}

	public boolean getIsAnomaly() {
		return saveInfo.getBoolean(IS_ANOMALY, false);
	}

	public void saveIsAnomaly(boolean isanomaly) {
		saveEditor.putBoolean(IS_ANOMALY, isanomaly);
		saveEditor.commit();
	}

	public void saveReportWhere(String where) {
		saveEditor.putString(REPORT_WHERE, where);
		saveEditor.commit();
	}

	public String getReportWhere() {
		return saveInfo.getString(REPORT_WHERE, null);
	}

	public void saveReportWhere2(String where) {
		saveEditor.putString(REPORT_WHERE2, where);
		saveEditor.commit();
	}

	public String getReportWhere2() {
		return saveInfo.getString(REPORT_WHERE2, null);
	}

	public void saveIsUploadLog(boolean isUploadLog) {
		saveEditor.putBoolean(IS_UPLOAD_LOG, isUploadLog);
		saveEditor.commit();
	}

	public boolean getIsUploadLog() {
		return saveInfo.getBoolean(IS_UPLOAD_LOG, false);
	}

	public void saveStoreInfoId(int storeInfoId) {
		saveEditor.putInt(STORE_INFO_ID, storeInfoId);
		saveEditor.commit();
	}

	public int getStoreInfoId() {
		return saveInfo.getInt(STORE_INFO_ID, 0);
	}

	public void savePhoneReport(boolean isShowReport) {
		saveEditor.putBoolean(IS_SHOW_PHONE_REPORT, isShowReport);
		saveEditor.commit();
	}

	public boolean getPhoneReport() {
		return saveInfo.getBoolean(IS_SHOW_PHONE_REPORT, false);
	}

	/**
	 * 保存上一次提醒的时间
	 * 
	 * @param workplantime
	 */
	public void saveWorkPlanTipTime(String workplantime) {
		saveEditor.putString(WORKPLAN_TIP_TIME, workplantime);
		saveEditor.commit();
	}

	public String getWorkPlanTipTime() {
		return saveInfo.getString(WORKPLAN_TIP_TIME, "");
	}

	/**
	 * 工作计划提醒时间段
	 * 
	 * @param workplanrule
	 */
	public void saveWorkPlanTipRule(String workplanrule) {
		saveEditor.putString(WORKPLAN_TIP_RULE, workplanrule);
		saveEditor.commit();
	}

	public String getWorkPlanTipRule() {
		return saveInfo.getString(WORKPLAN_TIP_RULE, "");
	}

	/**
	 * 标识是否下载完成了首页轮播图
	 * @param isDown
	 */
	public  void setIsSlideImageDown(boolean isDown){
		saveEditor.putBoolean(SLIDE_IMAGE_DOWN,isDown);
		saveEditor.commit();
	}
	public  boolean getIsSlideImageDown(){
		return saveInfo.getBoolean(SLIDE_IMAGE_DOWN,false);
	}
}
