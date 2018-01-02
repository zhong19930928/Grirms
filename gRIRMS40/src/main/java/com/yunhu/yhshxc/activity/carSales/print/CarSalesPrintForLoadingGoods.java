package com.yunhu.yhshxc.activity.carSales.print;

import java.util.List;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.content.Context;
import android.text.TextUtils;

public class CarSalesPrintForLoadingGoods extends AbsCarSalesPrint {
	private CarSales carSales;
	private boolean isLoading;
	public CarSalesPrintForLoadingGoods(Context context,boolean isLoading) {
		super(context);
		this.isLoading = isLoading;
	}
	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
		if (carSales!=null) {
			List<CarSalesProductData> datas = carSales.getProductList();
			for(CarSalesProductData d :datas){
//				if(d.getIsCarSalesMain() == 1){//如果是主产品
					for (Element e : columns) {
						if (e.getType() == Element.TYPE_TEXT) {
							switch (e.getVariable()) {
								case 8://产品名称
									String name = d.getProductName();
									e.setContent(TextUtils.isEmpty(d.getProductName())?"":name);
									break;
								case 9://数量
									e.setContent(PublicUtils.formatDouble(d.getActualCount()));
									break;	
								case 11://单位
									String unit = TextUtils.isEmpty(d.getProductUnit())?"":d.getProductUnit();
									e.setContent(unit);
									break;
							}
						}
						if (e.getContent() == null) {
							e.setContent("");
						}
						printElement(e);
					}
					printNewLine();
//				}
			}
		}
	}
	@Override
	public void printRow(List<Element> columns) {
		super.printRow(columns);
		for (Element e : columns) {
			if (e.getContent() == null) {
				e.setContent("");
			}
			if (e.getType() == Element.TYPE_TEXT) {
				switch (e.getVariable()) {
				
					case 1://1	企业名称
//						e.setContent(SharedPreferencesUtil.getInstance(context).getCompanyName());
						if(isLoading){
							e.setContent("装 货 单");
						}else{
							e.setContent("卸 货 单");
						}
						
						break;
					case 2://订单编号
						e.setContent(carSales.getCarSalesNo());
						break;
					case 3://	订单时间
						e.setContent(carSales.getCarSalesTime());
						break;
					case 4://	订单客户
						e.setContent(SharedPreferencesForCarSalesUtil.getInstance(context).getCarSalesStoreName());
						break;
					case 18://留言
						String note = carSales.getNote();
						e.setContent(TextUtils.isEmpty(note)?"":note);
						break;
					
				}
			}
			printElement(e);
		}
	}
	public CarSales getCarSales() {
		return carSales;
	}

	public void setCarSales(CarSales carSales) {
		this.carSales = carSales;
	}

}
