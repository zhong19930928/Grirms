package com.yunhu.yhshxc.comp;

import gcg.org.debug.JLog;

import java.util.Calendar;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

/**
 *日期控件
 */
public class DatePickerComp extends Component {
	private String TAG = "DatePickerComp";
	private Context context;
	private int year;//年
	private int month;//月
	private int day;//日
	private Func func;
	private CompDialog compDialog;

	public DatePickerComp(Context context, Func func, CompDialog compDialog) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.compDialog = compDialog;
		this.compFunc=func;
		boolean flag = initData();// true表示显示当前系统日子，false表示显示数据库中的日期
		if (!flag) {//显示系统默认日期
			Calendar c = Calendar.getInstance();// 获取系统默认是日期
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.valueOf(getYear())).append("-")
				.append(String.valueOf(getMonth() + 1+100).substring(1)).append("-")
				.append(String.valueOf(getDay()+100).substring(1));
		value = buffer.toString();// 将日期值赋给Value
		Log.d(TAG, "value=" + value);
	}

	/**
	 * 获取日期控件的布局
	 */
	@Override
	public View getObject() {
//		View view = View.inflate(context, R.layout.datepickercomp_comp, null);
//		DatePicker datePicker = (DatePicker) view
//				.findViewById(R.id.datePickerComp);
//		datePicker.init(year, month, day, new OnDateChangedListener() {
//
//			@Override
//			public void onDateChanged(DatePicker view, int years,
//					int monthOfYear, int dayOfMonth) {
//				year = years;
//				month = monthOfYear;
//				day = dayOfMonth;
//				StringBuffer currentBuffer = new StringBuffer();
//				currentBuffer.append(String.valueOf(getYear())).append("-")
//						.append(String.valueOf(getMonth() + 1+100).substring(1)).append("-")
//						.append(String.valueOf(getDay()+100).substring(1));
//				value = currentBuffer.toString();//日期控件的值 ，不足两位的前面补0
//				Log.d(TAG, "value=" + value);
//			}
//		});
		
		TimeView view = new TimeView(context, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				value = wheelTime;
			}
		});
		view.setOriDate(year, month+1, day);
		return view;
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
			
			String itemValue = item.getParamValue();// 将数据库中查出的值赋给当前组件
			if (!TextUtils.isEmpty(itemValue)) {
				String[] str = itemValue.split(" ");
				if (str.length==2) {
					itemValue = str[0];
				}
				
				JLog.d(TAG, "itemValue==>" + itemValue);
				String[] currentValue = itemValue.split("-");
				year = Integer.parseInt(currentValue[0]);
				month = Integer.parseInt(currentValue[1]) - 1;
				day = Integer.parseInt(currentValue[2]);
				flag = true;// 返回true说明已经操作过该项	
			}
		}else{
			if(compDialog.replenish!=null && compDialog.replenish.containsKey(func.getFuncId()+"")){//查询条件
				value = compDialog.replenish.getString(func.getFuncId()+"");
				String[] currentValue = value.split("-");
				year = Integer.parseInt(currentValue[0]);
				month = Integer.parseInt(currentValue[1]) - 1;
				day = Integer.parseInt(currentValue[2]);
				flag = true;// 返回true说明已经操作过该项	
				JLog.d(TAG, "itemValue==>" + value);
			}
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

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public Func getFunc() {
		return func;
	}
}
