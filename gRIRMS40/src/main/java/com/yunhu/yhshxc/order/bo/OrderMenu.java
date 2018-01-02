package com.yunhu.yhshxc.order.bo;

/**
 * 进销存菜单实例
 * @author jishen
 *
 */
public class OrderMenu {
	private String orderMenuName;
	private int type;//菜单类型
	private int number;//标识数量
	private String effectiveDate;//有效日期

	public String getOrderMenuName() {
		return orderMenuName;
	}
	public void setOrderMenuName(String orderMenuName) {
		this.orderMenuName = orderMenuName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
}
