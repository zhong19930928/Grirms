package com.yunhu.yhshxc.activity.carSales.bo;

public class CarSalesProductData {
	
	private int id;
	private int dataId;//数据ID
	private int productId;//产品ID
	private double carSalesCount;//单品个数
	private double actualCount;//实际个数 如果修改的话就是修改后的数量
	private String productUnit;//单位
	private int unitId;//单位ID
	private double carSalesPrice;//原价
	private double carSalesAmount;//单品总价 打折前
	private double actualAmount;//实际总价 折后
	private String status;//单位状态 送货状态
	private int iscarSalesMain;// 1主产品 2赠品
	private int promotionId;//促销ID
	private int mainProductId;//赠品的主产品ID
	private String carSalesRemark;//说明
	private String carSalesNo;//订单编号
	private double sendCount;//送货量
	private double currentSendCount;//本次送货量
	private String productName;	
	private double stockNum;//剩余库存数
	
	
	public double getStockNum() {
		return stockNum;
	}
	public void setStockNum(double stockNum) {
		this.stockNum = stockNum;
	}
	public int getIscarSalesMain() {
		return iscarSalesMain;
	}
	public void setIscarSalesMain(int iscarSalesMain) {
		this.iscarSalesMain = iscarSalesMain;
	}
	public double getActualCount() {
		return actualCount;
	}
	public void setActualCount(double actualCount) {
		this.actualCount = actualCount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public double getSendCount() {
		return sendCount;
	}
	public void setSendCount(double sendCount) {
		this.sendCount = sendCount;
	}
	
	public double getCurrentSendCount() {
		return currentSendCount;
	}
	public void setCurrentSendCount(double currentSendCount) {
		this.currentSendCount = currentSendCount;
	}
	public String getCarSalesNo() {
		return carSalesNo;
	}
	public void setCarSalesNo(String carSalesNo) {
		this.carSalesNo = carSalesNo;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
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
	public double getCarSalesCount() {
		return carSalesCount;
	}
	public void setCarSalesCount(double carSalesCount) {
		this.carSalesCount = carSalesCount;
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public double getCarSalesPrice() {
		return carSalesPrice;
	}
	public void setCarSalesPrice(double carSalesPrice) {
		this.carSalesPrice = carSalesPrice;
	}
	public double getCarSalesAmount() {
		return carSalesAmount;
	}
	public void setCarSalesAmount(double carSalesAmount) {
		this.carSalesAmount = carSalesAmount;
	}
	public double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getIsCarSalesMain() {
		return iscarSalesMain;
	}
	public void setIsCarSalesMain(int iscarSalesMain) {
		this.iscarSalesMain = iscarSalesMain;
	}
	public int getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}
	public int getMainProductId() {
		return mainProductId;
	}
	public void setMainProductId(int mainProductId) {
		this.mainProductId = mainProductId;
	}
	public String getCarSalesRemark() {
		return carSalesRemark;
	}
	public void setCarSalesRemark(String carSalesRemark) {
		this.carSalesRemark = carSalesRemark;
	}
	@Override
	public String toString() {
		return "carSales3ProductData [id=" + id + ", dataId=" + dataId
				+ ", productId=" + productId + ", carSalesCount=" + carSalesCount
				+ ", actualCount=" + actualCount + ", productUnit="
				+ productUnit + ", unitId=" + unitId + ", carSalesPrice="
				+ carSalesPrice + ", carSalesAmount=" + carSalesAmount
				+ ", actualAmount=" + actualAmount + ", status=" + status
				+ ", iscarSalesMain=" + iscarSalesMain + ", promotionId="
				+ promotionId + ", mainProductId=" + mainProductId
				+ ", carSalesRemark=" + carSalesRemark + ", carSalesNo=" + carSalesNo
				+ ", sendCount=" + sendCount + ", currentSendCount="
				+ currentSendCount + ", productName=" + productName + "]";
	}
	
}
