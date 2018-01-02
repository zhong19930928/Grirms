package com.yunhu.yhshxc.activity.carSales.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.content.Context;
import android.text.TextUtils;

public class CarSalesPrintForQianKuan extends AbsCarSalesPrint {
	private Map<Integer,Arrears> list = new HashMap<Integer,Arrears>();
	private String mount;
	public String getMount() {
		return mount;
	}
	public void setMount(String mount) {
		this.mount = mount;
	}
	public CarSalesPrintForQianKuan(Context context) {
		super(context);
	}
	@Override
	public void printLoop(List<Element> columns) {
		super.printLoop(columns);
			List<Arrears> listNew = new ArrayList<Arrears>();
			Set<Integer> set = list.keySet();
			Iterator<Integer> it = set.iterator();
			while(it.hasNext()){
				int key = it.next();
				if(list.get(key).getSkAmount()!=0){
					listNew.add(list.get(key));
				}else if(list.get(key).getIsOver() == 1){
					listNew.add(list.get(key));
				}
			}
			for(Arrears d :listNew){
					for (Element e : columns) {
						if (e.getType() == Element.TYPE_TEXT) {
							switch (e.getVariable()) {
								case 8://店面名称
									String name = TextUtils.isEmpty(d.getName())?"":d.getName();
									e.setContent("店面名称："+name);
									break;
								case 9://时间
									e.setContent("时间："+d.getTime());
									break;	
								case 13://欠款
									e.setContent("欠款："+PublicUtils.formatDouble(d.getArrearsAmount())+"元");
									break;
								case 11://本次收款
									e.setContent("本次收款："+PublicUtils.formatDouble(d.getSkAmount())+"元");
									break;
								case 12://是否结清
									if(d.getIsOver() == 1){
										e.setContent("结清：是");
									}else{
										e.setContent("结清：否");
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
						e.setContent("收取欠款");
						break;
					
					case 18://收回金额
						e.setContent(TextUtils.isEmpty(mount)?"":mount+"元");
						break;
					
				}
			}
			printElement(e);
		}
	}
	public Map<Integer, Arrears> getList() {
		return list;
	}
	public void setList(Map<Integer, Arrears> list) {
		this.list = list;
	}
	
	

}
