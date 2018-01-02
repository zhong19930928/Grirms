package com.yunhu.yhshxc.order3.send;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class Order3SendKH {
	
	private View view = null;
	private TextView tv_kh;//客户名称
	private TextView tv_lxr;//客户联系人
	private TextView tv_khdz;//客户地址
	private TextView tv_dd;//订单
	public Order3SendKH(Context context) {
		view = View.inflate(context, R.layout.order3_send_kh, null);
		tv_kh = (TextView)view.findViewById(R.id.tv_kh);
		tv_lxr = (TextView)view.findViewById(R.id.tv_lxr);
		tv_khdz = (TextView)view.findViewById(R.id.tv_khdz);
		tv_dd = (TextView)view.findViewById(R.id.tv_dd);
	}
	
	public void setKU(String kh){
		tv_kh.setText(kh);
	}
	
	public void setLXR(String lxr){
		tv_lxr.setText(lxr);
	}
	
	public void setKHDZ(String dz){
		tv_khdz.setText(dz);
	}
	
	public void setDD(String dd){
		tv_dd.setText(dd);
	}
	
	public View getVeiw(){
		return view;
	}
}
