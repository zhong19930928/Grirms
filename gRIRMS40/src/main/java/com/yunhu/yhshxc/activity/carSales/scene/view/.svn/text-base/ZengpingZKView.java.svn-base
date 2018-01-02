package com.yunhu.yhshxc.activity.carSales.scene.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionInfo;

public class ZengpingZKView {
	private View view;
	private  Context context;
	private TextView tv_zengpin_zhekou_item_info;
	private TextView tv_zengpin_zhekou_item_content;
	public ZengpingZKView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_zengpin_zhekou_item, null);
		tv_zengpin_zhekou_item_info = (TextView) view.findViewById(R.id.tv_zengpin_zhekou_item_info);
		tv_zengpin_zhekou_item_content = (TextView) view.findViewById(R.id.tv_zengpin_zhekou_item_content);
	}
	public View getView(){
		return view;
	}
	public void initData(int index,CarSalesPromotionInfo data){
		if(data != null){
			tv_zengpin_zhekou_item_info.setText(data.getTitle());
			tv_zengpin_zhekou_item_content.setText(data.getDetails());
		}else{
//			tv_zengpin_zhekou_item_num.setText("无");
		}
		
	}
}
