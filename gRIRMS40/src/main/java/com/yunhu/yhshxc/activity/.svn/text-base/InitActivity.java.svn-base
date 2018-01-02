package com.yunhu.yhshxc.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.login.LoginActivity;
import com.yunhu.yhshxc.attendance.SharedPrefsAttendanceUtil;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.help.SharedPreferencesHelp;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.http.download.apk.Dao;
import com.yunhu.yhshxc.http.listener.ResponseListener;
import com.yunhu.yhshxc.location.backstage.SharedPrefsBackstageLocation;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.parser.GetOrg;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil2;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.NumberProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import gcg.org.debug.JLog;

/**
 * 1:用于初始化信息的获取和解析 2:用于组织机构的信息的获取和解析
 * 
 * @author jishen
 * @version 2012-05-21
 */

public class InitActivity extends AbsBaseActivity implements ResponseListener {

	/**
	 * 信息解析进度条
	 */
	private NumberProgressBar progressBar;
	/**
	 * 显示初始化结果的文字标识 包括 "未检测到该用户，请联系管理员！"，"初始化失败"，"初始化完成"
	 */
	private TextView tv_info;
	/**
	 * 解析信息的工具类
	 */
	private CacheData cacheDate = new CacheData(this);

	private GetOrg getOrg;
	private int orgType = 1;

