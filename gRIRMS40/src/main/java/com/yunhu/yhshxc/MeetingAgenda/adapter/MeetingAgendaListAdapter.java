package com.yunhu.yhshxc.MeetingAgenda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.MeetingAgenda.bo.MeetingAgenda;
import com.yunhu.yhshxc.MeetingAgenda.notification.Event;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeetingAgendaListAdapter extends BaseAdapter {
	private Context context;
	private List<MeetingAgenda> list;
	private int uid;
	private boolean isFinish;
	private int companyId;
	private String uName;



	public MeetingAgendaListAdapter(Context context, List<MeetingAgenda> list,int uid,boolean isFinish) {
		this.context = context;
		this.list = list;
		this.uid = uid;
		this.isFinish=isFinish;
		companyId=SharedPreferencesUtil.getInstance(context).getCompanyId();
		uName=SharedPreferencesUtil.getInstance(context).getUserName();
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
			convertView = mInflater.inflate(R.layout.meetinglist_item, null);
			vh.time_tip = (TextView) convertView
					.findViewById(R.id.time_tip);
			vh.content_tx = (TextView) convertView
					.findViewById(R.id.content_tx);
			vh.address_tx = (TextView) convertView
					.findViewById(R.id.address_tx);
			vh.time_tx = (TextView) convertView
					.findViewById(R.id.time_tx);
			vh.type_txt= (TextView) convertView.findViewById(R.id.type_txt);
			vh.faqi_person= (TextView) convertView.findViewById(R.id.faqi_person);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		MeetingAgenda meetingAgenda=list.get(position);
		String aa=(meetingAgenda.getType()==1?"日程":"会议");
		String a2=meetingAgenda.getInfo();
		vh.content_tx.setText((meetingAgenda.getType()==1?"日程":"会议")+":"+meetingAgenda.getInfo());
		vh.address_tx.setText(meetingAgenda.getAddres());
		vh.faqi_person.setText("发起人："+meetingAgenda.getFquname());
		if(meetingAgenda.getType()==1){
			vh.time_tx.setText(meetingAgenda.getDataDay());
		}else{
			if(meetingAgenda.getStartTime()!=null&&!"null".equals(meetingAgenda.getStartTime())&&meetingAgenda.getEndTime()!=null&&!"null".equals(meetingAgenda.getEndTime())){
				vh.time_tx.setText(getMeetTime(meetingAgenda.getStartTime(),meetingAgenda.getEndTime()));
			}
		}
			if(meetingAgenda.getHuiYiType()==2){
				//同意
				vh.type_txt.setVisibility(View.VISIBLE);
				vh.type_txt.setText("接受");
				if(meetingAgenda.getRemindid()>0&&!isFinish){
					Event event=new Event(meetingAgenda.getInfo(),meetingAgenda.getStartTime(),meetingAgenda.getRemindingTime(),meetingAgenda.getHuiYiType());
					event.setCompanyID(companyId);
					event.setuID(uid);
					event.setuName(uName);
					event.setMeetingID(meetingAgenda.getHuiYiId());
					event.setType(meetingAgenda.getType());
					SoftApplication.getInstance().setEventsToList(event);
				}
			}else if(meetingAgenda.getHuiYiType()==3){
				//暂缓
				vh.type_txt.setVisibility(View.VISIBLE);
				vh.type_txt.setText("暂缓");
			}else if(meetingAgenda.getHuiYiType()==5){
				vh.type_txt.setText("拒绝");
				vh.type_txt.setVisibility(View.VISIBLE);
			}else if(meetingAgenda.getHuiYiType()==1){
				if(meetingAgenda.getRemindid()>0&&!isFinish){
					Event event=new Event(meetingAgenda.getInfo(),meetingAgenda.getStartTime(),meetingAgenda.getRemindingTime(),meetingAgenda.getHuiYiType());
					event.setCompanyID(companyId);
					event.setuID(uid);
					event.setuName(uName);
					event.setMeetingID(meetingAgenda.getHuiYiId());
					event.setType(meetingAgenda.getType());
					SoftApplication.getInstance().setEventsToList(event);
				}
				vh.type_txt.setVisibility(View.GONE);
			} else{
				//未读
				vh.type_txt.setVisibility(View.GONE);
			}
		//本人发起的不显示接收与否
		if (list.get(position).getFquid() == uid){
			vh.type_txt.setVisibility(View.GONE);

		}
		if(isFinish){
			vh.time_tx.setTextColor(Color.parseColor("#999999"));
			vh.content_tx.setTextColor(Color.parseColor("#999999"));
			vh.address_tx.setTextColor(Color.parseColor("#999999"));
			vh.type_txt.setTextColor(Color.parseColor("#999999"));
			vh.faqi_person.setTextColor(Color.parseColor("#999999"));
			vh.type_txt.setVisibility(View.GONE);
			vh.time_tip.setVisibility(View.GONE);
		}else{
			vh.time_tx.setTextColor(Color.parseColor("#71B0FA"));
			vh.content_tx.setTextColor(Color.BLACK);
			vh.address_tx.setTextColor(Color.parseColor("#666666"));
			vh.type_txt.setTextColor(Color.parseColor("#71B0FA"));
			vh.faqi_person.setTextColor(Color.parseColor("#666666"));
			if(meetingAgenda.getType()==1){
					vh.time_tip.setVisibility(View.GONE);
			}else if(meetingAgenda.getType()==2){
				if(isStart(meetingAgenda.getStartTime(),meetingAgenda.getEndTime())==1){
					vh.time_tip.setVisibility(View.VISIBLE);
					vh.time_tip.setText("即将开始");
				}else if((isStart(meetingAgenda.getStartTime(),meetingAgenda.getEndTime())==2)){
					vh.time_tip.setVisibility(View.VISIBLE);
					vh.time_tip.setText("正在进行");
				}else{
					vh.time_tip.setVisibility(View.GONE);
				}
			}
		}

		return convertView;
	}

	class ViewHolder {
		public TextView time_tip;
		public TextView content_tx;
		public TextView address_tx;
		public TextView time_tx;
		public TextView type_txt;
		public TextView faqi_person;

	}

	public void refresh(List<MeetingAgenda> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public interface TypeChangeListener{
		public void changeType(int position,int type);
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

	/**
	 * 是否显示即将开始、正在进行表示
	 * @param startDataTime
	 * @param endDataTime
	 * @return
	 */
	private int isStart(String startDataTime,String endDataTime){
		int flag=0 ;
		try {
			long time = System.currentTimeMillis();
			Date date=new Date(time);
			SimpleDateFormat sdf=null;
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(DateUtil.compareTwoDateForSameDay(date,sdf.parse(startDataTime))&&date.before(sdf.parse(startDataTime))){
					//表示即将开始
					flag=1;
				}else if(DateUtil.compareTwoDateForSameDay(date,sdf.parse(startDataTime))&&sdf.parse(startDataTime).before(date)&&sdf.parse(endDataTime).after(date)){
                    //表示正在进行
					flag=2;
				}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
