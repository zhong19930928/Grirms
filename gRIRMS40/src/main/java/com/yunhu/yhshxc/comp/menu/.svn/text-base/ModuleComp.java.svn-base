package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class ModuleComp extends Menu{
	
	private Context mContext;
	private View view;
	private LinearLayout linearLayout;
	private TextView textView;
	
	public ModuleComp(Context context){
		mContext = context;
		view = View.inflate(mContext,R.layout.module_item, null);
	}
	
	
	@Override
	public View getView() {
	
		return view;
	}
	
	public void setModuleCompName(String content){
		textView.setText(content);
	}

	@Override
	public void setBackgroundResource(int resid) {
		linearLayout.setBackgroundResource(resid);
	}
}
