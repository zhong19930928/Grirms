package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtilForTable {

	private static Editor saveEditor;

	private static SharedPreferences saveInfo;


	private static SharedPreferencesUtilForTable spUtil = new SharedPreferencesUtilForTable();
	private static Context mContext;

	public static SharedPreferencesUtilForTable getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_table", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}
	
	public void setTableSelect(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getTableSelect(String key){
		return saveInfo.getString(key, "");
	}
	
	public void clear(String key) {
		saveEditor.remove(key);
		saveEditor.commit();
	}
	

	private SharedPreferencesUtilForTable() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_table", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public void clearAll(){
		saveEditor.clear();
		saveEditor.commit();
	}
	
}
