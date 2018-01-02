package com.yunhu.yhshxc.utility;

import android.os.Environment;
   /**
    *            
    * 公共常量类,包含存储,版本,push类型,考勤..等一些常量
    *
    */
public class Constants {

	public static String APP_VERSION_4_0 = "4.0";// 电信版销售管家
	public static String APP_VERSION_4_5 = "4.5";// 免费版销售管家（执掌销售）
	public static String APP_VERSION_4_7 = "4.7";// 通用版销售管家

	public static String CURRENT_VERSION = ""; // 当前版本
	public static String SDCARD_PATH = Environment.getExternalStorageDirectory() + "/grirms/"; // 存储SD卡路径
	public static String PATH_TEMP = SDCARD_PATH + "temp/"; //
	public static String DOWN_NEWAPK_PATH = SDCARD_PATH + "grirms.apk"; // 存储SD卡路径
	public static String COMPANY_LOGO_PATH = SDCARD_PATH + "logo/"; // 存储SD卡路径
	public static String COMPANY_STYLE_PATH = SDCARD_PATH + "style_xs/"; // 存储SD卡路径
	public static String RECORD_PATH = SDCARD_PATH + "record/"; // 存储录音文件路径
	public static String VIDEO_PATH = SDCARD_PATH + "video/"; // 存储视频文件路径
	public static String FUNC_ATTACHMENT_PATH = SDCARD_PATH + "funcAttachment/"; // 存储附件文件路径
	public static String ATTACHMENT_PATH = SDCARD_PATH + "attachment/"; // 存储公告附件的路径
	public static final String ORDER3_PATH = SDCARD_PATH + "order3/"; // 存储order3照片的路径
	public static final String CARSALES_PATH = SDCARD_PATH + "carSales/"; // 存储车销照片的路径
	public static final String WECHAT_PATH = SDCARD_PATH + "wechat/";// 存储企业微信照片的路径
	public static final String WECHAT_PATH_HEADER_LOAD = WECHAT_PATH +"header/";
	public static final String WECHAT_PATH_LOAD = WECHAT_PATH + "load/";// 存储企业微信加载的照片的路径
//	public static final String WECHAT_PATH_VIDEO_LOAD = WECHAT_PATH_LOAD + "video";//企业微信储存视频路径
	public static final String LEAVE_PAHT=SDCARD_PATH+"leave/";//储存请假拍照的路径
	public static final String LEAVE_PAHT_TEMP=SDCARD_PATH+"leaveTemp/";//储存请假拍照的路径
	public static final String SUMMARY_PATH = SDCARD_PATH+"summary/";
	public static final String SUMMARY_PATH_LOAD = SUMMARY_PATH + "load/";// 工作总结加载的照片的路径
	public static final String ADV_PICTURE = SDCARD_PATH + "slidepicture/";// 首页轮播图路径


	public static int SCREEN_WIDTH = 480; // 分辨率
	public static int SCREEN_HEIGHT = 800; // 分辨率
	public static int PLANNUMBER = 0;
	public static int TASKNUMBER = 0;
	public static String changed_visit_task_notice;
	public static int NOTICENUMBER = 0;
	public static int DOUBLENUMBER = 0;

	public static boolean hasReplenish = false;
	public static boolean isNeadReflash = false;// bbs 进详细返回的时候用，判断是否需要传USERid

	public static final int DEFAULTINT = 0; // 通用默认值

	public static final String USERIV_PATH = SDCARD_PATH + "bbsUserIv/";
	public static final String CONTENTIV_PATH = SDCARD_PATH + "bbsContentIv/";
	public static final String TEMP_IMAGE_PATH = SDCARD_PATH + "tempImage/";

	public static final String HTTP_RESPONSE_ERROR = "HTTP_RESPONSE_ERROR";
	public static final int COMP_VALIDATION_TYPE_NUMERIC = 1;
	public static final int COMP_VALIDATION_TYPE_MAIL = 2;
	public static final int COMP_VALIDATION_TYPE_ID_NUMBER = 3;
	public static final int COMP_VALIDATION_TYPE_ZIP_CODE = 4;
	public static final int COMP_VALIDATION_TYPE_FIXED_TELEPHONE = 5;

