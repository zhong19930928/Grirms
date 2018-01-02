package com.yunhu.yhshxc.attendance;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.attendance.calendar.CalendarCardPopupWindow;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;

import com.loopj.android.http.RequestParams;

public class SchedulingActivity extends AbsBaseActivity implements
		OnClickListener, OnFuzzyQueryListener {

	/**
	 * 自助排班模块
	 * 
	 * @author abby_zhao
	 */

	private static Button btn_choose_date;
	private static TextView tv_first_last;
	private DropDown sp_attendance;
	private static Button btn_sumbit;
	private CalendarCardPopupWindow pw;// 日期选择控件
	private List<Dictionary> list = new ArrayList<Dictionary>();
	private AttendanceUtil attendanceUtil = new AttendanceUtil();
	private String storeId = "";
	private String storeName = "";
	private String firstTime;
	private String lastTime;
	private String dateStr;
	private String nextDate;
	private Date nowTime;
	private Date date;
	private Date dateSelect;

	private String selectTime;

	private List<AttendanceSchedulingView> viewList = new ArrayList<AttendanceSchedulingView>();
	private List<AttendanceScheduling> dataList = new ArrayList<AttendanceScheduling>();
	private boolean isUpdate = false;// true 是修改，false 不是修改
	private String updateJson;// 修改的时候json数据
	private String dataName;// 修改的时候传过来的名字
	private String dataTime;// 修改是传过来的时间

	private Dialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_scheduling);
		loadingDialog = new MyProgressDialog(this,
				R.style.CustomProgressDialog, PublicUtils.getResourceString(this,R.string.init));
		loadingDialog.show();
		init();

	}

	private void initIntentData() {
		Intent intent = getIntent();
		updateJson = intent.getStringExtra("array");
		dataName = intent.getStringExtra("name");
		dataTime = intent.getStringExtra("date");
		if (!TextUtils.isEmpty(updateJson)) {
			Dictionary dictionary = new Dictionary();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCtrlCol().equals(dataName)) {
					dictionary = list.get(i);
					storeId = list.get(i).getDid();
					storeName = list.get(i).getCtrlCol();
				}
			}

			sp_attendance.setSelected(dictionary);
			pw.setUpdateDate(dataTime.replace("-", "/"));
			isUpdate = true;

		}
		initData();
	}

	private void initData() {

		try {

			LinearLayout ll = (LinearLayout) findViewById(R.id.ll_scroll_attendance);
			if (isUpdate) {
				JSONArray array = new JSONArray(updateJson);
				JLog.d("abby", "updateJson" + array.toString());
				for (int i = 0; i < array.length(); i++) {

					JSONObject obj = array.getJSONObject(i);
					AttendanceScheduling data = new AttendanceScheduling();
//					String[] str_week = { "周一", "周二", "周三", "周四", "周五", "周六",
//							"周日" };
//					data.setWeekDay(str_week[i]);
					int dayNumber = obj.getInt("weekday");
					if(dayNumber ==1 ){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week1));
					}else if(dayNumber == 2){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week2));
					}else if(dayNumber == 3){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week3));
					}
					else if(dayNumber == 4){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week4));
					}
					else if(dayNumber == 5){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week5));
					}
					else if(dayNumber == 6){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week6));
					}
					else if(dayNumber == 7){
						data.setWeekDay(PublicUtils.getResourceString(this,R.string.week7));
					}
					if(!TextUtils.isEmpty(obj.getString("date"))){
						data.setDate(obj.getString("date"));
					}
					data.setWorkStartTime(obj.getString("workTimeStart"));
					data.setWorkEndTime(obj.getString("workTimeEnd"));
					data.setLeaveStartTime(obj.getString("leaveTimeStart"));
					data.setLeaveEndTime(obj.getString("leaveTimeEnd"));

					if (String.valueOf(AttendanceScheduling.TYPE_1).equals(obj.getString("type"))) {
						data.setWorkTimeStart(obj.getString("workTimeStart")
								.substring(11, 16));
						data.setLeaveTimeEnd(obj.getString("workTimeEnd")
								.substring(11, 16));
					} else {
						data.setWorkTimeStart("09:00");
						data.setLeaveTimeEnd("18:00");
					}

					if (TextUtils.isEmpty(data.getLeaveEndTime())) {
						data.setIsHalfDay("1");
					} else {
						data.setIsHalfDay("2");
					}
					if (!TextUtils.isEmpty(obj.getString("reason"))) {
						data.setLeaveReason(obj.getString("reason"));
					}
					data.setpType(obj.getInt("type"));
					AttendanceSchedulingView View = new AttendanceSchedulingView(
							this);
					View.setAttendanceScheduling(data, true);
					ll.addView(View.getView());
					viewList.add(View);
					dataList.add(data);
				}
			} else {

				for (int i = 0; i < 7; i++) {

					AttendanceScheduling data = new AttendanceScheduling();
					String[] str_week = { PublicUtils.getResourceString(this,R.string.week1), PublicUtils.getResourceString(this,R.string.week2), PublicUtils.getResourceString(this,R.string.week3), PublicUtils.getResourceString(this,R.string.week4), PublicUtils.getResourceString(this,R.string.week5), PublicUtils.getResourceString(this,R.string.week6),
							PublicUtils.getResourceString(this,R.string.week7) };
					data.setWeekDay(str_week[i]);

					String dateStr = DateUtil.getWeekDate(
							DateUtil.getCurrentDate(), i + 1);
					data.setDate(dateStr);
					data.setWorkStartTime(dateStr + " 09:00:00");
					data.setWorkEndTime(dateStr + " 18:00:00");
					data.setLeaveStartTime(dateStr + " 09:00:00");
					data.setLeaveEndTime(dateStr + " 18:00:00");
					data.setWorkTimeStart("09:00");
					data.setLeaveTimeEnd("18:00");
					data.setIsHalfDay("1");
					data.setLeaveReason("");
					data.setpType(AttendanceScheduling.TYPE_1);
					AttendanceSchedulingView View = new AttendanceSchedulingView(
							this);
					View.setAttendanceScheduling(data, false);
					ll.addView(View.getView());
					viewList.add(View);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(loadingDialog!=null)
		loadingDialog.dismiss();
	}

	private void init() {
 
		
		sp_attendance = (DropDown) findViewById(R.id.sp_attendance);
		/**
		 * 加载下拉列表数据
		 */
		sp_attendance.initWhite();
		sp_attendance.setOnFuzzyQueryListener((OnFuzzyQueryListener) this);
		sp_attendance.setOnResultListener(resultListener);
		initSpinnerData(null);

		btn_choose_date = (Button) findViewById(R.id.btn_choose_date);
		tv_first_last = (TextView) findViewById(R.id.tv_first_last);
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);
		btn_sumbit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(storeId)) {
					Toast.makeText(getApplicationContext(), R.string.work_infos3,
							Toast.LENGTH_SHORT).show();
				} else {
					submit();

				}

			}
		});

		nowTime = DateUtil.getCurrentDate();
		String nowTimeString = DateUtil.getDateByDate(nowTime);

		btn_choose_date.setText(nowTimeString);

		pw = new CalendarCardPopupWindow(this);
		pw.setSchedulingActivity(this);
		List<String> selectDateList = new ArrayList<String>();
		selectDateList.add(pw.getSelectDate());
		pw.setSelected(selectDateList);

		btn_choose_date.setOnClickListener(listner);

		initIntentData();

	}

	private void initSpinnerData(String fuuy) {
		list = attendanceUtil.attendanceScheduingData(this, fuuy);
		sp_attendance.setSrcDictList(list);
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				storeId = dic.getDid();
				storeName = dic.getCtrlCol();
			}
		}
	};

	// 提交按钮点击事件
	private void submit() {

		JSONArray arry2 = new JSONArray();
		Boolean isSubmit = true;
		Date selectDate = new Date(pw.getSelectDate().substring(0, 10));
		firstTime = DateUtil.getFirst(selectDate);
		lastTime = DateUtil.getLast(selectDate);

		try {
			for (int i = 0; i < viewList.size(); i++) {
				JSONObject obj = new JSONObject();
				AttendanceSchedulingView view = viewList.get(i);
				AttendanceScheduling data = view.getAttendanceScheduling();

				int j = i + 1;
				String weekDay = data.getWeekDay();
				if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week1))){
					j = 1;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week2))){
					j = 2;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week3))){
					j = 3;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week4))){
					j = 4;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week5))){
					j = 5;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week6))){
					j = 6;
				}else if(weekDay.equals(PublicUtils.getResourceString(this,R.string.week7))){
					j = 7;
				}
				obj.put("weekDay", j + "");
				int type = data.getpType();
				// 0出勤 1请假 2 休息
				obj.put("type", type);
				obj.put("workTimeStart", "");
				obj.put("workTimeEnd", "");
				obj.put("isHalf", "");
				obj.put("leaveTimeStart", "");
				obj.put("leaveTimeEnd", "");
				obj.put("date", "");
				obj.put("reason", "");

