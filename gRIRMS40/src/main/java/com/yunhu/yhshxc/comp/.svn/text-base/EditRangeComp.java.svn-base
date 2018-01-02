package com.yunhu.yhshxc.comp;

import gcg.org.debug.JLog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

/**
 * 有范围的输入框
 * @author jishen
 *
 */
public class EditRangeComp extends Component {
	private String TAG = "EditRangeComp";
	private Context context;
	private EditText firstEditText,secondEditText;//输入值的两个空间
	private Func func;
	private View view;
	private String firstValue="",secondValue="";//输入的两个值
	
	private Bundle replenishBundle;//携带查询条件的bundle
	/**
	 * @return 查询条件的bundle
	 */
	public Bundle getReplenishBundle() {
		return replenishBundle;
	}
	/**
	 * 设置查询条件的bundle
	 * @param replenishBundle
	 */
	public void setReplenishBundle(Bundle replenishBundle) {
		this.replenishBundle = replenishBundle;
	}
	/**
	 * @return 第一个控件的值
	 */
	public EditText getFirstEditText() {
		return firstEditText;
	}
	/**
	 * @return 第二个控件的值
	 */
	public EditText getSecondEditText() {
		return secondEditText;
	}

	public EditRangeComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		view = View.inflate(context, R.layout.editrange_comp, null);
		firstEditText = (EditText) view.findViewById(R.id.firstEditTextComp);
		secondEditText = (EditText) view.findViewById(R.id.secondEditTextComp);
	}

	/**
	 * 
	 * @return 当前控件
	 */
	public Func getFunc() {
		return func;
	}

	/**
	 * 设置默认值
	 * @param value 默认值
	 */
	public void setValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			firstEditText.setText(value.split("~@@")[0]);
			secondEditText.setText(value.split("~@@")[1]);
		}
	}

	@Override
	public View getObject() {
		if(func.getDataType() != null){
			switch(func.getDataType()){
			case Func.DATATYPE_BIG_INTEGER:  //整数（4到10位）
				firstEditText.setSingleLine(true);
				firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				secondEditText.setSingleLine(true);
				secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				setMaxLength(10);
				break;
			case Func.DATATYPE_EDITCOMP_NUMERIC:  //整数（4到10位）
				firstEditText.setSingleLine(true);
				firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				secondEditText.setSingleLine(true);
				secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				setMaxLength(10);
				break;
			case Func.DATATYPE_SMALL_INTEGER:  // 小整数（<=4位）
				firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				firstEditText.setSingleLine(true);
				secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				secondEditText.setSingleLine(true);
				setMaxLength(4);
				break;
			case Func.DATATYPE_DECIMAL:  // 小数类型
				firstEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				firstEditText.setSingleLine(true);
				secondEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				secondEditText.setSingleLine(true);
				break;
			case Func.DATATYPE_PWD:  // 密码类型
				firstEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
				secondEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			case Func.DATATYPE_AREATEXT:  // 文本域类型
				firstEditText.setLines(3);
				secondEditText.setLines(3);
				if(func.getLength() != null){
					setMaxLength(func.getLength());
				}
				break;
			default:
				firstEditText.setSingleLine(true);
				secondEditText.setSingleLine(true);
				if(func.getLength() != null){//设置最大输入个数
					setMaxLength(func.getLength());
				}
				break;
			}
		}else{
			firstEditText.setSingleLine(true);
			secondEditText.setSingleLine(true);
			if(func.getLength() != null){
				setMaxLength(func.getLength());
			}
		}
        
		initData();// 初始化数据
		firstEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				firstValue = s.toString();
				value=firstValue+"~@@"+secondValue;
				
				if(TextUtils.isEmpty(firstValue) || TextUtils.isEmpty(secondValue)){
					value="";
				}
				
				Log.d(TAG, "firstValue==>"+firstValue);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		secondEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				secondValue = s.toString();
				value=firstValue+"~@@"+secondValue;
				
				if(TextUtils.isEmpty(firstValue) || TextUtils.isEmpty(secondValue)){
					value="";
				}
				
				Log.d(TAG, "secondValue==>"+secondValue);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

//		value=firstValue+"@"+secondValue;
		JLog.d(TAG, "vaule==>"+value);
		
		return view;
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		String key=func.getFuncId()+"";
		if(replenishBundle.containsKey(key) && func.getType()==Func.TYPE_EDITCOMP){//从查询条件budle中获取值
			String itemValue = replenishBundle.getString(key);// 查询数据库中当前对应的值
			if(itemValue.split("~@@").length==1){
				firstValue=itemValue.split("~@@")[0];
				firstEditText.setText(firstValue);
			}else if(itemValue.split("~@@").length==2){
				firstValue=itemValue.split("~@@")[0];
				firstEditText.setText(firstValue);
				secondValue=itemValue.split("~@@")[1];
				secondEditText.setText(secondValue);
			}
			value = itemValue;
			JLog.d(TAG, "itemValue==>" + itemValue);
		}
	}

	@Override
	public void setIsEnable(boolean isEnable) {//设置控件是否可用
		getObject().setEnabled(isEnable);
	}
	/**
	 * 设置最大输入个数
	 * @param maxLength 最大值
	 */
	private void setMaxLength(int maxLength) {
		firstEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
		secondEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
	}
	
}
