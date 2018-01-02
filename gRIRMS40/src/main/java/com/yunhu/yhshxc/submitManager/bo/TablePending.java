package com.yunhu.yhshxc.submitManager.bo;


public class TablePending {
	
	//数据提交的最大次数
	public static int TABLE_PENDING_MAX_NUMBER = 3;
	public static int TYPE_DATA = 1; //一般数据
	public static int TYPE_LOATION = 2; //被动定位
	public static int TYPE_ACTIVE = 3; //金博要的观察用户活跃度
	public static int TYPE_ATTENDANCE_LOATION = 4; //考勤查岗定位
	public static int TYPE_IMAGE = 10; //压缩后的图片
	public static int TYPE_FILE = 11;//文件
	public static int TYPE_AUDIO = 12; //音频类型
	public static int TYPE_LARGE_IMAGE = 13;//较大图片
	
	//数据状态
	public static int STATUS_SUBIMTTING = 1;//正在提交
	public static int STATUS_READY = 2;//准备提交
	public static int STATUS_ERROR_BIG_SIZE = 10;//网络异常，提交内容过大的数据，避免自动提交耗费流量（未压缩的图片）
	public static int STATUS_ERROR_NETWORK = 11;//网络异常
	public static int STATUS_ERROR_SERVER = 12;//返回0001
	public static int STATUS_ERROR_USER = 13;//返回0002
	
	private int id;
	private int type;
	private int status;
	private String title;
	private String content;
	private String createDate;
	private int numberOfTimes;
//	private String note;//备注
	private CoreHttpPendingRequest request; 
	
	
	
//	public String getNote() {
//		return note;
//	}
//	public void setNote(String note) {
//		this.note = note;
//	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getNumberOfTimes() {
		return numberOfTimes;
	}
	public void setNumberOfTimes(int numberOfTimes) {
		this.numberOfTimes = numberOfTimes;
	}
	public CoreHttpPendingRequest getRequest() {
		return request;
	}
	public void setRequest(CoreHttpPendingRequest request) {
		this.request = request;
	}
}
