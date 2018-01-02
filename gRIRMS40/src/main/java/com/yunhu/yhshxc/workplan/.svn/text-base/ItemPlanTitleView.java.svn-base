package com.yunhu.yhshxc.workplan;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class ItemPlanTitleView {
	public Context context;
	private View view;
	private TextView tv ;
	public ItemPlanTitleView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.item_trace_title, null);
		tv= (TextView) view.findViewById(R.id.plan_item_title);
	}
	public View getView(){
		return view;
		
	}
	public void setTitle(String s){
		tv.setText(s);
	}
}
