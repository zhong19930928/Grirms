package com.yunhu.yhshxc.activity.carSales.bo;

import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodeId;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodeLabel;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodePid;

public class CarSalesProductCtrl  {
	
	private String id;
	@TreeNodeId
	private String cId;
	@TreeNodePid
	private String pId;
	@TreeNodeLabel
	private String label;
	private String levelLable;//所有上级的lable的组合，以","分开
//	private CarSalesProduct product;
	private int count;
	private int productId;
	private int unitId;
	private String unit;
	private String returnCount;//退货数量
	private String loadingCount;//装车数量
	private String unLoadingCount;//卸车数量
	private String replenishmentCount;//补货数量
	private String outCount;//缺货数量
	private String inverty;//库存盘点数量
	private boolean isProductLevel;//是否是单品层级
	private int dataId;

	
	
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getInverty() {
		return inverty;
	}
	public void setInverty(String inverty) {
		this.inverty = inverty;
	}
	public String getReplenishmentCount() {
		return replenishmentCount;
	}
	public void setReplenishmentCount(String replenishmentCount) {
		this.replenishmentCount = replenishmentCount;
	}
	public String getOutCount() {
		return outCount;
	}
	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}

	
	public String getLevelLable() {
		return levelLable;
	}
	public void setLevelLable(String levelLable) {
		this.levelLable = levelLable;
	}

	public boolean isProductLevel() {
		return isProductLevel;
	}
	public void setProductLevel(boolean isProductLevel) {
		this.isProductLevel = isProductLevel;
	}
	public String getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(String returnCount) {
		this.returnCount = returnCount;
	}
	public String getLoadingCount() {
		return loadingCount;
	}
	public void setLoadingCount(String loadingCount) {
		this.loadingCount = loadingCount;
	}
	public String getUnLoadingCount() {
		return unLoadingCount;
	}
	public void setUnLoadingCount(String unLoadingCount) {
		this.unLoadingCount = unLoadingCount;
	}
	public int getProductId() {
		return productId;
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
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CarSalesProductCtrl [id=" + id + ", cId=" + cId + ", pId="
				+ pId + ", label=" + label + ", levelLable=" + levelLable
				+ ", count=" + count + ", productId=" + productId + ", unitId="
				+ unitId + ", unit=" + unit + ", returnCount=" + returnCount
				+ ", loadingCount=" + loadingCount + ", unLoadingCount="
				+ unLoadingCount + ", replenishmentCount=" + replenishmentCount
				+ ", outCount=" + outCount + ", inverty=" + inverty
				+ ", isProductLevel=" + isProductLevel + ", dataId=" + dataId
				+ "]";
	}
	
	
}
