package com.yunhu.yhshxc.nearbyVisit.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class NearbyVisitStoreDetailItem {

	private View view;
	private TextView tv_nearby_visit_store_detail_item_name;
	private TextView tv_nearby_visit_store_detail_item_value;
	private Context context;
	public NearbyVisitStoreDetailItem(Context mCotext) {
		this.context = mCotext;
		view = View.inflate(mCotext, R.layout.nearby_visit_store_detail_item, null);
		tv_nearby_visit_store_detail_item_name = (TextView)view.findViewById(R.id.tv_nearby_visit_store_detail_item_name);
		tv_nearby_visit_store_detail_item_value = (TextView)view.findViewById(R.id.tv_nearby_visit_store_detail_item_value);
	}
	
	public void setData(String name,String value){
		if (!TextUtils.isEmpty(name)) {
			tv_nearby_visit_store_detail_item_name.setText(name);
		}else{
			tv_nearby_visit_store_detail_item_name.setText("");
		}
		if (!TextUtils.isEmpty(value)) {
			tv_nearby_visit_store_detail_item_value.setText(value);
		}else{
			tv_nearby_visit_store_detail_item_value.setText("");
		}
//		StringBuffer buffer = new StringBuffer();
//		if (!TextUtils.isEmpty(name)) {
//			buffer.append(name).append(" : ");
//		}
//		if (!TextUtils.isEmpty(value)) {
//			buffer.append(value);
//		}
//		String str = buffer.toString();
//		int bstart=str.indexOf(name+" : ");
//		int bend=bstart+(name+" : ").length();
//		
//		SpannableStringBuilder style=new SpannableStringBuilder(str); 
////		style.setSpan(new BackgroundColorSpan(Color.RED),bstart,bend,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   
//        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.visit_view_update_content)),bstart,bend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
//        style.setSpan(new StyleSpan(Typeface.BOLD), bstart, bend,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		tv_nearby_visit_store_detail_item_name.setText(style);
	}
	
	public View getView(){
		return view;
	}
}
