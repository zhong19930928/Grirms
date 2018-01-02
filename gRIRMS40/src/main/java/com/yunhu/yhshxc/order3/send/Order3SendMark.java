package com.yunhu.yhshxc.order3.send;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;

public class Order3SendMark {
	private View view = null;
	private TextView tv_order3_send_mark_amount;
	private EditText et_order3_send_sk;
	private EditText et_order3_send_message;
	private Context context;
	private LinearLayout ll_send_mark_amount;
	public Order3SendMark(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order3_send_mark, null);
		tv_order3_send_mark_amount = (TextView)view.findViewById(R.id.tv_order3_send_mark_amount);
		et_order3_send_sk = (EditText)view.findViewById(R.id.et_order3_send_sk);
		et_order3_send_message = (EditText)view.findViewById(R.id.et_order3_send_message);
		ll_send_mark_amount = (LinearLayout)view.findViewById(R.id.ll_send_mark_amount);
	}
	
	/**
	 * 设置总金额
	 * @param amount
	 * //配送单上是否显示金额 1不要 2要 默认是2
	 */
	public void setAmount(String amount){
		int isAmount = SharedPreferencesForOrder3Util.getInstance(context).getIsAmount();
		if (isAmount == 1) {
			ll_send_mark_amount.setVisibility(View.INVISIBLE);
		}else{
			ll_send_mark_amount.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(amount) && isAmount == 2) {//要显示金额的时候设置
			tv_order3_send_mark_amount.setText(amount + " 元");
		}
	}
	
	public String receiveAmount(){
		return et_order3_send_sk.getText().toString();
	}
	
	public String sendMessage(){
		return et_order3_send_message.getText().toString();
	}
	
	
	/**
	 * 收款金额
	 * @return
	 */
	public double SK(){
		double sk = 0;
		String str = et_order3_send_sk.getText().toString();
		if (!TextUtils.isEmpty(str)) {
			if(str.equals(".")){
				str = "0";
			}else if (str.startsWith(".")) {
				str = "0"+str;
			}else if(str.endsWith(".")){
				str = str+"0";
			}
			sk = Double.parseDouble(str);
		}
		return sk;
	}
	
	/**
	 * 备注信息
	 * @return
	 */
	public String mark(){
		String mark = et_order3_send_message.getText().toString();
		return mark;
	}
	
	public View getView(){
		return view;
	}
}
