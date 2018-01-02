package com.yunhu.yhshxc.attendance.attendCalendar;


import gcg.org.debug.JLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.calendar.CardGridItem;
import com.yunhu.yhshxc.attendance.calendar.CheckableLayout;
import com.yunhu.yhshxc.attendance.calendar.OnCellItemClick;
import com.yunhu.yhshxc.attendance.calendar.OnItemRender;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class AttendanceCalendarActivity extends Activity implements
OnItemRender, OnCellItemClick, OnClickListener{
	private final String TAG = "AttendanceCalendarActivity";
	private AttendCalendarCard mCalendarCard;
	private TextView cardTitle;

	private TextView tv_actTime;
	private String[] actTimeArr;
	private LinkedList<String> selectedDateList=new LinkedList<String>();
	private LinkedList<String> notSelectDateList=new LinkedList<String>();

	private TextView tv_selecedDate;
	private LinearLayout ll_address_return;
	private Button btn_previous;
	private Button btn_next;
	private Button btn_close;
	
	private Date nowTime;
	private String nowMoth;
	
	private TextView tv_info_title;
	private LinearLayout ll_all_record;
	
	private List<AttendanceInfo> infos = new ArrayList<AttendanceInfo>();
	private List<AttendanceInfo> infosAll = new ArrayList<AttendanceInfo>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_calendar_card);
		nowMoth = DateUtil.getDateByMonth(DateUtil.getCurrentDate());
		nowTime = DateUtil.getCurrentDate();
		String today = DateUtil.dateToDateString(nowTime,
				DateUtil.DATAFORMAT_STR_);
		setDate(today);
		init();
		initData(nowMoth,0);
		
	}
	private Dialog searchDialog;
	private void initData(final String nowMoth,final int n) {
			searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
					PublicUtils.getResourceString(this,R.string.init));
			String url = UrlInfo.queryAttendListInfo(this);
			RequestParams param = searchParams(nowMoth);
			GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
					param, new HttpResponseListener() {
						@Override
						public void onStart() {
								searchDialog.show();
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							JLog.d(TAG, "onSuccess:" + content);
							try {
								JSONObject obj = new JSONObject(content);
								String resultCode = obj.getString("resultcode");
								if ("0000".equals(resultCode)) {
									infos.clear();
										if(obj.has("data")){
											JSONObject array = obj.optJSONObject("data");
											infos = new CarSalesParse(
													getApplicationContext()).parserSearchAttendInfoItem(array);
										}
										
									updateInfos(infos,nowMoth);
								} else {
									throw new Exception();
								}
							} catch (Exception e) {
								e.printStackTrace();
//								ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA,
//										ToastOrder.LENGTH_SHORT).show();
							}
						}

						

						@Override
						public void onFailure(Throwable error, String content) {
							JLog.d(TAG, "onFailure:" + content);
							ToastOrder.makeText(getApplicationContext(), R.string.retry_net_exception,
									ToastOrder.LENGTH_SHORT).show();
						}

						@Override
						public void onFinish() {
							if (searchDialog != null && searchDialog.isShowing()) {
								searchDialog.dismiss();
							}
							initTodayDate(n);
						}

					});
	}

	private RequestParams searchParams(String nowMoth) {
		RequestParams param = new RequestParams();
		param.put("phoneno",
				PublicUtils.receivePhoneNO(getApplicationContext()));
//		param.put("userId", "");
		param.put("month", nowMoth);
		return param;
	}
	private void updateInfos(List<AttendanceInfo> infos,String nowMonth) {
		int number = PublicUtils.getNumberByMonth(nowMonth);
		infosAll.clear();
		for (int i = 0; i < number; i++) {
			AttendanceInfo info = null ;
			boolean istrue = false;
			for (int j = 0; j < infos.size(); j++) {
				String newData = nowMonth+"-"+String.format("%02d", i+1);
				if(newData.equals(infos.get(j).getTime())){
					istrue = true;
					info = infos.get(j);
					break;
				}
			}
			if(!istrue){
				info = new AttendanceInfo();
			}
			infosAll.add(info);
		}
	}
	private void initTodayDate(int n) {
		
		setCalendarCardCurrentDate(n);
	}

	private void setDate(String today) {
		selectedDateList.clear();
		selectedDateList.add(today);
		
	}

	private void init() {
		mCalendarCard = (AttendCalendarCard)findViewById(R.id.calendarCard1);
		mCalendarCard.setOnCellItemClick(this);
		mCalendarCard.setOnItemRender(this);
		cardTitle = (TextView)findViewById(R.id.cardTitle);
		tv_actTime = (TextView) findViewById(R.id.tv_actTime);
		btn_previous = (Button) findViewById(R.id.btn_previous);
		btn_previous.setOnClickListener(this);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_close = (Button) findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);
		btn_close.setVisibility(View.INVISIBLE);
		ll_address_return = (LinearLayout) findViewById(R.id.ll_address_return);
		ll_address_return.setOnClickListener(this);
		tv_selecedDate = (TextView) findViewById(R.id.tv_selecedDate);
		tv_info_title = (TextView) findViewById(R.id.tv_info_title);
		ll_all_record = (LinearLayout) findViewById(R.id.ll_all_record);
		ll_all_record.setVisibility(View.GONE);
		
	}
	private void setCalendarCardCurrentDate(int n) {
		if(n == 0){
			Calendar cal = Calendar.getInstance();
			mCalendarCard.setDateDisplay(cal);
			mCalendarCard.notifyChanges();
		}else{
//			mCalendarCard.getDateDisplay().add(Calendar.MONTH, n);
//			mCalendarCard.setDateDisplay(mCalendarCard.getDateDisplay());
			mCalendarCard.notifyChanges();
		}

		String select = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
				.format(mCalendarCard.getDateDisplay().getTime());
		cardTitle.setText(select);
	}
	// 2015/08/12 格式
		public void setUpdateDate(String date) {
			selectedDateList.clear();
			selectedDateList.add(date);
			
		}
	
	@Override
	public void onCellClick(View v, CardGridItem item) {
		String clickDate = DateUtil.dateToDateString(item.getDate().getTime(),
				DateUtil.DATAFORMAT_STR);
		CheckableLayout c = ((CheckableLayout) v);

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

		setRefresh(clickDate);
		
	}
	private void setRefresh(String clickDate){
		String[] str=clickDate.split("-");
		if(infosAll.size()>0){
			if(str.length>2){
				int index=Integer.parseInt(str[2])-1;
				if (index<infosAll.size()) {		
					AttendanceInfo in = infosAll.get(index);
					if(in!=null){
						if(!TextUtils.isEmpty(in.getTime())&&in.getTime().equals(clickDate)){
							ll_all_record.setVisibility(View.VISIBLE);
							setDetailInfo(in);
						}else{
							tv_info_title.setText(PublicUtils.getResourceString(this,R.string.date_comlpe2));
							ll_all_record.removeAllViews();
							ll_all_record.setVisibility(View.GONE);
						}
					}
				}
				

			}
			
		}else{
			tv_info_title.setText(PublicUtils.getResourceString(this,R.string.date_comlpe2));
			ll_all_record.removeAllViews();
			ll_all_record.setVisibility(View.GONE);
		}
	}
	private void setDetailInfo(AttendanceInfo in) {
		if(!TextUtils.isEmpty(in.getIsExp())){
			if(in.getIsExp().equals("0")){
				tv_info_title.setText(PublicUtils.getResourceString(this,R.string.calendar_title2));
				ll_all_record.removeAllViews();
				AttendanceInfoView view = new AttendanceInfoView(AttendanceCalendarActivity.this);
				view.setDataIn(in);
				ll_all_record.addView(view.getView());
				AttendanceInfoView view1 = new AttendanceInfoView(AttendanceCalendarActivity.this);
				view1.setDataOut(in);
				ll_all_record.addView(view1.getView());
				if(!TextUtils.isEmpty(in.getInTimeJ())){
					AttendanceInfoView view2 = new AttendanceInfoView(AttendanceCalendarActivity.this);
					view2.setDataInJ(in);
					ll_all_record.addView(view2.getView());
				}
				if(!TextUtils.isEmpty(in.getOutTimeJ())){
					AttendanceInfoView view3 = new AttendanceInfoView(AttendanceCalendarActivity.this);
					view3.setDataOutJ(in);
					ll_all_record.addView(view3.getView());
				}
				
				
			}else if(in.getIsExp().equals("1")){
				tv_info_title.setText(PublicUtils.getResourceString(this,R.string.calendar_title3));
				ll_all_record.removeAllViews();
				AttendanceInfoView view = new AttendanceInfoView(AttendanceCalendarActivity.this);
				view.setDataInLate(in);
				ll_all_record.addView(view.getView());
				AttendanceInfoView view1 = new AttendanceInfoView(AttendanceCalendarActivity.this);
				view1.setDataOutEarly(in);
				ll_all_record.addView(view1.getView());
				if(!TextUtils.isEmpty(in.getInTimeJ())){
					AttendanceInfoView view2 = new AttendanceInfoView(AttendanceCalendarActivity.this);
					view2.setDataInJ(in);
					ll_all_record.addView(view2.getView());
				}
				if(!TextUtils.isEmpty(in.getOutTimeJ())){
					AttendanceInfoView view3 = new AttendanceInfoView(AttendanceCalendarActivity.this);
					view3.setDataOutJ(in);
					ll_all_record.addView(view3.getView());
				}
			}else if(in.getIsExp().equals("3")||in.getIsExp().equals("9")||in.getIsExp().equals("2")){
				tv_info_title.setText(PublicUtils.getResourceString(this,R.string.others));
				ll_all_record.removeAllViews();
				if(in.getIsExp().equals("3")){
					tv_info_title.setText(PublicUtils.getResourceString(this,R.string.attendance_date4));
				}else if(in.getIsExp().equals("2")){
					tv_info_title.setText(PublicUtils.getResourceString(this,R.string.take_rest));
				}
				if(TextUtils.isEmpty(in.getInTime())&&TextUtils.isEmpty(in.getOutTime())&&TextUtils.isEmpty(in.getInTimeJ())&&TextUtils.isEmpty(in.getOutTimeJ())){
					if(in.getIsExp().equals("3")){
						AttendanceInfoView view = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view.setDataLeave(in);
						ll_all_record.addView(view.getView());
					}else if(in.getIsExp().equals("2")){
						AttendanceInfoView view = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view.setDateRest(in);
						ll_all_record.addView(view.getView());
					}
					
				}else{
					if(!TextUtils.isEmpty(in.getInTime())){
						AttendanceInfoView view = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view.setDataIn(in);
						ll_all_record.addView(view.getView());
					}
					if(!TextUtils.isEmpty(in.getOutTime())){
						AttendanceInfoView view1 = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view1.setDataOutEarly(in);
						ll_all_record.addView(view1.getView());
					}
					if(!TextUtils.isEmpty(in.getInTimeJ())){
						AttendanceInfoView view2 = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view2.setDataInJ(in);
						ll_all_record.addView(view2.getView());
					}
					if(!TextUtils.isEmpty(in.getOutTimeJ())){
						AttendanceInfoView view3 = new AttendanceInfoView(AttendanceCalendarActivity.this);
						view3.setDataOutJ(in);
						ll_all_record.addView(view3.getView());
					}
				}
				
			}
			
		}
	}

	private void setSelectedCell(CheckableLayout v, boolean flag) {
		LinearLayout attend_ll = (LinearLayout) v.findViewById(R.id.attend_ll);
//		ImageView imageView = (ImageView) v.findViewById(R.id.attend_imageView);
		TextView textView = (TextView) v.findViewById(R.id.attend_textView);
		TextView attend_point = (TextView) v.findViewById(R.id.attend_point);
		setSelectedCell(attend_point,textView, attend_ll, v, flag);
	}
	@SuppressLint("NewApi")
	private void setSelectedCell(TextView attend_point,TextView textView, LinearLayout imageView,
			CheckableLayout v, boolean flag) {
		if (flag) {
			imageView.setBackground(getResources().getDrawable(R.drawable.attend_calend_shape_selected));
			textView.setTextColor(this.getResources()
					.getColor(R.color.white));
		} else {
			
			imageView.setBackgroundResource(0);
			if(v.isEnabled()){
				textView.setTextColor(this.getResources()
						.getColor(R.color.black));
			}else{
				textView.setTextColor(this.getResources()
						.getColor(R.color.attend_number_date));
			}
			
		}
		v.setSelected(flag);
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
	@SuppressLint("NewApi")
	@Override
	public void onRender(CheckableLayout v, CardGridItem item) {
		LinearLayout imageView =  (LinearLayout) v.findViewById(R.id.attend_ll);
		imageView.setBackgroundResource(0);
		TextView textView = (TextView) v.findViewById(R.id.attend_textView);
		textView.setText(item.getDayOfMonth().toString());
		TextView attend_point = (TextView) v.findViewById(R.id.attend_point);
		// 上个月的文字显示灰色
		if (v.isEnabled()) {
			textView.setTextColor(this.getResources()
					.getColor(R.color.black));
			attend_point.setVisibility(View.VISIBLE);
		} else {
			textView.setTextColor(this.getResources()
					.getColor(R.color.attend_number_date));
			attend_point.setVisibility(View.INVISIBLE);
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
											DateUtil.DATAFORMAT_STR_))){
						
						v.setEnabled(false);
					}
						
					
					else{
						v.setEnabled(true);
					}
						

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
				s =s.replace("-", "/");
				if (DateUtil.dateToDateString(item.getDate().getTime(),
						DateUtil.DATAFORMAT_STR_).equals(s))
					setSelectedCell(attend_point,textView, imageView, v, true);
				String clickDate = s.replace("/", "-");
				setRefresh(clickDate);
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
		if(infosAll.size()>0){
			AttendanceInfo in = infosAll.get(item.getDayOfMonth()-1);
			if(in!=null){
				int data = item.getDayOfMonth();
				if(TextUtils.isEmpty(in.getTime())){
					attend_point.setVisibility(View.INVISIBLE);
				}else{
					String[] str = in.getTime().split("-");
					if(str.length==3){
						if(data==Integer.parseInt(str[2])){
							if(in.getIsExp().equals("0")){//正常
								attend_point.setBackground(getResources().getDrawable(R.drawable.attend_calend_shape));
							}else if(in.getIsExp().equals("1")){//迟到早退
								attend_point.setBackground(getResources().getDrawable(R.drawable.attend_calend_shape_yichang));
							}else if(in.getIsExp().equals("3")||in.getIsExp().equals("9")||in.getIsExp().equals("2")){//其他
								attend_point.setBackground(getResources().getDrawable(R.drawable.attend_calend_shape_commen));
							}
						}
					}
				}
				
			}else{
				attend_point.setVisibility(View.INVISIBLE);
			}
		}else{
			attend_point.setVisibility(View.INVISIBLE);
		}
		
		
	}
	private void setNotSelectCell(TextView textView, LinearLayout imageView,
			CheckableLayout v) {
//		imageView.setImageResource(R.drawable.calendar_cell_unchoose);
		textView.setTextColor(this.getResources()
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
		tv_actTime.setText(PublicUtils.getResourceString(this,R.string.date_comlpe1)+"： " + actTime);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_address_return:
			this.finish();
			break;
		case R.id.btn_previous:
			changeCalendarCardMonth(-1);
			break;
		case R.id.btn_next:
			changeCalendarCardMonth(1);
			break;
		
		default:
			break;
		}
	}
	private void changeCalendarCardMonth(int n) {
		mCalendarCard.getDateDisplay().add(Calendar.MONTH, n);
		mCalendarCard.setDateDisplay(mCalendarCard.getDateDisplay());
//		mCalendarCard.notifyChanges();
		
		String select = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
				.format(mCalendarCard.getDateDisplay().getTime());
		cardTitle.setText(select);
		
		String selct = new SimpleDateFormat("yyyy-MM", Locale.getDefault())
		.format(mCalendarCard.getDateDisplay().getTime());
		
		initData(selct,n);
	}

	
}
