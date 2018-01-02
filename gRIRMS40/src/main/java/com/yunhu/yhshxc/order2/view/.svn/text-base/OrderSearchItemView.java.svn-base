package com.yunhu.yhshxc.order2.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order2.Order2Data;
import com.yunhu.yhshxc.order2.OrderSearchDetailActivity;
import com.yunhu.yhshxc.order2.bo.Order2;

public class OrderSearchItemView {
	private View view;
	private LinearLayout ll_order2_search_item_view;
	private Order2 order;
	private Context context;
	public OrderSearchItemView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order_search_item_view, null);
		ll_order2_search_item_view = (LinearLayout)view.findViewById(R.id.ll_order2_search_item_view);
	}
	
	public View getView(){
		return view;
	}
	
	public void setData(Order2 order){
		this.order = order;
	}
	
	public void initTitleData(String[] data){
		ll_order2_search_item_view.setBackgroundResource(R.color.order_item_title_bg);
		if (data!=null && data.length>0) {
			for (int i = 0; i < data.length; i++) {
				TextView tv_title = (TextView) View.inflate(context, R.layout.table_list_item_unit, null);
				tv_title.setGravity(Gravity.CENTER);//标题设置居中
				DisplayMetrics dm = new DisplayMetrics();
				dm = context.getResources().getDisplayMetrics(); 
				if (dm.density<=1.5){
					tv_title.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));
				}else if(dm.density<=3){
					tv_title.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
				}
				tv_title.setText(data[i]);
				tv_title.setTextColor(Color.rgb(0, 0, 0));
				tv_title.setTextSize(16);
				ll_order2_search_item_view.addView(tv_title);
			}
		}
	}
	
	public void initContentData(String[] data){
		ll_order2_search_item_view.setBackgroundResource(R.drawable.order2_item_view);
		if (data!=null && data.length>0) {
			for (int i = 0; i < data.length; i++) {
				TextView tv_title = (TextView) View.inflate(context, R.layout.table_list_item_unit, null);
				tv_title.setGravity(Gravity.CENTER);//标题设置居中
				DisplayMetrics dm = new DisplayMetrics();
				dm = context.getResources().getDisplayMetrics(); 
				if (dm.density<=1.5){
					tv_title.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));
				}else if(dm.density<=3){
					tv_title.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
				}
				tv_title.setText(data[i]);
				tv_title.setTextSize(16);
				if (i == data.length - 1) {
					tv_title.setId(-100);
					tv_title.setTextColor(Color.rgb(0, 0, 255));
					tv_title.setOnClickListener(listener);
				}else{
					tv_title.setTextColor(Color.rgb(0, 0, 0));
				}
				ll_order2_search_item_view.addView(tv_title);
			}
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case -100:
				operation();
				break;
			default:
				break;
			}
			
		}
	};
	
	private void operation(){
		try {
			Intent intent = new Intent(context, OrderSearchDetailActivity.class);
			String json = Order2Data.order2Json(order);
			intent.putExtra("orderJson", json);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
