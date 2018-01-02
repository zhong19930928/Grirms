package com.yunhu.yhshxc.comp.menu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.DateUtil;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * home页面时钟布局
 * @author gcg_jishen
 *
 */
public class HomeMenuClockLayout extends Menu{
	
	private Context mContext = null;
	private View view = null;
	private TextView tv_hour = null;
	private TextView tv_maohao = null;
	private TextView tv_min = null;
	private TextView tv_date = null;
	private TextView tv_week = null;
	private TextView tv_AM_PM = null;
	private TextView tv_userName = null;
	private TextView tv_roleName = null;
	private boolean flag = true;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			exeClock();
		}
	};

	public HomeMenuClockLayout(Context context) {
		mContext = context;
		view = View.inflate(mContext,R.layout.layout_menu_clock, null);

		tv_hour = (TextView) view.findViewById(R.id.tv_hour);
		tv_maohao = (TextView) view.findViewById(R.id.tv_maohao);
		tv_min = (TextView) view.findViewById(R.id.tv_min);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_week = (TextView) view.findViewById(R.id.tv_week);
		tv_AM_PM = (TextView) view.findViewById(R.id.tv_AM_PM);
		tv_userName = (TextView) view.findViewById(R.id.tv_userName);
		tv_roleName = (TextView) view.findViewById(R.id.tv_roleName);
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
			tv_AM_PM.setText(mContext.getResources().getString(R.string.pm));
		}else{
			tv_AM_PM.setText(mContext.getResources().getString(R.string.am));
		}
		if(hour == 0 || tv_date.getText().equals("")){
//			tv_date.setText(DateUtil.dateToDateString(nowDate,DateUtil.ZHCN_DATAFORMAT_STR));
			tv_date.setText(DateUtil.getDate());
			tv_week.setText(DateUtil.getDayOfWeek(nowDate));
		}
		if(flag = flag?false:true){
			tv_maohao.setVisibility(View.VISIBLE);
		}else{
			tv_maohao.setVisibility(View.INVISIBLE);
		}
		
	}
	
	/**
	 * 设置用户名
	 * @param name
	 */
	public void setUserName(String name){
		tv_userName.setText(name);
	}
	
	/**
	 * 设置角色名
	 * @param name
	 */
	public void setUserRoleName(String name){
		tv_roleName.setText(name);
	}

	@Override
	public View getView() {
		
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		
		
	}
}
