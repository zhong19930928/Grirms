package com.yunhu.yhshxc.location2;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import gcg.org.debug.JLog;


public class KqBaidu {
	private final String TAG = "KqBaidu";
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private Handler mHander;
	private Context mContext;
	private int type;
	private int startType;
	private boolean isZhudong;
	Message message;
	Message message2;
	LocationResult info;

	public KqBaidu(Handler mHander, Context context) {
		this.mHander = mHander;
		this.mContext = context;
		mLocationClient = new LocationClient(mContext.getApplicationContext()); // 声明LocationClient类
	}

	public void startBD(int mode, int timeOut, int type, int startType,
			boolean isZhudong) {
		this.type = type;
		this.startType = startType;
		this.isZhudong = isZhudong;
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation(mode, timeOut);
		message = mHander.obtainMessage();
		info = new LocationResult();
		message2 = mHander.obtainMessage();
		message2.what = 1000;
		message2.obj = isZhudong;
		message2.arg1 = type;
		mHander.sendMessageDelayed(message2, 1 * 60 * 1000);
		// ----------------------------------------------------
	}

	public void endBD() {
		// ----------------------------------------------------
		mLocationClient.unRegisterLocationListener(myListener);
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		// ----------------------------------------------------
	}

	public void remove() {
		if (mHander.hasMessages(1000)) {
			synchronized (KqBaidu.class) {
				if (mHander.hasMessages(1000)) {
					try {
						mHander.removeMessages(1000);
					} catch (Exception e) {
						JLog.e(e);
					}

				}
			}
		}
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null) {
				JLog.d("BaiduLocationApiDem----------------BDLocation=null" );
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
				message = mHander.obtainMessage();
				message.obj = info;
				message.sendToTarget();
			}else{
				JLog.d("BaiduLocationApiDem",
						"----------------" + location.getLocType());
				/**
				 * 如果 使用的是GPS定位 但返回的是网络定位 则不移除消息
				 */
				try {
					if ((type == LocationType.BDGPS || type == LocationType.GPS)
							&& location.getLocType() == BDLocation.TypeNetWorkLocation) {
						/**
						 * 在有网络的情况下 及时 使用GPs定位 也会 反回一次网络定位 信息 此时认为定位 失败 关闭 定位 然后再
						 * 开启就是使用gprs定位
						 * 
						 */
						if (mLocationClient.isStarted()) {
							mLocationClient.stop();
						}
						if (!mLocationClient.isStarted()) {
							mLocationClient.start();
						}
						return;

					} else {
						remove();
					}
					// ------------------------------------------
					StringBuffer sb = new StringBuffer(256);
					sb.append("===============");
					sb.append("location.getLocType()   " + location.getLocType());
					sb.append("\ntime : ");
					sb.append(location.getTime());
					sb.append("\nerror code : ");
					sb.append(location.getLocType());
					sb.append("\nlatitude : ");
					sb.append(location.getLatitude());
					sb.append("\nlontitude : ");
					sb.append(location.getLongitude());
					sb.append("\naddress :");
					sb.append(location.getAddrStr());
					sb.append("\nradius : ");
					sb.append(location.getRadius());
					JLog.d( sb.toString());
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String time = df.format(date);// 定位时间
					if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

						info.setLatitude(location.getLatitude());
						info.setLongitude(location.getLongitude());

						// info.setLocType("百度定位GPS");
						info.setLocType(setString(R.string.location2_kq_03));
						info.setPosType(type);
						info.setRadius(location.getRadius());

						info.setLcationTime(time);
						info.setStatus(true);
						// if(type == LocationType.GPS){
						// info.setAddress(location.getAddrStr()+"(GPS定位)");
						// }else{
						// info.setAddress(location.getAddrStr());
						// }
						info.setAddress(location.getAddrStr() + "("+setString(R.string.location2_kq_03)+")");
						int vaildAcc = SharedPreferencesUtil.getInstance(mContext.getApplicationContext())
								.getLocationValidAcc(); // 用户设定精度
						JLog.d(TAG,
								"系统精度：" + vaildAcc + " 定位精度:" + location.getRadius());
						if (location.getRadius() > vaildAcc) {
							info.setInAcc(false);// 超出范围
						} else {
							info.setInAcc(true);// 没有超出范围
						}
						info.setType(type);
						info.setStartType(startType);
						message = mHander.obtainMessage();
						message.what = 1;
						message.obj = info;
						message.sendToTarget();

					} else if (location.getLocType() == BDLocation.TypeCriteriaException) { // Gps定位失败
						// info.setLocType("百度定位GPS");
						info.setLocType(setString(R.string.location2_kq_03));
						info.setAddress(setString(R.string.location2_kq_04));
						info.setLcationTime(time);
						info.setType(type);
						info.setPosType(type);
						info.setStatus(false);
						info.setInAcc(true);
						info.setStartType(startType);
						message = mHander.obtainMessage();
						message.what = 2;
						message.obj = info;
						message.sendToTarget();

					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

						info.setLatitude(location.getLatitude());
						info.setLongitude(location.getLongitude());

						// info.setLocType("百度定位");

						info.setPosType(type);
						info.setRadius(location.getRadius());
						info.setLcationTime(time);
						info.setStatus(true);
						int vaildAcc = SharedPreferencesUtil.getInstance(mContext.getApplicationContext())
								.getLocationValidAcc(); // 用户设定精度
						JLog.d(TAG,
								"系统精度：" + vaildAcc + " 定位精度:" + location.getRadius());
						if (location.getRadius() > vaildAcc) {
							info.setInAcc(false);// 超出范围
						} else {
							info.setInAcc(true);// 没有超出范围
						}
						info.setAddress(location.getAddrStr());
						if (startType == LocationType.WIFI) {
							info.setAddress(location.getAddrStr() + "("+setString(R.string.location2_kq_01)+")");
						} else if (startType == LocationType.GPS) {
							info.setAddress(location.getAddrStr() + setString(R.string.location2_kq_05));

						} else {
							info.setAddress(location.getAddrStr() + "("+setString(R.string.location2_kq_01)+")");
						}

						info.setLocType(setString(R.string.location2_kq_01));
						info.setType(type);
						info.setStartType(startType);
						message = mHander.obtainMessage();
						message.what = 3;
						message.obj = info;
						message.sendToTarget();
					} else if (location.getLocType() == BDLocation.TypeNetWorkException) {// 网络定位失败
						// info.setLocType("百度定位");
						info.setLocType(setString(R.string.location2_kq_01));
						info.setStartType(startType);
						info.setType(type);
						info.setPosType(type);
						info.setLcationTime(time);
						if (isZhudong) {
							info.setStatus(false);
						} else {
							info.setStatus(true);
						}

						info.setInAcc(true);

						info.setZhudong(isZhudong);
						message = mHander.obtainMessage();
						message.what = 4;
						message.obj = info;
						message.sendToTarget();
					} else { // 离线定位结果失败
						if (startType == LocationType.GPS) {
							info.setLocType(setString(R.string.location2_kq_03));
						} else {
							info.setLocType(setString(R.string.location2_kq_01));
						}
						info.setType(type);
						info.setPosType(type);
						info.setStartType(startType);
						info.setLcationTime(time);
						info.setStatus(false);
						info.setInAcc(true);
						info.setZhudong(isZhudong);
						info.setAddress(setString(R.string.location2_kq_02));
						message = mHander.obtainMessage();
						message.what = 6;
						message.obj = info;
						message.sendToTarget();

					}
					JLog.d("BaiduLocationApiDem  "+ sb.toString());
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
					message = mHander.obtainMessage();
					message.obj = info;
					message.sendToTarget();
				}
				
			}
			// ---------------------------------------
			
		}
	}

	private void initLocation(int mode, int timeOut) {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000 * 500 * 60;
		// option.setScanSpan(1000);//
		// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps

		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		// option.setIsNeedLocationDescribe(true);//
		// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		// option.setIsNeedLocationPoiList(true);//
		// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.disableCache(true);
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		// option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setTimeOut(timeOut);
		option.setAddrType("all");
		/**
		 * 定位模式 分为高精度定位模式 低功耗定位模式 仅设备定位模式 Hight_Accuracy
		 * 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果； Battery_Saving
		 * 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位） Device_Sensors
		 * 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
		 */
		if (mode == LocationType.WIFISTART) {// 高精度 wifi
			option.setLocationMode(LocationMode.Hight_Accuracy);

		} else if (mode == LocationType.JZSTART) {
			option.setLocationMode(LocationMode.Battery_Saving);
		} else {
			option.setLocationMode(LocationMode.Device_Sensors);// 仅GPS
		}
		mLocationClient.setLocOption(option);
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		mLocationClient.start();

	}
	private String setString(int stringId){
		return mContext.getResources().getString(stringId);
	}
}
