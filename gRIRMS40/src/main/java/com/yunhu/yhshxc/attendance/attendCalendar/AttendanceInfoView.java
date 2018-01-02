package com.yunhu.yhshxc.attendance.attendCalendar;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class AttendanceInfoView {
	private Context context;
	private View view;
	private TextView tv_attend_info;
	private TextView tv_attend_time;
	private TextView tv_attend_address;
	public AttendanceInfoView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.attendance_info_view, null);
		tv_attend_address = (TextView) view.findViewById(R.id.tv_attend_address);
		tv_attend_time = (TextView) view.findViewById(R.id.tv_attend_time);
		tv_attend_info = (TextView) view.findViewById(R.id.tv_attend_info);
	}
	
	public View getView(){
		return view;
	}
	/**
	 * 设置上班打卡信息 正常
	 * @param in
	 */
	public void setDataIn(AttendanceInfo in){
		tv_attend_address.setText(in.getInAddr());
		tv_attend_time.setText(in.getInTime());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info));
	}
	/**
	 * 设置下班打卡信息 正常
	 * @param in
	 */
	public void setDataOut(AttendanceInfo in){
		tv_attend_address.setText(in.getOutAddr());
		tv_attend_time.setText(in.getOutTime());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info2));
	}
	/**
	 * 设置上班打卡信息 迟到
	 * @param in
	 */
	public void setDataInLate(AttendanceInfo in){
		tv_attend_address.setText(in.getInAddr());
		tv_attend_time.setText(in.getInTime());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info));
	}
	/**
	 * 设置下班打卡信息 早退
	 * @param in
	 */
	public void setDataOutEarly(AttendanceInfo in){
		tv_attend_address.setText(in.getOutAddr());
		tv_attend_time.setText(in.getOutTime());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info2));
	}
	
	/**
	 * 设置上班打卡信息 加班
	 * @param in
	 */
	public void setDataInJ(AttendanceInfo in){
		tv_attend_address.setText(in.getInAddr());
		tv_attend_time.setText(in.getInTimeJ());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info1));
	}
	
	/**
	 * 设置下班班打卡信息 加班
	 * @param in
	 */
	public void setDataOutJ(AttendanceInfo in){
		tv_attend_address.setText(in.getOutAddr());
		tv_attend_time.setText(in.getOutTimeJ());
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.calendar_info3));
	}
	
	/**
	 * 设置请假
	 */
	public void setDataLeave(AttendanceInfo in){
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.attendance_leave));
		if(!TextUtils.isEmpty(in.getExpCom())){
			tv_attend_address.setText(in.getExpCom());
		}else if(!TextUtils.isEmpty(in.getExpName())){
			tv_attend_address.setText(in.getExpName());
		}else{
			tv_attend_address.setText("");
		}
	}
	
	public void setDateRest(AttendanceInfo in){
		tv_attend_info.setText(PublicUtils.getResourceString(context,R.string.take_rest));
		if(!TextUtils.isEmpty(in.getExpCom())){
			tv_attend_address.setText(in.getExpCom());
		}else if(!TextUtils.isEmpty(in.getExpName())){
			tv_attend_address.setText(in.getExpName());
		}else{
			tv_attend_address.setText("");
		}
	}
	
}
