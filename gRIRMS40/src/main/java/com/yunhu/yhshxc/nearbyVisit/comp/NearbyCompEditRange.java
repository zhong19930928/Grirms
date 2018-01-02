package com.yunhu.yhshxc.nearbyVisit.comp;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yunhu.yhshxc.R;

public class NearbyCompEditRange extends AbsNearbyComp{

	private EditText editTextFirst;
	private EditText editTextSecond;
	public NearbyCompEditRange(Context mContext) {
		super(mContext);
	}
	
	@Override
	protected View contentView() {
		View view = View.inflate(context, R.layout.nearby_comp_edit_range, null);
		editTextFirst = (EditText)view.findViewById(R.id.et_nearby_comp_first);
		editTextSecond = (EditText)view.findViewById(R.id.et_nearby_comp_second);
		return view;
	}
	
	public String startValue(){
		return editTextFirst.getText().toString();
	}
	
	public String endValue(){
		return editTextSecond.getText().toString();
	}
	@Override
	public String getValue() {
		String first = editTextFirst.getText().toString();
		String second = editTextSecond.getText().toString();
		return first+"~@@"+second;
	}
	public void setStartValue(String value){
		if(!TextUtils.isEmpty(value)&&editTextFirst!=null){
			editTextFirst.setText(value);
		}
		
	}
	public void setEndValue(String value){
		if(!TextUtils.isEmpty(value)&&editTextSecond!=null){
			editTextSecond.setText(value);
		}
		
	}

}
