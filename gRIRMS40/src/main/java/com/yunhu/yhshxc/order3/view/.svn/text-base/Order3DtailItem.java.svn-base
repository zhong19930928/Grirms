package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3DtailItem {
	private View view;
	private TextView zengpin_order_detail_tv_num;
	private TextView zengpin_order_detail_tv_name;
	private TextView zengpin_order_detail_tv_data;
	private TextView zengpin_order_detail_tv_price;
	private TextView zengpin_order_detail_tv_jine;
	private TextView zengpin_order_detail_tv_discount;
	private LinearLayout ll_zhekou;
	private Context context;
	private Order3Util util;
	public Order3DtailItem(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_zengpin_order_detail_item, null);
		zengpin_order_detail_tv_num = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_num);
		zengpin_order_detail_tv_name = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_name);
		zengpin_order_detail_tv_data = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_data);
		zengpin_order_detail_tv_price = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_price);
		zengpin_order_detail_tv_jine = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_jine);
		zengpin_order_detail_tv_discount = (TextView) view.findViewById(R.id.zengpin_order_detail_tv_discount);
		ll_zhekou = (LinearLayout) view.findViewById(R.id.ll_zhekou);
		util = new Order3Util(context);
	}
	public View getView(){
		return view;
	}
	public void initData(int index,Order3ShoppingCart data){
		
		if(data!=null){
			zengpin_order_detail_tv_num.setText(String.valueOf(index));
			Order3Product product = util.product(data.getProductId(), data.getUnitId());
			if(product!=null){
				zengpin_order_detail_tv_name.setText(product.getName());
				zengpin_order_detail_tv_data.setText("数量"+PublicUtils.formatDouble(data.getNumber())+product.getUnit());
				zengpin_order_detail_tv_price.setText(PublicUtils.formatDouble(product.getPrice())+"元");
				if((data.getDisAmount()+data.getDiscountPrice())!=0){
					zengpin_order_detail_tv_jine.setText("金额"+PublicUtils.formatDouble(data.getDisAmount()+data.getDiscountPrice())+"元");
				}else{
					zengpin_order_detail_tv_jine.setText("金额"+PublicUtils.formatDouble(data.getSubtotal())+"元");
				}
				double zhekou = data.getPreAmount()+data.getPrePrice();
				if(zhekou == 0){
					ll_zhekou.setVisibility(View.INVISIBLE);
				}else{
					ll_zhekou.setVisibility(View.VISIBLE);
					zengpin_order_detail_tv_discount.setText(PublicUtils.formatDouble(zhekou)+"元");
				}
				
			}
		}
	}
	public void initData2(int index,Order3ProductData data){
		zengpin_order_detail_tv_num.setText(String.valueOf(index));
		if(data!=null){
			Order3Product product = util.product(data.getProductId(), data.getUnitId());
			if(product!=null){
				zengpin_order_detail_tv_name.setText(product.getName());
				zengpin_order_detail_tv_data.setText("数量"+PublicUtils.formatDouble(data.getOrderCount())+data.getProductUnit());
				zengpin_order_detail_tv_price.setText(PublicUtils.formatDouble(product.getPrice())+"元");
				zengpin_order_detail_tv_jine.setText("金额"+PublicUtils.formatDouble(data.getActualAmount())+"元");
				double a = data.getOrderAmount() -data.getActualAmount();
				if(a == 0){
					ll_zhekou.setVisibility(View.INVISIBLE);
				}else{
					ll_zhekou.setVisibility(View.VISIBLE);
					zengpin_order_detail_tv_discount.setText(PublicUtils.formatDouble(a)+"元");
				}
			}
		}
	}
}
