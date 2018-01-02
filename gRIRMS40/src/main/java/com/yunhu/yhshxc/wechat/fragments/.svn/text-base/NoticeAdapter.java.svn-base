package com.yunhu.yhshxc.wechat.fragments;

import java.text.ParseException;
import java.util.List;

import com.yunhu.yhshxc.wechat.bo.Notification;
import com.yunhu.yhshxc.wechat.view.NoticeView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoticeAdapter extends BaseAdapter {
	private List<Notification> noticList;
	private Context context;
	public NoticeAdapter(Context context,List<Notification> noticList){
		this.context = context;
		this.noticList = noticList;
	}
	@Override
	public int getCount() {
		return noticList.size();
//		return 20;
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
		NoticeView view = null;
		if(convertView == null){
			view = new NoticeView(context);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (NoticeView) convertView.getTag();
		}
		try {
			view.initData(noticList.get(position));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertView;
	}
	public void refresh(List<Notification> noticList){
		this.noticList = noticList;
		notifyDataSetChanged();
	}
}
