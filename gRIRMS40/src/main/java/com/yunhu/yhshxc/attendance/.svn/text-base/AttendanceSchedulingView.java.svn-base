package com.yunhu.yhshxc.attendance;

import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.calendar.CalendarCardPopupWindow;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

public class AttendanceSchedulingView {
	private View view;
	private TextView tv_weekday;
	private LinearLayout attendance_item_worktime;
	private LinearLayout attendance_item_leave;
	private AttendanceScheduling attendanceSchedulingA = new AttendanceScheduling();
	private Spinner sp_attendance;
	private Context context;
	private Button btn_worktime;
	private Button btn_offtime;
	private RadioButton btn_all_day;
	private RadioButton btn_half_day;
	private RadioGroup rg_aaaa;

	private int year;// 年
	private int month;// 月
	private int day;// 日
	private int hour;// 小时
	private int minutes;// 分钟
	private int selectType;// 有范围的时间控件选中类型 0是开始时间 1是结束时间

	private CalendarCardPopupWindow pw;
	private String selectTime;

	private Button btn_start_time;
	private Button btn_end_time;
	private Button btn_work_start_time;
	private Button btn_work_end_time;
	private EditText edit_reason;

	private String LeaveStartTime = "";
	private String LeaveEndTime = "";
	private String LeaveWorkStartTime = "";
	private String LeaveWorkEndTime = "";

	private Boolean isUpdate = false;
	private String reason = "";
	private String updateIsHalf = "1";// 全天是1半天是2
	private   String today = DateUtil.dateToDateString(new Date(),
			DateUtil.DATAFORMAT_STR_);
	private Boolean ifChoose = true;
	
	
	public AttendanceSchedulingView(Context context) {
		this.context = context;
		view = View.inflate(context,
				R.layout.activity_attendtance_scheduling_item, null);
		rg_aaaa = (RadioGroup) view.findViewById(R.id.rg_aaaa);
		tv_weekday = (TextView) view.findViewById(R.id.tv_weekday);
		sp_attendance = (Spinner) view.findViewById(R.id.sp_attendance);

		
		attendance_item_worktime = (LinearLayout) view
				.findViewById(R.id.attendance_item_worktime);
		attendance_item_leave = (LinearLayout) view
				.findViewById(R.id.attendance_item_leave);

		btn_worktime = (Button) view.findViewById(R.id.tv_worktime);
		btn_offtime = (Button) view.findViewById(R.id.btn_offtime);
		btn_half_day = (RadioButton) view.findViewById(R.id.btn_half_day);
		btn_all_day = (RadioButton) view.findViewById(R.id.btn_all_day);
	}

