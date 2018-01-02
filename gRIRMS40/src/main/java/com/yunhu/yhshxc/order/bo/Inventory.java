package com.yunhu.yhshxc.order.bo;

/**
 * 库存管理实例
 * @author jishen
 */
public class Inventory {
    private long stockCount;//进货
	private long returnedCount;//退货
	private String reportTime;//上次盘点时间
	private int productId;//产品ID
	private long inventoryCount;//上次盘点数
	private long totalSales;//销量
	private String productName;//产品名称
	
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public long getStockCount() {
		return stockCount;
	}
	public void setStockCount(long stockCount) {
		this.stockCount = stockCount;
	}
	public long getReturnedCount() {
		return returnedCount;
	}
	public void setReturnedCount(long returnedCount) {
		this.returnedCount = returnedCount;
	}
	public long getInventoryCount() {
		return inventoryCount;
	}
	public void setInventoryCount(long inventoryCount) {
		this.inventoryCount = inventoryCount;
	}
	public long getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(long totalSales) {
		this.totalSales = totalSales;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
