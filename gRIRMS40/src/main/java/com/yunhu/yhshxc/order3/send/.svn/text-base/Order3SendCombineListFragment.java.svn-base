package com.yunhu.yhshxc.order3.send;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3SendCombineListFragment extends Fragment{
	private View view = null;
	private LinearLayout tv_order3_combine;
	private ListView lv_order3_send_combine;
	private List<Order3> orderList = new ArrayList<Order3>();
	private CombineAdapter adapter = null;
	private Order3SendActivity sendActivity;
	private FrameLayout fl_send_combin;
	
	public Order3SendCombineListFragment() {
		adapter = new CombineAdapter();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		sendActivity = (Order3SendActivity) this.getActivity();
		view  = View.inflate(sendActivity, R.layout.order3_send_combine_list, null);
		tv_order3_combine  = (LinearLayout)view.findViewById(R.id.ll_order3_combine);
		tv_order3_combine.setOnClickListener(listener);
		lv_order3_send_combine = (ListView)view.findViewById(R.id.lv_order3_send_combine);
		lv_order3_send_combine.setAdapter(adapter);
		fl_send_combin = (FrameLayout)view.findViewById(R.id.fl_send_combin);
		fl_send_combin.setOnClickListener(listener);
		return view;
	}
	
	public void setSrcData(List<Order3> list){
		this.orderList = list;
		adapter.notifyDataSetChanged();
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order3_combine:
				combine();
				break;
			}
		}
	};
	
	private void combine(){
		List<Order3> combineOrderList = new Order3DB(sendActivity).findCombineOrder();
		if (!combineOrderList.isEmpty()) {
			boolean flag = true;
			Order3 order = combineOrderList.get(0);
			String storeId = order.getStoreId();
			for (int i = 1; i < combineOrderList.size(); i++) {
				if (!storeId.equals(combineOrderList.get(i).getStoreId())) {
					ToastOrder.makeText(sendActivity, "请选择同一家客户的订单", ToastOrder.LENGTH_SHORT).show();
					flag = false;
					break;
				}
			}
			if (flag) {
				sendActivity.removeCombineFragment();
				sendActivity.initCombineFragment(combineOrderList);
			}
		}else{
			ToastOrder.makeText(sendActivity, "请选择订单", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	
	private class CombineAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return orderList.size();
		}

		@Override
		public Object getItem(int position) {
			return orderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Order3 order3 = orderList.get(position);
			Order3SendCombineView view = null;
			if (convertView==null) {
				view = new Order3SendCombineView(sendActivity);
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (Order3SendCombineView) convertView.getTag();
			}
			view.setData(order3);
			return convertView;
		}
		
	}
}
