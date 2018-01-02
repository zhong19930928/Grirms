package com.yunhu.yhshxc.wechat.bo;

/**
 * 通知公告
 * @author jishen
 *
 */
public class Notification {
	private int id;
	private int noticeId;
	private String title;//标题
	private String content;//内容
	private String from;//有效期开始
	private String to;//有效期结束
	private String attachment;//附件 file1|file2|file3
	private String peoples;//通知人群(1:所有，2：本部门包含下级 3：本部门不包含下级)  type
	private int orgId;//机构ID
	private String orgCode;//机构code
	private String creater;//发布人
	private String createOrg;//发布部门
	private String createDate;//发布日期
	
	
	private String role;
	private String users;
	private String isAttach;//是否有附件  1有 0 没有
	private String isNoticed;//是否被关注 1 关注 0 未关注
	private String isRead;//是否未读 1已读 0 未读
	
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getIsNoticed() {
		return isNoticed;
	}
	public void setIsNoticed(String isNoticed) {
		this.isNoticed = isNoticed;
	}
	public String getIsAttach() {
		return isAttach;
	}
	public void setIsAttach(String isAttach) {
		this.isAttach = isAttach;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getPeoples() {
		return peoples;
	}
	public void setPeoples(String peoples) {
		this.peoples = peoples;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreateOrg() {
		return createOrg;
	}
	public void setCreateOrg(String createOrg) {
		this.createOrg = createOrg;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
}
