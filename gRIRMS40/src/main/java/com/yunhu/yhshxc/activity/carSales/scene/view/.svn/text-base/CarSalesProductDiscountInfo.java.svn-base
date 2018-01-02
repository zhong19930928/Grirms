package com.yunhu.yhshxc.activity.carSales.scene.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class CarSalesProductDiscountInfo {
	private View view;
	private CheckBox cb;
	private Context context;
	private TextView tv_order3_cuxiao;
	public CarSalesProductDiscountInfo(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_product_detai_discount_item, null);
		cb = (CheckBox) view.findViewById(R.id.cb_order3_detail_discount);
		tv_order3_cuxiao = (TextView) view.findViewById(R.id.tv_order3_cuxiao);
	}
	public void initData(CarSalePromotionSyncInfo data){
		StringBuffer sb = new StringBuffer();
		sb.append(data.getSyncInfoName()).append("\n");
		if(!TextUtils.isEmpty(data.getSyncDisFunction())){
			sb.append(data.getSyncDisFunction()).append("\n");
		}
		sb.append(data.getSyncValidTerm());
		tv_order3_cuxiao.setText(sb.toString());
	}
	public View getView(){
		return view;
	}
	public CheckBox getCheckBox(){
		return cb;
	}
}
