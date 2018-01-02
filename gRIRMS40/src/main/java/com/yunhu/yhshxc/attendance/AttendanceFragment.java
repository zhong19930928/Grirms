package com.yunhu.yhshxc.attendance;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.attendCalendar.AttendanceCalendarActivity;
import com.yunhu.yhshxc.attendance.leave.InitiateLeaveActivity;
import com.yunhu.yhshxc.attendance.util.SharedPreferencesForLeaveUtil;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.comp.TitleBar;
import com.yunhu.yhshxc.comp.menu.HomeMenuClockLayout;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import gcg.org.debug.JLog;



/**
 * 考勤模块 分为上班、下班、查询三个菜单，其中查询菜单是任何时候都可以点击的， 而上班、下班菜单是否可点击受相关逻辑控制：上班考勤打卡前，下班考勤
 * 是不能点击的，只有完成上班考勤打卡后才会激活下班考勤的可点击状态。 另外，任何考勤打过卡后都不能再点击。
 * 
 */
public class AttendanceFragment extends Fragment {
	/**
	 * 封装了顶部用于显示信号等信息的组件
	 */
	private TitleBar titleBar = null;

	/**
	 * 时钟等相关可视组件的容器
	 */
	private LinearLayout homeView;
	private TextView shangban_daka_tip, xiaban_daka_tip;
	/**
	 * 上班、下班、查询菜单的View引用
	 */
	private View shangbanBtn, xiabanBtn, work_over, chaxun, fl_atten_pb, fl_atten_pb_search, fl_atten_leave;

	/**
	 * shangban, xiaban, 上班、下班两种状态的图标
	 */
	// private ImageView iconShangban, iconXiaban;

	/**
	 * 上班、下班两种状态的文本
	 */
	// private TextView labelShangban, labelXiaban,new_attendance_over_name;
	private PopupWindow popupWindow;

	private View rootView;

	private Context context;

	// private LinearLayout ll_late,ll_leaveEarly;//迟到 早退

	private LinearLayout ll_attend_calendar;// 考勤日历按钮
	public boolean isEnableNetWork = false;// 是否有网络，按钮是否可点击

	private TextView shangban_late;
	// private MyProgressDialog mDialog = null;

	private TextView xiaban_late;

	private ImageView xiaban_flag;

	private ImageView shangban_flag;

	private ImageView gang_flag;

	private View gang;

	private ImageView gang_icon;

	private TextView gang_text;

	private TextView gang_text2;

	private String overtimeName;

	private String finishName;

	private TextView shangban_new_text;

	private TextView xiaban_new_text;

