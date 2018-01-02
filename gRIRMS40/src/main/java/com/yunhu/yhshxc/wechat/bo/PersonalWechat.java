package com.yunhu.yhshxc.wechat.bo;

public class PersonalWechat {
	private int id;//本地数据库主键ID
	private int dataId;//私聊数据ID
	private int sUserId;//私聊发起人ID
	private String sUserName;//私聊发起人名称
	private int dUserId;//私聊被聊人ID
	private String dUserName;//私聊被聊人名称
	private String cUserHeadImg;//私聊发起人头像URL
	private String dUserHedaImg;//私聊被发起人头像URL
	private String attachment;//聊天附件信息
	private String content;//聊天内容
	private String date;//日期
	private String photo;
	private String msgKey;
	private String groupKey;//本身userID+其他用户ID
	private int isRead;//是否已读 1是已读0是未读
	
	
	
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public int getsUserId() {
		return sUserId;
	}
	public void setsUserId(int sUserId) {
		this.sUserId = sUserId;
	}
	public String getsUserName() {
		return sUserName;
	}
	public void setsUserName(String sUserName) {
		this.sUserName = sUserName;
	}
	public int getdUserId() {
		return dUserId;
	}
	public void setdUserId(int dUserId) {
		this.dUserId = dUserId;
	}
	public String getdUserName() {
		return dUserName;
	}
	public void setdUserName(String dUserName) {
		this.dUserName = dUserName;
	}
	public String getcUserHeadImg() {
		return cUserHeadImg;
	}
	public void setcUserHeadImg(String cUserHeadImg) {
		this.cUserHeadImg = cUserHeadImg;
	}
	public String getdUserHedaImg() {
		return dUserHedaImg;
	}
	public void setdUserHedaImg(String dUserHedaImg) {
		this.dUserHedaImg = dUserHedaImg;
	}
	
	
}
