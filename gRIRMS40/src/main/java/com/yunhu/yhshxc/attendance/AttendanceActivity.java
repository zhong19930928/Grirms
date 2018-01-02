package com.yunhu.yhshxc.attendance;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.comp.TitleBar;
import com.yunhu.yhshxc.comp.menu.HomeMenuClockLayout;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

/**
 * 考勤模块
 * 分为上班、下班、查询三个菜单，其中查询菜单是任何时候都可以点击的，
 * 而上班、下班菜单是否可点击受相关逻辑控制：上班考勤打卡前，下班考勤
 * 是不能点击的，只有完成上班考勤打卡后才会激活下班考勤的可点击状态。
 * 另外，任何考勤打过卡后都不能再点击。
 * 
 * @version 2013.5.22
 * @author wangchao
 */
public class AttendanceActivity extends AbsBaseActivity {
	/**
	 * 封装了顶部用于显示信号等信息的组件
	 */
	private TitleBar titleBar = null;
	
	/**
	 * 时钟等相关可视组件的容器
	 */
	private LinearLayout homeView;
	
	/**
	 * 上班、下班、查询菜单的View引用
	 */
	private View shangban, xiaban, chaxun,fl_atten_pb,fl_atten_pb_search;
	
	/**
	 * 上班、下班两种状态的图标
	 */
	private ImageView iconShangban, iconXiaban;
	
	/**
	 * 上班、下班两种状态的文本
	 */
	private TextView labelShangban, labelXiaban;
	
	/**
	 * 上班、下班、查询菜单的事件处理
	 * 打开相应的Activity，并且设置菜单被按下和抬起时的显示状态
	 */
	private final View.OnTouchListener touchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundResource(R.color.home_menu_pressed);//设置按下时的背景
					break;
					
				case MotionEvent.ACTION_UP:
					//打开相应的Avtivity
					switch (v.getId()) {
						case R.id.yidaka:// 上班
							startAttendance(Constants.ATTENDANCE_STATE_START);
							break;
						case R.id.weidaka:// 下班
							startAttendance(Constants.ATTENDANCE_STATE_STOP);
							break;
						case R.id.chaxun:// 查询
							searchAttendance();
							break;
					}
					AttendanceActivity.this.finish();//关闭自己
					//这里不能有break，因为ACTION_UP同样需要执行ACTION_CANCEL和ACTION_OUTSIDE的操作
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_OUTSIDE:
					v.setBackgroundResource(R.color.visit_item_disorder);//设置抬起时的背景
					break;
			}
			return true;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance);
		titlebar();//将TitleBar添加到当前Activity中
		clock();//将时钟、用户角色等信息添加到当前Activity中
		
		shangban = findViewById(R.id.yidaka);
//		shangban.setOnTouchListener(touchListener);
		shangban.setOnClickListener(listener);
		xiaban = findViewById(R.id.weidaka);
//		xiaban.setOnTouchListener(touchListener);
		xiaban.setOnClickListener(listener);
		chaxun = findViewById(R.id.chaxun);
