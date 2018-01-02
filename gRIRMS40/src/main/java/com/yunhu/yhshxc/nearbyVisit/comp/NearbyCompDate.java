package com.yunhu.yhshxc.nearbyVisit.comp;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

public class NearbyCompDate extends AbsNearbyComp{

	/**
	 * 有范围的日期控件的年月日的拼接结果
	 */
	private LinearLayout ll_range_time_start,ll_range_time_end;
	private TextView tv_range_time_start,tv_range_time_end;
	public NearbyCompDate(Context mContext) {
		super(mContext);
	}
	
	@Override
	protected View contentView() {
		View view = View.inflate(context, R.layout.nearby_comp_date, null);
		ll_range_time_start = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
		ll_range_time_end = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
		tv_range_time_start = (TextView)view.findViewById(R.id.tv_range_time_start);
		tv_range_time_end = (TextView)view.findViewById(R.id.tv_range_time_end);
		ll_range_time_start.setOnClickListener(listener);
		ll_range_time_end.setOnClickListener(listener);
		
		return view;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_range_time_start:
				selectData(TYPE_START, startDate);
				break;
			case R.id.ll_range_time_end:
				selectData(TYPE_END, endDate);
				break;

			default:
				break;
			}
		}
	};
	public void setStratTime(String start){
		if(!TextUtils.isEmpty(start)&&tv_range_time_start!=null){
			startDate = start;
			tv_range_time_start.setText(start);
		}
		
	}
	public void setEndTime(String end){
		if(!TextUtils.isEmpty(end)&&tv_range_time_end!=null){
			endDate = end;
			tv_range_time_end.setText(end);
		}
		
	}
	
	private final int TYPE_START = 1;//开始时间
	private final int TYPE_END = 2;//结束时间
	private int year,month,day;
	private String startDate,endDate;
	private String currentValue;
	private void selectData(final int type,String value){
		currentValue = value;
		final Dialog dialog = new Dialog(context, R.style.transparentDialog);
		View view=null;
		view=View.inflate(context, R.layout.report_date_dialog, null);
		if(!TextUtils.isEmpty(value)){
			String[] date = value.split("-");
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]) - 1;
			day = Integer.parseInt(date[2]);
		}else{
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
		}
		LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		TimeView dateView = new TimeView(context,TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				currentValue = wheelTime;
			}
		});
		dateView.setOriDate(year, month+1, day);
		ll_date.addView(dateView);
		Button confirmBtn=(Button)view.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn=(Button)view.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (type == TYPE_START) {
					startDate = currentValue;
					tv_range_time_start.setText(startDate);
				}else{
					endDate = currentValue;
					tv_range_time_end.setText(endDate);
				}
				
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	
//	@Override
//	protected View contentView() {
//		View view = View.inflate(context, R.layout.nearby_comp_date, null);
//		selectType = 0;
//		if(!TextUtils.isEmpty(currentValue)){
//			String[] date = currentValue.split("~@@");
//			String[] firstDate=date[0].split("-");
//			String[] secondDate=date[1].split("-");
//			year_first = Integer.parseInt(firstDate[0]);
//			month_first = Integer.parseInt(firstDate[1]) - 1;
//			day_first = Integer.parseInt(firstDate[2]);
//			year_second = Integer.parseInt(secondDate[0]);
//			month_second = Integer.parseInt(secondDate[1]) - 1;
//			day_second = Integer.parseInt(secondDate[2]);
//		}else{
//			Calendar __c = Calendar.getInstance();// 获取系统默认是日期
//			year_first = __c.get(Calendar.YEAR);
//			month_first = __c.get(Calendar.MONTH);
//			day_first = __c.get(Calendar.DAY_OF_MONTH);
//			year_second = __c.get(Calendar.YEAR);
//			month_second = __c.get(Calendar.MONTH);
//			day_second = __c.get(Calendar.DAY_OF_MONTH);
//		}
//		currentBufferFirst=year_first+"-"+String.valueOf(month_first+ 1+100).substring(1)+"-"+String.valueOf(day_first+100).substring(1);
//		currentBufferSecond=year_second+"-"+String.valueOf(month_second+ 1+100).substring(1)+"-"+String.valueOf(day_second+100).substring(1);
//		currentValue=currentBufferFirst+"~@@"+currentBufferSecond;
//		LinearLayout ll_second_date = (LinearLayout)view.findViewById(R.id.datePickerComp_second);
//		final LinearLayout ll_range_start_date = (LinearLayout)view.findViewById(R.id.ll_range_time_start);
//		final LinearLayout ll_range_end_date = (LinearLayout)view.findViewById(R.id.ll_range_time_end);
//		final TextView tv_range_start_date = (TextView)view.findViewById(R.id.tv_range_time_start);
//		final TextView tv_range_end_date = (TextView)view.findViewById(R.id.tv_range_time_end);
//		final TimeView d_second = new TimeView(context, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
//			
//			@Override
//			public void onResult(String wheelTime) {
//				if (selectType == 0) {//选中开始时间
//					currentBufferFirst = wheelTime;
//				}else{
//					currentBufferSecond = wheelTime;
//				}
//				tv_range_start_date.setText(currentBufferFirst);
//				tv_range_end_date.setText(currentBufferSecond);
//				currentValue=currentBufferFirst+"~@@"+currentBufferSecond;
//
//			}
//		});
//
//		ll_range_start_date.setBackgroundResource(R.color.bbs_menu_blue);
//		ll_range_start_date.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				selectType = 0;	
//				ll_range_start_date.setBackgroundResource(R.color.bbs_menu_blue);
//				ll_range_end_date.setBackgroundResource(R.color.transparent);
//				String[] first = currentBufferFirst.split("-");
//				d_second.setOriDate(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]));
//			}
//		});
//		ll_range_end_date.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				selectType = 1;	
//				ll_range_end_date.setBackgroundResource(R.color.bbs_menu_blue);
//				ll_range_start_date.setBackgroundResource(R.color.transparent);
//				String[] second = currentBufferSecond.split("-");
//				d_second.setOriDate(Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(second[2]));
//			}
//		});
//		
//		tv_range_start_date.setText(currentBufferFirst);
//		tv_range_end_date.setText(currentBufferSecond);
//		d_second.setOriDate(year_first, month_first+1, day_first);
//		ll_second_date.addView(d_second);
//		return view;
//	}
//	
	@Override
	public String getValue() {
		return startDate+"~@@"+endDate;
	}

}
