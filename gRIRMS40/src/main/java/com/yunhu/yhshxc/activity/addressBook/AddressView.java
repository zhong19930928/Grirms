package com.yunhu.yhshxc.activity.addressBook;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class AddressView {
	private Context context;
	private View view;
	private TextView catalog;
	private TextView title;
	private TextView tv_telephone_number;
	
	public AddressView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.address_all_view,null);
		title = (TextView) view.findViewById(R.id.title);
		catalog = (TextView) view.findViewById(R.id.catalog);
		tv_telephone_number = (TextView) view.findViewById(R.id.tv_telephone_number);
	}
	public View getView(){
		return view;
	}
	public void init(AdressBookUser user){
		if(user!=null){
			title.setText(user.getUn());
			tv_telephone_number.setText(user.getPn());
		}else{
			title.setText("");
			tv_telephone_number.setText("");
		}
	}
	public TextView getTextView(){
		return catalog;
	}
}
