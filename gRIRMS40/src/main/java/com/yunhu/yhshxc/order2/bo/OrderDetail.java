package com.yunhu.yhshxc.order2.bo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.order.bo.ProductConf;
import com.yunhu.yhshxc.order2.SharedPreferencesForOrder2Util;

public class OrderDetail {

	private Dictionary product;
	private Dictionary productUnit;
	private double price;
	private double quantity;
//	private double actualQuantity;
	private double amount;
//	private double actualAmount;
	private String remark;
	private Integer status; // 订单状态（-1：取消、100：关闭）

	public Dictionary getProduct() {
		return product;
	}

	public void setProduct(Dictionary product) {
		this.product = product;
	}

	public Dictionary getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(Dictionary productUnit) {
		this.productUnit = productUnit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

//	public double getActualQuantity() {
//		return actualQuantity;
//	}
//
//	public void setActualQuantity(double actualQuantity) {
//		this.actualQuantity = actualQuantity;
//	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

//	public double getActualAmount() {
//		return actualAmount;
//	}
//
//	public void setActualAmount(double actualAmount) {
//		this.actualAmount = actualAmount;
//	}

	public static List<Dictionary> getProductRelatedList(String productId,Context context) {
		DictDB dictDB = new DictDB(context);
		List<ProductConf> productConfList = SharedPreferencesForOrder2Util.getInstance(context).getProductCtrl();
		int size = productConfList.size();
		ProductConf product = productConfList.get(size-1);
		String[] relatedColumnDid = dictDB.findRelatedColumnDid(product.getDictTable(),product.getDictDataId(),product.getDictCols(), productId);
		if(relatedColumnDid == null || relatedColumnDid.length != size){
			throw new NullPointerException("FUNC Exception");
		}
		List<Dictionary> list = new ArrayList<Dictionary>(size);
		Dictionary dict = null;
		for(int i= 0; i<size; i++){
			dict = dictDB.findDictByDid(productConfList.get(i).getDictTable(), productConfList.get(i).getDictDataId(), relatedColumnDid[i]);
			list.add(dict);
		}
		return list;
	}
	
	public static List<Dictionary> getUnit(String productId,Context context) {
		List<Dictionary> list = getProductRelatedList(productId,context);
		DictDB dictDB = new DictDB(context);
		ProductConf conf = SharedPreferencesForOrder2Util.getInstance(context).getUnitCtrl();
		String[] relatedCols = dictDB.findRelatedForeignkey(conf.getDictCols(), conf.getDictDataId());
		String[] values = new String[list.size()];
		int i = 0;
		for(Dictionary dict : list){
			values[i] = dict.getDid();
			i++;
		}
		List<Dictionary> dict = dictDB.findDictByRelatedColumn(conf.getDictTable(), conf.getDictDataId(),relatedCols, values);
		return dict;
	}
}
