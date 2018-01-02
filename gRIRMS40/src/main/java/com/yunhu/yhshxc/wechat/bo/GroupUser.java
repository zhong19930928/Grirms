package com.yunhu.yhshxc.wechat.bo;

/**
 * 群中的人
 * @author jishen
 *
 */
public class GroupUser {
	private int id;
	private int groupId;//群ID
	private int userId;//用户ID
	private String userName;//用户名称
	private String photo;//用户图像
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
