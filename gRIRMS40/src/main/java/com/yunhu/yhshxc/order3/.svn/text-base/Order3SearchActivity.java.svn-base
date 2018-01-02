package com.yunhu.yhshxc.order3;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order3.bo.Order3;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.view.Order3SearchDetailItem;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.widget.pulltorefreshview.MyListView;
import com.yunhu.yhshxc.widget.pulltorefreshview.PullToRefreshView;
import com.yunhu.yhshxc.widget.pulltorefreshview.PullToRefreshView.OnFooterRefreshListener;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;
import com.loopj.android.http.RequestParams;

public class Order3SearchActivity extends Activity implements OnItemClickListener,OnFooterRefreshListener,OnFuzzyQueryListener  {
	
	private final String TAG = "Order3SearchActivity";
	private EditText et_search;
	private DropDown spinner_search;
	// 两个日期控件
	private Button search_ordetail_btn;
	private  Button start_btn;
	private  Button end_btn;
	private PullToRefreshView pulltorefresh_order3;
	private OrderDetailAdapter adapter;
	private MyListView lv_search_ordetail;
	private String startDate,endDate;
	private String storeId;
	private List<OrgStore> allStore;// 所有店面
	private TextView tv_order3_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_search_ordetail);
		initview();
		initWidget();
		initIntentData();
		initSearchDate();
	}
	
	private void initIntentData(){
		Intent intent = getIntent();
		if (intent!=null) {
			storeId = intent.getStringExtra("storeId");
			if (TextUtils.isEmpty(storeId)) {
				initStoreData(null);
			}else{
				OrgStore store = new OrgStoreDB(this).findOrgStoreByStoreId(storeId);
				if (store!=null) {
					tv_order3_name.setVisibility(View.VISIBLE);
					tv_order3_name.setText(store.getStoreName());
					spinner_search.setVisibility(View.GONE);
				}else{
					initStoreData(null);
				}
			}
		}else{
			initStoreData(null);
		}
	}
	
	private void initWidget() {
		// 查找所有订单显示
		adapter = new OrderDetailAdapter(Order3SearchActivity.this,orderList);
		lv_search_ordetail.setAdapter(adapter);
		tv_order3_name = (TextView)findViewById(R.id.tv_order3_name);

	}
	/**
	 *初始化查询日期
	 */
	private void initSearchDate(){
		startDate = DateUtil.getDateByDate(DateUtil.getInternalDateByDay(new Date(), -1));
		endDate = DateUtil.getCurDate();
		start_btn.setText(startDate);
		end_btn.setText(endDate);
	}
	private void initview() {
		et_search = (EditText) this.findViewById(R.id.et_search);
		spinner_search = (DropDown) this.findViewById(R.id.spinner_search);
		spinner_search.setOnResultListener(resultListener);
		spinner_search.setOnFuzzyQueryListener(this);
		start_btn = (Button) this.findViewById(R.id.btn_order3_data_start);
		end_btn = (Button) this.findViewById(R.id.btn_order3_data_end);
		start_btn.setOnClickListener(listner);
		end_btn.setOnClickListener(listner);
		search_ordetail_btn = (Button) this.findViewById(R.id.search_ordetail_btn);
		search_ordetail_btn.setOnClickListener(listner);
		pulltorefresh_order3 =  (PullToRefreshView) this.findViewById(R.id.pulltorefresh_order3);
		pulltorefresh_order3.setOnFooterRefreshListener(this);
		pulltorefresh_order3.setLastUpdated(new Date().toLocaleString());
		lv_search_ordetail = (MyListView) this.findViewById(R.id.lv_search_ordetail);
		// lv_search_ordetail.setAdapter(adapter);
		lv_search_ordetail.setOnItemClickListener(this);
		
	}
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_order3_data_start:
				searchData(TYPE_START,startDate);
				break;
			case R.id.btn_order3_data_end:
				searchData(TYPE_END,endDate);
				break;
			case R.id.search_ordetail_btn:
				search();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 查询
	 */
	private Dialog searchDialog = null;
	List<Order3> orderList = new ArrayList<Order3>();
	private void search(){
		page = 1;
		searchDialog = new MyProgressDialog(this,R.style.CustomProgressDialog,"正在查询...");
		String url = UrlInfo.queryThreeOrderInfo(this);
		GcgHttpClient.getInstance(this).post(url, searchParams(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
//				JLog.d(TAG, "content:"+content);
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
	
	/**
	 * 解析查询到的数据
	 * @param contnet
	 */
	private void parserSearchThread(final String content){
		new Thread(){
			public void run() {
				try {
					orderList = new Order3Util(Order3SearchActivity.this).parserOrder3(content,true);
					if(orderList.isEmpty()){
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
				 adapter.refresh(orderList);
				break;
			case 2:
				ToastOrder.makeText(Order3SearchActivity.this, "没有查询到该订单", ToastOrder.LENGTH_LONG).show();
				break;
			case 3:
				ToastOrder.makeText(Order3SearchActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_LONG).show();
				break;
			case 4:
				ToastOrder.makeText(Order3SearchActivity.this, "订单查询失败", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	int page = 1;
	private RequestParams searchParams(){
		RequestParams params = new RequestParams();
		params.put("fromTime", startDate.replaceAll("-", ""));
		params.put("toTime", endDate.replaceAll("-", ""));
		params.put("storeId", storeId);
		params.put("orderNum", et_search.getText().toString());
		params.put("page", page < 1 ? 1:page);
		JLog.d(TAG, "params:"+params.toString());
		return params;
	}
	
	/**
	 * 设置选中的店面
	 * 
	 * @param srcList
	 *            所有店面
	 */
	private void initStoreSelect(List<Dictionary> srcList) {
		if (!TextUtils.isEmpty(storeId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (storeId.equals(dic.getDid())) {
					spinner_search.setSelected(dic);
					break;
				}
			}
		}
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				storeId = dic.getDid();
			}

		}
	};
	/**
	 * 初始化所有客户数据
	 */
	private void initStoreData(String fuzzy) {
		allStore = new OrgStoreDB(this).findAllOrgList(fuzzy);
		List<Dictionary> srcList = new ArrayList<Dictionary>();
		if (allStore != null && allStore.size() > 0) {
			OrgStore store = null;
			Dictionary dic = null;

			for (int i = 0; i < allStore.size(); i++) {
				store = allStore.get(i);
				dic = new Dictionary();
				dic.setDid(String.valueOf(store.getStoreId()));
				dic.setCtrlCol(store.getStoreName());
				dic.setNote(store.getOrgCode());
				srcList.add(dic);
			}
		}
		spinner_search.setSrcDictList(srcList);
		initStoreSelect(srcList);
	}
	
	class OrderDetailAdapter extends BaseAdapter {
		private Context context;
		private List<Order3> list; 
		public OrderDetailAdapter(Context context,List<Order3> list){
			this.context = context;
			this.list = list;
		}
		@Override
		public int getCount() {
			if(list.size()>0){
				return list.size()+1;
			}else{
				return 0;
			}
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
			
			Order3SearchDetailItem view = null;
			if (position == 0) {
				view = new Order3SearchDetailItem(Order3SearchActivity.this, true);
				view.initData(null);
			} else {
				
				view = new Order3SearchDetailItem(Order3SearchActivity.this,false);
				view.initData(list.get(position-1));
			}
			convertView = view.getView();
			
			return convertView;
		}
		public void refresh(List<Order3> list3) {
			
			list = list3;
			notifyDataSetChanged();

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position == 0){
			return;
		}else{
			Intent intent = new Intent(Order3SearchActivity.this,Order3SearDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("orderNo", orderList.get(position-1).getOrderNo());
			intent.putExtras(bundle);
			startActivity(intent);
		}
		

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		
		refresh();

	}

	private void refresh() {
		page+=1;
		String url = UrlInfo.queryThreeOrderInfo(this);
		GcgHttpClient.getInstance(this).post(url, searchParams(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
//				JLog.d(TAG, "content:"+content);
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
				pulltorefresh_order3.onFooterRefreshComplete();
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
					List<Order3> newOrderList = new Order3Util(Order3SearchActivity.this).parserOrder3(content,false);
					if(newOrderList.size()>0){
						orderList.addAll(newOrderList);
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
				adapter.notifyDataSetChanged();
				break;
			case 2:
				ToastOrder.makeText(Order3SearchActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 3:
				ToastOrder.makeText(Order3SearchActivity.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 4:
				ToastOrder.makeText(Order3SearchActivity.this, "数据加载完毕", ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;

			default:
				break;
			}
		};
	};
	
	private final int TYPE_START = 1;//开始时间
	private final int TYPE_END = 2;//结束时间
	private int year,month,day;
	private String currentValue = null;
	private void searchData(final int type,String value){
		
		currentValue = value;
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view=null;
		view=View.inflate(this, R.layout.report_date_dialog, null);
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
		TimeView dateView = new TimeView(this,TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
			
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
					start_btn.setText(startDate);
				}else{
					endDate = currentValue;
					end_btn.setText(endDate);
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
	@Override
	public void onTextChanged(CharSequence s) {
		initStoreData(s.toString());
		
	}
}
