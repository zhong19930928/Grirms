package com.yunhu.yhshxc.activity.addressBook;

import java.util.List;

import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContentAdapter extends BaseAdapter {
	private Context context;
	private List<AdressBookUser> list;
	public ContentAdapter(Context context,List<AdressBookUser> list){
		this.context = context;
		this.list = list;
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
		AddressBookChilcdView view = null;
		if(convertView == null){
			view = new AddressBookChilcdView(context);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (AddressBookChilcdView) convertView.getTag();
		}
		view.initData(list.get(position));
		return convertView;
	}
	public void refresh(List<AdressBookUser> list){
		this.list = list;
		notifyDataSetChanged();
	}
}
