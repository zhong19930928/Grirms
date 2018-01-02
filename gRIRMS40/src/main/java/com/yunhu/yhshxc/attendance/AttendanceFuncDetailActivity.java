package com.yunhu.yhshxc.attendance;

import gcg.org.debug.JLog;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.FuncDetailActivity;
import com.yunhu.yhshxc.attendance.backstage.AttendanceLocationService;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

public class AttendanceFuncDetailActivity extends FuncDetailActivity {

	private String attendanceStart, attendanceEnd;// 旧考勤上班时间和下班时间
	private String newAttendanceStart, newAttendanceEnd, newAttendanceStartOver, newAttendanceEndOver;// 新考勤上班时间和下班时间
																										// 加班上班时间和加班下班时间
	private String entryFuncTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entryFuncTime = getIntent().getStringExtra("entryFuncTime");
	}

	@Override
	public void onClick(View v) {
		submitDataLayout.setEnabled(false);
		switch (v.getId()) {
		case R.id.ll_func_show_detail_data: // 提交数据
			if (menuType == Menu.TYPE_ATTENDANCE) {// 表示考勤操作
				if (usableControl()) {
					submitAttendance();
				}
			} else if (menuType == Menu.TYPE_NEW_ATTENDANCE) {// 新考勤操作
				// if (usableControl()) {
				// submitNewAttendance();
				// }
				setNewAttendTime();
			}
			break;
		case R.id.editPhoto_btn:
			editPhoto();// 照片编辑
			break;

		default:
			break;
		}
	}

	MyTimerTask task;

	private void setNewAttendTime() {
		flag = true;
		timer = new Timer(true);
		if (task == null) {
			task = new MyTimerTask();
			timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
		} else {
			if (task.cancel()) {
				timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
			}
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				Date date = PublicUtils.getNetDate();
				;
				Message msg = Message.obtain();
				msg.obj = date;
				handler.sendMessage(msg);
			}
		}).start();

	}

	Timer timer;

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.sendEmptyMessage(1);

		}

	}

	private boolean flag = true;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flag) {
				flag = false;
				if (task != null) {
					task.cancel();
					task = null;
				}
				if (timer != null) {
					timer.cancel();
				}

				Date date = (Date) msg.obj;
				if (attState == Constants.NEW_ATTENDANCE_START || attState == Constants.NEW_ATTENDANCE_END
						|| attState == Constants.NEW_ATTENDANCE_GANG) {
					if (null != date) {

						if (usableControl(date)) {
							submitNewAttendance(date);
						}
					} else {
						Toast.makeText(AttendanceFuncDetailActivity.this, R.string.check_net_time, Toast.LENGTH_LONG).show();
						submitDataLayout.setEnabled(true);
					}
				} else if (attState == Constants.NEW_ATTENDANCE_OVER_START
						|| attState == Constants.NEW_ATTENDANCE_OVER_END) {
					if (null != date) {
						if (usableControl(date)) {
							submitNewAttendance(date);
						}
					} else {
						Toast.makeText(AttendanceFuncDetailActivity.this, R.string.check_net_time, Toast.LENGTH_LONG).show();
						submitDataLayout.setEnabled(true);
					}
				}

			}
		};
	};

	@Override
	public void submitReceive(boolean isSuccess) {
		submitDataLayout.setEnabled(true);
		if (isSuccess) {
			SharedPreferencesUtil.getInstance(this).setCacheAttendanceData(null);
			submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
			if (menuType == Menu.TYPE_ATTENDANCE) {
				if (attState == Constants.ATTENDANCE_STATE_START) {
					SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
							.setStartWorkTime(attendanceStart + "do");
				} else if (attState == Constants.ATTENDANCE_STATE_STOP) {
					SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
							.setStopWorkTime(attendanceEnd + "do");
				}
			}
			if (menuType == Menu.TYPE_NEW_ATTENDANCE) {
				if (attState == Constants.NEW_ATTENDANCE_START) {
					if (!PublicUtils.ISDEMO) {
						SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
								.setNewAttendanceStart(newAttendanceStart);
					} else {
						SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
								.setNativeAttendanceStart(newAttendanceStart);

					}
					AttendanceUtil.startAttendanceLocationService(AttendanceFuncDetailActivity.this,
							AttendanceLocationService.WORK_START);
				} else if (attState == Constants.NEW_ATTENDANCE_END) {

					if (!PublicUtils.ISDEMO) {

						SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
								.setNewAttendanceEnd(newAttendanceEnd);
					} else {
						SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
								.setNativeAttendanceEnd(newAttendanceEnd);
					}
					AttendanceUtil.startAttendanceLocationService(AttendanceFuncDetailActivity.this,
							AttendanceLocationService.WORK_END);
				} else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {
					SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
							.setNewAttendanceStartOver(newAttendanceStartOver);
				} else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
					SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this)
							.setNewAttendanceEndOver(newAttendanceEndOver);
				}
			}
			thisFinish(R.id.submit_succeeded, null);
		} else {
			if (submit != null) {
				submit.setState(Submit.STATE_NO_SUBMIT);
				submitDB.updateSubmit(submit);
				submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
				paraseCacheAttendanceData();
			}
		}
	}

	@Override
	public String contentDescription() {
		String contentDescription = "";
		// int targetId = submit.getTargetid();
		// Menu menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		if (menuType == Menu.TYPE_ATTENDANCE) {// 表示考勤操作
			if (attState == Constants.ATTENDANCE_STATE_START) {
				contentDescription = PublicUtils.getResourceString(this,R.string.s_work);
			} else if (attState == Constants.ATTENDANCE_STATE_STOP) {
				contentDescription = PublicUtils.getResourceString(this,R.string.e_work);
			}
		} else if (menuType == Menu.TYPE_NEW_ATTENDANCE) {// 新考勤操作
			if (attState == Constants.NEW_ATTENDANCE_START) {
				contentDescription = PublicUtils.getResourceString(this,R.string.s_work)+"(" + newAttendanceStart + ")";// 后面跟的时间是确定唯一使用都传上班时间
			} else if (attState == Constants.NEW_ATTENDANCE_END) {
				contentDescription = PublicUtils.getResourceString(this,R.string.e_work)+"(" + newAttendanceStart + ")";
			} else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {
				String overName = SharedPreferencesUtil.getInstance(this).getNewAttendanceOverName();
				contentDescription = overName + PublicUtils.getResourceString(this,R.string.s_work) + "(" + newAttendanceStartOver + ")";
			} else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
				String overName = SharedPreferencesUtil.getInstance(this).getNewAttendanceOverName();
				contentDescription = overName + PublicUtils.getResourceString(this,R.string.e_work) + "(" + newAttendanceStartOver + ")";
			}

		}
		return contentDescription;
	}

	private boolean attendanceMust(List<SubmitItem> list) {
		boolean flag = true;
		// 1：拍照、2：定位、3：说明
		String attendanceMust = SharedPreferencesUtil.getInstance(this).getAttendanceMustDo();
		if (TextUtils.isEmpty(attendanceMust)) {
			if (list.size() < 1) {// 考勤信息不完全
				ToastOrder.makeText(AttendanceFuncDetailActivity.this, R.string.check_net_time2, ToastOrder.LENGTH_LONG).show();
				flag = false;
			}
		} else {
			if (attendanceMust.contains("1")) {// 拍照
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-2", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue())) {
					ToastOrder.makeText(this, R.string.check_net_time3, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
			if (attendanceMust.contains("2")) {// 定位
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-1", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue())) {
					ToastOrder.makeText(this,  R.string.check_net_time4, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
			if (attendanceMust.contains("3")) {// 说明
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-3", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue().trim())) {
					ToastOrder.makeText(this,  R.string.check_net_time5, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
		}
		if (!flag) {
			submitDataLayout.setEnabled(true);
		}
		return flag;
	}

	/**
	 * 提交考勤
	 */
	private void submitAttendance() {
		isNoWait = SharedPreferencesUtil.getInstance(this).getAttendWait() == 1 ? true : false;// 考勤的时候是否无等待从SharedPreferencesUtil获取
		List<SubmitItem> attSubmitItems = submitItemDB.findSubmitItemBySubmitId(submitId);
		if (attendanceMust(attSubmitItems)) {
			cacheAttendanceData(attSubmitItems);
			submit.setState(Submit.STATE_SUBMITED);
			submitDB.updateState(submit);
			submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
			SubmitItem attTimeItem = new SubmitItem();
			if (attState == Constants.ATTENDANCE_STATE_START) {// 上班
				attendanceStart = DateUtil.getCurDateTime();
				insertSubmitItem(attTimeItem, Constants.IN_TIME, attendanceStart);
				// 地址$经度$纬度$时间$定位方式
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 上班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.IN_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.IN_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.IN_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 上班拍照
						insertSubmitItem(submitItem, Constants.IN_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 上班说明
						insertSubmitItem(submitItem, Constants.IN_COMMENT, submitItem.getParamValue());
					}
				}

			} else if (attState == Constants.ATTENDANCE_STATE_STOP) {// 下班
				// attendanceEnd = DateUtil.getCurDateTime();
				attendanceEnd = entryFuncTime;
				insertSubmitItem(attTimeItem, Constants.OUT_TIME, attendanceEnd);
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 下班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.OUT_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.OUT_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.OUT_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.OUT_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 下班拍照
						insertSubmitItem(submitItem, Constants.OUT_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 下班说明
						insertSubmitItem(submitItem, Constants.OUT_COMMENT, submitItem.getParamValue());
					}
				}
			}

			Constants.ISCHECKOUTMODUL = true;
			submit();
		}
	}

	/**
	 * 提交新考勤信息
	 */
	private void submitNewAttendance(Date date) {
		isNoWait = SharedPreferencesUtil.getInstance(this).getOverAttendWait() == 1 ? true : false;// 考勤的时候是否无等待从SharedPreferencesUtil获取
		List<SubmitItem> attSubmitItems = submitItemDB.findSubmitItemBySubmitId(submitId);
		if (attendanceMust(attSubmitItems)) {
			cacheAttendanceData(attSubmitItems);
			JLog.d("考勤数据缓存成功");
			submit.setState(Submit.STATE_SUBMITED);
			submitDB.updateState(submit);
			JLog.d("考勤数据更新成功");
			submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
			JLog.d("考勤原始数据删除成功");
			SubmitItem attTimeItem = new SubmitItem();
			if (attState == Constants.NEW_ATTENDANCE_START) {// 上班
				// newAttendanceStart = DateUtil.getCurDateTime();
				newAttendanceStart = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
				insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStart);
				// 考勤类型
				SubmitItem attendtype = new SubmitItem();
				insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "1");
				if (PublicUtils.isBeLate(this, newAttendanceStart) > 0) {
					// 考勤上班迟到
					SubmitItem in_result_type = new SubmitItem();
					insertSubmitItem(in_result_type, Constants.NEW_IN_RESULT_TYPE, "1");
					// 迟到时间
					SubmitItem lateItem = new SubmitItem();
					insertSubmitItem(lateItem, Constants.NEW_LATE_DURATION,
							String.valueOf(PublicUtils.isBeLate(this, newAttendanceStart)));
				}
				// 考勤时间
				SubmitItem attendTime = new SubmitItem();
				// insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
				// newAttendanceStart);
				insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
						SharedPreferencesUtil.getInstance(this).getNewAttendTime());
				// 地址$经度$纬度$时间$定位方式
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 上班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 上班拍照
						insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 上班说明
						insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
					}
				}

			} else if (attState == Constants.NEW_ATTENDANCE_END) {// 下班
				// newAttendanceEnd = DateUtil.getCurDateTime();
				// newAttendanceEnd = entryFuncTime;
				newAttendanceEnd = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
				insertSubmitItem(attTimeItem, Constants.NEW_OUT_TIME, newAttendanceEnd);
				// 考勤类型
				SubmitItem attendtype = new SubmitItem();
				insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "1");
				if (PublicUtils.isLeaveEarly(this, newAttendanceEnd) > 0) {
					// 考勤下班早退
					SubmitItem out_result_type = new SubmitItem();
					insertSubmitItem(out_result_type, Constants.NEW_OUT_RESULT_TYPE, "1");
					// 早退时间
					SubmitItem leaveEarlyItem = new SubmitItem();
					insertSubmitItem(leaveEarlyItem, Constants.NEW_LEAVEEARLY_DURATION,
							String.valueOf(PublicUtils.isLeaveEarly(this, newAttendanceEnd)));
				}
				// 考勤时间
				SubmitItem attendTime = new SubmitItem();
				// insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
				// SharedPreferencesUtil.getInstance(this).getNewAttendanceStart());
				insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
						SharedPreferencesUtil.getInstance(this).getNewAttendTime());
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 下班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.NEW_OUT_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 下班拍照
						insertSubmitItem(submitItem, Constants.NEW_OUT_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 下班说明
						insertSubmitItem(submitItem, Constants.NEW_OUT_COMMENT, submitItem.getParamValue());
					}
				}
			} else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {// 上班
				// newAttendanceStartOver = DateUtil.getCurDateTime();
				newAttendanceStartOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
				insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStartOver);
				// 考勤类型
				SubmitItem attendtype = new SubmitItem();
				insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
				// 考勤时间
				SubmitItem attendTime = new SubmitItem();
				// insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
				// newAttendanceStartOver);
				insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
						SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
				// 地址$经度$纬度$时间$定位方式
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 上班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 上班拍照
						insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 上班说明
						insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
					}
				}

			} else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
				// 下班
				// newAttendanceEndOver = DateUtil.getCurDateTime();
				// newAttendanceEndOver = entryFuncTime;
				newAttendanceEndOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
				insertSubmitItem(attTimeItem, Constants.NEW_OUT_TIME, newAttendanceEndOver);
				// 考勤类型
				SubmitItem attendtype = new SubmitItem();
				insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
				// 考勤时间
				SubmitItem attendTime = new SubmitItem();
				// insertSubmitItem(attendTime,Constants.NEW_ATTEND_TIME,
				// SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this).getNewAttendanceStartOver());
				insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
						SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 下班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.NEW_OUT_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
					} else if (submitItem.getParamName().equals("-2")) {// 下班拍照
						insertSubmitItem(submitItem, Constants.NEW_OUT_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 下班说明
						insertSubmitItem(submitItem, Constants.NEW_OUT_COMMENT, submitItem.getParamValue());
					}
				}

			}

			else if (attState == Constants.NEW_ATTENDANCE_GANG) {// 报岗
				newAttendanceStartOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
				// newAttendanceStartOver = DateUtil.getCurDateTime();
				insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStartOver);
				// 考勤类型
				SubmitItem attendtype = new SubmitItem();
				// insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
				// 考勤时间
				SubmitItem attendTime = new SubmitItem();
				// insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
				// newAttendanceStartOver);
				// insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
				// SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
				// 地址$经度$纬度$时间$定位方式
				for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
					submitItem.setId(null);// 设置item的id为空
					if (submitItem.getParamName().equals("-1")) {// 上班定位
						String[] locationInfo = submitItem.getParamValue().split("\\$");
						insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
						insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
						insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
						insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
						insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
						insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
						insertSubmitItem(submitItem, "type", "gang");
					} else if (submitItem.getParamName().equals("-2")) {// 上班拍照
						insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
					} else if (submitItem.getParamName().equals("-3")) {// 上班说明
						insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
					}
				}
			}

			else {
				JLog.d("考勤类型:" + attState);
			}
			Constants.ISCHECKOUTMODUL = true;
			submit();
		}
	}

	private void insertSubmitItem(SubmitItem submitItem, String paramName, String paramValue) {
		if (submitItem != null) {
			submitItem.setSubmitId(submitId);
			submitItem.setParamName(paramName);
			submitItem.setParamValue(paramValue);
			submitItemDB.insertSubmitItem(submitItem,false);
		}
	}

	/**
	 * 临时缓存考勤要上报的数据，即时提交的时候使用
	 * 
	 * @param list
	 */
	private void cacheAttendanceData(List<SubmitItem> list) {
		try {
			if (list != null && list.size() > 0) {
				JSONArray array = new JSONArray();
				JSONObject obj = null;
				for (int i = 0; i < list.size(); i++) {
					obj = new JSONObject();
					SubmitItem item = list.get(i);
					obj.put("submitId", item.getSubmitId());
					obj.put("paramName", TextUtils.isEmpty(item.getParamName()) ? "" : item.getParamName());
					obj.put("paramValue", TextUtils.isEmpty(item.getParamValue()) ? "" : item.getParamValue());
					obj.put("type", item.getType());
					obj.put("orderId", item.getOrderId());
					obj.put("note", TextUtils.isEmpty(item.getNote()) ? "" : item.getNote());
					array.put(obj);
				}
				SharedPreferencesUtil.getInstance(this).setCacheAttendanceData(array.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析缓存的考勤数据存储到数据库
	 */
	private void paraseCacheAttendanceData() {
		try {
			String cacheJson = SharedPreferencesUtil.getInstance(this).getCacheAttendanceData();
			if (!TextUtils.isEmpty(cacheJson)) {
				JSONArray array = new JSONArray(cacheJson);
				JSONObject obj = null;
				SubmitItem item = null;
				for (int i = 0; i < array.length(); i++) {
					obj = array.getJSONObject(i);
					item = new SubmitItem();
					item.setSubmitId(obj.getInt("submitId"));
					item.setParamName(obj.getString("paramName"));
					item.setParamValue(obj.getString("paramValue"));
					item.setType(obj.getInt("type"));
					item.setOrderId(obj.getInt("orderId"));
					item.setNote(obj.getString("note"));
					submitItemDB.insertSubmitItem(item,false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("attendanceStart", attendanceStart);
		outState.putString("attendanceEnd", attendanceEnd);
		outState.putString("newAttendanceStart", newAttendanceStart);
		outState.putString("newAttendanceEnd", newAttendanceEnd);
		outState.putString("newAttendanceStartOver", newAttendanceStartOver);
		outState.putString("newAttendanceEndOver", newAttendanceEndOver);
		outState.putString("entryFuncTime", entryFuncTime);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		attendanceStart = savedInstanceState.getString("attendanceStart");
		newAttendanceEnd = savedInstanceState.getString("newAttendanceEnd");
		newAttendanceStart = savedInstanceState.getString("newAttendanceStart");
		newAttendanceEnd = savedInstanceState.getString("newAttendanceEnd");
		newAttendanceStartOver = savedInstanceState.getString("newAttendanceStartOver");
		newAttendanceEndOver = savedInstanceState.getString("newAttendanceEndOver");
		entryFuncTime = savedInstanceState.getString("entryFuncTime");
	}

}
