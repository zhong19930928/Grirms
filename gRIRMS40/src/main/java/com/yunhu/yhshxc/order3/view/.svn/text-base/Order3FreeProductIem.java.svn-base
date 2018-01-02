package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3FreeProductIem {
	private View view;
	private TextView tv_onsale_name;
	private TextView tv_product_name;
	private TextView tv_product_num;
	private TextView tv_product_unit;
	private Context context;
	private boolean isDiscount;
	Order3PromotionDB db;
	Order3Util order3Util;
	public Order3FreeProductIem(Context context,boolean isDiscount){
		this.context = context;
		this.isDiscount = isDiscount;
		view = View.inflate(context, R.layout.order3_zengpin_item, null);
		tv_onsale_name = (TextView) view.findViewById(R.id.tv_onsale_name);
		tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
		tv_product_num = (TextView) view.findViewById(R.id.tv_product_num);
		tv_product_unit = (TextView) view.findViewById(R.id.tv_product_unit);
		TextPaint tp = tv_onsale_name.getPaint();
		tp.setFakeBoldText(true); 
		db = new Order3PromotionDB(context);
		order3Util = new Order3Util(context);
	}
	public View getView(){
		return view;
	}
	/**
	 * 赠送产品
	 * @param data
	 */
	public void initFreeProductData(Order3ProductData data){
		if(isDiscount==true){
			if(data != null){
				Order3Promotion p = db.findPromotionByPromotionId(data.getPromotionId());
				Order3Product pro = order3Util.product(data.getProductId(), data.getUnitId());
				tv_onsale_name.setText(p!=null?p.getName():"");
				tv_product_name.setText(pro!=null?pro.getName():"");
				tv_product_num.setText(PublicUtils.formatDouble(data.getOrderCount()));
				tv_product_unit.setText(data.getProductUnit());
			}
		}
	}
	/**
	 * 总和减免
	 * @param data
	 */
	public void initDiscountData(Order3ProductData data){
		if(isDiscount == false){
			if(data != null ){
				Order3Promotion p = db.findPromotionByPromotionId(data.getPromotionId());
				if (p!=null) {
					tv_onsale_name.setText(p.getName());
					tv_product_name.setText("减免金额");
					tv_product_num.setText(PublicUtils.formatDouble(data.getActualAmount()));
					tv_product_unit.setText("元");
				}
			}
		}
	}
	/**
	 * 单品减免
	 * @param data
	 */
	public void initDiscountData2(Order3ProductData data) {
		if(isDiscount == false){
			if(data != null ){
				Order3Promotion p = db.findPromotionByPromotionId(data.getPromotionId());
				if (p!=null) {
					tv_onsale_name.setText(p.getName());
					tv_product_name.setText("减免金额");
					tv_product_num.setText(PublicUtils.formatDouble(data.getActualAmount()));
					tv_product_unit.setText("元");
				}
			}
		}
	}
	
}
