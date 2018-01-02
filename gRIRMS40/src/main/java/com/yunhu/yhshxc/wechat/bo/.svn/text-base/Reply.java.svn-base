package com.yunhu.yhshxc.wechat.bo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 回帖
 * 
 * @author jishen
 * 
 */
public class Reply {
	private int id;
	private int replyId;// 评论ID
	private int topicId;// 话题ID
	private String pathCode;// 格式 话题ID-回帖ID-回帖ID
	private int level;// 结合回帖path找到某回帖的子回帖
	private int userId;// 回帖人ID
	private String replyName;//回帖人名称
	private String survey;// 回帖调查选项 保存调查选项数字标号 存储格式 1，2，3，4
	private String content;// 回帖内容
	private String date;// 时间
	private String adress;// 地址
	private String attachment;// 附件文件名拼接 filename1~filename2
	private String photo;//photo1~photo2
	private String groupId;//群ID
	private int isSend;//是否发送成功
	private int isRead;//区分已读 未读 0未读  1已读 默认为未读
	private String url;//头像url
	private String topicType;//判断聊天、调查、审批类 聊天1 调查2 审批3
	private String msgKey;
	private String isPublic;//话题类型
	private boolean isPrivate;//是否是私聊回帖
	private int isClose;//回帖所属话题是否关闭 0、不关闭 1、关闭 默认为0
	private int authUserId; // 审核人ID
	private String authUserName; // 审核人名称
	private String delStatus; // 撤回状态  0：正常 1：撤回
	
	public int getIsClose() {
		return isClose;
	}

	public int getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(int authUserId) {
		this.authUserId = authUserId;
	}

	public String getAuthUserName() {
		return authUserName;
	}

	public void setAuthUserName(String authUserName) {
		this.authUserName = authUserName;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public void setIsClose(int isClose) {
		this.isClose = isClose;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
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

	public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	private ArrayList<HashMap<String, Object>> avatar;// 图片

	public ArrayList<HashMap<String, Object>> getAvatar() {
		return avatar;
	}

	public void setAvatar(ArrayList<HashMap<String, Object>> avatar) {
		this.avatar = avatar;
	}

	
	
	public String getPathCode() {
		return pathCode;
	}

	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}


	private boolean isMySelf;// 是否为本人

	public Boolean getIsMySelf() {
		return isMySelf;
	}

	public void setIsMySelf(Boolean isMySelf) {
		this.isMySelf = isMySelf;
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

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSurvey() {
		return survey;
	}

	public void setSurvey(String survey) {
		this.survey = survey;
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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
