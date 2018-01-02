package com.yunhu.yhshxc.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.loopj.android.http.RequestParams;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.ReportPreviewActivity;
import com.yunhu.yhshxc.activity.fragment.reportbean.ReportMonthIndex;
import com.yunhu.yhshxc.activity.fragment.reportbean.ReportNewBusiness;
import com.yunhu.yhshxc.activity.fragment.reportbean.ReportNewClient;
import com.yunhu.yhshxc.activity.fragment.reportbean.ReportSales;
import com.yunhu.yhshxc.attendance.attendCalendar.AttendanceCalendarActivity;
import com.yunhu.yhshxc.attendance.attendCalendar.AttendanceInfo;
import com.yunhu.yhshxc.bo.PhoneReportNew;
import com.yunhu.yhshxc.bo.PhoneReportVistTopTen;
import com.yunhu.yhshxc.bo.ThisMonthAttendance;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gcg.org.debug.JLog;

/**
 * home页的报表页面
 * 
 *
 */
public class ReportFragment extends Fragment implements OnClickListener {
	protected static final String TAG = ReportFragment.class.getSimpleName();
	private Context mContext;
	private ImageView report_bar_demo;// 标题栏跳转至展示图片的按钮
	// 个人报表组件初始化
	private LinearLayout person_report;// 个人报表布局
	private LinearLayout today_visit_condition;// 个人今日拜访条目布局
	private ProgressBar personreport_progress;// 今日拜访计划进度条
	private TextView personreport_progress_count;// 今日拜访计划完成数
	private TextView personreport_visit_progress;// 今日拜访计划完成度
	private LinearLayout personreport_attendance_statistics;// 本月考勤情况条目
	private TextView personreport_normal_duty;// 正常出勤
	private TextView personreport_late;// 迟到早退
	private TextView personreport_other;// 其他
	private TextView personal_attendance_exception_days;// 考勤异常总天数
	private LinearLayout personreport_attendance_chart;// 本月考勤情况布局
	private Button personreport_attendance_detail;// 查看详情
	private BarChart personreport_attendance_columchart;// 本月考勤情况图表
	// 管理者报表组件初始化
	private LinearLayout manager_report;// 管理者报表布局

	private LinearLayout managerreport_visit_plan;// 今日拜访计划条目
	private TextView managerreport_yedai_count;// 下发人数
	private TextView managerreport_visitstore_count;// 执行人数
	private TextView managerreport_customer_count;// 已访总数
	private TextView managerreport_everyvisit_count;// 拜访完成率
	private LinearLayout managerreport_visitstore_detail;// 今日拜访计划条目点击展示布局
	private BarChart managerreport_visitstore_columchart;// 今日拜访计划条目点击展示图表

	private LinearLayout managerreport_attandance_condition;// 本月出勤情况条目布局
	private TextView managerreport_rest_count;// 正常出勤
	private TextView managerreport_trip_count;// 迟到早退
	private TextView managerreport_leave_count;// 请假人次
	private TextView managerreport_month_normal_attendance;// 正常出勤率
	private LinearLayout managerreport_attendance_chart;// 本月出勤情况详情布局
	private BarChart managerreport_attendance_columchart;// 本月考勤情况图表
	private Button managerreport_attendance_detail;// 查看详情
	private LinearLayout managerreport_trip_detail;// 查看详情的list展示
	private ListView managerreport_trip_detail_list;// 出差详情的list控件

	private boolean isManager = false;// 是否是管理者
	private boolean isPerson = false;// 是否是个人
	private PhoneReportNew reporManager = null; // 管理者
	private PhoneReportVistTopTen reportPerson = null; // 访店前十实体类
	private ThisMonthAttendance thisMonthAttendance = null;// 本月出勤实体类
	private List<PhoneReportVistTopTen> topTenList = null;

	private boolean isManagerReportVisitStoreShow = false;// 响应今日拜访计划点击显示报表
	private boolean isManagerReportAttendanceShow = false;// 响应本月出勤情况点击显示报表
	private boolean isPersonReportAttendanceShow = false;// 响应个人报表本月考勤统计

