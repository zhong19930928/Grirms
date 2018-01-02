package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3PromotionInfo;

public class Order3DiscountItem {
	private View view;
//	private TextView tv_zengpin_zhekou_item_num;
	private TextView tv_zengpin_zhekou_item_info;
	private TextView tv_zengpin_zhekou_item_content;
	private Context context;
	public Order3DiscountItem(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_zengpin_zhekou_item, null);
//		tv_zengpin_zhekou_item_num = (TextView) view.findViewById(R.id.tv_zengpin_zhekou_item_num);
		tv_zengpin_zhekou_item_info = (TextView) view.findViewById(R.id.tv_zengpin_zhekou_item_info);
		tv_zengpin_zhekou_item_content = (TextView) view.findViewById(R.id.tv_zengpin_zhekou_item_content);
	}
	public View getView(){
		return view;
	}
	public void initData(int index,Order3PromotionInfo data){
		if(data != null){
//			tv_zengpin_zhekou_item_num.setText(String.valueOf(index));
			tv_zengpin_zhekou_item_info.setText(data.getTitle());
			tv_zengpin_zhekou_item_content.setText(data.getDetails());
		}else{
//			tv_zengpin_zhekou_item_num.setText("无");
		}
		
	}
}
