package com.yunhu.yhshxc.comp;

import java.util.Calendar;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

/**
 * 时间控件
 * @author gcg_jishen
 */
public class TimePickerComp extends Component implements OnTimeChangedListener {
	private final String TAG = "TimePickerComp";
	private Context context;
	private int hour;
	private int minutes;
	private Func func;
	private CompDialog compDialog;

	public TimePickerComp(Context context, Func func,CompDialog compDialog) {
		this.context = context;
		this.compDialog = compDialog;
		this.func = func;
		this.type = func.getType();
		boolean flag = initData();// true表示显示当前系统日子，false表示显示数据库中的日期
		if (!flag) {//显示系统默认时间
			Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minutes = c.get(Calendar.MINUTE);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.valueOf(getHour()+100).substring(1)).append(":").append(String.valueOf(getMinutes()+100).substring(1));
		value = buffer.toString();// 将日期值赋给Value
		Log.d(TAG, "value=" + value);
	}

	public Func getFunc() {
		return func;
	}

	@Override
	public View getObject() {
//		View view = View.inflate(context, R.layout.timepickercomp_comp, null);
//		TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePickerComp);
//		timePicker.setOnTimeChangedListener(this);
//		timePicker.setCurrentHour(hour);
//		timePicker.setCurrentMinute(minutes);
//		timePicker.setIs24HourView(true);
		TimeView view = new TimeView(context, TimeView.TYPE_TIME, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				value = wheelTime;
			}
		});
		view.setOriTime(hour, minutes);
		return view;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		hour = hourOfDay;
		minutes = minute;
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.valueOf(getHour()+100).substring(1)).append(":").append(String.valueOf(getMinutes()+100).substring(1));
		value = buffer.toString();// 将日期值赋给Value
		Log.d(TAG, "value=" + value);
	}
	
	
	/**
	 * 设置初始数据
	 * @return true表示显示当前系统日子，false表示显示数据库中的日期
	 */
	private boolean initData() {
		boolean flag = false;
		SubmitItem item=null;
		if(compDialog.isLink){//超链接模块中的控件 查询临时表
			item=new SubmitItemTempDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, compDialog.planId, 
					compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType, String.valueOf(func.getFuncId()));
		}else{
			item=new SubmitItemDB(context).findSubmitItem(null, Submit.STATE_NO_SUBMIT, compDialog.planId, 
					compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType, String.valueOf(func.getFuncId()));
		}
		if(item!=null && compDialog.replenish==null){//不是查询条件，从数据库中读取数据
			value = item.getParamValue();// 将数据库中查出的值赋给当前组件
		}else{
			if(compDialog.replenish!=null && compDialog.replenish.containsKey(String.valueOf(func.getFuncId()))){//查询条件
				value = compDialog.replenish.getString(String.valueOf(func.getFuncId()));
			}
		}
		if (!TextUtils.isEmpty(value)) {
			String[] currentValue = value.split(":");
			hour = Integer.parseInt(currentValue[0]);
			minutes = Integer.parseInt(currentValue[1]);
			flag = true;// 返回true说明已经操作过该项	
		}
		return flag;
	}
	

	/*
	 * 返回小时
	 */
	public int getHour() {
		return hour;
	}

	/*
	 * 返回分钟
	 */
	public int getMinutes() {
		return minutes;
	}

	@Override
	public void setIsEnable(boolean isEnable) {

	}

}
