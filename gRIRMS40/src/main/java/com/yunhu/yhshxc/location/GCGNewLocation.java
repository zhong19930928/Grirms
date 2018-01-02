package com.yunhu.yhshxc.location;

//import com.profession.CTLocationService;
//import com.profession.data.LocationItem;
//import com.profession.data.MULLocation;
//import com.profession.listener.MULLocationListener;

/**
 * 定位模块
 * 
 * 初始化locationMaster LocationMaster(Context,Handler,Options)
 * 
 * Context:调用方的Context Handler:接收定位结果的Handler Options: HashMap / null为使用默认的选项 常量
 * LocationMaster.OPT_USE_ALL 使用所有可用地图 默认true 常量
 * LocationMaster.OPT_USE_MAPBAR_ONLY 仅仅使用Mapbar定位 默认false 常量
 * LocationMaster.OPT_USE_WIFI 在定位失败后使用Wifi估计位置 默认true 常量
 * LocationMaster.OPT_BAIDU_OPEN_GPS 百度地图开启GPS 默认true 常量
 * LocationMaster.OPT_BAIDU_COOR_DB09LL 百度地图使用bd09ll测绘 默认true
 * //如果false将使用gcj02ll 常量 LocationMaster.OPT_BAIDU_DISABLE_CACHE 百度地图禁止缓存 默认true
 * 常量 LocationMaster.OPT_BAIDU_GPS_FIRST 百度优先启用GPS定位 默认true 常量
 * LocationMaster.OPT_BAIDU_USE_MINIMAL_SCAN_SPAN 百度使用最小间隔不停监听 默认true 常量
 * LocationMaster.OPT_MAPBAR_OPEN_GPS 图吧开启GPS 默认true 常量
 * LocationMaster.WAIT_FOR_LONG Timeout为60秒 默认false 常量
 * LocationMaster.WAIT_FOR_NORMAL Timeout为30秒 默认true 常量
 * LocationMaster.WAIT_FOR_SHORT Timeout为15秒 默认false 常量
 * LocationMaster.WAIT_FOR_FOREVER 无限等待指导两种方式均返回 默认false
 * 
 * 例如限制只用Mapbar，其他设置默认： HashMap<Integer,Boolean> options=new
 * HashMap<Integer,Boolean>();
 * options.put(LocationMaster.OPT_USE_MAPBAR_ONLY,true);
 * 
 * 
 * @version 2013-06-04
 * @author houyu
 * 
 */
//public class GCGNewLocation implements ControlLocation {

