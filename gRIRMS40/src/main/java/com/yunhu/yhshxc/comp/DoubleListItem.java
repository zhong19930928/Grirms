package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 双向列表item
 * @author jishen
 *
 */
public class DoubleListItem {
	private Context mContext;
	private View view;

	private TextView tv_content;//内容
	private TextView tv_data;//日期
	private TextView tv_creater;//创建人
	private LinearLayout doubleListItemLayout;

	public DoubleListItem(Context context) {
		mContext = context;
		view = View.inflate(context, R.layout.double_list_item, null);
		tv_creater = (TextView) view.findViewById(R.id.double_list_item_creater);
		tv_content = (TextView) view.findViewById(R.id.double_list_item_content);
		tv_data = (TextView) view.findViewById(R.id.double_list_item_data);
		doubleListItemLayout=(LinearLayout)view.findViewById(R.id.double_List_Item);
	}


	/**
	 * @return 当前view
	 */
	public View getView() {
		return view;
	}

	public void setCreater(String creater){
		this.tv_creater.setText(creater);
	}
	
	public void setContent(String content) {
		this.tv_content.setText(content);
	}
	
	public void setDate(String tv_data) {
		this.tv_data.setText(tv_data);
	}
	
	public void setDoubleListItemBackground(int id){
		doubleListItemLayout.setBackgroundResource(id);
	}
}