	public static final int COMP_VALIDATION_TYPE_MOBILE_TELEPHONE = 6;
	public static final int COMP_VALIDATION_TYPE_DATE = 7;
	public static final int COMP_VALIDATION_TYPE_INTERCONNECTION_VERIFICATION = 8;

	public static final int SETTRIALROUNDS = 5;
	public static boolean ISCHECKOUT = false;// 访店 是否checkOut
	public static boolean ISCHECKOUTMODUL = false;// 自定义 是否checkOut

	public static final String BROADCAST_ACTION_WAITING_PROCESS = "BROADCAST_ACTION_WAITING_PROCESS";// 待办事项广播接收
	public static final String BROADCAST_ACTION_STYLE = "BROADCAST_ACTION_STYLE";// 更新Style
																					// UI广播接收
	public static final String BROADCAST_ACTION_NEARBY_COPY = "BROADCAST_ACTION_NEARBY_COPY";// 就近拜访的时候点击了copy按钮
	public static final String BROADCAST_ORDER3_REFRASH_PRODUCT = "BROADCAST_ORDER3_REFRASH_PRODUCT";// 订单3刷新订单通知
	public static final String BROADCAST_ORDER3_SEND_SUCCESS = "BROADCAST_ORDER3_SEND_SUCCESS";// 订单送货成功通知
	public static final String BROADCAST_ORDER3_CREATE_SUCCESS = "BROADCAST_ORDER3_CREATE_SUCCESS";// 下订单提交数据后通知
	public static final String BROADCAST_ORDER3_REFRASH_STORE = "BROADCAST_ORDER3_REFRASH_STORE";// 刷新店面数据

	public static final String BROADCAST_CARSALES_CREATE_SUCCESS = "BROADCAST_CARSALES_CREATE_SUCCESS";// 车销卖货下单提交数据后通知

	public static final String BROADCAST_CAR_SALES_REFRASH_PRODUCT = "BROADCAST_CAR_SALES_REFRASH_PRODUCT";// 车销刷新产品通知
	public static final String BROADCAST_CAR_SALES_REFRASH_STORE = "BROADCAST_CAR_SALES_REFRASH_STORE";// 车销刷新店面数据

	public static final String BROADCAST_WECHAT_HUILIAO = "BROADCAST_WECHAT_HUILIAO";// 微信汇聊收到新消息通知
	public static final String BROADCAST_WECHAT_HUILIAO_CHAT = "BROADCAST_WECHAT_HUILIAO_CHAT";// 微信汇聊收到新消息通知
	public static final String BROADCAST_WECHAT_NOTICE = "BROADCAST_WECHAT_NOTICE";//微信回来通知公告广播
	public static final String BROADCAST_WECHAT_REPLY = "BROADCAST_WECHAT_REPLY";// 微信汇聊回帖广播
	public static final String BROADCAST_WECHAT_FOCUS = "BROADCAST_WECHAT_FOCUS";//微信关注界面收到新消息广播
	public static final String BROADCAST_WECHAT_PRIVATE = "BROADCAST_WECHAT_PRIVATE";//微信关注界面私聊收到新消息广播
	public static final String BROADCAST_WECHAT_PERSON = "BROADCAST_WECHAT_PERSON";//微信汇聊私聊收到新消息通知

	

	// public static int step = 0;

	/** 真实环境下的push */
	public static final int PUSH_TEST = 200;

	/** 测试环境下的push */
	// public static final int PUSH_TEST = 100;

	public static final String URL_PUSH = "http://push.zritc.com";
	public static final String URL_PUSH_BAK = "http://push.zritc.com";
	// 获取Push信息
	public static final String URL_PUSH_GET = URL_PUSH + "/mget.php?test="
			+ PUSH_TEST;
	public static final String URL_PUSH_GET_BAK = URL_PUSH_BAK
			+ "/mget.php?test=" + PUSH_TEST;

	// 告诉服务器已经获取到哪些Push信息
	public static final String URL_PUSH_CALLBACK_STATUS = URL_PUSH
			+ "/mcbk.php";
	public static final String URL_PUSH_CALLBACK_STATUS_BAK = URL_PUSH_BAK
			+ "/mcbk.php";
	// 初始化时清空服务器上积留的所有Push信息
	public static final String URL_PUSH_CLEAN_ALL = URL_PUSH
			+ "/mcln.php?test=" + PUSH_TEST;
	public static final String URL_PUSH_CLEAN_ALL_BAK = URL_PUSH_BAK
			+ "/mcln.php?test=" + PUSH_TEST;
	// 获取用户的手机号
	public static final String URL_GET_MDN = URL_PUSH + "/getmdn.php?test="
			+ PUSH_TEST + "&imsi=";
	public static final String URL_GET_MDN_BAK = URL_PUSH_BAK
			+ "/getmdn.php?test=" + PUSH_TEST + "&imsi=";

