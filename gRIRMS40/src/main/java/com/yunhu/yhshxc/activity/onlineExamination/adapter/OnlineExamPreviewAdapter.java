package com.yunhu.yhshxc.activity.onlineExamination.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.activity.onlineExamination.view.OnlinePreviewItemView;

public class OnlineExamPreviewAdapter extends BaseAdapter {
	private Context context;
	private List<ExamQuestion> list;
	
	public OnlineExamPreviewAdapter(Context context,List<ExamQuestion> list){
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
		OnlinePreviewItemView view = null;
		ExamQuestion question = list.get(position);
		if(convertView == null){
			view = new OnlinePreviewItemView(context);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (OnlinePreviewItemView) convertView.getTag();
		}
		view.setPreviewView();
		return convertView;
	}

}
