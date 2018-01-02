package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SearchDetailItem {
	private View view;
	private TextView tv_search_ordetail_item_name;
	private TextView tv_search_ordetail_item_time;
	private TextView tv_search_ordetail_item_bianhao;
	private TextView tv_search_ordetail_item_status;
	private TextView tv_search_ordetail_item_money;
	private Context context;
	private boolean isTitle;
	public Order3SearchDetailItem(Context context,boolean isTitle){
		this.context = context;
		this.isTitle = isTitle;
		view = View.inflate(context, R.layout.order3_search_ordetail_item, null);
		tv_search_ordetail_item_name = (TextView) view.findViewById(R.id.tv_search_ordetail_item_name);
		tv_search_ordetail_item_time = (TextView) view.findViewById(R.id.tv_search_ordetail_item_time);
		tv_search_ordetail_item_bianhao = (TextView) view.findViewById(R.id.tv_search_ordetail_item_bianhao);
		tv_search_ordetail_item_status = (TextView) view.findViewById(R.id.tv_search_ordetail_item_status);
		tv_search_ordetail_item_money = (TextView) view.findViewById(R.id.tv_search_ordetail_item_money);
	
	}
	public View getView(){
		return view;
	}
	public void initData(Order3 data){
		if(isTitle == true){
			view.setBackgroundColor(context.getResources().getColor(R.color.order3_search_yulan_bg));
			tv_search_ordetail_item_name.setText("客户名称");
			tv_search_ordetail_item_time.setText("订单日期");
			tv_search_ordetail_item_bianhao.setText("订单编号");
			tv_search_ordetail_item_status.setText("订单状态");
			tv_search_ordetail_item_money.setText("订单金额");
		}else if(data != null){
			tv_search_ordetail_item_money.setTextColor(context.getResources().getColor(R.color.red));
				tv_search_ordetail_item_name.setText(data.getStoreName());
				tv_search_ordetail_item_time.setText(data.getOrderTime());
				tv_search_ordetail_item_bianhao.setText(data.getOrderNo());
				tv_search_ordetail_item_status.setText(data.getOrderState());
				tv_search_ordetail_item_money.setText(PublicUtils.formatDouble(data.getActualAmount()) + " 元");
		
		}
		
	}
	
	
}
