package com.yunhu.yhshxc.nearbyVisit.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.nearbyVisit.bo.NearbyListItem;

public class NearbyVisitSearchResultItem {

	private View view;
	public String storeID;// 店面ID
	public String taskID;// 双向任务ID
	private NearbyListItem item;
	private TextView tv_1;
	private TextView tv_2;
	private TextView tv_3;
	private TextView tv_4;
	private Context context;

	public NearbyVisitSearchResultItem(Context mContext) {
		this.context = mContext;
		view = View.inflate(mContext, R.layout.nearby_visit_search_result_item,null);
		tv_1 = (TextView)view.findViewById(R.id.tv_nearby_search_result_item_1);
		tv_2 = (TextView)view.findViewById(R.id.tv_nearby_search_result_item_2);
		tv_3 = (TextView)view.findViewById(R.id.tv_nearby_search_result_item_3);
		tv_4 = (TextView)view.findViewById(R.id.tv_nearby_search_result_item_4);
	}

	public void setData(NearbyListItem data){
		this.item = data;
		if (!TextUtils.isEmpty(data.getTag_1())) {
			tv_1.setText(data.getTag_1());
		}else{
			tv_1.setText("");
		}
		if (!TextUtils.isEmpty(data.getTag_2())) {
			tv_2.setText(data.getTag_2());
		}else{
			tv_2.setText("");
		}
		if (!TextUtils.isEmpty(data.getTag_3())) {
			tv_3.setText(data.getTag_3());
		}else{
			tv_3.setText("");
		}
		if (!TextUtils.isEmpty(data.getTag_4())) {
			tv_4.setText(data.getTag_4());
		}else{
			tv_4.setText("");
		}
	}
	
	public View getView() {
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, item.getTag_1(), Toast.LENGTH_SHORT).show();
//			}
//		});
		return view;
	}
}
