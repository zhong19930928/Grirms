package com.yunhu.yhshxc.location2;

import com.yunhu.yhshxc.location.ReceiveLocationListener;

import android.content.Context;

public class LocationFactoy {

	private Context context;
	private LoctionService loctionUtils;
	private ReceiveLocationListener lisener;
	public LocationFactoy(Context context,ReceiveLocationListener lisener) {
		this.context = context;
		this.lisener = lisener;
		loctionUtils = new LoctionService(context);
	}
	/**
	 * 入口 GPS
	 * @param context
	 */
	public void startLoctionGPS() {
		loctionUtils.startLoctionByGPS(lisener,false);
	}
	
	/**
	 * 入口 WIFI
	 */
	public void sartLocationWiFi(){
		loctionUtils.startLocationByWifi(lisener);
	}
	
	/**
	 * 入口 混合定位
	 */
	public void startLocationHunHe(){
		loctionUtils.startLocatByHunH(lisener);
	}
	
	/**
	 * 主动定位  GPS
	 */
	public void startLocationG(){
		loctionUtils.startLocatG(lisener,true);
	}
	
	/**
	 * 主动定位 wifi
	 */
	public void startLocationWF(){
		loctionUtils.startLocatWF(lisener,true);
	}
	
	/**
	 * 主动定位 混合定位
	 */
	public void startLocationHH(){
		loctionUtils.startLocatHH(lisener, true);
	}
}
