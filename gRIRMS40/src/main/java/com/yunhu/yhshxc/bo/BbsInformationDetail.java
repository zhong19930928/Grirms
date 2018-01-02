package com.yunhu.yhshxc.bo;

import java.io.Serializable;


public class BbsInformationDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3371624793698137498L;
	private String id;
	private String content;
	private PhotoInfo photoInfo;
	private String createTime;
	private String address;
	private String commentNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public PhotoInfo getPhotoInfo() {
		return photoInfo;
	}
	public void setPhotoInfo(PhotoInfo photoInfo) {
		this.photoInfo = photoInfo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
	
}
