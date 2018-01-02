package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * 获取apk的 名称  code 版本号的工具类
 * @author jishen
 */
public class ApplicationHelper {

	public static class AppInfo
	{
		public String appName="";//名称
		public String appVersionCode="";//code
		public String appVersionName="";//版本号
	}
	
	public static AppInfo getApplicationName(Context context)
	{
		PackageManager manager = context.getPackageManager();
		try {  
		   PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		   String packageName = info.packageName;//apk 包名
		   int versionCode = info.versionCode;
		   String versionName = info.versionName;  
		   
		   AppInfo result=new AppInfo();
		   result.appName=packageName;
		   result.appVersionCode=Integer.toString(versionCode);
		   result.appVersionName=versionName;
		   
		   return result;

		} catch (NameNotFoundException e) { 
			Log.e("@@@", e.getMessage());
		}
		 return new AppInfo();
	}
	
}
