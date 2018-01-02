package com.yunhu.yhshxc.order2.bo;

import org.json.JSONException;

import com.yunhu.yhshxc.order2.Order2Data;

public class NewOrder {
	private String storeId;
	private Order2 order;
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public Order2 getOrder() {
		return order;
	}
	public void setOrder(Order2 order) {
		this.order = order;
	}
	
	public void saveDB(){
		try {
			String json = Order2Data.order2Json(order);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
