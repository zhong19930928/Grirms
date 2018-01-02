package com.yunhu.yhshxc.nearbyVisit.comp;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.nearbyVisit.bo.NearbyStyle;

public class AbsNearbyComp {
	private View view;
	private LinearLayout ll_nearby_comp_abs;
	private TextView tv_nearby_comp_name_abs;
	private TextView tv_line_orientation;
//	private TextView tv_line;
	private String value;
	protected Context context;
	private NearbyStyle nearbyStyle;
	
	public AbsNearbyComp(Context mContext) {
		this.context = mContext;
		view = View.inflate(mContext, R.layout.abs_nearby_comp, null);
		ll_nearby_comp_abs = (LinearLayout)view.findViewById(R.id.ll_nearby_comp_abs);
		tv_nearby_comp_name_abs = (TextView)view.findViewById(R.id.tv_nearby_comp_name_abs);
		tv_line_orientation = (TextView)view.findViewById(R.id.tv_line_orientation);
//		tv_line = (TextView)view.findViewById(R.id.tv_line);
	}
	
	public NearbyStyle getNearbyStyle() {
		return nearbyStyle;
	}
	public void setNearbyStyle(NearbyStyle nearbyStyle,boolean isVisible) {
		this.nearbyStyle = nearbyStyle;
		tv_nearby_comp_name_abs.setText(nearbyStyle.getName());
		if(isVisible){
			tv_nearby_comp_name_abs.setVisibility(View.VISIBLE);
//			tv_line.setVisibility(View.GONE);
			tv_line_orientation.setVisibility(View.VISIBLE);
		}else{
			tv_nearby_comp_name_abs.setVisibility(View.GONE);
//			tv_line.setVisibility(View.VISIBLE);
			tv_line_orientation.setVisibility(View.GONE);
		}
		
	}

	public String getValue() {
		return value;
	}

	protected View contentView () {
		return view;
	}
	
	public View getView(){
		ll_nearby_comp_abs.removeAllViews();
		ll_nearby_comp_abs.addView(contentView());
		return view;
	}

}
