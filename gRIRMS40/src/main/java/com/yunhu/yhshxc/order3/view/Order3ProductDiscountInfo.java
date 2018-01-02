package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3PromotionSyncInfo;

public class Order3ProductDiscountInfo {
	private View view;
	private CheckBox cb;
	private Context context;
	private TextView tv_order3_cuxiao;
	private OnSingleChecked onSingleChecked;
	private int index;
	public Order3ProductDiscountInfo(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_product_detai_discount_item, null);
		cb = (CheckBox) view.findViewById(R.id.cb_order3_detail_discount);
		tv_order3_cuxiao = (TextView) view.findViewById(R.id.tv_order3_cuxiao);
//		cb.setOnCheckedChangeListener(listner);
	}
	public void initData(Order3PromotionSyncInfo data){
//		cb.setText(data.getSyncInfoName());
		StringBuffer sb = new StringBuffer();
		sb.append(data.getSyncInfoName()).append("\n");
		if(!TextUtils.isEmpty(data.getSyncDisFunction())){
			sb.append(data.getSyncDisFunction()).append("\n");
		}
		sb.append(data.getSyncValidTerm());
		
//		cb.setText(	sb.toString());
		tv_order3_cuxiao.setText(sb.toString());
	}
//	private OnCheckedChangeListener listner = new OnCheckedChangeListener() {
//		
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			onSingleChecked.onChecked(isChecked,index);
//		}
//	};
	public View getView(){
		return view;
	}
	public CheckBox getCheckBox(){
		return cb;
	}
	public interface OnSingleChecked {
		public void onChecked(boolean isChecked,int position);
	}
	
}
