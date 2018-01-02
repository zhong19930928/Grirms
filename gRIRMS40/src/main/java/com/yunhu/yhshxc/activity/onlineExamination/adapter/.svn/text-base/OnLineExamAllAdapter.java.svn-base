package com.yunhu.yhshxc.activity.onlineExamination.adapter;

import com.yunhu.yhshxc.activity.onlineExamination.view.AllExamView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class OnLineExamAllAdapter extends BaseAdapter {
	private Context context;
	public OnLineExamAllAdapter(Context context){
		this.context = context;
	}

	@Override
	public int getCount() {
		return 5;
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
		AllExamView view = new  AllExamView(context);
		if(convertView == null){
			view = new AllExamView(context);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (AllExamView) convertView.getTag();
		}
		return convertView;
	}

}
