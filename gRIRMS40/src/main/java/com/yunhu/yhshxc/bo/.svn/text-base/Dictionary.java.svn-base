package com.yunhu.yhshxc.bo;


public class Dictionary {

	private String did;//字典id
	private String ctrlCol;//名称
	private String note;
	private int level;//订单3 店面转换的时候使用
	private int selectCount;//该项选择的次数
	
	
	public int getSelectCount() {
		return selectCount;
	}
	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getCtrlCol() {
		return ctrlCol;
	}
	public void setCtrlCol(String ctrlCol) {
		this.ctrlCol = ctrlCol;
	}
	
	@Override
	public String toString() {
		return ctrlCol;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		Dictionary d = (Dictionary)o;
		if(d.getDid().equals(did)){
			return true;
		}else{
			return false;
		}
	}

	
}