	public static final String RESULT_CODE = "resultcode"; // 返回结果标识
	public static final String RESULT_CODE_SUCCESS = "0000"; // 服务器返回成功
	public static final String RESULT_CODE_FAILURE = "0001"; // 服务器返回失败
	public static final String RESULT_CODE_NO_REGISTER = "0002"; // 后台没绑定

	public static final int DELETE_STORE_Y = 1;// 表示已删除
	public static final int DELETE_STORE_N = 0;
	// 访问服务器后客户端判断结果的类型，用于ReceiveDataListener的ReceiveData中
	public static final int RESULT_OK = 1;
	public static final int RESULT_ERROR = 99;

	public static final String LOCATION_PASSIVE = "Passive"; // 此值是一个Intent的Key，告诉定位service，需要被动定位
	public static final String LOCATION_ACTIVE = "Active"; // 此值是一个Intent的Key，告诉定位service，需要主动定位

	public static final String BROADCAST_LOCATION = "com.gcg.android.location"; // 此值是一个Intent的Key，正在定位
	public static final String BROADCAST_SUBMMIT_FINISH = "com.gcg.android.submit"; // 此值是一个Intent的Key，提交完成

	public static final String RELAY_STATE = "state"; // Intent进入RelayActivity中的KEY
	public static final int RELAY_NOTICE = 1;
	public static final int RELAY_TASK = 2;
	public static final int RELAY_PLAN = 3;
	public static final int RELAY_DOUBLE = 4;

	public static final String BROADCAST_ACTION_PUSH = "com.gcg.grirms.PNARRIVE";
	public static final String BROADCAST_KEY_PUSH = "NOTIFICATION";
	public static final int PUSH_CODE_INIT = 900; // push返回900强制初始化

	/** PUSH 发来的类型 **************/
	public static final int PUSH_NOTIFY = 1; // 下发公告
	public static final int PUSH_TASK = 2; // 下发查询类模块
	public static final int PUSH_LOCATION_RULE = 3; // 下发被动定位规则
	public static final int PUSH_PLAN = 4; // 下发计划，也就是拜访线路
	public static final int PUSH_VISIT_FUNC = 5;
	public static final int PUSH_DICT = 6; // 下发字典表数据
	public static final int PUSH_MENU = 7;
	public static final int PUSH_BASE_FUNC = 8;
	public static final int PUSH_BASE_FIXED = 9;
	public static final int PUSH_ALL = 10;
	public static final int PUSH_OPERATION = 11;
	public static final int PUSH_STORE = 12;
	public static final int PUSH_DOUBLE = 13;
	public static final int PUSH_USER = 14;
	public static final int PUSH_DOUBLE_STRUCT_CHANGE = 15;
	public static final int PUSH_ROLE = 16; // 下发角色信息
	public static final int PUSH_REPORT = 17; // 报表
	public static final int PUSH_AVAIL_DATE = 18; // 用户有效时间
	public static final int PUSH_REPORT_NEW = 19; // 新报表
	public static final int PUSH_INFO = 20;
	public static final int PUSH_BASE_PARAM = 21;
	public static final int PUSH_VISIT_PARAM = 22;
	public static final int PUSH_PSS = 23;// 进销存
	public static final int PUSH_NEW_ATTENDANCE = 26;// 新考勤配置解析
	public static final int PUSH_ORDER2 = 27;// 新订单
	public static final int PUSH_ORDER3 = 31;// 订单3
	public static final int PUSH_ORDER3_SEND = 32;// 送货
	public static final int PUSH_ORDER3_CUXIAO = 33;// 促销
	public static final int PUSH_ORDER3_FENXIAO = 34;// 分销
	public static final int PUSH_CAR_SALES_CONF = 35;// 车销配置
	public static final int PUSH_CAR_SALES_P = 36;// 车销促销
	public static final int IS_ADD_MODULE_STORE = 37;// 新店上报
	public static final int WECHAT_PUSH = 38;// 企业微信
	public static final int MAIL_LIST = 39;// 通讯录
	public static final int PUSH_TODO = 28;// 待办事项
	public static final int PUSH_WEB_REPORT = 29;// FR图表

