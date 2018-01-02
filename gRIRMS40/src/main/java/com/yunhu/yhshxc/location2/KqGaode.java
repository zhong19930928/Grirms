package com.yunhu.yhshxc.location2;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import gcg.org.debug.JLog;

//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
//import com.amap.api.services.geocoder.RegeocodeQuery;
//import com.amap.api.services.geocoder.RegeocodeResult;

public class KqGaode {
	private final String TAG = "KqGaode";
	private Context context;
	private Handler mHander;
	private int type;
	private int startType;
	private boolean isZhudong;
	GeocodeSearch geocoderSearch;
	//高德定位
		// 声明AMapLocationClient类对象
	public AMapLocationClient mGDLocationClient = null;
		// 声明mLocationOption对象
	public AMapLocationClientOption mGDLocationOption = null;
	
	public KqGaode(Context context, Handler mHander) {
		this.context = context;
		this.mHander = mHander;
		// 初始化定位
		mGDLocationClient = new AMapLocationClient(context.getApplicationContext());
	}
	Message message;
	Message message2;
	LocationResult info;
	public void startGD(int mode, long outTime,int type,int startType,boolean isZhudong) {
		this.type = type;
		this.startType = startType;
		this.isZhudong = isZhudong;
		
				// 设置定位回调监听
		mGDLocationClient.setLocationListener(mLocationListener);
				// 初始化定位参数
		mGDLocationOption = new AMapLocationClientOption();
				// 设置是否返回地址信息（默认返回地址信息）
		mGDLocationOption.setNeedAddress(true);
				// 设置是否只定位一次,默认为false
		mGDLocationOption.setOnceLocation(true);
				// 设置是否强制刷新WIFI，默认为强制刷新
		mGDLocationOption.setWifiActiveScan(true);
				// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mGDLocationOption.setMockEnable(false);
				// 设置定位间隔,单位毫秒,默认为2000ms
//		mGDLocationOption.setInterval(500*60*1000);
		mGDLocationOption.setHttpTimeOut(outTime);
		mGDLocationOption.setLocationCacheEnable(false);//默认是缓存模式true
//		mGDLocationOption.setKillProcess(arg0)
				/**
				 * 定位模式，目前支持三种定位模式 
				 * 
				 * Hight_Accuracy 高精度定位模式：在这种定位模式下，将同时使用高德网络定位和GPS定位,优先返回精度高的定位  
				 * 
				 * Battery_Saving  低功耗定位模式：在这种模式下，将只使用高德网络定位 
				 * 
				 * Device_Sensors  仅设备定位模式：在这种模式下，将只使用GPS定位。
				 */
		if(mode == 0){
			mGDLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		}else if(mode == 1){
			mGDLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		}else if(mode == 2){
			mGDLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);
		}
		
