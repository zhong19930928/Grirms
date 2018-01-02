package com.yunhu.yhshxc.activity.carSales.print;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionInfo;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

public class CarSalesPrintForSellingGoods extends AbsCarSalesPrint {
	private CarSales carSales;
	private List<CarSalesPromotionInfo> carSalesPromotionInfoList;
	public CarSalesPrintForSellingGoods(Context context) {
		super(context);
	}
	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
		if (carSales!=null) {
			List<CarSalesProductData> datas = carSales.getProductList();
			for(CarSalesProductData d :datas){
				if(d.getIsCarSalesMain() == 1){//如果是主产品
					for (Element e : columns) {
						if (e.getType() == Element.TYPE_TEXT) {
							switch (e.getVariable()) {
								case 8://产品名称
									String name = d.getProductName();
									e.setContent(TextUtils.isEmpty(d.getProductName())?"":name);
									break;
								case 9://数量
									String unit = TextUtils.isEmpty(d.getProductUnit())?"":d.getProductUnit();
									e.setContent(PublicUtils.formatDouble(d.getActualCount())+unit);
									break;	
								case 11://价格 （单价）
									e.setContent(PublicUtils.formatDouble(d.getCarSalesPrice())+ " 元");
									break;
								case 12://金额（总价）
									e.setContent(PublicUtils.formatDouble(d.getActualAmount())+ " 元");
									break;
								case 13://折扣
									double a = d.getCarSalesAmount() - d.getActualAmount();
									e.setContent(PublicUtils.formatDouble(a) + " 元");
									break;
							}
						}
						if (e.getContent() == null) {
							e.setContent("");
						}
						printElement(e);
					}
					printNewLine();
				}
			}
		}
	}
	@Override
	public void printPromotion(List<Element> columns) {
		super.printPromotion(columns);
		if (carSalesPromotionInfoList!=null && !carSalesPromotionInfoList.isEmpty()) {
			for (CarSalesPromotionInfo info : carSalesPromotionInfoList) {
				for (Element e : columns) {
					if (e.getType() == Element.TYPE_TEXT) {
						switch (e.getVariable()) {
						case 14:// 促销名称
							 e.setContent(info.getTitle());
							break;

						case 15:// 促销内容
							 e.setContent(info.getDetails());
							break;

						}
					}
					printElement(e);
				}
				printNewLine();
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
						e.setContent("订 货 单");
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
					case 5://联系人
						CarSalesContact contact = carSales.getContact();
						e.setContent(contact!=null && !TextUtils.isEmpty(contact.getUserName())?contact.getUserName():"");
						break;
					case 6://联系电话
						CarSalesContact contact_ = carSales.getContact();
						if (contact_!=null) {
							StringBuffer sb = new StringBuffer();
							if(!TextUtils.isEmpty(contact_.getUserPhone1())){
								sb.append(",").append(contact_.getUserPhone1());
							}
							if(!TextUtils.isEmpty(contact_.getUserPhone2())){
								sb.append(",").append(contact_.getUserPhone2());
							}
							if(!TextUtils.isEmpty(contact_.getUserPhone3())){
								sb.append(",").append(contact_.getUserPhone3());
							}
							if (sb.length()>0) {
								e.setContent(sb.substring(1));
							}else{
								e.setContent("");
							}
						}else{
							e.setContent("");
						}
						break;
					case 7://	订单金额（总计）
						e.setContent(PublicUtils.formatDouble(carSales.getActualAmount())+ " 元");
						break;
					case 16://收款
						e.setContent(PublicUtils.formatDouble(carSales.getPayAmount())+ " 元");
						break;
					case 17://减免
						e.setContent(PublicUtils.formatDouble(carSales.getCarSalesDiscount())+ " 元");
						break;
					case 18://留言
						String note = carSales.getNote();
						e.setContent(TextUtils.isEmpty(note)?"":note);
						break;
					case 161://未收款
						double price = carSales.getActualAmount()-carSales.getPayAmount();
						e.setContent(PublicUtils.formatDouble(price)+" 元");
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

	public List<CarSalesPromotionInfo> getCarSalesPromotionInfoList() {
		return carSalesPromotionInfoList;
	}

	public void setCarSalesPromotionInfoList(
			List<CarSalesPromotionInfo> carSalesPromotionInfoList) {
		this.carSalesPromotionInfoList = carSalesPromotionInfoList;
	}
}
