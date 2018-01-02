package com.yunhu.yhshxc.activity.carSales.statistics;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.view.CarSalesStockSearchResultItem;
import com.yunhu.yhshxc.comp.page.PageComp;
import com.yunhu.yhshxc.comp.page.PageCompListener;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSalesStockNewStatisticsActivity extends AbsBaseActivity
		implements PageCompListener {

	/**
	 * 车销_库存统计页面
	 * 
	 * @author abby.zhao
	 * @version 2015-07-08
	 */

	private ListView listview;
	private StockStatisticsAdapter adapter;
	private int size;
	private List<CarSalesStock> stockList = new ArrayList<CarSalesStock>();
	private LinearLayout ll_carsales_stock_new_page;
	private int currentPage = 1;
	private int cachrows = 20;
	private int totals;
	private String intent_name = "in";
	private TextView tv_stock_title_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carsales_stock_statistics_new);
		init();
		initWidget();
		getStockList(currentPage);

	}

	private void init() {

		Intent intent = getIntent();
		// 获取数据
		intent_name = intent.getStringExtra("stock");
		tv_stock_title_name = (TextView) findViewById(R.id.tv_stock_title_name);
		if (intent_name.equals("out")) {
			tv_stock_title_name.setText("缺货统计");
		}

		listview = (ListView) findViewById(R.id.lv_carsales_sales_stock_new);
		ll_carsales_stock_new_page = (LinearLayout) findViewById(R.id.ll_carsales_stock_new_page);
	}

	/**
	 * 查询库存情况列表
	 */

	private void initWidget() {

		adapter = new StockStatisticsAdapter(
				CarSalesStockNewStatisticsActivity.this, stockList);
		listview.setAdapter(adapter);

	}

	/**
	 * 列表查询
	 */

	private Dialog loadDialog = null;

	private void getStockList(int pages) {

		loadDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				"正在加载...");
		CarSalesStockDB stockDB = new CarSalesStockDB(this);

		if (intent_name.equals("in")) {
			stockList = stockDB.findCarSalesStockByPage(pages);
			totals = stockDB.count();

		} else {
			stockList = stockDB.findStockoutCarSalesStockBypages(pages);
			totals = stockDB.countStockOut();
			if(0 == stockList.size()){
				ToastOrder.makeText(CarSalesStockNewStatisticsActivity.this, "没有缺货信息！", ToastOrder.LENGTH_SHORT)
				.show();
			}

		}
		
		adapter.refresh(stockList);
		addPage(cachrows, totals);

		adapter.notifyDataSetChanged();

		// addPage(10,40);

		loadDialog.dismiss();

	}

	class StockStatisticsAdapter extends BaseAdapter {
		private Context context;
		private List<CarSalesStock> list;

		public StockStatisticsAdapter(Context context,
				List<CarSalesStock> orderList) {
			this.context = context;
			this.list = orderList;
		}

		@Override
		public int getCount() {
			if (list.size() > 0) {
				return list.size() + 1;
			} else {
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

			CarSalesStockSearchResultItem view = null;
			List<String> list_stock = new ArrayList<String>();

			if (position == 0) {
				view = new CarSalesStockSearchResultItem(
						CarSalesStockNewStatisticsActivity.this, true);
				if(intent_name.equals("in")){
					view.initData(null,true);
				}else{
					view.initData(null,false);
				}
				
			} else {
				try {

                   if (list.size()>0) {//当list的size不为0的时候
                       
   					list_stock = new CarSalesParse(getApplicationContext())
   							.parserStockListItem(list.get(position - 1));
					
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				view = new CarSalesStockSearchResultItem(
						CarSalesStockNewStatisticsActivity.this, false);
				if(intent_name.equals("in")){
					view.initData(list_stock,true);
				}else{
					view.initData(list_stock,false);
				}
			
			}

			convertView = view.getView();

			return convertView;
		}

		public void refresh(List<CarSalesStock> list3) {

			list = list3;
			notifyDataSetChanged();

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
		if (totals == 0) {
			ll_carsales_stock_new_page.removeAllViews();
		} else {
			if (ll_carsales_stock_new_page.getChildCount() == 0) {
				PageComp comp = new PageComp(this, this);
				comp.settingPage(cachrows, totals);
				ll_carsales_stock_new_page.addView(comp.getView());
				if (comp.isShowPageComp()) {
					View footView = View.inflate(this, R.layout.foot_listview,
							null);
					footView.setBackgroundResource(R.color.transparent);
					if (listview.getFooterViewsCount() == 0) {
						listview.addFooterView(footView);
					}
					listview.setAdapter(adapter);
				}
			}
		}
	}

	/**
	 * 分页的时候页的选择
	 */
	@Override
	public void pageSelect(int page) {
		currentPage = page + 1;
		getStockList(currentPage);
	}

}
