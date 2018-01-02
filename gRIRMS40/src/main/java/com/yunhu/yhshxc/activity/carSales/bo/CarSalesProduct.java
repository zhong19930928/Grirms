package com.yunhu.yhshxc.activity.carSales.bo;

/**
 * 
 * @author jishen
 *	车销产品
 */
public class CarSalesProduct {
	private int id;
	private int productId;//产品id
	private String name;//产品名称
	private double price;//产品价格
	private double inventory;//库存
	private String productInfo;//产品介绍
	private String code;//二维码
	private String unit;//单位
	private int unitId;//产品单位ID
	private boolean isPromotion;//是否有促销
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getInventory() {
		return inventory;
	}
	public void setInventory(double inventory) {
		this.inventory = inventory;
	}
	public String getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public boolean isPromotion() {
		return isPromotion;
	}
	public void setPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}
	@Override
	public String toString() {
		return "CarSalesProduct [id=" + id + ", productId=" + productId
				+ ", name=" + name + ", price=" + price + ", inventory="
				+ inventory + ", productInfo=" + productInfo + ", code=" + code
				+ ", unit=" + unit + ", unitId=" + unitId + ", isPromotion="
				+ isPromotion + "]";
	}
	
	
}
