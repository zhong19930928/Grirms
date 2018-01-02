package com.yunhu.yhshxc.widget.wheeltime;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

public class TimeView extends LinearLayout {

	public static int TYPE_DATE_TIME = 0;
	public static int TYPE_DATE = 1;
	public static int TYPE_TIME = 2;
	public static int TYPE_DATE_HOUR= 3;
	
	private int type = 0;

	
	private WheelView yearWV = null;
	private WheelView monthWV = null;
	private WheelView dayWV = null;
	private WheelView hourWV = null;
	private WheelView minuteWV = null;

	// 滚轮上的数据，字符串数组
	private String[] yearArrayString = null;
	private String[] dayArrayString = null;
	private String[] monthArrayString = null;
	private String[] hourArrayString = null;
	private String[] minuteArrayString = null;
	
	private WheelTimeListener wheelTimeListener;

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeView(Context context) {
		super(context);
		init();
	}

	public TimeView(Context context, int type) {
		super(context);
		this.type = type;
		init();
	}
	
	public TimeView(Context context, int type,WheelTimeListener listener) {
		super(context);
		this.type = type;
		this.wheelTimeListener = listener;
		init();
	}

	private void init() {

		yearArrayString = getYEARArray(1900, 1000);
		monthArrayString = getDayArray(12);
		hourArrayString = getHMArray(24);
		minuteArrayString = getHMArray(60);

		this.setOrientation(VERTICAL);

		View wheelView = View.inflate(this.getContext(), R.layout.time_wheel,
				null);
		this.addView(wheelView);

		setWheelView();
	}

	private void setWheelView() {

		yearWV = (WheelView) findViewById(R.id.time_year);
		monthWV = (WheelView) findViewById(R.id.time_month);
		dayWV = (WheelView) findViewById(R.id.time_day);
		hourWV = (WheelView) findViewById(R.id.time_hour);
		minuteWV = (WheelView) findViewById(R.id.time_minute);

		if (type == TYPE_DATE) {
			hourWV.setVisibility(GONE);
			minuteWV.setVisibility(GONE);
		} else if (type == TYPE_TIME) {
			yearWV.setVisibility(GONE);
			monthWV.setVisibility(GONE);
			dayWV.setVisibility(GONE);
		} else if(type == TYPE_DATE_HOUR){
			minuteWV.setVisibility(GONE);
		}else{
			
		}

		// 设置每个滚轮的行数
		yearWV.setVisibleItems(5);
		monthWV.setVisibleItems(5);
		dayWV.setVisibleItems(5);
		hourWV.setVisibleItems(5);
		minuteWV.setVisibleItems(5);

		// 设置滚轮的标签
		yearWV.setLabel(PublicUtils.getResourceString(getContext(),R.string.year));
		monthWV.setLabel(PublicUtils.getResourceString(getContext(),R.string.month));
		dayWV.setLabel(PublicUtils.getResourceString(getContext(),R.string.day));
		hourWV.setLabel(PublicUtils.getResourceString(getContext(),R.string.hour));
		minuteWV.setLabel(PublicUtils.getResourceString(getContext(),R.string.minute));

		// 设置是否要循环
		yearWV.setCyclic(true);
		monthWV.setCyclic(true);
		dayWV.setCyclic(true);
		hourWV.setCyclic(true);
		minuteWV.setCyclic(true);

		setData();
	}

