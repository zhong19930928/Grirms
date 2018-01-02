package com.yunhu.yhshxc.location2;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class RemindDialog extends Dialog {
	
	private Context context;

	public RemindDialog(Context context,String content) {
		super(context);
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.location_gps_dialog, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		LinearLayout iv_gps_dialog_cancle = (LinearLayout) view.findViewById(R.id.iv_gps_dialog_cancle);
		LinearLayout ll_gps_dialog_confirm = (LinearLayout) view.findViewById(R.id.ll_gps_dialog_confirm);
		LinearLayout ll_gps_dialog_newlocation = (LinearLayout) view.findViewById(R.id.ll_gps_dialog_newlocation);
		TextView tv_gps_dialog_content = (TextView) view.findViewById(R.id.tv_gps_dialog_content);
		tv_gps_dialog_content.setText(content);
		ll_gps_dialog_newlocation.setOnClickListener(listener);
		iv_gps_dialog_cancle.setOnClickListener(listener);
		ll_gps_dialog_confirm.setOnClickListener(listener);
		
	}
	private android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

}