				// 给定位客户端对象设置定位参数
		mGDLocationClient.setLocationOption(mGDLocationOption);
		mGDLocationClient.startLocation();
		message = mHander.obtainMessage();
		info = new LocationResult();
		message2 = mHander.obtainMessage();
		message2.what = 1001;
		message2.obj = isZhudong;
		message2.arg1 = type;
		mHander.sendMessageDelayed(message2, 100*1000);
				
	}
	
	public void endGD(){
		mGDLocationClient.unRegisterLocationListener(mLocationListener);
		if(mGDLocationClient.isStarted()){
			mGDLocationClient.stopLocation();
		}
	}
	public void remove() {
		if (mHander.hasMessages(1001)) {
			synchronized (KqGaode.class) {
				if (mHander.hasMessages(1001)) {
					try {
						mHander.removeMessages(1001);	
					} catch (Exception e) {
						Log.e("aaa", e.getMessage());
						JLog.e(e);
					}
					
				}
			}
		}
	}
	
	// 声明定位回调监听器
		public AMapLocationListener mLocationListener = new AMapLocationListener() {

			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				JLog.d("高德   ============");
				try {
					if (amapLocation != null) {
						JLog.d("高德   "+ "LocationType "+amapLocation.getLocationType()+"   ErrorCode "+amapLocation.getErrorCode());
						if (amapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
//							
							if((type == LocationType.GDJPS && LocationType.GPS == startType) &&(amapLocation.getLocationType() != AMapLocation.LOCATION_TYPE_GPS)){
								if (mGDLocationClient.isStarted()) {
									mGDLocationClient.stopLocation();
								}
								if (!mGDLocationClient.isStarted()) {
									mGDLocationClient.startLocation();
								}
								return;
							}else{
								remove();
							}
							SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date date = new Date(System.currentTimeMillis());
							String time = df.format(date);// 定位时间
							
							
							info.setLatitude(amapLocation.getLatitude());
							info.setLongitude(amapLocation.getLongitude());
							
							info.setRadius(amapLocation.getAccuracy());
							info.setPosType(type);
							info.setLcationTime(time);
							info.setStatus(true);
							int vaildAcc = SharedPreferencesUtil.getInstance(context.getApplicationContext())
									.getLocationValidAcc(); // 用户设定精度
							JLog.d(TAG+ "系统精度：" + vaildAcc + " 定位精度:" + amapLocation.getAccuracy());
							if (amapLocation.getAccuracy() > vaildAcc) {
								info.setInAcc(false);// 超出范围
							} else {
								info.setInAcc(true);// 没有超出范围
							}
							info.setZhudong(isZhudong);
							info.setAddress(amapLocation.getAddress());
//							JLog.d("aaa", amapLocation.getAddress()+"     ======="+amapLocation.getLatitude()+"  "+amapLocation.getLongitude());
							geocoderSearch = new GeocodeSearch(context.getApplicationContext()); 
							geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
								
								@Override
								public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
									if(arg0!=null &&arg0.getRegeocodeAddress()!=null){
										info.setAddress(arg0.getRegeocodeAddress().getFormatAddress()+setString(R.string.location2_kq_06));
									}else{
										info.setAddress(setString(R.string.location2_kq_03));
									}
									message.what = 17;
									message.obj = info;
									message.sendToTarget();
								}
								
								@Override
								public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
									
								}
							}); 
							
							if(amapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_WIFI){
								//Wifi定位结果 属于网络定位，定位精度相对基站定位会更好
								if(startType == LocationType.GPS){
									if(!isZhudong){
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_05));
										info.setLocType(setString(R.string.location2_kq_01));
									}else{
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_07));
										info.setLocType(setString(R.string.location2_kq_08));
									}
								}else if(startType == LocationType.JZ){
									info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
									info.setLocType(setString(R.string.location2_kq_01));
								}else{
									if(!isZhudong){
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
										info.setLocType(setString(R.string.location2_kq_01));
									}else{
										info.setAddress(amapLocation.getAddress()+"("+setString(R.string.location2_kq_08)+")");
										info.setLocType(setString(R.string.location2_kq_08));
									}
									
								}
								
								info.setType(type);
								info.setStartType(startType);
								message.what = 11;
								message.obj = info;
								message.sendToTarget();
							}else if(amapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS){
								info.setLocType(setString(R.string.location2_kq_03));
								info.setType(type);
								info.setStartType(startType);
									//latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
									RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude()), amapLocation.getAccuracy(),GeocodeSearch.AMAP); 
									geocoderSearch.getFromLocationAsyn(query);
								
								
							}else if(amapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_SAME_REQ){
									if(startType == LocationType.WIFI && type==LocationType.GDWIFI){
										if(!isZhudong){
											info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
											info.setLocType(setString(R.string.location2_kq_01));
										}else{
											info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_10));
											info.setLocType(setString(R.string.location2_kq_08));
										}
										
									}else if(startType == LocationType.WIFI && type==LocationType.GDJZ){
										if(!isZhudong){
											info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
											info.setLocType(setString(R.string.location2_kq_01));
										}else{
											info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_11));
											info.setLocType(setString(R.string.location2_kq_01));
										}
										
									}else if(startType == LocationType.JZ){
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_08));
										info.setLocType(setString(R.string.location2_kq_01));
									}else{
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_05));
										info.setLocType(setString(R.string.location2_kq_01));
									}
									info.setType(type);
									info.setStartType(startType);
									message.what = 13;
									message.obj = info;
									message.sendToTarget();
							}else{
								
								if(startType == LocationType.WIFI){
									if(!isZhudong){
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
									}else{
										info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_11));
									}
									
								}else if(startType == LocationType.JZ){
									info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_09));
								}else{
									info.setAddress(amapLocation.getAddress()+setString(R.string.location2_kq_05));
								}
								info.setLocType(setString(R.string.location2_kq_01));
								
								info.setType(type);
								info.setStartType(startType);
								message.what = 13;
								message.obj = info;
								message.sendToTarget();
							}
							
							// 定位成功回调信息，设置相关消息
							amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
							amapLocation.getLatitude();// 获取纬度
							amapLocation.getLongitude();// 获取经度
							amapLocation.getAccuracy();// 获取精度信息
							
							amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
							amapLocation.getCountry();// 国家信息
							amapLocation.getProvince();// 省信息
							amapLocation.getCity();// 城市信息
							amapLocation.getDistrict();// 城区信息
							amapLocation.getStreet();// 街道信息
							amapLocation.getStreetNum();// 街道门牌号信息
							amapLocation.getCityCode();// 城市编码
							amapLocation.getAdCode();// 地区编码
							
							StringBuffer sb = new StringBuffer();
							sb.append(setString(R.string.location2_kq_13)
									+" \n"+setString(R.string.location2_kq_14)+time+
									setString(R.string.location2_kq_15)+amapLocation.getAddress()+
									setString(R.string.location2_kq_16)+amapLocation.getLatitude()+
									setString(R.string.location2_kq_17)+amapLocation.getLongitude()+
									setString(R.string.location2_kq_18)+amapLocation.getErrorCode());
							JLog.d("高德   "+ "经纬度" + amapLocation.getLatitude()
									+ "   " + amapLocation.getLatitude() + "   "
									+ amapLocation.getCityCode()+"  Code"+amapLocation.getErrorCode());
							JLog.d("高德   "+sb.toString());
						} else {
							// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
							JLog.d("location Error, ErrCode:"
									+ amapLocation.getErrorCode() + ", errInfo:"
									+ amapLocation.getErrorInfo());
							remove();
							if(type == LocationType.GDWIFI){
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date date = new Date(System.currentTimeMillis());
								String time = df.format(date);// 定位时间
								info.setLocType(setString(R.string.location2_kq_08));
								info.setType(type);
								info.setPosType(type);
								info.setStartType(startType);
								info.setLcationTime(time);
								info.setStatus(false);
								
								info.setInAcc(true);
								info.setZhudong(isZhudong);
								message.what = 12;
								message.obj = info;
								message.sendToTarget();
							}else if(type == LocationType.GDJPS){
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date date = new Date(System.currentTimeMillis());
								String time = df.format(date);// 定位时间
								info.setLocType(setString(R.string.location2_kq_12));
								info.setType(type);
								info.setPosType(type);
								info.setStartType(startType);
								info.setLcationTime(time);
									info.setStatus(false);
								info.setInAcc(true);
								info.setZhudong(isZhudong);
								message.what = 18;
								message.obj = info;
								message.sendToTarget();
							}else{
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date date = new Date(System.currentTimeMillis());
								String time = df.format(date);// 定位时间
								info.setLocType(setString(R.string.location2_kq_01));
								info.setType(type);
								info.setPosType(type);
								info.setStartType(startType);
								info.setAddress(setString(R.string.location2_kq_02));
								info.setLcationTime(time);
									info.setStatus(false);
								info.setInAcc(true);
								info.setZhudong(isZhudong);
								message.what = 14;
								message.obj = info;
								message.sendToTarget();
							}
							
						}
					}else{
						JLog.d("amapLocation =null");
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date date = new Date(System.currentTimeMillis());
						String time = df.format(date);// 定位时间
						info.setLocType(setString(R.string.location2_kq_01));
						info.setType(type);
						info.setPosType(type);
						info.setStartType(startType);
						info.setAddress(setString(R.string.location2_kq_02));
						info.setLcationTime(time);
							info.setStatus(false);
						info.setInAcc(true);
						info.setZhudong(isZhudong);
						message.obj = info;
						message.sendToTarget();
					}
				} catch (Exception e) {
					JLog.e(e);
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String time = df.format(date);// 定位时间
					info.setLocType(setString(R.string.location2_kq_01));
					info.setType(type);
					info.setPosType(type);
					info.setStartType(startType);
					info.setAddress(setString(R.string.location2_kq_02));
					info.setLcationTime(time);
						info.setStatus(false);
					info.setInAcc(true);
					info.setZhudong(isZhudong);
					message.obj = info;
					message.sendToTarget();
				}
				
			}

		};

	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
}
