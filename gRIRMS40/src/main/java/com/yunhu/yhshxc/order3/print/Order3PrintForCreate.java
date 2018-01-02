package com.yunhu.yhshxc.order3.print;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3PromotionInfo;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3PrintForCreate  extends AbsOrder3Print{

	private Order3 order;
	private List<Order3PromotionInfo> order3PromotionInfoList;
	public Order3PrintForCreate(Context context) {
		super(context);
	}

	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
		if (order!=null) {
			List<Order3ProductData> datas = order.getProductList();
			for(Order3ProductData d :datas){
				if(d.getIsOrderMain() == 1){//如果是主产品
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
									e.setContent(PublicUtils.formatDouble(d.getOrderPrice())+ " 元");
									break;
								case 12://金额（总价）
									e.setContent(PublicUtils.formatDouble(d.getActualAmount())+ " 元");
									break;
								case 13://折扣
									double a = d.getOrderAmount() - d.getActualAmount();
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
		if (order3PromotionInfoList!=null && !order3PromotionInfoList.isEmpty()) {
			for (Order3PromotionInfo info : order3PromotionInfoList) {
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
	public void printZhekou(List<Element> columns) {
		super.printZhekou(columns);
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
						e.setContent(order.getOrderNo());
						break;
					case 3://	订单时间
						e.setContent(order.getOrderTime());
						break;
					case 4://	订单客户
						e.setContent(SharedPreferencesForOrder3Util.getInstance(context).getOrder3StoreName());
						break;
					case 5://联系人
						Order3Contact contact = order.getContact();
						e.setContent(contact!=null && !TextUtils.isEmpty(contact.getUserName())?contact.getUserName():"");
						break;
					case 6://联系电话
						Order3Contact contact_ = order.getContact();
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
						e.setContent(PublicUtils.formatDouble(order.getActualAmount())+ " 元");
						break;
					case 16://收款
						e.setContent(PublicUtils.formatDouble(order.getPayAmount())+ " 元");
						break;
					case 17://减免
						e.setContent(PublicUtils.formatDouble(order.getOrderDiscount())+ " 元");
						break;
					case 18://留言
						String note = order.getNote();
						e.setContent(TextUtils.isEmpty(note)?"":note);
						break;
					case 161://未收款
						double price = order.getActualAmount()-order.getPayAmount();
						e.setContent(PublicUtils.formatDouble(price)+" 元");
				}
			}
			printElement(e);
		}
		
	
	}

	public Order3 getOrder() {
		return order;
	}

	public void setOrder(Order3 order) {
		this.order = order;
	}

	public List<Order3PromotionInfo> getOrder3PromotionInfoList() {
		return order3PromotionInfoList;
	}

	public void setOrder3PromotionInfoList(
			List<Order3PromotionInfo> order3PromotionInfoList) {
		this.order3PromotionInfoList = order3PromotionInfoList;
	}
	
	
}
