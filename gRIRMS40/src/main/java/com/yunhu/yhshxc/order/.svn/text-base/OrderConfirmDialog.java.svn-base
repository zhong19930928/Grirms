package com.yunhu.yhshxc.order;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class OrderConfirmDialog extends Dialog {
	private Object data;
	
	private TextView txtTitle;
	private TextView txtMsg;
	private Button btnOk;
	private Button btnCancel;

	public OrderConfirmDialog(Context context) {
		super(context);
		preInitialize(context);
	}

	public OrderConfirmDialog(Context context, int theme) {
		super(context, theme);
		preInitialize(context);
	}

	public OrderConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		preInitialize(context);
	}

	private void preInitialize(Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		int padding1 = (int)(10 * density), padding2 = (int)(20 * density), buttonPadding = (int)(5 * density);
		
		LinearLayout container = new LinearLayout(context);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setBackgroundResource(R.color.home_menu_fix);
		
		txtTitle = new TextView(context);
		txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		txtTitle.setTextColor(Color.WHITE);
		txtTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		txtTitle.setPadding(padding1, padding1, padding1, padding1);
		txtTitle.setText(R.string.TIP);
		container.addView(txtTitle);
		
		txtMsg = new TextView(context);
		txtMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		txtMsg.setTextColor(Color.WHITE);
		txtMsg.setGravity(Gravity.CENTER);
		txtMsg.setPadding(padding2, padding2, padding2, padding2);
		container.addView(txtMsg);
		
		LinearLayout buttonContainer = new LinearLayout(context);
		buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
		buttonContainer.setPadding(padding1, padding1, padding1, padding1);
		buttonContainer.setGravity(Gravity.CENTER);
		container.addView(buttonContainer);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
//		params.rightMargin = paddingSmall;
		btnOk = new Button(context);
		btnOk.setTextColor(Color.WHITE);
		btnOk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		btnOk.setText("确定");
		btnOk.setBackgroundResource(R.drawable.func_detail_submit_btn);
		btnOk.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
		buttonContainer.addView(btnOk, params);
		
		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		params.leftMargin = padding1;
		btnCancel = new Button(context);
		btnCancel.setTextColor(Color.WHITE);
		btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		btnCancel.setText("取消");
		btnCancel.setBackgroundResource(R.drawable.func_detail_submit_btn);
		btnCancel.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
		buttonContainer.addView(btnCancel, params);
		
		setContentView(container, new ViewGroup.LayoutParams(context.getResources().getDisplayMetrics().widthPixels - padding2, LayoutParams.WRAP_CONTENT));
		
		View root = container.getRootView();
		root.setBackgroundColor(Color.TRANSPARENT);
	}
	
	public TextView getMessageText() {
		return txtMsg;
	}
	
	public Button getOkButton() {
		return btnOk;
	}
	
	public Button getCancelButton() {
		return btnCancel;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
