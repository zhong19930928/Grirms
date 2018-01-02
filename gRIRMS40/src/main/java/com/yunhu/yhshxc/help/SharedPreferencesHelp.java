package com.yunhu.yhshxc.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于通过SharedPreferences存取帮助相关的各种属性
 * 
 * @version 2013.5.22
 *
 */
public class SharedPreferencesHelp {
	/**
	 * 临时版本值在SharedPreferences中的Key
	 */
	private final String HELP_VERSION_TEMP = "help_version_temp";
	
	/**
	 * 版本值在SharedPreferences中的Key
	 */
	private final String HELP_VERSION = "help_version";
	
	/**
	 * 公司ID在SharedPreferences中的Key
	 */
	private final String LOGIN_ID="login_id";
	
	/**
	 * 常见问题的内容在SharedPreferences中的Key
	 */
	private final String QA = "QA";

	
	/**
	 * 用于保存SharedPreferences属性
	 */
	private static Editor saveEditor;

	/**
	 * SharedPreferences对象
	 */
	private static SharedPreferences saveInfo;
	
	/**
	 * 单例模式的唯一对象
	 */
	private static SharedPreferencesHelp spUtil = new SharedPreferencesHelp();
	private static Context mContext;

	/**
	 * 以单例模式获取当前类的实例化对象
	 * 
	 * @param context 上下文对象
	 * @return 当前类的实例化对象
	 */
	public static SharedPreferencesHelp getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_help", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	/**
	 * 构造方法
	 */
	private SharedPreferencesHelp() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms_help", Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	/**
	 * 清空所有数据
	 */
	public void clearAll(){
		saveEditor.clear();
		saveEditor.commit();
	}
	
	/**
	 * 保存帮助的版本
	 * 
	 * @param version 帮助的版本
	 */
	public void saveHelpVersion(String version) {
		saveEditor.putString(HELP_VERSION, version);
		saveEditor.commit();
	}
	
	/**
	 * 获取帮助的版本
	 * 
	 * @return 返回帮助的版本
	 */
	public String getHelpVersion(){
		return saveInfo.getString(HELP_VERSION, "");
	}
	
	/**
	 * 保存帮助的临时版本
	 * 
	 * @param version 帮助的临时版本
	 */
	public void saveHelpVersionTemp(String version) {
		saveEditor.putString(HELP_VERSION_TEMP, version);
		saveEditor.commit();
	}
	
	/**
	 * 获取帮助的临时版本
	 * 
	 * @return 返回帮助的临时版本
	 */
	public String getHelpVersionTemp(){
		return saveInfo.getString(HELP_VERSION_TEMP, null);
	}
	
	/**
	 * 保存帮助中常见问题的内容
	 * 
	 * @param qa json格式常见问题的内容
	 */
	public void saveQA(String qa) {
		saveEditor.putString(QA, qa);
		saveEditor.commit();
	}
	
	/**
	 * 获取帮助中常见问题的内容
	 * 
	 * @return 返回json格式常见问题的内容
	 */
	public String getQA(){
		return saveInfo.getString(QA, null);
	}
	
	/**
	 * 获取帮助的公司ID
	 * 
	 * @return 返回帮助的公司ID
	 */
	public String getLoginId() {
		return saveInfo.getString(LOGIN_ID, "");
	}
	
	/**
	 * 保存帮助的公司ID
	 * 
	 * @return 公司ID
	 */
	public void setLoginId(String loginId) {
		saveEditor.putString(LOGIN_ID, loginId);
		saveEditor.commit();
	}
	
	
}
