package com.yunhu.yhshxc.wechat.bo;

/**
 * 话题
 * 
 * @author jishen
 * 
 */
public class Topic {
	public static final int CLASSIFY_TYPE_GK = 1;// 公开
	public static final int CLASSIFY_TYPE_BM = 2;// 部门
	public static final int CLASSIFY_TYPE_SL = 3;// 私聊
	public static final String TYPE_1 = "1";//聊天
	public static final String TYPE_2 = "2";//调查
	public static final String TYPE_3 = "3";//评审
	public static final int ISREPLY_1 = 0;// 不必须
	public static final int ISREPLY_2 = 1;// 必须
	public static final int COMMENT_1 = 0;// 评论发言权限 0、所有人
	public static final int COMMENT_2 = 1;// 评论发言权限 1、只有本人
	public static final int REPLY_1 = 0;// 所有人
	public static final int REPLY_2 = 1;// 创建人和本人

	private int id;
	private int topicId;// 话题ID
	private int groupId;// 群ID
	private String title;// 话题标题
	private String explain;// 话题说明
	private String type;// 类型
	private String from;// 话题有效开始时间
	private String to;// 话题有效结束时间
	private int speakNum;// 允许发言次数 默认0 不限制发言
	private int isReply;// 是否必须回帖 默认0不必须 1 必须
	private int comment;// 评论发言权限 默认0所有人 1只话题创建人和本人
	private int replyReview;// 回帖查看权限 默认0所有人，1只有话题创建人和本人
	private int createUserId;// 话题创建人ID

	private String createTime;// 话题创建时间

	private String options;//调查类话题选项

	private int classify;// 话题分类 公开，部门交流，私聊
	private String recentTime;// 最新回帖时间
	private String recentContent;//最新回帖内容
	private String selectType; 
	private int  isAttention;//是否关注 0、不关注 1、关注
	private int isClose;//是否关闭 0、不关闭 1、关闭 默认为0
	private String msgKey;
	private String createUserName;//话题创建人名字
	private String createOrgName;//创建人部门
	
	
	
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateOrgName() {
		return createOrgName;
	}

	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public String getRecentContent() {
		return recentContent;
	}

	public void setRecentContent(String recentContent) {
		this.recentContent = recentContent;
	}
	
	public int getIsClose() {
		return isClose;
	}

	public void setIsClose(int isClose) {
		this.isClose = isClose;
	}

	public int getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(int isAttention) {
		this.isAttention = isAttention;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getRecentTime() {
		return recentTime;
	}

	public void setRecentTime(String recentTime) {
		this.recentTime = recentTime;
	}

	public int getClassify() {
		return classify;
	}

	public void setClassify(int classify) {
		this.classify = classify;
	}

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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getSpeakNum() {
		return speakNum;
	}

	public void setSpeakNum(int speakNum) {
		this.speakNum = speakNum;
	}

	public int getIsReply() {
		return isReply;
	}

	public void setIsReply(int isReply) {
		this.isReply = isReply;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getReplyReview() {
		return replyReview;
	}

	public void setReplyReview(int replyReview) {
		this.replyReview = replyReview;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