	/**
	 * 标识是否有解析线程正在运行 true表示有解析线程正在运行 false表示没有解析线程正在运行
	 */
	private boolean isRun = true; // 控件线程
	// 初始化显示的logo
	private ImageView initlogo;
	/**
	 * 网络请求工具类
	 */
	private CoreHttpHelper request = null;
	private TextView tv_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.init);
		tv_name = (TextView) findViewById(R.id.tv_name);
		// 初始化加载进度条
		progressBar = (NumberProgressBar) this.findViewById(R.id.progressBar);
		initlogo = (ImageView) findViewById(R.id.init_logo);
		tv_name.setText(getResources().getString(R.string.app_normalname));

		// 初始化加载信息提示标识
		tv_info = (TextView) this.findViewById(R.id.tv_info);

		request = new CoreHttpHelper(this);
		request.setResponseListener(this);// 设置网络请求监听
		request.performQueryRequest(new URLWrapper(UrlInfo.getUrlInit(this)).getRequestURL());// 发起初始化网络请求
	}

	/**
	 * 网络请求返回监听
	 * 
	 * @param requestCode
	 *            网络请求cod
	 * @param dataObj
	 *            请求返回的结果
	 *            如果返回的json格式不对则说明数据有误会抛出异常
	 * @exception ，提示用户初始化失败
	 */
	@Override
	public void receive(int requestCode, Object dataObj) {
		String result = (String) dataObj;// json格式的返回结果
		try {
			JSONObject jsonObject = new JSONObject(result);
			String resultcode = jsonObject.getString(Constants.RESULT_CODE);
			// "0000"表示返回成功"0001"表示返回失败"0002"表示后台没检测到用户
			if (resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)) {
				JLog.d(TAG, result);
				// 解析返回的数据
				new InitAnsyncTask().execute(result);

			} else if (resultcode.equalsIgnoreCase(Constants.RESULT_CODE_NO_REGISTER)) {
				// 返回"0002"表示没检测到用户"此时提示用户没检测到联系管理员
				tv_info.setText(PublicUtils.getResourceString(InitActivity.this,R.string.un_check_user));
				String projectVersion = this.getResources().getString(R.string.PROJECT_VERSIONS);
				// 4.5是用户自己输入手机号，此时则移除存储手机号的SharedPreferences
				if (projectVersion.equalsIgnoreCase(Constants.APP_VERSION_4_5)) {
					getApplicationContext().getSharedPreferences(PublicUtils.PREFERENCE_NAME, MODE_PRIVATE).edit()
							.remove(PublicUtils.PREFERENCE_NAME_PHONE).commit();
				}
				JLog.d(TAG, result);
			} else {
				tv_info.setText(PublicUtils.getResourceString(InitActivity.this,R.string.init_failure));
			}
		} catch (JSONException e) {
			tv_info.setText(PublicUtils.getResourceString(InitActivity.this,R.string.init_failure));
			e.printStackTrace();
			JLog.d(TAG, "json异常===>" + e.getMessage());
		}
	}

	/**
	 * 1：解析初始化信息线程 2：检测企业LOGO图片，如果没有企业logo图片就联网进行下载
	 */
	private class InitAnsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			clearCache("初始paserJson"); // 清除缓存数据，以便重新初始化
		}

		@Override
		protected Boolean doInBackground(String... params) {
			if (!isRun) {
				JLog.d(TAG, "已停止");
				return false;
			}
			boolean flag = true;// 设置是否有线程正在初始化标识 true表示有线程在运行 false表示没有线程在运行
			String json = params[0];// 获取要解析的json
			try {
				// long l = System.currentTimeMillis();
				// 开始解析
				paserJson(json, flag);
				// 检测企业LOGO图片，如果没有企业logo图片就联网进行下载
				String logoPath = SharedPreferencesUtil.getInstance(InitActivity.this).getPhoneLogoPath();
				if (!TextUtils.isEmpty(logoPath)) {
					new HttpHelper(InitActivity.this).downloadFile(logoPath, Constants.COMPANY_LOGO_PATH);
				}
				// publishProgress(80);
				isContinue(80, flag);
				// JLog.d(TAG, "paserAllJson
				// =>所用时间："+(System.currentTimeMillis()-l));
			} catch (Exception e) {// 如果解析出现异常线程会停止则改变标识状态为false
				isContinue(0, flag);
				flag = false;
				e.printStackTrace();
				JLog.d(TAG, "initAnsyncTask =>" + e.toString());
			}
			// publishProgress(85);
			isContinue(85, flag);
			return flag;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更改进度条和已经加载的百分比的UI
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {// 模块信息加载完以后开始获取组织机构和机构店面信息
				getOrg(orgType);
			} else {
				clearCache(PublicUtils.getResourceString(InitActivity.this,R.string.data_un_complement));// allInfo数据不完整，表示数据整体不完整，需要重新初始化
				tv_info.setText(PublicUtils.getResourceString(InitActivity.this,R.string.init_failure));
				// 设置false表示初始化失败，下载重新登录的时候还要进行初始化
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).setIsInit(false);
			}
		}

		private void isContinue(Integer proress, Boolean result) {
			// 4.1版本以上
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				publishProgress(proress);
			} else {

				final int pro = proress;
				progressBar.post(new Runnable() {

					@Override
					public void run() {
						progressBar.setProgress(pro);
					}
				});
				if (pro == 85) {
					Message message = orgHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
				}
			}
		}

		/**
		 * 
		 * @param json
		 *            要解析的json数据
		 * @throws Exception
		 *             json数据格式不正确抛异常
		 */
		private void paserJson(String json, boolean flag) throws Exception {
			JSONObject jsonObject = new JSONObject(json);
			// if (jsonObject.has(cacheDate.COMPANY_ID)) {//解析公司ID
			// SharedPreferencesUtil.getInstance(InitActivity.this).setCompanyId(jsonObject.getInt(cacheDate.COMPANY_ID));
			// }
			if (jsonObject.has(cacheDate.PHONE_FIXED_MODULE_NAME)) {
				SharedPreferencesUtil.getInstance(InitActivity.this)
						.setFixedName(jsonObject.getString(cacheDate.PHONE_FIXED_MODULE_NAME));
			}
			if (jsonObject.has(cacheDate.VOICE_TIME)) {
				SharedPreferencesUtil.getInstance(InitActivity.this)
						.setVoiceTime(jsonObject.getInt(cacheDate.VOICE_TIME) + "");
			}
			if (jsonObject.has(cacheDate.LOGIN_ID)) {// 解析login ID
				SharedPreferencesHelp.getInstance(InitActivity.this)
						.setLoginId(jsonObject.getString(cacheDate.LOGIN_ID));
			}
			if (jsonObject.has(cacheDate.USER_ID)) {// 解析用户ID
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUserId(jsonObject.getInt(cacheDate.USER_ID));
			}
			if (jsonObject.has(cacheDate.ROLEID)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setRoleId(jsonObject.getInt(cacheDate.ROLEID));
			}
			if (jsonObject.has(cacheDate.ORGID)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setOrgId(jsonObject.getInt(cacheDate.ORGID));
			}
			if (jsonObject.has(cacheDate.USER_NAME)) {// 解析用户Name
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUserName(jsonObject.getString(cacheDate.USER_NAME));
			}
			if (jsonObject.has(cacheDate.USER_ROLE_NAME)) {// 解析用户角色名称
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUserRoleName(jsonObject.getString(cacheDate.USER_ROLE_NAME));
			}
			if (jsonObject.has(cacheDate.USER_ROLE_NAME)) {// 解析用户角色名称
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUserRoleName(jsonObject.getString(cacheDate.USER_ROLE_NAME));
			}
			if (jsonObject.has(cacheDate.HEAD_IMG)) {// 解析用户头像
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setHeadImage(jsonObject.getString(cacheDate.HEAD_IMG));
			}
			if (jsonObject.has(cacheDate.NICK_NAME)) {// 解析用户昵称
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setNickName(jsonObject.getString(cacheDate.NICK_NAME));
			}
			if (jsonObject.has(cacheDate.SIGNATURE)) {// 解析用户个人签名
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setSignature(jsonObject.getString(cacheDate.SIGNATURE));
			}
			if (jsonObject.has(cacheDate.ORG_NAME)) {// 解析用户部门名称
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setOrgName(jsonObject.getString(cacheDate.ORG_NAME));
			}


			if (jsonObject.has(cacheDate.HELP_NAME)) {// 解析客服名称
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setHelpName(jsonObject.getString(cacheDate.HELP_NAME));
			}
			if (jsonObject.has(cacheDate.HELP_TEL)) {// 解析客服电话
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setHelpTel(jsonObject.getString(cacheDate.HELP_TEL));
			}
			if (jsonObject.has(cacheDate.STORE_INFO_ID)) {
				SharedPreferencesUtil2.getInstance(InitActivity.this.getApplicationContext())
						.saveStoreInfoId(jsonObject.getInt(cacheDate.STORE_INFO_ID));
			}
			if (jsonObject.has(cacheDate.ATTEND_AUTH)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.saveAttendAuth(jsonObject.getString(cacheDate.ATTEND_AUTH));
			}

			if (jsonObject.has(cacheDate.COMPANY_NAME)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setCompanyName(jsonObject.getString(cacheDate.COMPANY_NAME));
			}

			if (jsonObject.has(cacheDate.NEW_ATTENDAUTH)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.saveNewAttendAuth(jsonObject.getString(cacheDate.NEW_ATTENDAUTH));
			}

			if (jsonObject.has(cacheDate.ATTEND_FUNC)) {
				SharedPrefsAttendanceUtil.getInstance(InitActivity.this.getApplicationContext())
						.setAttendFunc(jsonObject.getString(cacheDate.ATTEND_FUNC));
			}

			if (jsonObject.has(cacheDate.PAI_FLG)) {
				SharedPrefsAttendanceUtil.getInstance(InitActivity.this.getApplicationContext())
						.setPaiFlg(jsonObject.getString(cacheDate.PAI_FLG));
			}

			if (jsonObject.has(cacheDate.PHONE_LOGO_PATH)) {// 解析企业logo下载地址
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneLogoPath(jsonObject.getString(cacheDate.PHONE_LOGO_PATH));
			}

			if (isValid(jsonObject, cacheDate.LOC_LEVEL)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setLocLevel(jsonObject.getString(cacheDate.LOC_LEVEL));
			}

			// 解析企业名称信息
			if (jsonObject.has(cacheDate.PHONE_NAME1)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName1(jsonObject.getString(cacheDate.PHONE_NAME1));
			}
			// 解析企业名称文字的大小
			if (jsonObject.has(cacheDate.PHONE_NAME1_SIZE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName1Size(jsonObject.getString(cacheDate.PHONE_NAME1_SIZE));
			}
			// 解析企业名称文字的位置
			if (jsonObject.has(cacheDate.PHONE_NAME1_ALIGN_TYPE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName1AlignType(jsonObject.getString(cacheDate.PHONE_NAME1_ALIGN_TYPE));
			}
			if (jsonObject.has(cacheDate.PHONE_NAME2)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName2(jsonObject.getString(cacheDate.PHONE_NAME2));
			}

			if (jsonObject.has(cacheDate.PHONE_NAME2_SIZE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName2Size(jsonObject.getString(cacheDate.PHONE_NAME2_SIZE));
			}

			if (jsonObject.has(cacheDate.PHONE_NAME2_ALIGN_TYPE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName2AlignType(jsonObject.getString(cacheDate.PHONE_NAME2_ALIGN_TYPE));
			}
			if (jsonObject.has(cacheDate.PHONE_NAME3)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName3(jsonObject.getString(cacheDate.PHONE_NAME3));
			}

			if (jsonObject.has(cacheDate.PHONE_NAME3_SIZE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName3Size(jsonObject.getString(cacheDate.PHONE_NAME3_SIZE));
			}

			if (jsonObject.has(cacheDate.PHONE_NAME3_ALIGN_TYPE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.savePhoneName3AlignType(jsonObject.getString(cacheDate.PHONE_NAME3_ALIGN_TYPE));
			}

			if (cacheDate.isValid(jsonObject, cacheDate.TRACE_FENCE)) {
				SharedPrefsBackstageLocation.getInstance(InitActivity.this.getApplicationContext())
						.saveTraceFence(jsonObject.getInt(cacheDate.TRACE_FENCE));
			}
			if (jsonObject.has(cacheDate.LOCATION_VAILD_ACC)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setLocationValidAcc(jsonObject.getInt(cacheDate.LOCATION_VAILD_ACC));
			}
			if (jsonObject.has(cacheDate.NEW_STORE_TITLE)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setXDSB(jsonObject.getString(cacheDate.NEW_STORE_TITLE));
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
			cacheDate.parseISStoreAddMod(jsonObject);// 新店上报
			cacheDate.parserWeiChat(jsonObject);// 解析企业微信
			cacheDate.parserIsMailList(jsonObject);// 解析通讯录
			cacheDate.parserQuestion(jsonObject);// 解析调查问卷
			cacheDate.parseisLeave(jsonObject);// 解析请假
			cacheDate.parseIsCompanyWeb(jsonObject);// 解析公司id指定展示的网页(例如皇明的mail)
			cacheDate.parserWorkPlan(jsonObject);// 解析工作计划
			cacheDate.parserWorkSum(jsonObject);// 解析工作总结
			cacheDate.parserPictures(jsonObject);//解析轮播图
			cacheDate.parserHY(jsonObject);

			if (jsonObject.has(cacheDate.ATTEND_MUST_ITEMS)) {
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setAttendanceMustDo(jsonObject.getString(cacheDate.ATTEND_MUST_ITEMS));
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setAttendanceMustDoNew(jsonObject.getString(cacheDate.ATTEND_MUST_ITEMS));
			}

			if (jsonObject.has(CacheData.AVAIL_START)) {// 解析有效期开始日期
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUsefulStartDate(jsonObject.getString(CacheData.AVAIL_START));
			}
			if (jsonObject.has(CacheData.AVAIL_END)) {// 解析有效期结束日期
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
						.setUsefulEndDate(jsonObject.getString(CacheData.AVAIL_END));
			}
			// publishProgress(5);//设置进度条的值
			isContinue(5, flag);

			if (jsonObject.has(cacheDate.MENU)) {// 解析menu
				String jsonForMenu = jsonObject.getString(cacheDate.MENU);
				cacheDate.parseMenu(jsonForMenu);
				// publishProgress(10);
				isContinue(10, flag);
			}

			if (jsonObject.has(cacheDate.MENU_SHIHUA)) {// 解析石化menu
				String jsonForMenu = jsonObject.getString(cacheDate.MENU_SHIHUA);
				cacheDate.parseShihuaMenu(jsonForMenu);
			}

			if (jsonObject.has(cacheDate.MOD)) {// 解析自定义模块
				String jsonForMod = jsonObject.getString(cacheDate.MOD);
				cacheDate.parseMod(jsonForMod);
				// publishProgress(15);
				isContinue(15, flag);
			}
			if (jsonObject.has(cacheDate.VISIT)) {// 解析访店
				String jsonForVisit = jsonObject.getString(cacheDate.VISIT);
				cacheDate.parseVisit(jsonForVisit);
				// publishProgress(20);
				isContinue(20, flag);
			}

			if (jsonObject.has(cacheDate.BASE_FUNC)) {// 解析基础控件
				String jsonForBasefunc = jsonObject.getString(cacheDate.BASE_FUNC);
				cacheDate.parseBaseFunc(jsonForBasefunc);
				// publishProgress(25);
				isContinue(25, flag);
			}
			if (jsonObject.has(cacheDate.VISIT_FUNC)) {// 解析访店控件
				String jsonForVisitFunc = jsonObject.getString(cacheDate.VISIT_FUNC);
				cacheDate.parseVisitFunc(jsonForVisitFunc);
				// publishProgress(30);
				isContinue(30, flag);
			}
			if (jsonObject.has(cacheDate.DICT)) {// 解析字典表
				String jsonForDict = jsonObject.getString(cacheDate.DICT);
				cacheDate.parseBatchDictionary(jsonForDict);
				// publishProgress(35);
				isContinue(35, flag);
			}
			if (jsonObject.has(cacheDate.NOTIFY)) {// 解析公告
				String jsonForDict = jsonObject.getString(cacheDate.NOTIFY);
				cacheDate.parseNotify(jsonForDict);
				// publishProgress(40);
				isContinue(40, flag);
			}
			if (jsonObject.has(cacheDate.TASK)) {// 解析任务
				String jsonForDict = jsonObject.getString(cacheDate.TASK);
				cacheDate.parseCustom(jsonForDict);
				// publishProgress(45);
				isContinue(45, flag);
			}
			if (jsonObject.has(cacheDate.DOUBLE)) {// 解析双向
				String doubleJson = jsonObject.getString(cacheDate.DOUBLE);
				cacheDate.parseDouble(doubleJson, true);
				// publishProgress(50);
				isContinue(50, flag);
			}
			if (jsonObject.has(cacheDate.LOCATION_TASK)) {// 解析被动定位
				String jsonLocationRule = jsonObject.getString(cacheDate.LOCATION_TASK);
				cacheDate.parseLocationRule(jsonLocationRule, false, false);
				// publishProgress(55);
				isContinue(55, flag);
			}

			if (jsonObject.has("version")) {// 解析最新apk版本的信息
				JSONObject versionObj = jsonObject.getJSONObject("version");
				if (versionObj.has("md5code")) {// 最新apk文件的MD5
					String md5code = versionObj.getString("md5code");
					SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).saveMD5Code(md5code);
				}
				if (versionObj.has("url")) {// 最新apk下载URl
					String url = versionObj.getString("url");
					SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).saveDownUrl(url);
				}
				if (versionObj.has("version")) {// 最新apk版本号
					String version = versionObj.getString("version");
					SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext())
							.saveNewVersion(version);
				}
				if (versionObj.has("size")) {// 最新APK文件的大小
					String size = versionObj.getString("size");
					SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).saveApkSize(size);
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
				SharedPreferencesUtil2.getInstance(InitActivity.this.getApplicationContext())
						.saveReportWhere(jsonObject.getString(cacheDate.REPORT_WHERE));
			}
			if (jsonObject.has(cacheDate.REPORT_WHERE2)) {// 解析新报表内容
				SharedPreferencesUtil2.getInstance(InitActivity.this.getApplicationContext())
						.saveReportWhere2(jsonObject.getString(cacheDate.REPORT_WHERE2));
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
				new ParsePSS(InitActivity.this).pssconfig(jsonPss);
				// publishProgress(60);
				isContinue(60, flag);
			}

			if (jsonObject.has(cacheDate.ATTENDANCE_IN_WORK)) {
				String jsonInWord = jsonObject.getString(cacheDate.ATTENDANCE_IN_WORK);
				if (!TextUtils.isEmpty(jsonInWord) && jsonInWord.equals("1")) {
					String date = DateUtil.getCurDateTime();
					SharedPreferencesUtil.getInstance(InitActivity.this).setStartWorkTime(date + "do");// 设置上班日期
				}

			}
			if (jsonObject.has(cacheDate.ATTENDANCE_OUT_WORK)) {
				String jsonOutWord = jsonObject.getString(cacheDate.ATTENDANCE_OUT_WORK);
				if (!TextUtils.isEmpty(jsonOutWord) && jsonOutWord.equals("1")) {
					String date = DateUtil.getCurDateTime();
					SharedPreferencesUtil.getInstance(InitActivity.this).setStopWorkTime(date + "do");// 设置下班日期
				}
			}

			if (jsonObject.has(cacheDate.NEW_ATTENDANCE_IN_WORK)) {
				String jsonInWord = jsonObject.getString(cacheDate.NEW_ATTENDANCE_IN_WORK);
				if (!TextUtils.isEmpty(jsonInWord)) {
					SharedPreferencesUtil.getInstance(InitActivity.this).setNewAttendanceStart(jsonInWord);
				}

			}
			if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OUT_WORK)) {
				String jsonOutWord = jsonObject.getString(cacheDate.NEW_ATTENDANCE_OUT_WORK);
				if (!TextUtils.isEmpty(jsonOutWord)) {
					SharedPreferencesUtil.getInstance(InitActivity.this).setNewAttendanceEnd(jsonOutWord);
				}
			}

			if (jsonObject.has(cacheDate.NEW_ATTENDANCE_IN_WORK_OVER)) {
				String jsonInWord = jsonObject.getString(cacheDate.NEW_ATTENDANCE_IN_WORK_OVER);
				if (!TextUtils.isEmpty(jsonInWord)) {
					SharedPreferencesUtil.getInstance(InitActivity.this).setNewAttendanceStartOver(jsonInWord);
				}

			}
			if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OUT_WORK_OVER)) {
				String jsonOutWord = jsonObject.getString(cacheDate.NEW_ATTENDANCE_OUT_WORK_OVER);
				if (!TextUtils.isEmpty(jsonOutWord)) {
					SharedPreferencesUtil.getInstance(InitActivity.this).setNewAttendanceStartOver("");
					SharedPreferencesUtil.getInstance(InitActivity.this).setNewAttendanceEndOver("");
				}
			}

			if (jsonObject.has(cacheDate.NEW_ATTENDANCE_OVERCONF)) {
				String jsonAttendance = jsonObject.getString(cacheDate.NEW_ATTENDANCE_OVERCONF);
				if (!TextUtils.isEmpty(jsonAttendance)) {
					cacheDate.parseNewAttendance(jsonAttendance);

				}
			}

			if (jsonObject.has(cacheDate.LOCATION_TIP_TYPE)) {
				int tipType = jsonObject.getInt(cacheDate.LOCATION_TIP_TYPE);
				SharedPrefsBackstageLocation.getInstance(InitActivity.this).saveLocationTipType(tipType);
				if (tipType == 1) {// 有新规则下达的时候要提示
					SharedPreferencesUtil.getInstance(InitActivity.this).setIsTipUser(false);// 设置成fales表示没有提示用户
				}
			}

			if (isValid(jsonObject, cacheDate.OVER_ATTEND_WAIT)) {
				int wait = jsonObject.getInt(cacheDate.OVER_ATTEND_WAIT);
				SharedPreferencesUtil.getInstance(InitActivity.this).setOverAttendWait(wait);
			} else {
				SharedPreferencesUtil.getInstance(InitActivity.this).setOverAttendWait(1);
			}

			if (isValid(jsonObject, cacheDate.ATTEND_WAIT)) {
				int wait = jsonObject.getInt(cacheDate.ATTEND_WAIT);
				SharedPreferencesUtil.getInstance(InitActivity.this).setAttendWait(wait);
			} else {
				SharedPreferencesUtil.getInstance(InitActivity.this).setAttendWait(1);
			}

			if (isValid(jsonObject, cacheDate.TO_DO)) {
				String toDoJson = jsonObject.getString(cacheDate.TO_DO);
				cacheDate.parserToDo(toDoJson);
			}

			cacheDate.parseOrder2(jsonObject);
			// publishProgress(62);
			isContinue(62, flag);
			cacheDate.parseOrder3(jsonObject);
			// publishProgress(65);
			isContinue(65, flag);
			cacheDate.parseOrder3Send(jsonObject);
			// publishProgress(67);
			isContinue(67, flag);
			cacheDate.parseCarSales(jsonObject);
			// publishProgress(70);

//			cacheDate.removeMenu();//排除重复的menu 待修改
			isContinue(70, flag);
		}
	}

	public boolean isValid(JSONObject jsonObject, String key) throws JSONException {
		boolean flag = false;
		if (jsonObject.has(key) && !jsonObject.isNull(key)) {
			flag = !"".equals(jsonObject.getString(key));
		}
		return flag;
	}

	/**
	 * 初始化完成跳转页面
	 */
	public void initFinsh() {
		if (!isRun) {
			JLog.d(TAG, "已停止");
			return;
		}
		progressBar.setProgress(100);
		tv_info.setText(PublicUtils.getResourceString(this,R.string.init_finish));
		// 初始化完成后设置初始化状态为true 这样下次登录的时候就不会再进行初始化
		SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).setIsInit(true);
		SharedPreferencesUtil.getInstance(this).setGrirmsUserIsCodeOne("2");// 赋值为2告诉服务端需要验证三码，不一致不通过
		// 尝试删除多余的push
		// clearPush(Constants.URL_PUSH_CLEAN_ALL);
		inputHome(); // 进入主页面

	}

	/**
	 * //请求服务器删除多余的push数据
	 * 
	 * @param url
	 */
	private void clearPush(final String url) {
		if (!isRun) {
			JLog.d(TAG, "已停止");
			return;
		}
		GcgHttpClient.getInstance(this, GcgHttpClient.TIME_OUT_SHORT)
				.get(url + "&mdn=" + PublicUtils.receivePhoneNO(this), null, new HttpResponseListener() {

					@Override
					public void onFailure(Throwable error, String content) {
						bak(url);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						String r = PublicUtils.verifyReturnValue(content);
						if (!TextUtils.isEmpty(r)) {
							inputHome(); // 进入主页面
						} else {
							bak(url);
						}

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

				});
	}

	private void bak(String url) {
		if (url.contains(Constants.URL_PUSH_CLEAN_ALL_BAK)) {
			inputHome(); // 进入主页面
		} else {
			clearPush(Constants.URL_PUSH_CLEAN_ALL_BAK);
		}
	}

	/**
	 * 跳转到主页面
	 */
	private void inputHome() {

		Intent intent = new Intent(InitActivity.this, HomePageActivity.class);
		startActivity(intent);
		InitActivity.this.finish();
	}

	/**
	 * 当前页面关闭的时候 关闭监听 并且把线程运行状态标识置为false
	 */
	@Override
	protected void onDestroy() {
		if (request.isOpenedHttp()) {
			request.releaseHttp();
		}
		super.onDestroy();
	}

	/**
	 * 初始化机构、用户、店面 此方法会根据type来进行递归遍历机构、用户、店面是否需要初始化下载，
	 * 如果type+1>3,代表已遍历完成，则进入initFinsh()
	 * 
	 * @param type
	 */
	private void getOrg(int type) {
		if (!isRun) {
			JLog.d(TAG, "已停止");
			return;
		}
		getOrg = new GetOrg(this, getOrgResponseListener);
		if (type == Func.ORG_OPTION && getOrg.getOrgOperation()) {
			getOrg.getOrg();
			progressBar.setProgress(90);
		} else if (type == Func.ORG_USER && (getOrg.getUserOperation() || CacheData.isWechat)) {
			getOrg.getUser();
			progressBar.setProgress(95);

		} else if (type == Func.ORG_STORE && getOrg.getStoreOperation()) {
			getOrg.getStore();
			progressBar.setProgress(99);
		} else if (type > Func.ORG_STORE) {
			initFinsh();
		} else {
			orgType = type;
			getOrg(orgType + 1);// 递归
		}

	}

	private GetOrg.ResponseListener getOrgResponseListener = new GetOrg.ResponseListener() {

		@Override
		public void onSuccess(int type, String result) {
			orgType = type;
			try {
				receiveOrgReq(result);
			} catch (JSONException e) {
				e.printStackTrace();
				getOrg(orgType + 1);// 递归
			}
		}

		@Override
		public void onFailure(int type) {
			orgType = type;
			getOrg(orgType + 1);// 递归

		}

	};

	/**
	 * 获取组织机构和机构店面信息 并开始解析信息
	 * 
	 * @param result
	 *            json格式的文本信息
	 * @throws JSONException
	 * 捕获解析异常
	 * @exception ，提示用户初始化失败
	 */
	public void receiveOrgReq(String result) throws JSONException {
		JSONObject jsonObject = new JSONObject(result);
		String resultcode = jsonObject.getString(Constants.RESULT_CODE);
		if (resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)) {
			JLog.d(TAG, result);
			// 解析返回的数据
			new initORGAnsyncTask().execute(result);
		} else {// 如果不是返回"0000"表示初始化失败
			getOrg(orgType + 1);// 递归
		}
	}

	/**
	 * 解析机构、店面、用户
	 * 
	 * @author houyu
	 *
	 */
	private class initORGAnsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			boolean flag = true;
			String json = params[0];// 获取要解析的json数据
			try {
				// 开始解析
				new OrgParser(InitActivity.this).batchParseByType(json, orgType);
			} catch (Exception e) {
				// 如果有异常该变标识状态
				flag = false;
				e.printStackTrace();
			}
			isOrgAsync();
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			getOrg(orgType + 1); // 递归
		}
	}

	public void isOrgAsync() {
		// 4.1版本以上
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

		} else {
			Message message = initHandler.obtainMessage();
			message.sendToTarget();
		}
	}

	private Handler initHandler = new Handler() {
		public void handleMessage(Message msg) {
			getOrg(orgType + 1); // 递归

		};
	};
	private Handler orgHandler = new Handler() {
		public void handleMessage(Message msg) {
			boolean flag = (Boolean) msg.obj;
			if (flag) {// 模块信息加载完以后开始获取组织机构和机构店面信息
				getOrg(orgType);
			} else {
				clearCache(PublicUtils.getResourceString(InitActivity.this,R.string.data_un_complement));// allInfo数据不完整，表示数据整体不完整，需要重新初始化
				tv_info.setText(PublicUtils.getResourceString(InitActivity.this,R.string.init_failure));
				// 设置false表示初始化失败，下载重新登录的时候还要进行初始化
				SharedPreferencesUtil.getInstance(InitActivity.this.getApplicationContext()).setIsInit(false);
			}
		};
	};

	/**
	 * 清除SharedPreferencesUtil2 SharedPrefsBackstageLocation 删除数据库中的表
	 * 
	 * @param temp
	 *            标识为什么要清楚存LOG使用
	 */
	private void clearCache(String temp) {
		JLog.writeErrorLog(Thread.currentThread().getName() + "(" + temp + ")=>删除全部缓存", TAG);
		SharedPreferencesUtil2.getInstance(this.getApplicationContext()).clearAll();
		SharedPrefsBackstageLocation.getInstance(this.getApplicationContext()).clearAll();
		DatabaseHelper.getInstance(this).deteleAllTable();
		new Dao(this).delete();
	}

	/**
	 * 捕获回退按键 按回退的时候跳转到登陆页面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 点击返回按钮
			isRun = false;
			if (!PublicUtils.ISDEMO) {
//				this.startActivity(new Intent(this, LoginActivity.class));
			}
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
