package com.yunhu.yhshxc.order2.bo;


public class OrderSpec {
	private double pay;//支付金额
	private double discount;//折扣
	private double unPay;//未付金额
	private OrderContacts placeUser;//订货联系人
	private OrderContacts receiveUser;//收货联系人
	private String spec;//订单说明
	
	
	public double getPay() {
		return pay;
	}
	public void setPay(double pay) {
		this.pay = pay;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public OrderContacts getPlaceUser() {
		return placeUser;
	}
	public void setPlaceUser(OrderContacts placeUser) {
		this.placeUser = placeUser;
	}
	public OrderContacts getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(OrderContacts receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public double getUnPay() {
		return unPay;
	}
	public void setUnPay(double unPay) {
		this.unPay = unPay;
	}
	
	
}
