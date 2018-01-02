package com.yunhu.yhshxc.activity.carSales.bo;

import java.util.List;

public class CarSales {
	private int id;
	private int carSalesId;
	private String carSalesNo;//年月日+用户ID+手机端记载的3为顺序号
	private String storeId;//客户
	private String storeName;//客户名称
	private int contactId;//联系人ID
	private CarSalesContact contact;//联系人
	private double carSalesAmount;//原价总额打折前
	private double actualAmount;//实际总额折扣后
	private List<CarSalesProductData> productList;//所有产品
	private double payAmount;//预收款
	private double carSalesDiscount;//特别折扣
	private double unPayAmount;//订单未付款
	private String note;//留言
	private int isPromotion;//是否有促销 0没1有
	private String carSalesTime;//订单时间
	private String carSalesState;//订单状态
	private int isCommbine;//是否合并订单 1合并 0不合并
	private String image1;//下单拍照
	private String image2;
	private int printCount;//下单打印个数
	
	private String salesDate;//日期
	private String carNo;//车牌号
	private String salesUser;//销售人员ID
	private String salesPhone;//销售人员号码
	private String driverUser;//司机
	private String driverPhone;//司机电话
	private String status;//0有欠款，1 已结清
	private String carId;
	private double returnAmount;//退款
	private boolean isStock;//是否统计库存true统计 false不统计
	private List<Arrears> arrears;
	
	
	
	public List<Arrears> getArrears() {
		return arrears;
	}
	public void setArrears(List<Arrears> arrears) {
		this.arrears = arrears;
	}
	public boolean isStock() {
		return isStock;
	}
	public void setStock(boolean isStock) {
		this.isStock = isStock;
	}
	public double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public int getPrintCount() {
		return printCount;
	}
	public void setPrintCount(int printCount) {
		this.printCount = printCount;
	}
	public double getUnPayAmount() {
		return unPayAmount;
	}
	public void setUnPayAmount(double unPayAmount) {
		this.unPayAmount = unPayAmount;
	}
	public int getCarSalesId() {
		return carSalesId;
	}
	public void setCarSalesId(int carSalesId) {
		this.carSalesId = carSalesId;
	}
	public int getIsCommbine() {
		return isCommbine;
	}
	public void setIsCommbine(int isCommbine) {
		this.isCommbine = isCommbine;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public String getCarSalesState() {
		return carSalesState;
	}
	public void setCarSalesState(String carSalesState) {
		this.carSalesState = carSalesState;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCarSalesTime() {
		return carSalesTime;
	}
	public void setCarSalesTime(String carSalesTime) {
		this.carSalesTime = carSalesTime;
	}
	public int getIsPromotion() {
		return isPromotion;
	}
	public void setIsPromotion(int isPromotion) {
		this.isPromotion = isPromotion;
	}
	public String getCarSalesNo() {
		return carSalesNo;
	}
	public void setCarSalesNo(String carSalesNo) {
		this.carSalesNo = carSalesNo;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public CarSalesContact getContact() {
		return contact;
	}
	public void setContact(CarSalesContact contact) {
		this.contact = contact;
	}
	public List<CarSalesProductData> getProductList() {
		return productList;
	}
	public void setProductList(List<CarSalesProductData> productList) {
		this.productList = productList;
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
	public double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}
	public double getCarSalesDiscount() {
		return carSalesDiscount;
	}
	public void setCarSalesDiscount(double carSalesDiscount) {
		this.carSalesDiscount = carSalesDiscount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	public String getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public String getSalesUser() {
		return salesUser;
	}
	public void setSalesUser(String salesUser) {
		this.salesUser = salesUser;
	}
	public String getSalesPhone() {
		return salesPhone;
	}
	public void setSalesPhone(String salesPhone) {
		this.salesPhone = salesPhone;
	}
	public String getDriverUser() {
		return driverUser;
	}
	public void setDriverUser(String driverUser) {
		this.driverUser = driverUser;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	@Override
	public String toString() {
		return "CarSales [id=" + id + ", carSalesId=" + carSalesId
				+ ", carSalesNo=" + carSalesNo + ", storeId=" + storeId
				+ ", storeName=" + storeName + ", contactId=" + contactId
				+ ", contact=" + contact + ", carSalesAmount=" + carSalesAmount
				+ ", actualAmount=" + actualAmount + ", productList="
				+ productList + ", payAmount=" + payAmount
				+ ", carSalesDiscount=" + carSalesDiscount + ", unPayAmount="
				+ unPayAmount + ", note=" + note + ", isPromotion="
				+ isPromotion + ", carSalesTime=" + carSalesTime
				+ ", carSalesState=" + carSalesState + ", isCommbine="
				+ isCommbine + ", image1=" + image1 + ", image2=" + image2
				+ ", printCount=" + printCount + ", salesDate=" + salesDate
				+ ", carNo=" + carNo + ", salesUser=" + salesUser
				+ ", salesPhone=" + salesPhone + ", driverUser=" + driverUser
				+ ", driverPhone=" + driverPhone + ", status=" + status
				+ ", carId=" + carId + ", returnAmount=" + returnAmount
				+ ", isStock=" + isStock + ", arrears=" + arrears + "]";
	}
	
	
	
	
}
