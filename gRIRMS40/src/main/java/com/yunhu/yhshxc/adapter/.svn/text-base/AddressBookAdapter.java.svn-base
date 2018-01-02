package com.yunhu.yhshxc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.activity.addressBook.AddressBookChilcdView;
import com.yunhu.yhshxc.activity.addressBook.AddressBookFrView;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;

import java.util.List;

public class AddressBookAdapter extends BaseAdapter {
	private Context context;
	private List<AdressBookUser> list;
	private boolean isVisible;
	public AddressBookAdapter(Context context, List<AdressBookUser> list,boolean isVisible){
		this.context = context;
		this.list = list;
		this.isVisible = isVisible;
	}
	@Override
	public int getCount() {
		return list.size();
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
		AddressBookFrView view = null;
		if(convertView == null){
			view = new AddressBookFrView(context,isVisible);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (AddressBookFrView) convertView.getTag();
		}
		view.initData(list.get(position));
		return convertView;
	}
	public void refresh(List<AdressBookUser> list){
		this.list = list;
		notifyDataSetChanged();
	}
}
