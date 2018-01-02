package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;


public class ButtonSmallComp extends Menu {
	private Context context;
	private TextView buttonTextFirst,buttonTextSecond;
	private View view;
	private LinearLayout backgroundLayoutFirst,backgroundLayoutSecond;
	public ButtonSmallComp(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.button_small_comp, null);
		backgroundLayoutFirst=(LinearLayout)view.findViewById(R.id.ll_button_comp_first);
		backgroundLayoutSecond=(LinearLayout)view.findViewById(R.id.ll_button_comp_second);
		buttonTextFirst = (TextView) view.findViewById(R.id.tv_button_comp_content_first);
		buttonTextSecond = (TextView) view.findViewById(R.id.tv_button_comp_content_second);
	}

	
	public void setFirstButtonText(String text) {
		buttonTextFirst.setText(text);
	}
	public void setSecondButtonText(String text) {
		buttonTextSecond.setText(text);
	}


	@Override
	public View getView() {
		return view;
	}

	public void setFirstBackgroundResource(int resid) {
		backgroundLayoutFirst.setBackgroundResource(resid);
	}
	public void setSecondBackgroundResource(int resid) {
		backgroundLayoutSecond.setBackgroundResource(resid);
	}

	public View getFirstView(){
		return backgroundLayoutFirst;
	}
	public View getSecondView(){
		return backgroundLayoutSecond;
	}

	@Override
	public void setBackgroundResource(int resid) {
		
		
	}

}
