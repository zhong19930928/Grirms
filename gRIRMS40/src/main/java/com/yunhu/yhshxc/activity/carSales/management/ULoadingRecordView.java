package com.yunhu.yhshxc.activity.carSales.management;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;

public class ULoadingRecordView {
	private View view;
	private Context context;
	private TextView tv_loading_time;
	private TextView tv_loading_state;
	private boolean isTitle;
	public ULoadingRecordView(Context context,boolean isTitle){
		this.context = context;
		this.isTitle = isTitle;
		view = View.inflate(context, R.layout.car_sales_loading_record_item, null);
		tv_loading_time = (TextView) view.findViewById(R.id.tv_loading_time);
		tv_loading_state = (TextView) view.findViewById(R.id.tv_loading_state);
	}
	public View getView(){
		return view;
	}
	public void initData(CarSales data){
		if(isTitle == true){
			view.setBackgroundColor(context.getResources().getColor(R.color.order3_search_yulan_bg));
			tv_loading_time.setText("卸车日期");
			tv_loading_state.setText("审批状态");
		}else if(data!=null){
			String time = data.getCarSalesTime();
			tv_loading_time.setText(time);
//			if(!TextUtils.isEmpty(time)){
//				String[] s = time.split(" ");
//				tv_loading_time.setText(s[0]);
//			}
			tv_loading_state.setText(data.getCarSalesState());
		}
	}
}
