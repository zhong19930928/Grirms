package com.yunhu.yhshxc.wechat.bo;

/**
 * 点赞
 * @author jishen
 *
 */
public class Zan {

	private int id;
	private int zanId;//点赞ID
	private int topicId;//话题ID
	private int replayId;//回帖ID
	private int userId;//点赞人ID
	private String userName;//点赞人名称
	private String date;//点赞时间
	private int isSend;//是否发送成功
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getIsSend() {
		return isSend;
	}
	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getZanId() {
		return zanId;
	}
	public void setZanId(int zanId) {
		this.zanId = zanId;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getReplayId() {
		return replayId;
	}
	public void setReplayId(int replayId) {
		this.replayId = replayId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
