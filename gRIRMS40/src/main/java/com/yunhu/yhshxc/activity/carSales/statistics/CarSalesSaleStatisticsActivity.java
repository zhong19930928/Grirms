package com.yunhu.yhshxc.activity.carSales.statistics;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.CarSaleSalesVolume;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.view.CarSalesSaleConditionItem;
import com.yunhu.yhshxc.comp.page.PageComp;
import com.yunhu.yhshxc.comp.page.PageCompListener;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;
import com.loopj.android.http.RequestParams;

public class CarSalesSaleStatisticsActivity extends AbsBaseActivity implements
		OnClickListener, PageCompListener {

	/**
	 * 车销_销售统计页面
	 * 
	 * @author abby.zhao
	 * @version 2015-07-01
	 */

	private Button btn_Start;// 选择开始时间按钮
	private Button btn_End;// 选择结束时间按钮
	private Button btnCarsalesSaleSearch;
	private ListView lv_sale_condition;// 下拉列表
	private OrderDetailAdapter adapter;
	private LinearLayout ll_carsales_page;

	private String startDate, endDate;
	private int size;
	private int currentPage = 1;
	private List<CarSaleSalesVolume> salesList = new ArrayList<CarSaleSalesVolume>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales_statistics);
		init();
		initWidget();
		initSearchDate();
	}

	private void init() {
		btn_Start = (Button) findViewById(R.id.btn_carsales_starttime);
		btn_End = (Button) findViewById(R.id.btn_carsales_endtime);
		btnCarsalesSaleSearch = (Button) findViewById(R.id.btn_carsales_salesearch);
		lv_sale_condition = (ListView) findViewById(R.id.lv_carsales_sales_condition2);
		ll_carsales_page = (LinearLayout) findViewById(R.id.ll_carsales_page);

		btn_Start.setOnClickListener(listner);
		btn_End.setOnClickListener(listner);
		btnCarsalesSaleSearch.setOnClickListener(listner);
	}

	/**
	 * 查询销售情况列表
	 */

	private void initWidget() {

		adapter = new OrderDetailAdapter(CarSalesSaleStatisticsActivity.this,salesList);
		lv_sale_condition.setAdapter(adapter);

	}

	private RequestParams searchParams() {
		RequestParams param = new RequestParams();
		param.put("phoneno",PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("carId",SharedPreferencesForCarSalesUtil.getInstance(CarSalesSaleStatisticsActivity.this).getCarId());
		// param.put("carId", 1);
		param.put("page", currentPage);
		param.put("fromTime", startDate.replace("-", ""));
		param.put("toTime", endDate.replace("-", ""));
		JLog.d(TAG, param.toString());
		return param;
	}

	/**
	 * 列表查询
	 */
	private Dialog loadDialog = null;

	private void getSalesSearchList() {
		loadDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,"正在加载...");
		String url = UrlInfo.carSalesInfo(this);
		RequestParams param = searchParams();

		GcgHttpClient.getInstance(this).post(url, param,
				new HttpResponseListener() {
					@Override
					public void onStart() {
						loadDialog.show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d(TAG, content + "");
						ToastOrder.makeText(CarSalesSaleStatisticsActivity.this,
								"网络异常请重试", ToastOrder.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						loadDialog.dismiss();
					}

					@Override
					public void onSuccess(int statusCode, String content) {

						JLog.d(TAG, content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								int cachrows = 0, totals = 0;
								if (obj.has("cacherows")) {// 每页的行数
									cachrows = obj.getInt("cacherows");
								}
								if (obj.has("cachetotal")) {// 总共的行数
									totals = obj.getInt("cachetotal");
								}
								JSONArray array = obj.optJSONArray("salesData");// 查询的销售情况
								if (array != null && array.length() > 0) {
									salesList = new CarSalesParse(getApplicationContext()).parserSalesListItem(array);
									// pages++;
									adapter.refresh(salesList);
								}else{
									ToastOrder.makeText(CarSalesSaleStatisticsActivity.this,"没有查询到数据", ToastOrder.LENGTH_SHORT).show();
								}
								addPage(cachrows, totals);
								adapter.notifyDataSetChanged();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(CarSalesSaleStatisticsActivity.this,"查询到数据异常", ToastOrder.LENGTH_SHORT).show();
						}

					}

				});
	}

	class OrderDetailAdapter extends BaseAdapter {
		private Context context;
		private List<CarSaleSalesVolume> list;

		public OrderDetailAdapter(Context context,List<CarSaleSalesVolume> orderList) {
			this.context = context;
			this.list = orderList;
		}

		@Override
		public int getCount() {
			if (list.size() > 0) {
				return list.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			CarSalesSaleConditionItem view = null;

			view = new CarSalesSaleConditionItem(CarSalesSaleStatisticsActivity.this, true);
			view.initData(list.get(position));

			convertView = view.getView();

			return convertView;
		}

		public void refresh(List<CarSaleSalesVolume> list3) {
			list = list3;
		}

	}

	/**
	 * 添加分页
	 * 
	 * @param cachrows
	 *            每页的行数
	 * @param totals
	 *            总行数
	 */
	private void addPage(int cachrows, int totals) {
		if (totals==0) {
			ll_carsales_page.removeAllViews();
		}else{
			if (ll_carsales_page.getChildCount() == 0) {
				PageComp comp = new PageComp(this, this);
				comp.settingPage(cachrows, totals);
				ll_carsales_page.addView(comp.getView());
				if (comp.isShowPageComp()) {
					View footView = View.inflate(this, R.layout.foot_listview, null);
					footView.setBackgroundResource(R.color.transparent);
					if (lv_sale_condition.getFooterViewsCount() == 0) {
						lv_sale_condition.addFooterView(footView);
					}
					lv_sale_condition.setAdapter(adapter);
				}
			}
		}
	}

	/**
	 * 初始化查询日期
	 */
	private void initSearchDate() {
		startDate = DateUtil.getDateByDate(DateUtil.getInternalDateByDay(
				new Date(), -1));
		endDate = DateUtil.getCurDate();
		btn_Start.setText(startDate);
		btn_End.setText(endDate);
	}

	private final int TYPE_START = 1;// 开始时间
	private final int TYPE_END = 2;// 结束时间
	private int year, month, day;
	private String currentValue = null;

	private void searchData(final int type, String value) {
		currentValue = value;
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view = null;
		view = View.inflate(this, R.layout.report_date_dialog, null);
		if (!TextUtils.isEmpty(value)) {
			String[] date = value.split("-");
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]) - 1;
			day = Integer.parseInt(date[2]);
		} else {
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
		}
		LinearLayout ll_date = (LinearLayout) view
				.findViewById(R.id.ll_compDialog);
		TimeView dateView = new TimeView(this, TimeView.TYPE_DATE,
				new TimeView.WheelTimeListener() {

					@Override
					public void onResult(String wheelTime) {
						currentValue = wheelTime;
					}
				});
		dateView.setOriDate(year, month + 1, day);
		ll_date.addView(dateView);
		Button confirmBtn = (Button) view
				.findViewById(R.id.report_dialog_confirmBtn);
		Button cancelBtn = (Button) view
				.findViewById(R.id.report_dialog_cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (type == TYPE_START) {
					startDate = currentValue;
					btn_Start.setText(startDate);
				} else {
					endDate = currentValue;
					btn_End.setText(endDate);
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

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_carsales_starttime:

				searchData(TYPE_START, startDate);
				break;
			case R.id.btn_carsales_endtime:
				searchData(TYPE_END, endDate);
				break;
			case R.id.btn_carsales_salesearch:
				if (salesList != null) {
					salesList.clear();
					currentPage = 1;
					adapter.notifyDataSetChanged();
				}
				getSalesSearchList();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 分页的时候页的选择
	 */
	@Override
	public void pageSelect(int page) {
		currentPage = page + 1;
		getSalesSearchList();
	}

}
