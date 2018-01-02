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
 *
 */
public class TimeRangeComp extends Component {
	private String TAG = "DatePickerRangeComp";
	private Context context;
	private int hour_first,hour_second;
	private int minutes_first,minutes_second;
	private Func func;//当前控件的func
	private CompDialog compDialog;
	/**
	 * 有范围的时间控件的值
	 */
	private String firstTimeValue,secondTimeValue;
	
	public TimeRangeComp(Context context, Func func, CompDialog compDialog) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.compDialog = compDialog;
		this.compFunc=func;
		boolean flag = initData();// true表示显示当前系统日子，false表示显示数据库中的日期
		if (!flag) {
			Calendar c_t=Calendar.getInstance();
			hour_first=c_t.get(Calendar.HOUR_OF_DAY);
			minutes_first=c_t.get(Calendar.MINUTE);
			hour_second=c_t.get(Calendar.HOUR_OF_DAY);
			minutes_second=c_t.get(Calendar.MINUTE);
			firstTimeValue=String.valueOf(hour_first+100).substring(1)+":"+String.valueOf(minutes_first+100).substring(1);
			secondTimeValue=String.valueOf(hour_second+100).substring(1)+":"+String.valueOf(minutes_second+100).substring(1);
		}
		
	}

	
	/**
	 * 最终的值是第一个控件的值和第二个控件的值的拼接的一个日期的范围
	 */
	 int selectType = 0;
	@Override
	public View getObject() {
		View view = View.inflate(context, R.layout.time_range_comp, null);
		LinearLayout ll_second = (LinearLayout)view.findViewById(R.id.timeComp_second);
		final LinearLayout ll_range_start = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
		final LinearLayout ll_range_end = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
		final TextView tv_range_start = (TextView)view.findViewById(R.id.tv_range_time_start);
		final TextView tv_range_end = (TextView)view.findViewById(R.id.tv_range_time_end);
		
		final TimeView t_second = new TimeView(context, TimeView.TYPE_TIME, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				if (selectType == 0) {//选中开始时间
					firstTimeValue = wheelTime;
				}else{
					secondTimeValue = wheelTime;
				}
				tv_range_start.setText(firstTimeValue);
				tv_range_end.setText(secondTimeValue);
				value=firstTimeValue+"~@@"+secondTimeValue;

			}
		});
		
		ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
		ll_range_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectType = 0;	
				ll_range_start.setBackgroundResource(R.color.bbs_menu_blue);
				ll_range_end.setBackgroundResource(R.color.transparent);
				String[] first = firstTimeValue.split(":");
				t_second.setOriTime(Integer.parseInt(first[0]), Integer.parseInt(first[1]));
			}
		});
		ll_range_end.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectType = 1;	
				ll_range_end.setBackgroundResource(R.color.bbs_menu_blue);
				ll_range_start.setBackgroundResource(R.color.transparent);
				String[] second = secondTimeValue.split(":");
				t_second.setOriTime(Integer.parseInt(second[0]), Integer.parseInt(second[1]));
			}
		});
		
		tv_range_start.setText(firstTimeValue);
		tv_range_end.setText(secondTimeValue);
		
		
		t_second.setOriTime(hour_first, minutes_first);
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
		if(compDialog.replenish.containsKey(key)&&func.getType()==Func.TYPE_TIMEPICKERCOMP){
			String itemValue = compDialog.replenish.getString(key);// 将数据库中查出的值赋给当前组件
			String[] time=itemValue.split("~@@");
			hour_first=Integer.parseInt(time[0].split(":")[0]);
			minutes_first=Integer.parseInt(time[0].split(":")[1]);
			hour_second=Integer.parseInt(time[1].split(":")[0]);
			minutes_second=Integer.parseInt(time[1].split(":")[1]);
			flag = true;// 返回true说明已经操作过该项	
			firstTimeValue=hour_first+":"+minutes_first;
			secondTimeValue=hour_second+":"+minutes_second;
			value=firstTimeValue+"~@@"+secondTimeValue;
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

	public Func getFunc() {
		return func;
	}
}
