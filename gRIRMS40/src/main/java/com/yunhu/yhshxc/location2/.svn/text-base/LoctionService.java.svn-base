package com.yunhu.yhshxc.location2;

import gcg.org.debug.JLog;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.location.ReceiveLocationListener;



public class LoctionService {
	private Context mContextl;
	// 百度定位

	private ReceiveLocationListener lisener;
	private KqBaidu baKqbaidu;

	private KqGaode gdKqGaode;

	public LoctionService(Context mContextl) {
		this.mContextl = mContextl;
	}

	/**
	 * 取GPS开关状态 被动
	 * 
	 * @param context
	 *            LocationType.GPS
	 * @param lisener
	 */
	public void startLoctionByGPS(ReceiveLocationListener lisener,boolean isZhu) {
		this.lisener = lisener;
		if (gPSIsOPen(mContextl)) { // 判断gps是否开启
			startGDGPS(mContextl, LocationType.GDJPS, LocationType.GPS, 90*1000,isZhu);
		} else {
			// 判断wifi是否开启
			if (isWifiActive(mContextl)) {
				startGDWifi(mContextl, LocationType.GDWIFI, LocationType.GPS, 5000, isZhu);
			} else {
				// 调用百度 混合定位，带地址，超时3s
				startGDHunHe(mContextl, LocationType.GDJZ, LocationType.GPS, 5000,isZhu);
			}
		}
	}

	/**
	 * 取Wifi开关状态
	 */
	public void startLocationByWifi(ReceiveLocationListener lisener) {
		this.lisener = lisener;
		if (isWifiActive(mContextl)) {
			startGDWifi(mContextl, LocationType.GDWIFI, LocationType.WIFI, 5000, false);
		} else {
			// 调用百度 混合定位，带地址，超时3s
			startGDHunHe(mContextl, LocationType.GDJZ, LocationType.WIFI, 5000,false);
		}
	}

	/**
	 * 先尝试wifi定位，再用混合定位
	 */
	public void startLocatByHunH(ReceiveLocationListener lisener) {
		this.lisener = lisener;
		if (isWifiActive(mContextl)) {
			startGDWifi(mContextl, LocationType.GDWIFI, LocationType.JZ, 5000, false);
		} else {
			// 调用高德 混合定位，带地址，超时3s
			startGDHunHe(mContextl, LocationType.GDJZ, LocationType.JZ, 5000,false);
		}
	}

	/**
	 * 主动定位gps
	 * 
	 * @param lisener
	 */
	public void startLocatG(ReceiveLocationListener lisener,boolean isZhu) {
		this.lisener = lisener;
		startGDGPS(mContextl, LocationType.GDJPS, LocationType.GPS,100*1000, isZhu);
	}

	/**
	 * 主动定位 wifi
	 */
	public void startLocatWF(ReceiveLocationListener lisener, boolean isZhudong) {
		this.lisener = lisener;
		startGDWifi(mContextl, LocationType.GDWIFI, LocationType.WIFI, 5000, isZhudong);
	}

	/**
	 * 主动定位 混合定位
	 */
	public void startLocatHH(ReceiveLocationListener lisener, boolean isZhudong) {
		this.lisener = lisener;
		if (isWifiActive(mContextl)) {
			startGDWifi(mContextl, LocationType.GDWIFI, LocationType.JZ, 5000, isZhudong);
//			startBAiduWIFi(mContextl, 3000, LocationType.BDWIFI, LocationType.JZ, isZhudong);
		} else {
			startGDHunHe(mContextl, LocationType.GDJZ, LocationType.JZ, 5000,isZhudong);
		}
	}

	/**
	 * 开启百度gps定位
	 * 
	 * @param context
	 */
	public void statrtBaiduGPS(Context context, int type, int startType,boolean isZhu) {
		// Gprs 开启
		// 调用百度定位
		if (baKqbaidu ==null) {
			baKqbaidu = new KqBaidu(mHander, context);
		}
		baKqbaidu.startBD(LocationType.GPSSTART, 60 * 1000, type, startType,
				isZhu);
	}

	/**
	 * 开启百度wifi定位
	 * 
	 * @param context
	 */
	public void startBAiduWIFi(Context context, int time, int type,
			int startType, boolean isZhudong) {
		if (baKqbaidu == null) {
			baKqbaidu = new KqBaidu(mHander, context);
		}
		baKqbaidu.startBD(LocationType.WIFISTART, time, type, startType,
				isZhudong);

	}

	/**
	 * 开启百度混合定位 基站定位
	 * 
	 * @param context
	 */
	public void startBDHunHe(Context context, int type, int startType,
			int outTime) {
		if (baKqbaidu == null) {
			baKqbaidu = new KqBaidu(mHander, context);
		}
		baKqbaidu
				.startBD(LocationType.JZSTART, outTime, type, startType, false);
	}

	/**
	 * 开启高德WIFI定位
	 */
	public void startGDWifi(Context context, int type, int startType,
			long time, boolean isZhudong) {
		if (gdKqGaode == null) {
			gdKqGaode = new KqGaode(context, mHander);
		}
		gdKqGaode.startGD(LocationType.WIFISTART, time, type, startType,
				isZhudong);
	}

