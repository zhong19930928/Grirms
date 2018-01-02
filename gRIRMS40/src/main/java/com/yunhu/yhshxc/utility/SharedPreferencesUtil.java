package com.yunhu.yhshxc.utility;

import gcg.org.debug.JLog;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;

public class SharedPreferencesUtil {

	private static Editor saveEditor;

	private static SharedPreferences saveInfo;

	private final String IS_SHOW_HELP = "isShowHelp";

	private final String IS_FIRST_LOGIN = "is1stLogin";
	private final String LOGIN_PASSWORD = "LoginPassword";
	private final String USERNAME = "userName";
	public final String HEAD_IMG = "headImg";//头像图片
	public final String NICK_NAME = "nickName"; // 昵称
	public final String SIGNATURE = "signature";// 个人签名
	public final String ORG_NAME = "orgName";// 部门名称



	private final String ROLENAME = "roleName";
	private final String COMPANY_ID = "companyid";
	private final String COMPANY_NAME = "companyName";
	private final String BUILDDING_ID = "buildingid";
	private final String FLOOR_ID = "floorid";
	private final String USER_ID = "userid";
	private final String ROLE_ID = "roleid";
	private final String ORG_ID = "orgid";
	private final String IS_INIT = "isInit";
	private final String LON = "Lon";
	private final String LAT = "Lat";
	private final String LOCATION_TYPE = "locationType";
	private final String LOCATION_SERVER_LAST_TIME = "location_server_last_time";
	private final String LOCATION_PHONE_LAST_TIME = "location_phone_last_time";
	private final String LOCATION_CONTENT = "location_content";

	private final String AVAIL_START_DATE = "avail_start_date";
	private final String AVAIL_END_DATE = "avail_end_date";

	private final String IS_READLAW = "is_readLaw";
	private final String SEARCH_CONTENT = "search_content";

	private final String DOWNURL = "downUrl";
	private final String VERSION_CODE = "version_code";
	private final String MD5_CODE = "md5_code";

	private final String SCREEN_WIDTH = "widthPixels";
	private final String SCREEN_HEIGHT = "heightPixels";

	private final String LOCATION_VERSION_CODE = "location_version_code";// 当前版本

	private final String HELP_NAME = "help_name";
	private final String HELP_TEL = "help_tel";

	private final String START_WORK_TIME = "start_work_time";
	private final String STOP_WORK_TIME = "stop_work_time";

	private final String NEW_ATTENDANCE_START_OVER = "new_attendance_start_over";
	private final String NEW_ATTENDANCE_END_OVER = "new_attendance_end_over";
	private final String NEW_ATTENDANCE_START_DO_CARD_OVER = "new_attendance_start_do_card_over";
	private final String NEW_ATTENDANCE_END_DO_CARD_OVER = "new_attendance_end_do_card_over";
	private final String NEW_ATTENDANCE_START = "new_attendance_start";
	private final String NEW_ATTENDANCE_END = "new_attendance_end";
	private final String NEW_ATTENDANCE_START_DO_CARD = "new_attendance_start_do_card";
	private final String NEW_ATTENDANCE_END_DO_CARD = "new_attendance_end_do_card";
	private final String NEW_ATTENDANCE_WORK_DAY = "new_attendance_work_day";
	private final String NEW_ATTENDANCE_WORK_TIME = "new_attendance_work_time";
	private final String NEW_ATTENDANCE_OVER_NAME = "new_attendance_over_name";
	private final String NEW_ATTENDANCE_RESET_TYPE = "new_attendance_reset_type";
	
	private final String REPLENISH = "replenish";
	private final String APKSIZE = "apksize";
	private final String DOWNAPKINFO = "down_apk_info";
	private final String SUBSCRIBER_ID = "subscriberId";// sim卡串号
	private final String RANDOM_CODE = "random_code";// 随机码

	private final String PHONE_LOGO_PATH = "phone_logo_path";
	private final String PHONE_NAME1 = "phone_name1";
	private final String PHONE_NAME1_SIZE = "phone_name1_size";
	private final String PHONE_NAME1_ALIGN_TYPE = "phone_name1_align_type";
	private final String PHONE_NAME2 = "phone_name2";
	private final String PHONE_NAME2_SIZE = "phone_name2_size";
	private final String PHONE_NAME2_ALIGN_TYPE = "phone_name2_align_type";
	private final String PHONE_NAME3 = "phone_name3";
	private final String PHONE_NAME3_SIZE = "phone_name3_size";
	private final String PHONE_NAME3_ALIGN_TYPE = "phone_name3_align_type";

	private final String LOCATION_VALID_ACC = "location_valid_acc";

	private final String COMPARE_RULE_IDS = "compare_rule_ids";

	private final String PHONE_FIXED_MODULE_NAME = "phone_fixed_module_name"; // 1:拜访,2:旧考勤,3:考勤,4:公告,5:帮助,6:圈子,7:待审批事项

	//（1：拍照、2：定位、3：说明    存储格式例： 1|2（拍照和定位都必须上报）、1（拍照必须，定位和说明可以不上报）、null（默认为null，如果没有定义，则默认和以前的处理逻辑相同）
	private final String ATTENDANCE_MUST_DO = "attendance_must_do";//考勤上报数据必须操作项
	private final String ATTENDANCE_MUST_DO_NEW = "attendance_must_do_new";//新考勤上报数据必须操作项

