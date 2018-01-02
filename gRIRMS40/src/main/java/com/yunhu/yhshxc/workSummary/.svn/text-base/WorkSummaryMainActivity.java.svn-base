package com.yunhu.yhshxc.workSummary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.ToastUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workSummary.bo.SummaryAssess;
import com.yunhu.yhshxc.workSummary.bo.WorkSummaryModle;
import com.yunhu.yhshxc.workSummary.view.CaledarAdapter;
import com.yunhu.yhshxc.workSummary.view.CalendarBean;
import com.yunhu.yhshxc.workSummary.view.CalendarDateView;
import com.yunhu.yhshxc.workSummary.view.CalendarLayout;
import com.yunhu.yhshxc.workSummary.view.SummaryAssessItemView;
import com.yunhu.yhshxc.workSummary.view.SummaryAssessItemView.AssessSubmitSucessListener;
import com.yunhu.yhshxc.workSummary.view.SummaryContentView;
import com.yunhu.yhshxc.workSummary.view.CalendarLayout.OnOpenChangeListener;
import com.yunhu.yhshxc.workplan.CreateNewPlanItem;
import com.yunhu.yhshxc.workplan.WorkPlanUtils;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.yunhu.yhshxc.workplan.db.AssessDB;
import com.yunhu.yhshxc.workplan.db.PlanDataDB;
import com.yunhu.yhshxc.workplan.db.WorkPlanModleDB;
import com.loopj.android.http.RequestParams;
import com.yunhu.yhshxc.workSummary.view.CalendarView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import gcg.org.debug.JLog;

import static com.yunhu.yhshxc.application.SoftApplication.context;

