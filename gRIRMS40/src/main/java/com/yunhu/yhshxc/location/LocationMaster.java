package com.yunhu.yhshxc.location;


public class LocationMaster {

	public static final String VERSION = "3";
	public static final String ACTION = "getaddr";
	
	public static final Long OVER_TIME = 20L;//普通定位延时
	public static final Long OVER_TIME_ONLY_GPS = 20L;//仅GPS定位延时

	public static final int OPT_USE_ALL = 150000;
	public static final int OPT_USE_MAPBAR_ONLY = 150001;
	public static final int OPT_USE_WIFI = 150002;
	public static final int OPT_WAIT_LONGER = 150003;
	public static final int OPT_WAIT_NORMAL = 150004;
	public static final int OPT_WAIT_SHORT = 150005;
	public static final int OPT_WAIT_FOREVER = 150006;

	public static final int OPT_BAIDU_OPEN_GPS = 160001;
	public static final int OPT_BAIDU_COOR_DB09LL = 160002;
	public static final int OPT_BAIDU_DISABLE_CACHE = 160004;
	public static final int OPT_BAIDU_GPS_FIRST = 160005;
	public static final int OPT_BAIDU_USE_MINIMAL_SCAN_SPAN = 160006;

	public static final int OPT_MAPBAR_OPEN_GPS = 170001;

	public static final int OPT_GOOGLE_OPEN_GPS = 180001;
	
	public static final int OPT_GET_ADDRESS = 190001;
	public static final int OPT_USE_GPS_ONLY = 190002;

	public static final int SUCESS = 200;
	public static final int FAILED = 400;

	public static final int LOCATION_TYPE_AUTO_NAVI_CELL = 602;
	public static final int LOCATION_TYPE_AUTO_NAVI_GPS = 601;
	public static final int LOCATION_TYPE_BAIDU_GPS = 61;
	public static final int LOCATION_TYPE_BAIDU_CELL = 161;
	public static final int LOCATION_TYPE_MAPBAR_GPS = 501;
	public static final int LOCATION_TYPE_MAPBAR_CELL = 502;

}
