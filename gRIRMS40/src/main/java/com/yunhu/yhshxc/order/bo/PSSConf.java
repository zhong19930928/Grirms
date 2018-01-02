package com.yunhu.yhshxc.order.bo;

/**
 * 进销存配置表
 * @author jishen
 *
 */
public class PSSConf {
	
	private int id; 
	private String phoneFun;  //进销存有哪些模块
	private String createOrderTimeConf; //（1、工作日2、每日3、每周）
	private String createOrderTimeWeekly; //每周几（7天的定义连接，每天占一个字符，0：没有被选择、1：被选择）例，0110101。
	private String createOrderStartTime; //创建订单开始时间
	private String createOrderEndTime; //创建订单结束时间
	private String salesTimeConf; //（1、工作日2、每日3、每周）
	private String salesTimeWeekly; //每周几（7天的定义连接，每天占一个字符，0：没有被选择、1：被选择）例，0110101。
	private String salesStartTime; //销量上报开始时间
	private String salesEndTime; //销量上报结束时间
	private String returnedTimeConf; //（1、工作日2、每日3、每周）
	private String returnedTimeWeekly; //每周几（7天的定义连接，每天占一个字符，0：没有被选择、1：被选择）例，0110101。
	private String returnedStartTime; //退货开始时间
	private String returnedEndTime; //退货结束时间
	private String returnedReasonDictTable; //退货原因字典表
	private String returnedReasonDictDataId; //退货原因显示字段
	private String stocktakeDifferDictTable; //盘货差异字典表
	private String stocktakeDifferDictDataId; //盘货差异显示字段
	private String isPriceEdit; //单价是否可修改
	private String orderPrintStyle;//订单样式
	private String stockPrintStyle;//进货样式
	private String dictOrderBy;//字典表排序列
	private String dictIsAsc;//字典表排序是否是生序
	
	
	
	public String getDictOrderBy() {
		return dictOrderBy;
	}
	public void setDictOrderBy(String dictOrderBy) {
		this.dictOrderBy = dictOrderBy;
	}

	public String getDictIsAsc() {
		return dictIsAsc;
	}
	public void setDictIsAsc(String dictIsAsc) {
		this.dictIsAsc = dictIsAsc;
	}
	
	
	public String getOrderPrintStyle() {
		return orderPrintStyle;
	}
	public void setOrderPrintStyle(String orderPrintStyle) {
		this.orderPrintStyle = orderPrintStyle;
	}

	public String getStockPrintStyle() {
		return stockPrintStyle;
	}
	public void setStockPrintStyle(String stockPrintStyle) {
		this.stockPrintStyle = stockPrintStyle;
	}
	public String getIsPriceEdit() {
		return isPriceEdit;
	}
	public void setIsPriceEdit(String isPriceEdit) {
		this.isPriceEdit = isPriceEdit;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneFun() {
		return phoneFun;
	}
	public void setPhoneFun(String phoneFun) {
		this.phoneFun = phoneFun;
	}
	
	public String getCreateOrderStartTime() {
		return createOrderStartTime;
	}
	public void setCreateOrderStartTime(String createOrderStartTime) {
		this.createOrderStartTime = createOrderStartTime;
	}
	public String getCreateOrderEndTime() {
		return createOrderEndTime;
	}
	public void setCreateOrderEndTime(String createOrderEndTime) {
		this.createOrderEndTime = createOrderEndTime;
	}
	public String getSalesStartTime() {
		return salesStartTime;
	}
	public void setSalesStartTime(String salesStartTime) {
		this.salesStartTime = salesStartTime;
	}
	public String getSalesEndTime() {
		return salesEndTime;
	}
	public void setSalesEndTime(String salesEndTime) {
		this.salesEndTime = salesEndTime;
	}
	public String getReturnedStartTime() {
		return returnedStartTime;
	}
	public void setReturnedStartTime(String returnedStartTime) {
		this.returnedStartTime = returnedStartTime;
	}
	public String getReturnedEndTime() {
		return returnedEndTime;
	}
	public void setReturnedEndTime(String returnedEndTime) {
		this.returnedEndTime = returnedEndTime;
	}
	public String getReturnedReasonDictTable() {
		return returnedReasonDictTable;
	}
	public void setReturnedReasonDictTable(String returnedReasonDictTable) {
		this.returnedReasonDictTable = returnedReasonDictTable;
	}
	public String getReturnedReasonDictDataId() {
		return returnedReasonDictDataId;
	}
	public void setReturnedReasonDictDataId(String returnedReasonDictDataId) {
		this.returnedReasonDictDataId = returnedReasonDictDataId;
	}
	public String getStocktakeDifferDictTable() {
		return stocktakeDifferDictTable;
	}
	public void setStocktakeDifferDictTable(String stocktakeDifferDictTable) {
		this.stocktakeDifferDictTable = stocktakeDifferDictTable;
	}
	public String getStocktakeDifferDictDataId() {
		return stocktakeDifferDictDataId;
	}
	public void setStocktakeDifferDictDataId(String stocktakeDifferDictDataId) {
		this.stocktakeDifferDictDataId = stocktakeDifferDictDataId;
	}
	public String getCreateOrderTimeConf() {
		return createOrderTimeConf;
	}
	public void setCreateOrderTimeConf(String createOrderTimeConf) {
		this.createOrderTimeConf = createOrderTimeConf;
	}
	public String getCreateOrderTimeWeekly() {
		return createOrderTimeWeekly;
	}
	public void setCreateOrderTimeWeekly(String createOrderTimeWeekly) {
		this.createOrderTimeWeekly = createOrderTimeWeekly;
	}
	
	public String getSalesTimeConf() {
		return salesTimeConf;
	}
	public void setSalesTimeConf(String salesTimeConf) {
		this.salesTimeConf = salesTimeConf;
	}
	public String getSalesTimeWeekly() {
		return salesTimeWeekly;
	}
	public void setSalesTimeWeekly(String salesTimeWeekly) {
		this.salesTimeWeekly = salesTimeWeekly;
	}
	public String getReturnedTimeConf() {
		return returnedTimeConf;
	}
	public void setReturnedTimeConf(String returnedTimeConf) {
		this.returnedTimeConf = returnedTimeConf;
	}
	public String getReturnedTimeWeekly() {
		return returnedTimeWeekly;
	}
	public void setReturnedTimeWeekly(String returnedTimeWeekly) {
		this.returnedTimeWeekly = returnedTimeWeekly;
	}
	
}
