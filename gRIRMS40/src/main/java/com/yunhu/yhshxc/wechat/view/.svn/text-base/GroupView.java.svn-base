package com.yunhu.yhshxc.wechat.view;


import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.OrgUser;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

public class GroupView {
	private View view;
	private Context context;
	private CheckBox cb_group_item;
	public GroupView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.wechat_create_group_item, null);
		cb_group_item= (CheckBox) view.findViewById(R.id.cb_group_item);
	}
	
	public View getView(){
		return view;
	}
	
	public CheckBox getCheckBox(){
		return cb_group_item;
	}

	public void initData(OrgUser orgUser) {
		if(orgUser!=null){
			cb_group_item.setText(orgUser.getUserName());
		}
	}
	public void initZWData(OrgUser orgUser){
		if(orgUser!=null){
			cb_group_item.setText(orgUser.getRoleName());
		}
	}
}
