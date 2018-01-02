package com.yunhu.yhshxc.wechat.bo;

/**
 * 评论
 * 
 * @author jishen
 * 
 */
public class Comment {
	private int id;
	private int replyId;// 回帖ID
	private int commentId;// 评论ID
	private String comment;// 评论内容
	private int cUserId;// 评论人ID
	private String cUserName;// 评论人名称
	private int dUserId;// 被评论人ID
	private String dUserName;// 被评论人名称
	private String pathCode;// 格式 话题ID-回帖ID-回帖ID
	private String date;// 评论时间
	private String topicId;//话题ID
	private int isSend;//是否发送成功 0是没发送成功 1是发送成功
	private String msgKey;
	private String isPublic;//话题类型
	private Integer authUserId; // 审核人ID
	private String authUserName; // 审核人名称
	
	public Integer getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(Integer authUserId) {
		this.authUserId = authUserId;
	}

	public String getAuthUserName() {
		return authUserName;
	}

	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPathCode() {
		return pathCode;
	}

	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getcUserId() {
		return cUserId;
	}

	public void setcUserId(int cUserId) {
		this.cUserId = cUserId;
	}

	public String getcUserName() {
		return cUserName;
	}

	public void setcUserName(String cUserName) {
		this.cUserName = cUserName;
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

}
