package com.yunhu.yhshxc.order3.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

public class Order3DataPickerItem {
	private View view;
	private Context context;
	private Button confirmBtn;
	private Button cancelBtn;
	private LinearLayout linearLayout;
	private Dialog dialog;
	
	private OnSelectData onSelectData;
	public Order3DataPickerItem(Context context,OnSelectData onSelectData){
		this.context = context;
		this.onSelectData = onSelectData;
		view = View.inflate(context, R.layout.comp_dialog, null);
		linearLayout = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		confirmBtn = (Button) view.findViewById(R.id.comp_dialog_confirmBtn);//保存按钮
		cancelBtn = (Button) view.findViewById(R.id.comp_dialog_cancelBtn);//取消按钮
		confirmBtn.setOnClickListener(listner);
		cancelBtn.setOnClickListener(listner);
		TimeView t_second = new TimeView(context, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				dataNow = wheelTime;
			}
		});
		linearLayout.addView(t_second);
	}
	public View getView(){
		return view;
	}
	private String dataNow;
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.comp_dialog_confirmBtn:
				onSelectData.onSelectData(dataNow);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			case R.id.comp_dialog_cancelBtn:
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			default:
				break;
			}
		}
	};
	public interface OnSelectData{
		public void onSelectData(String dataNow);
	}
//	public String getDate(){
//		return dataNow;
//	}
	public Dialog getDialog(){
		dialog = new Dialog(context, R.style.transparentDialog);
		dialog.setContentView(getView());
		return dialog;
	}
	
}
