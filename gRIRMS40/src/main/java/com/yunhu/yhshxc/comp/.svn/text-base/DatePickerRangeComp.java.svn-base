package com.yunhu.yhshxc.comp;

import gcg.org.debug.JLog;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

/**
 * 有范围的日期控件
 * @author jishen
 */
public class DatePickerRangeComp extends Component {
	private String TAG = "DatePickerRangeComp";
	private Context context;
	private int year_first;//控件1的年
	private int month_first;//控件1的月
	private int day_first;//控件1的日
	private int year_second;//控件2的年
	private int month_second;//控件2的月
	private int day_second;//控件2的日
	private Func func;//当前控件的func
	private CompDialog compDialog;
	private String currentBufferFirst,currentBufferSecond;//两个日期组件的值

	public DatePickerRangeComp(Context context, Func func, CompDialog compDialog) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.compDialog = compDialog;
		this.compFunc=func;
		boolean flag = initData();// true表示显示当前系统日子，false表示显示数据库中的日期
		if (!flag) {
			Calendar c = Calendar.getInstance();// 获取系统默认是日期
			year_first = c.get(Calendar.YEAR);
			month_first = c.get(Calendar.MONTH);
			day_first = c.get(Calendar.DAY_OF_MONTH);
			year_second = c.get(Calendar.YEAR);
			month_second = c.get(Calendar.MONTH);
			day_second = c.get(Calendar.DAY_OF_MONTH);
			currentBufferFirst=year_first+"-"+String.valueOf(month_first+ 1+100).substring(1)+"-"+String.valueOf(day_first+100).substring(1);
			currentBufferSecond=year_second+"-"+String.valueOf(month_second+ 1+100).substring(1)+"-"+String.valueOf(day_second+100).substring(1);
			value=currentBufferFirst+"~@@"+currentBufferSecond;
			
		}
	}

	/**
	 * 最终的值是第一个控件的值和第二个控件的值的拼接的一个日期的范围
	 */
	 int selectType = 0;
	@Override
	public View getObject() {
		View view = View.inflate(context, R.layout.date_range_comp, null);
		LinearLayout ll_second = (LinearLayout)view.findViewById(R.id.datePickerComp_second);
		final LinearLayout ll_range_start = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
		final LinearLayout ll_range_end = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
		final TextView tv_range_start = (TextView)view.findViewById(R.id.tv_range_time_start);
		final TextView tv_range_end = (TextView)view.findViewById(R.id.tv_range_time_end);
		
		final TimeView t_second = new TimeView(context, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				if (selectType == 0) {//选中开始时间
					currentBufferFirst = wheelTime;
				}else{
					currentBufferSecond = wheelTime;
				}
				tv_range_start.setText(currentBufferFirst);
				tv_range_end.setText(currentBufferSecond);
				value=currentBufferFirst+"~@@"+currentBufferSecond;
			}
		});
		t_second.setOriDate(year_first, month_first+1, day_first);
		
		ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
		ll_range_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectType = 0;	
				ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
				ll_range_end.setBackgroundResource(R.color.transparent);
				String[] first = currentBufferFirst.split("-");
				t_second.setOriDate(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]));
			}
		});
		ll_range_end.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectType = 1;	
				ll_range_end.setBackgroundResource(R.color.bbs_menu_blue);
				ll_range_start.setBackgroundResource(R.color.transparent);
				String[] second = currentBufferSecond.split("-");
				t_second.setOriDate(Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(second[2]));
			}
		});
		
		tv_range_start.setText(currentBufferFirst);
		tv_range_end.setText(currentBufferSecond);
		ll_second.addView(t_second);
		Log.d(TAG, "getObjectvalue=" + value);
		return view;
	}

	/**
	 * 设置初始数据
	 * @return true表示显示当前系统日子，false表示显示数据库中的日期
	 */
	private boolean initData() {
		boolean flag = false;
		String key=func.getFuncId()+"";
		if(compDialog.replenish.containsKey(key)&&func.getType()==Func.TYPE_DATEPICKERCOMP){
			String itemValue = compDialog.replenish.getString(key);// 将数据库中查出的值赋给当前组件
			JLog.d(TAG, "itemValue==>" + itemValue);
			String[] date = itemValue.split("~@@");
			String[] firstDate=date[0].split("-");
			String[] secondDate=date[1].split("-");
			year_first = Integer.parseInt(firstDate[0]);
			month_first = Integer.parseInt(firstDate[1]) - 1;
			day_first = Integer.parseInt(firstDate[2]);
			year_second = Integer.parseInt(secondDate[0]);
			month_second = Integer.parseInt(secondDate[1]) - 1;
			day_second = Integer.parseInt(secondDate[2]);
			flag = true;// 返回true说明已经操作过该项	
			
			currentBufferFirst=year_first+"-"+String.valueOf(month_first+ 1+100).substring(1)+"-"+String.valueOf(day_first+100).substring(1);
			currentBufferSecond=year_second+"-"+String.valueOf(month_second+ 1+100).substring(1)+"-"+String.valueOf(day_second+100).substring(1);
			value=currentBufferFirst+"~@@"+currentBufferSecond;
			JLog.d(TAG, "initDataValue==>"+value);
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		getObject().setEnabled(isEnable);
	}

	public int getFirstYear() {
		return year_first;
	}

	public int getFirstMonth() {
		return month_first;
	}

	public int getFirstDay() {
		return day_first;
	}
	public int getSecondYear() {
		return year_second;
	}
	
	public int getSecondMonth() {
		return month_second;
	}
	
	public int getSecondDay() {
		return day_second;
	}

	public Func getFunc() {
		return func;
	}
}
