package com.yunhu.yhshxc.order3.bo;

import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodeId;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodeLabel;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeNodePid;

public class Order3ProductCtrl  {
	
	private String id;
	@TreeNodeId
	private String cId;
	@TreeNodePid
	private String pId;
	@TreeNodeLabel
	private String label;
//	private Order3Product product;
	private int count;
	private int productId;
	private int unitId;
	private boolean isProductLevel;//是否是单品层级
	
	
	
	
	public boolean isProductLevel() {
		return isProductLevel;
	}
	public void setProductLevel(boolean isProductLevel) {
		this.isProductLevel = isProductLevel;
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
//	public Order3Product getProduct() {
//		return product;
//	}
//	public void setProduct(Order3Product product) {
//		this.product = product;
//	}
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Order3ProductCtrl [id=" + id + ", cId=" + cId + ", pId=" + pId
				+ ", label=" + label + ", count="
				+ count + ", productId=" + productId + ", unitId=" + unitId
				+ "]";
	}
	
	
}
