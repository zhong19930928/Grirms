package com.yunhu.yhshxc.order3;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3PromotionInfo;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.view.Order3DiscountItem;

public class ZengpinFragment extends Fragment {
	private Context context;
	private ListView lv_now_discount;
	private ZengPinAdapter adapter;
	private ShoppingCarActivity activity;
	List<Order3PromotionInfo> proInfoList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_now_dicount, null);
		context = v.getContext();
		activity = (ShoppingCarActivity) context;
		initView(v);
		return v;
	}
	private void initView(View v) {
		lv_now_discount = (ListView) v.findViewById(R.id.lv_now_discount);
		proInfoList = ((ShoppingCarActivity)activity).getProInfo();
		adapter = new ZengPinAdapter(context,proInfoList);
		lv_now_discount.setAdapter(adapter);
		
	}
	Order3ShoppingCart cart;
	public void initData() {
		proInfoList = ((ShoppingCarActivity)activity).getProInfo();
		adapter.notifyDataSetChanged();
		
	}
	

	class ZengPinAdapter extends BaseAdapter{
		private Context context;
		private List<Order3PromotionInfo> proInfo;
		public ZengPinAdapter(Context context,List<Order3PromotionInfo> proInfo){
			this.context = context;
			this.proInfo = proInfo;
		}

		@Override
		public int getCount() {
			return proInfo.size();
			
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Order3DiscountItem view =null;
			if(convertView == null){
				view = new Order3DiscountItem(context);
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (Order3DiscountItem) convertView.getTag();
			}
			view.initData(position+1, proInfo.get(position));
			
			return convertView;
		}
		
	}
}