	private void setSpinnerEnable(){
		
		if("3".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg()) || "4".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg())){
			sp_attendance.setEnabled(true);
			sp_attendance.setBackgroundResource(R.drawable.pic_attend_select);
			ifChoose = true;
		}else{
			 if(!TextUtils.isEmpty(attendanceSchedulingA.getDate())){
			    	if(Integer.parseInt(attendanceSchedulingA.getDate().replaceAll("-", "")) <= Integer.parseInt(today.replaceAll("/", ""))){
			    		sp_attendance.setEnabled(false);
			    		sp_attendance.setBackgroundResource(R.drawable.btn_attendance_select);
			    		ifChoose = false;
			    		
					}else{
						sp_attendance.setEnabled(true);
						sp_attendance.setBackgroundResource(R.drawable.pic_attend_select);
						ifChoose = true;
					}
			    }
			
		}
	}

	
	
	private void setRadioButtonState(boolean isHalf) {
		if (isHalf) {// 半天
			btn_all_day.setButtonDrawable(R.drawable.rb_attendance);
			btn_half_day.setButtonDrawable(R.drawable.rb_attendance_select);
		} else {
			btn_all_day.setButtonDrawable(R.drawable.rb_attendance_select);
			btn_half_day.setButtonDrawable(R.drawable.rb_attendance);
		}

	}
	
	public void setViewVisibility(){

		if("3".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg()) || "4".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg())){
			sp_attendance.setEnabled(true);
			sp_attendance.setBackgroundResource(R.drawable.pic_attend_select);
			ifChoose = true;
		}else{
			sp_attendance.setEnabled(false);
			sp_attendance.setBackgroundResource(R.drawable.btn_attendance_select);
			ifChoose = false;
		}
	
	}
	
	public void setView(String weekTime,String nowTime){
		
		if("3".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg()) || "4".equals(SharedPrefsAttendanceUtil.getInstance(context).getPaiFlg())){
			sp_attendance.setEnabled(true);
			sp_attendance.setBackgroundResource(R.drawable.pic_attend_select);
			ifChoose = true;
		}else{
			if(Integer.parseInt(weekTime.replace("-", "")) <= Integer.parseInt(nowTime.replace("/", ""))){
				sp_attendance.setEnabled(false);
				sp_attendance.setBackgroundResource(R.drawable.btn_attendance_select);
				ifChoose = false;
			}else{
				sp_attendance.setEnabled(true);
				sp_attendance.setBackgroundResource(R.drawable.pic_attend_select);
				ifChoose = true;
			}
		}
	}

	public View getView() {
		return view;
	}

	public AttendanceScheduling getAttendanceScheduling() {
		return attendanceSchedulingA;
	}

	public void setDateSelect(String time) {
		selectTime = time;
		attendanceSchedulingA.setDate(selectTime);

	}

	private void initData(AttendanceScheduling attendanceScheduling) {
		int dataType = attendanceScheduling.getpType();
		if (AttendanceScheduling.TYPE_1 == dataType) {
			sp_attendance.setSelection(0);
		}else if (AttendanceScheduling.TYPE_2 == dataType) {
			sp_attendance.setSelection(2);
		}else if (AttendanceScheduling.TYPE_3 == dataType) {
			sp_attendance.setSelection(1);

		}
		switch (dataType) {
		case AttendanceScheduling.TYPE_1:// 出勤
			String time = attendanceScheduling.getWorkStartTime();
			if (!TextUtils.isEmpty(time)) {
				time = time.substring(11, 16);
				btn_worktime.setText(time);
			}

			String endTime = attendanceScheduling.getWorkEndTime();
			if (!TextUtils.isEmpty(endTime)) {
				endTime = endTime.substring(11, 16);
				btn_offtime.setText(endTime);
			}
			break;
		case AttendanceScheduling.TYPE_3:// 请假
			isUpdate = true;
			// String leaveEndTime_1 = attendanceScheduling.getLeaveEndTime();
			if ("1".equals(attendanceScheduling.getIsHalfDay())) {
				updateIsHalf = "1";
				setRadioButtonState(false);

			} else {
				updateIsHalf = "2";

				setRadioButtonState(true);
				reason = attendanceScheduling.getLeaveReason();

				LeaveStartTime = attendanceScheduling.getLeaveStartTime()
						.substring(11, 16);
				LeaveEndTime = attendanceScheduling.getLeaveEndTime()
						.substring(11, 16);
				LeaveWorkStartTime = attendanceScheduling.getWorkStartTime()
						.substring(11, 16);
				LeaveWorkEndTime = attendanceScheduling.getWorkEndTime()
						.substring(11, 16);

			}

			break;

		case AttendanceScheduling.TYPE_2:// 休息
			break;

		default:
			break;
		}

	}

	public void setAttendanceScheduling(
			AttendanceScheduling attendanceScheduling, Boolean isUpadate) {
		this.attendanceSchedulingA = attendanceScheduling;
		setSpinnerEnable();
		tv_weekday.setText(attendanceScheduling.getWeekDay());

		String[] str = { PublicUtils.getResourceString(context,R.string.work_infos2), PublicUtils.getResourceString(context,R.string.attendance_leave), PublicUtils.getResourceString(context,R.string.take_rest) };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.attendance_spinner, str);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_attendance.setAdapter(adapter);

		if (attendanceScheduling.getpType() == 0) {
			btn_worktime.setText(attendanceScheduling.getWorkStartTime()
					.substring(11, 16));
			btn_offtime.setText(attendanceScheduling.getWorkEndTime()
					.substring(11, 16));
		} else {
			btn_worktime.setText(attendanceScheduling.getWorkTimeStart());
			btn_offtime.setText(attendanceScheduling.getLeaveTimeEnd());
		}

		btn_worktime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ifChoose){
					timeDialog(btn_worktime, 0);
				}
			}
		});

		btn_offtime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ifChoose){
					timeDialog(btn_offtime, 1);
				}
			
			}
		});

		btn_all_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ifChoose){
					setRadioButtonState(false);
					attendanceSchedulingA.setIsHalfDay("1");
				}
				
			}
		});

		btn_half_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ifChoose){
					askForLeave();
				}
				
			}
		});

		sp_attendance.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				switch (position) {
				case 0:// 出勤.
					attendance_item_worktime.setVisibility(View.VISIBLE);
					attendance_item_leave.setVisibility(View.GONE);
					attendanceSchedulingA.setpType(AttendanceScheduling.TYPE_1);

					btn_worktime.setText(attendanceSchedulingA
							.getWorkTimeStart());
					btn_offtime.setText(attendanceSchedulingA.getLeaveTimeEnd());

					break;
				case 1:// 请假
					attendance_item_worktime.setVisibility(View.GONE);
					attendance_item_leave.setVisibility(View.VISIBLE);

					attendanceSchedulingA.setpType(AttendanceScheduling.TYPE_3);
					// if ("1".equals(attendanceSchedulingA.getIsHalfDay())) {
					if ("1".equals(attendanceSchedulingA.getIsHalfDay())) {
						setRadioButtonState(false);
					}else{
						setRadioButtonState(true);
					}

					break;
				case 2:// 休息
					attendance_item_worktime.setVisibility(View.INVISIBLE);
					attendance_item_leave.setVisibility(View.GONE);
					attendanceSchedulingA.setpType(AttendanceScheduling.TYPE_2);
					break;
				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		if (isUpadate) {
			initData(attendanceScheduling);
		}
	}

	private void askForLeave() {

		final Dialog dialog = new Dialog(context, R.style.transparentDialog);

		View view = View.inflate(context, R.layout.report_time_dialog, null);
		Button confirmBtn = (Button) view
				.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn = (Button) view
				.findViewById(R.id.report_dialog_cancelBtn);

		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		View convertView = View.inflate(context, R.layout.ask_for_off_dialog,
				null);
		btn_start_time = (Button) convertView.findViewById(R.id.btn_start_time);
		btn_end_time = (Button) convertView.findViewById(R.id.btn_end_time);
		btn_work_start_time = (Button) convertView
				.findViewById(R.id.btn_work_start_time);
		btn_work_end_time = (Button) convertView
				.findViewById(R.id.btn_work_end_time);
		edit_reason = (EditText) convertView.findViewById(R.id.edit_reason);

		if (!TextUtils.isEmpty(attendanceSchedulingA.getLeaveStartTime())
				&& attendanceSchedulingA.getLeaveStartTime().length() > 5) {
			btn_start_time.setText(attendanceSchedulingA.getLeaveStartTime()
					.substring(11, 16));
		} else {
			if (TextUtils.isEmpty(attendanceSchedulingA.getLeaveStartTime())) {
				btn_start_time.setText("09:00");
			} else {
				btn_start_time.setText(attendanceSchedulingA
						.getLeaveStartTime());
			}

		}
		if (!TextUtils.isEmpty(attendanceSchedulingA.getLeaveEndTime())
				&& attendanceSchedulingA.getLeaveEndTime().length() > 5) {
			btn_end_time.setText(attendanceSchedulingA.getLeaveEndTime()
					.substring(11, 16));
		} else {
			if (TextUtils.isEmpty(attendanceSchedulingA.getLeaveEndTime())) {
				btn_end_time.setText("18:00");
			} else {
				btn_end_time.setText(attendanceSchedulingA.getLeaveEndTime());
			}

		}
		if (!TextUtils.isEmpty(attendanceSchedulingA.getWorkStartTime())
				&& attendanceSchedulingA.getWorkStartTime().length() > 5) {
			btn_work_start_time.setText(attendanceSchedulingA
					.getWorkStartTime().substring(11, 16));
		} else {
			if (TextUtils.isEmpty(attendanceSchedulingA.getWorkStartTime())) {
				btn_work_start_time.setText("09:00");
			} else {
				btn_work_start_time.setText(attendanceSchedulingA
						.getWorkStartTime());
			}

		}
		if (!TextUtils.isEmpty(attendanceSchedulingA.getWorkEndTime())
				&& attendanceSchedulingA.getWorkEndTime().length() > 5) {
			btn_work_end_time.setText(attendanceSchedulingA.getWorkEndTime()
					.substring(11, 16));
		} else {
			if (TextUtils.isEmpty(attendanceSchedulingA.getWorkEndTime())) {
				btn_work_end_time.setText("18:00");
			} else {
				btn_work_end_time.setText(attendanceSchedulingA
						.getWorkEndTime());
			}

		}
		if (!TextUtils.isEmpty(attendanceSchedulingA.getLeaveReason())) {
			edit_reason.setText(attendanceSchedulingA.getLeaveReason());
		}

		btn_work_start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeDialog(btn_work_start_time, 2);
			}
		});

		btn_work_end_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				timeDialog(btn_work_end_time, 3);
			}
		});

		btn_end_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeDialog(btn_end_time, 4);
			}
		});

		btn_start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeDialog(btn_start_time, 5);
			}
		});
		ll.addView(convertView);

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isUpdate = false;
				attendanceSchedulingA.setLeaveStartTime(btn_start_time
						.getText().toString());
				attendanceSchedulingA.setLeaveEndTime(btn_end_time.getText()
						.toString());
				attendanceSchedulingA.setWorkStartTime(btn_work_start_time
						.getText().toString());
				attendanceSchedulingA.setWorkEndTime(btn_work_end_time
						.getText().toString());

				if (TextUtils.isEmpty(edit_reason.getText().toString())) {
					attendanceSchedulingA.setLeaveReason("");
				} else {
					attendanceSchedulingA.setLeaveReason(edit_reason.getText()
							.toString());
				}
				String workStartTime = attendanceSchedulingA.getWorkStartTime();
				String workEndTime = attendanceSchedulingA.getWorkEndTime();
				if(!TextUtils.isEmpty(workStartTime)&&!TextUtils.isEmpty(workEndTime)){
					if(comPareDate(workStartTime, workEndTime)>0){
						Toast.makeText(context, R.string.work_infos, Toast.LENGTH_SHORT).show();
						return;
					}
				}
				String leaveStartTime = attendanceSchedulingA.getLeaveStartTime();
				String leaveEndTime = attendanceSchedulingA.getLeaveEndTime();
				if(!TextUtils.isEmpty(leaveStartTime)&&!TextUtils.isEmpty(leaveEndTime)){
					if(comPareDate(leaveStartTime, leaveEndTime)>0){
						Toast.makeText(context, R.string.work_infos1, Toast.LENGTH_LONG).show();
						return;
					}
				}
				dialog.dismiss();
				attendanceSchedulingA.setIsHalfDay("2");
				setRadioButtonState(true);

			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();

	}

	private String time = "";

	private void timeDialog(final Button btn_time, final int i) {

		final Dialog dialog = new Dialog(context, R.style.transparentDialog);

		hour = Integer.parseInt(btn_time.getText().toString().substring(0, 2));
		minutes = Integer.parseInt(btn_time.getText().toString()
				.substring(3, 5));

		View view = View.inflate(context, R.layout.report_time_dialog, null);
		Button confirmBtn = (Button) view
				.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn = (Button) view
				.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (i) {
				case 0:
					// attendanceSchedulingA.setWorkStartTime(time);
					attendanceSchedulingA.setWorkTimeStart(time);
					break;
				case 1:
					attendanceSchedulingA.setLeaveTimeEnd(time);
					break;
				default:
					break;
				}

				btn_time.setText(time);
				
				String start = attendanceSchedulingA.getWorkStartTime();
				String end = attendanceSchedulingA.getLeaveTimeEnd();
				if(!TextUtils.isEmpty(start)&&!TextUtils.isEmpty(end)){
					if(comPareDate(start, end)>0){
						Toast.makeText(context, R.string.work_infos, Toast.LENGTH_SHORT).show();
//						return;
					}
				}
						dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		TimeView timeView = new TimeView(context, TimeView.TYPE_TIME,
				new TimeView.WheelTimeListener() {

					@Override
					public void onResult(String wheelTime) {
						time = wheelTime;

					}
				});
		timeView.setOriTime(hour, minutes);
		ll.addView(timeView);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	 public static int comPareDate(String date1,String date2){
//		  Date date22 = DateUtil.getDate(date2, DateUtil.DATATIMEF_STR);
//		  Date date11 = DateUtil.getDate(date1, DateUtil.DATATIMEF_STR);
		 String[] dd1 = date1.split(" ");
		 String[] dd2 = date2.split(" ");
		 String[] d1 = dd1[dd1.length-1].split(":");
		 String[] d2 = dd2[dd2.length-1].split(":");
		 int re = Integer.parseInt(d1[0])-Integer.parseInt(d2[0]);
//		  int re = date11.compareTo(date22);
		  return re;
	  }
}
