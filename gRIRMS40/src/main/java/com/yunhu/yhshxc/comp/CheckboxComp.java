package com.yunhu.yhshxc.comp;

import gcg.org.debug.JLog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;

public class CheckboxComp extends Component implements OnCheckedChangeListener {
	private String TAG="CheckboxComp";
	private Context context;
	private boolean isChecked;// true表示选中 false表示没选中
	private Func func;
	private CheckBox chechBox;
	private TextView textView;
	private View view;
	private CompDialog compDialog;

	public Func getFunc() {
		return func;
	}


	public CheckboxComp(Context context,Func func,CompDialog compDialog) {
		this.compDialog=compDialog;
		this.context = context;
		this.func=func;
		view = View.inflate(context, R.layout.checkbox_comp, null);
		chechBox = (CheckBox) view.findViewById(R.id.checkBoxComp);
		textView = (TextView)view.findViewById(R.id.checkBoxText);
		textView.setText(func.getName());
		chechBox.setOnCheckedChangeListener(this);
	}

	@Override
	public View getObject() {
		initData();
		return view;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
		if (checked) {
			isChecked = true;
			this.value=Func.CHECKBOX_CHECKED+"";
		} else {
			isChecked = false;
			this.value=Func.CHECKBOX_NO_CHECKED+"";
		}
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setIsEnable(boolean isEnable) {
		getObject().setEnabled(isEnable);
	}
	
	/*
	 * 初始化数据
	 */
	private void initData() {
//		SubmitItem item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, compDialog.planId, 
//				compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType, String.valueOf(func.getFuncId()));
		SubmitItem item=null;
		if(isLink){
			item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, compDialog.planId, 
					compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType, String.valueOf(func.getFuncId()));
		}else{
			item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, compDialog.planId, 
					compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType, String.valueOf(func.getFuncId()));
		}
		if(item!=null){
			String itemValue = item.getParamValue();// 查询数据库中当前对应的值
			value = itemValue;
			JLog.d(TAG, "itemValue==>" + itemValue);
			if (value.equals(Func.CHECKBOX_CHECKED + "")) {
				chechBox.setChecked(true);
			}			
		}
	}
}
