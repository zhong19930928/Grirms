package com.yunhu.yhshxc.attendance;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class SchedulingSearchResultItem {

	private View view;
	private Context context;
	private Scheduling item;
	private TextView tv_scheduling_name;
	private TextView tv_scheduling_starttime;
	private TextView tv_scheduling_endtime;
	


	public SchedulingSearchResultItem(Context mContext) {
		this.context = mContext;
		view = View.inflate(mContext,
				R.layout.scheduling_search_result, null);
		tv_scheduling_name = (TextView)view.findViewById(R.id.tv_scheduling_name);
		tv_scheduling_starttime = (TextView)view.findViewById(R.id.tv_scheduling_starttime);
		tv_scheduling_endtime = (TextView)view.findViewById(R.id.tv_scheduling_endtime);
		
		
	}

	public void setData(Scheduling data) {
		this.item = data;
		tv_scheduling_name.setText(data.getName());
		tv_scheduling_starttime.setText(PublicUtils.getResourceString(context,R.string.start_time) + data.getStartTime());
		tv_scheduling_endtime.setText(PublicUtils.getResourceString(context,R.string.end_time) + data.getEndTimel());

	}

	public View getView() {

		return view;
	}
}