//	private final String TAG = "GCGNewLocation";
//	private Context context = null;
//	private ReceiveLocationListener listener = null;
//	private CTLocationService service = null;
//	private boolean isNeedAddress;
//	private String provider;
//
//	public GCGNewLocation(Context context) {
//		this.context = context;
//		this.isNeedAddress = true;
//		this.provider = CTLocationService.PROFESSION_PROVIDER;
//		service = new CTLocationService(context.getApplicationContext());
//		service.registerLocationListener(new MyListener());
//	}
//
//	public void setNeedAddress(boolean isNeedAddress) {
//		this.isNeedAddress = isNeedAddress;
//	}
//
//	protected void setOnlyGPS() {
//		this.provider = CTLocationService.GPS_PROVIDER;
//	}
//
//	private class MyListener implements MULLocationListener {
//
//		@Override
//		public void onResponse(MULLocation mulLocation) {
//			// JLog.d(TAG, Thread.currentThread().getName());
//
//			// mulLocation.getLocationResponse().setAddress(getAddr(mulLocation));
//			Message msg = new Message();
//			msg.what = 0;
//			msg.obj = mulLocation;
//			locationHandler.sendMessage(msg);
//		}
//	}
//
//	// private String getAddr(MULLocation mulLocation){
//	// if (mulLocation.getResultCode() == 200) {
//	// LocationItem result = mulLocation.getLocationResponse();
//	// HashMap<String, String> params = new HashMap<String, String>();
//	// params.clear();
//	// params.put("action", LocationMaster.ACTION);
//	// params.put("version", LocationMaster.VERSION);
//	// params.put("lonlat", result.getLon()+ "," + result.getLat());
//	// params.put("status",Integer.toString(result.getType()));
//	// params.put("acc", Float.toString(result.getAcc()));
//	// String addr = new
//	// HttpHelper(context).connectPost("http://location.gcgcloud.com/search.php",params);
//	// return addr;
//	// }else{
//	// return null;
//	// }
//	// }
//
//	public void setListener(ReceiveLocationListener listener) {
//		this.listener = listener;
//	}
//
//	// 定义当定位成功后的回调Handler
//	private Handler locationHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			LocationResult locationResult = new LocationResult();
//			MULLocation location = (MULLocation) msg.obj;
//			if (location == null) {
//				locationResult.setStatus(false);
//				locationResult.setInAcc(true);// 没有超出范围
//			} else if (location.getResultCode() == 501) {
//				Toast.makeText(context, "正在定位，请稍后", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (location.getResultCode() == 200
//					|| location.getResultCode() == 401
//					|| location.getResultCode() == 206) {
//				LocationItem result = location.getLocationResponse();
//				if (result != null && result.getLat() > 0) {
//					int vaildAcc = SharedPreferencesUtil.getInstance(
//							context.getApplicationContext())
//							.getLocationValidAcc(); // 用户设定精度
//					JLog.d(TAG, "系统精度：" + vaildAcc + " 定位精度:" + result.getAcc());
//					if (result.getAcc() > vaildAcc) {
//						locationResult.setInAcc(false);// 超出范围
//					} else {
//						locationResult.setInAcc(true);// 没有超出范围
//					}
//
//					locationResult.setLatitude(result.getLat());
//					locationResult.setLongitude(result.getLon());
//					locationResult.setRadius(result.getAcc());
//					locationResult.setAddress(result.getAddress());
//					locationResult.setLocType(getLocType(result.getType()));
//					locationResult.setPosType(result.getType());
//					locationResult.setLcationTime(DateUtil
//							.dateToDateString(new Date()));
//					locationResult.setStatus(true);
//				} else {
//					locationResult.setStatus(false);
//					locationResult.setInAcc(true);// 没有超出范围
//				}
//
//			} else {
//				locationResult.setStatus(false);
//				locationResult.setInAcc(true);// 没有超出范围
//			}
//			JLog.d("定位返回结果:"
//					+ listener.getClass().getName()
//					+ "回调/定位结果码是："
//					+ (location != null ? String.valueOf(location
//							.getResultCode()) : "location是空"));
//			// Log.e(TAG, listener.getClass().getName() + "回调/定位结果码是：" +
//			// location.getResultCode());
//			listener.onReceiveResult(radiusStr(locationResult));
//			JLog.d(TAG, "save success(" + Thread.currentThread().getName()
//					+ ")=>" + "lon =" + locationResult.getLongitude()
//					+ " lat =" + locationResult.getLatitude()
//					+ " locationTime =" + locationResult.getLcationTime());
//			JLog.d(TAG, "save success(" + Thread.currentThread().getName()
//					+ ")=>" + "address =" + locationResult.getAddress());
//			JLog.d(TAG, "save success(" + Thread.currentThread().getName()
//					+ ")=>" + "type =" + locationResult.getPosType());
//			super.handleMessage(msg);
//		}
//
//	};
//
//	/**
//	 * 精确到多少米文本信息
//	 * 
//	 * @param locationResult
//	 * @return
//	 */
//	public LocationResult radiusStr(LocationResult locationResult) {
//		String adress = locationResult.getAddress();
//		if (locationResult != null && !TextUtils.isEmpty(adress)) {
//			float radius = locationResult.getRadius();
//			if (radius > 0 && radius < 1500) {
//				locationResult
//						.setAddress(adress + "[精确到" + (int) radius + "米]");
//			} else if (radius >= 1500) {
//				locationResult.setAddress(adress + "[精确到1500米]");
//			}
//		}
//		return locationResult;
//	}
//
//	@Override
//	public void stopLocation() {
//
//	}
//
//	@Override
//	public void requestLocation() {
//		JLog.d(TAG, "----开始定位----");
//		service.getLocation(LocationMaster.OVER_TIME, this.isNeedAddress,
//				this.provider);
//	}
//
//	/**
//	 * 开始定位
//	 * 
//	 * @param time
//	 *            设置定位超时时间
//	 */
//	public void requestLocation(long time) {
//		JLog.d(TAG, "----开始定位----");
//		service.getLocation(time, this.isNeedAddress, this.provider);
//	}
//
//	private String getLocType(Integer type) {
//		String str = "";
//		if (type != null) {
//			str = (type == LocationMaster.LOCATION_TYPE_BAIDU_GPS
//					|| type == LocationMaster.LOCATION_TYPE_MAPBAR_GPS || type == LocationMaster.LOCATION_TYPE_AUTO_NAVI_GPS) ? "GPS定位"
//					: "混合定位";
//		}
//		return str;
//	}

//}
