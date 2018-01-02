package com.yunhu.yhshxc.activity.synchrodata;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.help.SharedPreferencesHelp;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncParse {
	private Context context;
	private CacheData cacheDate = null;

	public SyncParse(Context mContext) {
		this.context = mContext;
		cacheDate = new CacheData(context);
	}

	/**
	 * 
	 * @param json
	 *            要解析的json数据
	 * @throws Exception
	 *             json数据格式不正确抛异常
	 */
	public void paserJson(String json) throws Exception {
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has(cacheDate.COMPANY_ID)) {// 解析公司ID
			SharedPreferencesUtil.getInstance(context).setCompanyId(
					jsonObject.getInt(cacheDate.COMPANY_ID));
		}
		if (jsonObject.has(cacheDate.PHONE_FIXED_MODULE_NAME)) {
			SharedPreferencesUtil.getInstance(context).setFixedName(
					jsonObject.getString(cacheDate.PHONE_FIXED_MODULE_NAME));
		}
		if (jsonObject.has(cacheDate.VOICE_TIME)) {
			SharedPreferencesUtil.getInstance(context).setVoiceTime(
					jsonObject.getInt(cacheDate.VOICE_TIME) + "");
		}
		if (jsonObject.has(cacheDate.LOGIN_ID)) {// 解析login ID
			SharedPreferencesHelp.getInstance(context).setLoginId(
					jsonObject.getString(cacheDate.LOGIN_ID));
		}
		if (jsonObject.has(cacheDate.USER_ID)) {// 解析用户ID
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setUserId(jsonObject.getInt(cacheDate.USER_ID));
		}
		if (jsonObject.has(cacheDate.USER_NAME)) {// 解析用户Name
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setUserName(jsonObject.getString(cacheDate.USER_NAME));
		}
		if (jsonObject.has(cacheDate.USER_ROLE_NAME)) {// 解析用户角色名称
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setUserRoleName(
							jsonObject.getString(cacheDate.USER_ROLE_NAME));
		}
		if (isValid(jsonObject, cacheDate.LOC_LEVEL)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setLocLevel(jsonObject.getString(cacheDate.LOC_LEVEL));
		}
		if (jsonObject.has(cacheDate.HELP_NAME)) {// 解析客服名称
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setHelpName(jsonObject.getString(cacheDate.HELP_NAME));
		}
		if (jsonObject.has(cacheDate.PAI_FLG)) {
			SharedPrefsAttendanceUtil.getInstance(context.getApplicationContext()).setPaiFlg(jsonObject.getString(cacheDate.PAI_FLG));
		}
		if (jsonObject.has(cacheDate.HELP_TEL)) {// 解析客服电话
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setHelpTel(jsonObject.getString(cacheDate.HELP_TEL));
		}
		if (jsonObject.has(cacheDate.STORE_INFO_ID)) {
			SharedPreferencesUtil2
					.getInstance(context.getApplicationContext())
					.saveStoreInfoId(jsonObject.getInt(cacheDate.STORE_INFO_ID));
		}
		if (jsonObject.has(cacheDate.ATTEND_AUTH)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.saveAttendAuth(jsonObject.getString(cacheDate.ATTEND_AUTH));
		}
		if (jsonObject.has(cacheDate.ATTEND_FUNC)) {
			SharedPrefsAttendanceUtil.getInstance(context.getApplicationContext()).setAttendFunc(jsonObject.getString(cacheDate.ATTEND_FUNC));
		}
		
		if (jsonObject.has(cacheDate.COMPANY_NAME)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setCompanyName(
							jsonObject.getString(cacheDate.COMPANY_NAME));
		}

		if (jsonObject.has(cacheDate.NEW_ATTENDAUTH)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.saveNewAttendAuth(
							jsonObject.getString(cacheDate.NEW_ATTENDAUTH));
		}

		if (jsonObject.has(cacheDate.PHONE_LOGO_PATH)) {// 解析企业logo下载地址
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneLogoPath(
							jsonObject.getString(cacheDate.PHONE_LOGO_PATH));
		}

		// 解析企业名称信息
		if (jsonObject.has(cacheDate.PHONE_NAME1)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName1(jsonObject.getString(cacheDate.PHONE_NAME1));
		}
		// 解析企业名称文字的大小
		if (jsonObject.has(cacheDate.PHONE_NAME1_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName1Size(
							jsonObject.getString(cacheDate.PHONE_NAME1_SIZE));
		}
		// 解析企业名称文字的位置
		if (jsonObject.has(cacheDate.PHONE_NAME1_ALIGN_TYPE)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName1AlignType(
							jsonObject
									.getString(cacheDate.PHONE_NAME1_ALIGN_TYPE));
		}
		if (jsonObject.has(cacheDate.PHONE_NAME2)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName2(jsonObject.getString(cacheDate.PHONE_NAME2));
		}

		if (jsonObject.has(cacheDate.PHONE_NAME2_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName2Size(
							jsonObject.getString(cacheDate.PHONE_NAME2_SIZE));
		}

		if (jsonObject.has(cacheDate.PHONE_NAME2_ALIGN_TYPE)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName2AlignType(
							jsonObject
									.getString(cacheDate.PHONE_NAME2_ALIGN_TYPE));
		}
		if (jsonObject.has(cacheDate.PHONE_NAME3)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName3(jsonObject.getString(cacheDate.PHONE_NAME3));
		}

		if (jsonObject.has(cacheDate.PHONE_NAME3_SIZE)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.savePhoneName3Size(
							jsonObject.getString(cacheDate.PHONE_NAME3_SIZE));
		}

		if (jsonObject.has(cacheDate.PHONE_NAME3_ALIGN_TYPE)) {
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.savePhoneName3AlignType(
							jsonObject
									.getString(cacheDate.PHONE_NAME3_ALIGN_TYPE));
		}

		if (cacheDate.isValid(jsonObject, cacheDate.TRACE_FENCE)) {
			SharedPrefsBackstageLocation.getInstance(
					context.getApplicationContext()).saveTraceFence(
					jsonObject.getInt(cacheDate.TRACE_FENCE));
		}
		if (jsonObject.has(cacheDate.LOCATION_VAILD_ACC)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setLocationValidAcc(
							jsonObject.getInt(cacheDate.LOCATION_VAILD_ACC));
		}
		if(jsonObject.has(cacheDate.NEW_STORE_TITLE)){
			SharedPreferencesUtil.getInstance(context.getApplicationContext()).setXDSB(jsonObject.getString(cacheDate.NEW_STORE_TITLE));
		}

		cacheDate.parseISVisit(jsonObject);// 解析访店
		cacheDate.parseISNotify(jsonObject);// 解析公告
		cacheDate.parseIsAttendance(jsonObject);// 主页面是否显示考勤模块
		cacheDate.parseIsNewAttendance(jsonObject);// 解析新考勤模块
		cacheDate.parseISBBS(jsonObject);// 解析论坛
		cacheDate.parseIsHelp(jsonObject); // 主页面是否显示帮助模块
		cacheDate.parseIsToDo(jsonObject);// 主页是否显示待办事项
		cacheDate.parserStyle(jsonObject);// 解析样式
		cacheDate.parserNearbyConf(jsonObject);// 解析就近拜访
		cacheDate.parseISStoreAddMod(jsonObject);
		cacheDate.parserWeiChat(jsonObject);//解析企业微信
		cacheDate.parserIsMailList(jsonObject);//解析通讯录
		cacheDate.parserQuestion(jsonObject);
		cacheDate.parseisLeave(jsonObject);//解析请假
		cacheDate.parseIsCompanyWeb(jsonObject);////解析公司id指定展示的网页(例如皇明的mail)
		cacheDate.parserWorkPlan(jsonObject);//解析工作计划
		cacheDate.parserWorkSum(jsonObject);//解析工作总结
		cacheDate.parserPictures(jsonObject);//解析轮播图
		cacheDate.parserHY(jsonObject);
		
		if (jsonObject.has(cacheDate.ATTEND_MUST_ITEMS)) {
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setAttendanceMustDo(
							jsonObject.getString(cacheDate.ATTEND_MUST_ITEMS));
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setAttendanceMustDoNew(
							jsonObject.getString(cacheDate.ATTEND_MUST_ITEMS));
		}

		if (jsonObject.has(CacheData.AVAIL_START)) {// 解析有效期开始日期
			SharedPreferencesUtil.getInstance(context.getApplicationContext())
					.setUsefulStartDate(
							jsonObject.getString(CacheData.AVAIL_START));
		}
		if (jsonObject.has(CacheData.AVAIL_END)) {// 解析有效期结束日期
			SharedPreferencesUtil
					.getInstance(context.getApplicationContext())
					.setUsefulEndDate(jsonObject.getString(CacheData.AVAIL_END));
		}

		if (jsonObject.has(cacheDate.MENU)) {// 解析menu
			String jsonForMenu = jsonObject.getString(cacheDate.MENU);
			cacheDate.parseMenu(jsonForMenu);
		}

        if (jsonObject.has(cacheDate.MENU_SHIHUA)) {// 解析石化menu
            String jsonForMenu = jsonObject.getString(cacheDate.MENU_SHIHUA);
            cacheDate.parseShihuaMenu(jsonForMenu);
        }


		if (jsonObject.has(cacheDate.MOD)) {// 解析自定义模块
			String jsonForMod = jsonObject.getString(cacheDate.MOD);
			cacheDate.parseMod(jsonForMod);
		}
		if (jsonObject.has(cacheDate.VISIT)) {// 解析访店
			String jsonForVisit = jsonObject.getString(cacheDate.VISIT);
			cacheDate.parseVisit(jsonForVisit);
		}

		if (jsonObject.has(cacheDate.BASE_FUNC)) {// 解析基础控件
			String jsonForBasefunc = jsonObject.getString(cacheDate.BASE_FUNC);
			cacheDate.parseBaseFunc(jsonForBasefunc);
		}
		if (jsonObject.has(cacheDate.VISIT_FUNC)) {// 解析访店控件
			String jsonForVisitFunc = jsonObject
					.getString(cacheDate.VISIT_FUNC);
			cacheDate.parseVisitFunc(jsonForVisitFunc);
		}
		if (jsonObject.has(cacheDate.DICT)) {// 解析字典表
			String jsonForDict = jsonObject.getString(cacheDate.DICT);
			cacheDate.parseBatchDictionary(jsonForDict);
		}
		if (jsonObject.has(cacheDate.NOTIFY)) {// 解析公告
			String jsonForDict = jsonObject.getString(cacheDate.NOTIFY);
			cacheDate.parseNotify(jsonForDict);
		}
		if (jsonObject.has(cacheDate.TASK)) {// 解析任务
			String jsonForDict = jsonObject.getString(cacheDate.TASK);
			cacheDate.parseCustom(jsonForDict);
		}
		if (jsonObject.has(cacheDate.DOUBLE)) {// 解析双向
			String doubleJson = jsonObject.getString(cacheDate.DOUBLE);
			cacheDate.parseDouble(doubleJson, true);
		}
		if (jsonObject.has(cacheDate.LOCATION_TASK)) {// 解析被动定位
			String jsonLocationRule = jsonObject
					.getString(cacheDate.LOCATION_TASK);
			cacheDate.parseLocationRule(jsonLocationRule, false,true);
		}

		if (jsonObject.has("version")) {// 解析最新apk版本的信息
			JSONObject versionObj = jsonObject.getJSONObject("version");
			if (versionObj.has("md5code")) {// 最新apk文件的MD5
				String md5code = versionObj.getString("md5code");
				SharedPreferencesUtil.getInstance(
						context.getApplicationContext()).saveMD5Code(md5code);
			}
			if (versionObj.has("url")) {// 最新apk下载URl
				String url = versionObj.getString("url");
				SharedPreferencesUtil.getInstance(
						context.getApplicationContext()).saveDownUrl(url);
			}
			if (versionObj.has("version")) {// 最新apk版本号
				String version = versionObj.getString("version");
				SharedPreferencesUtil.getInstance(
						context.getApplicationContext())
						.saveNewVersion(version);
			}
			if (versionObj.has("size")) {// 最新APK文件的大小
				String size = versionObj.getString("size");
				SharedPreferencesUtil.getInstance(
						context.getApplicationContext()).saveApkSize(size);
			}
		}

		if (jsonObject.has(cacheDate.REPORT)) {// 解析报表
			String jsonReport = jsonObject.getString(cacheDate.REPORT);
			cacheDate.parseReport(jsonReport, Menu.TYPE_REPORT);
		}

		if (jsonObject.has(cacheDate.REPORT2)) {// 解析新报表
			String jsonReport = jsonObject.getString(cacheDate.REPORT2);
			cacheDate.parseReport(jsonReport, Menu.TYPE_REPORT_NEW);
		}

		if (jsonObject.has(cacheDate.REPORT_WHERE)) {// 解析报表内容
			SharedPreferencesUtil2.getInstance(context.getApplicationContext())
					.saveReportWhere(
							jsonObject.getString(cacheDate.REPORT_WHERE));
		}
		if (jsonObject.has(cacheDate.REPORT_WHERE2)) {// 解析新报表内容
			SharedPreferencesUtil2.getInstance(context.getApplicationContext())
					.saveReportWhere2(
							jsonObject.getString(cacheDate.REPORT_WHERE2));
		}
		if (jsonObject.has(cacheDate.WEB_REPORT)) {
			String jsonWebReport = jsonObject.getString(cacheDate.WEB_REPORT);
			cacheDate.parseWebReport(jsonWebReport);
		}
		if (jsonObject.has(cacheDate.ROLE)) {// 解析角色
			String jsonReport = jsonObject.getString(cacheDate.ROLE);
			cacheDate.parseRole(jsonReport);
		}
		if (jsonObject.has(cacheDate.ROLE)) {// 解析角色
			String jsonReport = jsonObject.getString(cacheDate.ROLE);
			cacheDate.parseRole(jsonReport);
		}
		if (jsonObject.has(cacheDate.PSS)) { // 进销存配置
			String jsonPss = jsonObject.getString(cacheDate.PSS);
			new ParsePSS(context).pssconfig(jsonPss);
		}

		if (jsonObject.has(cacheDate.ATTENDANCE_IN_WORK)) {
			String jsonInWord = jsonObject
					.getString(cacheDate.ATTENDANCE_IN_WORK);
			if (!TextUtils.isEmpty(jsonInWord) && jsonInWord.equals("1")) {
				String date = DateUtil.getCurDateTime();
				SharedPreferencesUtil.getInstance(context).setStartWorkTime(
						date + "do");// 设置上班日期
			}

		}
		if (jsonObject.has(cacheDate.ATTENDANCE_OUT_WORK)) {
			String jsonOutWord = jsonObject
					.getString(cacheDate.ATTENDANCE_OUT_WORK);
			if (!TextUtils.isEmpty(jsonOutWord) && jsonOutWord.equals("1")) {
				String date = DateUtil.getCurDateTime();
				SharedPreferencesUtil.getInstance(context).setStopWorkTime(
						date + "do");// 设置下班日期
			}
		}

		if (jsonObject.has(cacheDate.NEW_ATTENDANCE_IN_WORK)) {
			String jsonInWord = jsonObject
					.getString(cacheDate.NEW_ATTENDANCE_IN_WORK);
			if (!TextUtils.isEmpty(jsonInWord)) {
				SharedPreferencesUtil.getInstance(context)
						.setNewAttendanceStart(jsonInWord);
			}

		}
		if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OUT_WORK)) {
			String jsonOutWord = jsonObject
					.getString(cacheDate.NEW_ATTENDANCE_OUT_WORK);
			if (!TextUtils.isEmpty(jsonOutWord)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceEnd(
						jsonOutWord);
			}
		}

		if (jsonObject.has(cacheDate.NEW_ATTENDANCE_IN_WORK_OVER)) {
			String jsonInWord = jsonObject
					.getString(cacheDate.NEW_ATTENDANCE_IN_WORK_OVER);
			if (!TextUtils.isEmpty(jsonInWord)) {
				SharedPreferencesUtil.getInstance(context)
						.setNewAttendanceStartOver(jsonInWord);
			}

		}
		if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OUT_WORK_OVER)) {
			String jsonOutWord = jsonObject
					.getString(cacheDate.NEW_ATTENDANCE_OUT_WORK_OVER);
			if (!TextUtils.isEmpty(jsonOutWord)) {
				SharedPreferencesUtil.getInstance(context)
						.setNewAttendanceStartOver("");
				SharedPreferencesUtil.getInstance(context)
						.setNewAttendanceEndOver("");
			}
		}

		if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OVERCONF)) {
			String jsonAttendance = jsonObject
					.getString(cacheDate.NEW_ATTENDANCE_OVERCONF);
			if (!TextUtils.isEmpty(jsonAttendance)) {
				cacheDate.parseNewAttendance(jsonAttendance);

			}
		}

		if (jsonObject.has(cacheDate.LOCATION_TIP_TYPE)) {
			int tipType = jsonObject.getInt(cacheDate.LOCATION_TIP_TYPE);
			SharedPrefsBackstageLocation.getInstance(context)
					.saveLocationTipType(tipType);
			if (tipType == 1) {// 有新规则下达的时候要提示
				SharedPreferencesUtil.getInstance(context).setIsTipUser(false);// 设置成fales表示没有提示用户
			}
		}

		if (isValid(jsonObject, cacheDate.OVER_ATTEND_WAIT)) {
			int wait = jsonObject.getInt(cacheDate.OVER_ATTEND_WAIT);
			SharedPreferencesUtil.getInstance(context).setOverAttendWait(wait);
		} else {
			SharedPreferencesUtil.getInstance(context).setOverAttendWait(1);
		}

		if (isValid(jsonObject, cacheDate.ATTEND_WAIT)) {
			int wait = jsonObject.getInt(cacheDate.ATTEND_WAIT);
			SharedPreferencesUtil.getInstance(context).setAttendWait(wait);
		} else {
			SharedPreferencesUtil.getInstance(context).setAttendWait(1);
		}

		if (isValid(jsonObject, cacheDate.TO_DO)) {
			String toDoJson = jsonObject.getString(cacheDate.TO_DO);
			cacheDate.parserToDo(toDoJson);
		}
		
		cacheDate.parseOrder2(jsonObject);
		cacheDate.parseOrder3(jsonObject);
		cacheDate.parseOrder3Send(jsonObject);
		cacheDate.parseCarSales(jsonObject);
//		if (cacheDate.isHasOrder3) {//有订单3的话处理下订单三数据 不能在解析订单3的时候处理，因为里面要用到一些字典表数据可能还没解析到
//			new Order3Util(context).initOrder3ProductCtrl();
//		}
	}

	public boolean isValid(JSONObject jsonObject, String key)
			throws JSONException {
		boolean flag = false;
		if (jsonObject.has(key) && !jsonObject.isNull(key)) {
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}
}
