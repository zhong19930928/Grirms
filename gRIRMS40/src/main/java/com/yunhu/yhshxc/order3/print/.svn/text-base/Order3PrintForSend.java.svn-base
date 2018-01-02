package com.yunhu.yhshxc.order3.print;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.yunhu.yhshxc.order3.send.Order3SendPriviewActivity;
import com.yunhu.yhshxc.order3.send.Order3SendPriviewListData;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3PrintForSend extends AbsOrder3Print{

	private Order3SendPriviewActivity activity;
	
	public Order3PrintForSend(Context context) {
		super(context);
		activity = (Order3SendPriviewActivity) context;
	}
	
	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
		if (!activity.order3SendPriviewListData.isEmpty()) {
			for (int i = 0; i < activity.order3SendPriviewListData.size(); i++) {
				Order3SendPriviewListData data = activity.order3SendPriviewListData.get(i);
				for (Element e : columns) {
					if (e.getType() == Element.TYPE_TEXT) {
						switch (e.getVariable()) {
						case 8:// 产品名称
							e.setContent(data.getName());
							break;
						case 9:// 数量
							e.setContent(PublicUtils.formatDouble(data.getNumber()));
							break;
						case 11:// 单位
							e.setContent(data.getUnit());
							break;

						}
					}
					if (e.getContent() == null) {
						e.setContent("");
					}
					printElement(e);
				}
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
			if (e.getType() == Element.TYPE_TEXT) {
				switch (e.getVariable()) {
				
					case 1://1	企业名称
//						e.setContent(SharedPreferencesUtil.getInstance(context).getCompanyName());
						e.setContent("送 货 单");
						break;
//					case 2://订单编号
//						e.setContent(activity.orderNo.replace("\n", "     "));
//						break;
					case 4://	订单客户
						e.setContent(activity.khmc);
						break;
					case 5://联系人
						e.setContent(activity.contact.replace("\n", ","));
						break;
					case 7://	订单金额（总计）
						e.setContent(activity.amount + " 元");
						break;
					case 18://留言
						String note = activity.sendMsg;
						e.setContent(TextUtils.isEmpty(note)?"":note);
						break;
					case 19://历史收款
						e.setContent(activity.amountHistory+"元");
						break;
					case 20://未付金额
						e.setContent(activity.unPayAmount+"元");
						break;
					case 21://本次收款
						e.setContent(activity.orderAmount+"元");
						break;
				}
				if (e.getContent() == null) {
					e.setContent("");
				}
			}
			printElement(e);
		}
		
	
	}
	@Override
	public void printDingdanbianhao(List<Element> columns) {
		super.printDingdanbianhao(columns);
		if(!TextUtils.isEmpty(activity.orderNo)){
			List<String> list = new ArrayList<String>();
			String[] strs = activity.orderNo.split("\n");
			
			if(strs!=null&&strs.length>0){
				for(int i = 0; i<strs.length; i++){
					list.add(strs[i]);
				}
			}
			for(int i = 0;i<list.size(); i++){
				String[] s = list.get(i).split("	");
				if (s.length>=2) {
					for (Element e : columns) {
						if (e.getType() == Element.TYPE_TEXT) {
							switch (e.getVariable()) {
							
								case 2://订单编号
									e.setContent(s[0]);
									break;
								case 200://订单日期
									e.setContent(s[1]);
									break;
							}
							if (e.getContent() == null) {
								e.setContent("");
							}
						}
						printElement(e);
					}
				}
			}
		}
		
		
	}

}
