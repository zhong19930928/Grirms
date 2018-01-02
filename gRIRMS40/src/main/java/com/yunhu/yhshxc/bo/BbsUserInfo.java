package com.yunhu.yhshxc.bo;

import java.io.Serializable;


public class BbsUserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1306944219113765161L;
	private String id;
	private String name;
	private PhotoInfo photoInfo;
	private String score;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PhotoInfo getPhotoInfo() {
		return photoInfo;
	}
	public void setPhotoInfo(PhotoInfo photoInfo) {
		this.photoInfo = photoInfo;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
}