//				dateStr = DateUtil.getWeekDate(selectDate, i + 1);
//				nextDate = DateUtil.getWeekDate(selectDate, i + 2);
				dateStr = DateUtil.getWeekDate(selectDate, j);
				nextDate = DateUtil.getWeekDate(selectDate, j+1);

				switch (type) {
				case AttendanceScheduling.TYPE_1:
					String work_time_start = "";
					String work_time_end = "";
					if (data.getWorkTimeStart().length() > 10) {
						work_time_start = data.getWorkTimeStart().substring(11,
								16);
					} else {
						work_time_start = data.getWorkTimeStart();
					}

					if (data.getLeaveTimeEnd().length() == 5) {
						work_time_end = data.getLeaveTimeEnd();
					} else {
						work_time_end = data.getLeaveTimeEnd()
								.substring(11, 16);
					}

					float a = Float.parseFloat(work_time_start.substring(0, 2));
					float b = Float.parseFloat(work_time_end.substring(0, 2));
					if (a <= b) {
						obj.put("workTimeStart", dateStr + " "
								+ work_time_start + ":00");
						obj.put("workTimeEnd", dateStr + " " + work_time_end
								+ ":00");
					} else {
						obj.put("workTimeStart", dateStr + " "
								+ work_time_start + ":00");
						obj.put("workTimeEnd", nextDate + " " + work_time_end
								+ ":00");
					}

					break;
				case AttendanceScheduling.TYPE_3:
					// 1全天 2 半天
					if (TextUtils.isEmpty(data.getIsHalfDay())) {
						isSubmit = false;
						Toast.makeText(SchedulingActivity.this,R.string.date_comlpe,
								Toast.LENGTH_SHORT).show();
						break;
					} else {
						if ("1".equals(data.getIsHalfDay())) {
							obj.put("isHalf", "1");
						} else {
							if ("2".equals(data.getIsHalfDay())) {
								obj.put("isHalf", "2");
								obj.put("reason", data.getLeaveReason());
								String leave_time_start = "";
								String leave_time_end = "";
								String leave_work_time_start = "";
								String leave_work_time_end = "";
								if (data.getLeaveStartTime().length() > 10) {
									leave_time_start = data.getLeaveStartTime()
											.substring(11, 16);
								} else {
									leave_time_start = data.getLeaveStartTime();
								}

								if (data.getLeaveEndTime().length() > 10) {
									leave_time_end = data.getLeaveEndTime()
											.substring(11, 16);
								} else {
									leave_time_end = data.getLeaveEndTime();
								}
								if (data.getWorkStartTime().length() > 10) {
									leave_work_time_start = data
											.getWorkStartTime().substring(11,
													16);
								} else {
									leave_work_time_start = data
											.getWorkStartTime();
								}
								if (data.getWorkEndTime().length() > 10) {
									leave_work_time_end = data.getWorkEndTime()
											.substring(11, 16);
								} else {
									leave_work_time_end = data.getWorkEndTime();
								}

								float c = Float
										.parseFloat(leave_work_time_start
												.substring(0, 2)
												+ leave_work_time_start
														.substring(3, 5));

								float d = Float.parseFloat(leave_work_time_end
										.substring(0, 2)
										+ leave_work_time_end.substring(3, 5));

								float e = Float.parseFloat(leave_time_start
										.substring(0, 2)
										+ leave_time_start.substring(3, 5));

								float f = Float.parseFloat(leave_time_end
										.substring(0, 2)
										+ leave_time_end.substring(3, 5));

								if (c < d) {
									obj.put("leaveTimeStart", dateStr + " "
											+ leave_time_start + ":00");
									obj.put("leaveTimeEnd", dateStr + " "
											+ leave_time_end + ":00");
								} else {
									obj.put("leaveTimeStart", dateStr + " "
											+ leave_time_start + ":00");
									obj.put("leaveTimeEnd", nextDate + " "
											+ leave_time_end + ":00");
								}

								// 请假时：请假开始时间 leaveTimeStart 请假结束时间 leaveTimeEnd
								// 请假时：leaveWorkTimeStart 考勤开始时间
								// leaveWorkTimeEnd 考勤结束时间

								if (e < f) {
									obj.put("workTimeStart", dateStr + " "
											+ leave_work_time_start + ":00");
									obj.put("workTimeEnd", dateStr + " "
											+ leave_work_time_end + ":00");
								} else {
									obj.put("workTimeStart", dateStr + " "
											+ leave_work_time_start + ":00");
									obj.put("workTimeEnd", nextDate + " "
											+ leave_work_time_end + ":00");
								}

							}
						}

					}
					break;
				case AttendanceScheduling.TYPE_2:
					break;
				default:
					break;
				}
				obj.put("date", dateStr);
				arry2.put(obj);
			}

			// 可提交信息
			if (isSubmit) {
				loadingDialog = new MyProgressDialog(this,
						R.style.CustomProgressDialog,PublicUtils.getResourceString(this,R.string.submint_loding));
				loadingDialog.show();
				JLog.d("abby", arry2.toString());
				submitData(arry2);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private RequestParams searchParams(JSONArray array) {

		JLog.d("abby", "startTime:" + firstTime + "endTime" + lastTime + "id"
				+ storeId + "name" + storeName);

		RequestParams param = new RequestParams();
		param.put("data", array.toString());
		param.put("startTime", firstTime);
		param.put("endTime", lastTime);
		param.put("userId", storeId);
		param.put("userName", storeName);
		return param;
	}

	// 提交数据
	private void submitData(JSONArray array) {

		String url = UrlInfo.doAttendPaiInfo(this);
		RequestParams param = searchParams(array);
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
				param, new HttpResponseListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						  if (SchedulingActivity.this!=null&&!SchedulingActivity.this.isFinishing()) {
							  loadingDialog.dismiss();
						  }
						
						JLog.d(TAG, "onSuccess:" + content);
						try {
							Toast.makeText(getApplicationContext(), R.string.submit_ok,
									Toast.LENGTH_SHORT).show();
							finish();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), R.string.ERROR_DATA,
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						if (SchedulingActivity.this!=null&&!SchedulingActivity.this.isFinishing()) {
							loadingDialog.dismiss();
						}
						
						JLog.d(TAG, "onFailure:" + content);
						Toast.makeText(getApplicationContext(), R.string.retry_net_exception,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
					}

				});

	}

	public void setDate(String time) {
		time = time.substring(0, 10);
		date = new Date(time);

		dateSelect = date;
		String firstTime = DateUtil.getFirst(date);
		String lastTime = DateUtil.getLast(date);
		SchedulingActivity.btn_choose_date.setText(time);
		SchedulingActivity.tv_first_last.setText("注:" + firstTime + "--"
				+ lastTime + "周的排班！");
	    String today = DateUtil.dateToDateString(new Date(),
				DateUtil.DATAFORMAT_STR_);
	  if(Integer.parseInt(lastTime.replaceAll("-", "")) < Integer.parseInt(today.replaceAll("/", ""))){
		  setViewList();
	  }else{
		  for(int i = 0;i < viewList.size();i++){
				AttendanceSchedulingView attendanceSchedulingView = viewList.get(i);
				String dateStr = DateUtil.getWeekDate(
						date, i + 1);
				attendanceSchedulingView.setView(dateStr,today);
				
			}  
		  SchedulingActivity.btn_sumbit.setVisibility(View.VISIBLE);
	  }
	    
	   
		
	}

	private void setViewList(){
		for(int i = 0;i < viewList.size();i++){
			AttendanceSchedulingView attendanceSchedulingView = viewList.get(i);
			attendanceSchedulingView.setViewVisibility();
		}
		if("3".equals(SharedPrefsAttendanceUtil.getInstance(this).getPaiFlg()) || "4".equals(SharedPrefsAttendanceUtil.getInstance(this).getPaiFlg())){
			btn_sumbit.setVisibility(View.VISIBLE);
		}else{
			btn_sumbit.setVisibility(View.GONE);
		}
		
	}
	
	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_choose_date:
				pw.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onTextChanged(CharSequence s) {
		initSpinnerData(s.toString());
	}

}
