package com.yunhu.yhshxc.utility;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class NetSettingUtil {
	
	private static final String TAG = "NetSettingUtil";

//	private class APN {
//		String _id;
//		String name;
//		String numeric;
//		String mcc;
//		String mnc;
//		String apn;
//		String user;
//		String server;
//		String password;
//		String proxy;
//		String port;
//		String mmsproxy;
//		String mmsport;
//		String mmsc;
//		String authtype;
//		String type;
//		String current;
//	}
	
//	public static boolean openCTWAP(Context context) {
//		String proxyHost = Proxy.getDefaultHost();
//		if (proxyHost == null || proxyHost.equals("")) {
//			Uri uri = Uri.parse("content://telephony/carriers");
//			Cursor cr = context.getContentResolver().query(uri, null,"apn='ctwap' or apn='CTWAP' or proxy='10.0.0.200'", null,null);
//			if (cr != null && cr.moveToNext()) {
//				String id = cr.getString(cr.getColumnIndex("_id"));
//				uri = Uri.parse("content://telephony/carriers/preferapn");
//				ContentResolver resolver = context.getContentResolver();
//				ContentValues values = new ContentValues();
//				values.put("apn_id", Integer.parseInt(id));
//				resolver.update(uri, values, null, null);
//			}
//			if (!cr.isClosed())
//				cr.close();// 注意游标关闭
//			return true;
//		}else{
//			return false;
//		}
//	}
	
	public static boolean openGPS(Context context) {
		LocationManager alm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent intent = new Intent();
			intent.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
			intent.addCategory("android.intent.category.ALTERNATIVE");
			intent.setData(Uri.parse("custom:3"));// 0:打开wifi,1:BRIGHTNESS亮度调节,3:gps
			try {
				PendingIntent.getBroadcast(context, 0, intent, 0).send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * 关闭WIFI
	 */
	public static void closeWifi(Context context) {
		WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}
	
	public static boolean isOpenWifi(Context context){
		WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean isConnectMobileNetwork(Context context){   
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(null!=networkInfo){
        	if (networkInfo.isConnectedOrConnecting()) {  
            	return true;
            } else {             
            	return false;
            }  
        }
        return false;
        
    }
	
	public static boolean isConnectWifiNetwork(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
        if (wifiInfo.isConnectedOrConnecting()) {  
        	return true;
        } else {             
        	return false;
        }  
        
        
        
        
        
        
	}
	
	/**
	 * 
	 * @param context
	 * @return 0 表示通过  2表示提示开启网络
	 */
	public static int isPassByNetwork(Context context){
		if (!TextUtils.isEmpty(PublicUtils.receivePhoneNO(context))){//如果有电话号码的话直接让通过
			return 0;
		}
		boolean isOpenWifi = isConnectWifiNetwork(context);
		boolean isOpen3G = isConnectMobileNetwork(context);
		if(!isOpenWifi && !isOpen3G){
			return 2;
		}else{
			return 0;
		}
	}
	
}
