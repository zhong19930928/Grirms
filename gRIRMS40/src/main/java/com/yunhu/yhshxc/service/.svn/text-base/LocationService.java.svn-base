package com.yunhu.yhshxc.service;

import gcg.org.debug.JLog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 暂时不使用此类
 * @author david.hou
 *
 */
public class LocationService extends Service implements ReceiveLocationListener{
	private final String TAG = "LocationService";
//	private LocationFactory location;
	private LocationFactoy factory;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		JLog.d(TAG,"LocationService==========>start");
		startLocation();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_REDELIVER_INTENT;
	}
	
	private void startLocation(){
//		location = new LocationFactory(this);
//		location.startNewLocation(this,true);
		factory = new LocationFactoy(this, this);
	}
	
	@Override
	public void onDestroy() {
//		location.stopLocation();
		
		super.onDestroy();
	}

	@Override
	public void onReceiveResult(LocationResult result) {
		if(result!=null && result.isStatus()){
			String lon = result.getLongitude()+"";
			String lat = result.getLatitude()+"";
			String address = result.getAddress();
			String time = result.getLcationTime();
			String type = PublicUtils.receiveLocationTypeByType(result.getLocType());
			JLog.d(TAG, "经度："+ lon);
			JLog.d(TAG, "纬度："+ lat);
			JLog.d(TAG, "地址" + (address==null?"":address));
			JLog.d(TAG, "定位时间" + time);
			JLog.d(TAG, "定位类型："+ type);
			JLog.d(TAG, "-------------后台定位结束-----------------");
		}else{
			JLog.d(TAG, "本次定位失败");
		}
		stopSelf();
	}
	

}
