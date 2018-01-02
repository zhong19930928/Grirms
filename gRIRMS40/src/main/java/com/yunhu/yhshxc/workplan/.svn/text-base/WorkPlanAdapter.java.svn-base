package com.yunhu.yhshxc.workplan;

import java.util.List;

import com.yunhu.yhshxc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkPlanAdapter extends BaseAdapter {
	private List<String> data;
	private Context context;
	private LayoutInflater inflater;

	public WorkPlanAdapter(Context context, List<String> data) {
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_text_view, null, false);
			holder = new ViewHolder();
			holder.textContent = (TextView) convertView.findViewById(R.id.workplan_spinner_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textContent.setText(data.get(position));
		return convertView;
	}

	class ViewHolder {
		public TextView textContent;// 内容

	}
}
