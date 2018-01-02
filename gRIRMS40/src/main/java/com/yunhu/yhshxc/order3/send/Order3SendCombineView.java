package com.yunhu.yhshxc.order3.send;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SendCombineView {
	private View view = null;
	private TextView tv_combine_khmc;
	private TextView tv_combine_date;
	private TextView tv_combine_amount;
	private TextView tv_combine_state;
	private CheckBox cb_order3_combine;
	private Order3 order = null;
	private Order3DB db = null;
	public Order3SendCombineView(Context context) {
		db = new Order3DB(context);
		view = View.inflate(context, R.layout.order3_send_combine_view, null);
		tv_combine_khmc = (TextView)view.findViewById(R.id.tv_combine_khmc);
		tv_combine_date = (TextView)view.findViewById(R.id.tv_combine_date);
		tv_combine_amount = (TextView)view.findViewById(R.id.tv_combine_amount);
		tv_combine_state = (TextView)view.findViewById(R.id.tv_combine_state);
		cb_order3_combine = (CheckBox)view.findViewById(R.id.cb_order3_combine);
		cb_order3_combine.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					order.setIsCommbine(1);
				}else{
					order.setIsCommbine(0);
				}
				db.updateOrder3(order);
			}
		});
	}
	
	public void setData(Order3 order){
		if (order!=null) {
			this.order = order;
			tv_combine_khmc.setText(order.getStoreName());
			tv_combine_date.setText(order.getOrderTime());
			tv_combine_amount.setText(PublicUtils.formatDouble(order.getActualAmount()) + " 元");
			tv_combine_state.setText(order.getOrderState());
			if (order.getIsCommbine() == 1) {
				cb_order3_combine.setChecked(true);
			}else{
				cb_order3_combine.setChecked(false);
			}
		}
	}
	
	
	public boolean isCombine(){
		return cb_order3_combine.isChecked();
	}
	
	public View getView(){
		return view;
	}
}
