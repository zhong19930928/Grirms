package com.yunhu.yhshxc.nearbyVisit.comp;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yunhu.yhshxc.R;

public class NearbyCompEdit extends AbsNearbyComp{

	private EditText editText;
	public NearbyCompEdit(Context mContext) {
		super(mContext);
	}
	
	@Override
	protected View contentView() {
		View view = View.inflate(context, R.layout.nearby_comp_edit, null);
		editText = (EditText)view.findViewById(R.id.et_nearby_comp);
		return view;
	}
	
	@Override
	public String getValue() {
		String value = editText.getText().toString();
		return value;
	}
	
	public void setValue(String vlue){
		if(!TextUtils.isEmpty(vlue)&&editText!=null){
			editText.setText(vlue);
		}
		
	}

}
