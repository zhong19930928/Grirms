package com.yunhu.yhshxc.bo;


public class Task{
	
	public static final int ISREAD_Y = 0;
	public static final int ISREAD_N = 1;
	
	private Integer id;
	private String createUser;
	private String taskTitle;
	private String detailTask;
	private String createTime;
	private Integer isread;
	private int moduleid;
	private int taskId;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getDetailTask() {
		return detailTask;
	}

	public void setDetailTask(String detailTask) {
		this.detailTask = detailTask;
	}

	public Integer getIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getModuleid() {
		return moduleid;
	}

	public void setModuleid(int moduleid) {
		this.moduleid = moduleid;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	
}
