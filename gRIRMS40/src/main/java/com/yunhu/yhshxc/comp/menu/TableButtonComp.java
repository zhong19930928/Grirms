package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * 表格中的删除按钮
 * @author gcg_jishen
 */
public class TableButtonComp extends Menu {
	private TextView buttonText;
	private View view;
	private LinearLayout backgroundLayout;
	public TableButtonComp(Context context) {
		view = View.inflate(context, R.layout.comp_table_delete, null);
		backgroundLayout=(LinearLayout)view.findViewById(R.id.ll_button_comp);
		buttonText = (TextView) view.findViewById(R.id.tv_button_comp_content);
	}

	
	public void setButtonText(String text) {
		buttonText.setText(text);
	}


	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		backgroundLayout.setBackgroundResource(resid);
	}

	public LinearLayout backgroundLayout(){
		return backgroundLayout;
	}
}
