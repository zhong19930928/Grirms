package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3PromotionSyncInfo;

public class Order3SyncInfoItem {
	private View view;
	private Context context;
	private TextView tv_discount_info1;
	private TextView tv_discount_number;
	private TextView tv_info_sync_item_time;//期限
	private TextView tv_info_sync_item_func;//方式
	private TextView tv_info_sync_item_content;//内容
	private TextView tv_info_sync_item_instructions;//说明
	public Order3SyncInfoItem(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_info_sync_item, null);
		tv_discount_number = (TextView) view.findViewById(R.id.tv_discount_number);
		tv_discount_info1 = (TextView) view.findViewById(R.id.tv_discount_info1);
		tv_info_sync_item_time = (TextView) view.findViewById(R.id.tv_info_sync_item_time);
		tv_info_sync_item_func = (TextView) view.findViewById(R.id.tv_info_sync_item_func);
		tv_info_sync_item_content = (TextView) view.findViewById(R.id.tv_info_sync_item_content);
		tv_info_sync_item_instructions = (TextView) view.findViewById(R.id.tv_info_sync_item_instructions);
	}
	public void initdata(int index,Order3PromotionSyncInfo data){
		if(data!=null){
			tv_discount_number.setText(String.valueOf(index+1));
			tv_discount_info1.setText(data.getSyncInfoName());
			tv_info_sync_item_time.setText(data.getSyncValidTerm());
			tv_info_sync_item_func.setText(data.getSyncDisFunction());
			tv_info_sync_item_content.setText(data.getSyncContent());
			tv_info_sync_item_instructions.setText(data.getSyncInstruction());
		}
	}
	public View getView(){
		return view;
	}
}
