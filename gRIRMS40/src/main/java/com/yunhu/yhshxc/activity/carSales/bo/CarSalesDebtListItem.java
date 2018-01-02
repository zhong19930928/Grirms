package com.yunhu.yhshxc.activity.carSales.bo;

import java.util.List;


public class CarSalesDebtListItem {

	private String resultName;
	private String resultTotalPrice;
	public String getResultTotalPrice() {
		return resultTotalPrice;
	}

	public void setResultTotalPrice(String resultTotalPrice) {
		this.resultTotalPrice = resultTotalPrice;
	}

	private List<CarSalesDebtDetail> Data;

	

	public List<CarSalesDebtDetail> getData() {
		return Data;
	}

	public void setData(List<CarSalesDebtDetail> data) {
		Data = data;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

}