	private final String OVER_ATTEND_WAIT = "over_attend_wait";//新考勤是否需要无等待(0：不需要、1：需要，默认为1)
	private final String ATTEND_WAIT = "attend_wait";//旧考勤是否需要无等待(0：不需要、1：需要，默认为1)

	private final String CACHE_ATTENDANCE_DATA = "cacheAttendanceData";//存储考勤要提交的数据
	
	private final String TO_DO_DATE = "to_do_date";//待办事项时间
	
	/**
	 * 音频时间
	 */
	private final String VOICE_TIME = "voice_time";

	// private static final String TAG = "CmccompSharedPreferences";
	
	private final String GRIRMS_USER_IS_CODE_ONE = "grirms_user_is_code_one";//随机码是否是第一次生成 1是第一次2表示不是第一次 服务端用来标识是否要比对三码 1不比对，2比对

	private final String WECHAR_TAGS = "WECHAR_TAGS";//企业微信群聊标签
	
	private final String XIN_DIAN_SB = "xin_dian_sb";//新店上报模块名称
	
	private static SharedPreferencesUtil spUtil = new SharedPreferencesUtil();
	private static Context mContext;
	
	/**
	 * 被动定位规则是否提示过用户
	 */
	private  final String IS_TIP_USER = "is_tip_user";
	
	/**
	 * 程序最后一次在前端运行时间
	 */
	private final String LAST_RUN_TIME = "last_run_time";

	/**
	 * 考勤时间，服务器用于区别同一天的考勤数据
	 */
	private final String NEW_ATTEND_TIME = "new_attend_time";
	private final String NEW_ATTEND_OVER_TIME = "new_attend_over_time";
	private final String LOC_LEVEL = "loc_level";//被动定位方式（1：GPS优先、2：WIFI优先、3：混合定位），空或null时默认为：3：混合定位

	private final String FIND_TIME = "find_time";
	private final String FIND_NUMS = "find_nums";

	private final String CONTRACT_INFO = "contranc_info";
	
