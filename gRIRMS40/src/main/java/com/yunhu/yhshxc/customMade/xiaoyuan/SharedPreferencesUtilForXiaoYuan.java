package com.yunhu.yhshxc.customMade.xiaoyuan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtilForXiaoYuan {

	private static Editor saveEditor;

	private static SharedPreferences saveInfo;

	private final String XIAO_YUAN = "XIAO_YUAN";
	
	private static SharedPreferencesUtilForXiaoYuan spUtil = new SharedPreferencesUtilForXiaoYuan();
	private static Context mContext;

	public static SharedPreferencesUtilForXiaoYuan getInstance(Context context) {
		mContext = context.getApplicationContext();
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("xiaoyuan", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	private SharedPreferencesUtilForXiaoYuan() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("xiaoyuan", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public void clearAll(){
		saveEditor.clear();
		saveEditor.commit();
	}
	
	public void saveXYStoreProperty(String property) {
		saveEditor.putString(XIAO_YUAN, property);
		saveEditor.commit();
	}
	
	public String getXYStoreProperty(){
		return saveInfo.getString(XIAO_YUAN, null);
	}
	
	
}
