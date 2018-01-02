package com.yunhu.yhshxc.order.bo;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private String orderNo;// 订单号,退单号
	private String date;// 订单日期
	private List<OrderItem> items = new ArrayList<OrderItem>();//订单查询列表中Item对应的Bean
	private List<Returned> returneds = new ArrayList<Returned>();//进货管理和退货管理中退货Item对应的Bean
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	public List<Returned> getReturneds() {
		return returneds;
	}
	public void setReturneds(List<Returned> returneds) {
		this.returneds = returneds;
	}
	@Override
	public String toString() {
		return orderNo == null ? "" : orderNo;
	}

}
