package com.yunhu.yhshxc.order3.bo;

/**
 * 促销信息
 * @author shenji
 *
 */
public class Order3PromotionInfo {
	private String title;
	private String details;//促销内容
	private int productId;//主产品id
	private int unitId;//主产品单位id
	private int giftId;//赠品id
	private int giftUnit;//赠品单位id
	private double giftNum;//赠品数量
	private int promotionId;//促销id;
	private double actualAmount;//实际价格
	private double orderPrice;//原价格
	public double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public double getGiftNum() {
		return giftNum;
	}
	public void setGiftNum(double giftNum) {
		this.giftNum = giftNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getProductId() {
		return productId;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public int getGiftUnit() {
		return giftUnit;
	}
	public void setGiftUnit(int giftUnit) {
		this.giftUnit = giftUnit;
	}
	
	
	
}
