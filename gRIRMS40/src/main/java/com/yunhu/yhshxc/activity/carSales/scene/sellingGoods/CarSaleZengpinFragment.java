package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import java.util.ArrayList;
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
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionInfo;
import com.yunhu.yhshxc.activity.carSales.scene.view.ZengpingZKView;

public class CarSaleZengpinFragment extends Fragment {
	
	private Context context;
	private ListView lv_now_discount;
	private CarSalesZengPingAdapter adapter;
	private CarSaleShoppingCartActivity activity;
	private List<CarSalesPromotionInfo> proInfoList  =new ArrayList<CarSalesPromotionInfo>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_now_dicount, container, false);
		context = v.getContext();
		activity = (CarSaleShoppingCartActivity) context;
		initView(v);
		return v;
	}

	private void initView(View v) {
		lv_now_discount = (ListView) v.findViewById(R.id.lv_now_discount);
		proInfoList = activity.getProInfo();
		if (proInfoList != null && proInfoList.size() > 0) {
			adapter = new CarSalesZengPingAdapter(context, proInfoList);
			lv_now_discount.setAdapter(adapter);
		}
		
	}
//	CarSalesShoppingCart cart;
	public void initData() {
		proInfoList = ((CarSaleShoppingCartActivity) activity).getProInfo();
		if (adapter!=null) {
			adapter.notifyDataSetChanged();
		}
	}
	class CarSalesZengPingAdapter extends BaseAdapter{
		private Context context;
		private List<CarSalesPromotionInfo> proInfoList;
		public CarSalesZengPingAdapter(Context context,List<CarSalesPromotionInfo> proInfoList){
			this.context = context;
			this.proInfoList = proInfoList;
		}
		@Override
		public int getCount() {
			return proInfoList.size();
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
			ZengpingZKView view =null;
			if(convertView == null){
				view = new ZengpingZKView(context);
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (ZengpingZKView) convertView.getTag();
			}
			view.initData(position+1, proInfoList.get(position));
			return convertView;
		}
		
	}
}
