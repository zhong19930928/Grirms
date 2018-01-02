package com.yunhu.yhshxc.comp;



import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

/**
 * 表格中扫描控件的封装
 * @author jishen
 *
 */
public class ScanInTableComp extends Component {
	private Context context;
	private Button currentButton;
	private Func func;
	private View view;
	
	public ScanInTableComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		view = View.inflate(context, R.layout.scan_in_table_comp, null);
		currentButton = (Button) view.findViewById(R.id.scan_in_table_Comp);
		
	}

	public Func getFunc() {
		return func;
	}

	/**
	 * 设置当前控件的值
	 * @param value 当前值
	 */
	public void setValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			currentButton.setText(value);
		}else{
			currentButton.setText(context.getResources().getString(R.string.scan_date_comp));
		}
		this.value=value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#getObject()
	 */
	@Override
	public View getObject() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		currentButton.setEnabled(isEnable);
	}
	
	/**
	 * 
	 * @return 当前button
	 */
	public Button getCurrentButton() {
		return currentButton;
	}
}