	private boolean isCanUseManagerReportVisitStoreChart = false;// 是否使用今日拜访计划图表
	private boolean isCanUseManagerReportAttendanceChart = false;// 是否使用本月出勤情况图表
	private boolean isManagerReportAttendanceCanClick = false;// 是否使用管理者本月出勤报表
	private boolean isPersonReportAttendanceCanClick = false;// 是否使用个人本月出勤报表
	private String phoneNumber;
	private MyProgressDialog mDialog = null;
	private boolean isHaveDate = false;// 是否配置了报表数据

	private LinearLayout reportMainContainer;// 布局容器,用于添加新增条目

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_phone_report, null);
		mContext = getActivity();
		phoneNumber = PublicUtils.receivePhoneNO(getActivity());
		// phoneNumber = "18910901892";
		// phoneNumber = "18910901869";

		initView(view);
		// initData();

		return view;
	}

	/**
	 * 初始化当前页面组件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		report_bar_demo = (ImageView) view.findViewById(R.id.report_bar_demo);
		report_bar_demo.setOnClickListener(this);
		if (PublicUtils.ISDEMO) {
			report_bar_demo.setVisibility(View.VISIBLE);
		} else {
			report_bar_demo.setVisibility(View.GONE);

		}
		// 个人
		person_report = (LinearLayout) view.findViewById(R.id.person_report);
		today_visit_condition = (LinearLayout) view.findViewById(R.id.today_visit_condition);
		personreport_progress = (ProgressBar) view.findViewById(R.id.personreport_progress);
		personreport_progress_count = (TextView) view.findViewById(R.id.personreport_progress_count);
		personreport_visit_progress = (TextView) view.findViewById(R.id.personreport_visit_progress);
		personreport_attendance_statistics = (LinearLayout) view.findViewById(R.id.personreport_attendance_statistics);
		personreport_normal_duty = (TextView) view.findViewById(R.id.personreport_normal_duty);
		personreport_late = (TextView) view.findViewById(R.id.personreport_late);
		personreport_other = (TextView) view.findViewById(R.id.personreport_other);
		personal_attendance_exception_days = (TextView) view.findViewById(R.id.personal_attendance_exception_days);
		personreport_attendance_chart = (LinearLayout) view.findViewById(R.id.personreport_attendance_chart);
		personreport_attendance_detail = (Button) view.findViewById(R.id.personreport_attendance_detail);
		personreport_attendance_columchart = (BarChart) view.findViewById(R.id.personreport_attendance_columchart);
		// 管理者
		manager_report = (LinearLayout) view.findViewById(R.id.manager_report);
		managerreport_visit_plan = (LinearLayout) view.findViewById(R.id.managerreport_visit_plan);
		managerreport_yedai_count = (TextView) view.findViewById(R.id.managerreport_yedai_count);
		managerreport_visitstore_count = (TextView) view.findViewById(R.id.managerreport_visitstore_count);
		managerreport_customer_count = (TextView) view.findViewById(R.id.managerreport_customer_count);
		managerreport_everyvisit_count = (TextView) view.findViewById(R.id.managerreport_everyvisit_count);
		managerreport_visitstore_detail = (LinearLayout) view.findViewById(R.id.managerreport_visitstore_detail);
		managerreport_visitstore_columchart = (BarChart) view.findViewById(R.id.managerreport_visitstore_columchart);
		managerreport_attandance_condition = (LinearLayout) view.findViewById(R.id.managerreport_attandance_condition);
		managerreport_rest_count = (TextView) view.findViewById(R.id.managerreport_rest_count);
		managerreport_trip_count = (TextView) view.findViewById(R.id.managerreport_trip_count);
		managerreport_leave_count = (TextView) view.findViewById(R.id.managerreport_leave_count);
		managerreport_month_normal_attendance = (TextView) view
				.findViewById(R.id.managerreport_month_normal_attendance);
		managerreport_attendance_chart = (LinearLayout) view.findViewById(R.id.managerreport_attendance_chart);
		managerreport_attendance_columchart = (BarChart) view.findViewById(R.id.managerreport_attendance_columchart);
		managerreport_attendance_detail = (Button) view.findViewById(R.id.managerreport_attendance_detail);
		managerreport_trip_detail = (LinearLayout) view.findViewById(R.id.managerreport_trip_detail);
		managerreport_trip_detail_list = (ListView) view.findViewById(R.id.managerreport_trip_detail_list);
		reportMainContainer = (LinearLayout) view.findViewById(R.id.report_main_container);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	/**
	 * 初始化对应的数据展示
	 */
	private void initData() {
		mDialog = new MyProgressDialog(mContext, R.style.CustomProgressDialog, PublicUtils.getResourceString(mContext,R.string.dialog_one2));
		// 发起请求获取拜访,判断是个人还是管理者
		String url = UrlInfo.queryVisitReportTodayInfo(mContext);
		// RequestParams params =
		// searchParams(PublicUtils.receivePhoneNO(getActivity()));

		RequestParams params = searchParams(phoneNumber);
		GcgHttpClient.getInstance(mContext).post(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				// JLog.d(TAG, "onSuccess: " + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {// 有返回码
						if (obj.has("visitReport")) {// 并且有内容
							isHaveDate = true;
							JSONObject obj1 = obj.getJSONObject("visitReport");
							if ("1".equals(obj1.getString("isManager"))) {// 说明是管理者
								isManager = true;
								isPerson = false;
								JSONObject objManager = obj1.getJSONObject("manager");
								reporManager = new PhoneReportNew();
								reporManager.setTotalPerson(objManager.getInt("totalPerson") + "");// 给的Int值转为String
								reporManager.setTotalVpCnt(objManager.getInt("totalVpCnt") + "");
								reporManager.setTotalNoVisit(objManager.getInt("totalNoVisit") + "");
								reporManager.setTotalVisit(objManager.getInt("totalVisit") + "");
								reporManager.setTotalStore(objManager.getInt("totalStore") + "");
								reporManager.setAverage(objManager.getString("average"));
								if (objManager.has("topTen")) {
									isCanUseManagerReportVisitStoreChart = true;
									JSONArray array = objManager.getJSONArray("topTen");

									topTenList = new ArrayList<PhoneReportVistTopTen>();
									for (int i = 0; i < array.length(); i++) {
										PhoneReportVistTopTen everyOne = new PhoneReportVistTopTen();
										everyOne.setName(array.getJSONObject(i).getString("name"));
										everyOne.setVpCnt(array.getJSONObject(i).getInt("vpCnt") + "");// 给的Int值转为String
										everyOne.setVsCnt(array.getJSONObject(i).getInt("vsCnt") + "");
										everyOne.setXdvpCnt(array.getJSONObject(i).getInt("xdvpCnt") + "");
										topTenList.add(everyOne);

									}
									reporManager.setList(topTenList);

								}

							} else if ("0".equals(obj1.getString("isManager"))) {// 个人
								isManager = false;
								isPerson = true;
								today_visit_condition.setVisibility(View.VISIBLE);
								reportPerson = new PhoneReportVistTopTen();
								JSONObject jsonPerson = obj1.getJSONObject("person");
								reportPerson.setName(jsonPerson.getString("name"));
								reportPerson.setVpCnt(jsonPerson.getString("vpCnt"));
								reportPerson.setVsCnt(jsonPerson.getString("vsCnt"));
								reportPerson.setXdvpCnt(jsonPerson.getString("xdvpCnt"));
								reportPerson.setAverage(jsonPerson.getString("average"));
								// generatePersonMonthAttendance2();//
								// 获取个人报表所有信息

							}
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				mDialog.show();

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				// Log.e("XXXXXX", "onFinish--");
				// 获取本月出勤情况数据,并判断是个人还是管理者
				getThisMonthAttendance();

				JLog.d(TAG, "onFinish");

			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				// JLog.d(TAG, "onFailure");
				Toast.makeText(mContext, R.string.toast_one11, Toast.LENGTH_SHORT).show();
				// Log.e("XXXXXX", "onFailure--"+content);

			}
		});

	}

	/**
	 * 获取 本月出勤情况数据,管理者或者个人
	 */
	private void getThisMonthAttendance() {
		// TODO Auto-generated method stub
		String url = UrlInfo.queryWorkReportMonthInfo(mContext);
		// RequestParams params =
		// searchParams(PublicUtils.receivePhoneNO(getActivity()));
		RequestParams params = searchParams(phoneNumber);
		GcgHttpClient.getInstance(mContext).post(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				JLog.d(TAG, "onSuccess:getThisMonthAttendance");
				// Log.e("XXXXXX", "总数据--"+content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode) && obj.has("workReport")) {// 当有返回码并且有数据,但可能都为0
						isHaveDate = true;
						if (1 == obj.getInt("isWorkReport")) {// 说明有数据,且内容不都为0(默认没有数据都返回0)

							JSONObject workReport = obj.getJSONObject("workReport");
							if ("1".equals(workReport.getString("isManager"))) {// 管理者考勤
								isManager = true;
								isManagerReportAttendanceCanClick = true;
								managerreport_attandance_condition.setVisibility(View.VISIBLE);
								managerreport_attandance_condition.setOnClickListener(ReportFragment.this);// 本月出勤情况条目点击
								thisMonthAttendance = new ThisMonthAttendance();
								thisMonthAttendance.setNormal(workReport.getInt("normal"));
								thisMonthAttendance.setLate(workReport.getInt("late"));
								thisMonthAttendance.setLeaveof(workReport.getInt("leaveof"));
								thisMonthAttendance.setOther(workReport.getInt("other"));
								thisMonthAttendance.setOvertime(workReport.getInt("overtime"));
								thisMonthAttendance.setRest(workReport.getInt("rest"));
								thisMonthAttendance.setAverage(workReport.getString("average"));
								initThisMonthData();
							} else if ("0".equals(workReport.getString("isManager"))) {// 个人考勤
								isPerson = true;
								generatePersonMonthAttendance2();
							}

						}

					} else {

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				setData();

				JLog.d(TAG, "onFinish");
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				JLog.d(TAG, "onFailure");
			}
		});

	}

	/**
	 * 初始化管理者报表本月出勤情况数据
	 */
	private void initThisMonthData() {
		// TODO Auto-generated method stub
		if (thisMonthAttendance != null) {
			managerreport_rest_count.setText(thisMonthAttendance.getNormal() + "");// 正常出勤
			managerreport_trip_count.setText(thisMonthAttendance.getLate() + "");// 迟到早退
			managerreport_leave_count.setText(thisMonthAttendance.getLeaveof() + "");// 请假人次
			managerreport_month_normal_attendance.setText(thisMonthAttendance.getAverage() + "");// 正常出勤率
		}

	}

	private RequestParams searchParams(String receivePhoneNO) {
		RequestParams param = new RequestParams();
		param.put("phoneno", receivePhoneNO);
		return param;

	}

	/**
	 * 根据网络解析数据给各个组件设置值  王老板
	 */
	private void setData() {

		if (isManager && reporManager != null) {// 如果是管理者报表.则对管理者布局数据进行初始化
			managerreport_visit_plan.setVisibility(View.VISIBLE);// 管理者今日拜访计划条目显示
			managerreport_yedai_count.setText(reporManager.getTotalPerson());// 下发人数
			managerreport_visitstore_count.setText(reporManager.getTotalVisit());// 执行人数
			managerreport_everyvisit_count.setText(reporManager.getAverage());// 拜访完成率
			managerreport_customer_count.setText(reporManager.getTotalVpCnt());// 已访总数
			if (isCanUseManagerReportVisitStoreChart) {
				managerreport_visit_plan.setOnClickListener(this);// 今日拜访计划条目点击
			}

		} else if (isPerson && reportPerson != null) {// 个人报表,对个人报表布局数据进行初始化

			int vpcntInt = Integer.parseInt(reportPerson.getVpCnt());// 访店数
			int xdvpCntInt = Integer.parseInt(reportPerson.getXdvpCnt());// 下达访店总数
			float vpcnt = Float.parseFloat(reportPerson.getVpCnt());// 转为float类型
			float xdvpCnt = Float.parseFloat(reportPerson.getXdvpCnt());
			personreport_progress.setMax(xdvpCntInt);// 进度条
			personreport_progress.setProgress(vpcntInt);
			personreport_progress_count.setText(vpcntInt + "/" + xdvpCntInt); // 完成数
			// DecimalFormat mFormat = new DecimalFormat("#.##");//
			// 格式化float数据保留小数点后两位
			// float resultF = vpcnt / xdvpCnt;
			// String resultProgress = mFormat.format(resultF) + "%";
			personreport_visit_progress.setText(reportPerson.getAverage());// 完成度

		}
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		if (!isHaveDate) {
			// Toast.makeText(mContext, "未配置报表数据", Toast.LENGTH_SHORT).show();
		}

		// *********************************************************************************************************
		// 请求其他表的数据
		requestOtherData();
	}

	private void requestOtherData() {
		reportMainContainer.removeAllViews();
		String otherUrl = UrlInfo.queryOtherReportInfo(mContext);
		GcgHttpClient.getInstance(mContext).get(otherUrl, null, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
//				Log.e("View", content);
				// 解析数据,根据图表类型设置相应的图标条目
				try {
					JSONObject jsob = new JSONObject(content);
					if ("0000".equals(jsob.get("resultcode"))) {

						if (PublicUtils.isValid(jsob, "pr_report")) {
							JSONArray jsArr = jsob.getJSONArray("pr_report");
							for (int i = 0; i < jsArr.length(); i++) {
								JSONObject jsobOne = jsArr.getJSONObject(i);
								// 判断数据图标类型
								String drawType = jsobOne.getString("drawing");
								if ("1".equals(drawType)) {// 柱形图
									addOneBarChart(jsobOne);

								} else if ("2".equals(drawType)) {// 折线图
									addOneLineChart(jsobOne);
								} else if ("3".equals(drawType)) {// 饼状图
									addOnePieChart(jsobOne);

								} else if ("4".equals(drawType)) {// 漏斗图
									addOneFunnelChart(jsobOne);
								}

							}

						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStart() {
			

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 创建一个漏斗图条目
	 * 
	 * @param jsobOne
	 */
	private void addOneFunnelChart(JSONObject jsobOne) {

		ReportSales reportSales = new ReportSales(mContext);
		reportSales.initData(jsobOne);
		reportMainContainer.addView(reportSales.getView());

	}

	/**
	 * 创建一个饼状图条目
	 * 
	 * @param jsobOne
	 */
	private void addOnePieChart(JSONObject jsobOne) {
		ReportNewBusiness reportNewBusiness = new ReportNewBusiness(mContext);
		reportNewBusiness.initData(jsobOne);
		reportMainContainer.addView(reportNewBusiness.getView());
	}

	/**
	 * 创建一个折线图条目
	 * 
	 * @param jsobOne
	 */
	private void addOneLineChart(JSONObject jsobOne) {
		ReportNewClient reportNewClient = new ReportNewClient(mContext);
		reportNewClient.initData(jsobOne);
		reportMainContainer.addView(reportNewClient.getView());

	}

	/**
	 * 创建一个柱形图条目
	 * 
	 * @param jsobOne
	 */
	private void addOneBarChart(JSONObject jsobOne) {
		ReportMonthIndex reportMonthIndex = new ReportMonthIndex(mContext);
		// 设置填充数据
		reportMonthIndex.initData(jsobOne);
		reportMainContainer.addView(reportMonthIndex.getView());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.report_bar_demo:
			// 跳转至图表图片展示页面
			startActivity(new Intent(mContext, ReportPreviewActivity.class));
			break;
		case R.id.managerreport_visit_plan:// 今日拜访计划完成情况
			isManagerReportVisitStoreShow = !isManagerReportVisitStoreShow;
			if (isManagerReportVisitStoreShow) {
				managerreport_visitstore_detail.setVisibility(View.VISIBLE);
				managerreport_visitstore_columchart.setOnChartValueSelectedListener(new ValueTouchListener());
				generateTopTenData();
			} else {
				managerreport_visitstore_detail.setVisibility(View.GONE);
			}
			break;
		case R.id.managerreport_attandance_condition:// 本月出勤情况
			isManagerReportAttendanceShow = !isManagerReportAttendanceShow;
			if (isManagerReportAttendanceShow && isManagerReportAttendanceCanClick) {
				managerreport_attendance_chart.setVisibility(View.VISIBLE);
				managerreport_attendance_columchart.setOnChartValueSelectedListener(new ValueTouchListener());

				generateMonthAttandance();
			} else {
				managerreport_attendance_chart.setVisibility(View.GONE);
			}

			break;

		case R.id.personreport_attendance_statistics:// 个人本月考勤条目点击
			isPersonReportAttendanceShow = !isPersonReportAttendanceShow;
			if (isPersonReportAttendanceShow && isPersonReportAttendanceCanClick) {
				personreport_attendance_chart.setVisibility(View.VISIBLE);
				personreport_attendance_detail.setOnClickListener(this);
				personreport_attendance_columchart.setOnChartValueSelectedListener(new ValueTouchListener());

			} else {
				personreport_attendance_chart.setVisibility(View.GONE);
			}
			break;

		case R.id.personreport_attendance_detail:// 个人报表中的查看详情,跳转到考勤日历界面
			Intent intent = new Intent(mContext, AttendanceCalendarActivity.class);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}

	}

	/**
	 * 获取个人本月考勤
	 */
	private void generatePersonMonthAttendance2() {
		String url = UrlInfo.queryWorkReportMonthInfo(mContext);
		RequestParams params = searchParams(phoneNumber);
		GcgHttpClient.getInstance(mContext).post(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				// Log.e("XXXXXX", "个人数据---" + content);

				try {
					JSONObject obj1 = new JSONObject(content);
					String resultCode = obj1.getString("resultcode");

					if ("0000".equals(resultCode) && 1 == obj1.getInt("isWorkReport")) {

						JSONObject obj = obj1.getJSONObject("workReport");
						isPersonReportAttendanceCanClick = true;
						personreport_attendance_statistics.setVisibility(View.VISIBLE);// 个人本月考勤条目显示
						personreport_attendance_statistics.setOnClickListener(ReportFragment.this);// 个人报表本月考勤统计条目
						thisMonthAttendance = new ThisMonthAttendance();
						int normal = 0, late = 0, leaveof = 0, other = 0, rest = 0, overtime = 0;
						normal = obj.getInt("normal");
						late = obj.getInt("late");
						leaveof = obj.getInt("leaveof");
						rest = obj.getInt("rest");
						other = obj.getInt("other");
						overtime = obj.getInt("overtime");
						thisMonthAttendance.setNormal(normal);
						thisMonthAttendance.setLate(late);
						thisMonthAttendance.setLeaveof(leaveof);
						thisMonthAttendance.setOther(other);
						thisMonthAttendance.setRest(rest);
						thisMonthAttendance.setOvertime(overtime);
						initPersonMonthAttendance();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 个人报表本月考勤图表初始化
	 */
	private void generatePersonMonthAttendance() {
		String url = UrlInfo.queryAttendListInfo(mContext);
		RequestParams params = searchParams(phoneNumber);
		GcgHttpClient.getInstance(mContext).post(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				// Log.e("XXXXXX", "个人--" + content);
				// TODO Auto-generated method stub
				// JLog.d(TAG, "个人报表本月考勤数据获取:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");

					if ("0000".equals(resultCode)) {

						isPersonReportAttendanceCanClick = true;

						JSONObject data = obj.getJSONObject("data");
						List<AttendanceInfo> attendanceList = new CarSalesParse(mContext)
								.parserSearchAttendInfoItem(data);

						thisMonthAttendance = new ThisMonthAttendance();
						int normal = 0, late = 0, leaveof = 0, chuchai = 0, rest = 0, overtime = 0;
						// 0：考勤正常 1：迟到早退 2：休息 3：请假 9：其它
						for (AttendanceInfo attendanceInfo : attendanceList) {

							if ("0".equals(attendanceInfo.getIsExp()) || (!TextUtils.isEmpty(attendanceInfo.getInTime())
									&& !TextUtils.isEmpty(attendanceInfo.getOutTime()))) {// 给定值或者有上下班打卡记录就为正常考勤
								normal += 1;
							}
							if (!TextUtils.isEmpty(attendanceInfo.getInTimeJ())) {// 只要加班值不为空就为加班
								overtime += 1;
							}
							if ("1".equals(attendanceInfo.getIsExp())) {// 迟到早退
								late += 1;
							}
							if ("2".equals(attendanceInfo.getIsExp())) {// 休息
								rest += 1;
							}
							if ("3".equals(attendanceInfo.getIsExp())) {// 请假
								leaveof += 1;
							}
							if ("9".equals(attendanceInfo.getIsExp())) {// 其他
								chuchai += 1;
							}
						}
						// Log.e("XXXXXX",
						// ""+normal+"--"+late+"---"+leaveof+"---"+chuchai+"---"+rest+"---"+overtime);
						thisMonthAttendance.setNormal(normal);
						thisMonthAttendance.setLate(late);
						thisMonthAttendance.setLeaveof(leaveof);
						thisMonthAttendance.setOther(chuchai);
						thisMonthAttendance.setRest(rest);
						thisMonthAttendance.setOvertime(overtime);
						initPersonMonthAttendance();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 对个人报表的本月考勤初始化
	 */
	private void initPersonMonthAttendance() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (thisMonthAttendance != null) {
			// 初始化个人报表的本月考勤统计条目数据
			personreport_normal_duty.setText(thisMonthAttendance.getNormal() + "");
			personreport_late.setText(thisMonthAttendance.getLate() + "");
			personreport_other.setText(thisMonthAttendance.getOther() + "");
			personal_attendance_exception_days
					.setText(thisMonthAttendance.getLate() + thisMonthAttendance.getOther() + "");
			String[] lables = { PublicUtils.getResourceString(mContext,R.string.report_item_title5), PublicUtils.getResourceString(mContext,R.string.report_item_title6), PublicUtils.getResourceString(mContext,R.string.attendance_leave),PublicUtils.getResourceString(mContext,R.string.take_rest), PublicUtils.getResourceString(mContext,R.string.work_over) };// X轴标签
			ArrayList<String> xValues = new ArrayList<String>();
			for (int i = 0; i < lables.length; i++) {
				xValues.add(lables[i]);
			}
			// 添加Y轴的值
			ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
			yValues.add(new BarEntry(thisMonthAttendance.getNormal(), 0));
			yValues.add(new BarEntry(thisMonthAttendance.getLate(), 1));
			yValues.add(new BarEntry(thisMonthAttendance.getLeaveof(), 2));
			yValues.add(new BarEntry(thisMonthAttendance.getRest(), 3));
			yValues.add(new BarEntry(thisMonthAttendance.getOvertime(), 4));

			// y轴的数据集合
			BarDataSet barDataSet = new BarDataSet(yValues, PublicUtils.getResourceString(mContext,R.string.report_item_title10));

			// ArrayList<Integer> colors = new ArrayList<Integer>();
			// for(int i = 0;i < lables.length ;i++){
			// colors.add(Color.parseColor("#4BB8C2"));//柱形图的填充色
			// }
			// barDataSet.setColors(colors);
			barDataSet.setColor(Color.parseColor("#4BB8C2"));
			// 设置栏阴影颜色
			barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
			barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
			barDataSet.setBarSpacePercent(50f);// 设置柱子间距
			// 绘制值
			barDataSet.setDrawValues(true);
			ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
			barDataSets.add(barDataSet);
			// barDataSet.set
			BarData barData = new BarData(xValues, barDataSets);
			barData.setValueTextSize(15f);
			personreport_attendance_columchart.setVisibleXRangeMaximum(6);// 设置最大显示数,超出可滑动查看
			BarChartsUtil.showBarChart(personreport_attendance_columchart, barData, "", 0);

		}

	}

	/**
	 * 对管理者本月出勤情况柱状图进行初始化
	 */
	private void generateMonthAttandance() {
		// TODO Auto-generated method stub
		if (thisMonthAttendance != null) {
			String[] lables = { PublicUtils.getResourceString(mContext,R.string.report_item_title5), PublicUtils.getResourceString(mContext,R.string.report_item_title6), PublicUtils.getResourceString(mContext,R.string.attendance_leave),PublicUtils.getResourceString(mContext,R.string.take_rest), PublicUtils.getResourceString(mContext,R.string.work_over) };// X轴标签

			ArrayList<String> xValues = new ArrayList<String>();
			for (int i = 0; i < lables.length; i++) {
				xValues.add(lables[i]);
			}
			// 添加Y轴的值
			ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
			yValues.add(new BarEntry(thisMonthAttendance.getNormal(), 0));
			yValues.add(new BarEntry(thisMonthAttendance.getLate(), 1));
			yValues.add(new BarEntry(thisMonthAttendance.getLeaveof(), 2));
			yValues.add(new BarEntry(thisMonthAttendance.getRest(), 3));
			yValues.add(new BarEntry(thisMonthAttendance.getOvertime(), 4));
			// y轴的数据集合
			BarDataSet barDataSet = new BarDataSet(yValues, "");

			// ArrayList<Integer> colors = new ArrayList<Integer>();
			// for(int i = 0;i < lables.length ;i++){
			// colors.add(Color.parseColor("#4BB8C2"));//柱形图的填充色
			// }
			// barDataSet.setColors(colors);
			barDataSet.setColor(Color.parseColor("#4BB8C2"));
			// 设置栏阴影颜色
			barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
			barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
			barDataSet.setBarSpacePercent(50f);// 设置柱子之间的间距,必须与setVisibleXRangeMaximum(6)配合有效
			barDataSet.setDrawValues(true);
			// ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
			ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
			barDataSets.add(barDataSet);

			// 绘制值
			// barDataSet.set
			BarData barData = new BarData(xValues, barDataSet);
			barData.setValueTextSize(15f);
			managerreport_attendance_columchart.setVisibleXRangeMaximum(6);// 设置X轴上柱子最大数量,超出的可滑动查看
			managerreport_attendance_columchart.setOnChartValueSelectedListener(new ValueTouchListener());
			BarChartsUtil.showBarChart(managerreport_attendance_columchart, barData, "", -60);

		}

	}

	/**
	 * 对今日拜访计划柱状图的数据进行初始化
	 */
	private void generateTopTenData() {
		if (reporManager != null) {
			List<PhoneReportVistTopTen> list = reporManager.getList();
			// 对前十访店人员访店数进行一个降序排序
			Collections.sort(list, new Comparator<PhoneReportVistTopTen>() {

				@Override
				public int compare(PhoneReportVistTopTen lhs, PhoneReportVistTopTen rhs) {
					// TODO Auto-generated method stub
					if (Integer.parseInt(lhs.getVpCnt()) > Integer.parseInt(rhs.getVpCnt())) {
						return -1;
					}
					if (Integer.parseInt(lhs.getVpCnt()) == Integer.parseInt(rhs.getVpCnt())) {
						return 0;
					}
					return 1;
				}
			});

			ArrayList<String> xValues = new ArrayList<String>();

			ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
			for (int i = 0; i < list.size(); i++) {
				// 添加X轴的值
				xValues.add(list.get(i).getName());
				// 添加Y轴的值
				yValues.add(new BarEntry(Float.parseFloat(list.get(i).getVpCnt()), i));

			}

			BarDataSet barDataSet = new BarDataSet(yValues, "");

			// ArrayList<Integer> colors = new ArrayList<Integer>();
			// for(int i = 0;i < list.size() ;i++){
			// colors.add(Color.parseColor("#4BB8C2"));//柱形图的填充色
			// }
			// barDataSet.setColors(colors);
			barDataSet.setColor(Color.parseColor("#4BB8C2"));
			// 设置栏阴影颜色
			barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
			barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
			// 绘制值
			barDataSet.setDrawValues(true);
			ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
			barDataSets.add(barDataSet);
			// barDataSet.set
			BarData barData = new BarData(xValues, barDataSets);
			barData.setValueTextSize(15f);

			BarChartsUtil.showBarChart(managerreport_visitstore_columchart, barData, "", -60);

		}

	}

	/**
	 * 点击柱形图的监听回调
	 * 
	 * @author qingli
	 *
	 */
	private class ValueTouchListener implements OnChartValueSelectedListener {

		@Override
		public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
			// TODO Auto-generated method stub
			// Toast.makeText(getActivity(), e.getVal()+"",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected() {
			// TODO Auto-generated method stub

		}

	}
}
