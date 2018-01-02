package com.yunhu.yhshxc.order3.send;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SendPriviewListView {
	private View  view = null;
	private TextView tv_p_name;
	private TextView tv_p_number;
	private TextView tv_p_unit;
	private Context context;
	public Order3SendPriviewListView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order3_send_p_list_item, null);
		tv_p_name = (TextView)view.findViewById(R.id.tv_p_name);
		tv_p_number = (TextView)view.findViewById(R.id.tv_p_number);
		tv_p_unit = (TextView)view.findViewById(R.id.tv_p_unit);
	}
	
	
	public void setData(int index,Order3SendPriviewListData data){
		if(index%2==0){
			view.setBackgroundColor(context.getResources().getColor(R.color.order3_send_list_item_bg));
		}else{
			
		}
		tv_p_name.setText(data.getName());
		tv_p_number.setText(PublicUtils.formatDouble(data.getNumber()));
		tv_p_unit.setText(data.getUnit());
	}
	
	public View getView(){
		return view;
	}
}
