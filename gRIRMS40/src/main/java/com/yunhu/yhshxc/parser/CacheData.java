package com.yunhu.yhshxc.parser;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.todo.Todo;
import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.attendance.leave.LeaveInfo;
import com.yunhu.yhshxc.attendance.leave.LeaveInfoDB;
import com.yunhu.yhshxc.attendance.util.SharedPreferencesForLeaveUtil;
import com.yunhu.yhshxc.bo.Content;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.bo.Role;
import com.yunhu.yhshxc.bo.ShiHuaMenu;
import com.yunhu.yhshxc.bo.Task;
import com.yunhu.yhshxc.bo.VisitStore;
import com.yunhu.yhshxc.bo.VisitWay;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncCacheDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.MenuShiHuaDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.NoticeDB;
import com.yunhu.yhshxc.database.RoleDB;
import com.yunhu.yhshxc.database.SlidePictureDB;
import com.yunhu.yhshxc.database.StyleDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.database.VisitStoreDB;
import com.yunhu.yhshxc.database.VisitWayDB;
import com.yunhu.yhshxc.help.SharedPreferencesHelp;
import com.yunhu.yhshxc.location.backstage.BackstageLocationManager;
import com.yunhu.yhshxc.location.backstage.BackstageLocationService;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.nearbyVisit.bo.NearbyListItem;
import com.yunhu.yhshxc.order2.SharedPreferencesForOrder2Util;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.service.DownSlidePicService;
import com.yunhu.yhshxc.style.SlidePicture;
import com.yunhu.yhshxc.style.Style;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.bo.Comment;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.bo.Reply;
import com.yunhu.yhshxc.wechat.bo.Survey;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.db.CommentDB;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.GroupUserDB;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.SurveyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

public class CacheData {

	public static final String CUD_C = "c"; // 新增
	public static final String CUD_U = "u"; // 修改
	public static final String CUD_D = "d"; // 删除
	public static final String CUD_DT = "dt"; // 删除table后添加
	public static final String CUD_ALL = "dall"; // 删除table

	private final int FUNC_TYPE_TEXT = 1; // 文本框
	private final int FUNC_TYPE_PWD = 2; // 密码框
	private final int FUNC_TYPE_SELECT = 3; // 单选下拉框
	private final int FUNC_TYPE_SINGLE_CHOICE_FUZZY_QUERY = 4; // 模糊搜索单选下拉框
	private final int FUNC_TYPE_MULTI_SELECT = 5; // 多选下拉框
	private final int FUNC_TYPE_LINK = 7; // 链接
	private final int FUNC_TYPE_TEXTAREA = 8; // 大文本框
	private final int FUNC_TYPE_LABLE = 9; // 文本标签
	// private final int FUNC_TYPE_CHECKBOX = 10; //多选框
	private final int FUNC_TYPE_HIDDEN = 11; // 隐藏域
	private final int FUNC_TYPE_TIME = 39; // 日期控件（带时间）
	private final int FUNC_TYPE_DATE = 13; // 日期控件（不带时间）
	private final int FUNC_TYPE_CAMERA = 15; // 拍照
	private final int FUNC_TYPE_LOCATION = 16; // 定位
	private final int FUNC_TYPE_SCAN = 17; // 二维码扫描
	private final int FUNC_TYPE_TABLE = 18; // 表格
	private final int FUNC_TYPE_TEXT_NUMERIC = 19; // 表格中数字类型输入框
	private final int FUNC_TYPE_SELECT_OTHER = 21; // 包含输入框的下拉选项
	private final int FUNC_TYPE_SUBMIT_BUTTON = 22; // 提交按钮

	private final int FUNC_TYPE_SELECT_USER = 23; // 表格中用户
	private final int FUNC_TYPE_SELECT_STORE = 24; // 表格中店面

	private final int FUNC_TYPE_STORE_VIEW = 25; // 店面信息（查看）
	private final int FUNC_TYPE_STORE_UPDATA = 26; // 店面信息（修改）
	private final int FUNC_TYPE_HIDDEN_SELECT = 27; // 隐藏域下拉框
	private final int FUNC_TYPE_MULTI_CHOICE_FUZZY_QUERY = 28; // 模糊多选下拉框
	private final int FUNC_TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY = 29; // 模糊下拉框其他
	private final int FUNC_TYPE_LINK_OUTSIDE_DATA = 30; // sql查询超链接
	private final int FUNC_TYPE_HIDDEN_MULTI_SELECT = 31; // 隐藏域多选下拉框
	private final int FUNC_TYPE_RECORD = 32; // 录音
	private final int FUNC_TYPE_ORDER = 33; // 录音
	private final int FUNC_TYPE_PRODUCT_CODE = 34;// 串码
	private final int FUNC_TYPE_SQL_BIG_DATA = 35;// 大数据sql查询
	private final int FUNC_TYPE_CAMERA_MIDDLE = 36; // 拍照中品质
	private final int FUNC_TYPE_CAMERA_HEIGHT = 37; // 拍照高品质
	private final int FUNC_TYPE_CAMERA_CUSTOM = 40;// 自定义拍照压缩类型
	private final int FUNC_TYPE_QUANTITY = 38; //数量控件
	private static final int FUNC_TYPE_ORDER2 = 41;//新订单
	private  final int FUNC_TYPE_ORDER3 = 42;//订单3
	private  final int FUNC_TYPE_ORDER3_SEND = 43;//送货
	private  final int FUNC_TYPE_VIDEO = 44;//视频
	private  final int FUNC_TYPE_SIGNATURE = 46;//视频signature
	private  final int FUNC_TYPE_ATTACHMENT  = 14;//附件


	private final int FUNC_DATATYPE_SMALL_INTEGER = 1; // 小整数（<=4位）
	private final int FUNC_DATATYPE_BIG_INTEGER = 2; // 整数（4到11位）
	private final int FUNC_DATATYPE_DECIMAL = 3; // 小数

	public final String LOGIN_ID = "loginid";
	public final String COMPANY_ID = "companyid";
	public final String COMPANY_NAME = "companyname";
	public final String USER_ID = "userid";
	public final String ROLEID = "roleid";
	public final String ORGID = "orgid";
	public final String USER_NAME = "username";
	public final String USER_ROLE_NAME = "rolename";
	public final String MENU = "menu";
	public final String HEAD_IMG = "headImg";//头像图片
	public final String NICK_NAME = "nickName"; // 昵称
	public final String SIGNATURE = "signature";// 个人签名
	public final String ORG_NAME = "orgName";// 部门名称
	public final String MENU_SHIHUA = "menu_folder";//石化menu菜单


	public final String MODULE_TYPE = "module_type";// 模块区分
	public final String BASE_TIME = "base_time";// 基准时间
	public final String PHONE_SERIAL_NO = "phone_serial_no";// 排序

	public final String MOD = "mod";
	public final String VISIT = "visit";
	public final String BASE_FUNC = "basefunc";
	public final String VISIT_FUNC = "visitfunc";
	public final String DICT = "dict";
	private final String IS_VISIT = "isvisit";
	private final String IS_NOTIFY = "isnotify";
	private final String IS_STORE_ADD_MOD = "isStoreAddMod"; // 是否有新店面上报 0：无 1：有
//	public final String CONF_CUSTOM_NAME = "conf_custom_name";//店面拓展模块名称
	public final String NEW_STORE_TITLE="newStoreTitle";
	public final String PHONE_FIXED_MODULE_NAME = "phone_fixed_module_name"; // 1:拜访,2:旧考勤,3:考勤,4:公告,5:帮助,6:圈子

	private final String IS_BBS = "isbbs";
	private final String IS_ATTENDANCE = "isattend";
	private final String IS_HELP = "ishelp";

	private final String ID = "id";
	private final String TYPE = "type";
	private final String NAME = "name";
	private final String SHOW_COLOR = "show_color";
	private final String DATA_TYPE = "datatype";
	private final String STORE = "store";
	private final String ORDER = "order";
	private final String PLAN_ID = "planid";
	private final String IS_SCAN = "is_scan";// 预值数据模块是否有扫描（0：否、1：是）
	private final String SCAN_STATUS = "scan_status"; // 扫描预值数据模块的数据状态
	private final String SCAN_COLS = "scan_cols";// 扫描对应的sqlId

	// private final String AWOKE_TYPE = "awokeType";
	private final String INTERVAL_TYPE = "intervaltype"; // 目标时间间隔（1：每日、2：每周、3：每月、4：自定义）
	private final String WEEKLY = "weekly"; // "每周几（7天的定义连接，每天占一个字符，0：没有被选择、1：被选择）例，0110101。"
	private final String FROM = "from"; // 目标时间自定义From
	private final String TO = "to"; // 目标时间自定义To
	private final String START_DATE = "startdate"; // 开始日期（当日时用）
	private final String TIME = "time";// 店的最后提交时间
	private final String CYCLE_COUNT = "cycle_count";// 周期数
	private final String VISIT_COUNT = "visit_count";// 可拜访次数
	private final String IS_ADD_DEL = "is_add_del";//设置是否允许删除或添加行，允许删除允许添加为『11』，不允许删除不允许添加为『00』（普通表格不设置）
	private final String IS_CACHE_FUNC="is_cache";//是否是缓存控件
	private final String  RESTRICT_TYPE="restrict_type";// 时间限制类型1：天、2：月 
	private final String  RESTRICT_RULE="restrict_rule";//  时间限制规则:天（1-5,8-10,15）、月（1-3,5,12）
	public final String ATTEND_MUST_ITEMS = "attend_must_items";

	private final String WAY_ID = "wayid";
	public final String STORE_INFO_ID = "exp";
	private final String TARGET_ID = "targetid";
	public final String LOC_LEVEL = "loc_level";
	private final String IS_CHECKIN = "ischeckin";
	private final String IS_CHECKOUT = "ischeckout";
	private final String STORE_LON = "storelon";
	private final String STORE_LAT = "storelat";
	private final String IS_CHECK = "ischeck";
	private final String STROE_DISTANCE = "stroe_distance";
	private final String IS_NO_WAIT = "is_no_wait";// 是否无等待提交
	private final String PHONE_USABLE_TIME = "phone_usable_time";// 手机段可用时间段
	private final String VISIT_COUNTS = "submit_count";// 手机已经拜访的次数


	private final String LOC_TYPE = "loc_type";// 定位方式 1仅GPS 2所有 默认为2
	private final String IS_ADDR = "is_addr";// 显示地址详细 0不显示 1显示 默认为1
	private final String IS_ANEW_LOC = "is_anew_loc";// 超出范围要重新定位 0不需要重新定位
														// 1需要重新定位 默认为0
	private final String LOC_CHECK_DISTANCE = "check_in_location_value";// 定位控件的检测距离

	private final String AREA_SEARCH_VALUE = "area_search_value";// 机构店面距离
	private final String IS_AREA_SEARCH = "is_area_search";// 是否使用机构店面距离

	private final String CODE_CONTROL = "compare_result"; // 串码比对结果（1：不显示不控制、2：显示不控制、3：显示控制，4不显示控制）
	private final String CODE_UPDATE = "is_code_update";// 串码已使用更新（1：不更新、2：查看属性时更新、3：提报后更新）//服务端使用

	private final String LENGTH = "length";
	private final String IS_EMPTY = "isempty";
	private final String CHECK_TYPE = "checktype";
	private final String NEXT = "next";
	private final String DATA = "data";
	private final String DEFAULT_TYPE_VALUE = "defaulttype";
	private final String DEFAULT_VALUE = "defaultvalue";
	private final String DICT_TABLE = "dict_table";
	private final String DICT_DATA_ID = "dict_data_id";
	private final String DICT_COLS = "dict_cols";
	private final String COLS = "cols";
	public static final String DICT_ID = "did";
	public static final String DATA_N = "data_";
	private static final String TASK_STATUS = "task_status";//任务上报控件所属任务（1：主任务M、2：子任务S）

	// ----------字典表排序-----------
	public final String DICT_SORTS = "dict_sorts"; // 字典排序
	public final String DICT_SORTS_CLOS = "dict_sorts_clos"; // 字典排序列

	public final String MENU_ID = "menuid";
	public static Task currentTask;
	public static int currentTargetId;

	// ---------公告----------
	public final String NOTIFY = "notify";
	private final String NOTIFY_CONTENT = "content";
	private final String NOTIFY_TITLE = "title";
	private final String NOTIFY_ISSUER_TIME = "issuertime";
	private final String NOTIFY_ISSUER_NAME = "issuername";
	private final String NOTIFY_ISSUER_ORG = "issuerorg";
	private final String NOTIFY_READ = "isread";
	private final String NOTIFY_TYPE = "dataType";// s：系统公告 n：普通公告
	private final String NOTIFY_ATTACHMENT = "attachment";// 公告附件

	// ---------自定义----------
	public final String TASK = "task";
	private final String MODULE_ID = "moduleid";
	private final String TASK_ID = "taskid";
	private final String TASK_CONTENT = "content";
	private final String TASK_TITLE = "title";
	private final String TASK_CREATE_TIME = "createtime";
	private final String TASK_CREATE_NAME = "createuser";
	private final String OPERATION = "operation"; // 响应操作（1：机构联动、3：机构店面联动）
	private final String OPERATION_LEVEL = "operationlevelnum"; // 3：机构店面联动
	private final String REPLENISH_ABLE_STATUS = "serviceablestatus"; // 可使用的状态（存储格式是“0,1,2...”）
	private final String WEDORDER = "phoneorder";
	private final String DUP_ALLOW_DAYS = "dupallowdays";
	private final String DUP_ALLOW_TIMES = "dupallowtimes";
	private final String IS_SEARCH_MODIFY = "is_search_modify"; // 查询/修改历史数据（0：不可查/修改、1：可查询、2：可修改，默认为0）
	private final String INPUT_ORDER = "input_order"; // 输入顺序
	private final String ENTER_MUST_LIST = "enter_must_list"; // 必须输入的条件（格式：data_189(控件)|n(1：等于、2：不等于、3：大于、4：大于等于、5：小于、6：小于等于)|X（输入值））
	private final String IS_SEARCH_MUL = "is_search_mul"; // is_search_mul 是否查询条件多选（0：否、1：是）
	private final String PRINT_ALIGNMENT = "print_alignment";//对齐方式（1：靠左、2：靠右、3：居中）
	
	
	private final String IS_FUZZY = "is_fuzzy"; // is_fuzzy 是否模糊查询（0：否、1：是）
	private final String IS_EDIT = "is_edit";// is_edit 是否可修改（0：不是、1：是）
	private final String IS_IMPORT_KEY = "is_import_key";// is_edit 是否可修改（0：不是、1：是）
	public final String ATTEND_AUTH = "attendAuth";
	public final String PHOTO_TIME_TYPE = "photo_time_type";// 拍照添加时间类型
	public final String PHOTO_LOCATION_TYPE = "photo_location_type";// 拍照添加时间类型
	public final String PHOTO_FLG = "photo_flg";//拍照类型 1：相机现场拍照、2：从相册中选取
	public final String IS_IMG_CUSTOM = "is_img_mod";//照片水印内容自定义  1添加 0不添加
	public final String IS_IMG_STORE = "is_img_store";//照片水印内容自定义  1添加 0不添加
	public final String IS_IMG_USER = "is_img_user";//照片水印内容自定义  1添加 0不添加
	
	

	// -------定位规则-------
	private final String LOCATION_START_TIME = "starttime"; // 开始时间
	private final String LOCATION_END_TIME = "endtime"; // 结束时间
	private final String LOCATION_INTERVAL_TIME = "intervaltime"; // 间隔时间
	private final String LOCATION_WEEKLY = "weekly"; // 每周几（7天的定义连接，每天占一个字符，0：没有被选择、1：被选择）例，0110101。
	private final String LOCATION_STATUS = "status"; // 任务状态（0：禁用；1：启用）
	public final String LOCATION_TASK = "locationtask";
	/*
	 * 被动定位提示方式 
	 * 0：每次定位前提示
	 * 1：只在第一次提示，当有新规则下达时要提示如果不清除缓存重新初始化后要提示，如果不清除缓存而apk版本更新不提示
	 * 2：只在第一次提示，即使有新规则下达也不再提示，如果清除缓存重新初始化后要提示，如果不清除缓存apk版本更新不提示
	 * 3：直接定位，永远不提示
	 * 无论下达新定位规则 版本更新 初始化 一概不提示
	 */
	public final String LOCATION_TIP_TYPE = "loc_tips";

	public static final String AVAIL_START = "availstart"; // 有效开始日
	public static final String AVAIL_END = "availend"; // 有效结束日

	private final String DOUBLE_DATA = "doubledata"; // 数据
	private final String DOUBLE_TABLE = "doubletable"; // 表名
	private final String DOUBLE_COLS = "doublecols"; // 表列
	public final String DOUBLE = "double"; // 数据
	public static final String DOUBLE_COLUMN_TASKID = "taskid"; // id
	public static final String DOUBLE_COLUMN_TIMESTAMP = "createtime"; // 时间
	public static final String DOUBLE_COLUMN_CREATERUSER = "createuser"; // 时间
	public static final String DOUBLE_COLUMN_TASK_NO = "task_no";
	public static final String DOUBLE_COLUMN_DATA_STATUS = "data_status";
	public static final String DOUBLE_COLUMN_TATOL = "tatol";
	public static final String DOUBLE_COLUMN_CUT = "cut";

	// private final String IS_UPDATE = "isupdate"; // 是否允许补报（0：不允许、1：允许）
	private final String IS_LIST_DISP = "islistdisp"; // 移动端是否列表显示(0：不在列表中显示、1：在列表中显示)
	private final String IS_SEARCH = "issearch"; // 移动端是否查询条件(0：不在查找条件中出现、1：在查找条件中出现)

	private final String STATUS = "status"; // 提交按钮值
	// private final String BTN_MEANING= "restatus"; //提交按钮值的含义

	// 报表
	public  final String REPORT = "report";
	public  final String REPORT2 = "report2";
	private final String REPORT_NAME = "report_name";
	public  final String REPORT_WHERE = "reportwhere";
	public  final String REPORT_WHERE2 = "reportwhere2";

	// 模块权限
	private final String MOD_AUTH_ISSUER = "auth_mobile_issuer";
	private final String MOD_AUTH_QUERY = "auth_mobile_query";
	private final String MOD_AUTH_UPDATE = "auth_mobile_update";
	private final String MOD_AUTH_AUDIT = "auth_mobile_audit";
	private final String MOD_AUTH_REPORT = "auth_mobile_report";
	private final String MOD_AUTH_REDESIGN = "auth_mobile_redesign";
	private final String MOD_AUTH_EXECUTE = "auth_mobile_execute";
	private final String MOD_AUTH_PAY = "auth_mobile_pay";
	private final String AUTH_ISSUER = "auth_issuer"; // 下发权限
	private final String AUTH_QUERY = "auth_query"; // 查询权限
	private final String AUTH_AUDIT = "auth_audit"; // 审核权限
	private final String AUTH_UPDATE = "auth_update"; // 修改权限
	private final String ORG_ID_ISSUER = "org_id_issuer";
	private final String ORG_ID_QUERY = "org_id_query";
	private final String ORG_ID_AUDIT = "org_id_audit";
	private final String ORG_ID_UPDATE = "org_id_update";
	private final String MOD_NAME_ISSUER = "issuer_btn_nm";
	private final String MOD_NAME_REPORT = "report_btn_nm";
	private final String MOD_NAME_QUERY = "query_btn_nm";
	private final String MOD_NAME_AUDIT = "audit_btn_nm";
	private final String MOD_NAME_REDESIGN = "redesign_btn_nm";
	private final String MOD_NAME_UPDATE = "update_btn_nm";
	private final String MOD_NAME_EXECUTE = "execute_btn_nm";
	private final String MOD_NAME_PAY = "pay_btn_nm";
	private final String MOD_IS_CANCEL = "is_cancel";
	private final String CODE_TYPE = "code_type";// 串码类型
	private final String PHONE_TASK_FUNS = "phone_task_funs";//新双向按钮
	private final String DYNAMIC_STATUS = "dynamic_status";//新双向动态按钮
	private final String IS_REPORT_TASK = "is_report_task";//新双向任务是否可多上报

