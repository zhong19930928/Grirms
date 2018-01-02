package com.yunhu.yhshxc.attendance.leave;

public class LeaveInfo {
	private int id;
	private String type;//请假类型
	private String name;//请假名称
	private String maxDays;//天数
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(String maxDays) {
		this.maxDays = maxDays;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