//		chaxun.setOnTouchListener(touchListener);
		chaxun.setOnClickListener(listener);
		
		iconShangban = (ImageView)findViewById(R.id.yidaka_icon);
		iconXiaban = (ImageView)findViewById(R.id.weidaka_icon);
		labelShangban = (TextView)findViewById(R.id.yidaka_icon_label);
		labelXiaban = (TextView)findViewById(R.id.weidaka_icon_label);
		
		fl_atten_pb = findViewById(R.id.fl_atten_pb);
		fl_atten_pb.setOnClickListener(listener);
		fl_atten_pb_search = findViewById(R.id.fl_atten_pb_search);
		fl_atten_pb_search.setOnClickListener(listener);
		
		String attenFunc  = SharedPrefsAttendanceUtil.getInstance(this).getAttendFunc();
		if ("1".equals(attenFunc)) {
			fl_atten_pb.setVisibility(View.VISIBLE);
			fl_atten_pb_search.setVisibility(View.VISIBLE);
		}else{
			fl_atten_pb.setVisibility(View.GONE);
			fl_atten_pb_search.setVisibility(View.GONE);
		}

	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.yidaka:// 上班
				startAttendance(Constants.ATTENDANCE_STATE_START);
				break;
			case R.id.weidaka:// 下班
				startAttendance(Constants.ATTENDANCE_STATE_STOP);
				break;
			case R.id.chaxun:// 查询
				searchAttendance();
				break;
			case R.id.fl_atten_pb:// 考勤排班
				attendanceScheduing();
				break;
			case R.id.fl_atten_pb_search://考勤排班查询
				attendanceScheduingSearch();
				break;
		}
		}
	};
	
	/**
	 * 考勤排班
	 */
	private void attendanceScheduing(){
		
		Intent intent = new Intent(this, SchedulingActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 考勤排班查询
	 */
	private void attendanceScheduingSearch(){
		Intent intent = new Intent(this, SearchSchedulingActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 初始化顶部TitleBar，并将其添加入当前Activity中
	 */
	private void titlebar(){
		View rl_titlebar = this.findViewById(R.id.rl_titlebar);
		titleBar = new TitleBar(this,rl_titlebar);
		titleBar.register();
	}

	/**
	 * 初始化时钟、用户名、角色等信息的组件，并添加入当前Activity中
	 */
	private void clock(){
		// 往这个布局里添加
		homeView = (LinearLayout) this.findViewById(R.id.ll_homeClock);
		// 顶部时钟
		HomeMenuClockLayout homeMenuClockLayout = new HomeMenuClockLayout(this);
		// 获取用户名的值
		homeMenuClockLayout.setUserName(SharedPreferencesUtil.getInstance(this).getUserName());
		// 获取用户角色的值
		homeMenuClockLayout.setUserRoleName(SharedPreferencesUtil.getInstance(this).getUserRoleName());
		homeView.addView(homeMenuClockLayout.getView());
	}

	/**
	 * 打开查询考勤的Activity
	 */
	private void searchAttendance() {
		int targetId = -2;

		Intent intent = new Intent(this, QueryFuncActivity.class);

		Bundle bundle = new Bundle();

		bundle.putInt("targetId", targetId);
		
		bundle.putInt("menuType", Menu.TYPE_ATTENDANCE);

		intent.putExtra("bundle", bundle);

		startActivity(intent);

	}

	/**
	 * 打开考勤详细界面的Activity
	 * 
	 * @param attendanceState 用于标记打开的是上班考勤还是下班考勤，
	 * 这个值参考Constants.ATTENDANCE_STATE_START和Constants.ATTENDANCE_STATE_STOP
	 */
	private void startAttendance(int attendanceState) {

		int targetId = Constants.ATTENDANCE_TARGETID;

		Intent intent = new Intent(this, AttendanceFuncActivity.class);

		Bundle bundle = new Bundle();

		bundle.putInt("attState", attendanceState);

		bundle.putInt("targetId", targetId);
		
		bundle.putInt("menuType", Menu.TYPE_ATTENDANCE);

		intent.putExtra("bundle", bundle);

		startActivity(intent);
		AttendanceActivity.this.finish();//关闭自己

	}

	@Override
	protected void onStart() {
		super.onStart();

		// 考勤的上班日期
		String startDate = SharedPreferencesUtil.getInstance(this).getStartWorkTime();

		// 考勤的下班日期
		String stopDate = SharedPreferencesUtil.getInstance(this).getStopWorkTime();
		
		// 根据考勤日期判断控件是否可用
		if (TextUtils.isEmpty(startDate)) {
			// 第一次考勤
			shangban.setEnabled(true);
			xiaban.setEnabled(false);
			xiaban.setBackgroundResource(R.color.visit_item_order_disable);
		}else if (DateUtil.compareTwoDateForSameDay(DateUtil.getDate(startDate.split("do")[0]), new Date()) && startDate.endsWith("do")) { //判断当天是否打上班卡，“do”为卡已打过
			// 已经完成上班打卡时
			shangban.setEnabled(false);
			iconShangban.setImageResource(R.drawable.kaoqin_done);
			labelShangban.setText(getString(R.string.yidaka));
			if (TextUtils.isEmpty(stopDate)) {
				//下班考勤未打卡时
				xiaban.setEnabled(true);
				xiaban.setBackgroundResource(R.color.visit_item_disorder);
			}else if (DateUtil.compareTwoDateForSameDay(DateUtil.getDate(stopDate.split("do")[0]), new Date())  && stopDate.endsWith("do")) { //判断当天是否打下班卡
				// 已经完成下班打卡时
				xiaban.setEnabled(false);
				xiaban.setBackgroundResource(R.color.visit_item_disorder);
				iconXiaban.setImageResource(R.drawable.kaoqin_done);
				labelXiaban.setText(getString(R.string.yidaka));
			}else {
				//已经完成上班打卡时，对下班菜单的默认处理方式
				xiaban.setEnabled(true);
				xiaban.setBackgroundResource(R.color.visit_item_disorder);
			}
		}else {
			//默认状态下只有上班菜单可点击
			shangban.setEnabled(true);
			xiaban.setEnabled(false);
			xiaban.setBackgroundResource(R.color.visit_item_order_disable);
		}
	}

	@Override
	protected void onDestroy() {
		// Activity销毁前必须调用TitleBar.unregister()
		if(titleBar != null){
			titleBar.unregister();
		}
		super.onDestroy();
	}

}
