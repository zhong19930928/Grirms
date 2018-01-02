package com.yunhu.yhshxc.activity.carSales.bo;


/**
 * 购物车
 * @author shenji
 *
 */
public class CarSalesShoppingCart {
	private int id;
	private int productId;//产品
	private int unitId;//单位id
	private double number;//购买产品数量
	private double subtotal;//小计 总价
	private int pitcOn;//是否选中 0没选中 1选中
	private String pId;
	private String promotionIds;//促销数据ID
	private double disAmount;//返还现金后价格
	private double preAmount;//返还现金
	private double disNumber;//赠送产品数量
	private double discountPrice;//打折后价格
	private double prePrice;//打的折扣
	private int giftId;//赠品的产品ID
	private int giftUnitId;//赠品的UnidId;
	private double nowProductPrict;//产品的当前价格，默认为产品单价
	private CarSalesProduct product;
	private CarSalesProduct pro;
	
	
	public CarSalesProduct getPro() {
		return pro;
	}
	public void setPro(CarSalesProduct pro) {
		this.pro = pro;
	}
	public CarSalesProduct getProduct() {
		return product;
	}
	public void setProduct(CarSalesProduct product) {
		this.product = product;
	}
	public int getGiftUnitId() {
		return giftUnitId;
	}
	public double getNowProductPrict() {
		return nowProductPrict;
	}
	public void setNowProductPrict(double nowProductPrict) {
		this.nowProductPrict = nowProductPrict;
	}
	public void setGiftUnitId(int giftUnitId) {
		this.giftUnitId = giftUnitId;
	}
	public int getGiftId() {
		return giftId;
	}
	public double getPreAmount() {
		return preAmount;
	}
	public void setPreAmount(double preAmount) {
		this.preAmount = preAmount;
	}
	public double getPrePrice() {
		return prePrice;
	}
	public void setPrePrice(double prePrice) {
		this.prePrice = prePrice;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public double getDisAmount() {
		return disAmount;
	}
	public void setDisAmount(double disAmount) {
		this.disAmount = disAmount;
	}
	public double getDisNumber() {
		return disNumber;
	}
	public void setDisNumber(double disNumber) {
		this.disNumber = disNumber;
	}
	public double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	
	public String getPromotionIds() {
		return promotionIds;
	}
	public void setPromotionIds(String promotionIds) {
		this.promotionIds = promotionIds;
	}
	public int getPitcOn() {
		return pitcOn;
	}
	public void setPitcOn(int pitcOn) {
		this.pitcOn = pitcOn;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
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
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	@Override
	public String toString() {
		return "Order3ShoppingCart [id=" + id + ", productId=" + productId
				+ ", unitId=" + unitId + ", number=" + number + ", subtotal="
				+ subtotal + ", pitcOn=" + pitcOn + ", pId=" + pId
				+ ", promotionIds=" + promotionIds + ", disAmount=" + disAmount
				+ ", preAmount=" + preAmount + ", disNumber=" + disNumber
				+ ", discountPrice=" + discountPrice + ", prePrice=" + prePrice
				+ ", giftId=" + giftId + ", giftUnitId=" + giftUnitId
				+ ", nowProductPrict=" + nowProductPrict + ", pro=" + product + "]";
	}
	
	
}
