package com.yunhu.yhshxc.order3.send;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SendPriviewProductListFragment extends Fragment{
	
	private ListView lv_order3_send_p_product_list;
	private MyAdapter adapter = null;
	private List<Order3SendPriviewListData> srcList;
	
	public Order3SendPriviewProductListFragment() {
		adapter = new MyAdapter();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.order3_send_priview_list_fragment, null);
		lv_order3_send_p_product_list = (ListView)v.findViewById(R.id.lv_order3_send_p_product_list);
		lv_order3_send_p_product_list.setAdapter(adapter);
		return v;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		PublicUtils.setListViewHeightBasedOnChildren(lv_order3_send_p_product_list);
	}
	
	public void setDataSrc(List<Order3SendPriviewListData> list){
		this.srcList = list;
		adapter.notifyDataSetChanged();
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return srcList.size();
		}

		@Override
		public Object getItem(int position) {
			return srcList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Order3SendPriviewListData data = srcList.get(position);
			Order3SendPriviewListView view = null;
			if (convertView == null) {
				view = new Order3SendPriviewListView(getActivity());
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (Order3SendPriviewListView) convertView.getTag();
			}
			view.setData(position,data);
			return convertView;
		}
		
	}
}
