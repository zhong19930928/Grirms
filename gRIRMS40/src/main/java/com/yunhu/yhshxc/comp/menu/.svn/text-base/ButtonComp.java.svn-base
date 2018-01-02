package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * button类型的控件
 * @author gcg_jishen
 *
 */
public class ButtonComp extends Menu {
	private Context context;
	private TextView buttonText;
	private View view;
	private LinearLayout backgroundLayout;
	public ButtonComp(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.button_comp, null);
		backgroundLayout=(LinearLayout)view.findViewById(R.id.ll_button_comp);
		buttonText = (TextView) view.findViewById(R.id.tv_button_comp_content);
	}

	/**
	 * 设置button文本标签
	 * @param text
	 */
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
