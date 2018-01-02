package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.comp.page.PageComp;
import com.yunhu.yhshxc.comp.page.PageCompListener;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.view.OrderSearchItemView;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderSearchListActivity extends AbsBaseActivity implements PageCompListener{
	private final String TAG = "OrderSearchListActivity";
	private ListView mListView;
	private LinearLayout ll_order_search_list_title;
	private OrderSearchItemAdapter searchAdapter;
	private List<Order2> dataList = null;
	private LinearLayout ll_order_page;
	private int currentPage=1;
	
	/**
	 * 请求网络的参数
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_search_list_activity);
		dataList = new ArrayList<Order2>();
		ll_order_page = (LinearLayout)findViewById(R.id.ll_order_page);
		// 初始化加载对话框
		ll_order_search_list_title = (LinearLayout) findViewById(R.id.ll_order_search_list_title);
		searchAdapter = new OrderSearchItemAdapter();
		mListView = (ListView) findViewById(R.id.pull_refresh_list);
		mListView.setAdapter(searchAdapter);
		initTitleDate();
		getOrderSearchList();
	}
	
	/**
	 * 查询订单列表URL
	 */
	protected RequestParams orderSearchRequestParams(){
		return new RequestParams();
	}
	/**
	 * 列表查询
	 */
	private Dialog loadDialog = null;
	private void getOrderSearchList(){
		loadDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在加载...");
		String url = UrlInfo.queryOrderNewInfo(this,currentPage);
		GcgHttpClient.getInstance(this).post(url, orderSearchRequestParams(),new HttpResponseListener() {
			@Override
			public void onStart() {
				loadDialog.show();
			}


			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, content+"");
				ToastOrder.makeText(OrderSearchListActivity.this, "网络异常请重试", ToastOrder.LENGTH_SHORT).show();
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
						int cachrows = 0,totals = 0;
						if (obj.has("cacherows")) {//每页的行数
							cachrows = obj.getInt("cacherows");
						}
						if (obj.has("cachetotal")) {//总共的行数
							totals = obj.getInt("cachetotal");
						}
						JSONArray array = obj.getJSONArray("orderData");//查询的订单的数据
						if (array!=null && array.length()>0) {
							dataList.clear();
							Order2 order = null;
							JSONObject orderObj = null;
							for (int i = 0; i < array.length(); i++) {
								orderObj = array.getJSONObject(i);
								order = Order2Data.json2Order(orderObj.toString());
								dataList.add(order);
							}
						}
						addPage(cachrows,totals);
						searchAdapter.notifyDataSetChanged();
					}else{
						ToastOrder.makeText(OrderSearchListActivity.this, "没有查询到数据", ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(OrderSearchListActivity.this, "没有查询到数据", ToastOrder.LENGTH_SHORT).show();
				}
							
			}

		});
	}
	
	/**
	 * 添加分页
	 * @param cachrows 每页的行数
	 * @param totals 总行数
	 */
	private void addPage(int cachrows,int totals){
		if (ll_order_page.getChildCount()==0) {
			PageComp comp = new PageComp(this, this);
			comp.settingPage(cachrows, totals);
			ll_order_page.addView(comp.getView());
			if (comp.isShowPageComp()) {
				View footView = View.inflate(this, R.layout.foot_listview, null);
				footView.setBackgroundResource(R.color.transparent);
				if (mListView.getFooterViewsCount() == 0) {
					mListView.addFooterView(footView);
				}
				mListView.setAdapter(searchAdapter);
			}
		}
	}

	/**
	 * 初始化查询列表标题
	 */
	private void initTitleDate() {
		OrderSearchItemView titleView = new OrderSearchItemView(this);
		String[] title = new String[] { "客户名称", "订单日期", "订单状态", "金额", "订单编号","操作" };
		titleView.initTitleData(title);
		View c = titleView.getView();
		c.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll_order_search_list_title.addView(c);
	}

	private class OrderSearchItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			OrderSearchItemView itemView = new OrderSearchItemView(OrderSearchListActivity.this);
//			String[] data = new String[] { "客户名称" + position,"订单日期" + position, String.valueOf(300 + position),"订单编号" + position, "订单状态" + position, "查看" };
			Order2 order = dataList.get(position);
			String[] data = new String[] { order.getStoreName(),order.getOrderDate(), order.getStatus(), PublicUtils.formatDouble(Double.parseDouble(TextUtils.isEmpty(order.getAmount())?"0":order.getAmount())),order.getOrderNo(), "查看" };
			itemView.initContentData(data);
			itemView.setData(order);
			convertView = itemView.getView();
			return convertView;
		}

	}

	/**
	 * 分页的时候页的选择
	 */
	@Override
	public void pageSelect(int page) {
		currentPage = page+1;
		getOrderSearchList();
	}
	
}
