package com.yunhu.yhshxc.bo;

import java.io.Serializable;

public class Module implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3585366821524271994L;
	/**
	 * 
	 */
	private Integer id;//主键
	private Integer menuId;//模块ID
	private Integer type;//模块类型
	private String name;//模块名称
	private Integer auth;//下发的时候查询用户穿透的类型
	private String authOrgId;//指定多个区域的orgId
	private String orgCode;//指定多个区域的机构的code
	private Integer isCancel;//双向是否可取消执行
	private String phoneTaskFuns;//双向主任务按钮
	private String dynamicStatus;//双向动态按钮
	private String isReportTask;//新双向任务是否可多上报
	
	
	
	public String getIsReportTask() {
		return isReportTask;
	}
	public void setIsReportTask(String isReportTask) {
		this.isReportTask = isReportTask;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public String getPhoneTaskFuns() {
		return phoneTaskFuns;
	}
	public void setPhoneTaskFuns(String phoneTaskFuns) {
		this.phoneTaskFuns = phoneTaskFuns;
	}
	public String getDynamicStatus() {
		return dynamicStatus;
	}
	public void setDynamicStatus(String dynamicStatus) {
		this.dynamicStatus = dynamicStatus;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAuth() {
		return auth;
	}
	public void setAuth(Integer auth) {
		this.auth = auth;
	}
	public String getAuthOrgId() {
		return authOrgId;
	}
	public void setAuthOrgId(String authOrgId) {
		this.authOrgId = authOrgId;
	}
	public Integer getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(Integer isCancel) {
		this.isCancel = isCancel;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
}
