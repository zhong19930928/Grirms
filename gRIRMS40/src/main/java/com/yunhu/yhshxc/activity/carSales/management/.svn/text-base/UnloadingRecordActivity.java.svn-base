package com.yunhu.yhshxc.activity.carSales.management;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.RequestParams;

public class UnloadingRecordActivity extends AbsBaseActivity implements OnItemClickListener{
	private PullToRefreshListView lv_loading_record;
	private TextView tv_record_name;
	private UnLoadingRecordAdapter adapter ;
	private List<CarSales> sales = new ArrayList<CarSales>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_sales_loading_record);
		initWidget();
		initParseData();
	}
	private void initWidget() {
		lv_loading_record = (PullToRefreshListView) findViewById(R.id.lv_loading_record);
		adapter = new UnLoadingRecordAdapter(UnloadingRecordActivity.this,sales);
		lv_loading_record.setAdapter(adapter);
		lv_loading_record.setOnItemClickListener(this);
		tv_record_name = (TextView) findViewById(R.id.tv_record_name);
		tv_record_name.setText("卸车记录查看");
		String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
				   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		lv_loading_record.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_loading_record.setMode(Mode.PULL_FROM_END);
		lv_loading_record.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					refresh();
				}
			}
		});
	}
	private Dialog searchDialog = null;
	private void initParseData() {
		searchDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,"正在加载...");
		String url = UrlInfo.queryUnLoadingInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "content:"+content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode)) {
						parserSearchThread(content);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					searchHandler.sendEmptyMessage(3);
				}
			}

			@Override
			public void onStart() {
				if (searchDialog!=null && !searchDialog.isShowing()) {
					searchDialog.show();
				}
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				searchHandler.sendEmptyMessage(4);
			}
		});
	}
	int page = 1;
	private RequestParams params(){
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("carId", SharedPreferencesForCarSalesUtil.getInstance(this).getCarId());
		params.put("storeName", SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreName());
		params.put("storeId", SharedPreferencesForCarSalesUtil.getInstance(this).getStoreId());
		params.put("page", page < 1 ? 1:page);
		JLog.d(TAG, "params:"+params.toString());
		return params;
	}
	/**
	 * 解析查询到的数据
	 * @param contnet
	 */
	private void parserSearchThread(final String content){
		new Thread(){
			public void run() {
				try {
					sales = new CarSalesParse(UnloadingRecordActivity.this).parserUnLodingCarSales(content, true);
					if(sales.isEmpty()){
						searchHandler.sendEmptyMessage(2);
					}else{
						searchHandler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					searchHandler.sendEmptyMessage(3);
				}
			};
		}.start();
	}
	private Handler searchHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (searchDialog!=null && searchDialog.isShowing()) {
				searchDialog.dismiss();
			}
			switch (what) {
			case 1://解析成功
				adapter.refresh(sales);
				break;
			case 2:
				ToastOrder.makeText(UnloadingRecordActivity.this, "暂无任何记录", ToastOrder.LENGTH_LONG).show();
				break;
			case 3:
				ToastOrder.makeText(UnloadingRecordActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_LONG).show();
				break;
			case 4:
				ToastOrder.makeText(UnloadingRecordActivity.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	private void refresh() {
		page+=1;
		String url = UrlInfo.queryUnLoadingInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "content:"+content);
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode)) {
						parderRefreshThread(content);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					refreshHandler.sendEmptyMessage(2);
				}
			}
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				lv_loading_record.onRefreshComplete();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				refreshHandler.sendEmptyMessage(3);
			}
		});
	}
	/**
	 * 解析刷新数据
	 * @param content
	 */
	private void parderRefreshThread(final String content){
		new Thread(){
			public void run() {
				try {
					List<CarSales> newOrderList =new CarSalesParse(UnloadingRecordActivity.this).parserLodingCarSales(content, false);
					if(newOrderList.size()>0){
						sales.addAll(newOrderList);
						refreshHandler.sendEmptyMessage(1);
					}else{
						refreshHandler.sendEmptyMessage(4);
					}	
				} catch (Exception e) {
					refreshHandler.sendEmptyMessage(2);

				}
			};
		}.start();
	} 
	private Handler refreshHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				adapter.refresh(sales);
				break;
			case 2:
				ToastOrder.makeText(UnloadingRecordActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 3:
				ToastOrder.makeText(UnloadingRecordActivity.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 4:
				ToastOrder.makeText(UnloadingRecordActivity.this, "数据加载完毕", ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;

			default:
				break;
			}
		};
	};
	class UnLoadingRecordAdapter extends BaseAdapter{
		private Context context;
		private List<CarSales> sales;
		public UnLoadingRecordAdapter(Context context,List<CarSales> sales){
			this.context = context;
			this.sales  = sales;
		}

		@Override
		public int getCount() {
			return sales.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ULoadingRecordView view = null;
			if (position == 0) {
				view = new ULoadingRecordView(context, true);
				view.initData(null);
			} else {
				view = new ULoadingRecordView(context,false);
				view.initData(sales.get(position-1));
			}
			convertView = view.getView();
			return convertView;
		}
		public void refresh(List<CarSales> list3) {
			sales = list3;
			notifyDataSetChanged();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position == 1){
			return;
		}else{
			Intent intent = new Intent(UnloadingRecordActivity.this,UnLoadingListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("carSalesNo", sales.get(position-2).getCarSalesNo());
			bundle.putInt("isRecord", 1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
}
