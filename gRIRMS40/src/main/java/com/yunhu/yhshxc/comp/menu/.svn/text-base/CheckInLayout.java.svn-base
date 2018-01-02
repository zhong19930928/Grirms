package com.yunhu.yhshxc.comp.menu;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.DateUtil;

public class CheckInLayout extends Menu{
	
	private Context mContext;
	private View view;
	private TextView tv_hour;
	private TextView tv_maohao;
	private TextView tv_min;
	private TextView tv_date;
	private TextView tv_week;
	private TextView tv_AM_PM;
	private TextView tv_userName;
	private boolean flag = true;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			exeClock();
		}
	};

	public CheckInLayout(Context context) {
		mContext = context;
		view = View.inflate(mContext,R.layout.checkinlayout, null);

		tv_hour = (TextView) view.findViewById(R.id.tv_hour);
		tv_maohao = (TextView) view.findViewById(R.id.tv_maohao);
		tv_min = (TextView) view.findViewById(R.id.tv_min);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_week = (TextView) view.findViewById(R.id.tv_week);
		tv_AM_PM = (TextView) view.findViewById(R.id.tv_AM_PM);
		tv_userName = (TextView) view.findViewById(R.id.tv_userName);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 0, 1000);
	}
	
	private void exeClock(){
		Date nowDate = new Date();
		int hour = DateUtil.getHour(nowDate);
		tv_hour.setText(String.valueOf(hour+100).substring(1));
		tv_min.setText(String.valueOf(DateUtil.getMin(nowDate)+100).substring(1));
		if(hour >= 12){
			tv_AM_PM.setText("PM");
		}else{
			tv_AM_PM.setText("AM");
		}
		if(hour == 0 || tv_date.getText().equals("")){
			tv_date.setText(DateUtil.dateToDateString(nowDate,DateUtil.ZHCN_DATA_STR));
			tv_week.setText(DateUtil.getDayOfWeek(nowDate));
		}
		if(flag = flag?false:true){
			tv_maohao.setVisibility(View.VISIBLE);
		}else{
			tv_maohao.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public void setUserName(String name){
		tv_userName.setText(name);
	}

	@Override
	public View getView() {
	
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
	
		
	}
}
