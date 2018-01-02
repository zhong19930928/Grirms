package com.yunhu.yhshxc.order3.print;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.order3.Order3SearDetailActivity;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3PrintForSearch extends AbsOrder3Print{
	private Order3 order;
	private List<Order3ProductData> mainData;
	private List<Order3ProductData> zhekouData;
	private List<Order3ProductData> zengpinData;
	private String dicountAll;
	Order3SearDetailActivity activity;
	Order3PromotionDB db;
	Order3Util order3Util;
	Order3Contact contract;
	public Order3PrintForSearch(Context context) {
		super(context);
		activity = (Order3SearDetailActivity) context;
		db = new Order3PromotionDB(context);
		order3Util = new  Order3Util(context);
	}
	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
		
		if(mainData!=null && !mainData.isEmpty()){
			for(Order3ProductData d :mainData){
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
									e.setContent(PublicUtils.formatDouble(a)+ " 元");
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
		if (zengpinData!=null && !zengpinData.isEmpty()&&zengpinData.size()>0) {
			for (Order3ProductData data : zengpinData) {
				
				Order3Promotion p = db.findPromotionByPromotionId(data.getPromotionId());
				Order3Product pro = order3Util.product(data.getProductId(), data.getUnitId());
				for (Element e : columns) {
					if (e.getType() == Element.TYPE_TEXT) {
						switch (e.getVariable()) {
						case 14:// 赠品名称
							 e.setContent(p!=null?p.getName():"");
							break;

						case 15:// 赠品内容
							String name =pro!=null?pro.getName():"";
							String num = PublicUtils.formatDouble(data.getOrderCount());
							String unit = data.getProductUnit();
							StringBuffer sb = new StringBuffer();
							sb.append(name).append(num).append(unit);
							 e.setContent(sb.toString());
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
	@Override
	public void printZhekou(List<Element> columns) {
		super.printZhekou(columns);
		if (zhekouData!=null && !zhekouData.isEmpty()&&zhekouData.size()>0) {
			for (Order3ProductData data : zhekouData) {
				Order3Promotion p = db.findPromotionByPromotionId(data.getPromotionId());
				for (Element e : columns) {
					if (e.getType() == Element.TYPE_TEXT) {
						switch (e.getVariable()) {
						case 17:// 折扣名称
							e.setContent(p.getName());
							break;
							
						case 18:// 折扣内容
							StringBuffer sb = new StringBuffer();
							if(data.getProductId()!=0){
								double a = data.getOrderAmount() - data.getActualAmount();
								sb.append("减免金额").append(PublicUtils.formatDouble(a)).append("元");
								e.setContent(sb.toString());
							}else{
								sb.append("减免金额").append(PublicUtils.formatDouble(data.getActualAmount())).append("元");
								e.setContent(sb.toString());
							}
							
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
						e.setContent("查 询 订 单");
						break;
					case 2://订单编号
						e.setContent(order.getOrderNo());
						break;
					case 4://订单客户
						e.setContent(order.getStoreName());
						break;
					case 5://联系人
						e.setContent(contract!=null && !TextUtils.isEmpty(contract.getUserName())?contract.getUserName():"");
						break;
					case 6://联系电话
						
						if (contract!=null) {
							StringBuffer sb = new StringBuffer();
							if(!TextUtils.isEmpty(contract.getUserPhone1())){
								sb.append(",").append(contract.getUserPhone1());
							}
							if(!TextUtils.isEmpty(contract.getUserPhone2())){
								sb.append(",").append(contract.getUserPhone2());
							}
							if(!TextUtils.isEmpty(contract.getUserPhone3())){
								sb.append(",").append(contract.getUserPhone3());
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
					case 25://	订单金额（总计）
						e.setContent(PublicUtils.formatDouble(order.getActualAmount()));
						break;
					case 26://总折扣
						e.setContent("折扣共"+dicountAll+"元");
						break;
					case 19://收款
						e.setContent(PublicUtils.formatDouble(order.getPayAmount())+ " 元");
						break;
					case 20://减免
						e.setContent(PublicUtils.formatDouble(order.getOrderDiscount())+ " 元");
						break;
					case 21://留言
						String note = order.getNote();
						e.setContent(TextUtils.isEmpty(note)?"":note);
						break;
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
	public List<Order3ProductData> getMainData(){
		return mainData;
	}
	public void setMainData(List<Order3ProductData> main){
		this.mainData = main;
	}
	public List<Order3ProductData> getZheKouData(){
		return zhekouData;
	}
	public void setZheKouData(List<Order3ProductData> zhekou){
		this.zhekouData = zhekou;
	}
	public List<Order3ProductData> getZengPinData(){
		return zengpinData;
	}
	public void setZengPinData(List<Order3ProductData> zengpin){
		this.zengpinData = zengpin;
	}
	public String getDiscount(){
		return dicountAll;
	}
	public void setDiscount(String dicountAll){
		this.dicountAll = dicountAll;
	}
	public Order3Contact getContact(){
		return contract;
	}
	public void setContact(Order3Contact contract){
		this.contract = contract;
	}
}
