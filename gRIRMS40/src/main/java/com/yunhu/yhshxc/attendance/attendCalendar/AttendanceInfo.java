package com.yunhu.yhshxc.attendance.attendCalendar;
/**
 * 考勤日历每天考勤信息
 * @author xuelinlin
 *
 */
public class AttendanceInfo {
	private String time;
	private String isExp;// 0：考勤正常 1：迟到早退 2：休息 3：请假 9：其它
	private String expName;// 请假名称、如时间
	private String expCom;//请假说明
	private String inTime;//考勤上班时间
	private String outTime;//考勤下班时间
	private String inAddr;//上班地址定位
	private String outAddr;//下班地址定位
	private String inComment;//上班说明
	private String outComment;//下班说明
	private String inTimeJ;// 考勤加班上班时间
	private String outTimeJ; // 考勤加班下班时间
	private String inCommentJ;//考勤加班上班说明
	private String outCommentJ;//考勤加班下班说明
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIsExp() {
		return isExp;
	}
	public void setIsExp(String isExp) {
		this.isExp = isExp;
	}
	public String getExpName() {
		return expName;
	}
	public void setExpName(String expName) {
		this.expName = expName;
	}
	public String getExpCom() {
		return expCom;
	}
	public void setExpCom(String expCom) {
		this.expCom = expCom;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getInAddr() {
		return inAddr;
	}
	public void setInAddr(String inAddr) {
		this.inAddr = inAddr;
	}
	public String getOutAddr() {
		return outAddr;
	}
	public void setOutAddr(String outAddr) {
		this.outAddr = outAddr;
	}
	public String getInComment() {
		return inComment;
	}
	public void setInComment(String inComment) {
		this.inComment = inComment;
	}
	public String getOutComment() {
		return outComment;
	}
	public void setOutComment(String outComment) {
		this.outComment = outComment;
	}
	public String getInTimeJ() {
		return inTimeJ;
	}
	public void setInTimeJ(String inTimeJ) {
		this.inTimeJ = inTimeJ;
	}
	public String getOutTimeJ() {
		return outTimeJ;
	}
	public void setOutTimeJ(String outTimeJ) {
		this.outTimeJ = outTimeJ;
	}
	public String getInCommentJ() {
		return inCommentJ;
	}
	public void setInCommentJ(String inCommentJ) {
		this.inCommentJ = inCommentJ;
	}
	public String getOutCommentJ() {
		return outCommentJ;
	}
	public void setOutCommentJ(String outCommentJ) {
		this.outCommentJ = outCommentJ;
	}
	
	
}
