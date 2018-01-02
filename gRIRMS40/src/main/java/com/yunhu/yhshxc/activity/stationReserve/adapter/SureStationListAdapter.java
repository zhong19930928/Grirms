package com.yunhu.yhshxc.activity.stationReserve.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.onlineExamination.OnlineExamExplainationActivity;
import com.yunhu.yhshxc.activity.stationReserve.ReserveStationActivity;
import com.yunhu.yhshxc.activity.stationReserve.StationMiddleActivity;
import com.yunhu.yhshxc.bo.StationBean;
import com.yunhu.yhshxc.bo.SureStationBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SureStationListAdapter extends BaseAdapter {
	private Context context;
	private List<SureStationBean> list;
	private StationMiddleActivity stationMiddleActivity;

	public SureStationListAdapter(Context context, List<SureStationBean> list) {
		this.context = context;
		this.list = list;
		stationMiddleActivity= (StationMiddleActivity) context;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (vh == null) {
			vh = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.stationfloorlist_item, null);
			vh.station_address = (TextView) convertView
					.findViewById(R.id.station_address);
			vh.can_reserve = (TextView) convertView
					.findViewById(R.id.can_reserve);
			vh.station_num = (TextView) convertView
					.findViewById(R.id.station_num);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.station_address.setText(list.get(position).getLouName()+"-"+list.get(position).getCengName());
		vh.station_num.setText(list.get(position).getNums()+"个座位");

		vh.can_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ReserveStationActivity.class);
				intent.putExtra("SureStationBean",list.get(position));
//                context.startActivity(intent);
				stationMiddleActivity.startActivityForResult(intent,100);
            }
        });

		return convertView;
	}

	class ViewHolder {
		public TextView station_address;
		public TextView can_reserve;
		public TextView station_num;

	}

	public void refresh(List<SureStationBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}


}
