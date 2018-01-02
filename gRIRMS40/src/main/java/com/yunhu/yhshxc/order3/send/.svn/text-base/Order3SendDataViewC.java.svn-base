package com.yunhu.yhshxc.order3.send;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.db.Order3ProductDataDB;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SendDataViewC {
	private View view = null;
	private LinearLayout ll_order3_send_data_view_c;
	private TextView tv_order3_send_data_view_c_order_num;
	private TextView tv_order3_send_data_view_c_order_date;
	private TextView tv_order3_send_data_view_c_amount;
	private TextView tv_order3_send_data_view_c_unsend;
	private EditText et_order3_send_data_view_c_send;
	private Order3SendData data = null;
	private Context context;
	public Order3SendDataViewC(final Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order3_send_data_view_c, null);
		ll_order3_send_data_view_c = (LinearLayout)view.findViewById(R.id.ll_order3_send_data_view_c);
		tv_order3_send_data_view_c_order_num = (TextView)view.findViewById(R.id.tv_order3_send_data_view_c_order_num);
		tv_order3_send_data_view_c_order_date = (TextView)view.findViewById(R.id.tv_order3_send_data_view_c_order_date);
		tv_order3_send_data_view_c_amount = (TextView)view.findViewById(R.id.tv_order3_send_data_view_c_amount);
		tv_order3_send_data_view_c_unsend = (TextView)view.findViewById(R.id.tv_order3_send_data_view_c_unsend);
		et_order3_send_data_view_c_send = (EditText)view.findViewById(R.id.et_order3_send_data_view_c_send);
		et_order3_send_data_view_c_send.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				double sendCount = 0;
				if (!TextUtils.isEmpty(str)) {
					sendCount = Double.parseDouble(str);
				}
				new Order3ProductDataDB(context).updateOrder3ProductDataUnSendCount(data.getDataId(), sendCount);
			}
		});
		controlAmount();
	}
	
	//配送单上是否显示金额 1不要 2要 默认是2
	private void controlAmount(){
		int isAmount = SharedPreferencesForOrder3Util.getInstance(context).getIsAmount();
		if (isAmount == 1) {
			tv_order3_send_data_view_c_amount.setVisibility(View.INVISIBLE);
		}else{
			tv_order3_send_data_view_c_amount.setVisibility(View.VISIBLE);
		}
	}
	
	public void setData(Order3SendData data){
		if (data!=null) {
			this.data = data;
			tv_order3_send_data_view_c_order_num.setText(data.getOrderNmber());
			tv_order3_send_data_view_c_order_date.setText(data.getDate());
			tv_order3_send_data_view_c_amount.setText("金额:"+PublicUtils.formatDouble(data.getAmount())+"元");
			String unit = TextUtils.isEmpty(data.getUnit())?"":data.getUnit();
			tv_order3_send_data_view_c_unsend.setText(PublicUtils.formatDouble(data.getUnSendNumber())+unit);
			double send = data.getSendNumber();
			if (send > 0) {
				et_order3_send_data_view_c_send.setText(PublicUtils.formatDouble(data.getSendNumber()));
			}else{
				et_order3_send_data_view_c_send.setText("");
				et_order3_send_data_view_c_send.setHint("本次配送");
			}
			
		}
	}
	
	public Order3SendData getData(){
		return data;
	}
	
	public void isShowNumber(boolean show){
		ll_order3_send_data_view_c.setVisibility(show ? View.VISIBLE:View.GONE);
	}
	
	public View getView(){
		return view;
	}
	
}
