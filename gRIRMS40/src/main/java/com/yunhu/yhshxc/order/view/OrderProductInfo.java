package com.yunhu.yhshxc.order.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 订单产品名称信息
 * @author jishen
 *
 */
public class OrderProductInfo {
	private View view=null;
	private TextView tv_name;
	public OrderProductInfo(Context mContext,String name) {
		view=View.inflate(mContext, R.layout.order_product_info, null);
		tv_name=(TextView)view.findViewById(R.id.tv_order_product_info_name);
		tv_name.setText(name);
	}
	
	public View getView(){
		return view;
	}
	
}