	// 角色
	public  final String ROLE = "role";
	private final String ROLE_ORG_LEVEL = "org_level";
	private final String ROLE_NAME = "name";
	private final String ROLE_ID = "role_id";

	private final String PHONE_COL_WIDTH = "phone_col_width";

	private Context context;
	private MainMenuDB mainMenuDB; // 主页面DB
	private VisitWayDB visitWayDB; // 线路DB
	private VisitStoreDB visitStoreDB; // 店面DB
	private FuncDB funcDB; // 控件DB
	private VisitFuncDB visitFuncDB; // 访店控件DB
	private DictDB dictDB; // 字典表DB
	private NoticeDB noticeDB; // 字典表DB
	private TaskDB taskDB; // 字典表DB
	private DoubleDB doubleDB; // 双向DB
	private ModuleDB moduleDB;
	private RoleDB roleDB;
	private LeaveInfoDB leaveDB;
	private MenuShiHuaDB menuShiHuaDB;//menu石化db

	public final String HELP_NAME = "service";
	public final String HELP_TEL = "phone";

	public final String TRACE_FENCE = "trace_fence"; // 轨迹定位的允许栅栏值

	public final String PHONE_LOGO_PATH = "phone_logo_path";// 手机端企业Logo
	public final String PHONE_NAME1 = "phone_name1";// 手机端系统名称（文字1）
	public final String PHONE_NAME1_SIZE = "phone_name1_size";// 手机端系统名称（文字大小1）
	public final String PHONE_NAME1_ALIGN_TYPE = "phone_name1_align_type";// 手机端系统名称（对齐方式1）（1、居左；2、居中；3、居右）
	public final String PHONE_NAME2 = "phone_name2";// 手机端系统名称（文字2）
	public final String PHONE_NAME2_SIZE = "phone_name2_size";// 手机端系统名称（文字大小2）
	public final String PHONE_NAME2_ALIGN_TYPE = "phone_name2_align_type";// 手机端系统名称（对齐方式2）（1、居左；2、居中；3、居右）
	public final String PHONE_NAME3 = "phone_name3";// 手机端系统名称（文字3）
	public final String PHONE_NAME3_SIZE = "phone_name3_size";// 手机端系统名称（文字大小3）
	public final String PHONE_NAME3_ALIGN_TYPE = "phone_name3_align_type";// 手机端系统名称（对齐方式3）（1、居左；2、居中；3、居右）

	public final String VOICE_TIME = "voice_time"; // 音频时间

	public final String PSS = "pss";// 进销存模块

	public final String LOCATION_VAILD_ACC = "location_acc";

	public final String ATTENDANCE_IN_WORK = "inwork";
	public final String ATTENDANCE_OUT_WORK = "outwork";// 0是未打卡 1是已经打卡

	public final String NEW_ATTENDANCE_IN_WORK = "over_inwork";// 上班
	public final String NEW_ATTENDANCE_OUT_WORK = "over_outwork";// 下班 0是未打卡
																	// 1是已经打卡
	public final String NEW_ATTENDANCE_IN_WORK_OVER = "over_inwork_exp";// 开始加班
	public final String NEW_ATTENDANCE_OUT_WORK_OVER = "over_outwork_exp";// 结束加班
																			// 0是未打卡
																			// 1是已经打卡

	
	
	public final String MAX_TIME_ON = "max_time_on";//新考勤正常上下班区别一条数据的时间
	public final String MAX_TIME_OFF = "max_time_off";//新考勤加班上下班区别一条数据的时间
	
	public final String COMFILES = "comFiles";
	
	public final String NEW_ATTENDANCE_OVERCONF = "overconf";
	public final String NEW_ATTENDANCE_WORK_DAY = "workday";// 上班时间
	public final String NEW_ATTENDANCE_WORK_TIME = "worktime";// 工作时间
	public final String NEW_ATTENDANCE_OVERTIME_NAME = "attend_overtime_name";// 加班名称
	public final String NEW_ATTENDANCE_RESET_TYPE  = "reset_type";// 上下班复位方式（1：按天自动复位、2：都完成后复位）
	private final String NEW_IS_ATTENDANCE = "over_isattend";// 新考勤显示 0：禁用 1：激活
	public final String NEW_ATTENDAUTH = "over_attendAuth"; // 新考勤权限
	private final String PHONE_USABLE_TIME_OLD = "phone_usable_time_old"; // 旧考勤可用时间段
	private final String PHONE_USABLE_TIME_NEW = "phone_usable_time_new"; // 新考勤可用时间段
	
	private final String NEW_ATTENDANCE_INTERVAL_TIME = "interval_time"; // 查岗间隔时间
	private final String NEW_ATTENDANCE_EXP_TIME = "exp_time"; // 例外时间段（格式 12:00,1.0|14:00,0.5）
	
	public final String NEW_ATTENDANCE_GANG = "gang";
	public final String NEW_ATTENDANCE_OVERTIMENAME = "overtimeName";
	public final String NEW_ATTENDANCE_GANGNAME = "gangName";
	public final String NEW_ATTENDANCE_GONAME = "goName";
	public final String NEW_ATTENDANCE_FINISHNAME = "finishName";
	
	
	
	
	private final String ATTENDANCE_SERVICE_FLG = "service_flg";
	private final String ATTENDANCE_WEEKS = "weeks";
	private final String ATTENDANCE_ATTEND_TIME = "attend_time";
	
	public final String OVER_ATTEND_WAIT = "over_attend_wait";//新考勤是否需要无等待(0：不需要、1：需要，默认为1)
	public final String ATTEND_WAIT = "attend_wait";//旧考勤是否需要无等待(0：不需要、1：需要，默认为1)

	//请假参数配置
	public final String IS_LEAVE="isLeave";// 是否有请假 1：有 0：无
	private final String IS_QUERY="isQuery";// 是否查询 1：有 0：无
	private final String IS_INSERT="isInsert";// 是否上报 1：有 0：无
	private final String IS_UPDATE="isUpdate";//是否修改 1：有 0：无
	private final String IS_AUDIT="isAudit";// 是否审核 1：有 0：无

	private final String ENABLE_TIME = "unable_time"; //下订单可用时间, 
	private final String IS_PHONE_PRICE = "is_phone_price"; // 手机端价格的显示控制（1、显示、不可修改；2、显示、可修改；3、不显示；默认：2）
	private final String IS_PHONE_PRINT = "is_phone_print"; //是否需要手机打印（1、不要；2、要；默认：1）
	private final String IS_PRICE = "is_price"; //是否配送单上显示价格（1、不要；2、要；默认：2）
	private final String IS_STOCKS = "is_stocks"; //下订单时库存参考（1、不需要；2、需要；默认：1）
	private final String IS_PAY = "is_pay"; //是否需要支付（1、不要；2、要；默认：2）
	private final String IS_AMOUNT = "is_amount"; //是否配送单上显示金额（1、不要；2、要；默认：2）
	private final String IS_PLACE_USER = "is_place_user"; //是否需要订货联系人（1、不要；2、要；默认：2）
	private final String IS_RECEIVE_USER = "is_receive_user"; //是否需要收货联系人（1、不要；2、要；默认：2）
	private final String PRICE_CTRL = "price_ctrl"; //价格控件ID（格式：模块ID|控件ID）
	private final String CODE_CTRL = "code_ctrl"; //二维码控件ID（格式：模块ID|控件ID）
	private final String UNIT_CTRL = "unit_ctrl"; //计量单位控件（格式：模块ID|控件ID）
	private final String PRODUCT_CTRL = "product_ctrl";// 产品名称控件（格式：第1级模块ID|控件ID,第2级模块ID|控件ID,第3级模块ID|控件ID）
	public final String ORDER_NEW = "orderNew";
	public final String ORDER_MENU =  "is_order_new";
	
	
	public final String ORDER_3 =  "is_order_three";
	public final String ORDER_3_SEND =  "is_send";
	public final String THREE_ORDER =  "three_order";
	public final String THREE_PROMOTION =  "three_promotion";
	public final String ORDERDIS =  "orderDis";
	public final String IS_INPUT =  "is_input";// 是否必须填报，0：不必须（默认）、1：必须
	
	public final String CAR_SALES = "is_car";
	
	public final String CAR_CONF = "car_conf";
	public final String CAR_PROMOTION = "car_promotion";
	
	//代办事项
	private final String IS_TODO = "is_todo";
	public final String TO_DO = "todo";
	public final String TO_DO_DATA = "data";
	public final String TO_DO_TIME = "time";
	private final String M_NAME = "m_name";
	private final String S_NAME = "s_name";
	private final String TODO_NUM = "todo_num";
	
	public final String WEB_REPORT = "repmenu";
	
	//UI样式
	public final String STYLE = "style";
	public final String STYLE_MODULE_ID = "module_id";
	public final String STYLE_STYLE_TYPE = "style_type";
	public final String STYLE_IMG_URL = "icon_image";
	public final String STYLE_IMG_MD5 = "img_md5";
	public final String STYLE_BG_COLOR = "bg_color";
	public final String STYLE_BG_IMAGE = "bg_image";
	public final String STYLE_TYPE = "type";


	//nearby
	public final String NEARBY_CONF = "nearby_conf";//就近拜访配置项
	public final String NEARBY_STYLE = "style";
	public final String NEARBY_MODULE_ID = "module_id";//模块ID
	public final String STORE_LIST_SQL = "store_list_sql";//取店面列表用SQL ID
	public final String STORE_DETAIL_SQL = "store_detail_sql";//取店面详细用SQL ID
	public final String NEARBY_DATA_STATUS = "data_status";//数据状态
	public final String IS_MDL = "is_mdl";//是否需要模板
	public final String IS_DATA_CPY = "is_data_cpy";//是否有数据复制
	public final String NEARBY_STORE_INFO = "store_info";//店面属性信息（store_info，1：查看、2：修改）
	public final String BTN_VISIT = "btn_visit";//拜访按钮名称
	public final String BTN_MDL = "btn_mdl";//数据模版按钮
	public final String BTN_HISTRY = "btn_histry";//历史数据按钮
	public final String BTN_STORE = "btn_store";//店面属性按钮
	
	public final String ATTEND_FUNC = "attend_func";//（0：不要排班、1：要排班）
	public final String PAI_FLG = "pai_flg";
	
	
	public final String WEI_CHAT = "is_weChat";//企业微信
	
	public final String IS_MAIL_LIST = "isMailList";//通讯录
	
	public final String QUESTION = "question";//问卷调查
	
	//工作计划
	public final String IS_WORK_PLAN= "isWorkPlan"; // 是否有工作计划（0：不要、1：要）
	public final String IS_WORK_SUM= "isWorkSum"; // 是否有工作总结（0：不要、1：要）
	public final String WORK_PLAN_RULE= "work_plan_rule"; // 工作计划提醒规则   提醒时间段小时格式 ：  08-17
	public final String WORK_PLAN_AUTH="workPlan_auth";//工作计划权限

	//轮播图url列表,用于下载配置的轮播图在首页进行展示
	private final String ADV_PICTURE = "adv_picture";

	public final String QUESTIONNAIRE="isQuestQuery"; //解析调查问卷菜单排序

	private Handler handler = null;

	public CacheData(Context context) {
		init(context);
	}

	public CacheData(Context context, Handler handler) {
		this.handler = handler;
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		mainMenuDB = new MainMenuDB(context);
		visitWayDB = new VisitWayDB(context);
		visitStoreDB = new VisitStoreDB(context);
		visitFuncDB = new VisitFuncDB(context);
		funcDB = new FuncDB(context);
		dictDB = new DictDB(context);
		noticeDB = new NoticeDB(context);
		taskDB = new TaskDB(context);
		doubleDB = new DoubleDB(context);
		moduleDB = new ModuleDB(context);
		roleDB = new RoleDB(context);
		leaveDB = new LeaveInfoDB(context);
		menuShiHuaDB=new MenuShiHuaDB(context);
	}

	/**
	 * 初始化所有数据
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseAll(String json) throws JSONException {
		parse(json, false);
	}

	/**
	 * 初始化所有数据
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseAll(String json, boolean isDelDict) throws JSONException {
		parse(json, isDelDict);
	}

	public void parse(String json, boolean isDelDict) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(COMPANY_ID)) {
			SharedPreferencesUtil.getInstance(context).setCompanyId(
					jsonObject.getInt(COMPANY_ID));
		}
		if (jsonObject.has(VOICE_TIME)) {
			SharedPreferencesUtil.getInstance(context).setVoiceTime(
					jsonObject.getInt(VOICE_TIME) + "");
		}
		if (jsonObject.has(COMPANY_NAME)) {
			SharedPreferencesUtil.getInstance(context).setCompanyName(
					jsonObject.getString(COMPANY_NAME));
		}

		if (jsonObject.has(PHONE_FIXED_MODULE_NAME)) {
			SharedPreferencesUtil.getInstance(context).setFixedName(
					jsonObject.getString(PHONE_FIXED_MODULE_NAME));
		}

		if (isValid(jsonObject, LOCATION_VAILD_ACC)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setLocationValidAcc(jsonObject.getInt(LOCATION_VAILD_ACC));
		}
		
		if (isValid(jsonObject, LOC_LEVEL)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setLocLevel(jsonObject.getString(LOC_LEVEL));
		}

		if (jsonObject.has(LOGIN_ID)) {
			SharedPreferencesHelp.getInstance(context.getApplicationContext())
					.setLoginId(jsonObject.getString(LOGIN_ID));
		}
		if (jsonObject.has(USER_ID)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setUserId(jsonObject.getInt(USER_ID));
		}
		if (jsonObject.has(ROLEID)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setRoleId(jsonObject.getInt(ROLEID));
		}
		if (jsonObject.has(ORGID)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setOrgId(jsonObject.getInt(ORGID));
		}
		if (jsonObject.has(USER_NAME)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setUserName(jsonObject.getString(USER_NAME));
		}
		if (jsonObject.has(USER_ROLE_NAME)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setUserRoleName(jsonObject.getString(USER_ROLE_NAME));
		}
		// if(jsonObject.has(AVAIL_START)){
		// SharedPreferencesUtil.getInstance(context.getApplicationContext()).setUsefulStartDate(jsonObject.getString(AVAIL_START));
		// }
		// if(jsonObject.has(AVAIL_END)){
		// SharedPreferencesUtil.getInstance(context.getApplicationContext()).setUsefulEndDate(jsonObject.getString(AVAIL_END));
		// }
		if (jsonObject.has(HELP_NAME)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setHelpName(jsonObject.getString(HELP_NAME));
		}
		if (jsonObject.has(HELP_TEL)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setHelpTel(jsonObject.getString(HELP_TEL));
		}
		if (jsonObject.has(STORE_INFO_ID)) {
			SharedPreferencesUtil2.getInstance(context.getApplicationContext())
					.saveStoreInfoId(jsonObject.getInt(STORE_INFO_ID));
		}
		if (jsonObject.has(ATTEND_AUTH)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.saveAttendAuth(jsonObject.getString(ATTEND_AUTH));
		}

		if (jsonObject.has(NEW_ATTENDAUTH)) { //workPlan_auth
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.saveNewAttendAuth(jsonObject.getString(NEW_ATTENDAUTH));
		}

		if (jsonObject.has(ATTEND_FUNC)) {
			SharedPrefsAttendanceUtil.getInstance(context.getApplicationContext()).setAttendFunc(jsonObject.getString(ATTEND_FUNC));
		}
		if (jsonObject.has(PAI_FLG)) {
			SharedPrefsAttendanceUtil.getInstance(context.getApplicationContext()).setPaiFlg(jsonObject.getString(PAI_FLG));
		}
		if (jsonObject.has(PHONE_LOGO_PATH)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneLogoPath(jsonObject.getString(PHONE_LOGO_PATH));
		}

		if (jsonObject.has(PHONE_NAME1)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName1(jsonObject.getString(PHONE_NAME1));
		}

		if (jsonObject.has(PHONE_NAME1_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName1Size(jsonObject.getString(PHONE_NAME1_SIZE));
		}

		if (jsonObject.has(PHONE_NAME1_ALIGN_TYPE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName1AlignType(
							jsonObject.getString(PHONE_NAME1_ALIGN_TYPE));
		}
		if (jsonObject.has(PHONE_NAME2)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName2(jsonObject.getString(PHONE_NAME2));
		}

		if (jsonObject.has(PHONE_NAME2_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName2Size(jsonObject.getString(PHONE_NAME2_SIZE));
		}

		if (jsonObject.has(PHONE_NAME2_ALIGN_TYPE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName2AlignType(
							jsonObject.getString(PHONE_NAME2_ALIGN_TYPE));
		}
		if (jsonObject.has(PHONE_NAME3)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName3(jsonObject.getString(PHONE_NAME3));
		}

		if (jsonObject.has(PHONE_NAME3_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName3Size(jsonObject.getString(PHONE_NAME3_SIZE));
		}

		if (jsonObject.has(PHONE_NAME3_ALIGN_TYPE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName3AlignType(
							jsonObject.getString(PHONE_NAME3_ALIGN_TYPE));
		}

		if (isValid(jsonObject, TRACE_FENCE)) {
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).saveTraceFence(
					jsonObject.getInt(TRACE_FENCE));
		}
		
		if(isValid(jsonObject, NEW_STORE_TITLE)){
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setXDSB(jsonObject.getString(NEW_STORE_TITLE));
		}

		parseISVisit(jsonObject); // 主页面是否显示访店模块
		parseISNotify(jsonObject);// 主页面是否显示公告模块
		parseIsAttendance(jsonObject);// 主页面是否显示考勤模块
		parseIsNewAttendance(jsonObject);
		parseISBBS(jsonObject); // 主页面是否显示圈子模块
		parseIsHelp(jsonObject); // 主页面是否显示帮助模块
		parseIsToDo(jsonObject);//主页是否显示待办事项
		parserStyle(jsonObject);//样式
		parserNearbyConf(jsonObject);//就近拜访
		parseISStoreAddMod(jsonObject);//新店上报
		parserWeiChat(jsonObject);//解析企业微信
		parserIsMailList(jsonObject);//解析通讯录
		parserQuestion(jsonObject); //解析调查问卷
		parseisLeave(jsonObject);//解析请假
		parseIsCompanyWeb(jsonObject);//解析公司指定展示的网址(暂时只有皇明的Email)
		parserWorkPlan(jsonObject);//解析工作计划
		parserWorkSum(jsonObject);//解析工作总结
		parserPictures(jsonObject);//解析首页轮播图
		parserHY(jsonObject);//解析会议日程
		if (jsonObject.has(ATTEND_MUST_ITEMS)) {
			SharedPreferencesUtil.getInstance(context).setAttendanceMustDo(jsonObject.getString(ATTEND_MUST_ITEMS));
			SharedPreferencesUtil.getInstance(context).setAttendanceMustDoNew(jsonObject.getString(ATTEND_MUST_ITEMS));
		}

		if (jsonObject.has(MENU)) {
			String jsonForMenu = jsonObject.getString(MENU);
			parseMenu(jsonForMenu);
		}
		if (jsonObject.has(MOD)) {
			String jsonForMOD = jsonObject.getString(MOD);
			parseMod(jsonForMOD);
		}
		if (jsonObject.has(VISIT)) {
			String jsonForVisit = jsonObject.getString(VISIT);
			parseVisit(jsonForVisit);
		}
		if (jsonObject.has(BASE_FUNC)) {
			String jsonForBasefunc = jsonObject.getString(BASE_FUNC);
			parseBaseFunc(jsonForBasefunc);
		}
		if (jsonObject.has(VISIT_FUNC)) {
			String jsonForVisitFunc = jsonObject.getString(VISIT_FUNC);
			parseVisitFunc(jsonForVisitFunc);
		}
		if (jsonObject.has(DICT)) { // 要在解析控件之后
			String jsonForDict = jsonObject.getString(DICT);
			parseDictionary(jsonForDict, isDelDict);
		}
		if (jsonObject.has(NOTIFY)) {
			String jsonForDict = jsonObject.getString(NOTIFY);
			parseNotify(jsonForDict);
		}
		if (jsonObject.has(TASK)) {
			String jsonForDict = jsonObject.getString(TASK);
			parseCustom(jsonForDict);
		}
		if (jsonObject.has(LOCATION_TASK)) {
			String jsonLocationRule = jsonObject.getString(LOCATION_TASK);
			parseLocationRule(jsonLocationRule, true,false);
		}
		if (jsonObject.has(DOUBLE)) {
			String doubleJson = jsonObject.getString(DOUBLE);
			this.parseDouble(doubleJson, false);
		}
		if (jsonObject.has(REPORT)) {
			String jsonReport = jsonObject.getString(REPORT);
			this.parseReport(jsonReport, Menu.TYPE_REPORT);
		}
		if (jsonObject.has(REPORT_WHERE)) {
			SharedPreferencesUtil2.getInstance(context).saveReportWhere(
					jsonObject.getString(REPORT_WHERE));
		}
		if (jsonObject.has(REPORT2)) {
			String jsonReport2 = jsonObject.getString(REPORT2);
			this.parseReport(jsonReport2, Menu.TYPE_REPORT_NEW);
		}
		if (jsonObject.has(REPORT_WHERE2)) {
			SharedPreferencesUtil2.getInstance(context).saveReportWhere2(
					jsonObject.getString(REPORT_WHERE2));
		}
		if (jsonObject.has(WEB_REPORT)) {
			String jsonWebReport = jsonObject.getString(WEB_REPORT);
			this.parseWebReport(jsonWebReport);
		}
		if (jsonObject.has(ROLE)) {
			String jsonReport = jsonObject.getString(ROLE);
			this.parseRole(jsonReport);
		}
		if (jsonObject.has(PSS)) {
			String jsonPss = jsonObject.getString(PSS);
			new ParsePSS(context).pssconfig(jsonPss);
		}
		if (jsonObject.has(ATTENDANCE_IN_WORK)) {
			String jsonInWord = jsonObject.getString(ATTENDANCE_IN_WORK);
			if (!TextUtils.isEmpty(jsonInWord) && jsonInWord.equals("1")) {
				String date = DateUtil.getCurDateTime();
				SharedPreferencesUtil.getInstance(context).setStartWorkTime(date + "do");// 设置上班日期
			}

		}
		if (jsonObject.has(ATTENDANCE_OUT_WORK)) {
			String jsonOutWord = jsonObject.getString(ATTENDANCE_OUT_WORK);
			if (!TextUtils.isEmpty(jsonOutWord) && jsonOutWord.equals("1")) {
				String date = DateUtil.getCurDateTime();
				SharedPreferencesUtil.getInstance(context).setStopWorkTime(date + "do");// 设置下班日期

			}
		}

		if (jsonObject.has(NEW_ATTENDANCE_IN_WORK)) {
			String jsonInWord = jsonObject.getString(NEW_ATTENDANCE_IN_WORK);
			if (!TextUtils.isEmpty(jsonInWord)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceStart(jsonInWord);
			}

		}
		if (jsonObject.has(NEW_ATTENDANCE_OUT_WORK)) {
			String jsonOutWord = jsonObject.getString(NEW_ATTENDANCE_OUT_WORK);
			if (!TextUtils.isEmpty(jsonOutWord)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceEnd(jsonOutWord);
			}
		}

		if (jsonObject.has(NEW_ATTENDANCE_IN_WORK_OVER)) {
			String jsonInWord = jsonObject.getString(NEW_ATTENDANCE_IN_WORK_OVER);
			if (!TextUtils.isEmpty(jsonInWord)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceStartOver(jsonInWord);
			}

		}
		if (jsonObject.has(NEW_ATTENDANCE_OUT_WORK_OVER)) {
			String jsonOutWord = jsonObject.getString(NEW_ATTENDANCE_OUT_WORK_OVER);
			if (!TextUtils.isEmpty(jsonOutWord)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceEndOver(jsonOutWord);
			}
		}

		if (jsonObject.has(NEW_ATTENDANCE_OVERCONF)) {
			String jsonAttendance = jsonObject.getString(NEW_ATTENDANCE_OVERCONF);
			if (!TextUtils.isEmpty(jsonAttendance)) {
				parseNewAttendance(jsonAttendance);
			}
		}
		if (jsonObject.has(LOCATION_TIP_TYPE)) {
			int tipType = jsonObject.getInt(LOCATION_TIP_TYPE);
			SharedPrefsBackstageLocation.getInstance(context).saveLocationTipType(tipType);
			if (tipType == 1) {// 有新规则下达的时候要提示
				SharedPreferencesUtil.getInstance(context).setIsTipUser(false);// 设置成fales表示没有提示用户
			}
		}
		
		if (isValid(jsonObject, OVER_ATTEND_WAIT)) {
			int wait = jsonObject.getInt(OVER_ATTEND_WAIT);
			SharedPreferencesUtil.getInstance(context).setOverAttendWait(wait);
		}else{
			SharedPreferencesUtil.getInstance(context).setOverAttendWait(1);
		}
		if (isValid(jsonObject, ATTEND_WAIT)) {
			int wait = jsonObject.getInt(ATTEND_WAIT);
			SharedPreferencesUtil.getInstance(context).setAttendWait(wait);
		}else{
			SharedPreferencesUtil.getInstance(context).setAttendWait(1);
		}
		
		if (isValid(jsonObject, TO_DO)) {
			String toDoJson = jsonObject.getString(TO_DO);
			parserToDo(toDoJson);
		}
		
		parseOrder2(jsonObject);
		parseOrder3(jsonObject);
		parseOrder3Send(jsonObject);
		parseCarSales(jsonObject);
		parseComFiles(jsonObject);//解析公司资料

	}

	/**
	 * 解析配置的首页轮播图
	 * @param jsonObject
	 */
	public void parserPictures(JSONObject jsonObject) {
		if (jsonObject.has(ADV_PICTURE)){
            //读取图片路径存入数据库
			try {
				JSONArray JsArr = jsonObject.getJSONArray(ADV_PICTURE);

				SlidePictureDB slidePictureDB = new SlidePictureDB(context);
				slidePictureDB.deleteAll();
				for (int i = 0; i < JsArr.length(); i++) {
					String url1 = JsArr.getString(i);
					String picName = url1.substring(url1.lastIndexOf("/")+1);
					SlidePicture slidePicture = new SlidePicture();
					slidePicture.setUrl(url1);
					slidePicture.setSortId(i);
					slidePicture.setSdPath(Constants.ADV_PICTURE);
					slidePicture.setPictureName(picName);
					slidePictureDB.insert(slidePicture);


				}
				//创建文件夹存放图片
				File file = new File(Constants.ADV_PICTURE);
				if (!file.exists()){

					file.mkdirs();
				}

				//开启IntentService进行下载
				List<SlidePicture> spList = slidePictureDB.queryAll();
				if(spList.size()>0){

					Intent intent = new Intent(context, DownSlidePicService.class);
					intent.putExtra("download",true);
					context.startService(intent);
				}


			} catch (JSONException e) {
				e.printStackTrace();

			}


		}
	}


