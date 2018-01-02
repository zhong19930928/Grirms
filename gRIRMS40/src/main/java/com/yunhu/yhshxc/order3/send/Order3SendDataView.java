package com.yunhu.yhshxc.order3.send;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class Order3SendDataView {
	private View view = null;
	private TextView tv_order3_send_data_ptoduct_name;
	private TextView tv_order3_send_z;
	private LinearLayout ll_order2_send_total;
	private EditText et_order3_send_total;
	private LinearLayout ll_order3_send_data_product_list;
	private Context context;
	private List<Order3SendDataViewC> sViewList = new ArrayList<Order3SendDataViewC>();
	public Order3SendDataView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order3_send_data_view, null);
		tv_order3_send_data_ptoduct_name = (TextView)view.findViewById(R.id.tv_order3_send_data_ptoduct_name);
		tv_order3_send_z = (TextView)view.findViewById(R.id.tv_order3_send_z);
		ll_order2_send_total = (LinearLayout)view.findViewById(R.id.ll_order2_send_total);
		et_order3_send_total = (EditText)view.findViewById(R.id.et_order3_send_total);
		ll_order3_send_data_product_list = (LinearLayout)view.findViewById(R.id.ll_order3_send_data_product_list);
		et_order3_send_total.addTextChangedListener(watcher);
	}
	
	public void setData(List<Order3SendData> list){
		ll_order3_send_data_product_list.removeAllViews();
		if (list!=null && !list.isEmpty()) {
			if (list.size() == 1) {//说明没有合并的
				Order3SendDataViewC cView = new Order3SendDataViewC(context);
				Order3SendData data = list.get(0);
				cView.setData(data);
				cView.isShowNumber(false);
				addChileView(cView.getView());
				ll_order2_send_total.setVisibility(View.GONE);
				tv_order3_send_data_ptoduct_name.setText(data.getProductName());
				if (data.getIsPresent() == 2) {//是赠品
					tv_order3_send_z.setVisibility(View.VISIBLE);
				}else{
					tv_order3_send_z.setVisibility(View.GONE);
				}
			}else{
				ll_order2_send_total.setVisibility(View.VISIBLE);
				for (int i = 0; i < list.size(); i++) {
					Order3SendData data = list.get(i);
					Order3SendDataViewC cView = new Order3SendDataViewC(context);
					cView.setData(data);
					cView.isShowNumber(true);
					addChileView(cView.getView());
					sViewList.add(cView);
					tv_order3_send_data_ptoduct_name.setText(data.getProductName());
					if (data.getIsPresent() == 2) {
						tv_order3_send_z.setVisibility(View.VISIBLE);
					}else{
						tv_order3_send_z.setVisibility(View.GONE);
					}
				}
			}
		}
	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			try {
				if (!TextUtils.isEmpty(str)) {
					totalChange(Double.parseDouble(str));
				}else{
					throw new Exception();
				}
			} catch (Exception e) {
				totalChange(0);
			}
			
		}
	};
	
	private double subNumber;
	private void totalChange(double toatal){
		subNumber = toatal;
		if (toatal>0) {
			for (int i = 0; i < sViewList.size(); i++) {
				Order3SendDataViewC c = sViewList.get(i);
				Order3SendData data = c.getData();
				double unsent  = data.getUnSendNumber();
				if (i == sViewList.size() - 1) {//如果是最后一个，无论剩余多少都分给最后一个
					data.setSendNumber(subNumber);
				}else{
					if (subNumber >= unsent) {
						data.setSendNumber(unsent);
						subNumber = toatal - unsent;
					}else if(subNumber > 0){
						data.setSendNumber(subNumber);
						subNumber = 0;
					}else{
						data.setSendNumber(subNumber);
					}
				}
				c.setData(data);
			}
		}else{
			for (int i = 0; i < sViewList.size(); i++) {
				Order3SendDataViewC c = sViewList.get(i);
				Order3SendData data = c.getData();
				data.setSendNumber(0);
				c.setData(data);
			}
		}
	}
	
	public void addChileView(View view){
		ll_order3_send_data_product_list.addView(view);
//		ll_order2_send_total.setVisibility(View.GONE);
	}
	
	public void addChileListView(List<View> views){
		if (views!=null && !views.isEmpty()) {
			for (int i = 0; i < views.size(); i++) {
				View view = views.get(i);
				ll_order3_send_data_product_list.addView(view);
			}
		}
//		else{
//			ll_order2_send_total.setVisibility(View.GONE);
//		}
	}
	
	public View getView(){
		return view;
	}
}