	/**
	 * 给滚轮提供数据
	 */
	private void setData() {
		// 给滚轮提供数据
		yearWV.setAdapter(new ArrayWheelAdapter<String>(yearArrayString));
		monthWV.setAdapter(new ArrayWheelAdapter<String>(monthArrayString));
		hourWV.setAdapter(new ArrayWheelAdapter<String>(hourArrayString));
		minuteWV.setAdapter(new ArrayWheelAdapter<String>(minuteArrayString));

		yearWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// 获取年和月
				int year = Integer.parseInt(yearArrayString[yearWV
						.getCurrentItem()]);
				int month = Integer.parseInt(monthArrayString[monthWV
						.getCurrentItem()]);
				// 根据年和月生成天数数组
				dayArrayString = getDayArray(getDay(year, month));
				// 给天数的滚轮设置数据
				dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
				// 防止数组越界
				if (dayWV.getCurrentItem() >= dayArrayString.length) {
					dayWV.setCurrentItem(dayArrayString.length - 1);
				}
				// 显示的时间
				showDate();
			}
		});

		// 当月变化时显示的时间
		monthWV.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				// 获取年和月
				int year = Integer.parseInt(yearArrayString[yearWV
						.getCurrentItem()]);
				int month = Integer.parseInt(monthArrayString[monthWV
						.getCurrentItem()]);
				// 根据年和月生成天数数组
				dayArrayString = getDayArray(getDay(year, month));
				// 给天数的滚轮设置数据
				dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
				// 防止数组越界
				if (dayWV.getCurrentItem() >= dayArrayString.length) {
					dayWV.setCurrentItem(dayArrayString.length - 1);
				}
				// 显示的时间
				showDate();
			}
		});

		// 当天变化时，显示的时间
		dayWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 当小时变化时，显示的时间
		hourWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 当分钟变化时，显示的时间
		minuteWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 把当前系统时间显示为滚轮默认时间
		setOriTime();
	}

	// 设定初始时间
	private void setOriTime() {
		// 把当前系统时间显示为滚轮默认时间
		Calendar c = Calendar.getInstance();
		setOriDateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE));
	}

	// 设定初始时间
	public void setOriDateTime(int year, int month, int day, int hour, int min) {
		yearWV.setCurrentItem(getNumData(year + "", yearArrayString));
		monthWV.setCurrentItem(getNumData(getFormatNum(month), monthArrayString));

		dayArrayString = getDayArray(getDay(year, month));
		dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
		dayWV.setCurrentItem(getNumData(getFormatNum(day), dayArrayString));

		hourWV.setCurrentItem(getNumData(getFormatNum(hour), hourArrayString));
		minuteWV.setCurrentItem(getNumData(getFormatNum(min), minuteArrayString));

		// 初始化显示的时间
		showDate();
	}

	// 设定初始时间
	public void setOriDate(int year, int month, int day) {
		yearWV.setCurrentItem(getNumData(year + "", yearArrayString));
		monthWV.setCurrentItem(getNumData(getFormatNum(month), monthArrayString));

		dayArrayString = getDayArray(getDay(year, month));
		dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
		dayWV.setCurrentItem(getNumData(getFormatNum(day), dayArrayString));

		// 初始化显示的时间
		showDate();
	}

	//设定初始时间
	public void setOriDateByHour(int year, int month, int day,int hour){
		yearWV.setCurrentItem(getNumData(year + "", yearArrayString));
		monthWV.setCurrentItem(getNumData(getFormatNum(month), monthArrayString));

		dayArrayString = getDayArray(getDay(year, month));
		dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
		dayWV.setCurrentItem(getNumData(getFormatNum(day), dayArrayString));

		hourWV.setCurrentItem(getNumData(getFormatNum(hour), hourArrayString));
//		minuteWV.setCurrentItem(getNumData(getFormatNum(min), minuteArrayString));

		// 初始化显示的时间
		showDate();
	}
	
	// 设定初始时间
	public void setOriTime(int hour, int min) {
		hourWV.setCurrentItem(getNumData(getFormatNum(hour), hourArrayString));
		minuteWV.setCurrentItem(getNumData(getFormatNum(min), minuteArrayString));
		// 初始化显示的时间
		showDate();
	}

	// 显示时间
	private void showDate() {
		// time_TV.setText("选择时间为：" + createDate());
		if(wheelTimeListener != null){
			wheelTimeListener.onResult(createDate());
		}
	}

	public String createDate() {
		return createDate(yearArrayString[yearWV.getCurrentItem()],
				monthArrayString[monthWV.getCurrentItem()],
				dayArrayString[dayWV.getCurrentItem()],
				hourArrayString[hourWV.getCurrentItem()],
				minuteArrayString[minuteWV.getCurrentItem()]);
	}
	
	public interface WheelTimeListener {
		
		public void onResult(String wheelTime);
	}

	// 生成时间
	private String createDate(String year, String month, String day,
			String hour, String minute) {
		String dateStr = null;
		if (type == TYPE_DATE) {
			dateStr = year + "-" + month + "-" + day;
		} else if (type == TYPE_TIME) {
			dateStr = hour + ":" + minute;
		} else {
			dateStr = year + "-" + month + "-" + day + " " + hour + ":"
					+ minute;
		}

		return dateStr;
	}

	// 在数组Array[]中找出字符串s的位置
	private int getNumData(String s, String[] Array) {
		int num = 0;
		for (int i = 0; i < Array.length; i++) {
			if (s.equals(Array[i])) {
				num = i;
				break;
			}
		}
		return num;
	}

	// 根据当前年份和月份判断这个月的天数
	private int getDay(int year, int month) {
		int day;
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) { // 闰年
			if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				day = 31;
			} else if (month == 2) {
				day = 29;
			} else {
				day = 30;
			}
		} else { // 平年
			if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				day = 31;
			} else if (month == 2) {
				day = 28;
			} else {
				day = 30;
			}
		}
		return day;
	}

	// 根据数字生成一个字符串数组
	private String[] getDayArray(int day) {
		String[] dayArr = new String[day];
		for (int i = 0; i < day; i++) {
			dayArr[i] = getFormatNum(i + 1);
		}
		return dayArr;
	}

	// 根据数字生成一个字符串数组
	private String[] getHMArray(int day) {
		String[] dayArr = new String[day];
		for (int i = 0; i < day; i++) {
			dayArr[i] = getFormatNum(i);
		}
		return dayArr;
	}

	// 根据初始值start和step得到一个字符数组，自start起至start+step-1
	private String[] getYEARArray(int start, int step) {
		String[] dayArr = new String[step];
		for (int i = 0; i < step; i++) {
			dayArr[i] = start + i + "";
		}
		return dayArr;
	}
	
	private String getFormatNum(int num){
		return String.valueOf(num+100).substring(1);
	}
}
