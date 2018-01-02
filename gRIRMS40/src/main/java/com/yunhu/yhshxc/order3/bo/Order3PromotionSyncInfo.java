package com.yunhu.yhshxc.order3.bo;

public class Order3PromotionSyncInfo {
	private String syncInfoName;//促销名称
	private String syncValidTerm ;//有效期限
	private String syncDisFunction;//促销方式
	private String syncContent;//促销内容
	private String syncInstruction;//促销说明
	public String getSyncInfoName() {
		return syncInfoName;
	}
	public void setSyncInfoName(String syncInfoName) {
		this.syncInfoName = syncInfoName;
	}
	public String getSyncValidTerm() {
		return syncValidTerm;
	}
	public void setSyncValidTerm(String syncValidTerm) {
		this.syncValidTerm = syncValidTerm;
	}
	public String getSyncDisFunction() {
		return syncDisFunction;
	}
	public void setSyncDisFunction(String syncDisFunction) {
		this.syncDisFunction = syncDisFunction;
	}
	public String getSyncContent() {
		return syncContent;
	}
	public void setSyncContent(String syncContent) {
		this.syncContent = syncContent;
	}
	public String getSyncInstruction() {
		return syncInstruction;
	}
	public void setSyncInstruction(String syncInstruction) {
		this.syncInstruction = syncInstruction;
	}
	
}
