package com.yunhu.yhshxc.attendance.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesForLeaveUtil {

	private static Editor saveEditor;
	private static SharedPreferences saveInfo;
	private static SharedPreferencesForLeaveUtil spUtil = new SharedPreferencesForLeaveUtil();
	private static Context mContext;
	
	private final String IS_LEAVE = "isLeave"; // 是否有请假 1：有 0：无
	private final String IS_QUERY="isQuery";// 是否查询 1：有 0：无
	private final String IS_INSERT="isInsert";// 是否上报 1：有 0：无
	private final String IS_UPDATE="isUpdate";//是否修改 1：有 0：无
	private final String IS_AUDIT="isAudit";// 是否审核 1：有 0：无
	private final String AUTITBTN = "autitBtn";
	
	
	private SharedPreferencesForLeaveUtil() {}

	public static SharedPreferencesForLeaveUtil getInstance(Context context) {
		if (saveInfo == null && context != null) {
			mContext = context.getApplicationContext();
			saveInfo = mContext.getSharedPreferences("is_leave", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}
	
	public void clearAll() {
		saveEditor.clear();
		saveEditor.commit();
	}

	public void clear(String key) {
		saveEditor.remove(key);
		saveEditor.commit();
	}
	
	public void setAutitBtn(String timestamp){
		saveEditor.putString(AUTITBTN, timestamp);
		saveEditor.commit();
	}
	
	public String getAutitBtn(){
		return saveInfo.getString(AUTITBTN, "");
	}
	

	public String getIS_LEAVE() {
		return saveInfo.getString(IS_LEAVE, "0");
	}
	public void setIS_LEAVE(String value){
		saveEditor.putString(IS_LEAVE,value);
		saveEditor.commit();
	}

	public String getIS_QUERY() {
		return saveInfo.getString(IS_QUERY, "0");
	}
	public void setIS_QUERY(String value){
		saveEditor.putString(IS_QUERY,value);
		saveEditor.commit();
	}

	public String getIS_INSERT() {
		return saveInfo.getString(IS_INSERT, "0");
	}
	public void setIS_INSERT(String value){
		saveEditor.putString(IS_INSERT,value);
		saveEditor.commit();
	}

	public String getIS_UPDATE() {
		return saveInfo.getString(IS_UPDATE, "0");
	}
	public void setIS_UPDATE(String value){
		saveEditor.putString(IS_UPDATE,value);
		saveEditor.commit();
	}

	public String getIS_AUDIT() {
		return saveInfo.getString(IS_AUDIT, "0");
	}
	public void setIS_AUDIT(String value){
		saveEditor.putString(IS_AUDIT,value);
		saveEditor.commit();
	}
	
	
	
}
