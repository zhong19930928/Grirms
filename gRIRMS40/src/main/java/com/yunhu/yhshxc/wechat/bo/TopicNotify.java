package com.yunhu.yhshxc.wechat.bo;

/**
 * 话题公告
 * @author jishen
 *
 */
public class TopicNotify {
	private int id;
	private int topicId;//话题ID
	private String content;//公告内容
	private String date;//日期
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
