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
public class ProductCodeInTableComp extends Component {
	private Context context;
	private Button currentButton;
	private Func func;
	private View view;
	public boolean codeSubmitControl = true;
	private String compareRuleIds;
	private String compareContent;
	
	public ProductCodeInTableComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		view = View.inflate(context, R.layout.product_code_in_table_comp, null);
		currentButton = (Button) view.findViewById(R.id.p_code_in_table_Comp);
		
	}

	public Func getFunc() {
		return func;
	}

	/**
	 * 设置当前控件的值
	 * @param value 当前值
	 */
	public void setValue(String value) {
		if (!TextUtils.isEmpty(value)){
			currentButton.setText(value);
		}else{
			currentButton.setText(context.getResources().getString(R.string.p_code_date_comp));
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

	public String getCompareRuleIds() {
		return compareRuleIds;
	}

	public void setCompareRuleIds(String compareRuleIds) {
		this.compareRuleIds = compareRuleIds;
	}

	public String getCompareContent() {
		return compareContent;
	}

	public void setCompareContent(String compareContent) {
		this.compareContent = compareContent;
	}
	
	
}
