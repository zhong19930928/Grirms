package com.yunhu.yhshxc.attendance;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
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

public class SearchSchedulingActivity extends AbsBaseActivity implements
		OnItemClickListener {

	/**
	 * 考勤查询模块
	 */

	private PullToRefreshListView scheduling_list;
	private List<Scheduling> schedulingList = new ArrayList<Scheduling>();
	private SearchResultAdapter searchResultAdapter;
	private List<Scheduling> newListItem = new ArrayList<Scheduling>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_search_scheduling);
		init();
	}

	private void init() {
		scheduling_list = (PullToRefreshListView) findViewById(R.id.scheduling_list);

		searchResultAdapter = new SearchResultAdapter();
		scheduling_list.setAdapter(searchResultAdapter);
		search();

		String label = DateUtils.formatDateTime(this.getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		scheduling_list.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		scheduling_list.setMode(Mode.PULL_FROM_END);
		scheduling_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					search();
				}
			}
		});

		scheduling_list.setOnItemClickListener(this);
	}

	public int pages = 1;

	private RequestParams searchParams() {

		RequestParams param = new RequestParams();
		param.put("phoneno",
				PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("page", pages);
		return param;
	}

	private Dialog searchDialog;

	private void search() {
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				PublicUtils.getResourceString(this,R.string.init));
		String url = UrlInfo.attendPaiInfo(this);
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
								if (PublicUtils.isValid(obj, "data")) {
									arrayStr = obj.getString("data");
								}
								if ("[]".endsWith(arrayStr)) {
									ToastOrder.makeText(getApplicationContext(),
											R.string.un_search_data4, ToastOrder.LENGTH_SHORT)
											.show();
								} else {

									JSONArray array = obj.optJSONArray("data");
									newListItem.clear();
									newListItem = new CarSalesParse(
											getApplicationContext())
											.parserSearchListItem(array);

									if (pages == 1) {// 说明是刷新
										schedulingList.clear();
									}
									schedulingList.addAll(newListItem);
									searchResultAdapter.notifyDataSetChanged();
									pages++;
								}
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastOrder.makeText(getApplicationContext(), R.string.ERROR_DATA,
									ToastOrder.LENGTH_SHORT).show();
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
						scheduling_list.onRefreshComplete();
					}

				});
	}

	private class SearchResultAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return schedulingList.size();
		}

		@Override
		public Object getItem(int position) {
			return schedulingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SchedulingSearchResultItem schedulingSearchResultItem = null;
			Scheduling item = schedulingList.get(position);
			
			if (convertView == null) {
				schedulingSearchResultItem = new SchedulingSearchResultItem(
						SearchSchedulingActivity.this);
				convertView = schedulingSearchResultItem.getView();
				convertView.setTag(schedulingSearchResultItem);
			} else {
				schedulingSearchResultItem = (SchedulingSearchResultItem) convertView
						.getTag();
			}
			schedulingSearchResultItem.setData(item);
			
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.putExtra("name", schedulingList.get(position - 1).getName());
		intent.putExtra("date", schedulingList.get(position - 1).getStartTime());
		intent.putExtra("array", schedulingList.get(position - 1).getArray()
				.toString());
		intent.setClass(this, SchedulingActivity.class);
		startActivity(intent);
		finish();
	}

}
