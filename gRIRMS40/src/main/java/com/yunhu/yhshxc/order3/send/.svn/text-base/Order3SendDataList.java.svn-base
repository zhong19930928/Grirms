package com.yunhu.yhshxc.order3.send;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class Order3SendDataList {
	private View view = null;
	private ListView lv_order3_send_detail;
	private List<List<Order3SendData>> srcList = new ArrayList<List<Order3SendData>>();
	private Context context;
	private SendAdapter adapter = null;
	
	public Order3SendDataList(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.order3_send_detail_list, null);
		lv_order3_send_detail = (ListView)view.findViewById(R.id.lv_order3_send_detail);
		adapter = new SendAdapter();
		lv_order3_send_detail.setAdapter(adapter);
	}
	
	public void setSrcData(List<List<Order3SendData>> srcList){
		this.srcList = srcList;
		PublicUtils.setListViewHeightBasedOnChildren(lv_order3_send_detail);
		adapter.notifyDataSetChanged();
	}
	
	public View getView(){
		return view;
	}
	
	public final int SETND_TYPE_ALL = 1;//配送完
	public final int SETND_TYPE_RESET = 2;//重置
	
	public void sendType(int type){
		switch (type) {
		case SETND_TYPE_ALL:
			sendAll();
			break;
		case SETND_TYPE_RESET:
			sendReset();
			break;

		default:
			break;
		}
	}
	
	private void sendAll(){
		for (int i = 0; i < srcList.size(); i++) {
			List<Order3SendData>  dList = srcList.get(i);
			for (int j = 0; j < dList.size(); j++) {
				Order3SendData data = dList.get(j);
				data.setSendNumber(data.getUnSendNumber());
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	private void sendReset(){
		for (int i = 0; i < srcList.size(); i++) {
			List<Order3SendData>  dList = srcList.get(i);
			for (int j = 0; j < dList.size(); j++) {
				Order3SendData data = dList.get(j);
				data.setSendNumber(0);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	private class SendAdapter extends BaseAdapter{

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
			Order3SendDataView dView = null;
			List<Order3SendData>  dList = srcList.get(position);
			if (convertView == null) {
				dView = new Order3SendDataView(context);
				convertView = dView.getView();
				convertView.setTag(dView);
			}else{
				dView = (Order3SendDataView) convertView.getTag();
			}
			dView.setData(dList);
			return convertView;
		}
		
	}
}
