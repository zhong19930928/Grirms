package com.yunhu.yhshxc.activity.carSales.statistics;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesDebtListItem;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.view.CarSalesDebtSearchResultItem;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

public class CarSalesDebtStatisticsActivity extends AbsBaseActivity {

	/**
	 * 欠款统计表
	 * 
	 * @author abby.zhao
	 */

	private PullToRefreshListView debtStatisticsListView;
	private List<CarSalesDebtListItem> debtListItemList = new ArrayList<CarSalesDebtListItem>();
	private DebtResultAdapter debtResultAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debt_statistics);
		init();
	}

	private void init() {
		debtStatisticsListView = (PullToRefreshListView) findViewById(R.id.pull_debt_statistics_list);

		debtResultAdapter = new DebtResultAdapter();
		debtStatisticsListView.setAdapter(debtResultAdapter);

		search();
		String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
				   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		debtStatisticsListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		debtStatisticsListView.setMode(Mode.PULL_FROM_END);
		debtStatisticsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					search();
				}
			}
		});
	}

	public int pages = 1;// 查询列表页数

	private RequestParams searchParams() {

		RequestParams param = new RequestParams();
		param.put("phoneno",PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("carId",	SharedPreferencesForCarSalesUtil.getInstance(CarSalesDebtStatisticsActivity.this).getCarId());
//		param.put("carId", 1);
		param.put("page", pages);
		param.put("storeId", SharedPreferencesForCarSalesUtil.getInstance(CarSalesDebtStatisticsActivity.this).getStoreId());
		param.put("storeName", SharedPreferencesForCarSalesUtil.getInstance(CarSalesDebtStatisticsActivity.this).getCarSalesStoreName());
		JLog.d(TAG, param.toString());
		return param;
	}

	private Dialog searchDialog;
	private void search() {
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,"正在加载...");
		String url = UrlInfo.carBalanceInfo(this);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(this.getApplicationContext()).post(url,
				param, new HttpResponseListener() {
					@Override
					public void onStart() {
						if (pages == 1) {
							searchDialog.show();
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d(TAG, "onSuccess:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultCode = obj.getString("resultcode");
							if ("0000".equals(resultCode)) {
								String arrayStr = "";
								if (PublicUtils.isValid(obj, "balanceData")) {
									arrayStr = obj.getString("balanceData");
								}
								if (TextUtils.isEmpty(arrayStr)) {
									ToastOrder.makeText(getApplicationContext(),"数据加载完毕", ToastOrder.LENGTH_SHORT).show();
								} else {
									
									JSONArray array = obj.optJSONArray("balanceData");
									List<CarSalesDebtListItem> newListItem = new CarSalesParse(
											getApplicationContext())
											.parserDebtListItem(array);
									if (pages == 1) {// 说明是刷新
										debtListItemList.clear();
									}
									debtListItemList.addAll(newListItem);
									debtResultAdapter.notifyDataSetChanged();
									pages++;
								}
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA,ToastOrder.LENGTH_SHORT).show();
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
						debtStatisticsListView.onRefreshComplete();
					}

				});
	}

	private class DebtResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return debtListItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return debtListItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CarSalesDebtSearchResultItem carSalesDebtSearchResultItem = null;
			CarSalesDebtListItem item = debtListItemList.get(position);
			if (convertView == null) {
				carSalesDebtSearchResultItem = new CarSalesDebtSearchResultItem(
						CarSalesDebtStatisticsActivity.this , item);
				convertView = carSalesDebtSearchResultItem.getView();
				convertView.setTag(carSalesDebtSearchResultItem);
			} else {
				carSalesDebtSearchResultItem = (CarSalesDebtSearchResultItem) convertView
						.getTag();
			}
			carSalesDebtSearchResultItem.setData(item);
			return convertView;
		}

	}

}