	public static final int PUSH_SUBMIT = 97;
	public static final int PUSH_UPLOAD_LOG = 98; // log
	public static final int PUSH_ERROR = 99;
	public static final int PUSH_ORG_ALL = 100;

	public static final String NOTIFY_IS_MUST = "ismust"; // 是否必读
	public static final String AWOKE_TYPE = "awoketype"; // 提醒类型 2
															// 表示只铃声，1表示只震动，3表示铃声加震动
	/***********************/

	public static final int VISITWAY_TYPE_EVERY_DAY = 1; // 每天类型
	public static final int VISITWAY_TYPE_EVERY_WEEK = 2; // 每周类型
	public static final int VISITWAY_TYPE_EVERY_MONTH = 3; // 每月类型
	public static final int VISITWAY_TYPE_MOUDLE = 4; // 自定义类型

	// 考勤模块
	public static final String IN_TIME = "intime"; // 上班时间
	public static final String IN_POSITION = "inposition"; // 上班定位地址
	public static final String IN_GIS_X = "ingisx"; // 上班定位经度
	public static final String IN_GIS_Y = "ingisy"; // 上报定位纬度
	public static final String IN_GIS_TYPE = "ingistype"; // 上班定位类型
	public static final String IN_IMAGE = "inimage"; // 上班拍照
	public static final String IN_COMMENT = "incomment"; // 上班说明
	public static final String OUT_TIME = "outtime"; // 下班时间
	public static final String OUT_POSITION = "outposition";// 下班
	public static final String OUT_GIS_X = "outgisx";// 下班
	public static final String OUT_GIS_Y = "outgisy";// 下班
	public static final String OUT_GIS_TYPE = "outgistype";// 下班
	public static final String OUT_IMAGE = "outimage";// 下班
	public static final String OUT_COMMENT = "outcomment";// 下班

	// 新考勤模块
	public static final String NEW_IN_TIME = "over_intime"; // 上班时间
	public static final String NEW_IN_POSITION = "over_inposition"; // 上班定位地址
	public static final String NEW_IN_GIS_X = "over_ingisx"; // 上班定位经度
	public static final String NEW_IN_GIS_Y = "over_ingisy"; // 上报定位纬度
	public static final String NEW_IN_GIS_TYPE = "over_ingistype"; // 上班定位类型
	public static final String NEW_IN_IMAGE = "over_inimage"; // 上班拍照
	public static final String NEW_IN_COMMENT = "over_incomment"; // 上班说明
	public static final String NEW_OUT_TIME = "over_outtime"; // 下班时间
	public static final String NEW_OUT_POSITION = "over_outposition";// 下班
	public static final String NEW_OUT_GIS_X = "over_outgisx";// 下班
	public static final String NEW_OUT_GIS_Y = "over_outgisy";// 下班
	public static final String NEW_OUT_GIS_TYPE = "over_outgistype";// 下班
	public static final String NEW_OUT_IMAGE = "over_outimage";// 下班
	public static final String NEW_OUT_COMMENT = "over_outcomment";// 下班
	public static final String NEW_ATTENDTYPE = "attendtype";
	public static final String NEW_IN_RESULT_TYPE = "over_inresulttype";// 是否迟到
	public static final String NEW_OUT_RESULT_TYPE = "over_outresulttype";// 是否早退
	public static final String NEW_ATTEND_TIME = "attend_time";// 服务端使用，用于区分考勤上下班是一组数据
																// 上班和下班传给服务器上班时间
	public static final String NEW_LATE_DURATION = "late_duration";// 迟到时长
	public static final String NEW_LEAVEEARLY_DURATION = "leaveEarly_duration";// 早退时长

	public static final String ACTION = "action";
	public static final String VERSION = "version";
	public static final String STATUS = "status";
	public static final String ACC = "acc";

	// 考勤
	public static final int ATTENDANCE_STATE_START = 1;// 上班
	public static final int ATTENDANCE_STATE_STOP = 2;// 下班
	public static final int ATTENDANCE_TARGETID = -1;// 考勤任务ID

