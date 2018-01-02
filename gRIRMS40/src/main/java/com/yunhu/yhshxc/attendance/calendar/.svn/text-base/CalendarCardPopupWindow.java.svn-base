package com.yunhu.yhshxc.attendance.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.SchedulingActivity;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CalendarCardPopupWindow extends BasePopupWindow implements
		OnItemRender, OnCellItemClick, OnClickListener {

	private CalendarCard mCalendarCard;
	private TextView cardTitle;

	private TextView tv_actTime;
	private String[] actTimeArr;
	private LinkedList<String> selectedDateList;
	private LinkedList<String> notSelectDateList;

	private TextView tv_selecedDate;

	private Button btn_previous;
	private Button btn_next;
	private Button btn_close;
	private Button btn_confirm;
	private SchedulingActivity schedulingActivity;
	private Context context;

	public CalendarCardPopupWindow(Context context) {
		super(context);
		this.context = context;
		this.selectedDateList = new LinkedList<String>();
		this.notSelectDateList = new LinkedList<String>();
		initTodayDate();
	}

	/**
	 * 设置今天日期 格式yyyy/mm/dd
	 */
	private void initTodayDate() {
		String today = DateUtil.dateToDateString(new Date(),
				DateUtil.DATAFORMAT_STR_);
		setDate(today);
	}

	public void setSchedulingActivity(SchedulingActivity schedulingActivity){
		this.schedulingActivity = schedulingActivity;
	}
	@Override
	public View createView() {
		View view = View.inflate(this.getContext(), R.layout.card_popupwindow,
				null);
		mCalendarCard = (CalendarCard) view.findViewById(R.id.calendarCard1);
		mCalendarCard.setOnCellItemClick(this);
		mCalendarCard.setOnItemRender(this);
		cardTitle = (TextView) view.findViewById(R.id.cardTitle);
		tv_actTime = (TextView) view.findViewById(R.id.tv_actTime);
		btn_previous = (Button) view.findViewById(R.id.btn_previous);
		btn_previous.setOnClickListener(this);
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_close = (Button) view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		tv_selecedDate = (TextView) view.findViewById(R.id.tv_selecedDate);
		btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		return view;
	}

	// 2015/08/12 格式
	public void setDate(String date) {
		selectedDateList.clear();
		selectedDateList.add(date);
		if(schedulingActivity != null){
			schedulingActivity.setDate(date);
		}else{
			SchedulingActivity schedulingActivity = new SchedulingActivity();
			schedulingActivity.setDate(date);
		}
		
	}

	// 2015/08/12 格式
	public void setUpdateDate(String date) {
		selectedDateList.clear();
		selectedDateList.add(date);
		if(schedulingActivity != null){
			schedulingActivity.setDate(date);
		}else{
			SchedulingActivity schedulingActivity = new SchedulingActivity();
			schedulingActivity.setDate(date);
		}
	}

	@Override
	public void show() {
		setCalendarCardCurrentDate();
		super.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_close:
			this.dismiss();
			break;
		case R.id.btn_previous:
			changeCalendarCardMonth(-1);
			break;
		case R.id.btn_next:
			changeCalendarCardMonth(1);
			break;
		case R.id.btn_confirm:
			String time = getSelectDate();
			if ("".equals(time)) {
				ToastOrder.makeText(this.getContext(), R.string.end_service3, ToastOrder.LENGTH_SHORT)
						.show();
			} else {
				if(schedulingActivity != null){
					schedulingActivity.setDate(time);
				}else{
					SchedulingActivity schedulingActivity = new SchedulingActivity();
					schedulingActivity.setDate(time);
				}
		
				this.dismiss();
			}

			break;
		default:
			break;
		}
	}

	private void setCalendarCardCurrentDate() {
		Calendar cal = Calendar.getInstance();
		mCalendarCard.setDateDisplay(cal);
		mCalendarCard.notifyChanges();

		String select = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
				.format(mCalendarCard.getDateDisplay().getTime());
		cardTitle.setText(select);
	}

	private void changeCalendarCardMonth(int n) {
		mCalendarCard.getDateDisplay().add(Calendar.MONTH, n);
		mCalendarCard.setDateDisplay(mCalendarCard.getDateDisplay());
		mCalendarCard.notifyChanges();

		String select = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
				.format(mCalendarCard.getDateDisplay().getTime());
		cardTitle.setText(select);
	}

	@Override
	public void onCellClick(View v, CardGridItem item) {
		String clickDate = DateUtil.dateToDateString(item.getDate().getTime(),
				DateUtil.DATAFORMAT_STR_);
		CheckableLayout c = ((CheckableLayout) v);
		// //设置已被他人选中样式
		// if(!c.isEnabled() && c.isSelected()){
		// return;
		// }
		// if (c.isChecked()) { //如果已经选中，则从List中删除，反之添加。
		// c.setChecked(false);
		// setSelectedCell(c,false);
		// selectedDateList.remove(clickDate);
		// } else {
		// c.setChecked(true);
		// setSelectedCell(c,true);
		// selectedDateList.add(clickDate);
		// }
		// tv_selecedDate.setText(getSelectDate());

		cellSelect(c, clickDate);
	}

	private void cellSelect(CheckableLayout c, String clickDate) {
		selectedDateList.clear();
		for (CheckableLayout c_ : mCalendarCard.cells) {
			c_.setChecked(false);
			setSelectedCell(c_, false);
		}
		c.setChecked(true);
		setSelectedCell(c, true);
		selectedDateList.add(clickDate);
		tv_selecedDate.setText(getSelectDate());
	}

	@Override
	public void onRender(CheckableLayout v, CardGridItem item) {

		ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
		imageView.setImageResource(0);
		TextView textView = (TextView) v.findViewById(R.id.textView);
		textView.setText(item.getDayOfMonth().toString());

		// 上个月的文字现实灰色
		if (v.isEnabled()) {
			textView.setTextColor(this.getContext().getResources()
					.getColor(R.color.black));
		} else {
			textView.setTextColor(this.getContext().getResources()
					.getColor(R.color.gray_main));
		}

		// v.setEnabled(false);

		if (item.getDate() == null) {
			return;
		}

		// 显示活动时间区域，其他区域不可点击
		if (actTimeArr != null) {
			for (String per : actTimeArr) {
				if (per.indexOf("-") > 0) {
					if (item.getDate()
							.getTime()
							.before(DateUtil.getDate(per.split("-")[0],
									DateUtil.DATAFORMAT_STR_))
							|| item.getDate()
									.getTime()
									.after(DateUtil.getDate(per.split("-")[1],
											DateUtil.DATAFORMAT_STR_)))
						v.setEnabled(false);
					else
						v.setEnabled(true);

				} else {
					if (DateUtil.dateToDateString(item.getDate().getTime(),
							DateUtil.DATAFORMAT_STR_).equals(per))
						v.setEnabled(true);
				}
			}
		}
		// 设置选中日期的样式
		if (this.selectedDateList.size() > 0) {
			for (String s : selectedDateList) {
				if (DateUtil.dateToDateString(item.getDate().getTime(),
						DateUtil.DATAFORMAT_STR_).equals(s))
					setSelectedCell(textView, imageView, v, true);
			}
		}

		// 设置已被他人选中样式
		if (this.notSelectDateList.size() > 0) {
			for (String s : notSelectDateList) {
				if (DateUtil.dateToDateString(item.getDate().getTime(),
						DateUtil.DATAFORMAT_STR_).equals(s))
					setNotSelectCell(textView, imageView, v);
			}
		}
	}

	private void setSelectedCell(CheckableLayout v, boolean flag) {
		ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
		TextView textView = (TextView) v.findViewById(R.id.textView);
		setSelectedCell(textView, imageView, v, flag);
	}

	private void setSelectedCell(TextView textView, ImageView imageView,
			CheckableLayout v, boolean flag) {
		if (flag) {
			imageView.setImageResource(R.drawable.calendar_cell_choosed);
			textView.setTextColor(this.getContext().getResources()
					.getColor(R.color.white));
		} else {
			imageView.setImageResource(0);
			textView.setTextColor(this.getContext().getResources()
					.getColor(R.color.black));
		}
		v.setSelected(flag);
	}

	private void setNotSelectCell(TextView textView, ImageView imageView,
			CheckableLayout v) {
		imageView.setImageResource(R.drawable.calendar_cell_unchoose);
		textView.setTextColor(this.getContext().getResources()
				.getColor(R.color.black));
		v.setSelected(true);
		v.setEnabled(false);
	}

	/**
	 * 设置用户活动时间，也就是用户可以在日历控件上选择的日期
	 * 
	 * @param actTime
	 */
	public void setActTime(String actTime) {
		if (TextUtils.isEmpty(actTime)) {
			return;
		}
		actTimeArr = actTime.split(",");
		tv_actTime.setText(PublicUtils.getResourceString(context,R.string.date_comlpe1)+"： " + actTime);
	}

	/**
	 * 用户选择后的日期
	 * 
	 * @return
	 */
	public String getSelectDate() {
		StringBuilder sb = new StringBuilder();
		for (String s : selectedDateList) {
			sb.append(",").append(s);
		}
		return sb.length() > 0 ? sb.substring(1) : "";
	}

	public void setSelected(List<String> selectedDateList) {
		this.selectedDateList.addAll(selectedDateList);
	}

	/**
	 * 设置用户已不可以选择的日期
	 * 

	 */
	public void setNotSelectDate(List<String> notSelectDateList) {
		this.notSelectDateList.addAll(notSelectDateList);
	}

	/**
	 * 销毁
	 */
	public void onDestroy() {
		selectedDateList.remove();
		notSelectDateList.remove();
	}
}