	public void parseComFiles(JSONObject json) throws JSONException {
	if(json!=null&&json.has(COMFILES)){
		JSONArray ComFilesList = json.getJSONArray(COMFILES);
		if(ComFilesList!=null&&ComFilesList.length()>0){

			
			SharedPreferencesUtil.getInstance(context).putInt(COMFILES,ComFilesList.length());
			for(int i= 0;i<100;i++){
				SharedPreferencesUtil.getInstance(context).clear(COMFILES+"name"+i);
				SharedPreferencesUtil.getInstance(context).clear(COMFILES+"url"+i);
	
			}
			for(int i=0;i<ComFilesList.length();i++){//clear
				
				SharedPreferencesUtil.getInstance(context).setString(COMFILES+"name"+i,ComFilesList.getJSONObject(i).getString("name"));
				SharedPreferencesUtil.getInstance(context).setString(COMFILES+"url"+i,ComFilesList.getJSONObject(i).getString("url"));

			}
		}
	}	
	
	
		

	}
	
	/**
	 * 解析公司指定展示的网址(暂时只有皇明的Email)
	 * 根据公司id进行配置插入菜单中
	 * @throws JSONException
	 */
	public void parseIsCompanyWeb(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(COMPANY_ID)) {
			int company_id=jsonObject.getInt(COMPANY_ID);
			SharedPreferencesUtil.getInstance(context).setCompanyId(company_id);
			Menu menu = new Menu();
			if (company_id==Menu.COMPANY_TYPE1) {
				menu.setType(Menu.COMPANY_TYPE1);
				menu.setName(setString(R.string.parser_cache_email));
				mainMenuDB.insertMenu(menu);
			}
			
			
		}
		
		
	}

	public void parseisLeave(JSONObject jsonObject) throws JSONException {
		SharedPreferencesForLeaveUtil.getInstance(context).clearAll();
		if(jsonObject.has(IS_LEAVE)){
			SharedPreferencesForLeaveUtil.getInstance(context).setIS_LEAVE(jsonObject.getString(IS_LEAVE));
		}
		if(jsonObject.has("leaveConf")){
			JSONObject obj = jsonObject.optJSONObject("leaveConf");
				if(PublicUtils.isValid(obj,IS_QUERY )){
					SharedPreferencesForLeaveUtil.getInstance(context).setIS_QUERY(obj.getString(IS_QUERY));
				}
				if(PublicUtils.isValid(obj,IS_INSERT )){
					SharedPreferencesForLeaveUtil.getInstance(context).setIS_INSERT(obj.getString(IS_INSERT));
				}
				if(PublicUtils.isValid(obj,IS_UPDATE )){
					SharedPreferencesForLeaveUtil.getInstance(context).setIS_UPDATE(obj.getString(IS_UPDATE));
				}
				if(PublicUtils.isValid(obj,IS_AUDIT )){
					SharedPreferencesForLeaveUtil.getInstance(context).setIS_AUDIT(obj.getString(IS_AUDIT));
				}
				if(PublicUtils.isValid(obj, "autitBtn")){
					JSONObject btnObj = obj.optJSONObject("autitBtn");
					SharedPreferencesForLeaveUtil.getInstance(context).setAutitBtn(btnObj.toString());
				}
				if(PublicUtils.isValid(obj, "data")){
					JSONArray btnarry = obj.optJSONArray("data");
					leaveDB.delete();
					if(btnarry!=null&&btnarry.length()>0){
						for(int i = 0; i<btnarry.length();i++){
							LeaveInfo leave = new LeaveInfo();
							JSONObject jsonObj = btnarry.getJSONObject(i);
							if(PublicUtils.isValid(jsonObj, "id")){
								leave.setType(jsonObj.getString("id"));
							}
							if(PublicUtils.isValid(jsonObj, "name")){
								leave.setName(jsonObj.getString("name"));
							}
							if(PublicUtils.isValid(jsonObj, "maxDays")){
								leave.setMaxDays(jsonObj.getString("maxDays"));
							}
							leaveDB.insert(leave);
						}
					}
				}
		}
	}

	public boolean isValid(JSONObject jsonObject, String key)throws JSONException {
		boolean flag = false;
		if (jsonObject.has(key) && !jsonObject.isNull(key)) {
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}

	/**
	 * 是否要显示访店
	 * 
	 * @throws JSONException
	 */
	public void parseISVisit(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_VISIT)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_VISIT);
			String visit = jsonObject.getString(IS_VISIT);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_VISIT);
				menu.setName(context.getResources().getString(
						R.string.visit_store));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				if (jsonObject.has(PHONE_USABLE_TIME)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME);
					menu.setPhoneUsableTime(time);
				}
				mainMenuDB.insertMenu(menu);
				SharedPreferencesUtil2.getInstance(context).savePhoneReport(true);
			} else {
				// 取消访店的时候 删除线路表 店面表 控件表
				DatabaseHelper.getInstance(context).deteleVisit();
			}
			// SharedPreferencesUtil.getInstance(context).setIsVisit(jsonObject.getInt("isVisit")==1?true:false);
		}
	}
	
	/**
	 * 是否要显示新店上报
	 * 
	 * @throws JSONException
	 */
	public void parseISStoreAddMod(JSONObject jsonObject) throws JSONException {
//		jsonObject.put(IS_STORE_ADD_MOD, "1");
		if (jsonObject.has(IS_STORE_ADD_MOD)) {
			mainMenuDB.removeMenuByType(Menu.IS_STORE_ADD_MOD);
			String visit = jsonObject.getString(IS_STORE_ADD_MOD);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1) {
				Menu menu = new Menu();
				menu.setType(Menu.IS_STORE_ADD_MOD);
//				menu.setName(context.getResources().getString(R.string.is_store_add_mod));
				String xdName = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getXDSB();
				if(TextUtils.isEmpty(xdName)){
					menu.setName(context.getResources().getString(R.string.is_store_add_mod));
				}else{
					menu.setName(xdName);
				}
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				//Todo

				menu.setPhoneSerialNo(5);//tempo
				if (jsonObject.has(PHONE_USABLE_TIME)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME);
					menu.setPhoneUsableTime(time);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}
	public static boolean isWechat = false;
	/**
	 * 企业微信
	 * @param jsonObject
	 * @throws JSONException
	 */
	public void parserWeiChat(JSONObject jsonObject) throws JSONException{
		if (jsonObject.has(WEI_CHAT)) {
			isWechat = true;
			mainMenuDB.removeMenuByType(Menu.WEI_CHAT);
			String visit = jsonObject.getString(WEI_CHAT);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1) {
				Menu menu = new Menu();
				menu.setType(Menu.WEI_CHAT);
				menu.setName(context.getResources().getString(R.string.web_chat));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				if (jsonObject.has(PHONE_USABLE_TIME)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME);
					menu.setPhoneUsableTime(time);
				}
				mainMenuDB.insertMenu(menu);
				webConfig(jsonObject);//解析企业微信配置
			}
		}
	}
	
	/**
	 * 解析通讯录
	 * @param jsonObject
	 * @throws JSONException 
	 */
	public void parserIsMailList(JSONObject jsonObject) throws JSONException{
		if (jsonObject.has(IS_MAIL_LIST)) {
			mainMenuDB.removeMenuByType(Menu.MAIN_LIST);
			String visit = jsonObject.getString(IS_MAIL_LIST);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1) {
				Menu menu = new Menu();
				menu.setType(Menu.MAIN_LIST);
				menu.setName(context.getResources().getString(R.string.mail_list));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				if (jsonObject.has(PHONE_USABLE_TIME)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME);
					menu.setPhoneUsableTime(time);
				}
//				mainMenuDB.insertMenu(menu);
				paraerMailList(jsonObject);
			}
		}
		
	}
	
	/**
	 * 解析调查问卷
	 * @param jsonObject
	 * @throws JSONException 
	 */
	public void parserQuestion(JSONObject jsonObject) throws JSONException{
		if (jsonObject.has(QUESTION)) {

			
			JSONArray questionArr = jsonObject.getJSONArray(QUESTION);
			if (questionArr!=null && questionArr.length()>0) {
				mainMenuDB.removeMenuByType(Menu.QUESTIONNAIRE);
				Menu menu = new Menu();
				menu.setType(Menu.QUESTIONNAIRE);
				menu.setName(setString(R.string.parser_cache_02));
				String visit = jsonObject.getString(QUESTIONNAIRE);
				String[] str = visit.split("\\|");
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				mainMenuDB.insertMenu(menu);
			}
//			mainMenuDB.removeMenuByType(Menu.QUESTIONNAIRE);
//			String visit = jsonObject.getString(QUESTIONNAIRE);
//			String[] str = visit.split("\\|");
//			if (Integer.parseInt(str[0]) == 1) {
//				Menu menu = new Menu();
//				menu.setType(Menu.QUESTIONNAIRE);
//				menu.setName(context.getResources().getString(R.string.mail_list));
//				if (str.length == 2) {
//					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
//				} else {
//					menu.setPhoneSerialNo(0);
//				}
//				if (jsonObject.has(PHONE_USABLE_TIME)) {
//					String time = jsonObject.getString(PHONE_USABLE_TIME);
//					menu.setPhoneUsableTime(time);
//				}
//				mainMenuDB.insertMenu(menu);
//				paraerMailList(jsonObject);
//			}
		}
		
	}
	/**
	 * 解析工作计划
	 * @param jsonObject
	 * @throws JSONException 
	 */
	public void parserWorkPlan(JSONObject jsonObject) throws JSONException{
		if (jsonObject.has(IS_WORK_PLAN)) {

			String isWorkPlan = jsonObject.getString(IS_WORK_PLAN);
			String[] str = isWorkPlan.split("\\|");


//			int isWorkPlan = jsonObject.getInt(IS_WORK_PLAN);


			if(Integer.parseInt(str[0]) == 0){
				mainMenuDB.removeMenuByType(Menu.WORK_PLAN);
			}else if(Integer.parseInt(str[0]) ==1){
				mainMenuDB.removeMenuByType(Menu.WORK_PLAN);
				Menu menu = new Menu();
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				menu.setType(Menu.WORK_PLAN);
				menu.setName(setString(R.string.parser_cache_05));
				mainMenuDB.insertMenu(menu);
				if (jsonObject.has(WORK_PLAN_RULE)&&!jsonObject.isNull(WORK_PLAN_RULE)) {
					String tipRule = jsonObject.getString(WORK_PLAN_RULE);
					if (!TextUtils.isEmpty(tipRule)) {						
						SharedPreferencesUtil2.getInstance(context).saveWorkPlanTipRule(tipRule);
					}
				}
				if(jsonObject.has(WORK_PLAN_AUTH)&&!jsonObject.isNull(WORK_PLAN_AUTH)){
					String auth = jsonObject.getString(WORK_PLAN_AUTH);
					if(!TextUtils.isEmpty(auth)){
						SharedPreferencesUtil.getInstance(context).saveWorkPlanAuth(auth);
					}
				}
			}
			
			
			
		}
		
	}
	/**
	 * 解析工作总结
	 * @param jsonObject
	 * @throws JSONException 
	 */
	public void parserWorkSum(JSONObject jsonObject) throws JSONException{
//		if (jsonObject.has(IS_WORK_SUM)) {
//
//			int isWorkPlan = jsonObject.getInt(IS_WORK_SUM);
//			if(isWorkPlan == 0){
//				mainMenuDB.removeMenuByType(Menu.WORK_SUM);
//			}else if(isWorkPlan ==1){
//				mainMenuDB.removeMenuByType(Menu.WORK_SUM);
//				Menu menu = new Menu();
//				menu.setType(Menu.WORK_SUM);
//				menu.setName(setString(R.string.parser_cache_06));
//				mainMenuDB.insertMenu(menu);
//			}
//
//		}
		
	}
	public void parserHY(JSONObject jsonObject) throws JSONException{
		if (jsonObject.has("isRoom")) {

			int isHY = jsonObject.getInt("isRoom");
			if(isHY == 0){
				mainMenuDB.removeMenuByType(Menu.HUI_YI);
			}else if(isHY ==1){
				mainMenuDB.removeMenuByType(Menu.HUI_YI);
				Menu menu = new Menu();
				menu.setType(Menu.HUI_YI);
				menu.setName("会议管理");
				mainMenuDB.insertMenu(menu);
			}

		}

	}
	
	public void paraerMailList(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "mailList")) {
			JSONArray array = jsonObject.getJSONArray("mailList");
//			List<AdressBookUser> userList = new ArrayList<AdressBookUser>();
			AdressBookUserDB db = new AdressBookUserDB(context);
			db.deleteAll();
			if (array!=null && array.length()>0) {
				DatabaseHelper.getInstance(context).beginTransaction();
				for (int i = 0; i < array.length(); i++) {
					AdressBookUser user = new AdressBookUser();
					JSONObject obj = array.getJSONObject(i);
					if (PublicUtils.isValid(obj, "uId")) {
						int value = obj.getInt("uId");
						user.setuId(value);
						if(6123582==value){
							user.setOlevel(1);
						}else
						if(6123570==value){
							user.setOlevel(2);
						}else
						if(6123526==value){
							user.setOlevel(3);
						}else
						if(6123470==value){
							user.setOlevel(4);
						}else
						if(6123474==value){
							user.setOlevel(5);
						}else
						if(6123645==value){
							user.setOlevel(6);
						}else
						if(6123487==value){
							user.setOlevel(7);
						}else
						if(6123968==value){
							user.setOlevel(8);
						}else
						if(6123531==value){
							user.setOlevel(9);
						}else
						if(6123871==value){
							user.setOlevel(10);
						}else
						if(6123698==value){
							user.setOlevel(11);
						}else
						if(6123663==value){
							user.setOlevel(12);
						}else
						if(6123851==value){
							user.setOlevel(13);
						}else
						if(6123693==value){
							user.setOlevel(14);
						}else
						if(6123514==value){
							user.setOlevel(15);
						}else
						if(6123566==value){
							user.setOlevel(16);
						}
					}
					
					if (PublicUtils.isValid(obj, "rId")) {
						int value = obj.getInt("rId");
						user.setrId(value);
					}
					
					if (PublicUtils.isValid(obj, "un")) {
						String value = obj.getString("un");
						user.setUn(value);


					}
					
					if (PublicUtils.isValid(obj, "pn")) {
						String value = obj.getString("pn");
						user.setPn(value);
					}
					
					if (PublicUtils.isValid(obj, "rn")) {
						String value = obj.getString("rn");
						user.setRn(value);
					}
					
					if (PublicUtils.isValid(obj, "rl")) {
						int value = obj.getInt("rl");
						user.setRl(value);
					}
					
					if (PublicUtils.isValid(obj, "on")) {
						String value = obj.getString("on");
						user.setOn(value);
					}
					
					if (PublicUtils.isValid(obj, "oc")) {
						String value = obj.getString("oc");
						user.setOc(value);
					}
					
					if (PublicUtils.isValid(obj, "ol")) {
						int value = obj.getInt("ol");
						user.setOl(value);
					}
					
					if (PublicUtils.isValid(obj, "oId")) {
						int value = obj.getInt("oId");
						user.setoId(value);
					}
					if(PublicUtils.isValid(obj,"op")){
						String values = obj.getString("op");
						user.setOp(values);
					}
					if(PublicUtils.isValid(obj,"photo")){
						String values = obj.getString("photo");
						user.setPhoto(values);
					}
					if(PublicUtils.isValid(obj,"mailAddr")){
						String values = obj.getString("mailAddr");
						user.setMailAddr(values);
					}
//					userList.add(user);
					db.insert(user);
				}
				DatabaseHelper.getInstance(context).endTransaction();

//				if (!userList.isEmpty()) {
//					DatabaseHelper.getInstance(context).beginTransaction();
//					for (int i = 0; i < userList.size(); i++) {
//						AdressBookUser user = userList.get(i);
//						db.insert(user);
//					}
//					DatabaseHelper.getInstance(context).endTransaction();
//				}
			}
		}
	}
	
	
	/**
	 * 解析企业微信配置项
	 * @param jsonObject
	 * @throws JSONException
	 */
	public void webConfig(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "weChat_conf")) {
			JSONObject confObj= jsonObject.getJSONObject("weChat_conf");
			if (PublicUtils.isValid(confObj, "isPhone")) {
				String value = confObj.getString("isPhone");
				SharedPrefrencesForWechatUtil.getInstance(context).setIsPhone(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setIsPhone("0");
			}
			
			if (PublicUtils.isValid(confObj, "isNotice")) {
				String value = confObj.getString("isNotice");
				SharedPrefrencesForWechatUtil.getInstance(context).setisNotice(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setisNotice("1");
			}
			
			if (PublicUtils.isValid(confObj, "isGroup")) {
				String value = confObj.getString("isGroup");
				SharedPrefrencesForWechatUtil.getInstance(context).setIsGroup(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setIsGroup("1");
			}
			
			if (PublicUtils.isValid(confObj, "isTopic")) {
				String value = confObj.getString("isTopic");
				SharedPrefrencesForWechatUtil.getInstance(context).setIsTopic(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setIsTopic("1");
			}
			
			if (PublicUtils.isValid(confObj, "isPrivate")) {
				String value = confObj.getString("isPrivate");
				SharedPrefrencesForWechatUtil.getInstance(context).setIsPrivate(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setIsPrivate("1");
			}
			
			if (PublicUtils.isValid(confObj, "groupAuth")) {
				String value = confObj.getString("groupAuth");
				SharedPrefrencesForWechatUtil.getInstance(context).setGroupAuth(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setGroupAuth("1");
			}
			
			if (PublicUtils.isValid(confObj, "noticeRange")) {
				String value = confObj.getString("noticeRange");
				SharedPrefrencesForWechatUtil.getInstance(context).setNoticeRange(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setNoticeRange("1");
			}
			
			if (PublicUtils.isValid(confObj, "topicRange")) {
				String value = confObj.getString("topicRange");
				SharedPrefrencesForWechatUtil.getInstance(context).setTopicRange(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setTopicRange("1");
			}
			
			if (PublicUtils.isValid(confObj, "privateRange")) {
				String value = confObj.getString("privateRange");
				SharedPrefrencesForWechatUtil.getInstance(context).setPrivateRange(value);
			}else{
				SharedPrefrencesForWechatUtil.getInstance(context).setPrivateRange("1");
			}
			parserGroupTag(jsonObject);
			parserGroups(jsonObject);
			parserReplies(jsonObject);
			parserTopic(jsonObject);
			parserAudit(jsonObject);
			parserNotice(jsonObject);
		}
	}
	
	/**
	 * 解析回帖
	 * @param jsonObject
	 * @throws JSONException 
	 */
	private void parserReplies(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "replies")) {
			JSONArray array = jsonObject.getJSONArray("replies");
			List<Reply> replyList = new ArrayList<Reply>();

			if (array!=null && array.length()>0) {
				
				new ReplyDB(context).deleteAll();
				new CommentDB(context).deleteAll();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					if (PublicUtils.isValid(obj, "pathLevel")) {
						int level = obj.getInt("pathLevel");
						if (level == 2) {//回帖
							Reply reply = new Reply();
							reply.setIsRead(1);
							if (PublicUtils.isValid(obj, "repliesId")) {
								int value = obj.getInt("repliesId");
								reply.setReplyId(value);
							}
							if (PublicUtils.isValid(obj, "topicId")) {
								int value = obj.getInt("topicId");
								reply.setTopicId(value);
							}
							if (PublicUtils.isValid(obj, "pathCode")) {
								String value = obj.getString("pathCode");
								reply.setPathCode(value);
							}
							if (PublicUtils.isValid(obj, "msgKey")) {
								String value = obj.getString("msgKey");
								reply.setMsgKey(value);
							}
							
							if (PublicUtils.isValid(obj, "repliesUserId")) {
								int value = obj.getInt("repliesUserId");
								reply.setUserId(value);
							}
							if (PublicUtils.isValid(obj, "repliesUser")) {
								String value = obj.getString("repliesUser");
								reply.setReplyName(value);
							}
							if (PublicUtils.isValid(obj, "repliesContent")) {
								String value = obj.getString("repliesContent");
								reply.setContent(value);
							}else{
								reply.setContent("");
							}
							
							
							if (PublicUtils.isValid(obj, "repliesTime")) {
								String value = obj.getString("repliesTime");
								reply.setDate(value);
							}
							if (PublicUtils.isValid(obj, "headImg")) {
								String value = obj.getString("headImg");
								reply.setUrl(value);
							}
							if (PublicUtils.isValid(obj, "type")) {
								String value = obj.getString("type");
								reply.setTopicType(value);
							}
							if(PublicUtils.isValid(obj, "isPublic")){
								String isPublic = obj.getString("isPublic");
								reply.setIsPublic(isPublic);
							}
							
							StringBuffer sb = new StringBuffer();
							if (PublicUtils.isValid(obj, "photo1")) {
								String p = obj.getString("photo1");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo2")) {
								String p = obj.getString("photo2");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo3")) {
								String p = obj.getString("photo3");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo4")) {
								String p = obj.getString("photo4");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo5")) {
								String p = obj.getString("photo5");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo6")) {
								String p = obj.getString("photo6");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo7")) {
								String p = obj.getString("photo7");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo8")) {
								String p = obj.getString("photo8");
								sb.append(",").append(p);
							}
							if (PublicUtils.isValid(obj, "photo9")) {
								String p = obj.getString("photo9");
								sb.append(",").append(p);
							}

							if (sb.length()>0) {
								String photo = sb.substring(1);
								reply.setPhoto(photo);
							}
							
							
							if (PublicUtils.isValid(obj, "attachment")) {
								String attachment = obj.getString("attachment");
								reply.setAttachment(attachment);
							}
							
							if (PublicUtils.isValid(obj, "review")) {
								JSONArray cArray = obj.getJSONArray("review");
								if (cArray!=null) {
									List<Comment> commentList = new ArrayList<Comment>();
									for (int j = 0; j < cArray.length(); j++) {
										JSONObject cObj =  cArray.getJSONObject(j);
										Comment comment = new Comment();
										comment.setReplyId(reply.getReplyId());
										if (PublicUtils.isValid(cObj, "repliesId")) {
											int value = cObj.getInt("repliesId");
											comment.setCommentId(value);
										}
										if (PublicUtils.isValid(cObj, "pathCode")) {
											String value = cObj.getString("pathCode");
											comment.setPathCode(value);
										}
										if (PublicUtils.isValid(cObj, "msgKey")) {
											String value = cObj.getString("msgKey");
											comment.setMsgKey(value);
										}
										if (PublicUtils.isValid(cObj, "toUserId")) {
											int value = cObj.getInt("toUserId");
											comment.setdUserId(value);
										}
										if (PublicUtils.isValid(cObj, "toUser")) {
											String value = cObj.getString("toUser");
											comment.setdUserName(value);
										}
										if (PublicUtils.isValid(cObj, "repliesContent")) {
											String value = cObj.getString("repliesContent");
											comment.setComment(value);
										}
										if (PublicUtils.isValid(cObj, "repliesTime")) {
											String value = cObj.getString("repliesTime");
											comment.setDate(value);
										}
										
										if (PublicUtils.isValid(cObj, "repliesUserId")) {
											int value = cObj.getInt("repliesUserId");
											comment.setcUserId(value);
										}
										
										if (PublicUtils.isValid(cObj, "repliesUser")) {
											String value = cObj.getString("repliesUser");
											comment.setcUserName(value);
										}
										commentList.add(comment);
									}
									if (!commentList.isEmpty()) {
										DatabaseHelper.getInstance(context).beginTransaction();
										CommentDB cDB = new CommentDB(context);
										for (int k = 0; k < commentList.size(); k++) {
											Comment c = commentList.get(k);
											cDB.insert(c);
										}
										DatabaseHelper.getInstance(context).endTransaction();
									}
								}
							}
							replyList.add(reply);
						}
					}
				}
			}
			
			if (!replyList.isEmpty()) {
				DatabaseHelper.getInstance(context).beginTransaction();
				ReplyDB rDB = new ReplyDB(context);
				for (int i = 0; i < replyList.size(); i++) {
					Reply r  = replyList.get(i);
					rDB.insert(r);
				}
				DatabaseHelper.getInstance(context).endTransaction();
			}
		}
		
	}
	
	/**
	 * 解析审批和调查
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void parserAudit(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "audit")) {
			JSONArray array = jsonObject.getJSONArray("audit");
			List<Survey> surveyList = new ArrayList<Survey>();
			if (array!=null && array.length()>0) {
				
				new SurveyDB(context).deleteAll();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Survey survey = new Survey();
//					if (!TextUtils.isEmpty(surveyType)) {
//						survey.setSurveyType(Integer.parseInt(surveyType));
//					}
					
					if (PublicUtils.isValid(obj, "topicId")) {
						survey.setTopicId(Integer.parseInt(obj.getString("topicId")));
					}
					
					if (PublicUtils.isValid(obj, "optionOrder")) {
						survey.setOptionOrder(obj.getInt("optionOrder"));
					}
					
					if (PublicUtils.isValid(obj, "optionLabel")) {
						survey.setOptions(obj.getString("optionLabel"));
					}
					
					if (PublicUtils.isValid(obj, "isSelect")) {
						survey.setType(Integer.parseInt(obj.getString("isSelect")));
					}
					surveyList.add(survey);
				}
			}
			if (!surveyList.isEmpty()) {
				DatabaseHelper.getInstance(context).beginTransaction();
				SurveyDB db = new SurveyDB(context);
				for (int i = 0; i <surveyList.size(); i++) {
					Survey s = surveyList.get(i);
					db.insert(s);
				}
				DatabaseHelper.getInstance(context).endTransaction();
			}
		}
	}
	
	
	/**
	 * 解析群标签
	 * @param jsonObject
	 * @throws JSONException 
	 */
	private void parserGroupTag(JSONObject jsonObject) throws JSONException{
		
		if (PublicUtils.isValid(jsonObject, "groupTag")) {
			SharedPreferencesUtil.getInstance(context).setWetChatTags("");
			JSONArray array = jsonObject.getJSONArray("groupTag");
			String str = array.toString().replace("[", "");
			str = str.replace("]", "");
			if (!TextUtils.isEmpty(str)) {
				new WechatUtil(context).wetCharTags(true, str.trim());
			}
		}
	}
	
	/**
	 * 解析群
	 * @param jsonObject
	 * @throws JSONException
	 */
	private void parserGroups(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "groups")) {
			JSONArray array = jsonObject.getJSONArray("groups");
			List<Group> list = new ArrayList<Group>();
			if (array!=null && array.length()>0) {
				GroupUserDB dbUser = new GroupUserDB(context);
				dbUser.deleteAll();
				new GroupDB(context).deleteAll();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Group group = new Group();
					if (PublicUtils.isValid(obj, "groupTag")) {
						String groupTag = obj.getString("groupTag");
					}
					if (PublicUtils.isValid(obj, "id")) {
						int value = obj.getInt("id");
						group.setGroupId(value);
					}
					
					if (PublicUtils.isValid(obj, "logo")) {
						String value = obj.getString("logo");
						group.setLogo(value);
					}
					
					if (PublicUtils.isValid(obj, "userId")) {
						String userId = obj.getString("userId");
						group.setCreateUserId(Integer.parseInt(userId));
					}
					
					if (PublicUtils.isValid(obj, "name")) {
						String value = obj.getString("name");
						group.setGroupName(value);
					}
					
					if (PublicUtils.isValid(obj, "orgCode")) {
						String value = obj.getString("orgCode");
						group.setOrgCode(value);
					}
					if (PublicUtils.isValid(obj, "subDesc")) {
						String value = obj.getString("subDesc");
						group.setExplain(value);
					}
					if (PublicUtils.isValid(obj, "type")) {
						int value = obj.getInt("type");
						group.setType(value);
					}
					if (PublicUtils.isValid(obj, "createTime")) {
						String value = obj.getString("createTime");
					}
					
					if (PublicUtils.isValid(obj, "createUser")) {
						String value = obj.getString("createUser");
					}
					
					if (PublicUtils.isValid(obj, "updateTime")) {
						String value = obj.getString("updateTime");
					}
					
					if (PublicUtils.isValid(obj, "updateUser")) {
						String value = obj.getString("updateUser");
					}
					if (PublicUtils.isValid(obj, "user")) {
						JSONArray userArry = obj.getJSONArray("user");
						List<GroupUser> userList = new ArrayList<GroupUser>();
						if (userArry!=null && userArry.length()>0) {
							for (int j = 0; j < userArry.length(); j++) {
								JSONObject uObj = userArry.getJSONObject(j);
								GroupUser user = new GroupUser();
								user.setGroupId(group.getGroupId());
								if (PublicUtils.isValid(uObj, "userId")) {
									int value = uObj.getInt("userId");
									user.setUserId(value);
								}
								if (PublicUtils.isValid(uObj, "headImg")) {
									String value = uObj.getString("headImg");
									user.setPhoto(value);
									SharedPrefrencesForWechatUtil.getInstance(context).setUserHedaImg(String.valueOf(user.getUserId()), user.getPhoto());
								}
								if (PublicUtils.isValid(uObj, "nickName")) {
									String value = uObj.getString("nickName");
								}
								
								if (PublicUtils.isValid(uObj, "userName")) {
									String value = uObj.getString("userName");
									user.setUserName(value);
								}
								
								userList.add(user);
							}
						}
						
						if (!userList.isEmpty()) {
							DatabaseHelper.getInstance(context).beginTransaction();
							for (int k = 0; k < userList.size(); k++) {
								GroupUser gUser = userList.get(k);
								dbUser.insert(gUser);
							}
							DatabaseHelper.getInstance(context).endTransaction();
						}
						
					}
					list.add(group);
				}
				
				if (!list.isEmpty()) {
					GroupDB db  = new GroupDB(context);
					db.deleteAll();
					DatabaseHelper.getInstance(context).beginTransaction();
					for (int i = 0; i < list.size(); i++) {
						Group g = list.get(i);
						db.insert(g);
					}
					DatabaseHelper.getInstance(context).endTransaction();

				}
			}
		}
	}
	
	/**
	 * 解析企业微信通知
	 * @param jsonObject
	 * @throws JSONException 
	 */
	private void parserNotice(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "notice")) {
			JSONArray array = jsonObject.getJSONArray("notice");
			if (array!=null && array.length()>0) {
				new NotificationDB(context).deleteAll();
				List<Notification> list = new ArrayList<Notification>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Notification no = new Notification();
					if (PublicUtils.isValid(obj, "issuerName")) {
						String value = obj.getString("issuerName");
						no.setCreateDate(value);
					}
					if (PublicUtils.isValid(obj, "issuerTime")) {
						String value = obj.getString("issuerTime");
						no.setCreateDate(value);
					}
					if (PublicUtils.isValid(obj, "title")) {
						String value = obj.getString("title");
						no.setTitle(value);
					}
					if (PublicUtils.isValid(obj, "content")) {
						String value = obj.getString("content");
						no.setContent(value);
					}
					if (PublicUtils.isValid(obj, "noticeId")) {
						int value = obj.getInt("noticeId");
						no.setNoticeId(value);
					}
					if(PublicUtils.isValid(obj, "orgName")){
						String orgName = obj.getString("orgName");
						no.setCreateOrg(orgName);
					}
					if (PublicUtils.isValid(obj, "isFollow")) {
						String value = obj.getString("isFollow");
						no.setIsNoticed(value);
					}
					if (PublicUtils.isValid(obj, "attachment")) {
						String attachment = obj.getString("attachment");
						if(!TextUtils.isEmpty(attachment)){
							no.setIsAttach("1");
						}else{
							no.setIsAttach("0");
						}
					}
					list.add(no);
				}
				if (!list.isEmpty()) {
					DatabaseHelper.getInstance(context).beginTransaction();
					NotificationDB db = new NotificationDB(context);
					for (int i = 0; i < list.size(); i++) {
						Notification n = list.get(i);
						db.insert(n);
					}
					DatabaseHelper.getInstance(context).endTransaction();
				}
			}
		}
	}
	
	/**
	 * 解析话题
	 * @param jsonObject
	 * @throws JSONException 
	 */
	private void parserTopic(JSONObject jsonObject) throws JSONException{
		if (PublicUtils.isValid(jsonObject, "topic")) {
			JSONArray array = jsonObject.getJSONArray("topic");
			List<Topic> list = new ArrayList<Topic>();
			ReplyDB replyDB = new ReplyDB(context);
			if (array!=null && array.length()>0) {
				new TopicDB(context).deleteAll();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Topic topic = new Topic();
					if (PublicUtils.isValid(obj, "topicId")) {
						int value = obj.getInt("topicId");
						topic.setTopicId(value);
					}
					if (PublicUtils.isValid(obj, "groupId")) {
						int value = obj.getInt("groupId");
						topic.setGroupId(value);
					}
					
					if (PublicUtils.isValid(obj, "title")) {
						String value = obj.getString("title");
						topic.setTitle(value);
					}
					if (PublicUtils.isValid(obj, "msgKey")) {
						String value = obj.getString("msgKey");
						topic.setMsgKey(value);
					}
					if (PublicUtils.isValid(obj, "explain")) {
						String value = obj.getString("explain");
						topic.setExplain(value);
					}
					
					if (PublicUtils.isValid(obj, "type")) {
						String value = obj.getString("type");
						topic.setType(value);
					}
					
					if (PublicUtils.isValid(obj, "startDate")) {
						String value = obj.getString("startDate");
						topic.setFrom(value);
					}
					
					if (PublicUtils.isValid(obj, "endDate")) {
						String value = obj.getString("endDate");
						topic.setTo(value);
					}
					
					if (PublicUtils.isValid(obj, "speakNum")) {
						int value = obj.getInt("speakNum");
						topic.setSpeakNum(value);
					}
					
					if (PublicUtils.isValid(obj, "isReplies")) {
						int value = obj.getInt("isReplies");
						topic.setIsReply(value);
					}
					
					if (PublicUtils.isValid(obj, "reviewAuth")) {
						String value = obj.getString("reviewAuth");
						topic.setReplyReview(Integer.parseInt(value));
					}
					if (PublicUtils.isValid(obj, "repliesAuth")) {
						String value = obj.getString("repliesAuth");
						topic.setComment(Integer.parseInt(value));
					}
					
					if (PublicUtils.isValid(obj, "topicUserId")) {
						int value = obj.getInt("topicUserId");
						topic.setCreateUserId(value);
					}
					
					if (PublicUtils.isValid(obj, "topicUserName")) {
						String value = obj.getString("topicUserName");
						topic.setCreateUserName(value);
					}
					
					
					if (PublicUtils.isValid(obj, "topicOrgName")) {
						String value = obj.getString("topicOrgName");
						topic.setCreateOrgName(value);
					}
					
					
					if (PublicUtils.isValid(obj, "createTime")) {
						String value = obj.getString("createTime");
						topic.setCreateTime(value);
					}
					
					if (PublicUtils.isValid(obj, "isOver")) {
						int value = obj.getInt("isOver");
						if(value == 0){
							topic.setIsClose(1);
						}else{
							topic.setIsClose(0);
						}
					}
					
					if (PublicUtils.isValid(obj, "isFollow")) {
						String value = obj.getString("isFollow");
						topic.setIsAttention(Integer.parseInt(value));
					}
					if(PublicUtils.isValid(obj, "isPublic")){
						String isPublic = obj.getString("isPublic");
						if(!TextUtils.isEmpty(isPublic)){
							topic.setClassify(Integer.parseInt(isPublic));
						}
						
					}
					Reply reply = replyDB.findNewReply(topic.getTopicId());
					if (reply!=null) {
						topic.setRecentTime(reply.getDate());
						if(TextUtils.isEmpty(reply.getContent())){
	 						if(!TextUtils.isEmpty(reply.getPhoto())){
	 							topic.setRecentContent(reply.getReplyName() + ":" + setString(R.string.parser_cache_03));
	 						}else if(!TextUtils.isEmpty(reply.getAttachment())){
	 							topic.setRecentContent(reply.getReplyName() + ":" + setString(R.string.parser_cache_04));
	 						}
	 					}else {
	 						topic.setRecentContent(reply.getReplyName() + ":" + reply.getContent());
	 					}
					}
					list.add(topic);
					
				}
			}
			if (!list.isEmpty()) {
				TopicDB db = new TopicDB(context);
				db.deleteAll();
				DatabaseHelper.getInstance(context).beginTransaction();
				for (int i = 0; i <list.size(); i++) {
					Topic t = list.get(i);
					db.insert(t);
				}
				DatabaseHelper.getInstance(context).endTransaction();
			}
		}
	}
	
	/**
	 * 是否要显示公告
	 */
	public void parseISNotify(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_NOTIFY)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_NOTICE);
			String visit = jsonObject.getString(IS_NOTIFY);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_NOTICE);
				menu.setName(context.getResources().getString(
						R.string.notify_list_head_ch));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				mainMenuDB.insertMenu(menu);
			}
			// SharedPreferencesUtil.getInstance(context).setIsNotify(jsonObject.getInt("isNotify")==1?true:false);
		}
	}

	/**
	 * 是否要显示考勤
	 * 
	 * @throws JSONException
	 */
	public void parseIsAttendance(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_ATTENDANCE)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_ATTENDANCE);
			String visit = jsonObject.getString(IS_ATTENDANCE);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_ATTENDANCE);
				menu.setName(context.getResources().getString(
						R.string.attendance));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				if (jsonObject.has(PHONE_USABLE_TIME_OLD)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME_OLD);
					menu.setPhoneUsableTime(time);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}

	/**
	 * 是否要显示新考勤
	 * 
	 * @throws JSONException
	 */
	public void parseIsNewAttendance(JSONObject jsonObject)
			throws JSONException {
		if (jsonObject.has(NEW_IS_ATTENDANCE)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_NEW_ATTENDANCE);
			
			String visit = jsonObject.getString(NEW_IS_ATTENDANCE);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_NEW_ATTENDANCE);
				menu.setName(setString(R.string.parser_cache_attendance));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}

				if (jsonObject.has(PHONE_USABLE_TIME_NEW)) {
					String time = jsonObject.getString(PHONE_USABLE_TIME_NEW);
					menu.setPhoneUsableTime(time);
				}
				SharedPreferencesUtil2.getInstance(context).savePhoneReport(true);
				mainMenuDB.insertMenu(menu);
			}
		}
	}

	/**
	 * 是否要显示帮助
	 * 
	 * @throws JSONException
	 */
	public void parseIsHelp(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_HELP)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_HELP);
			String visit = jsonObject.getString(IS_HELP);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_HELP);
				menu.setName(context.getResources().getString(R.string.HELP));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}
	/**
	 * 是否要显示代办事项
	 * 
	 * @throws JSONException
	 */
	public void parseIsToDo(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_TODO)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_TODO);
			String todo = jsonObject.getString(IS_TODO);
			String[] str = todo.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_TODO);
				menu.setName(setString(R.string.parser_cache_01));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(100);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}
	
	/**
	 * 解析UI样式
	 * @param jsonObject
	 * @throws JSONException
	 */
	public void parserStyle(JSONObject jsonObject) throws JSONException{
		if (this.isValid(jsonObject, STYLE)) {
			StyleDB styleDB = new StyleDB(context);
			styleDB.deleteAllStyle();
			JSONArray array = jsonObject.getJSONArray(STYLE);
			if (array!=null && array.length()>0) {
				Style style = null;
				for (int i = 0; i < array.length(); i++) {
					style = new Style();
					JSONObject obj = array.getJSONObject(i);
					if (isValid(obj, STYLE_MODULE_ID)) {
						style.setModuleId(obj.getInt(STYLE_MODULE_ID));
					}
					if (isValid(obj, STYLE_STYLE_TYPE)) {
						style.setStyleType(obj.getInt(STYLE_STYLE_TYPE));
					}
					if (isValid(obj, STYLE_IMG_MD5)) {
						style.setImgMd5(obj.getString(STYLE_IMG_MD5));
					}
					if (style.getStyleType() == Style.STYLE_TYPE_BG) {
						if (isValid(obj, STYLE_BG_IMAGE)) {//如果是背景图片
							style.setImgUrl(obj.getString(STYLE_BG_IMAGE));
							try {
								style.setImgName(MD5Helper.EncoderByMd5(style.getImgUrl()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{
						if (isValid(obj, STYLE_IMG_URL)) {
							style.setImgUrl(obj.getString(STYLE_IMG_URL));
							try {
								style.setImgName(MD5Helper.EncoderByMd5(style.getImgUrl()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					if (isValid(obj, STYLE_BG_COLOR)) {
						style.setBgColor(obj.getString(STYLE_BG_COLOR));
					}
					if (isValid(obj, COMPANY_ID) && obj.getInt(COMPANY_ID) == 0) {
						style.setType(Style.TYPE_DEFAULT);
					}else{
						style.setType(Style.TYPE_CUSTOM);
					}
					
					styleDB.insertStyle(style);
				}
			}
		}
	}

	/**
	 * 是否要显示圈子
	 * 
	 * @throws JSONException
	 */
	public void parseISBBS(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has(IS_BBS)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_BBS);
			String visit = jsonObject.getString(IS_BBS);
			String[] str = visit.split("\\|");
			if (Integer.parseInt(str[0]) == 1 ? true : false) {
				Menu menu = new Menu();
				menu.setType(Menu.TYPE_BBS);
				menu.setName(context.getResources().getString(R.string.BBS));
				if (str.length == 2) {
					menu.setPhoneSerialNo(Integer.parseInt(str[1]));
				} else {
					menu.setPhoneSerialNo(0);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}

	/**
	 * 解析Home页数据
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseMenu(String json) throws JSONException {
		JSONArray menuArr = new JSONArray(json);
		int len = menuArr.length();
		JSONObject menuObject = null;
		Menu menu = null;
		for (int i = 0; i < len; i++) {
			menuObject = menuArr.getJSONObject(i);
			menu = putMenu(menuObject);
			mainMenuDB.insertMenu(menu);
		}
	}


	/**
	 * 解析Home页石化数据
	 *
	 * @param json
	 * @throws JSONException
	 */
	public void parseShihuaMenu(String json) throws JSONException {
		if(!TextUtils.isEmpty(json)){
			JSONArray menuArr = new JSONArray(json);
			int len = menuArr.length();
			JSONObject menuObject = null;
			for (int i = 0; i < len; i++) {
				menuObject = menuArr.getJSONObject(i);
				ContentValues cv = new ContentValues();
				cv.put("folderName", menuObject.getString("folderName"));
				cv.put("icon", menuObject.getString("icon"));
				cv.put("list", menuObject.getString("list"));
				menuShiHuaDB.insertMenu(cv);
			}
		}

	}

	/**
	 * 去掉menu中重复的menu  暂时这样 待修改
	 *
	 */
	public void removeMenu(){
		try{
			List<Menu> menuList=mainMenuDB.findAllMenuList();
			List<ShiHuaMenu> smenuList=menuShiHuaDB.findAllMenuList();
			if(smenuList!=null&&smenuList.size()>0){
				for(int j=0;j<smenuList.size();j++){
					if(smenuList.get(j).getMenuList()!=null){
						for(int k=0;k<smenuList.get(j).getMenuList().size();k++){
							if(smenuList.get(j).getMenuList().get(k).getMenuIdList()!=null){
								for(int l=0;l<smenuList.get(j).getMenuList().get(k).getMenuIdList().size();l++){
									for (int i = 0; i < menuList.size(); i++) {
										if((menuList.get(i).getMenuId()+"").equals(smenuList.get(j).getMenuList().get(k).getMenuIdList().get(l))){
											mainMenuDB.removeMenuByMenuId(menuList.get(i).getMenuId());
										}
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e){


		}

	}

	/**
	 * 解析menu
	 * 
	 * @param jsonObject
	 *            要解析的menu json对象
	 * @return
	 * @throws JSONException
	 */
	private Menu putMenu(JSONObject jsonObject) throws JSONException {
		Menu menu = new Menu();
		menu.setMenuId(jsonObject.getInt(ID));
		int type = jsonObject.getInt(TYPE);
		switch (type) {
		case 1:
			menu.setType(Menu.TYPE_MODULE);
			break;
		case 2:
			menu.setType(Menu.TYPE_TARGET);
			break;
		case 5:
			menu.setType(Menu.TYPE_NEW_TARGET);
			break;
		case 6:
			menu.setType(Menu.TYPE_NEARBY);
			break;
		default:
			menu.setType(Menu.TYPE_MODULE);
		}
		mainMenuDB.removeMenuByMenuIdAndType(menu.getMenuId(), menu.getType());
		if (isValid(jsonObject, NAME)) {
			menu.setName(jsonObject.getString(NAME));
		}
		if (isValid(jsonObject, MODULE_TYPE)) {
			menu.setModuleType(Integer.valueOf(jsonObject
					.getString(MODULE_TYPE)));
		}
		if (isValid(jsonObject, BASE_TIME)) {
			menu.setBaseTime(jsonObject.getString(BASE_TIME));
		}
		if (isValid(jsonObject, PHONE_SERIAL_NO)) {
			menu.setPhoneSerialNo(jsonObject.getInt(PHONE_SERIAL_NO));
		}
		if (isValid(jsonObject, IS_NO_WAIT)) {
			menu.setIsNoWait(jsonObject.getInt(IS_NO_WAIT));
		}else{
			menu.setIsNoWait(1);
		}
		if (isValid(jsonObject, PHONE_USABLE_TIME)) {
			menu.setPhoneUsableTime(jsonObject.getString(PHONE_USABLE_TIME));
		}
		if (type == 5) {//新双向模块
			putNewDoubleMod(jsonObject, menu);			
		}else{//自定义模块
			putMod(jsonObject, menu.getMenuId());
		}
		return menu;
	}
	
	private void putNewDoubleMod(JSONObject jsonObject,Menu menu) throws JSONException{
		Module mod = new Module();
		mod.setMenuId(menu.getMenuId());
		mod.setType(Constants.MODULE_TYPE_EXECUTE_NEW);
		if (isValid(jsonObject, MOD_AUTH_EXECUTE) && jsonObject.getInt(MOD_AUTH_EXECUTE) == 1) {
			if (isValid(jsonObject, MOD_NAME_EXECUTE)) {
				mod.setName(jsonObject.getString(MOD_NAME_EXECUTE));
			}
			if (isValid(jsonObject, PHONE_TASK_FUNS)) {
				mod.setPhoneTaskFuns(jsonObject.getString(PHONE_TASK_FUNS));
			}
			if (isValid(jsonObject, DYNAMIC_STATUS)) {
				mod.setDynamicStatus(jsonObject.getString(DYNAMIC_STATUS));
			}
			if (isValid(jsonObject, IS_REPORT_TASK)) {
				mod.setIsReportTask(jsonObject.getString(IS_REPORT_TASK));
			}
		}
		moduleDB.insertModule(mod);
	}

	/**
	 * 解析自定义模块
	 * 
	 * @param json
	 *            要解析的数据
	 * @throws JSONException
	 */
	public void parseMod(String json) throws JSONException {
		JSONArray modArr = new JSONArray(json);
		int len = modArr.length();
		JSONObject modObject = null;
		for (int i = 0; i < len; i++) {
			modObject = modArr.getJSONObject(i);
			int id = modObject.getInt(ID);
			moduleDB.removeModule(id);
			putMod(modObject, id);
		}
	}

	/**
	 * 解析自定义模块
	 * 
	 * @param jsonObject
	 *            要解析的json对象
	 * @param menuId
	 *            菜单ID
	 * @throws JSONException
	 */
	private void putMod(JSONObject jsonObject, Integer menuId)
			throws JSONException {
		Module mod = null;
		if (isValid(jsonObject, MOD_AUTH_ISSUER)
				&& jsonObject.getInt(MOD_AUTH_ISSUER) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_ISSUED);
			if (isValid(jsonObject, MOD_NAME_ISSUER)) {
				mod.setName(jsonObject.getString(MOD_NAME_ISSUER));
			}
			if (isValid(jsonObject, AUTH_ISSUER)) {
				mod.setAuth(jsonObject.getInt(AUTH_ISSUER));
			}
			if (isValid(jsonObject, ORG_ID_ISSUER)) {
				mod.setAuthOrgId(jsonObject.getString(ORG_ID_ISSUER));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_QUERY)
				&& jsonObject.getInt(MOD_AUTH_QUERY) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_QUERY);
			if (isValid(jsonObject, MOD_NAME_QUERY)) {
				mod.setName(jsonObject.getString(MOD_NAME_QUERY));
			}
			if (isValid(jsonObject, AUTH_QUERY)) {
				mod.setAuth(jsonObject.getInt(AUTH_QUERY));
			}
			if (isValid(jsonObject, ORG_ID_QUERY)) {
				mod.setAuthOrgId(jsonObject.getString(ORG_ID_QUERY));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_UPDATE)
				&& jsonObject.getInt(MOD_AUTH_UPDATE) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_UPDATE);
			if (isValid(jsonObject, MOD_NAME_UPDATE)) {
				mod.setName(jsonObject.getString(MOD_NAME_UPDATE));
			}
			if (isValid(jsonObject, AUTH_UPDATE)) {
				mod.setAuth(jsonObject.getInt(AUTH_UPDATE));
			}
			if (isValid(jsonObject, ORG_ID_UPDATE)) {
				mod.setAuthOrgId(jsonObject.getString(ORG_ID_UPDATE));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_AUDIT)
				&& jsonObject.getInt(MOD_AUTH_AUDIT) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_VERIFY);
			if (isValid(jsonObject, MOD_NAME_AUDIT)) {
				mod.setName(jsonObject.getString(MOD_NAME_AUDIT));
			}
			if (isValid(jsonObject, AUTH_AUDIT)) {
				mod.setAuth(jsonObject.getInt(AUTH_AUDIT));
			}
			if (isValid(jsonObject, ORG_ID_AUDIT)) {
				mod.setAuthOrgId(jsonObject.getString(ORG_ID_AUDIT));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_REPORT)
				&& jsonObject.getInt(MOD_AUTH_REPORT) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_REPORT);
			if (isValid(jsonObject, MOD_NAME_REPORT)) {
				mod.setName(jsonObject.getString(MOD_NAME_REPORT));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_REDESIGN)
				&& jsonObject.getInt(MOD_AUTH_REDESIGN) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_REASSIGN);
			if (isValid(jsonObject, MOD_NAME_REDESIGN)) {
				mod.setName(jsonObject.getString(MOD_NAME_REDESIGN));
			}
			if (isValid(jsonObject, AUTH_ISSUER)) {
				mod.setAuth(jsonObject.getInt(AUTH_ISSUER));
			}
			if (isValid(jsonObject, ORG_ID_ISSUER)) {
				mod.setAuthOrgId(jsonObject.getString(ORG_ID_ISSUER));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_EXECUTE)
				&& jsonObject.getInt(MOD_AUTH_EXECUTE) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			int type = jsonObject.getInt(TYPE);
			if (type == 5) {//新双向
				mod.setType(Constants.MODULE_TYPE_EXECUTE_NEW);
			}else{
				mod.setType(Constants.MODULE_TYPE_EXECUTE);
			}
			if (isValid(jsonObject, MOD_NAME_EXECUTE)) {
				mod.setName(jsonObject.getString(MOD_NAME_EXECUTE));
			}
			if (isValid(jsonObject, MOD_IS_CANCEL)) {
				mod.setIsCancel(jsonObject.getInt(MOD_IS_CANCEL));
			}
			if (isValid(jsonObject, PHONE_TASK_FUNS)) {
				mod.setPhoneTaskFuns(jsonObject.getString(PHONE_TASK_FUNS));
			}
			if (isValid(jsonObject, DYNAMIC_STATUS)) {
				mod.setDynamicStatus(jsonObject.getString(DYNAMIC_STATUS));
			}
			moduleDB.insertModule(mod);
		}
		if (isValid(jsonObject, MOD_AUTH_PAY)
				&& jsonObject.getInt(MOD_AUTH_PAY) == 1) {
			mod = new Module();
			mod.setMenuId(menuId);
			mod.setType(Constants.MODULE_TYPE_PAY);
			if (isValid(jsonObject, MOD_NAME_PAY)) {
				mod.setName(jsonObject.getString(MOD_NAME_PAY));
			}
			moduleDB.insertModule(mod);
		}
		
	}

	/**
	 * 解析访店模块
	 * 
	 * @param json
	 *            要解析的数据
	 * @throws JSONException
	 */
	public void parseVisit(String json) throws JSONException {
		JSONArray wayArr = new JSONArray(json);
		int len = wayArr.length();
		JSONObject wayObject = null;
		VisitWay visitWay = null;
		for (int i = 0; i < len; i++) { //一个计划下只能有一个线路,等于在迭代计划
			wayObject = wayArr.getJSONObject(i);
			visitWay = putVisitWay(wayObject);

			visitWayDB.removeVisitWayByPlanId(visitWay.getPlanId());

			visitWayDB.insertVisitWay(visitWay);
			// 解析Store
			JSONArray storeArr = wayObject.getJSONArray(STORE);
			int size = storeArr.length();
			JSONObject storeObject = null;
			for (int j = 0; j < size; j++) {
				if (!storeArr.isNull(j)) {
					storeObject = storeArr.getJSONObject(j);
					VisitStore vs = putVisitStore(storeObject);
					vs.setPlanId(visitWay.getPlanId());
					visitStoreDB.insertVisitStore(vs);
				}
			}

		}
	}

	@SuppressLint("SimpleDateFormat")
	public void parseNewAttendance(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		if (obj != null) {
			if (isValid(obj, NEW_ATTENDANCE_WORK_DAY)) {// 上班时间
				String str = obj.getString(NEW_ATTENDANCE_WORK_DAY);
				SharedPreferencesUtil.getInstance(context).setNewAttendanceWorkDay(str);
			}
			if (isValid(obj, NEW_ATTENDANCE_WORK_TIME)) {// 工作时间
				String str = obj.getString(NEW_ATTENDANCE_WORK_TIME);
				SharedPreferencesUtil.getInstance(context).setNewAttendanceWorkTime(str);
			}
			if (isValid(obj, NEW_ATTENDANCE_OVERTIME_NAME)) {// 加班名称
				String str = obj.getString(NEW_ATTENDANCE_OVERTIME_NAME);
				SharedPreferencesUtil.getInstance(context).setNewAttendanceOverName(str);
			}
			if (isValid(obj, NEW_ATTENDANCE_RESET_TYPE)) {
				String str = obj.getString(NEW_ATTENDANCE_RESET_TYPE);
				SharedPreferencesUtil.getInstance(context).setNewAttendanceResetType(str);
			}
			if (isValid(obj, NEW_ATTENDANCE_EXP_TIME)) { // 例外时间段（格式 12:00,1.0|14:00,0.5）
				String str = obj.getString(NEW_ATTENDANCE_EXP_TIME);
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationExpTime(str.trim());
			}else{
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationExpTime("");
			}
			if (isValid(obj, NEW_ATTENDANCE_INTERVAL_TIME)) { // 查岗间隔时间
				int interval = obj.getInt(NEW_ATTENDANCE_INTERVAL_TIME);
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationIntervalTime(interval);
			}else{
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationIntervalTime(0);
			}
			if (isValid(obj, ATTENDANCE_SERVICE_FLG)) {
				int serviceFlg = obj.getInt(ATTENDANCE_SERVICE_FLG);
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationServiceFlg(serviceFlg);
			}else{
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationServiceFlg(1);
			}
			if (isValid(obj, ATTENDANCE_WEEKS)) {
				String weeks = obj.getString(ATTENDANCE_WEEKS);
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationWeeks(weeks);
			}else{
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationWeeks(null);
			}
			if (isValid(obj, ATTENDANCE_ATTEND_TIME)) {
				String attendTime = obj.getString(ATTENDANCE_ATTEND_TIME);
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationAttendTime(attendTime);
			}else{
				SharedPrefsAttendanceUtil.getInstance(context).setAttendanceBackstageLocationAttendTime(null);
			}
			
			if (isValid(obj, MAX_TIME_ON)) {
				
				
				
				String attendTime = obj.getString(MAX_TIME_ON);//服务端获取的时间标记				
				String currentTimeStr = SharedPreferencesUtil.getInstance(context).getNewAttendTime();//本地生成存储的时间标记
				SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try {
					if(!attendTime.equals("")){
						if(currentTimeStr.equals("")||s.parse(attendTime).after(s.parse(currentTimeStr))){
							SharedPreferencesUtil.getInstance(context).setNewAttendTime(attendTime);
						}
					}
					
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
	
			}
			
			if (isValid(obj, MAX_TIME_OFF)) {
				String attendTime = obj.getString(MAX_TIME_OFF);
				SharedPreferencesUtil.getInstance(context).setNewAttendOverTime(attendTime);
			}
			
			
///////////////新增自定义名称以及报岗功能////////////////////Aug22
			
			if (isValid(obj, NEW_ATTENDANCE_GANG)) {//报岗时间段列表
				JSONArray gang = obj.getJSONArray(NEW_ATTENDANCE_GANG);
				SharedPreferencesUtil.getInstance(context).putInt(NEW_ATTENDANCE_GANG,gang.length());
			
				for(int i= 0;i<50;i++){
					SharedPreferencesUtil.getInstance(context).clear(NEW_ATTENDANCE_GANG+"startHM"+i);
					SharedPreferencesUtil.getInstance(context).clear(NEW_ATTENDANCE_GANG+"endHM"+i);
					SharedPreferencesUtil.getInstance(context).clear(NEW_ATTENDANCE_GANG+"name"+i);
					SharedPreferencesUtil.getInstance(context).clear(NEW_ATTENDANCE_GANG+"isFlag"+i);
				}
				for(int i=0;i<gang.length();i++){//clear
					
					SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANG+"startHM"+i,gang.getJSONObject(i).getString("startHM"));
					SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANG+"endHM"+i,gang.getJSONObject(i).getString("endHM"));
//					SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANG+"name"+i,gang.getJSONObject(i).getString("name"));
					SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANG+"isFlag"+i,gang.getJSONObject(i).getString("isFlag"));
					SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANG+"date"+i,"");
				}	
			}
//				else{
//				SharedPreferencesUtil.getInstance(context).clear(NEW_ATTENDANCE_GANG);
//			}
			
			if (isValid(obj, NEW_ATTENDANCE_OVERTIMENAME)) {//加班名
				String overtimeName = obj.getString(NEW_ATTENDANCE_OVERTIMENAME);
				SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_OVERTIMENAME,overtimeName);
			}
			if (isValid(obj, NEW_ATTENDANCE_GONAME)) {//上班名
				String goName = obj.getString(NEW_ATTENDANCE_GONAME);
				SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GONAME,goName);
			}
			if (isValid(obj, NEW_ATTENDANCE_FINISHNAME)) {//下班名
				String finishName = obj.getString(NEW_ATTENDANCE_FINISHNAME);
				SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_FINISHNAME,finishName);
			}
			if (isValid(obj, NEW_ATTENDANCE_GANGNAME)) {//报岗班名
				String gangName = obj.getString(NEW_ATTENDANCE_GANGNAME);
				SharedPreferencesUtil.getInstance(context).putString(NEW_ATTENDANCE_GANGNAME,gangName);
			}
			
		}
	}

	/**
	 * 解析线路
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private VisitWay putVisitWay(JSONObject jsonObject) throws JSONException {
		VisitWay visitWay = new VisitWay();
		if (isValid(jsonObject, ID)) {
			visitWay.setWayId(jsonObject.getInt(ID));
		}
		if (isValid(jsonObject, NAME)) {
			visitWay.setName(jsonObject.getString(NAME));
		}
		if (isValid(jsonObject, ORDER)) {
			visitWay.setIsOrder(jsonObject.getInt(ORDER));
		}
		if (isValid(jsonObject, PLAN_ID)) {
			visitWay.setPlanId(jsonObject.getInt(PLAN_ID));
		}
		// if(isValid(jsonObject,AWOKE_TYPE)){
		// visitWay.setAwokeType(jsonObject.getInt(AWOKE_TYPE));
		// }
		if (isValid(jsonObject, INTERVAL_TYPE)) {
			visitWay.setIntervalType(jsonObject.getInt(INTERVAL_TYPE));
		}
		if (isValid(jsonObject, WEEKLY)) {
			visitWay.setWeekly(jsonObject.getString(WEEKLY));
		}
		if (isValid(jsonObject, FROM)) {
			visitWay.setFromDate(jsonObject.getString(FROM));
		}
		if (isValid(jsonObject, TO)) {
			visitWay.setToDate(jsonObject.getString(TO));
		}
		if (isValid(jsonObject, START_DATE)) {
			visitWay.setStartdate(jsonObject.getString(START_DATE));
		}
		if (isValid(jsonObject, CYCLE_COUNT)) {
			visitWay.setCycleCount(jsonObject.getInt(CYCLE_COUNT));
		} else {// 默认周期数是1
			visitWay.setCycleCount(1);
		}
		if (isValid(jsonObject, VISIT_COUNT)) {
			visitWay.setVisitCount(jsonObject.getInt(VISIT_COUNT));
		} else {// 默认周期内提交的次数也是1
			visitWay.setVisitCount(1);
		}
		//如果有序线路，VISIT_COUNT如果大于1，则视为无序操作
		if(visitWay.getIsOrder() == VisitWay.ORDER_YES  && visitWay.getVisitCount() > 1){
			visitWay.setIsOrder(VisitWay.ORDER_NO);
		}
		return visitWay;
	}

	/**
	 * 解析店面
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private VisitStore putVisitStore(JSONObject jsonObject)
			throws JSONException {
		VisitStore visitStore = new VisitStore();
		if (isValid(jsonObject, ID)) {
			visitStore.setStoreId(jsonObject.getInt(ID));
		}
		if (jsonObject.has(WAY_ID)) {
			visitStore.setWayId(jsonObject.getInt(WAY_ID));
		}
		if (isValid(jsonObject, NAME)) {
			visitStore.setName(jsonObject.getString(NAME));
		}
		if (isValid(jsonObject, TARGET_ID)) {
			visitStore.setTargetid(jsonObject.getInt(TARGET_ID));
		}

		if (isValid(jsonObject, TIME)) {
			visitStore.setSubmitDate(jsonObject.getString(TIME));
		}

		if (isValid(jsonObject, IS_CHECKIN)) {
			visitStore.setIsCheckin(jsonObject.getInt(IS_CHECKIN));
		}
		if (isValid(jsonObject, IS_CHECKOUT)) {
			visitStore.setIsCheckout(jsonObject.getInt(IS_CHECKOUT));
		}

		if (isValid(jsonObject, STORE_LON)) {
			visitStore.setLon(jsonObject.getString(STORE_LON));
		}

		if (isValid(jsonObject, STORE_LAT)) {
			visitStore.setLat(jsonObject.getString(STORE_LAT));
		}

		if (isValid(jsonObject, IS_CHECK)) {
			visitStore.setIsCheck(jsonObject.getInt(IS_CHECK));
		}

		if (isValid(jsonObject, STROE_DISTANCE)) {
			visitStore.setStoreDistance(jsonObject.getInt(STROE_DISTANCE));
		}

		if (isValid(jsonObject, LOC_TYPE)) {
			visitStore.setLocType(jsonObject.getString(LOC_TYPE));
		}

		if (isValid(jsonObject, IS_ADDR)) {
			visitStore.setIsAddress(jsonObject.getString(IS_ADDR));
		}

		if (isValid(jsonObject, IS_ANEW_LOC)) {
			visitStore.setIsNewLoc(jsonObject.getString(IS_ANEW_LOC));
		}
		if (isValid(jsonObject, IS_NO_WAIT)) {
			visitStore.setIsNoWait(jsonObject.getInt(IS_NO_WAIT));
		}else{
			visitStore.setIsNoWait(1);
		}
		if (isValid(jsonObject, VISIT_COUNTS)) {
			visitStore.setSubmitNum(jsonObject.getInt(VISIT_COUNTS));
		}
		
		if (!TextUtils.isEmpty(visitStore.getSubmitDate()) && visitStore.getSubmitNum() == 0) {
			visitStore.setSubmitNum(1);
		}
		
		return visitStore;
	}

	/**
	 * 解析访店控件模块
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseVisitFunc(String json) throws JSONException {
		JSONArray funcArr = new JSONArray(json);
		int len = funcArr.length();
		JSONObject funcObject = null;
		Func func = null;
		VisitStore visitStore = null;
		Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
		Map<Integer, VisitStore> storeMap = new HashMap<Integer, VisitStore>();
		for (int i = 0; i < len; i++) {
			funcObject = funcArr.getJSONObject(i);
			func = putFunc(funcObject);
			if (func == null)
				continue;
			if (!tempMap.containsKey(func.getTargetid())) {
				visitFuncDB.removeFuncByTargetid(func.getTargetid());
			}
			tempMap.put(func.getTargetid(), 0);
			visitFuncDB.insertFunc(func);

			visitStore = new VisitStore();
			if (isValid(funcObject, IS_CHECKIN)) {
				visitStore.setIsCheckin(funcObject.getInt(IS_CHECKIN));
			}
			if (isValid(funcObject, IS_CHECKOUT)) {
				visitStore.setIsCheckout(funcObject.getInt(IS_CHECKOUT));
			}
			if (isValid(funcObject, IS_CHECK)) {
				visitStore.setIsCheck(funcObject.getInt(IS_CHECK));
			}
			if (isValid(funcObject, STROE_DISTANCE)) {
				visitStore.setStoreDistance(funcObject.getInt(STROE_DISTANCE));
			}
			if (isValid(funcObject, LOC_TYPE)) {
				visitStore.setLocType(funcObject.getString(LOC_TYPE));
			}

			if (isValid(funcObject, IS_ADDR)) {
				visitStore.setIsAddress(funcObject.getString(IS_ADDR));
			}
			if (isValid(funcObject, IS_ANEW_LOC)) {
				visitStore.setIsNewLoc(funcObject.getString(IS_ANEW_LOC));
			}
			if (isValid(funcObject, IS_NO_WAIT)) {
				visitStore.setIsNoWait(funcObject.getInt(IS_NO_WAIT));
			}else{
				visitStore.setIsNoWait(1);
			}
			if (isValid(funcObject, VISIT_COUNTS)) {
				visitStore.setSubmitNum(funcObject.getInt(VISIT_COUNTS));
			}
			
			if (!storeMap.containsKey(func.getTargetid())) { // 只执行一遍，执行过了就不在执行了
				visitStoreDB.updateVisitStore(func.getTargetid(),
						visitStore.getIsCheckin(), visitStore.getIsCheckout(),
						visitStore.getIsCheck(), visitStore.getStoreDistance(),
						visitStore.getLocType(), visitStore.getIsAddress(),
						visitStore.getIsNewLoc(), visitStore.getIsNoWait());
				storeMap.put(func.getTargetid(), visitStore);
			}
		}
	}

	
	/**
	 * 解析自定义模块控件
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public Func putFunc(JSONObject jsonObject) throws JSONException {
		Func func = new Func();

         if (isValid(jsonObject,IS_SCAN)){
			 func.setIsScan(Integer.parseInt(jsonObject.get(IS_SCAN).toString()));
		 }
		if (isValid(jsonObject,SCAN_STATUS)){
			func.setScanStatus(Integer.parseInt(jsonObject.get(SCAN_STATUS).toString()));
		}
		if (isValid(jsonObject,SCAN_COLS)){
			func.setScanCols(jsonObject.get(SCAN_COLS).toString());
		}

		if (isValid(jsonObject,IS_SCAN)){
			func.setIsScan(jsonObject.getInt(IS_SCAN));
		}
		if (isValid(jsonObject, ID)) {
			func.setFuncId(jsonObject.getInt(ID));
		}
		if (isValid(jsonObject, NAME)) {
			func.setName(jsonObject.getString(NAME));
		}
//		if (isValid(jsonObject, SHOW_COLOR)) {
//			func.setShowColor(jsonObject.getString(SHOW_COLOR));
//		}
		if (isValid(jsonObject, DEFAULT_TYPE_VALUE)) {
			func.setDefaultType(jsonObject.getInt(DEFAULT_TYPE_VALUE));
		}
		if (isValid(jsonObject, DEFAULT_VALUE)) {
			func.setDefaultValue(jsonObject.getString(DEFAULT_VALUE));
		}
		if (isValid(jsonObject, IS_FUZZY)) {
			func.setIsFuzzy(jsonObject.getInt(IS_FUZZY));
		}
		if (isValid(jsonObject, PHOTO_FLG)) {
			func.setPhotoFlg(jsonObject.getInt(PHOTO_FLG));
		}else{
			func.setPhotoFlg(0);
		}
		if (isValid(jsonObject, IS_IMG_CUSTOM)) {
			func.setIsImgCustom(jsonObject.getInt(IS_IMG_CUSTOM));
		}
		if (isValid(jsonObject, IS_IMG_STORE)) {
			func.setIsImgStore(jsonObject.getInt(IS_IMG_STORE));
		}
		if (isValid(jsonObject, IS_IMG_USER)) {
			func.setIsImgUser(jsonObject.getInt(IS_IMG_USER));
		}
		if (isValid(jsonObject, TYPE)) {
			int type = jsonObject.getInt(TYPE);
			switch (type) {
			case FUNC_TYPE_TEXT:
				func.setType(Func.TYPE_EDITCOMP);
				func.setDataType(Func.DATATYPE_TEXT);
				break;
			case FUNC_TYPE_RECORD:
				func.setType(Func.TYPE_RECORD);
				break;
			case FUNC_TYPE_ORDER:
				func.setType(Func.TYPE_ORDER);
				break;
			case FUNC_TYPE_PRODUCT_CODE:
				func.setType(Func.TYPE_PRODUCT_CODE);
				break;
			case FUNC_TYPE_SQL_BIG_DATA:
				func.setType(Func.TYPE_SQL_BIG_DATA);
				break;
			case FUNC_TYPE_PWD:
				func.setType(Func.TYPE_EDITCOMP);
				func.setDataType(Func.DATATYPE_PWD); // 密码被分到数据类型中
				break;
			case FUNC_TYPE_SELECT:
				if (func.getIsFuzzy() != null && func.getIsFuzzy() == Func.IS_Y) {
					func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
				} else {
					func.setType(Func.TYPE_SELECTCOMP);
				}
				break;
			case FUNC_TYPE_SINGLE_CHOICE_FUZZY_QUERY:
				func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP);
				break;
			case FUNC_TYPE_MULTI_CHOICE_FUZZY_QUERY:
				func.setType(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);
				break;
			case FUNC_TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY:
				func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP);
				break;
			case FUNC_TYPE_TEXTAREA:
				func.setType(Func.TYPE_EDITCOMP);
				func.setDataType(Func.DATATYPE_AREATEXT); // 文本域被分动数据类型中
				break;
			case FUNC_TYPE_LABLE:
				func.setType(Func.TYPE_TEXTCOMP);
				break;
			case FUNC_TYPE_DATE:
				func.setType(Func.TYPE_DATEPICKERCOMP);
				break;
			case FUNC_TYPE_TIME:
				func.setType(Func.TYPE_TIMEPICKERCOMP);
				break;
			case FUNC_TYPE_CAMERA:
				func.setType(Func.TYPE_CAMERA);
				break;
			case FUNC_TYPE_CAMERA_MIDDLE:
				func.setType(Func.TYPE_CAMERA_MIDDLE);
				break;
			case FUNC_TYPE_CAMERA_HEIGHT:
				func.setType(Func.TYPE_CAMERA_HEIGHT);
				break;
			case FUNC_TYPE_CAMERA_CUSTOM:
				func.setType(Func.TYPE_CAMERA_CUSTOM);
				break;
			case FUNC_TYPE_LOCATION:
				func.setType(Func.TYPE_LOCATION);
				break;
			case FUNC_TYPE_TABLE:
				func.setType(Func.TYPE_TABLECOMP);
				break;
			case FUNC_TYPE_LINK:
			case FUNC_TYPE_LINK_OUTSIDE_DATA:
				func.setType(Func.TYPE_LINK);
				break;
			case FUNC_TYPE_TEXT_NUMERIC:
				func.setType(Func.TYPE_EDITCOMP_NUMERIC);
				func.setDataType(Func.DATATYPE_EDITCOMP_NUMERIC);
				break;
			case FUNC_TYPE_HIDDEN:
				func.setType(Func.TYPE_HIDDEN);
				break;
			case FUNC_TYPE_SELECT_USER:
				func.setType(Func.TYPE_SELECTCOMP);
				func.setOrgOption(Func.ORG_USER);
				break;
			case FUNC_TYPE_SELECT_STORE:
				func.setType(Func.TYPE_SELECTCOMP);
				func.setOrgOption(Func.ORG_STORE);
				break;
			case FUNC_TYPE_SELECT_OTHER:
				if (func.getIsFuzzy() != null && func.getIsFuzzy() == Func.IS_Y) {
					func.setType(Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP);
				} else {
					func.setType(Func.TYPE_SELECT_OTHER);
				}

				break;
			case FUNC_TYPE_SUBMIT_BUTTON:
				func.setType(Func.TYPE_BUTTON);
				break;
			case FUNC_TYPE_MULTI_SELECT:
				if (func.getIsFuzzy() != null && func.getIsFuzzy() == Func.IS_Y) {
					func.setType(Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP);
				} else {
					func.setType(Func.TYPE_MULTI_CHOICE_SPINNER_COMP);
				}
				break;
			case FUNC_TYPE_SCAN:
				if ("99".equals(func.getDefaultValue())) {// DefaultValue=99说明该扫描控件是可输入的扫描控件
					func.setType(Func.TYPE_SCAN_INPUT);
				}else{
					func.setType(Func.TYPE_SCAN);
				}
				break;
			case FUNC_TYPE_STORE_VIEW:
				func.setType(Func.TYPE_STORE_VIEW);
				break;
			case FUNC_TYPE_STORE_UPDATA:
				func.setType(Func.TYPE_STORE_UPDATA);
				break;
			case FUNC_TYPE_HIDDEN_SELECT:
				func.setType(Func.TYPE_HIDDEN);
				func.setDefaultType(Func.DEFAULT_TYPE_SELECT);
				func.setDefaultValue(null);
				break;
			// case FUNC_TYPE_LINK_OUTSIDE_DATA:
			// func.setType(Func.TYPE_LINK_OUTSIDE_DATA);
			// break;
			case FUNC_TYPE_HIDDEN_MULTI_SELECT:
				func.setType(Func.TYPE_HIDDEN);
				func.setDefaultType(Func.DEFAULT_TYPE_MULTI_SELECT);
				func.setDefaultValue(null);
				break;
			case FUNC_TYPE_QUANTITY:
				func.setType(Func.TYPE_EDITCOMP);
				func.setDataType(Func.DATATYPE_BIG_INTEGER);
				break;
			case FUNC_TYPE_ORDER2:
				func.setType(Func.TYPE_ORDER2);
				break;
			case FUNC_TYPE_ORDER3:
				func.setType(Func.TYPE_ORDER3);
				break;
			case FUNC_TYPE_VIDEO:
				func.setType(Func.TYPE_VIDEO);
                break;
                case FUNC_TYPE_SIGNATURE:
				func.setType(Func.TYPE_SIGNATURE);
                break;
			case FUNC_TYPE_ATTACHMENT:
				func.setType(Func.TYPE_ATTACHMENT);
				break;
			default:
				return null;
			}

		} else {
			return null;
		}
		if (isValid(jsonObject, DATA_TYPE)) {
			int type = jsonObject.getInt(DATA_TYPE);
			switch (type) {
			case FUNC_DATATYPE_SMALL_INTEGER:
				func.setDataType(Func.DATATYPE_SMALL_INTEGER);
				break;
			case FUNC_DATATYPE_BIG_INTEGER:
				func.setDataType(Func.DATATYPE_BIG_INTEGER);
				break;
			case FUNC_DATATYPE_DECIMAL:
				func.setDataType(Func.DATATYPE_DECIMAL);
				break;
			}

		}
		if (isValid(jsonObject, TARGET_ID)) {
			func.setTargetid(jsonObject.getInt(TARGET_ID));
		}
		if (isValid(jsonObject, LOC_LEVEL)) {
			func.setLocLevel(jsonObject.getString(LOC_LEVEL));
		}
		if (isValid(jsonObject, LENGTH)) {
			func.setLength(jsonObject.getInt(LENGTH));
		}
		if (isValid(jsonObject, IS_EMPTY)) {
			func.setIsEmpty(jsonObject.getInt(IS_EMPTY));
		}
		if (isValid(jsonObject, CHECK_TYPE)) {
			func.setCheckType(jsonObject.getInt(CHECK_TYPE));
		}
		if (isValid(jsonObject, NEXT)) {
			func.setNextDropdown(jsonObject.getInt(NEXT));
		}
		if (isValid(jsonObject, "table")) {
			func.setTableId(jsonObject.getInt("table"));
		}

		if (isValid(jsonObject, DICT_TABLE)) {
			func.setDictTable(jsonObject.getString(DICT_TABLE));
		}
		if (isValid(jsonObject, DICT_DATA_ID)) {
			func.setDictDataId(jsonObject.getString(DICT_DATA_ID));
		}
		if (isValid(jsonObject, DICT_COLS)) {
			func.setDictCols(jsonObject.getString(DICT_COLS));
		}
		if (isValid(jsonObject, DICT_SORTS)) {
			func.setDictIsAsc(jsonObject.getString(DICT_SORTS));
		}
		if (isValid(jsonObject, DICT_SORTS_CLOS)) {
			func.setDictOrderBy(jsonObject.getString(DICT_SORTS_CLOS));
		}
		if (isValid(jsonObject, this.MENU_ID)) {
			func.setMenuId(jsonObject.getInt(MENU_ID));
		}
		if (isValid(jsonObject, OPERATION)) {
			func.setOrgOption(jsonObject.getInt(OPERATION));
		}
		if (isValid(jsonObject, OPERATION_LEVEL)) {
			func.setOrgLevel(jsonObject.getInt(OPERATION_LEVEL));
		}
		if (isValid(jsonObject, WEDORDER)) {
			func.setWebOrder(jsonObject.getInt(WEDORDER));
		}
		if (isValid(jsonObject, IS_SEARCH)) {
			func.setIsSearch(jsonObject.getInt(IS_SEARCH));
		}
		if (isValid(jsonObject, IS_LIST_DISP)) {
			func.setIsShowColumn(jsonObject.getInt(IS_LIST_DISP));
		}
		if (isValid(jsonObject, STATUS)) {
			func.setStatus(jsonObject.getInt(STATUS));
		}
		if (isValid(jsonObject, REPLENISH_ABLE_STATUS)) {
			func.setReplenish_able_status(jsonObject
					.getString(REPLENISH_ABLE_STATUS));
		}
		if (isValid(jsonObject, DUP_ALLOW_DAYS)) {
			func.setDayNumber(jsonObject.getInt(DUP_ALLOW_DAYS));

		}
		if (isValid(jsonObject, DUP_ALLOW_TIMES)) {
			func.setOperateNumber(jsonObject.getInt(DUP_ALLOW_TIMES));

		}
		if (isValid(jsonObject, IS_SEARCH_MODIFY)) {
			func.setIsSearchModify(jsonObject.getInt(IS_SEARCH_MODIFY));
		}
		if (isValid(jsonObject, INPUT_ORDER)) {
			func.setInputOrder(jsonObject.getInt(INPUT_ORDER));
		}
		if (isValid(jsonObject, ENTER_MUST_LIST)) {
			func.setEnterMustList(jsonObject.getString(ENTER_MUST_LIST));
		}
		if (isValid(jsonObject, IS_SEARCH_MUL)) {
			func.setIsSearchMul(jsonObject.getInt(IS_SEARCH_MUL));
		}
		if (isValid(jsonObject, PRINT_ALIGNMENT)) {
			func.setPrintAlignment(jsonObject.getInt(PRINT_ALIGNMENT));
		}
		if (isValid(jsonObject, IS_EDIT)) {
			func.setIsEdit(jsonObject.getInt(IS_EDIT));
		}
		if (isValid(jsonObject, IS_IMPORT_KEY)) {
			func.setIsImportKey(jsonObject.getInt(IS_IMPORT_KEY));
		}
		if (isValid(jsonObject, PHONE_COL_WIDTH)) {
			func.setColWidth(jsonObject.getInt(PHONE_COL_WIDTH));
		}
		if (isValid(jsonObject, PHOTO_TIME_TYPE)) {
			func.setPhotoTimeType(jsonObject.getInt(PHOTO_TIME_TYPE));
		}
		if (isValid(jsonObject, PHOTO_LOCATION_TYPE)) {
			func.setPhotoLocationType(jsonObject.getInt(PHOTO_LOCATION_TYPE));
		}
		if (isValid(jsonObject, CODE_TYPE)) {
			func.setCodeType(jsonObject.getString(CODE_TYPE));
		}
		if (isValid(jsonObject, IS_AREA_SEARCH)) {
			func.setIsAreaSearch(jsonObject.getInt(IS_AREA_SEARCH));
		}
		if (isValid(jsonObject, AREA_SEARCH_VALUE)) {
			func.setAreaSearchValue(Float.parseFloat(jsonObject
					.getString(AREA_SEARCH_VALUE)));
		}
		if (isValid(jsonObject, CODE_CONTROL)) {
			func.setCodeControl(jsonObject.getString(CODE_CONTROL));
		}
		if (isValid(jsonObject, CODE_UPDATE)) {
			func.setCodeUpdate(jsonObject.getString(CODE_UPDATE));
		}

		if (isValid(jsonObject, LOC_TYPE)) {
			func.setLocType(jsonObject.getString(LOC_TYPE));
		}
		if (isValid(jsonObject, IS_ADDR)) {
			func.setIsAddress(jsonObject.getString(IS_ADDR));
		}
		if (isValid(jsonObject, IS_ANEW_LOC)) {
			func.setIsNewLoc(jsonObject.getString(IS_ANEW_LOC));
		}
		if (isValid(jsonObject, LOC_CHECK_DISTANCE)) {
			func.setLocCheckDistance(jsonObject.getString(LOC_CHECK_DISTANCE));
		}
		if (isValid(jsonObject, TASK_STATUS)) {
			func.setTaskStatus(jsonObject.getString(TASK_STATUS));
		}
		if (isValid(jsonObject, IS_ADD_DEL)) {
			func.setAbleAD(jsonObject.getString(IS_ADD_DEL));
		}
		if (isValid(jsonObject, IS_CACHE_FUNC)) {//是否是 缓存控件
			func.setIsCacheFun(jsonObject.getInt(IS_CACHE_FUNC));
		}
		if (isValid(jsonObject, RESTRICT_TYPE)) {//控件时间限制类型
			func.setRestrictType(jsonObject.getInt(RESTRICT_TYPE));
		 }
		if (isValid(jsonObject, RESTRICT_RULE)) {//控件时间显示规则
			func.setRestrictRule(jsonObject.getString(RESTRICT_RULE));
		}
			
		return func;
	}

	/**
	 * 解析自定义模块控件模块
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseBaseFunc(String json) throws JSONException {
		FuncCacheDB funcCacheDB = new FuncCacheDB(context);
		JSONArray funcArr = new JSONArray(json);
		int len = funcArr.length();
		JSONObject funcObject = null;
		Func func = null;
		Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < len; i++) {
			funcObject = funcArr.getJSONObject(i);
			func = putFunc(funcObject);
			if (func == null)
				continue;
			if (!tempMap.containsKey(func.getTargetid())) {
				funcDB.removeFuncByTargetid(func.getTargetid());
			}
			tempMap.put(func.getTargetid(), 0);
			funcDB.insertFunc(func);
//			if (func.getIsCacheFun()==1) {//如果是缓存控件
//				FuncCache funcCache = new FuncCache();
//				funcCache.setFuncId(func.getFuncId()+"");
//				funcCache.setModleId(func.getTargetid()+"");
//				funcCacheDB.insertFuncCache(funcCache);
//			}
		}
	}

	/**
	 * 解析字典表
	 * 
	 * @param json
	 * @param isDelDict
	 *            是否删除字典表 true是 false 否
	 * @throws JSONException
	 */
	public void parseDictionary(String json, boolean isDelDict)throws JSONException {
		long l = System.currentTimeMillis();
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (isValid(jsonObject, DICT_TABLE)) {
				String dictTable = jsonObject.getString(DICT_TABLE);
				String dictCols = jsonObject.getString(COLS);
				String cols[] = dictCols.split(",");
				if (isDelDict) {
					dictDB.deleteTable(dictTable);
					dictDB.createDictTable(dictTable, cols);
				} else {
					if (!dictDB.isEmptyTable(dictTable)) {
						dictDB.createDictTable(dictTable, cols);
					}
				}
				if (isValid(jsonObject, DATA)) {
					JSONArray dictArr = jsonObject.getJSONArray(DATA);
					JSONArray dataArr = null;
					int length = dictArr.length();
					for (int j = 0; j < length; j++) {
						dataArr = dictArr.getJSONArray(j);
						dictDB.insertDict2(putDictionary(dataArr, cols),dictTable);
					}
				}
			}
		}
		JLog.d("InitActivity",
				"parseDictionary =>所用时间：" + (System.currentTimeMillis() - l));
	}

	/**
	 * 解析字典表
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseBatchDictionary(String json) throws JSONException {
		long l = System.currentTimeMillis();
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (isValid(jsonObject, DICT_TABLE)) {
				String dictTable = jsonObject.getString(DICT_TABLE);
				String dictCols = jsonObject.getString(COLS);
				String cols[] = dictCols.split(",");
				dictDB.createDictTable(dictTable, cols);
				if (isValid(jsonObject, DATA)) {
					JSONArray dictArr = jsonObject.getJSONArray(DATA);
					JSONArray dataArr = null;
					int length = dictArr.length();
					DatabaseHelper.getInstance(context).beginTransaction();
					for (int j = 0; j < length; j++) {
						dataArr = dictArr.getJSONArray(j);
						dictDB.insertDict(putDictionary(dataArr, cols),
								dictTable);
					}
					DatabaseHelper.getInstance(context).endTransaction();
				}
			}
		}
		JLog.d("InitActivity",
				"parseBatchDictionary =>所用时间："
						+ (System.currentTimeMillis() - l));
	}

	/**
	 * 获取字典数据的名称的集合
	 * 
	 * @param jsonArray
	 * @param cols
	 * @return
	 * @throws JSONException
	 */
	private List<Content> putDictionary(JSONArray jsonArray, String[] cols)
			throws JSONException {
		int length = cols.length;
		List<Content> list = new ArrayList<Content>();
		Content content = null;
		for (int i = 0; i < length; i++) {
			content = new Content();
			content.setKey(cols[i]);
			content.setValue(jsonArray.isNull(i) ? null : jsonArray
					.getString(i));
			list.add(content);
		}
		return list;
	}

	/**
	 * 解析公告
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseNotify(String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			Notice notice = putNotify(jsonObject);
			if (notice.getIsread() == null) {
				notice.setIsread(Notice.ISREAD_N);
			}
			noticeDB.deleteNoticeByNotifyId(notice.getNotifyId());
			noticeDB.insertNotice(notice);
		}
	}

	/**
	 * 解析获取的公告
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public List<Notice> parseGetNotify(String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		List<Notice> list = new ArrayList<Notice>();
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			Notice notice = putNotify(jsonObject);
			list.add(notice);
		}
		return list;
	}

	/**
	 * 解析获取的任务
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public List<Task> parseGetTask(String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		List<Task> list = new ArrayList<Task>();
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			Task task = putTask(jsonObject);
			task.setIsread(Notice.ISREAD_N);
			list.add(task);
		}
		return list;
	}

	/**
	 * 解析公告对象
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public Notice putNotify(JSONObject jsonObject) throws JSONException {
		Notice notic = new Notice();
		if (isValid(jsonObject, this.NOTIFY_CONTENT)) {
			notic.setDetailNotice(jsonObject.getString(NOTIFY_CONTENT));
		}
		if (isValid(jsonObject, NOTIFY_TITLE)) {
			notic.setNoticeTitle(jsonObject.getString(NOTIFY_TITLE));
		}
		if (isValid(jsonObject, NOTIFY_ISSUER_TIME)) {
			notic.setCreateTime(jsonObject.getString(NOTIFY_ISSUER_TIME));
		}
		if (isValid(jsonObject, NOTIFY_ISSUER_NAME)) {
			notic.setCreateUser(jsonObject.getString(NOTIFY_ISSUER_NAME));
		}
		if (isValid(jsonObject, NOTIFY_ISSUER_ORG)) {
			notic.setCreateOrg(jsonObject.getString(NOTIFY_ISSUER_ORG));
		}
		if (isValid(jsonObject, ID)) {
			notic.setNotifyId(jsonObject.getInt(ID));
		}
		if (isValid(jsonObject, NOTIFY_READ)) {
			notic.setIsread(jsonObject.getInt(NOTIFY_READ));
		}
		if (isValid(jsonObject, NOTIFY_TYPE)) {
			notic.setNotifyType(jsonObject.getString(NOTIFY_TYPE));
		}
		if (isValid(jsonObject, NOTIFY_ATTACHMENT)) {
			String attachment = jsonObject.getString(NOTIFY_ATTACHMENT);
			notic.setAttachment(attachment);
		}
		return notic;
	}

	/**
	 * 解析自定义模块查询类
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseCustom(String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		JSONArray array = null;
		for (int i = 0; i < len; i++) {
			array = jsonArray.getJSONArray(i);
			for (int j = 0; j < array.length(); j++) {
				jsonObject = array.getJSONObject(j);
				Task task = putTask(jsonObject);
				if (task.getIsread() == null) {
					task.setIsread(Task.ISREAD_N);
				}
				currentTask = task;
				taskDB.removeTask(task.getModuleid() + "", task.getTaskId()
						+ "");
				taskDB.insertTaskData(task);
			}
		}
	}

	/**
	 * 解析任务对象
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public Task putTask(JSONObject jsonObject) throws JSONException {
		Task task = new Task();
		if (isValid(jsonObject, this.TASK_CONTENT)) {
			task.setDetailTask(jsonObject.getString(TASK_CONTENT));
		}
		if (isValid(jsonObject, TASK_TITLE)) {
			task.setTaskTitle(jsonObject.getString(TASK_TITLE));
		}
		if (isValid(jsonObject, TASK_CREATE_TIME)) {
			task.setCreateTime(jsonObject.getString(TASK_CREATE_TIME));
		}
		if (isValid(jsonObject, TASK_CREATE_NAME)) {
			task.setCreateUser(jsonObject.getString(TASK_CREATE_NAME));
		}
		if (isValid(jsonObject, MODULE_ID)) {
			task.setModuleid(Integer.parseInt(jsonObject.getString(MODULE_ID)));
		}
		if (isValid(jsonObject, TASK_ID)) {
			task.setTaskId(Integer.parseInt(jsonObject.getString(TASK_ID)));
		}
		return task;
	}

	/**
	 * 解析自定义模块双向类
	 * 
	 * @param json
	 * @param isInit
	 *            是否初始化双向表 true是 false否
	 * @throws JSONException
	 */
	public void parseDouble(String json, boolean isInit) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		int len = jsonArray.length();
		JSONObject jsonObject = null;
		for (int i = 0; i < len; i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (isValid(jsonObject, DOUBLE_TABLE)) {
				String table = jsonObject.getString(DOUBLE_TABLE);
				String colStr = jsonObject.getString(DOUBLE_COLS);
				String cols[] = colStr.split(",");
				if (isInit) {
					doubleDB.createDoubleTable(table, cols);
				} else {
					doubleDB.createDoubleTable2(table, cols);
				}
				if (isValid(jsonObject, DOUBLE_DATA)) {
					JSONArray doubleArr = jsonObject.getJSONArray(DOUBLE_DATA);
					JSONArray dataArr = null;
					int length = doubleArr.length();
					List<Content> cList = null;
					for (int j = 0; j < length; j++) {
						dataArr = doubleArr.getJSONArray(j);
						cList = putDouble(dataArr, cols, table);
						doubleDB.insert(cList, table);
						if (!isInit) {
							pushDoubleCallBack(cList, table);
						}
					}
				}
			}
		}
	}

	private void pushDoubleCallBack(List<Content> list, String targetId) {

		if (!list.isEmpty()) {
			String did = "";
			for (Content content : list) {
				if ("taskid".equals(content.getKey())) {
					did = content.getValue();
					break;
				}
			}
			if (handler != null) {
				String url = UrlInfo.getUrlDoDataStatusInfo(context,
						TextUtils.isEmpty(targetId) ? "" : targetId, 1,
						TextUtils.isEmpty(did) ? "" : did);
				handler.obtainMessage(100, url).sendToTarget();
			}
		}
	}

	/**
	 * 获取双双向内容
	 * 
	 * @param jsonArray
	 * @param cols
	 *            控件ID
	 * @param table
	 *            表名
	 * @return
	 * @throws JSONException
	 */
	private List<Content> putDouble(JSONArray jsonArray, String[] cols,String table) throws JSONException {
		int length = cols.length;
		List<Content> list = new ArrayList<Content>();
		Content content = null;
		for (int i = 0; i < length; i++) {
			content = new Content();
			content.setKey(cols[i]);
			content.setValue(jsonArray.isNull(i) ? null : jsonArray.getString(i));
			list.add(content);
			if (cols[i].equals(DOUBLE_COLUMN_TASKID)) {
				currentTargetId = Integer.valueOf(table);
				doubleDB.removeDoubleTask(content.getValue(), table);
			}
		}
		return list;
	}

	/**
	 * 解析被动定位的规则
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void parseLocationRule(String json, boolean isStart,boolean isSyncData)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		String start = null;
		String stop = null;
		Integer interval = null;
		if (isValid(jsonObject, LOCATION_START_TIME)) {
			start = jsonObject.getString(LOCATION_START_TIME);
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext())
					.setLocationStartTime(start);
		}
		if (isValid(jsonObject, LOCATION_END_TIME)) {
			stop = jsonObject.getString(LOCATION_END_TIME);
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).setLocationStopTime(stop);
		}
		if (isValid(jsonObject, LOCATION_INTERVAL_TIME)) {
			interval = jsonObject.getInt(LOCATION_INTERVAL_TIME);
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).setLocationEachInterval(
					interval);

		}
		if (isValid(jsonObject, LOCATION_WEEKLY)) {
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).setLocationPeriodRule(
					jsonObject.getString(LOCATION_WEEKLY));
		}
		if (isValid(jsonObject, LOCATION_STATUS)) {
			int flag = jsonObject.getInt(LOCATION_STATUS);
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).setLocationIsAvailable(
					flag == 1 ? true : false);
		}

		if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(stop)  && !isSyncData) {
			BackstageLocationManager manager = new BackstageLocationManager(context);
			manager.updateLocationRuleForInit(true);
		}

		if (isValid(jsonObject, LOCATION_TIP_TYPE)) {
			int tipType = jsonObject.getInt(LOCATION_TIP_TYPE);
			SharedPrefsBackstageLocation.getInstance(context).saveLocationTipType(tipType);
			if (tipType == 1 && !isSyncData) {// 有新规则下达的时候并且不是同步数据要提示 
				SharedPreferencesUtil.getInstance(context).setIsTipUser(false);// 设置成fales表示没有提示用户
			}
		}

		if (isStart) {
			// 开启被动定位service
			Intent i = new Intent(context, BackstageLocationService.class);
			i.putExtra("IS_STOP", true);
			context.startService(i);
		}
	}

	/**
	 * 解析报表
	 */
	public void parseReport(String json, int type) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		JSONObject jsonObject = null;
		Menu menu = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			menu = new Menu();
			if (isValid(jsonObject, REPORT_NAME)) {
				menu.setName(jsonObject.getString(REPORT_NAME));
			}
			if (isValid(jsonObject, PHONE_SERIAL_NO)) {
				menu.setPhoneSerialNo(jsonObject.getInt(PHONE_SERIAL_NO));
			}
			if (isValid(jsonObject, ID)) {
				menu.setMenuId(jsonObject.getInt(ID));
				if (type == Menu.TYPE_REPORT) {
					mainMenuDB.removeMenuByMenuIdAndType(menu.getMenuId(),
							Menu.TYPE_REPORT);
					menu.setType(Menu.TYPE_REPORT);
				} else {
					mainMenuDB.removeMenuByMenuIdAndType(menu.getMenuId(),
							Menu.TYPE_REPORT_NEW);
					menu.setType(Menu.TYPE_REPORT_NEW);
				}
				mainMenuDB.insertMenu(menu);
			}
		}
	}

	/**
	 * 解析Role
	 */
	public void parseRole(String json) throws JSONException {
		JSONArray jsonArray = new JSONArray(json);
		JSONObject jsonObject = null;
		Role role = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			role = new Role();
			if (isValid(jsonObject, ROLE_ID)) {
				role.setRoleId(jsonObject.getInt(ROLE_ID));
				roleDB.removeByRoleId(role.getRoleId() + "");
			}
			if (isValid(jsonObject, ROLE_NAME)) {
				role.setName(jsonObject.getString(ROLE_NAME));
			}
			if (isValid(jsonObject, ROLE_ORG_LEVEL)) {
				role.setLevel(jsonObject.getString(ROLE_ORG_LEVEL));
			}
			roleDB.insertRole(role);
		}
	}
	
//	public boolean isHasOrder3 = false;//是否有订单3
	public void parseOrder3(JSONObject jsonObj) throws JSONException{
		if (jsonObj != null) {
			if (jsonObj.has(ORDER_3)) {//添加订单模块
				mainMenuDB.removeMenuByType(Menu.TYPE_ORDER3);
				String str = jsonObj.getString(ORDER_3);
				String[] strArr = str.split("\\|");
				if (Integer.parseInt(strArr[0]) == 1 ? true : false) {
					Menu menu = new Menu();
					menu.setType(Menu.TYPE_ORDER3);
					menu.setName(context.getResources().getString(R.string.order_3));
//					menu.setPhoneUsableTime(SharedPreferencesForOrder3Util.getInstance(context).getEnableTime());//解析的时候处理
					if (strArr.length == 2) {
						menu.setPhoneSerialNo(Integer.parseInt(strArr[1]));
					} else {
						menu.setPhoneSerialNo(0);
					}
					mainMenuDB.insertMenu(menu);
//					isHasOrder3 = true;
					if (PublicUtils.isValid(jsonObj, THREE_ORDER)) {
						Order3Parse parser = new Order3Parse(context);
						JSONObject threeObj = jsonObj.getJSONObject(THREE_ORDER);
						parser.parserOrder3(threeObj);
						if (PublicUtils.isValid(jsonObj, THREE_PROMOTION)) {
							JSONArray promotionArray = jsonObj.getJSONArray(THREE_PROMOTION);
							parser.parserProductPromotion(promotionArray);
						}
						if (PublicUtils.isValid(jsonObj, ORDERDIS)) {
							JSONArray disArray = jsonObj.getJSONArray(ORDERDIS);
							parser.parserOrder3Dis(disArray);
						}
						new Order3Util(context).initOrder3ProductCtrl();
					}
					
				}
			}

		}
	}
	/**
	 * 送货
	 * @param jsonObj
	 * @throws JSONException
	 */
	public void parseOrder3Send(JSONObject jsonObj) throws JSONException{
		if (jsonObj != null) {
			if (jsonObj.has(ORDER_3_SEND)) {//添加订单模块
				mainMenuDB.removeMenuByType(Menu.TYPE_ORDER3_SEND);
				String str = jsonObj.getString(ORDER_3_SEND);
				String[] strArr = str.split("\\|");
				if (Integer.parseInt(strArr[0]) == 1 ? true : false) {
					Menu menu = new Menu();
					menu.setType(Menu.TYPE_ORDER3_SEND);
					menu.setName(context.getResources().getString(R.string.order_3_send));
//					menu.setPhoneUsableTime(SharedPreferencesForOrder2Util.getInstance(context).getEnableTime());
					if (strArr.length == 2) {
						menu.setPhoneSerialNo(Integer.parseInt(strArr[1]));
					} else {
						menu.setPhoneSerialNo(0);
					}
					mainMenuDB.insertMenu(menu);
					Order3Parse parser = new Order3Parse(context);
					if (PublicUtils.isValid(jsonObj, THREE_ORDER)) {
						JSONObject threeObj = jsonObj.getJSONObject(THREE_ORDER);
						parser.parserOrder3(threeObj);
					}
					if (PublicUtils.isValid(jsonObj, THREE_PROMOTION)) {
						JSONArray promotionArray = jsonObj.getJSONArray(THREE_PROMOTION);
						parser.parserProductPromotion(promotionArray);
					}
					if (PublicUtils.isValid(jsonObj, ORDERDIS)) {
						JSONArray disArray = jsonObj.getJSONArray(ORDERDIS);
						parser.parserOrder3Dis(disArray);
					}
					MainMenuDB db = new MainMenuDB(context);
					if (menu!= null) {
						menu.setPhoneUsableTime(SharedPreferencesForOrder3Util.getInstance(context).getSendEnableTime());
						db.updateMenuById(menu);
					}
				}
			}
			
		}
	}
	
	/**
	 * 送货
	 * @param jsonObj
	 * @throws JSONException
	 */
	public void parseCarSales(JSONObject jsonObj) throws JSONException{
		if (jsonObj != null) {
//			jsonObj.put(CAR_SALES, "1");
			if (jsonObj.has(CAR_SALES)) {//添加订单模块
				mainMenuDB.removeMenuByType(Menu.TYPE_CAR_SALES);
				String str = jsonObj.getString(CAR_SALES);
				String[] strArr = str.split("\\|");
				if (Integer.parseInt(strArr[0]) == 1 ? true : false) {
					Menu menu = new Menu();
					menu.setType(Menu.TYPE_CAR_SALES);
					menu.setName(context.getResources().getString(R.string.car_sales));
//					menu.setPhoneUsableTime(SharedPreferencesForCarSalesUtil.getInstance(context).getEnableTime());
					if (strArr.length == 2) {
						menu.setPhoneSerialNo(Integer.parseInt(strArr[1]));
					} else {
						menu.setPhoneSerialNo(0);
					}
					mainMenuDB.insertMenu(menu);
					CarSalesParse carSalesParse = new CarSalesParse(context);
					if (PublicUtils.isValid(jsonObj, CAR_CONF)) {
						JSONObject confObj = jsonObj.getJSONObject(CAR_CONF);
						carSalesParse.parserCarSales(confObj);
						new CarSalesUtil(context).initCarSalesProductCtrl();
					}
					if (PublicUtils.isValid(jsonObj, CAR_PROMOTION)) {
						JSONArray promotionArray = jsonObj.getJSONArray(CAR_PROMOTION);
						carSalesParse.parserProductPromotion(promotionArray);
					}
				}
			}
			
		}
	}

	
	/**
	 * 解析订单
	 */
	public void parseOrder2(JSONObject jsonObj) throws JSONException {
		if (jsonObj != null) {
			
			if (jsonObj.has(ORDER_NEW)) {
				String orderNew = jsonObj.getString(ORDER_NEW);
				if (TextUtils.isEmpty(orderNew)) {
					return;
				}
				JSONObject jsonObject = jsonObj.getJSONObject(ORDER_NEW);
				if (jsonObject.has(ENABLE_TIME)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setEnableTime(jsonObject.getString(ENABLE_TIME));
				}
				if (jsonObject.has(IS_PHONE_PRICE)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPhonePrice(jsonObject.getInt(IS_PHONE_PRICE));
				}
				if (jsonObject.has(IS_PHONE_PRINT)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPhonePrint(jsonObject.getInt(IS_PHONE_PRINT));
				}
				if (jsonObject.has(IS_PRICE)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPrice(jsonObject.getInt(IS_PRICE));
				}
				if (jsonObject.has(IS_STOCKS)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsStocks(jsonObject.getInt(IS_STOCKS));
				}
				if (jsonObject.has(IS_PAY)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPay(jsonObject.getInt(IS_PAY));
				}
				// if (jsonObject.has(IS_AMOUNT)) {
				// SharedPreferencesForOrder2Util.getInstance(context).setEnableTime(jsonObject.getInt(IS_AMOUNT));
				// }
				if (jsonObject.has(IS_PLACE_USER)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPlaceUser(jsonObject.getInt(IS_PLACE_USER));
				}
				if (jsonObject.has(IS_RECEIVE_USER)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsReceiveUser(
									jsonObject.getInt(IS_RECEIVE_USER));
				}
				if (jsonObject.has(PRICE_CTRL)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setPriceCtrl(jsonObject.getString(PRICE_CTRL));
				}
				if (jsonObject.has(UNIT_CTRL)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setUnitCtrl(jsonObject.getString(UNIT_CTRL));
				}
				if (jsonObject.has(IS_PAY)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setIsPay(jsonObject.getInt(IS_PAY));
				}
				if (jsonObject.has(PRODUCT_CTRL)) {
					SharedPreferencesForOrder2Util.getInstance(context)
							.setProductCtrl(jsonObject.getString(PRODUCT_CTRL));
				}
			}
			
			if (jsonObj.has(ORDER_MENU)) {
				mainMenuDB.removeMenuByType(Menu.TYPE_ORDER2);
				String str = jsonObj.getString(ORDER_MENU);
				String[] strArr = str.split("\\|");
				if (Integer.parseInt(strArr[0]) == 1 ? true : false) {
					Menu menu = new Menu();
					menu.setType(Menu.TYPE_ORDER2);
					menu.setName(context.getResources().getString(R.string.order));
					menu.setPhoneUsableTime(SharedPreferencesForOrder2Util.getInstance(context).getEnableTime());
					if (strArr.length == 2) {
						menu.setPhoneSerialNo(Integer.parseInt(strArr[1]));
					} else {
						menu.setPhoneSerialNo(0);
					}
					mainMenuDB.insertMenu(menu);
				}
			}
			
			
		}
	}
	
	public void parserToDo(String json) throws JSONException{
		TodoDB toDoDB = new TodoDB(context);
		toDoDB.deleteAllToDo();
		JSONObject obj = new JSONObject(json);
		if (isValid(obj, TO_DO_TIME)) {
			String time = obj.getString(TO_DO_TIME);
			Date date = DateUtil.getDate(time, DateUtil.DATATIMEF_STR);
			String s_time = SharedPreferencesUtil.getInstance(context).getToDoDate();
			if (TextUtils.isEmpty(s_time) || date.after(DateUtil.getDate(s_time, DateUtil.DATATIMEF_STR))) {
				SharedPreferencesUtil.getInstance(context).setToDoDate(time);
				JSONArray array = new JSONArray(obj.getString(TO_DO_DATA));
				if (array!=null && array.length()>0) {
					JSONObject toDoObj = null;
					Todo todo = null;
					for (int i = 0; i < array.length(); i++) {
						toDoObj = array.getJSONObject(i);
						todo = new Todo();
						if (toDoObj.has(M_NAME)) {
							todo.setMenuName(toDoObj.getString(M_NAME));
						}
						if (toDoObj.has(S_NAME)) {
							todo.setStateName(toDoObj.getString(S_NAME));
						}
						if (toDoObj.has(TODO_NUM)) {
							todo.setTodoNum(toDoObj.getInt(TODO_NUM));
						}
						toDoDB.insertToDo(todo);
					}
				}
			}
		}
	}
	
	/**
	 * 解析FR图表
	 * @param json
	 * @throws JSONException
	 */
	public void parseWebReport(String json) throws JSONException{
		if (TextUtils.isEmpty(json)) {
			mainMenuDB.removeMenuByType(Menu.TYPE_WEB_REPORT);
			return;
		}
		JSONArray jsonArray = new JSONArray(json);
		JSONObject obj = null;
		Menu menu = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			obj = jsonArray.getJSONObject(i);
			menu = new Menu();
			if (isValid(obj, ID)) {
				menu.setMenuId(obj.getInt(ID));
			}else{
				continue;
			}
			if (isValid(obj, NAME)) {
				menu.setName(obj.getString(NAME));
			}
			if (isValid(obj, PHONE_SERIAL_NO)) {
				menu.setPhoneSerialNo(obj.getInt(PHONE_SERIAL_NO));
			}
			mainMenuDB.removeMenuByMenuIdAndType(menu.getMenuId(),Menu.TYPE_WEB_REPORT);
			menu.setType(Menu.TYPE_WEB_REPORT);
			mainMenuDB.insertMenu(menu);
		}
	}
	
	/**
	 * 解析就近拜访配置列表
	 * @param jsonObject
	 * @throws JSONException
	 */
	public void parserNearbyConf(JSONObject jsonObject)throws JSONException{
		if (isValid(jsonObject, NEARBY_CONF)){
//			SharedPreferencesUtil2.getInstance(context).savePhoneReport(true);
			JSONArray array = jsonObject.getJSONArray(NEARBY_CONF);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (isValid(obj, NEARBY_MODULE_ID)) {
					parserNearby(obj.getString(NEARBY_MODULE_ID), obj);
				}
			}
		}
	}
	
	/**
	 * 解析就近拜访
	 * @throws JSONException 
	 */
	private void parserNearby(String menuId,JSONObject obj) throws JSONException{
		if (isValid(obj, NEARBY_STYLE)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStyle(menuId,obj.getString(NEARBY_STYLE));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStyle(menuId,null);
		}
		if (isValid(obj, BTN_STORE)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnStore(menuId,obj.getString(BTN_STORE));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnStore(menuId,null);
		}
		if (isValid(obj, BTN_HISTRY)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnHistry(menuId,obj.getString(BTN_HISTRY));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnHistry(menuId,null);
		}
		if (isValid(obj, BTN_MDL)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsMdl(menuId,obj.getString(BTN_MDL));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsMdl(menuId,null);
		}
		if (isValid(obj, BTN_VISIT)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnVisit(menuId,obj.getString(BTN_VISIT));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyBtnVisit(menuId,null);
		}
		if (isValid(obj, NEARBY_STORE_INFO)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreInfo(menuId,obj.getString(NEARBY_STORE_INFO));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreInfo(menuId,null);
		}
		if (isValid(obj, IS_DATA_CPY)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsDataCpy(menuId,obj.getString(IS_DATA_CPY));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsDataCpy(menuId,null);
		}
		if (isValid(obj, IS_MDL)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsMdl(menuId,obj.getString(IS_MDL));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyIsMdl(menuId,null);
		}
		if (isValid(obj, NEARBY_DATA_STATUS)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyDataStatus(menuId,obj.getString(NEARBY_DATA_STATUS));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyDataStatus(menuId,null);
		}
//		if (isValid(obj, NEARBY_MODULE_ID)) {
//			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyModuleId(obj.getString(NEARBY_MODULE_ID));
//		}else{
//			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyModuleId(null);
//		}
		if (isValid(obj, STORE_DETAIL_SQL)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreDetailSql(menuId,obj.getString(STORE_DETAIL_SQL));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreDetailSql(menuId,null);
		}
		if (isValid(obj, STORE_LIST_SQL)) {
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreListSql(menuId,obj.getString(STORE_LIST_SQL));
		}else{
			SharedPreferencesUtilForNearby.getInstance(context).saveNearbyStoreListSql(menuId,null);
		}
	}
	
	/**
	 * 解析就近拜访店面列表数据
	 * @return
	 * @throws JSONException 
	 */
	public List<NearbyListItem> parserNearbyListItem(JSONArray array) throws JSONException{
		List<NearbyListItem> itemList = new ArrayList<NearbyListItem>();
		if (array!=null && array.length()>0) {
			for (int i = 0; i < array.length(); i++) {
				JSONArray itemArray = array.getJSONArray(i);
				NearbyListItem item = new NearbyListItem();
				item.setPatchID(itemArray.getString(0));
				item.setStoreID(itemArray.getString(1));
				item.setTag_1(itemArray.getString(2));
				item.setTag_2(itemArray.getString(3));
				item.setTag_3(itemArray.getString(4));
				item.setTag_4(itemArray.getString(5));
				itemList.add(item);
			}
		}
		return itemList;
	}
	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
}
