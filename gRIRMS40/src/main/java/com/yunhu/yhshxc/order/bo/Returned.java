package com.yunhu.yhshxc.order.bo;

import com.yunhu.yhshxc.bo.Dictionary;

public class Returned {
	private int id; //唯一
	private Integer productId;
	private String name;//货物名称
	private Dictionary reason;//退货原因
	private long returnedQuantity;//退货数量
	private int state;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Dictionary getReason() {
		return reason;
	}
	public void setReason(Dictionary reason) {
		this.reason = reason;
	}
	public long getReturnedQuantity() {
		return returnedQuantity;
	}
	public void setReturnedQuantity(long returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
