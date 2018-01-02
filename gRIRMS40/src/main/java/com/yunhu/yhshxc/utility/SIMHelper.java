package com.yunhu.yhshxc.utility;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMHelper {

	public static String getIMSI(Context localContext) {
		TelephonyManager telMgr = (TelephonyManager) localContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telMgr.getSubscriberId();
		return imsi;
	}

	public static String getIMEI(Context localContext) {
		TelephonyManager telMgr = (TelephonyManager) localContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telMgr.getDeviceId();
		int index = imsi.indexOf(",");
		if(index > -1){
			imsi = imsi.substring(0,index);
		}
		return imsi;
	}
	
	public static String checkType(Context localContext){
		String imsi=getIMSI(localContext);
		if(imsi!=null){
	        if(imsi.startsWith("46000") || imsi.startsWith("46002")){//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
	        //中国移动
	        	return "YD";
	        }else if(imsi.startsWith("46001")){
	        //中国联通
	        	return "LT";
	        }else if(imsi.startsWith("46003")){
	        	return "DX";
	        //中国电信
	        }
		}
		return "";
	}
}
