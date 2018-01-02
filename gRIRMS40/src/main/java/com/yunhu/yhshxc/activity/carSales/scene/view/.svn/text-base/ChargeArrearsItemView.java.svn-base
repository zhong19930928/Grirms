package com.yunhu.yhshxc.activity.carSales.scene.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ChargeArrearsItemView {
	private View view;
	private Context context;
	private TextView tv_time_charge;
	private TextView tv_qiankuan;
	private TextView tv_store_name;
	private EditText et_bencishoukuan;
	private CheckBox cb_charge_arrears;
	private OnCheckedIdOver onChecked;
	private Arrears data;
	private double subInput;//剩余收款
	private boolean isLast;
	public double getSubInput() {
		return subInput;
	}
	public void setSubInput(double subInput,boolean isLast) {
		this.subInput = subInput;
		this.isLast = isLast;
	}
	public ChargeArrearsItemView(Context context,OnCheckedIdOver onChecked){
		this.context = context;
		this.onChecked = onChecked;
		view = View.inflate(context, R.layout.car_sales_charge_arrears_item, null);
		tv_time_charge = (TextView) view.findViewById(R.id.tv_time_charge);
		tv_qiankuan = (TextView) view.findViewById(R.id.tv_qiankuan);
		tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
		et_bencishoukuan = (EditText) view.findViewById(R.id.et_bencishoukuan);
		cb_charge_arrears = (CheckBox) view.findViewById(R.id.cb_charge_arrears);
		et_bencishoukuan.addTextChangedListener(watcher);
		cb_charge_arrears.setOnCheckedChangeListener(changeListner);
	}
	public View getView(){
		return view;
	}
	public void initData(Arrears data,String name){
		if(data!=null){
			this.data = data;
			tv_store_name.setText(name);
			if(!TextUtils.isEmpty(data.getTime())){
//				String[] strs = data.getTime().split(" ");
//				tv_time_charge.setText(strs[0]);
				tv_time_charge.setText(data.getTime());
			}
			tv_qiankuan.setText(PublicUtils.formatDouble(data.getArrearsAmount()));
			if(subInput>=data.getArrearsAmount()){
				if(isLast){
					et_bencishoukuan.setText(PublicUtils.formatDouble(subInput));
				}else{
					et_bencishoukuan.setText(PublicUtils.formatDouble(data.getArrearsAmount()));
				}
			}else if(subInput != 0){
				et_bencishoukuan.setText(PublicUtils.formatDouble(subInput));
			}
		}
	}
	private OnCheckedChangeListener changeListner = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			onChecked.onChecked(isChecked, data);
		}
	};
	public interface OnCheckedIdOver{
		public void onChecked(boolean isChecked,Arrears data);
		public void onSkChanged(Editable s,Arrears data);
		public void onSingleChanged(boolean isSingle,CharSequence s);
	}
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
//			if(tv_qiankuan.hasFocus()){
				onChecked.onSingleChanged(false, s);
//			}
		}
		
		@Override
		public void afterTextChanged(Editable s) {
//			if(tv_qiankuan.hasFocus()){
				onChecked.onSingleChanged(true, s);
//			}
			onChecked.onSkChanged(s,data);
		}
	};
	public double setDatas(double inputs, boolean last) {
		double textin = Double.valueOf(!TextUtils.isEmpty(tv_qiankuan.getText().toString())?tv_qiankuan.getText().toString():"0");
		if (last) {
			if (inputs == 0) {
				et_bencishoukuan.setText("");
			} else {
				et_bencishoukuan.setText(PublicUtils.formatDouble(inputs));
			}
		} else {
			if (inputs == 0) {
				et_bencishoukuan.setText("");
				return 0;
			}
			if (inputs <= textin) {
				et_bencishoukuan.setText(PublicUtils.formatDouble(inputs));
				return 0;
			} else {
				double textin2 = inputs - textin;
				et_bencishoukuan.setText(PublicUtils.formatDouble(textin));
				return textin2;
			}
		}
		return 0;

	}

}
