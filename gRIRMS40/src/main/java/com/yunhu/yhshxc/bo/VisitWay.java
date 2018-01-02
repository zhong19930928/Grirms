package com.yunhu.yhshxc.bo;

public class VisitWay {
	public static final int ORDER_NO = 0;//不排序
	public static final int ORDER_YES = 1;//排序
	
	public static final int AWOKETYPE_ONLY_SHOCK = 1;//震动
	public static final int AWOKETYPE_ONLY_RING = 2;//响铃
	public static final int AWOKETYPE_RING_SHOCK = 3;//震动加响铃
	
	private int id;//主键
	private int wayId;//线路ID
	private String name;//线路名称
	private int isOrder;//是否排序
	private int planId;//计划ID
	private int awokeType;//提醒类型
	private int intervalType;//执行类型
	private String weekly;//每周执行类型的参数
	private String fromDate;//自定义执行类型的开始日期
	private String toDate;//自定义执行类型的结束日期
	private String startdate;//可以拜访那个的开始日期
	private int cycleCount;//周期数
	private int visitCount;//拜访次数
	
	private int unVisitCount;
	
	
	public int getCycleCount() {
		return cycleCount;
	}
	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	public int getIntervalType() {
		return intervalType;
	}
	public void setIntervalType(int intervalType) {
		this.intervalType = intervalType;
	}
	public String getWeekly() {
		return weekly;
	}
	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public int getAwokeType() {
		return awokeType;
	}
	public void setAwokeType(int awokeType) {
		this.awokeType = awokeType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWayId() {
		return wayId;
	}
	public void setWayId(int wayId) {
		this.wayId = wayId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public int getUnVisitCount() {
		return unVisitCount;
	}
	public void setUnVisitCount(int unVisitCount) {
		this.unVisitCount = unVisitCount;
	}
}
