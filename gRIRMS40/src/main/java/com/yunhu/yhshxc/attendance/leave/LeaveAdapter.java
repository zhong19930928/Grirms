package com.yunhu.yhshxc.attendance.leave;

import java.util.List;

import com.yunhu.yhshxc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeaveAdapter extends BaseAdapter {
	private Context context;
	private List<AskForLeaveInfo> list;

	public LeaveAdapter(Context context, List<AskForLeaveInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (vh == null) {
			vh = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.attend_leave_item, null);
			vh.tv_leave_type = (TextView) convertView
					.findViewById(R.id.tv_leave_type);
			vh.tv_leave_info = (TextView) convertView
					.findViewById(R.id.tv_leave_info);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		AskForLeaveInfo info = list.get(position);
		if (info != null) {
			vh.tv_leave_type.setText(info.getLeaveName());
			vh.tv_leave_info.setText(info.getStartTime().substring(0, 16) + "~"
					+ info.getEndTime().substring(0, 16));
		}
		return convertView;
	}

	class ViewHolder {
		public TextView tv_leave_type;
		public TextView tv_leave_info;
	}

	public void refresh(List<AskForLeaveInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}
}
