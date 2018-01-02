package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.Inventory;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 库存管理列表
 * 
 * @author jishen
 * 
 */
public class OrderStockList extends Activity {
	private final String TAG = "OrderStockList";
	private TextView tv_order_stock_store_name;// 店面名称
	private LinearLayout ll_order_stock_refalsh;// 刷新
	private LinearLayout ll_order_stock_add;// 添加
	private ListView order_stock_listView;// 列表
	private String storeId;// 店面ID
	private String storeName;// 店面名称
	private OrderStockListAdapter orderStockListAdapter;
	private Dialog loadDialog = null;//加载dialog
	private List<Inventory> inventoryList=null;//产品库存列表
	private boolean isNeedRefalsh=true;//是否需要刷新 true需要 false 不需要刷新
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_stock_list);
		inventoryList=new ArrayList<Inventory>();
		loadDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,getResources().getString(R.string.initTable));
		tv_order_stock_store_name = (TextView) findViewById(R.id.tv_order_stock_store_name);
		order_stock_listView = (ListView) findViewById(R.id.order_stock_listView);
		ll_order_stock_refalsh = (LinearLayout) findViewById(R.id.ll_order_stock_refalsh);
		ll_order_stock_add = (LinearLayout) findViewById(R.id.ll_order_stock_add);
		storeId = getIntent().getStringExtra("storeId");
		storeName = getIntent().getStringExtra("storeName");
		tv_order_stock_store_name.setText(storeName);
		ll_order_stock_refalsh.setOnClickListener(onClickListener);
		ll_order_stock_add.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order_stock_refalsh:
				refalsh();
				break;
			case R.id.ll_order_stock_add:
				add();
				break;

			default:
				break;
			}
		}
	};

	
	@Override
	protected void onStart() {
		super.onStart();
		JLog.d(TAG, "onStart:isNeedRefalsh:"+isNeedRefalsh);
		if(isNeedRefalsh){
			refalsh();
		}
	}
	
	/**
	 * 刷新
	 */
	private void refalsh() {
//		new GetOrderStock().execute();
		getOrderStock();
	}
	
	/**
	 * 添加
	 */
	private void add(){
		Intent intent=new Intent(this,OrderStockUpdate.class);
		intent.putExtra("storeName", storeName);//店面名称
		intent.putExtra("storeId", storeId);//店面Id
		intent.putExtra("addProduct", true);//产品名称
		startActivityForResult(intent,R.id.order_stock_submit_return);
	}
	
	/**
	 * 获取库存列表信息
	 * 
	 * @author jishen
	 * 
	 */
	private void getOrderStock(){
		String url = UrlInfo.UrlPSSInventory(OrderStockList.this, storeId);
		JLog.d(TAG, "获取库存列表URL：" + url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				loadDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "获取库存列表result：" + content);
				if(loadDialog.isShowing()){//关闭加载对话框
					loadDialog.dismiss();
				}
				ToastOrder.makeText(OrderStockList.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				if(loadDialog.isShowing()){//关闭加载对话框
					loadDialog.dismiss();
				}
				JLog.d(TAG, "获取库存列表result：" + content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
						inventoryList=new ParsePSS(OrderStockList.this).getInventoryList(content);
						orderStockListAdapter=new OrderStockListAdapter(OrderStockList.this, inventoryList);
						orderStockListAdapter.setStoreInfo(storeId, storeName);
						order_stock_listView.setAdapter(orderStockListAdapter);
					}else{//失败
						ToastOrder.makeText(OrderStockList.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
					}
				
				} catch (Exception e) {//数据异常，失败
					JLog.d(TAG, "异常："+e.getMessage());
					ToastOrder.makeText(OrderStockList.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
				}				
			}
			
		});
	}

//	/**
//	 * 获取库存列表信息
//	 * 
//	 * @author jishen
//	 * 
//	 */
//	private class GetOrderStock extends AsyncTask {
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			loadDialog.show();
//		}
//
//		@Override
//		protected Object doInBackground(Object... params) {
//			String url = UrlInfo.UrlPSSInventory(OrderStockList.this, storeId);
//			JLog.d(TAG, "获取库存列表URL：" + url);
//			String result = new HttpHelper(OrderStockList.this,HttpHelper.TIMEOUT_SHORT).connectGet(url);
//			JLog.d(TAG, "获取库存列表result：" + result);
//			try {
//				if(TextUtils.isEmpty(result)){//返回结果是空 获取失败
//					Message msg=getOrderStockHandler.obtainMessage(2);
//					getOrderStockHandler.sendMessage(msg);
//				}else{
//					JSONObject jsonObject = new JSONObject(result);
//					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
//					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
//						inventoryList=new ParsePSS(OrderStockList.this).getInventoryList(result);
//						Message msg=getOrderStockHandler.obtainMessage(1);
//						getOrderStockHandler.sendMessage(msg);
//					}else{//失败
//						Message msg=getOrderStockHandler.obtainMessage(2);
//						getOrderStockHandler.sendMessage(msg);
//					}
//				}
//			} catch (Exception e) {//数据异常，失败
//				JLog.d(TAG, "异常："+e.getMessage());
//				Message msg=getOrderStockHandler.obtainMessage(2);
//				getOrderStockHandler.sendMessage(msg);
//			}
//
//			return result;
//		}
//
//	}
//
//	/**
//	 * 获取库存列表信息handler
//	 * 
//	 * @author jishen
//	 * 
//	 */
//	private Handler getOrderStockHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if(loadDialog.isShowing()){//关闭加载对话框
//				loadDialog.dismiss();
//			}
//			int what=msg.what;
//			switch (what) {
//			case 1://加载成功
//				orderStockListAdapter=new OrderStockListAdapter(OrderStockList.this, inventoryList);
//				orderStockListAdapter.setStoreInfo(storeId, storeName);
//				order_stock_listView.setAdapter(orderStockListAdapter);
//				break;
//			case 2://加载失败
//				Toast.makeText(OrderStockList.this, R.string.reload_failure, Toast.LENGTH_SHORT).show();
//				break;
//			}
//			super.handleMessage(msg);
//		};
//	};
	
	/**
	 * 返回
	 */
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
		if(resultCode==R.id.order_stock_submit_return){
			isNeedRefalsh=true;//提交后返回
		}else{
			isNeedRefalsh=false;//回退按钮返回
		}
	};
}