	public static SharedPreferencesUtil getInstance(Context context) {
		mContext = context;
		if (saveInfo == null && mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms",
					Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
		return spUtil;
	}

	private SharedPreferencesUtil() {
		if (mContext != null) {
			saveInfo = mContext.getSharedPreferences("grirms",
					Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public void clearAll() {
		saveEditor.clear();
		saveEditor.commit();
	}

	public void clear(String key) {
		saveEditor.remove(key);
		saveEditor.commit();
	}

	public String getString(String key) {
		return saveInfo.getString(key, "");
	}

	public void setString(String key, String value) {
		saveEditor.putString(key, value);
		saveEditor.commit();
	}

	public String getKey(String key) {
		return saveInfo.getString(key, "0");
	}

	public void setKey(String key, String value) {
		saveEditor.putString(key, value);
		saveEditor.commit();
	}

	/**
	 * 获取是否显示帮助屏
	 * 
	 * @return
	 */
	public Boolean getIsShowHelp() {
		return saveInfo.getBoolean(IS_SHOW_HELP, true);
	}

	/**
	 * 保存是否显示帮助屏
	 * 
	 * @return
	 */
	public void setIsShowHelp(Boolean isShowHelp) {
		saveEditor.putBoolean(IS_SHOW_HELP, isShowHelp);
		saveEditor.commit();
	}

	/**
	 * 获取是否第一次登录
	 * 
	 * @return
	 */
	public Boolean getIsFirstLogin() {
		return saveInfo.getBoolean(IS_FIRST_LOGIN, true);
	}

	/**
	 * 保存是否第一次登录
	 * 
	 * @return
	 */
	public void setIsFirstLogin(Boolean isFirstLogin) {
		saveEditor.putBoolean(IS_FIRST_LOGIN, isFirstLogin);
		saveEditor.commit();
	}

	/**
	 * 获取用户名跟密码的MD5加密
	 * 
	 * @return
	 */
	public String getPassword() {
		return saveInfo.getString(LOGIN_PASSWORD, "");
	}

	/**
	 * 保存用户名跟密码的MD5加密
	 * 
	 * @return
	 */
	public void setPassword(String pwd) {
		saveEditor.putString(LOGIN_PASSWORD, pwd);
		saveEditor.commit();
	}

	/**
	 * 获取补报内容
	 * 
	 * @return
	 */
	public String getReplenish() {
		return saveInfo.getString(REPLENISH, "");
	}

	/**
	 * 保存用户名跟密码的MD5加密
	 * 
	 * @return
	 */
	public void setReplenish(String replenish) {
		saveEditor.putString(REPLENISH, replenish);
		saveEditor.commit();
	}

	/**
	 * 获取公司ID
	 * 
	 * @return
	 */
	public int getCompanyId() {
		return saveInfo.getInt(COMPANY_ID, 0);
	}

	/**
	 * 保存公司ID
	 * 
	 * @return
	 */
	public void setCompanyId(int companyId) {
		saveEditor.putInt(COMPANY_ID, companyId);
		saveEditor.commit();
	}

	/**Id
	 * 获取楼ID
	 *
	 * @return
	 */
	public int getBuildingId() {
		return saveInfo.getInt(BUILDDING_ID, 0);
	}

	/**
	 * 保存楼ID
	 *
	 * @return
	 */
	public void setBuildingId(int buildingid) {
		saveEditor.putInt(BUILDDING_ID, buildingid);
		saveEditor.commit();
	}

	/**Id
	 * 获取层ID
	 *
	 * @return
	 */
	public int getFloorId() {
		return saveInfo.getInt(FLOOR_ID, 0);
	}

	/**
	 * 保存层ID
	 *
	 * @return
	 */
	public void setFloorId(int floorid) {
		saveEditor.putInt(FLOOR_ID, floorid);
		saveEditor.commit();
	}


	/**
	 * 获取公司名称
	 * 
	 * @return
	 */
	public String getCompanyName() {
		return saveInfo.getString(COMPANY_NAME, "");
	}

	/**
	 * 保存公司名称
	 * 
	 * @return
	 */
	public void setCompanyName(String companyName) {
		saveEditor.putString(COMPANY_NAME, companyName);
		saveEditor.commit();
	}

	/**
	 * 获取当前用户ID
	 * 
	 * @return
	 */
	public int getUserId() {
		return saveInfo.getInt(USER_ID, 0);
	}

	/**
	 * 保存当前用户ID
	 * 
	 * @param userId
	 */
	public void setUserId(int userId) {
		saveEditor.putInt(USER_ID, userId);
		saveEditor.commit();
	}

	/**
	 * 获取homeActivity当前用户名字
	 *
	 */
	public String getUserName() {
		return saveInfo.getString(USERNAME, "");
	}

	/**
	 * 获得头像地址
	*/
	public String getHeadImage(){
		return saveInfo.getString(HEAD_IMG,"");
	}

	public void setHeadImage(String url){
		saveEditor.putString(HEAD_IMG, url);
		saveEditor.commit();
	}

	/**
	 * 设置昵称
	 */
	public String getNickName(){
		return saveInfo.getString(NICK_NAME,"");
	}

	public void setNickName(String nickName){
		saveEditor.putString(NICK_NAME, nickName);
		saveEditor.commit();
	}

	/**
	 * 设置个性签名
	 */
	public String getSignature(){
		return saveInfo.getString(SIGNATURE,"");
	}

	public void setSignature(String signature){
		saveEditor.putString(SIGNATURE, signature);
		saveEditor.commit();
	}

	/**
	 * 设置部门
	 */
	public String getOrgName(){
		return saveInfo.getString(ORG_NAME,"");
	}

	public void setOrgName(String orgName ){
		saveEditor.putString(ORG_NAME, orgName );
		saveEditor.commit();
	}


	public void setRoleId(int userId) {
		saveEditor.putInt(ROLE_ID, userId);
		saveEditor.commit();
	}
	
	public int getRoleId() {
		return saveInfo.getInt(ROLE_ID, 0);
	}
	public void setOrgId(int userId) {
		saveEditor.putInt(ORG_ID, userId);
		saveEditor.commit();
	}
	
	public int getOrgId() {
		return saveInfo.getInt(ORG_ID, 0);
	}

	/**
	 * 设置homeActivity当前用户名字
	 * 
	 * @param
	 */
	public void setUserName(String userName) {
		saveEditor.putString(USERNAME, userName);
		saveEditor.commit();
	}

	public String getUserRoleName() {
		return saveInfo.getString(ROLENAME, "");
	}

	public void setUserRoleName(String useRolerName) {
		saveEditor.putString(ROLENAME, useRolerName);
		saveEditor.commit();
	}

	/**
	 * 获取是否已经初始化过
	 * 
	 * @return
	 */
	public Boolean getIsInit() {
		return saveInfo.getBoolean(IS_INIT, false);
	}

	/**
	 * 保存是否已经初始化过
	 * 
	 * @return
	 */
	public void setIsInit(Boolean isInit) {
		saveEditor.putBoolean(IS_INIT, isInit);
		saveEditor.commit();
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 */
	public String getLon() {
		return saveInfo.getString(LON, "");
	}

	/**
	 * 保存经度
	 * 
	 * @return
	 */
	public void setLon(String lon) {
		saveEditor.putString(LON, lon);
		saveEditor.commit();
	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 */
	public String getLat() {
		return saveInfo.getString(LAT, "");
	}

	/**
	 * 保存纬度
	 * 
	 * @return
	 */
	public void setLat(String lat) {
		saveEditor.putString(LAT, lat);
		saveEditor.commit();
	}

	/**
	 * 获取定位方式
	 * 
	 * @return
	 */
	public String getLocationType() {
		return saveInfo.getString(LOCATION_TYPE, "");
	}

	/**
	 * 保存定位方式
	 * 
	 * @return
	 */
	public void setLocationType(String locationType) {
		saveEditor.putString(LOCATION_TYPE, locationType);
		saveEditor.commit();
	}

	/**
	 * 获取定位时间
	 * 
	 * @return
	 */
	public String getLocationTime() {
		return saveInfo.getString(LOCATION_SERVER_LAST_TIME, null);
	}

	/**
	 * 保存定位时间
	 * 
	 * @return
	 */
	public void setLocationTime(String time) {
		saveEditor.putString(LOCATION_SERVER_LAST_TIME, time);
		saveEditor.commit();
	}

	/**
	 * 获取定位时手机时间
	 * 
	 * @return
	 */
	public String getLocationPhoneTime() {
		return saveInfo.getString(LOCATION_PHONE_LAST_TIME, null);
	}

	/**
	 * 保存定位时手机时间
	 * 
	 * @return
	 */
	public void setLocationPhoneTime(String time) {
		saveEditor.putString(LOCATION_PHONE_LAST_TIME, time);
		saveEditor.commit();
	}

	/**
	 * 保存定位位置信息
	 * 
	 * @return
	 */
	public void setLocationContent(String locationContent) {
		saveEditor.putString(LOCATION_CONTENT, locationContent);
		saveEditor.commit();
	}

	/**
	 * 获取定位位置信息
	 * 
	 * @return
	 */
	public String getLocationContent() {
		return saveInfo.getString(LOCATION_CONTENT, "");
	}

	/**
	 * 保存模糊查询内容
	 * 
	 * @return
	 */
	public void setSearchContent(String content) {
		saveEditor.putString(SEARCH_CONTENT, content);
		saveEditor.commit();
	}

	/**
	 * 获取模糊查询内容
	 * 
	 * @return
	 */
	public String getSearchContent() {
		return saveInfo.getString(SEARCH_CONTENT, "");
	}

	/**
	 * 保存是否读过法律条文
	 * 
	 * @param isReadedLaw
	 */
	public void saveIsReadedLaw(boolean isReadedLaw) {
		saveEditor.putBoolean(IS_READLAW, isReadedLaw);
		saveEditor.commit();
	}

	/**
	 * 获取是否读过法律条文
	 */
	public boolean getIsReadedLaw() {
		return saveInfo.getBoolean(IS_READLAW, false);
	}

	/**
	 * 保存有效开始日期
	 * 
	 * @param
	 */
	public void setUsefulStartDate(String startData) {
		saveEditor.putString(AVAIL_START_DATE, startData);
		saveEditor.commit();
	}

	/**
	 * 获取是有效开始日期
	 */
	public String getUsefulStartDate() {
		return saveInfo.getString(AVAIL_START_DATE, "");
	}

	/**
	 * 保存有效结束日期
	 * 
	 * @param
	 */
	public void setUsefulEndDate(String EndDate) {
		saveEditor.putString(AVAIL_END_DATE, EndDate);
		saveEditor.commit();
	}

	/**
	 * 获取是有效结束日期
	 */
	public String getUsefulEndDate() {
		return saveInfo.getString(AVAIL_END_DATE, "");
	}

	/**
	 * 保存下载地址
	 * 
	 * @param
	 */
	public void saveDownUrl(String url) {
		saveEditor.putString(DOWNURL, url);
		saveEditor.commit();
	}

	/**
	 * 获取下载地址
	 */
	public String getDownUrl() {
		return saveInfo.getString(DOWNURL, "");
	}

	/**
	 * 保存新版本号
	 * 
	 * @param
	 */
	public void saveNewVersion(String newVersion) {
		saveEditor.putString(VERSION_CODE, newVersion);
		saveEditor.commit();
	}

	/**
	 * 获取新版本号
	 */
	public String getNewVersion() {
		return saveInfo.getString(VERSION_CODE, "");
	}

	/**
	 * 保存新版本号
	 * 
	 * @param
	 */
	public void saveMD5Code(String md5Code) {
		saveEditor.putString(MD5_CODE, md5Code);
		saveEditor.commit();
	}

	/**
	 * 获取新版本号
	 */
	public String getMD5Code() {
		return saveInfo.getString(MD5_CODE, "");
	}

	public void setScreenWidth(int width) {
		saveEditor.putInt(SCREEN_WIDTH, width);
		saveEditor.commit();
	}

	public int getScreenWidth() {
		return saveInfo.getInt(SCREEN_WIDTH, 240);
	}

	public void setScreenHeight(int height) {
		saveEditor.putInt(SCREEN_HEIGHT, height);
		saveEditor.commit();
	}

	public int getScreenHeight() {
		return saveInfo.getInt(SCREEN_HEIGHT, 320);
	}

	/**
	 * 保存本地版本
	 * 
	 * @param locationVersion
	 */
	public void setLocationVersion(String locationVersion) {
		saveEditor.putString(LOCATION_VERSION_CODE, locationVersion);
		saveEditor.commit();
	}

	/**
	 * 获取本地版本
	 */
	public String getLocationVersion() {
		return saveInfo.getString(LOCATION_VERSION_CODE, "");
	}

	public void setHelpName(String name) {
		saveEditor.putString(HELP_NAME, name);
		saveEditor.commit();
	}

	public String getHelpName() {
		return saveInfo.getString(HELP_NAME, "");
	}

	public void setHelpTel(String tel) {
		saveEditor.putString(HELP_TEL, tel);
		saveEditor.commit();
	}

	public String getHelpTel() {
		return saveInfo.getString(HELP_TEL, "");
	}

	// 设置上班时间(yyyy-MM-dd HH:mm:ss)
	public void setStartWorkTime(String date) {
		saveEditor.putString(START_WORK_TIME, date);
		saveEditor.commit();
	}

	// 获取上班时间(yyyy-MM-dd HH:mm:ss)
	public String getStartWorkTime() {
		return saveInfo.getString(START_WORK_TIME, null);
	}

	// 设置上班加班时间(yyyy-MM-dd HH:mm:ss)
	public void setNewAttendanceStartOver(String date) {
		saveEditor.putString(NEW_ATTENDANCE_START_OVER, date);
		saveEditor.commit();
	}

	// 获取上班加班时间(yyyy-MM-dd HH:mm:ss)
	public String getNewAttendanceStartOver() {
		return saveInfo.getString(NEW_ATTENDANCE_START_OVER, null);
	}

	// 设置下班时间(yyyy-MM-dd HH:mm:ss)
	public void setStopWorkTime(String date) {
		saveEditor.putString(STOP_WORK_TIME, date);
		saveEditor.commit();
	}

	// 获取下班时间(yyyy-MM-dd HH:mm:ss)
	public String getStopWorkTime() {
		return saveInfo.getString(STOP_WORK_TIME, null);
	}

	// 设置上班加班时间(yyyy-MM-dd HH:mm:ss)
	public void setNewAttendanceStart(String date) {
		saveEditor.putString(NEW_ATTENDANCE_START, date);
		saveEditor.commit();
	}

	// 获取上班加班时间(yyyy-MM-dd HH:mm:ss)
	public String getNewAttendanceStart() {
		return saveInfo.getString(NEW_ATTENDANCE_START, "");
	}

	// 设置下班时间(yyyy-MM-dd HH:mm:ss)
	public void setNewAttendanceEnd(String date) {
		saveEditor.putString(NEW_ATTENDANCE_END, date);
		saveEditor.commit();
	}

	// 获取下班时间(yyyy-MM-dd HH:mm:ss)
	public String getNewAttendanceEnd() {
		return saveInfo.getString(NEW_ATTENDANCE_END, "");
	}

	/**
	 * 新考勤上班打卡
	 * 
	 * @return
	 */
	public boolean getNewAttendanceStartDoCard() {
		return saveInfo.getBoolean(NEW_ATTENDANCE_START_DO_CARD, false);
	}

	public void setNewAttendanceStartDoCard(boolean flag) {
		saveEditor.putBoolean(NEW_ATTENDANCE_START_DO_CARD, flag);
		saveEditor.commit();
	}

	/**
	 * 新考勤上班打卡
	 * 
	 * @return
	 */
	public boolean getNewAttendanceStartDoCardOver() {
		return saveInfo.getBoolean(NEW_ATTENDANCE_START_DO_CARD_OVER, false);
	}

	public void setNewAttendanceStartDoCardOver(boolean flag) {
		saveEditor.putBoolean(NEW_ATTENDANCE_START_DO_CARD_OVER, flag);
		saveEditor.commit();
	}

	/**
	 * 新考勤下班打卡
	 * 
	 * @return
	 */
	public boolean getNewAttendanceEndDoCardOver() {
		return saveInfo.getBoolean(NEW_ATTENDANCE_END_DO_CARD_OVER, false);
	}

	public void setNewAttendanceEndDoCardOver(boolean flag) {
		saveEditor.putBoolean(NEW_ATTENDANCE_END_DO_CARD_OVER, flag);
		saveEditor.commit();
	}

	/**
	 * 新考勤下班打卡
	 * 
	 * @return
	 */
	public boolean getNewAttendanceEndDoCard() {
		return saveInfo.getBoolean(NEW_ATTENDANCE_END_DO_CARD, false);
	}

	public void setNewAttendanceEndDoCard(boolean flag) {
		saveEditor.putBoolean(NEW_ATTENDANCE_END_DO_CARD, flag);
		saveEditor.commit();
	}

	// 设置下班加班时间(yyyy-MM-dd HH:mm:ss)
	public void setNewAttendanceEndOver(String date) {
		saveEditor.putString(NEW_ATTENDANCE_END_OVER, date);
		saveEditor.commit();
	}

	// 获取下班加班时间(yyyy-MM-dd HH:mm:ss)
	public String getNewAttendanceEndOver() {
		return saveInfo.getString(NEW_ATTENDANCE_END_OVER, null);
	}

	public void setNewAttendanceWorkDay(String str) {
		saveEditor.putString(NEW_ATTENDANCE_WORK_DAY, str);
		saveEditor.commit();
	}

	public String getNewAttendanceWorkDay() {
		return saveInfo.getString(NEW_ATTENDANCE_WORK_DAY, "");
	}

	public void setNewAttendanceWorkTime(String str) {
		saveEditor.putString(NEW_ATTENDANCE_WORK_TIME, str);
		saveEditor.commit();
	}

	public String getNewAttendanceWorkTime() {
		return saveInfo.getString(NEW_ATTENDANCE_WORK_TIME, "");
	}

	public void setNewAttendanceOverName(String str) {
		saveEditor.putString(NEW_ATTENDANCE_OVER_NAME, str);
		saveEditor.commit();
	}

	public String getNewAttendanceOverName() {
		return saveInfo.getString(NEW_ATTENDANCE_OVER_NAME, null);
	}

	public void setNewAttendanceResetType(String type){
		saveEditor.putString(NEW_ATTENDANCE_RESET_TYPE, type);
		saveEditor.commit();
	}
	public String getNewAttendanceResetType(){
		return saveInfo.getString(NEW_ATTENDANCE_RESET_TYPE, "1");
	}
	
	// 存储用户名
	public void setUserLoginName(String name) {
		saveEditor.putString("setUserLoginName", name);
		saveEditor.commit();
	}

	// 获取登陆用户名
	public String getUserLoginName() {
		return saveInfo.getString("setUserLoginName", null);
	}

	// //存储用户名
	// public void setUserLoginPsw(String psw) {
	// saveEditor.putString("setUserLoginPsw", psw);
	// saveEditor.commit();
	// }

	// 获取登陆用户名
	public String getUserLoginPsw() {
		return saveInfo.getString("setUserLoginPsw", null);
	}

	// 存储apk大小
	public void saveApkSize(String size) {
		saveEditor.putString(APKSIZE, size);
		saveEditor.commit();
	}

	// 获取apk大小
	public String getApkSize() {
		return saveInfo.getString(APKSIZE, "0");
	}

	// 存储下载apk信息
	public void saveDownApkInfo(int info) {
		saveEditor.putInt(DOWNAPKINFO, info);
		saveEditor.commit();
	}

	// 获取下载apk信息
	public int getDownApkInfo() {
		return saveInfo.getInt(DOWNAPKINFO, 0);
	}

	// 存储下载apk信息
	public void saveDownApkState(boolean state) {
		saveEditor.putBoolean("downState", state);
		saveEditor.commit();
	}

	// 获取下载apk信息
	public boolean getDownApkState() {
		return saveInfo.getBoolean("downState", false);
	}

	// 存储下载APK是否成功
	public void saveIsDownApkSuccess(boolean isSuccess) {
		saveEditor.putBoolean("downSuccess", isSuccess);
		saveEditor.commit();
	}

	// 获取下载APK是否成功
	public boolean getIsDownApkSuccess() {
		return saveInfo.getBoolean("downSuccess", false);
	}

	// 存储考勤查看用户权限
	public void saveNewAttendAuth(String auth) {
		saveEditor.putString("NEW_ATTEND_AUTH", auth);
		saveEditor.commit();
	}

	// 存储考勤查看用户权限
	public String getNewAttendAuth() {
		return saveInfo.getString("NEW_ATTEND_AUTH", "");
	}
	// 存储工作计划查看用户权限
	public void saveWorkPlanAuth(String auth) {
		saveEditor.putString("WORK_PLAN_AUTH", auth);
		saveEditor.commit();
	}
	
	// 存储工作计划查看用户权限
	public String getWorkPlanAuth() {
		return saveInfo.getString("WORK_PLAN_AUTH", "");
	}

	// 存储考勤查看用户权限
	public void saveAttendAuth(String auth) {
		saveEditor.putString("ATTEND_AUTH", auth);
		saveEditor.commit();
	}

	// 存储考勤查看用户权限
	public String getAttendAuth() {
		return saveInfo.getString("ATTEND_AUTH", "");
	}

	public void saveSubscriberId(String subscriberId) {
		saveEditor.putString(SUBSCRIBER_ID, subscriberId);
		saveEditor.commit();
	}

	public String getSubscriberId() {
		return saveInfo.getString(SUBSCRIBER_ID, "");
	}
	
	/**
	 * 鉴权使用的随机码
	 * @param code
	 */
	public void saveRandomCode(String code){
		saveEditor.putString(RANDOM_CODE, code);
		saveEditor.commit();
	}
	
	public String getRandomCode(){
		return saveInfo.getString(RANDOM_CODE, "");
	}

	public void savePhoneLogoPath(String phone_logo_path) {
		JLog.d("wangchao", "phone_logo_path:" + phone_logo_path);
		saveEditor.putString(PHONE_LOGO_PATH, phone_logo_path);
		saveEditor.commit();
	}

	public String getPhoneLogoPath() {
		return saveInfo.getString(PHONE_LOGO_PATH, "");
	}

	public void savePhoneName1(String phone_name1) {
		JLog.d("wangchao", "phone_name1:" + phone_name1);
		saveEditor.putString(PHONE_NAME1, phone_name1);
		saveEditor.commit();
	}

	public String getPhoneName1() {
		return saveInfo.getString(PHONE_NAME1, "");
	}

	public void savePhoneName2(String phone_name2) {
		JLog.d("wangchao", "phone_name2:" + phone_name2);
		saveEditor.putString(PHONE_NAME2, phone_name2);
		saveEditor.commit();
	}

	public String getPhoneName2() {
		return saveInfo.getString(PHONE_NAME2, "");
	}

	public void savePhoneName3(String phone_name3) {
		JLog.d("wangchao", "phone_name3:" + phone_name3);
		saveEditor.putString(PHONE_NAME3, phone_name3);
		saveEditor.commit();
	}

	public String getPhoneName3() {
		return saveInfo.getString(PHONE_NAME3, "");
	}

	public void savePhoneName1Size(String phone_name1_size) {
		JLog.d("wangchao", "phone_name1_size:" + phone_name1_size);
		saveEditor.putString(PHONE_NAME1_SIZE, phone_name1_size);
		saveEditor.commit();
	}

	public String getPhoneName1Size() {
		return saveInfo.getString(PHONE_NAME1_SIZE, "");
	}

	public void savePhoneName2Size(String phone_name2_size) {
		JLog.d("wangchao", "phone_name2_size:" + phone_name2_size);
		saveEditor.putString(PHONE_NAME2_SIZE, phone_name2_size);
		saveEditor.commit();
	}

	public String getPhoneName2Size() {
		return saveInfo.getString(PHONE_NAME2_SIZE, "");
	}

	public void savePhoneName3Size(String phone_name3_size) {
		JLog.d("wangchao", "phone_name3_size:" + phone_name3_size);
		saveEditor.putString(PHONE_NAME3_SIZE, phone_name3_size);
		saveEditor.commit();
	}

	public String getPhoneName3Size() {
		return saveInfo.getString(PHONE_NAME2_SIZE, "");
	}

	public void savePhoneName1AlignType(String phone_name1_align_type) {
		JLog.d("wangchao", "phone_name1_align_type:" + phone_name1_align_type);
		saveEditor.putString(PHONE_NAME1_ALIGN_TYPE, phone_name1_align_type);
		saveEditor.commit();
	}

	public String getPhoneName1AlignTAype() {
		return saveInfo.getString(PHONE_NAME1_ALIGN_TYPE, "");
	}

	public void savePhoneName2AlignType(String phone_name2_align_type) {
		JLog.d("wangchao", "phone_name2_align_type:" + phone_name2_align_type);
		saveEditor.putString(PHONE_NAME2_ALIGN_TYPE, phone_name2_align_type);
		saveEditor.commit();
	}

	public String getPhoneName2AlignTAype() {
		return saveInfo.getString(PHONE_NAME2_ALIGN_TYPE, "");
	}

	public void savePhoneName3AlignType(String phone_name3_align_type) {
		JLog.d("wangchao", "phone_name3_align_type:" + phone_name3_align_type);
		saveEditor.putString(PHONE_NAME3_ALIGN_TYPE, phone_name3_align_type);
		saveEditor.commit();
	}

	public String getPhoneName3AlignTAype() {
		return saveInfo.getString(PHONE_NAME3_ALIGN_TYPE, "");
	}

	/**
	 * 获取录音时间
	 * 
	 * @return
	 */
	public String getVoiceTime() {
		return saveInfo.getString(VOICE_TIME, "1");
	}

	/**
	 * 保存录音时间
	 * 
	 * @return 公时间
	 */
	public void setVoiceTime(String time) {
		saveEditor.putString(VOICE_TIME, time);
		saveEditor.commit();
	}

	public int getLocationValidAcc() {
		return saveInfo.getInt(LOCATION_VALID_ACC, 3000);
	}

	public void setLocationValidAcc(int acc) {
		saveEditor.putInt(LOCATION_VALID_ACC, acc);
		saveEditor.commit();
	}

	public void setCompareRuleIds(String key, String compare_rule_ids) {
		saveEditor.putString(key, compare_rule_ids);
		saveEditor.commit();
	}

	/**
	 * 串码查询数据id 服务端
	 * 
	 * @return
	 */
	public String getCompareRuleIds(String key) {
		return saveInfo.getString(key, "");
	}

	public void setCompareContent(String key, String compareContent) {
		saveEditor.putString(key, compareContent);
		saveEditor.commit();
	}

	/**
	 * 串码比对结果
	 * 
	 * @return
	 */
	public String getCompareContent(String key) {
		return saveInfo.getString(key, "");
	}
	
	public void  setAttendanceMustDo(String value){
		saveEditor.putString(ATTENDANCE_MUST_DO, value);
		saveEditor.commit();
	}
	
	public String getAttendanceMustDo(){
		return saveInfo.getString(ATTENDANCE_MUST_DO, null);
	}
	
	public void  setAttendanceMustDoNew(String value){
		saveEditor.putString(ATTENDANCE_MUST_DO_NEW, value);
		saveEditor.commit();
	}
	
	public String getAttendanceMustDoNew(){
		return saveInfo.getString(ATTENDANCE_MUST_DO_NEW, null);
	}

	/**
	 * 固定模块动态名字
	 * 
	 * 1:拜访,2:旧考勤,3:考勤,4:公告,5:帮助,6:圈子
	 * 
	 * @return
	 */
	public HashMap<Integer, String> getFixedName() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		String names = saveInfo.getString(PHONE_FIXED_MODULE_NAME, null);
		if (!TextUtils.isEmpty(names)) {
			String[] modNames = names.split(",");
			for (String nameStr : modNames) {
				if (TextUtils.isEmpty(nameStr))
					continue;
				String[] arr = nameStr.split(":");
				if(arr.length!=2 || arr[0]==null || arr[1]==null)
					continue;
				map.put(Integer.valueOf(arr[0].trim()), arr[1].trim());
			}
		}
		return map;
	}

	public void setFixedName(String names) {
		saveEditor.putString(PHONE_FIXED_MODULE_NAME, names);
		saveEditor.commit();
	}
	
	public void setIsTipUser(boolean flag){
		saveEditor.putBoolean(IS_TIP_USER, flag);
		saveEditor.commit();
	}
	
	public boolean isTipUser(){
		return saveInfo.getBoolean(IS_TIP_USER, false);
	}
	
	public void setOverAttendWait(int wait){
		saveEditor.putInt(OVER_ATTEND_WAIT, wait);
		saveEditor.commit();
	}
	
	public int getOverAttendWait(){
		return saveInfo.getInt(OVER_ATTEND_WAIT, 1);
	}
	
	public void setAttendWait(int wait){
		saveEditor.putInt(ATTEND_WAIT, wait);
		saveEditor.commit();
	}
	
	public int getAttendWait(){
		return saveInfo.getInt(ATTEND_WAIT, 1);
	}
	
	public void setCacheAttendanceData(String data){
		saveEditor.putString(CACHE_ATTENDANCE_DATA, data);
		saveEditor.commit();
	}
	
	public String getCacheAttendanceData(){
		return saveInfo.getString(CACHE_ATTENDANCE_DATA, null);
	}
	
	public void setToDoDate(String date){
		saveEditor.putString(TO_DO_DATE, date);
		saveEditor.commit();
	}
	
	public String getToDoDate(){
		return saveInfo.getString(TO_DO_DATE, "");
	}
	
	public void setLastRunTime(String date){
		saveEditor.putString(LAST_RUN_TIME, date);
		saveEditor.commit();
	}
	
	public String getLastRunTime(){
		return saveInfo.getString(LAST_RUN_TIME, "");
	}
	
	public void setGrirmsUserIsCodeOne(String date){
		saveEditor.putString(GRIRMS_USER_IS_CODE_ONE, date);
		saveEditor.commit();
	}
	
	public String getGrirmsUserIsCodeOne(){
		return saveInfo.getString(GRIRMS_USER_IS_CODE_ONE, "1");
	}
	
	public void setNewAttendTime(String time){
		saveEditor.putString(NEW_ATTEND_TIME, time);
		saveEditor.commit();
	}
	public String getNewAttendTime(){
		return saveInfo.getString(NEW_ATTEND_TIME, "");
	}
	
	public void setNewAttendOverTime(String time){
		saveEditor.putString(NEW_ATTEND_OVER_TIME, time);
		saveEditor.commit();
	}
	public String getNewAttendOverTime(){
		return saveInfo.getString(NEW_ATTEND_OVER_TIME, DateUtil.getCurDateTime());
	}
//////////增加统一新方法//////Aug22
public void putString(String key,String value){
	saveEditor.putString(key, value);
	saveEditor.commit();
}

public void settInt(String key,int value){
	 saveEditor.putInt(key, value);
	 saveEditor.commit();
	 
}
	public void putInt(String key,int value){
		saveEditor.putInt(key, value);
		saveEditor.commit();
	}
	public int getInt(String key){
		return saveInfo.getInt(key, 0);
	}
	public void setWetChatTags(String tags){
		saveEditor.putString(WECHAR_TAGS, tags);
		saveEditor.commit();
	}
	public String gettString(String key){
		return saveInfo.getString(key, "");
	}
	public int gettInt(String key){
		return saveInfo.getInt(key, 0);
	}
	public String getWetChatTags(){
		return saveInfo.getString(WECHAR_TAGS, "");
	}
	
	public void setTableSelect(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getTableSelect(String key){
		return saveInfo.getString(key, "");
	}
	
	public void setLocLevel(String value){
		saveEditor.putString(LOC_LEVEL, value);
		saveEditor.commit();
	}
	
	public String getLocLevel(String key){
		return saveInfo.getString(LOC_LEVEL, "");
	}
	
	
	public void setFinding(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getFinding(String key){
		return saveInfo.getString(key, DateUtil.getCurDate());
	}
	
	public void setFindingNums(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getFindingNums(String i){
		return saveInfo.getString(i, "0");
	}
	
	public void setIfSave(String key,String value){
		saveEditor.putString(key, value);
		saveEditor.commit();
	}
	
	public String getIfSave(String key){
		return saveInfo.getString(key, PublicUtils.getResourceString(mContext, R.string.utility_string4));
	}
	
	
	public void setXDSB(String values){
		saveEditor.putString(XIN_DIAN_SB, values);
		saveEditor.commit();
	}
	
	public String getXDSB(){
		return saveInfo.getString(XIN_DIAN_SB, "");
	}
	/**
	 * 用于演示版本的考勤上班打卡,存为本地数据,保证每个体验用户每天都可以体验打卡
	 */
	public void setNativeStartCard(boolean flag) {
		saveEditor.putBoolean("nativestart", flag);
		saveEditor.commit();
	}

	public boolean getNativeStartCard() {
		return saveInfo.getBoolean("nativestart", false);
	}

	/**
	 * 用于演示版本的考勤下班打卡,存为本地数据,保证每个体验用户每天都可以体验打卡
	 */
	public void setNativeEndCard(boolean flag) {
		saveEditor.putBoolean("nativeend", flag);
		saveEditor.commit();
	}

	public boolean getNativeEndCard() {
		return saveInfo.getBoolean("nativeend", false);
	}
	// 设置本地上班时间(yyyy-MM-dd HH:mm:ss),当为演示版时使用
	public void setNativeAttendanceStart(String date) {
		saveEditor.putString("nativestarttime", date);
		saveEditor.commit();
	}
	public String getNativeAttendanceStart(){
		
		return saveInfo.getString("nativestarttime", "");
	}

	
	// 设置本地下班时间(yyyy-MM-dd HH:mm:ss),当为演示版时使用
	public void setNativeAttendanceEnd(String date) {
		saveEditor.putString("nativeendtime", date);
		saveEditor.commit();
	}
	public String getNativeAttendanceEnd(){
		
		return saveInfo.getString("nativeendtime", "");
	}

	public void setContracInfo(String info){
		saveEditor.putString(CONTRACT_INFO,info);
		saveEditor.commit();
	}
	public String getContracInfo(){
		return saveInfo.getString(CONTRACT_INFO,"");
	}
	
}