	private String goName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = getActivity();
		gangName=PublicUtils.getResourceString(context,R.string.attend_bg);
		if (null != rootView) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		} else {
			rootView = inflater.inflate(R.layout.attendance_new, container, false);
			initView(rootView);
			// init(rootView);
			// clock(rootView);
		}
		// mDialog = new MyProgressDialog(getActivity(),
		// R.style.CustomProgressDialog, "正在加载,请稍后...");
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		init();// 进入提交页面提交完数据后打卡状态不能立即改变,需要重新构建状态,重新调用一下初始化
		// initGang();
	}

	void initView(View rootView) {
		gang = rootView.findViewById(R.id.gang);
		gang.setOnClickListener(clickListener);
		//
		TextView attendance_title = (TextView) rootView.findViewById(R.id.attendance_title);

		attendance_title.setText(name);
		ll_attend_calendar = (LinearLayout) rootView.findViewById(R.id.ll_attend_calendar);
		ll_attend_calendar.setOnClickListener(clickListener);
		shangbanBtn = rootView.findViewById(R.id.shangban_new_test);// 上班
		shangbanBtn.setOnClickListener(clickListener);
		xiabanBtn = rootView.findViewById(R.id.xiaban_new_test);// 下班
		xiabanBtn.setOnClickListener(clickListener);
		shangban_daka_tip = (TextView) rootView.findViewById(R.id.shangban_daka_tip);
		xiaban_daka_tip = (TextView) rootView.findViewById(R.id.xiaban_daka_tip);
		work_over = rootView.findViewById(R.id.work_over_new);
		work_over.setOnClickListener(clickListener);//
		chaxun = rootView.findViewById(R.id.chaxun_new);

		chaxun.setOnClickListener(clickListener);

		shangban_late = (TextView) rootView.findViewById(R.id.shangban_late);
		xiaban_late = (TextView) rootView.findViewById(R.id.xiaban_late);// android:id="@+id/xiaban_flag"

		xiaban_flag = (ImageView) rootView.findViewById(R.id.xiaban_flag);
		shangban_flag = (ImageView) rootView.findViewById(R.id.shangban_flag);
		gang_icon = (ImageView) rootView.findViewById(R.id.gang_icon);
		gang_text = (TextView) rootView.findViewById(R.id.gang_text);
		gang_text2 = (TextView) rootView.findViewById(R.id.gang_text2);

		shangban_new_text = (TextView) rootView.findViewById(R.id.shangban_new_text);
		xiaban_new_text = (TextView) rootView.findViewById(R.id.xiaban_new_text);

		fl_atten_pb = rootView.findViewById(R.id.fl_atten_pb);
		fl_atten_pb.setOnClickListener(clickListener);
		fl_atten_pb_search = rootView.findViewById(R.id.fl_atten_pb_search);
		fl_atten_pb_search.setOnClickListener(clickListener);

		fl_atten_leave = rootView.findViewById(R.id.fl_atten_leave);
		fl_atten_leave.setOnClickListener(clickListener);
		String isLeave = SharedPreferencesForLeaveUtil.getInstance(context).getIS_LEAVE();
		if ("1".equals(isLeave)) {
			fl_atten_leave.setVisibility(View.VISIBLE);
		} else {
			fl_atten_leave.setVisibility(View.GONE);
		}

		String attenFunc = SharedPrefsAttendanceUtil.getInstance(context).getAttendFunc();
		if ("1".equals(attenFunc)) {
			fl_atten_pb.setVisibility(View.VISIBLE);
			fl_atten_pb_search.setVisibility(View.VISIBLE);
		} else {
			fl_atten_pb.setVisibility(View.GONE);
			fl_atten_pb_search.setVisibility(View.GONE);
		}

		// init();

	}

	Calendar calendar;
	String gangName = "";

	private String name;

	// 检查是否需要报岗－－Aug22
	private boolean checkGangVisibility(int length, Date dateBaidu) {
		boolean flag = false;
		calendar = Calendar.getInstance();
		if (null == dateBaidu) {
			return false;
		}
		calendar.setTime(dateBaidu);
		int cMonth = calendar.get(Calendar.MONTH) + 1;// 当前月
		int cDay = calendar.get(Calendar.DAY_OF_MONTH);// 当前日

		for (int i = 0; i < length; i++) {

			String date = SharedPreferencesUtil.getInstance(context).gettString("gang" + "date" + i);
			String startHM1 = SharedPreferencesUtil.getInstance(context).gettString("gang" + "startHM" + (i + 1));
			String endHM1 = SharedPreferencesUtil.getInstance(context).gettString("gang" + "endHM" + (i + 1));

			if (date != null && !date.equals("") && !date.equals("null") && date.indexOf("-") > 0) {
				String[] dateArr = date.split("-");
				int MONTH = Integer.parseInt(dateArr[0]);
				int DAY = Integer.parseInt(dateArr[1]);

				if (MONTH == cMonth && DAY == cDay) {

					if (!startHM1.equals("") && !endHM1.equals("")) {// 下次报岗时间
						gang_text2.setText(PublicUtils.getResourceString(context,R.string.next_post)+":" + startHM1 + "-" + endHM1);
					}
					gang_icon.setImageResource(R.drawable.kaoqin_done);
					// labelGang.setText(getString(R.string.gangdone));

					continue;
				}
			}

			String isFlag = SharedPreferencesUtil.getInstance(context).getString("gang" + "isFlag" + i);

			int m = 0, d = 0;
			if (isFlag != null && !isFlag.equals("") && !isFlag.equals("null") && isFlag.indexOf("-") > 0) {
				String[] flagDate = isFlag.split("-");
				m = Integer.parseInt(flagDate[1]);
				d = Integer.parseInt(flagDate[2]);

			}

			if (m != cMonth || d != cDay) { // 本次未报岗

				String startHM = SharedPreferencesUtil.getInstance(context).gettString("gang" + "startHM" + i);
				if (startHM != null && !startHM.equals("") && !startHM.equals("null")) {

					String[] arr = startHM.split(":");
					int HOUR = Integer.parseInt(arr[0]);// 起始小时
					int MINUTE = Integer.parseInt(arr[1]);// 起始分钟
					int cHour = calendar.get(Calendar.HOUR_OF_DAY);// 当前小时
					int cMinute = calendar.get(Calendar.MINUTE);// 当前分钟

					if (cHour > HOUR || (HOUR == cHour && cMinute >= MINUTE)) {// 超过起始时间

						if (!startHM1.equals("") && !endHM1.equals("")) {// 下次报岗时间
							gang_text2.setText(PublicUtils.getResourceString(context,R.string.next_post)+":" + startHM1 + "-" + endHM1);
						}

						String endHM = SharedPreferencesUtil.getInstance(context).gettString("gang" + "endHM" + i);
						if (endHM != null && !endHM.equals("") && !endHM.equals("null")) {
							String[] arr1 = endHM.split(":");
							int HOUR1 = Integer.parseInt(arr1[0]);
							int MINUTE1 = Integer.parseInt(arr1[1]);
							if (HOUR1 > cHour || (HOUR1 == cHour && MINUTE1 >= cMinute)) {// 在结束时间之内
								gang_text2.setText(PublicUtils.getResourceString(context,R.string.post_time)+":" + startHM + "-" + endHM);// 本次报岗时间

								gang_icon.setImageResource(R.drawable.kaoqin_undo);
								// labelGang.setText(getString(R.string.e_gang));
								// String name =
								// SharedPreferencesUtil.getInstance(this).gettString("gang"+"name"+i);
								// if(name!=null&&!name.equals("")&&!name.equals("null")){
								// gangName = name;
								// }
								flag = true;
								break;
							}
						}
					}
				}
			} else {
				gang_icon.setImageResource(R.drawable.kaoqin_done);
				// labelGang.setText(getString(R.string.gangdone));
				if (!startHM1.equals("") && !endHM1.equals("")) {// 下次报岗时间
					gang_text2.setText(PublicUtils.getResourceString(context,R.string.next_post)+":" + startHM1 + "-" + endHM1);
				}

			}
		}
		return flag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			name = getArguments().getString("name");

		}
	}

	private void initGang(Date date) {
		overtimeName = SharedPreferencesUtil.getInstance(context).gettString("overtimeName");// plus
		goName = SharedPreferencesUtil.getInstance(context).gettString("goName");// up
		finishName = SharedPreferencesUtil.getInstance(context).gettString("finishName");// down

		String gangName1 = SharedPreferencesUtil.getInstance(context).gettString("gangName");// gang

		if (gangName1 != null && !gangName1.equals("") && !gangName1.equals("null")) {
			this.gangName = gangName1;
		}
		if (finishName != null && !finishName.equals("") && !finishName.equals("null")) {
			xiaban_new_text.setText(finishName);
		}
		if (goName != null && !goName.equals("") && !goName.equals("null")) {
			shangban_new_text.setText(goName);
		}

		String overName = SharedPreferencesUtil.getInstance(context).getNewAttendanceOverName();
		if (TextUtils.isEmpty(overName)) {
			work_over.setVisibility(View.GONE);
		} else {
			if (overtimeName != null && !overtimeName.equals("") && !overtimeName.equals("null")) {
				overName = overtimeName;
			}
			TextView new_attendance_over_name = (TextView) rootView.findViewById(R.id.new_attendance_over_name);
			new_attendance_over_name.setText(overName);
		}

		String startHM = SharedPreferencesUtil.getInstance(context).gettString("gang" + "startHM0");
		String endHM = SharedPreferencesUtil.getInstance(context).gettString("gang" + "endHM0");
		if (!startHM.equals("") && !endHM.equals("")) {
			gang_text2.setText(PublicUtils.getResourceString(context,R.string.post_time)+":" + startHM + "-" + endHM);// 第一次报岗时间
		}
		int gangLength = SharedPreferencesUtil.getInstance(context).gettInt("gang");
		if (gangLength > 0) {
			gang_text.setText(this.gangName);
			gang.setVisibility(View.VISIBLE);

			if (checkGangVisibility(gangLength, date)) {
				gang.setEnabled(true);
				gang.setBackgroundResource(R.drawable.shape_re_blue);

			} else {
				gang.setEnabled(false);
				gang.setBackgroundResource(R.drawable.shape_re_blue2);
			}

		} else {
			gang.setVisibility(View.GONE);
		}
	}

	private void init() {

		String overName = SharedPreferencesUtil.getInstance(context).getNewAttendanceOverName();
		if (TextUtils.isEmpty(overName)) {
			work_over.setVisibility(View.GONE);
		} else {
		}
		setNewAttendTime();

	}

	// ================================================================

	public void setNewAttendTime() {
		flagTime = true;
		timerT = new Timer(true);
		if (taskT.cancel()) {
			timerT.schedule(taskT, 5000, 100000000); // 延时5000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				//context.getApplicationContext().getResources().getString(R.string.bai_du)
				Date date = PublicUtils.getNetDate();
				;
				Message msg2 = Message.obtain();
				msg2.obj = date;
				mHanlderTime.sendMessage(msg2);
			}
		}).start();
	}

	Handler mHanlderTime = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flagTime) {
				flagTime = false;
				if (taskT != null) {
					taskT.cancel();
				}
				Date date = (Date) msg.obj;
				if (null != date) {
					isEnableNetWork = true;
				} else {
					isEnableNetWork = false;
					Toast.makeText(context,R.string.check_net_time, Toast.LENGTH_LONG).show();
				}
				AttendanceUtil.resetAttendance(context, date);
				initAttendance(date);
				initGang(date);
			}

		};
	};
	private boolean flagTime = true;
	Timer timerT;
	TimerTask taskT = new TimerTask() {
		public void run() {
			Message msg = Message.obtain();
			mHanlderTime.sendMessage(msg);
		}
	};

	// ====================================================================

	// @Override
	// protected void onStart() {
	// super.onStart();
	// init();
	// }

	/**
	 * 初始化顶部TitleBar，并将其添加入当前Activity中
	 */
	private void titlebar() {
		View rl_titlebar = rootView.findViewById(R.id.rl_titlebar);
		titleBar = new TitleBar(context, rl_titlebar);
		titleBar.register();
	}

	/**
	 * 初始化时钟、用户名、角色等信息的组件，并添加入当前Activity中
	 */
	private void clock() {
		// 往这个布局里添加
		homeView = (LinearLayout) rootView.findViewById(R.id.ll_homeClock);
		// 顶部时钟
		HomeMenuClockLayout homeMenuClockLayout = new HomeMenuClockLayout(context);
		// 获取用户名的值
		homeMenuClockLayout.setUserName(SharedPreferencesUtil.getInstance(context).getUserName());
		// 获取用户角色的值
		homeMenuClockLayout.setUserRoleName(SharedPreferencesUtil.getInstance(context).getUserRoleName());
		homeView.addView(homeMenuClockLayout.getView());
	}

	/**
	 * 上班、下班、查询菜单的事件处理 打开相应的Activity，并且设置菜单被按下和抬起时的显示状态
	 */
	private final View.OnTouchListener touchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.color.home_menu_pressed);// 设置按下时的背景
				break;

			case MotionEvent.ACTION_UP:
				// 打开相应的Avtivity
				switch (v.getId()) {
				// case R.id.shangban_new:// 上班
				// startAttendance(Constants.NEW_ATTENDANCE_START);
				// break;
				// case R.id.xiaban_new:// 下班
				// startAttendance(Constants.NEW_ATTENDANCE_END);
				// break;

				case R.id.shangban_over_new:// 上班加班
					startAttendance(Constants.NEW_ATTENDANCE_OVER_START, overtimeName);
					break;
				case R.id.xiaban_over_new:// 下班加班
					startAttendance(Constants.NEW_ATTENDANCE_OVER_END, overtimeName);
					break;
				case R.id.work_over_new:// 加班
					workOver();
					break;
				case R.id.chaxun_new:// 查询
					searchAttendance();
					break;
				case R.id.fl_atten_pb:// 排班
					attendanceScheduing();
					break;
				case R.id.fl_atten_pb_search:// 排班
					attendanceScheduingSearch();
					break;

				}
				// AttendanceNewActivity.context.finish();// 关闭自己
				// 这里不能有break，因为ACTION_UP同样需要执行ACTION_CANCEL和ACTION_OUTSIDE的操作
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				v.setBackgroundResource(R.color.visit_item_disorder);// 设置抬起时的背景
				break;
			}
			return true;
		}

	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (PublicUtils.checkConnection(context)) {
				if (isEnableNetWork) {
					switch (v.getId()) {
					// case R.id.shangban_new:// 上班
					// startAttendance(Constants.NEW_ATTENDANCE_START);
					// break;
					// case R.id.xiaban_new:// 下班
					// startAttendance(Constants.NEW_ATTENDANCE_END);
					// break;

					case R.id.shangban_new_test:
						startAttendance(Constants.NEW_ATTENDANCE_START, goName);// 上班

						// isGoToShangeBan();
						break;
					case R.id.xiaban_new_test:
						startAttendance(Constants.NEW_ATTENDANCE_END, finishName);// 下班
						break;
					case R.id.gang:// 报岗
						// if(checkGangVisibility()){
						startAttendance(Constants.NEW_ATTENDANCE_GANG, gangName);
						// }else{
						// ToastOrder.makeText(context, "已报岗",
						// ToastOrder.LENGTH_SHORT);
						// }
						break;
					case R.id.shangban_over_new:// 上班加班
						startAttendance(Constants.NEW_ATTENDANCE_OVER_START, overtimeName);
						break;
					case R.id.xiaban_over_new:// 下班加班
						startAttendance(Constants.NEW_ATTENDANCE_OVER_END, overtimeName);
						break;
					case R.id.work_over_new:// 加班
						workOver();
						break;
					case R.id.chaxun_new:// 查询
						searchAttendance();
						break;
					case R.id.fl_atten_pb:// 排班
						attendanceScheduing();
						break;
					case R.id.fl_atten_pb_search:// 排班
						attendanceScheduingSearch();
						break;
					case R.id.ll_attend_calendar:// 考勤日历
						startAttendCalendar();
						break;
					case R.id.fl_atten_leave:// 请假
						askForLeave();
						break;
					}
				} else {
					Toast.makeText(context, R.string.check_net_time, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, R.string.check_net_time1, Toast.LENGTH_SHORT).show();
			}

		}
	};

	/**
	 * 考勤日历查询
	 */
	private void startAttendCalendar() {
		Intent intent = new Intent(context, AttendanceCalendarActivity.class);
		startActivity(intent);
	}

	/**
	 * 考勤排班
	 */
	private void attendanceScheduing() {

		Intent intent = new Intent(context, SchedulingActivity.class);
		startActivity(intent);
	}

	/**
	 * 考勤排班查询
	 */
	private void attendanceScheduingSearch() {
		Intent intent = new Intent(context, SearchSchedulingActivity.class);
		startActivity(intent);
	}

	/**
	 * 请假模块
	 */
	private void askForLeave() {
		Intent intent = new Intent(context, InitiateLeaveActivity.class);
		startActivity(intent);
	}

	/**
	 * 打开查询考勤的Activity
	 */
	private void searchAttendance() {
		int targetId = -2;

		Intent intent = new Intent(context, QueryFuncActivity.class);

		Bundle bundle = new Bundle();

		bundle.putInt("targetId", targetId);

		bundle.putInt("menuType", Menu.TYPE_NEW_ATTENDANCE);

		intent.putExtra("bundle", bundle);

		startActivity(intent);

	}

	/**
	 * 打开考勤详细界面的Activity
	 * 
	 * @param attendanceState
	 * 用于标记打开的是上班考勤还是下班考勤，这个值参考Constants.
	 *  attendanceState用于标记打开的是上班考勤还是下班考勤，这个值参考Constants.
	 *            ATTENDANCE_STATE_START和Constants.ATTENDANCE_STATE_STOP
	 */
	private void startAttendance(int attendanceState, String name) {

		int targetId = Constants.ATTENDANCE_TARGETID;

		Intent intent = new Intent(context, AttendanceFuncActivity.class);

		Bundle bundle = new Bundle();

		bundle.putInt("attState", attendanceState);

		bundle.putInt("targetId", targetId);
		bundle.putString("name", name);

		bundle.putInt("menuType", Menu.TYPE_NEW_ATTENDANCE);

		intent.putExtra("bundle", bundle);

		startActivity(intent);
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}

	}

	/**
	 * 处理下班
	 */
	private void initAttendance(Date date) {
		// 上班时间或者工作时长有可能是小数
		String startTime = SharedPreferencesUtil.getInstance(context).getNewAttendanceWorkDay();// 要求的工作开始时间09:30
		String[] str = startTime.split(":");
		int startHour = 0;// 开始工作时间小时单位
		int startFen = 0;// 开始工作时间分钟单位
		double workHour = 0;// 工作时长
		// 假如上班时间是9.30 工作时长 8.5 9.10 8.5
		if (str != null && str.length >= 2) {
			startHour = Integer.parseInt(str[0]);
			startFen = Integer.parseInt(str[1]);
			workHour = Double.parseDouble(SharedPreferencesUtil.getInstance(context).getNewAttendanceWorkTime());// 获取配置的工作时长
		}
		double workAllTime = startHour + workHour;// 18.0 //工作下班时间
		String xTime = workAllTime + ""; // 工作下班时间转为字符串"18.0"
		String[] xTime1 = xTime.split("\\."); // 分割字符串
		int leftTime = Integer.parseInt(xTime1[0]);// 时间:小时
		if (leftTime >= 24) {
			leftTime = leftTime - 24;
		}
		String endTime = "";
		if (Integer.parseInt(xTime1[1]) == 0) {// 如果下班时间是整数,没有半点
			if (startFen == 0) { // 工作开始时间分钟数为0 例如9:00
				endTime = leftTime + ":00";// 要求结束工作时间
			} else {// 工作开始时间分钟数不为0 例如9:30
				endTime = leftTime + ":" + startFen;
			}
		} else {// 如果配置的工作时长不为整 例如8.5小时
			double rightTime = Double.parseDouble(xTime1[1]);// 获取小数部分
			double fenTime = (rightTime / 10) * 60 + startFen;// 将小数部分转为分钟数,并与开始工作的分钟数相加,得到总分钟数
			int fenTime1 = (int) fenTime;// 总分钟数转为int类型
			if (fenTime1 >= 60) {// 如果总分中>=60,需要向上进一小时
				if (fenTime1 == 60) {
					endTime = leftTime + 1 + ":00";
				} else {
					int fenTime2 = fenTime1 - 60;
					leftTime += 1;
					if (leftTime >= 24) {
						leftTime = leftTime - 24;
					}
					endTime = leftTime + ":" + fenTime2;
				}
			} else {
				if (leftTime >= 24) {
					leftTime = leftTime - 24;
				}
				endTime = leftTime + ":" + fenTime1;
			}

		}
		
		boolean start=false;
		boolean end=false;
		String startDate;
		String stopDate;
		if (!PublicUtils.ISDEMO) {
			 start = SharedPreferencesUtil.getInstance(context).getNewAttendanceStartDoCard();
			 end = SharedPreferencesUtil.getInstance(context).getNewAttendanceEndDoCard();
			 startDate = SharedPreferencesUtil.getInstance(context).getNewAttendanceStart();
			 stopDate = SharedPreferencesUtil.getInstance(context).getNewAttendanceEnd();
		} else {
			 start = SharedPreferencesUtil.getInstance(context).getNativeStartCard();
			 end = SharedPreferencesUtil.getInstance(context).getNativeEndCard();
			 startDate = SharedPreferencesUtil.getInstance(context).getNativeAttendanceStart();
			 stopDate = SharedPreferencesUtil.getInstance(context).getNativeAttendanceEnd();
		}

		// end = false;
		shangban_daka_tip.setText(startTime);// 上班开始时间

		String[] arr = endTime.split(":");
		if (Integer.parseInt(arr[1].trim()) < 10) {
			endTime = arr[0] + ":" + "0" + arr[1];
			if (Integer.parseInt(arr[1].trim()) < 10) {
				endTime = arr[0] + ":" + "00";
			}
		}
		xiaban_daka_tip.setText(endTime);// 下班结束时间
		Calendar calendar = Calendar.getInstance();
		String currentDate = "1900-01-01";
		if (null != date) {
			calendar.setTime(date);
			currentDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH);// 月份按数组下标形式排列，需要加1
		}

		JLog.d("考勤：start:" + start + " end:" + end);
		if (!start && !end) {// 上班下班都没打卡
			shangbanBtn.setEnabled(true);
			xiabanBtn.setEnabled(false);
			xiabanBtn.setBackgroundResource(R.drawable.shape_re_blue2);// shape_re_blue2
			shangbanBtn.setBackgroundResource(R.drawable.shape_re_blue);
			shangban_flag.setImageResource(R.drawable.kaoqin_undo);
			xiaban_flag.setImageResource(R.drawable.kaoqin_undo);

			if (null != date && !TextUtils.isEmpty(startTime)) {
				Date startTimeDate = DateUtil.getDate(currentDate + " " + startTime + ":00");// 要求上班的时间
				if (startTimeDate.before(date)) {
					shangban_late.setText(PublicUtils.getResourceString(context,R.string.late));
				} else {
					shangban_late.setText("");
				}

			} else {
				shangban_late.setText("");
				xiaban_late.setText("");
			}

		} else if (start && !end) {// 上班打卡，下班没打卡
			shangbanBtn.setEnabled(false);
			shangbanBtn.setBackgroundResource(R.drawable.shape_re_blue2);
			xiabanBtn.setEnabled(true);
			xiabanBtn.setBackgroundResource(R.drawable.shape_re_blue);

			shangban_flag.setImageResource(R.drawable.kaoqin_done);
			xiaban_flag.setImageResource(R.drawable.kaoqin_undo);

			if (null != date && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {

				Date startDateDate = DateUtil.getDate(startDate);// 实际上班打卡时间
				Date startTimeDate = DateUtil.getDate(currentDate + " " + startTime + ":00");// 要求上班的时间
				Date endTimeDate = DateUtil.getDate(currentDate + " " + endTime + ":00");// 要求下班的时间
				if (startTimeDate.before(startDateDate)) {
					shangban_late.setText(PublicUtils.getResourceString(context,R.string.late));
					xiaban_late.setText("");
					Date nowDate = new Date();
					if (nowDate.before(endTimeDate)) {// 未到下班时间,提示早退
						xiaban_late.setText(PublicUtils.getResourceString(context,R.string.early));
					} else {
						xiaban_late.setText("");
					}

				} else {
					shangban_late.setText("");
				}
			} else {
				shangban_late.setText("");
				xiaban_late.setText("");
			}
		} else if (start && end) {// 上班下班都打过卡
			shangbanBtn.setEnabled(false);
			shangbanBtn.setBackgroundResource(R.drawable.shape_re_blue2);
			xiabanBtn.setEnabled(false);
			xiabanBtn.setBackgroundResource(R.drawable.shape_re_blue2);

			shangban_flag.setImageResource(R.drawable.kaoqin_done);
			xiaban_flag.setImageResource(R.drawable.kaoqin_done);

			if (null != date && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {

				Date endTimeDate = DateUtil.getDate(currentDate + " " + endTime + ":00");// 要求下班的时间
				Date stopDateDate = DateUtil.getDate(stopDate);// 实际下班打卡时间
				if (stopDateDate.before(endTimeDate)) {
					xiaban_late.setText(PublicUtils.getResourceString(context,R.string.early));
				} else {
					xiaban_late.setText("");
				}
				Date startDateDate = DateUtil.getDate(startDate);// 实际上班打卡时间
				Date startTimeDate = DateUtil.getDate(currentDate + " " + startTime + ":00");// 要求上班的时间
				if (startTimeDate.before(startDateDate)) {
					shangban_late.setText(PublicUtils.getResourceString(context,R.string.late));
				} else {
					shangban_late.setText("");
				}
			} else {
				shangban_late.setText("");
				xiaban_late.setText("");
			}

		}

	}

	/**
	 * 加班
	 */
	private void workOver() {
		View contentView = View.inflate(context, R.layout.attendance_over, null);
		popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句
		int high = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 2 - 100;

		popupWindow.setAnimationStyle(R.style.attendancePopupwindow);
		popupWindow.setHeight(high);
		popupWindow.showAtLocation((LinearLayout) rootView.findViewById(R.id.buttom), Gravity.BOTTOM, 0, 0);
		View shangban_over = (View) contentView.findViewById(R.id.shangban_over_new);
		View xiaban_over = (View) contentView.findViewById(R.id.xiaban_over_new);
		TextView labelShangban_over = (TextView) contentView.findViewById(R.id.shangban_over_icon_label);
		TextView labelXiaban_over = (TextView) contentView.findViewById(R.id.xiaban_over_icon_label);
		ImageView iconShangban_over = (ImageView) contentView.findViewById(R.id.shangban_over_icon);
		ImageView iconXiaban_over = (ImageView) contentView.findViewById(R.id.xiaban_over_icon);

		shangban_over.setOnTouchListener(touchListener);
		xiaban_over.setOnTouchListener(touchListener);

		// 考勤的上班加班日期
		String startOverDate = SharedPreferencesUtil.getInstance(context).getNewAttendanceStartOver();
		// 考勤的下班加班日期
		String stopOverDate = SharedPreferencesUtil.getInstance(context).getNewAttendanceEndOver();

		if (!TextUtils.isEmpty(startOverDate) && !TextUtils.isEmpty(stopOverDate)) {// 初始化
			SharedPreferencesUtil.getInstance(context).setNewAttendanceStartDoCardOver(false);
			SharedPreferencesUtil.getInstance(context).setNewAttendanceEndDoCardOver(false);
			SharedPreferencesUtil.getInstance(context).setNewAttendanceEndOver("");
			SharedPreferencesUtil.getInstance(context).setNewAttendanceStartOver("");
			// SharedPreferencesUtil.getInstance(context).setNewAttendOverTime(DateUtil.getCurDateTime());
			setNewAttendOverTime();
		} else {
			if (!TextUtils.isEmpty(startOverDate)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceStartDoCardOver(true);
			} else {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceStartDoCardOver(false);
			}

			if (!TextUtils.isEmpty(stopOverDate)) {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceEndDoCardOver(true);
			} else {
				SharedPreferencesUtil.getInstance(context).setNewAttendanceEndDoCardOver(false);
			}
		}

		boolean startOver = SharedPreferencesUtil.getInstance(context).getNewAttendanceStartDoCardOver();
		boolean endOver = SharedPreferencesUtil.getInstance(context).getNewAttendanceEndDoCardOver();
		if (!startOver && !endOver) {// 都没打卡
			shangban_over.setEnabled(true);
			xiaban_over.setEnabled(false);
			xiaban_over.setBackgroundResource(R.color.visit_item_order_disable);
			labelShangban_over.setText(getString(R.string.weidaka));
			// SharedPreferencesUtil.getInstance(context).setNewAttendOverTime(DateUtil.getCurDateTime());
			setNewAttendOverTime();
		} else if (startOver && !endOver) {// 上班打卡了下班没有
			shangban_over.setEnabled(false);
			shangban_over.setBackgroundResource(R.color.visit_item_order_disable);
			iconShangban_over.setImageResource(R.drawable.kaoqin_done);
			labelShangban_over.setText(getString(R.string.yidaka));
			xiaban_over.setEnabled(true);
			xiaban_over.setBackgroundResource(R.color.app_color);
		} else if (startOver && endOver) {// 上班下班都打过卡
			shangban_over.setEnabled(false);
			xiaban_over.setEnabled(false);
			xiaban_over.setBackgroundResource(R.color.visit_item_order_disable);
			shangban_over.setBackgroundResource(R.color.visit_item_order_disable);
			labelShangban_over.setText(getString(R.string.yidaka));
			labelXiaban_over.setText(getString(R.string.yidaka));
			iconShangban_over.setImageResource(R.drawable.kaoqin_done);
			iconXiaban_over.setImageResource(R.drawable.kaoqin_done);
		}

	}

	private void setNewAttendOverTime() {
		flag = true;
		timer = new Timer(true);
		if (task.cancel()) {
			timer.schedule(task, 3000, 100000000); // 延时3000ms后执行，1000ms执行一次
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				//context.getApplicationContext().getResources().getString(R.string.bai_du)
				Date date = PublicUtils.getNetDate();
				;
				Message msg = Message.obtain();
				msg.obj = date;
				mHanlder.sendMessage(msg);
			}
		}).start();
	}

	Handler mHanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flag) {
				flag = false;
				if (task != null) {
					task.cancel();
				}
				Date date = (Date) msg.obj;
				if (null == date) {
					SharedPreferencesUtil.getInstance(context).setNewAttendOverTime("");
				} else {
					SharedPreferencesUtil.getInstance(context)
							.setNewAttendOverTime(DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR));
				}
			}
		};
	};
	private boolean flag = true;
	Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			mHanlder.sendEmptyMessage(1);
		}
	};
	//
	// @Override
	// protected void onDestroy() {
	// // Activity销毁前必须调用TitleBar.unregister()
	// if (titleBar != null) {
	// titleBar.unregister();
	// }
	// super.onDestroy();
	// }

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// isEnableNetWork = false;
	// setNewAttendTime();
	// }

}