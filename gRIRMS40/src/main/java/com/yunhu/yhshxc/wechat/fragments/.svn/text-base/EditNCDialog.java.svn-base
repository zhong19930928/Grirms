package com.yunhu.yhshxc.wechat.fragments;

import com.yunhu.yhshxc.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditNCDialog extends Dialog{
	private Context context;
	private EditText ed_store_name;
	public EditNCDialog(Context context) {
		super(context);
	}
	public EditNCDialog(final Context context,int them,final TextView textView,final boolean title){
		super(context,them);
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.wechat_dialog_edit_nc,null);
		setContentView(view);// 为Dialoge设置自己定义的布局
		TextView tittle = (TextView) view.findViewById(R.id.tv_dialog_edit);
		 ed_store_name = (EditText) view
				.findViewById(R.id.ed_store_name);
		if(title){
			tittle.setText(R.string.wechat_content34);
			ed_store_name.setHint(R.string.wechat_content34);
		}else{
			tittle.setText(R.string.wechat_content33);
			ed_store_name.setHint(R.string.wechat_content33);
		}
		String str = textView.getText().toString().trim();
		if(!TextUtils.isEmpty(str)){
			String sContent = str.replaceAll("\r|\n", "");
			ed_store_name.setText(sContent);
		}else{
			ed_store_name.setText(str);
		}
		
		LinearLayout btn_add_store_confirm = (LinearLayout) view
					.findViewById(R.id.btn_add_store_confirm);
		btn_add_store_confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (ed_store_name.getText().toString().equals("")) {
					if(title){
						Toast.makeText(getContext(), R.string.wechat_content34, Toast.LENGTH_SHORT)
						.show();
					}else{
						Toast.makeText(getContext(), R.string.wechat_content33, Toast.LENGTH_SHORT)
						.show();
					}
					
				} else {
//					if(title){
//						SharedPrefrencesForWechatUtil.getInstance(context).setNiCheng(ed_store_name.getText().toString());
//					}else{
//						SharedPrefrencesForWechatUtil.getInstance(context).setQianMing(ed_store_name.getText().toString());
//					}
					String str = ed_store_name.getText().toString().trim();
					if(!TextUtils.isEmpty(str)){
						String sContent = str.replaceAll("\r|\n", "");
						textView.setText(sContent);
					}else{
						textView.setText(str);
					}
					
					dismiss();
				}

			}
		});

		LinearLayout btn_add_store_cancel = (LinearLayout) view.findViewById(R.id.btn_add_store_cancel);

		btn_add_store_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			dismiss();

			}
		});
		this.setCancelable(false);

	}

}
