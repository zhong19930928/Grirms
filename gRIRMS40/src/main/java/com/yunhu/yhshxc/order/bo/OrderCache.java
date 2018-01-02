package com.yunhu.yhshxc.order.bo;


/**
 * 订单
 * @author jishen
 */
public class OrderCache {
	private int id;
	private String orderNumber;//订单编号
	private int orderProductId;//订单产品ID
	private String orderProductName;//订单产品名称
	private double unitPrice;//单价
	private String totalPrice;//总价
	private long availableOrderQuantity;//可订货数量
	private String periodDate;//数据时间段
	private long totalSales;//总计销售量
	private long projectedInventoryQuantity;//预计库存量
	private long orderQuantity;//订货数量
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getOrderProductId() {
		return orderProductId;
	}
	public void setOrderProductId(int orderProductId) {
		this.orderProductId = orderProductId;
	}
	public String getOrderProductName() {
		return orderProductName;
	}
	public void setOrderProductName(String orderProductName) {
		this.orderProductName = orderProductName;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public long getAvailableOrderQuantity() {
		return availableOrderQuantity;
	}
	public void setAvailableOrderQuantity(long availableOrderQuantity) {
		this.availableOrderQuantity = availableOrderQuantity;
	}
	public String getPeriodDate() {
		return periodDate;
	}
	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}
	public long getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(long totalSales) {
		this.totalSales = totalSales;
	}
	public long getProjectedInventoryQuantity() {
		return projectedInventoryQuantity;
	}
	public void setProjectedInventoryQuantity(long projectedInventoryQuantity) {
		this.projectedInventoryQuantity = projectedInventoryQuantity;
	}
	public long getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	
	
}