	public static final int NEW_ATTENDANCE_OVER_START = 3;// 上班加班
	public static final int NEW_ATTENDANCE_OVER_END = 4;// 下班加班
	public static final int NEW_ATTENDANCE_START = 5;
	public static final int NEW_ATTENDANCE_END = 6;
	public static final int NEW_ATTENDANCE_GANG = 7;

	// 补报key值
	public static final String SEARCH_RE_DATE = "searchredate"; // 有效期
	public static final String TASK_ID = "taskid"; // 每条数据的主键，服务器端用来做update动作
	public static final String DATA_STATUS = "status"; // 数据
	public static final String PATCH_ID = "patchid"; // 本条数据创建时间
	public static final String AUTH_USER = "authuser"; // 本条数据创建时间

	// 屏幕分辨率分类
	public static final int RESOLUTION_320_240 = 0;
	public static final int RESOLUTION_480_320 = 1;
	public static final int RESOLUTION_800_480 = 2;
	public static final int RESOLUTION_854_480 = 3;

	// 自定义模块中二级菜单item类型
	public static final int MODULE_TYPE_REPORT = 1;// 上报
	public static final int MODULE_TYPE_ISSUED = 2;// 下发
	public static final int MODULE_TYPE_QUERY = 3;// 查询
	public static final int MODULE_TYPE_VERIFY = 4;// 审核
	public static final int MODULE_TYPE_EXECUTE = 5;// 执行
	public static final int MODULE_TYPE_REASSIGN = 6;// 改派
	public static final int MODULE_TYPE_UPDATE = 7;// 修改
	public static final int MODULE_TYPE_PAY = 8;// 支付
	public static final int MODULE_TYPE_EXECUTE_NEW = 9;// 新双向执行

	// 报表空间类型
	public static final int FUNC_TYPE_TEXT = 1; // 文本框
	public static final int FUNC_TYPE_RANGE_TEXT = 41; // 文本框
	public static final int FUNC_TYPE_SELECT = 3; // 单选下拉框
	public static final int FUNC_TYPE_TIME = 39; // 日期控件（带时间）
	public static final int FUNC_TYPE_RANGE_TIME = 439; // 日期控件（带时间）
	public static final int FUNC_TYPE_DATE = 13; // 日期控件（不带时间）
	public static final int FUNC_TYPE_RANGE_DATE = 413; // 日期控件（不带时间）
	public static final int FUNC_TYPE_MULTI_SELECT = 5; // 多选框

	public static final int PAY_TYPE_PAY = 1;// 支付
	public static final int PAY_TYPE_SEARCH = 2;// 支付查询

	public static final int STORE_INFO_TYPE = 1;// 店面信息修改

	public static boolean PUSH_IS_RUNNING = false; // push是否正在执行

	// 订单模块
	public static final String ORDER_BOUNDLE_TITLE_KEY = "title";
	public static final String ORDER_BOUNDLE_STORE_ID_KEY = "storeid";
	public static final String ORDER_BOUNDLE_ID_KEY = "id";
	public static final String ORDER_BOUNDLE_PRODUCT_ID_KEY = "proid";
	public static final String ORDER_BOUNDLE_PRODUCT_NAME_KEY = "proname";
	public static final String ORDER_BOUNDLE_PRODUCT_PRICE_KEY = "proprice";
	public static final String ORDER_BOUNDLE_PRODUCT_QUANTITY_KEY = "proquantity";

	public static final int ORDER_PRODUCT_STATE_UNIMPLEMENTED = 10;// 未生效
	public static final int ORDER_PRODUCT_STATE_COMPLETING = 51;// 完成中
	public static final int ORDER_PRODUCT_STATE_EXAMINE_OK = 21;// 审批通过
	public static final int ORDER_PRODUCT_STATE_EXAMINE_REFUSE = 22;// 审批驳回
	public static final int ORDER_PRODUCT_STATE_CANCELED = 90;// 被取消
	public static final int ORDER_PRODUCT_STATE_COMPLETED = 99;// 已完成

	// 对齐center / left / right
	public static final String ALIGN_CENTER = "center";// 已完成
	public static final String ALIGN_LEFT = "left";// 已完成
	public static final String ALIGN_RIGHT = "right";// 已完成
}
