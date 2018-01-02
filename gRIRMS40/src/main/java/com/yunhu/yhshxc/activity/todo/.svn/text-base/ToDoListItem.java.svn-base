package com.yunhu.yhshxc.activity.todo;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ToDoListItem {
	private View view = null;
	private TextView tv_mName;
	private TextView tv_sName;
	private TextView tv_num;
	private Context context;
	public ToDoListItem(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.todo_list_item, null);
		tv_mName = (TextView)view.findViewById(R.id.tv_todo_m_name);
		tv_sName = (TextView)view.findViewById(R.id.tv_todo_s_name);
		tv_num = (TextView)view.findViewById(R.id.tv_todo_num);
	}
	
	
	public void setData(Todo todo){
		if (todo!=null) {
			String mName = todo.getMenuName();
			if (!TextUtils.isEmpty(mName)) {
				tv_mName.setText(mName);
			}else{
				tv_mName.setText("");
			}
			String sName = todo.getStateName();
			if (!TextUtils.isEmpty(sName)) {
				tv_sName.setText(sName);
			}else{
				tv_sName.setText("");
			}
			
			int num = todo.getTodoNum();
			if (num!=0) {
				tv_num.setText(PublicUtils.getResourceString(context,R.string.you_have3)+num+PublicUtils.getResourceString(context,R.string.you_have4));
			}else{
				tv_num.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public View getView(){
		return view;
	}
}
