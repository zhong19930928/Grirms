package com.yunhu.yhshxc.activity.addressBook;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressBookChilcdView {
	private Context context;
	private View view;
	private TextView tv_bumen_name;
	private TextView tv_bumen_number;
	private TextView bumen_role_name;
	private TextView tv_person_number;
	private LinearLayout ll_person;
	private LinearLayout ll_org;
	private AdressBookUserDB userDB;
	
	public AddressBookChilcdView(Context context){
		this.context = context;
		userDB = new AdressBookUserDB(context);
		view = View.inflate(context, R.layout.address_bumen_item_view, null);
		tv_bumen_name = (TextView) view.findViewById(R.id.tv_bumen_name);
		tv_bumen_number = (TextView) view.findViewById(R.id.tv_bumen_number);
		bumen_role_name = (TextView) view.findViewById(R.id.bumen_role_name);
		tv_person_number = (TextView) view.findViewById(R.id.tv_person_number);
		ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
		ll_org = (LinearLayout) view.findViewById(R.id.ll_org);
	}
	public View getView(){
		return view;
	}
	public void initData(AdressBookUser user){
		if(user!=null){
			if(user.isOrg()){
				ll_org.setVisibility(View.VISIBLE);
				ll_person.setVisibility(View.GONE);
				bumen_role_name.setText(user.getOn());
				int count = userDB.orgBookUserCount(user.getoId(),user.getOl());
				tv_person_number.setText(count+ PublicUtils.getResourceString(context,R.string.address_book_person));
			}else{
				ll_person.setVisibility(View.VISIBLE);
				ll_org.setVisibility(View.GONE);
				tv_bumen_name.setText(user.getUn());
				tv_bumen_number.setText(user.getPn());
			}
		}
	}
}
