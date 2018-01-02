package com.yunhu.yhshxc.wechat.bo;

/**
 * 关注
 * @author jishen
 *
 */
public class Attention {
	private int id;
	private int attentionId;//关注id
	private int userId;//用户ID
	private int type;// 1话题 2 通知
	private int topicId;//话题ID
	private int noticeId;//通知ID
	private String date;//时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAttentionId() {
		return attentionId;
	}
	public void setAttentionId(int attentionId) {
		this.attentionId = attentionId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getNotifyId() {
		return noticeId;
	}
	public void setNotifyId(int notifyId) {
		this.noticeId = notifyId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
