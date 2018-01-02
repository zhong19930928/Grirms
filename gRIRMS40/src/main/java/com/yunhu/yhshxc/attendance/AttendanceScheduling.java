package com.yunhu.yhshxc.attendance;

public class AttendanceScheduling {
	public static final int TYPE_1 = 0;// 0出勤 2休息 3请假
	public static final int TYPE_2 = 1;//休息
	public static final int TYPE_3 = 2;//请假
	private String weekDay;//周几？
	private String date;//日期
	private int pType = 0;//区分 0出勤 1休息 2 请假
	private String isHalfDay = "";//全天半天 1全天 2 半天
	private String workStartTime;//请假上班时间
	private String workEndTime;//请假下班时间
	private String leaveStartTime;//请假开始时间
	private String leaveEndTime;//请假结束时间
	private String WorkTimeStart;//上班时间
	private String LeaveTimeEnd;//下班时间
	
	
	public String getWorkTimeStart() {
		return WorkTimeStart;
	}
	public void setWorkTimeStart(String workTimeStart) {
		WorkTimeStart = workTimeStart;
	}
	public String getLeaveTimeEnd() {
		return LeaveTimeEnd;
	}
	public void setLeaveTimeEnd(String leaveTimeEnd) {
		LeaveTimeEnd = leaveTimeEnd;
	}
	private String leaveReason;//请假理由

	
	public String getIsHalfDay() {
		return isHalfDay;
	}
	public void setIsHalfDay(String isHalfDay) {
		this.isHalfDay = isHalfDay;
	}
	
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getpType() {
		return pType;
	}
	public void setpType(int pType) {
		this.pType = pType;
	}
	public String getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(String workStartTime) {
		this.workStartTime = workStartTime;
	}
	public String getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(String workEndTime) {
		this.workEndTime = workEndTime;
	}
	public String getLeaveStartTime() {
		return leaveStartTime;
	}
	public void setLeaveStartTime(String leaveStartTime) {
		this.leaveStartTime = leaveStartTime;
	}
	public String getLeaveEndTime() {
		return leaveEndTime;
	}
	public void setLeaveEndTime(String leaveEndTime) {
		this.leaveEndTime = leaveEndTime;
	}

	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	
}
