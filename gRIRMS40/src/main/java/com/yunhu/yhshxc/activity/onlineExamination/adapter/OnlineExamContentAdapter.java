package com.yunhu.yhshxc.activity.onlineExamination.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.activity.onlineExamination.bo.ExamQuestion;
import com.yunhu.yhshxc.activity.onlineExamination.view.ExamContentItemView;

public class OnlineExamContentAdapter extends BaseAdapter {
	private Context context;
	private List<ExamQuestion> list;
	public OnlineExamContentAdapter(Context context,List<ExamQuestion> list){
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
		ExamContentItemView  view = null;
		if(convertView == null){
			view = new ExamContentItemView(context);
			convertView = view.getView();
			convertView.setTag(view);
		}else{
			view = (ExamContentItemView) convertView.getTag();
		}
		ExamQuestion question = list.get(position);
		view.setQuestion(question);
		return convertView;
	}

}
