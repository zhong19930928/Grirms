package com.yunhu.yhshxc.bo;

import java.io.Serializable;

public class BbsCommentItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392415454612758569L;
	private String id;
	private String content;
	private String createTime;
	private String createuser;
	private String score;
	
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
}