public class WorkSummaryMainActivity extends Activity implements OnClickListener, OnFuzzyQueryListener {
	private LinearLayout workPlanLayout, workSummaryLayout, workSummaryAccessLayout;// 计划内容容器,总结内容容器,
																					// 审批内容容器
	private Button workSummaryDate;// 工作总结日期
	private DropDown workSummaryMine;// 我的总结
	private ImageView workSummaryBack;// 返回
	private String selectDate = "";// 测试用
	private MyProgressDialog searchDialog;
	private int planId;// 计划id
	private String userId;// 用户id,首先默认为自己的,选择后为选择用户的
	private CalendarLayout worksummaryCalendarlayout;
	private CalendarDateView mCalendarDateView;// 日历视图
	private LinearLayout summaryWeekTitle;// 日历星期标题
	private TextView workPlantitle, worksummarytitle, worksummaryAccessTitle, tvToLeft, tvToRight;// 今日计划标题,今日总结标题,
	// private Map<String,View> dayViews = new HashMap<String,View>();//
	// 记录每个月的每天的view //
	List<View> viewsDay = new ArrayList<View>();
	// 审批标题,日历向左,日历向右
	private List<Dictionary> srcList = new ArrayList<Dictionary>();

	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_summary_main);
		initView();
	}

	private void initView() {
		selectDate = DateUtil.getCurDate();
		sv = (ScrollView) findViewById(R.id.list);
		sv.getParent().requestDisallowInterceptTouchEvent(true);
		userId = String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId());
		workSummaryDate = (Button) findViewById(R.id.work_summary_date);
		workSummaryDate.setText(selectDate.substring(0, selectDate.lastIndexOf("-")));
		workSummaryMine = (DropDown) findViewById(R.id.work_summary_mine);
		workSummaryBack = (ImageView) findViewById(R.id.worksummary_back);
		workPlanLayout = (LinearLayout) findViewById(R.id.work_plan_layout);
		workSummaryLayout = (LinearLayout) findViewById(R.id.work_summary_layout);
		mCalendarDateView = (CalendarDateView) findViewById(R.id.calendarDateView);
		summaryWeekTitle = (LinearLayout) findViewById(R.id.work_summary_weektitle);
		workSummaryAccessLayout = (LinearLayout) findViewById(R.id.work_summaryaccess_layout);
		worksummaryAccessTitle = (TextView) findViewById(R.id.worksummary_accesstitle);
		worksummaryCalendarlayout = (CalendarLayout) findViewById(R.id.worksummary_calendarlayout);
		worksummarytitle = (TextView) findViewById(R.id.worksummary_summarytitle);
		workPlantitle = (TextView) findViewById(R.id.worksummary_plantitle);
		tvToLeft = (TextView) findViewById(R.id.work_summary_left);
		tvToRight = (TextView) findViewById(R.id.work_summary_right);
		workSummaryMine.setOnFuzzyQueryListener(this);
		workSummaryMine.setOnResultListener(resultListener);
		tvToLeft.setOnClickListener(this);
		tvToRight.setOnClickListener(this);
		workSummaryBack.setOnClickListener(this);
		summaryWeekTitle.setVisibility(View.GONE);
		worksummaryCalendarlayout.setOpenChangeListener(new OnOpenChangeListener() {

			@Override
			public void onOpen() {
				summaryWeekTitle.setVisibility(View.VISIBLE);

			}

			@Override
			public void onFlod() {
				summaryWeekTitle.setVisibility(View.GONE);

			}
		});
		// 根据当前日期初始化日历展示,首先只展示当前日期所在的的一行,点击时展示当前月所有
		// dayViews.clear();
		mCalendarDateView.setAdapter(new CaledarAdapter() {
			@Override
			public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {

				if (convertView == null) {
					convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_xiaomi, null);
				}
				String month1 = (bean.moth) < 10 ? "0" + (bean.moth) : (bean.moth) + "";
				String day1 = bean.day < 10 ? "0" + bean.day : bean.day + "";
				String tag1 = bean.year + "-" + month1 + "-" + day1;
				ImageView ivPoint = (ImageView) convertView.findViewById(R.id.data_red_point);
				ivPoint.setVisibility(View.INVISIBLE);
				convertView.setTag(tag1);
				if (viewsDay.contains(convertView)) {
					viewsDay.remove(convertView);
				}
				viewsDay.add(convertView);



				TextView text = (TextView) convertView.findViewById(R.id.text);
				text.setText("" + bean.day);
				if (bean.mothFlag != 0) {
					text.setTextColor(0xff9299a1);
				} else {
					text.setTextColor(Color.WHITE);
				}
				// chinaText.setText(bean.chinaDay);

				return convertView;
			}
		});
		mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int postion, CalendarBean bean) {
				// mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day);
				String rYear = bean.year + "";
				String rMonth = bean.moth < 10 ? "0" + bean.moth : "" + bean.moth;
				String rDay = bean.day < 10 ? "0" + bean.day : "" + bean.day;
				selectDate = rYear + "-" + rMonth + "-" + rDay;
				workSummaryDate.setText(rYear + "-" + rMonth);
				// 每次选择完日期都要联网查询数据
				initWorkInfo();
			}
		});

		// 初始化用户选择数据
		initUserData(null);

		initWorkInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 选择用户,权限

		case R.id.worksummary_back:// 返回
			 this.finish();
			break;
		case R.id.work_summary_left:// 日期左翻
			mCalendarDateView.setCurrentItem(mCalendarDateView.getCurrentItem() - 1);
			break;
		case R.id.work_summary_right:// 日期右翻
			mCalendarDateView.setCurrentItem(mCalendarDateView.getCurrentItem() + 1);
			break;
		default:
			break;
		}

	}

	// 初始化用户数据
	private void initUserData(String fuzzy) {
		srcList = workPlanUserData(WorkSummaryMainActivity.this, fuzzy);
		workSummaryMine.setSrcDictList(srcList);
		initStoreSelect(srcList);

	}

	/**
	 * 设置选中的用户
	 * 
	 * @param srcList
	 *            所有用户
	 */
	private void initStoreSelect(List<Dictionary> srcList) {
		if (!TextUtils.isEmpty(userId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (userId.equals(dic.getDid())) {
					workSummaryMine.setSelected(dic);
					break;
				}
			}
		}
	}

	/**
	 * 工作计划查看人员
	 * 
	 * @param context
	 * @return
	 */
	public List<Dictionary> workPlanUserData(Context context, String fuuy) {
		String attendAuth = SharedPreferencesUtil.getInstance(context.getApplicationContext()).getWorkPlanAuth(); // 工作计划加入权限

		Integer auth = null;
		String authOrgId = null;
		if (!TextUtils.isEmpty(attendAuth)) {
			String arr[] = attendAuth.split("\\|");
			auth = Integer.valueOf(arr[0]);
			if (arr.length == 2) {
				authOrgId = arr[1];
			}
		}
		List<OrgUser> list = new OrgUserDB(context).findOrgUserListByAuth(auth, authOrgId, fuuy, null);
		return setDataSrcListForOrgUser(list);
	}

	/**
	 * 将用户实例转化为字典的形式
	 * 
	 * @param orgUserList
	 *            用户实例集合
	 */
	private List<Dictionary> setDataSrcListForOrgUser(List<OrgUser> orgUserList) {
		List<Dictionary> dataSrcList = new ArrayList<Dictionary>();
		if (orgUserList != null && !orgUserList.isEmpty()) {
			for (int i = 0; i < orgUserList.size(); i++) {
				OrgUser orgUser = orgUserList.get(i);
				Dictionary dic = new Dictionary();
				// dic.setCtrlCol(orgUser.getUserName());
				dic.setDid(orgUser.getUserId() + "");

				if (orgUser.getUserId()
						.equals(SharedPreferencesUtil.getInstance(WorkSummaryMainActivity.this).getUserId())) {
					dic.setCtrlCol(PublicUtils.getResourceString(context,R.string.my_summary));
					dataSrcList.add(0, dic);
				} else {
					dic.setCtrlCol(orgUser.getUserName());
					dataSrcList.add(dic);
				}

			}
		}
		return dataSrcList;
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				userId = dic.getDid().trim();
				String ctrlcol = dic.getCtrlCol();
				String myId = PublicUtils.getResourceString(WorkSummaryMainActivity.this,R.string.my_summary).trim();
				if (ctrlcol.equals(myId)){//说明是自己的id
					userId=SharedPreferencesUtil.getInstance(WorkSummaryMainActivity.this).getUserId()+"";
				}
				initWorkInfo();
			}

		}
	};

	// 根据选择的日期初始化工作计划和工作总结数据
	// 分为自己的和别人的
	private void initWorkInfo() {
		// 先清空数据存储的上次查询数据
		clearDatabaseData();
		clearViewPoint();
		// 清空上次添加的view信息
		workPlanLayout.removeAllViews();
		workSummaryLayout.removeAllViews();
		workSummaryAccessLayout.removeAllViews();
		// 隐藏标题
		workPlantitle.setVisibility(View.GONE);
		worksummarytitle.setVisibility(View.GONE);
		worksummaryAccessTitle.setVisibility(View.GONE);
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.init));
		String url = UrlInfo.queryWorkPlan(this);
		RequestParams param = searchWorkPlanParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url, param, new HttpResponseListener() {
			@Override
			public void onStart() {
				searchDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				// Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						WorkPlanUtils util = new WorkPlanUtils(WorkSummaryMainActivity.this);
						if (PublicUtils.isValid(obj, "planData")) {
							JSONArray arrayPlan = obj.optJSONArray("planData");
							List<PlanData> planList = util.parsePlanDataList(arrayPlan);
						}
						if (PublicUtils.isValid(obj, "detail")) {
							JSONArray arrayDetail = obj.optJSONArray("detail");
							List<WorkPlanModle> moleList = util.parsePlanModleList(arrayDetail);
						}
						if (PublicUtils.isValid(obj, "assess")) {
							JSONArray arrayAssess = obj.optJSONArray("assess");
							List<Assess> assessList = util.parseAssessList(arrayAssess);
						}
						PlanDataDB planDataDB = new PlanDataDB(WorkSummaryMainActivity.this);
						// 如果点击日期后,每次查询完,planid应该发生改变,需要根据查询的开始日期去表中查找
						planId = planDataDB.getWorkPlanIdByStartDate(selectDate);

					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(getApplicationContext(), PublicUtils.getResourceString(context,R.string.ERROR_DATA), ToastOrder.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("view", "onFailure:" + content);
				ToastOrder.makeText(getApplicationContext(), PublicUtils.getResourceString(context,R.string.retry_net_exception), ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				// 解析并放入数据库以后,从数据库中读取并展示出来
				// 根据所选日期去数据库查询对应的planId,如果没有数据,则提示
				int pl_id = new PlanDataDB(WorkSummaryMainActivity.this).getWorkPlanIdByStartDate(selectDate);
				if (pl_id > 0 || pl_id < 0) {// 说明数据库有数据
					planId = pl_id;
					// 根据数据的useid判断是否是自己的数据
					if (Integer.parseInt(userId) == SharedPreferencesUtil.getInstance(WorkSummaryMainActivity.this)
							.getUserId()) {// 自己的数据
						initDataSelf();
					} else {// 别人的数据
						initDataOther();
					}
					// getArrayForCirclePoint();//初始化小红点
				} else {
					Toast.makeText(WorkSummaryMainActivity.this,PublicUtils.getResourceString(context,R.string.search_no_data), Toast.LENGTH_SHORT).show();
				}

			}

		});

	}

	// 初始化别人的
	private void initDataOther() {

		// 去WORK_PLAN_MODLE表里查询数据
		WorkPlanModleDB woDb = new WorkPlanModleDB(this);
		List<WorkPlanModle> list = woDb.checkPlansByPlanId(planId);
		if (list.size() > 0) {
			workPlantitle.setVisibility(View.VISIBLE);
		} else {
			workPlantitle.setVisibility(View.GONE);
		}
		for (int i = 0; i < list.size(); i++) {
			WorkPlanModle modle = list.get(i);
			modle.setPlanSort(i + 1);
			final CreateNewPlanItem item = new CreateNewPlanItem(this);
			item.getTextNumber().setVisibility(View.VISIBLE);
			item.setSaveViewHidden(false);// 只显示内容框
			item.setItemId(System.currentTimeMillis());
			item.setAllSaveInfo(modle);
			workPlanLayout.addView(item.getView());
		}
		// 查询工作总结
		searChWorkSumInfo();

	}

	// 初始化自己的
	private void initDataSelf() {
		// 去WORK_PLAN_MODLE表里查询数据
		WorkPlanModleDB woDb = new WorkPlanModleDB(this);
		List<WorkPlanModle> list = woDb.checkPlansByPlanId(planId);
		if (list.size() > 0) {
			workPlantitle.setVisibility(View.VISIBLE);
		} else {
			workPlantitle.setVisibility(View.GONE);
		}
		for (int i = 0; i < list.size(); i++) {
			WorkPlanModle modle = list.get(i);
			modle.setPlanSort(i + 1);
			final CreateNewPlanItem item = new CreateNewPlanItem(this);
			item.getTextNumber().setVisibility(View.VISIBLE);
			item.setSaveViewHidden(false);// 只显示内容框
			item.setItemId(System.currentTimeMillis());
			item.setAllSaveInfo(modle);
			workPlanLayout.addView(item.getView());
		}
		// 查询工作总结
		searChWorkSumInfo();
	}

	// 查询工作总结信息
	private void searChWorkSumInfo() {
		String url = UrlInfo.queryWorkSum(this);
		RequestParams para = searchWorkSumParams();
		GcgHttpClient.getInstance(this).get(url, para, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				// 处理工作总结的信息
				try {
					JSONObject allObject = new JSONObject(content);
					String resultCode = allObject.getString("resultcode");
					if (resultCode.equals("0000")) {// 数据正常返回
						if (PublicUtils.isValid(allObject, "list")) {// 本月份有工作总结的日期集合
							parseDayList(allObject);
						}else{
//							viewsDay.clear();

						}
						if (PublicUtils.isValid(allObject, "sumData")) {
							JSONObject jsObject = allObject.getJSONObject("sumData");

							WorkSummaryModle summaryModle = new WorkSummaryModle();
							SummaryAssess sa = new SummaryAssess();
							if (PublicUtils.isValid(jsObject, "id")) {
								sa.setId(jsObject.getInt("id"));
							}
							if (PublicUtils.isValid(jsObject, "plan_id")) {// 计划id
								summaryModle.setPlanId(jsObject.getString("plan_id"));

							}
							if (PublicUtils.isValid(jsObject, "sub_time")) {// 总结时间
								summaryModle.setSubTime(jsObject.getString("sub_time"));
							}
							if (PublicUtils.isValid(jsObject, "sum_date")) {// 总结日期
								summaryModle.setSumDate(jsObject.getString("sum_date"));
							}
							if (PublicUtils.isValid(jsObject, "sum_marks")) {// 总结内容
								summaryModle.setSumMarks(jsObject.getString("sum_marks"));
							}
							if (PublicUtils.isValid(jsObject, "user_code")) {// 权限码
								summaryModle.setUserCode(jsObject.getString("user_code"));
							}
							if (PublicUtils.isValid(jsObject, "user_id")) {// 总结人id
								summaryModle.setUserId(jsObject.getString("user_id"));
							}
							if (PublicUtils.isValid(jsObject, "audit_user_name")) {// 评审人
								summaryModle.setAuditUserName(jsObject.getString("audit_user_name"));
								sa.setAuditName(jsObject.getString("audit_user_name"));
							}
							if (PublicUtils.isValid(jsObject, "audit_type")) {// 评审类型
								summaryModle.setAuditType(jsObject.getString("audit_type"));
								sa.setAudit_type(jsObject.getString("audit_type"));
							}
							if (PublicUtils.isValid(jsObject, "audit_status")) {// 评审状态
								summaryModle.setAuditStatus(jsObject.getString("audit_status"));
								sa.setAudit_status(jsObject.getString("audit_status"));
							}
							if (PublicUtils.isValid(jsObject, "audit_marks")) {// 评审内容
								summaryModle.setAuditMarks(jsObject.getString("audit_marks"));
								sa.setAudit_marks(jsObject.getString("audit_marks"));
							}
							if (PublicUtils.isValid(jsObject, "isAduit")) {// 评论权限
								summaryModle.setIsAduit(jsObject.getString("isAduit"));
								sa.setIsAduit(jsObject.getString("isAduit"));
							}
							if (PublicUtils.isValid(jsObject, "attachment")) {// 附件信息
								JSONObject attachmentObj = jsObject.getJSONObject("attachment");

								// 获取图片路径
								Iterator iterator = attachmentObj.keys(); // joData是JSONObject对象
								while (iterator.hasNext()) {
									String key = iterator.next() + "";// id
									JSONObject obj = attachmentObj.optJSONObject(key);
									Iterator it = obj.keys();
									List<String> picUrls = new ArrayList<String>();
									List<String> recUrls = new ArrayList<String>();
									while (it.hasNext()) {
										String kt = it.next() + "";// 文件名
										String url = (String) obj.get(kt);// 文件url
										if (kt.endsWith(".jpg")) {
											picUrls.add(url);
										} else if (kt.endsWith(".3gp")) {
											recUrls.add(url);
										}
									}
									summaryModle.setPicUrl(picUrls);
									summaryModle.setRecordUrl(recUrls);
								}

								// 获取录音路径
							}

							// 所有数据解析完毕以后,加载UI界面展示读取的数据
							loadSummaryUI(summaryModle, sa);

						} else {
							ToastUtil.showText(WorkSummaryMainActivity.this, PublicUtils.getResourceString(context,R.string.search_no_data));
							loadSummaryUI(null, null);// 如果没有工作总结展示添加工作总结的视图
						}

					} else {
						ToastUtil.showText(WorkSummaryMainActivity.this, PublicUtils.getResourceString(context,R.string.search_no_data));
					}

				} catch (JSONException e) {
					e.printStackTrace();
					JLog.writeErrorLog("工作总结数据解析异常", "WorkSummaryMainActivity");
					Toast.makeText(WorkSummaryMainActivity.this, PublicUtils.getResourceString(context,R.string.data_parse_exception), Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				if (searchDialog != null && searchDialog.isShowing()) {
					searchDialog.dismiss();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(WorkSummaryMainActivity.this, PublicUtils.getResourceString(context,R.string.request_failure_checknet), Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void clearViewPoint(){
		for (View view : viewsDay) {
			ImageView ivPoint = (ImageView) view.findViewById(R.id.data_red_point);
			if (ivPoint!=null){
				ivPoint.setVisibility(View.INVISIBLE);

			}
		}
	}

	private void parseDayList(JSONObject allObject) {

//		for (int i = 0; i < viewsDay.size(); i++) {
//			View view = viewsDay.get(i);
//			ImageView ivPoint = (ImageView) view.findViewById(R.id.data_red_point);
//			ivPoint.setVisibility(View.INVISIBLE);
//		}
		try {
			JSONArray dayList = allObject.getJSONArray("list");
			for (int i = 0; i < dayList.length(); i++) {
				String aDay = dayList.getString(i);
				for (int j = 0; j < viewsDay.size(); j++) {
					View view = viewsDay.get(j);
					String tag = (String) view.getTag();
					if (aDay.equals(tag)) {
						ImageView ivPoint = (ImageView) view.findViewById(R.id.data_red_point);
						ivPoint.setVisibility(View.VISIBLE);
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 加载工作总结的UI界面
	 * 
	 * @param
	 *
	 * @param summaryModle
	 */
	private void loadSummaryUI(WorkSummaryModle summaryModle, SummaryAssess sa) {
		if (summaryModle != null) {
			SummaryContentView suView = new SummaryContentView(this);
			suView.setSummaryModle(summaryModle);
			suView.setDate(selectDate);
			suView.setPlanId(planId);
			workSummaryLayout.addView(suView.getView());
			// 判断userId是否为自己的,
			String localUserId = SharedPreferencesUtil.getInstance(this).getUserId() + "";
			if (userId.equals(localUserId)) {
				// 如果是自己的,去获取别人的评审内容,不展示提交评审项
//				if () {// 0未审批 1 已审批
					SummaryAssessItemView assess = new SummaryAssessItemView(this);
					assess.initData(sa);
					workSummaryAccessLayout.addView(assess.getView());
//				}

			} else {
				// 否则获取评审内容,如果没有,则判断自己是否有权限进行评审,来控制评审项的展示
				if (summaryModle.getIsAduit().equals("1")) { // 0 无权限 1 有权限
					if (summaryModle.getAuditStatus().equals("0")) {// 0 未审批 1
																	// 已审批
						// if(DateUtil.compareDateStr(selectDate,
						// DateUtil.getCurDateTime())<0){

						SummaryAssessItemView assess = new SummaryAssessItemView(this);
						assess.setWorkId(sa.getId());
						assess.initData(null);
						assess.setAssessSubmitListener(new AssessSubmitSucessListener() {

							@Override
							public void onSubmitSucess() {
								initWorkInfo();
							}
						});
						workSummaryAccessLayout.addView(assess.getView());
						// }
					} else {// 已审批 ，展示
						SummaryAssessItemView assess = new SummaryAssessItemView(this);
						assess.initData(sa);
						workSummaryAccessLayout.addView(assess.getView());
					}
				} else if (summaryModle.getIsAduit().equals("0") ) {// 无权限，已审批，展示
					SummaryAssessItemView assess = new SummaryAssessItemView(this);
					assess.initData(sa);
					workSummaryAccessLayout.addView(assess.getView());
				}
			}

		} else {
			String localUserId = SharedPreferencesUtil.getInstance(this).getUserId() + "";
			if (userId.equals(localUserId)) {
				// 如果是自己的,显示添加工作总结，点击可新创建
				SummaryContentView suView = new SummaryContentView(this);
				suView.setSummaryModle(summaryModle);
				suView.setDate(selectDate);
				suView.setPlanId(planId);
				workSummaryLayout.addView(suView.getView());
			} else {
				// 否则不能创建
			}

			// 没有工作总结,只展示添加工作总结项
			// 点击添加工作总结项进行跳转到创建工作总结页面
		}

	}

	/**
	 * 清空工作计划的存储数据库
	 */
	private void clearDatabaseData() {
		WorkPlanModleDB modleDb = new WorkPlanModleDB(this);
		modleDb.clearWorkPlan();
		PlanDataDB planDb = new PlanDataDB(this);
		planDb.clearPlanDataList();
		AssessDB aDb = new AssessDB(this);
		aDb.clearAssess();
		// 清空加载数据的容器
		// workplan_detail_container.removeAllViews();
		// workplan_Assess_container.removeAllViews();

	}

	// 工作计划参数
	private RequestParams searchWorkPlanParams() {

		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", userId);
		param.put("type", 1);
		param.put("fromTime", selectDate.substring(0, selectDate.lastIndexOf("-")));
		return param;

	}

	// 工作总结的参数
	private RequestParams searchWorkSumParams() {

		if (TextUtils.isEmpty(userId)) {
			userId = String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId());
		}
		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", userId);
		param.put("fromTime", selectDate);// 选择日期
		param.put("test", "gcg");
		return param;

	}

	@Override
	public void onTextChanged(CharSequence s) {
		initUserData(s.toString());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearDatabaseData();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			initWorkInfo();
			break;

		default:
			break;
		}
		
	}
}
