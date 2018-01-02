package com.yunhu.yhshxc.comp;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.widget.ToastOrder;
/**
 * 可输入类型的扫描
 * @author qingli
 *
 */
public class ScanInputCodeTable extends ProductCode{


	public ScanInputCodeTable(Context context, Func func, CompDialog compDialog) {
		super(context, func, compDialog);
	}
	
	
	@Override
	public String scanBtnName() {
		return myContext.getString(R.string.scan_input_code);
	}
	
	@Override
	public int requestCode() {
		return 112;
	}
	
	private Dialog dialog = null;
	public void scanInputCodeInTable(final ScanInTableComp scanInTableComp, String code) {
		View tableCodeview = View.inflate(myContext, R.layout.comp_dialog, null);
		LinearLayout linearLayout = (LinearLayout) tableCodeview.findViewById(R.id.ll_compDialog);
		Button confirmBtn = (Button) tableCodeview.findViewById(R.id.comp_dialog_confirmBtn);//保存按钮
		Button cancelBtn = (Button) tableCodeview.findViewById(R.id.comp_dialog_cancelBtn);//取消按钮
		dialog = new Dialog(myContext, R.style.transparentDialog);
		linearLayout.addView(getObject());
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (TextUtils.isEmpty(value)) {
					ToastOrder.makeText(myContext, myContext.getResources().getString(R.string.please_imput_chuancode), ToastOrder.LENGTH_SHORT).show();
				}else{
					if (table_type == TABLE_TYPE_TABLE && scanInTableComp!=null) {
						scanInTableComp.setValue(value);
					}
				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (!TextUtils.isEmpty(code)) {
			setProductCode(code);
		}
		dialog.setContentView(tableCodeview);
		dialog.show();
		dialog.setCancelable(false);
	}

}
