package com.yunhu.yhshxc.activity.stationReserve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.StationBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StationReserveListAdapter extends BaseAdapter {
	private Context context;
	private List<StationBean> list;
	private OnClickItemListener onClickItemListener;
	private boolean isManage;

	public StationReserveListAdapter(Context context, List<StationBean> list,OnClickItemListener onClickItemListener) {
		this.context = context;
		this.list = list;
		this.onClickItemListener=onClickItemListener;
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
			convertView = mInflater.inflate(R.layout.stationlist_item, null);
			vh.station_name = (TextView) convertView
					.findViewById(R.id.station_name);
			vh.station_time = (TextView) convertView
					.findViewById(R.id.station_time);
			vh.release_station = (TextView) convertView
					.findViewById(R.id.release_station);
			vh.per_name= (TextView) convertView.findViewById(R.id.per_name);
			vh.modify_per_icon= (ImageView) convertView.findViewById(R.id.modify_per_icon);
			vh.pername_layout= (LinearLayout) convertView.findViewById(R.id.pername_layout);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.station_name.setText(getGswnName(list.get(position).getGwname()));
//		vh.station_time.setText(list.get(position).getStartTime()+"-"+list.get(position).getEndTime());
		if(isManage){
			if(list.get(position).getYgHaoName()!=null&&!"".equals(list.get(position).getYgHaoName())&&!"null".equals(list.get(position).getYgHaoName())){
				vh.release_station.setVisibility(View.GONE);
				vh.per_name.setVisibility(View.VISIBLE);
				vh.modify_per_icon.setVisibility(View.GONE);
				vh.per_name.setText(list.get(position).getYgHaoName());
				vh.per_name.setClickable(false);
				vh.pername_layout.setClickable(false);
			}else{
				vh.per_name.setVisibility(View.GONE);
				vh.modify_per_icon.setVisibility(View.GONE);
				vh.release_station.setVisibility(View.GONE);
				vh.pername_layout.setClickable(false);
			}
		}else {
			vh.release_station.setVisibility(View.VISIBLE);
            if(list.get(position).getYgHaoName()!=null&&!"".equals(list.get(position).getYgHaoName())&&!"null".equals(list.get(position).getYgHaoName())){
                vh.per_name.setVisibility(View.VISIBLE);
                vh.modify_per_icon.setVisibility(View.GONE);
                vh.per_name.setText(list.get(position).getYgHaoName());
            }else{
                vh.per_name.setVisibility(View.GONE);
                vh.modify_per_icon.setVisibility(View.VISIBLE);
            }
			vh.release_station.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onClickItemListener.clickItem(position,view);
				}
			});
			vh.pername_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onClickItemListener.clickItem(position,view);
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		public TextView station_name;
		public TextView station_time;
		public TextView release_station;
		public TextView per_name;
		public LinearLayout pername_layout;
		public ImageView modify_per_icon;

	}

	public void refresh(List<StationBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public interface OnClickItemListener{
		public void clickItem(int position, View view);
	}

	private String getMeetTime(String startTime,String endTime){
		StringBuffer buffer=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
		try {

			Date sd = sdf.parse(startTime);
			Date ed = sdf.parse(endTime);
			buffer.append(sdf2.format(sd)).append("-").append(sdf1.format(ed));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public void setManage(boolean manage) {
		isManage = manage;
	}

	private String getGswnName(String gswnName){
		StringBuffer buffer=new StringBuffer();
		if(gswnName!=null&&!"".equals(gswnName)){
			String[] split = gswnName.split("-");
			if(split!=null&&split.length==3){
				buffer.append(split[0]).append("座 ").append(split[1]).append("层 ").append(split[2]);
			}
		}
		return buffer.toString();
	}
}