	/**
	 * 开启高德混合定位 基站定位
	 */
	public void startGDHunHe(Context context, int type, int startType,
			long outTime,boolean isZhu) {
		if (gdKqGaode == null) {
			gdKqGaode = new KqGaode(context, mHander);
		}
		gdKqGaode
				.startGD(LocationType.JZSTART, outTime, type, startType,isZhu);
	}

	/**
	 * 开启高德GPS定位
	 */
	public void startGDGPS(Context context, int type, int startType,
			long outTime,boolean isZhu) {
		if (gdKqGaode == null) {
			gdKqGaode = new KqGaode(context, mHander);
		}
		gdKqGaode.startGD(LocationType.GPSSTART, outTime, type, startType, isZhu);
	}

	private Handler mHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				switch (msg.what) {
				case 1: // 百度GPS成功 返回 B01
					// 百度 gps定位处理
					// 判断定位成功失败
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult infsgo = (LocationResult) msg.obj;
					lisener.onReceiveResult(infsgo);
					
					JLog.d("收到定位结果" );
					
					break;
				case 2: // 百度GPS失败
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult infogo = (LocationResult) msg.obj;
					lisener.onReceiveResult(infogo);
					
					break;
				case 3:// 百度wifi定位成功 返回 B02
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					LocationResult info = (LocationResult) msg.obj;
					lisener.onReceiveResult(info);
					
					break;
				case 4:// 百度 wifi定位失败 使用高德混合定位 带地址，超时3s
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					LocationResult infowifi = (LocationResult) msg.obj;
					if(infowifi !=null){
						if (infowifi.isZhudong()  //如果是主动定位，WIFI定位失败  直接返回结果
								&& infowifi.getStartType() == LocationType.WIFI) {
							lisener.onReceiveResult(infowifi);
						} else if (infowifi.isZhudong()  //如果是主动定位混合定位失败 ,调用高德混合定位
								&& infowifi.getStartType() == LocationType.JZ) {
							startGDHunHe(mContextl, LocationType.GDJZ, infowifi.getStartType(), 6000,infowifi.isZhudong());
						} else {//否则为被动定位失败 ，调用高德混合定位
							startGDHunHe(mContextl, LocationType.GDJZ, infowifi.getStartType(), 6000,infowifi.isZhudong());
						}
					}else{
						lisener.onReceiveResult(infowifi);
					}
					

					break;
				case 5:// 百度混合定位 返回结果 + 后缀(GPS和Wifi不可用，基站定位) 正常B03
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					LocationResult infogos = (LocationResult) msg.obj;
					lisener.onReceiveResult(infogos);
					

					break;
				case 6:// 百度混合定位失败 调用高德 混合定位，带地址，超时3s
						if (gdKqGaode != null) {
							gdKqGaode.endGD();
						}
						if (baKqbaidu != null) {
							baKqbaidu.endBD();
						}
						LocationResult bdInfo = (LocationResult) msg.obj;
						lisener.onReceiveResult(bdInfo);
					
					break;
				case 11:// 高德wifi定位成功
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult infogd = (LocationResult) msg.obj;
					lisener.onReceiveResult(infogd);

					break;
				case 12:// 高德wifi定位失败，调用百度混合定位
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					LocationResult ingd = (LocationResult) msg.obj;
					if(ingd!=null){
						if (ingd.isZhudong() //主动定位WIFI定位失败，调用百度WIFI定位
								&& ingd.getStartType() == LocationType.WIFI) {
							startBAiduWIFi(mContextl, 5000, LocationType.BDWIFI, ingd.getStartType(),
									ingd.isZhudong());
						} else if (ingd.isZhudong()//主动定位 混合定位失败，调用百度WIFI定位
								&& ingd.getStartType() == LocationType.JZ) {
							startBAiduWIFi(mContextl, 5000, LocationType.BDWIFI, ingd.getStartType(),
									ingd.isZhudong());
						} else {//否则为被动定位失败，调用百度WIFI定位
							startBAiduWIFi(mContextl, 5000, LocationType.BDWIFI, ingd.getStartType(),
									ingd.isZhudong());
						}
					}else{
						lisener.onReceiveResult(ingd);
					}
					

					break;
				case 13:// 高德 混合定位成功
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult inhhgd = (LocationResult) msg.obj;
					lisener.onReceiveResult(inhhgd);
					
					break;
				case 14:// 高德 混合定位 不返回定位结果，返回文字(定位失败，Wifi、GPS、蜂窝网络不可用。)
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					LocationResult inhhgds = (LocationResult) msg.obj;
					if(inhhgds!=null){
						if (inhhgds.isZhudong() //高德混合定位主动定位失败，启动百度混合定位
								&& LocationType.JZ == inhhgds.getStartType()) {
							startBDHunHe(mContextl, LocationType.BDJIZHAN,inhhgds.getStartType(), 6000);
						} else if (LocationType.GPS == inhhgds.getStartType()) {//GPS优先，混合定位失败，直接返回结果
							lisener.onReceiveResult(inhhgds);
							
						}else{
							lisener.onReceiveResult(inhhgds);
						}
					}else{
						lisener.onReceiveResult(inhhgds);
					}
					
					
					break;
				case 17://高德GPS定位成功
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult resutl = (LocationResult) msg.obj;
					lisener.onReceiveResult(resutl);
					break;
				case 18://高德GPS定位失败
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult resutlt = (LocationResult) msg.obj;
					if(resutlt!=null){
						if (resutlt.isZhudong()) {
							//主动
							lisener.onReceiveResult(resutlt);
						}else{
							//被动
							if(resutlt.getType() == LocationType.GDJPS){
								startGDWifi(mContextl, LocationType.GDWIFI, LocationType.GPS, 3000, false);
							}else if(resutlt.getType() == LocationType.BDWIFI){
								startGDHunHe(mContextl, LocationType.GDJZ, LocationType.GPS, 3000,false);
							}else if(resutlt.getType() == LocationType.GDWIFI){
								startBAiduWIFi(mContextl, 3000, LocationType.BDWIFI,LocationType.GPS, false);
							}else if(resutlt.getType() == LocationType.GDJZ){
								startBDHunHe(mContextl, LocationType.BDJIZHAN, LocationType.GPS, 3000);
							}else{
								lisener.onReceiveResult(resutlt);
							}
							
						}
					}else{
						lisener.onReceiveResult(resutlt);
					}
					
					break;
				case 100:
					// -=----------------------------------------------
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					LocationResult resu3 = new LocationResult();
					
					lisener.onReceiveResult(resu3);
					// -=----------------------------------------------
					break;
				case 1000://百度定位超时 60*1000
					// -=----------------------------------------------
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					boolean isZhuDong = (Boolean) msg.obj;
					if (isZhuDong) {
						//主动
						LocationResult resu2 = new LocationResult();
						lisener.onReceiveResult(resu2);
					}else{
						//被动
						int i = msg.arg1;
						if(i == LocationType.BDWIFI){
							startGDHunHe(mContextl, LocationType.GDJZ, LocationType.GPS, 3000,false);
						}else{
							
							LocationResult resu2 = new LocationResult();
							lisener.onReceiveResult(resu2);
						}
						
					}
					
					
					// -=----------------------------------------------
					break;
				case 1001: //高德定位超时 100*1000
					
					if (gdKqGaode != null) {
						gdKqGaode.endGD();
					}
					if (baKqbaidu != null) {
						baKqbaidu.endBD();
					}
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String time = df.format(date);// 定位时间
					
					boolean isZhuDongGd = (Boolean) msg.obj;
					if (isZhuDongGd) {
						//主动
						LocationResult resu2 = new LocationResult();
						lisener.onReceiveResult(resu2);
					}else{
						//被动
						int i = msg.arg1;
						if(i == LocationType.GDJPS){
							startGDWifi(mContextl, LocationType.GDWIFI, LocationType.GPS, 3000, false);
						}else if(i == LocationType.GDWIFI){
							startBAiduWIFi(mContextl, 3000, LocationType.BDWIFI,LocationType.GPS, false);
						}else if(i == LocationType.GDJZ){
							startBDHunHe(mContextl, LocationType.BDJIZHAN, LocationType.GPS, 3000);
						}else{
							LocationResult resu2 = new LocationResult();
							resu2.setStatus(false);
							resu2.setInAcc(true);
							resu2.setLcationTime(time);
							resu2.setAddress(setString(R.string.location2_kq_02));
							lisener.onReceiveResult(resu2);
						}
						
					}
					break;
					default:
						SimpleDateFormat df1 = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date date1 = new Date(System.currentTimeMillis());
						String time1 = df1.format(date1);// 定位时间
						if (gdKqGaode != null) {
							gdKqGaode.endGD();
						}
						if (baKqbaidu != null) {
							baKqbaidu.endBD();
						}
						LocationResult resu2 = new LocationResult();
						resu2.setStatus(false);
						resu2.setInAcc(true);
						resu2.setLcationTime(time1);
						resu2.setAddress(setString(R.string.location2_kq_02));
						lisener.onReceiveResult(resu2);
						break;
				}
			} catch (Exception e) {
				JLog.e(e);
				SimpleDateFormat df1 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date1 = new Date(System.currentTimeMillis());
				String time1 = df1.format(date1);// 定位时间
				if (gdKqGaode != null) {
					gdKqGaode.endGD();
				}
				if (baKqbaidu != null) {
					baKqbaidu.endBD();
				}
				LocationResult resu2 = new LocationResult();
				resu2.setStatus(false);
				resu2.setInAcc(true);
				resu2.setLcationTime(time1);
				resu2.setAddress(setString(R.string.location2_kq_02));
				if(lisener!=null){
					lisener.onReceiveResult(resu2);
				}
			}
			
		};
	};

	/**
	 * 判断是否开启GPS
	 * 
	 * @param context
	 * @return
	 */
	public static final boolean gPSIsOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	/**
	 * 检测wifi或网络是否开启
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiActive(Context icontext) {

		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;

	}
	private String setString(int stringId){
		return mContextl.getResources().getString(stringId);
	}
}
