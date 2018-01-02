package com.yunhu.yhshxc.activity.addressBook;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;

public class AddressBookFrView {
	private Context context;
	private View view;
	private TextView tv_bumen_name;
	private TextView tv_bumen_number;
	private TextView bumen_role_name;
	private TextView tv_person_number;
	private LinearLayout ll_person;
	private LinearLayout ll_org;
	private AdressBookUserDB userDB;
	private boolean isVisible;
	private CheckBox cb_group,cb_child;

	public AddressBookFrView(Context context,boolean isVisible){
		this.context = context;
		this.isVisible = isVisible;
		userDB = new AdressBookUserDB(context);
		view = View.inflate(context, R.layout.address_fragment_item_view, null);
		tv_bumen_name = (TextView) view.findViewById(R.id.title);
		tv_bumen_number = (TextView) view.findViewById(R.id.tv_telephone_number);
		bumen_role_name = (TextView) view.findViewById(R.id.title_name);
		tv_person_number = (TextView) view.findViewById(R.id.tv_person_number);
		ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
		ll_org = (LinearLayout) view.findViewById(R.id.ll_org);
		cb_child = (CheckBox) view.findViewById(R.id.cb_child);
//		cb_group = (CheckBox) view.findViewById(R.id.cb_child);
	}
	public View getView(){
		return view;
	}
	public void initData(final AdressBookUser user){
		if(user!=null){
			if(user.isOrg()){
				ll_org.setVisibility(View.VISIBLE);
				ll_person.setVisibility(View.GONE);
				bumen_role_name.setText(user.getOn());

//				int count = userDB.orgBookUserCount(user.getoId(),user.getOl());
				String[] ocs = user.getOc().split("-");
				int count = userDB.orgBookUserCount(Integer.parseInt(ocs[ocs.length-1]),ocs.length);
				tv_person_number.setText("("+count+")");
			}else{
				ll_person.setVisibility(View.VISIBLE);
				ll_org.setVisibility(View.GONE);
				tv_bumen_name.setText(user.getUn());
				tv_bumen_number.setText(user.getPn());
				if(user.getIsVisit()==1){
					cb_child.setChecked(true);
				}else{
					cb_child.setChecked(false);
				}
				if(isVisible){
					cb_child.setVisibility(View.VISIBLE);
				}else {
					cb_child.setVisibility(View.GONE);
				}
				cb_child.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(cb_child.isChecked()){
							user.setIsVisit(1);
						}else{
							user.setIsVisit(0);
						}
						new AdressBookUserDB(context).updateAdressUser(user);
					}
				});
			}
		}
	}
}
