package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.OrderCacheDB;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.OrderCache;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.view.OrderProductItem;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class CreateOrderActivity extends Activity implements DataSource {
	private final String TAG="CreateOrderActivity";
	private LinearLayout ll_create_order_sure;// 确定下单
	private LinearLayout ll_create_order_more;// 继续创建订单
	private LinearLayout ll_create_order_print;// 打印
	private TextView tv_order_create_number;//订单编号
	private TextView tv_order_create_total_price;//订单总价
	private LinearLayout ll_order_create;//历史产品列表
	private TextView tv_order_create_store_name;//店面名称
	private List<OrderCache> orderCatchList=null;
	private Dialog loadDialog=null;
	private String storeId=null;
	private String orderNumber=null;
	private String storeName;//店面名称

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_create);
		
		ll_order_create = (LinearLayout) findViewById(R.id.ll_order_create);
		ll_create_order_sure = (LinearLayout) findViewById(R.id.ll_order_create_sure);
		ll_create_order_more = (LinearLayout) findViewById(R.id.ll_order_create_more);
		ll_create_order_print = (LinearLayout) findViewById(R.id.ll_order_create_print);
		
		tv_order_create_number=(TextView)findViewById(R.id.tv_order_create_number);
		tv_order_create_total_price=(TextView)findViewById(R.id.tv_order_create_total_price);
		tv_order_create_store_name=(TextView)findViewById(R.id.tv_order_create_store_name);
		ll_create_order_sure.setOnClickListener(listener);
		ll_create_order_more.setOnClickListener(listener);
		ll_create_order_print.setOnClickListener(listener);
		storeId=getIntent().getStringExtra("storeId");
		storeName=getIntent().getStringExtra("storeName");
		tv_order_create_store_name.setText(storeName);
		orderNumber=OrderCalculate.getOrderNumber();
		tv_order_create_number.setText(orderNumber);

		PSSConfDB pssConfDB = new PSSConfDB(this);
		PSSConf pssConf = pssConfDB.findPSSConf();
		if (!TextUtils.isEmpty(pssConf.getOrderPrintStyle())) {
			ll_create_order_print.setVisibility(View.VISIBLE);
		}
		
//		new GetHistoryOrder().execute();
		getHistoryOrder();
	}

	
	@Override
	protected void onRestart() {
		super.onRestart();
		orderCatchList=new OrderCacheDB(this).findAllOrderCacheList();
		totalOrderPrice="0.0";
		initLayout();
	}
	
	/**
	 * 获取历史订单信息
	 * @author jishen
	 *
	 */
	private void getHistoryOrder(){
		String url=UrlInfo.getUrlPSSCreateOrderSearchHistory(CreateOrderActivity.this,storeId);
		JLog.d(TAG, "获取历史订单URL："+url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				showloadDialog();
			}
			
			
			@Override
			public void onFailure(Throwable error, String content) {
				if(loadDialog!=null && loadDialog.isShowing()){
					loadDialog.dismiss();
				}
				JLog.d(TAG, "result:"+content);
				ToastOrder.makeText(CreateOrderActivity.this, "网络异常加载失败！", ToastOrder.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				if(loadDialog!=null && loadDialog.isShowing()){
					loadDialog.dismiss();
				}
				JLog.d(TAG, "result:"+content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
						new OrderCacheDB(CreateOrderActivity.this).removeAllData();//插入前先清除数据
						new ParsePSS(CreateOrderActivity.this).getOrderCacheList(content);//解析
						orderCatchList=new OrderCacheDB(CreateOrderActivity.this).findAllOrderCacheList();
						initLayout();
					}else{//失败
						ToastOrder.makeText(CreateOrderActivity.this, "网络异常加载失败！", ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {//数据异常，失败
					JLog.d(TAG, "异常："+e.getMessage());
					ToastOrder.makeText(CreateOrderActivity.this, "网络异常加载失败！", ToastOrder.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
//	/**
//	 * 获取历史订单信息
//	 * @author jishen
//	 *
//	 */
//	private class GetHistoryOrder extends AsyncTask{
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			showloadDialog();
//		}
//		@Override
//		protected Object doInBackground(Object... params) {
//			String url=UrlInfo.getUrlPSSCreateOrderSearchHistory(CreateOrderActivity.this,storeId);
//			JLog.d(TAG, "获取历史订单URL："+url);
//			String result=new HttpHelper(CreateOrderActivity.this,HttpHelper.TIMEOUT_SHORT).connectGet(url);
//			JLog.d(TAG, "result:"+result);
//			try {
//				if(TextUtils.isEmpty(result)){//返回结果是空 获取失败
//					Message msg=loadHander.obtainMessage(2);
//					loadHander.sendMessage(msg);
//				}else{
//					JSONObject jsonObject = new JSONObject(result);
//					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
//					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
//						new OrderCacheDB(CreateOrderActivity.this).removeAllData();//插入前先清除数据
//						new ParsePSS(CreateOrderActivity.this).getOrderCacheList(result);//解析
//						orderCatchList=new OrderCacheDB(CreateOrderActivity.this).findAllOrderCacheList();
//						Message msg=loadHander.obtainMessage(1);
//						loadHander.sendMessage(msg);
//					}else{//失败
//						Message msg=loadHander.obtainMessage(2);
//						loadHander.sendMessage(msg);
//					}
//				}
//			} catch (Exception e) {//数据异常，失败
//				JLog.d(TAG, "异常："+e.getMessage());
//				Message msg=loadHander.obtainMessage(2);
//				loadHander.sendMessage(msg);
//			}
//			return result;
//		}
//	}
//	
//	/**
//	 * 加载数据Hander
//	 */
//	private Handler loadHander=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			super.handleMessage(msg);
//			if(loadDialog!=null && loadDialog.isShowing()){
//				loadDialog.dismiss();
//			}
//			int what=msg.what;
//			switch (what) {
//			case 1://加载成功
//				initLayout();
//				break;
//			case 2://加载失败
//				Toast.makeText(CreateOrderActivity.this, "网络异常加载失败！", Toast.LENGTH_SHORT).show();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
	
	/**
	 * 初始化页面
	 */
	private String totalOrderPrice="0.0";//订单总价
	private void initLayout(){
		if(ll_order_create.getChildCount()>0){
			ll_order_create.removeAllViews();
		}
		
		if(orderCatchList!=null && !orderCatchList.isEmpty()){
			for (int i = 0; i < orderCatchList.size(); i++) {
				final OrderCache orderCatch=orderCatchList.get(i);
				orderCatch.setOrderNumber(orderNumber);
				OrderProductItem orderProductItem=new OrderProductItem(this, orderCatch);
				orderProductItem.setStoreInfo(storeId,storeName);
				View view=orderProductItem.getView();
				view.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						deleteDialog("是否确定删除该产品？",(OrderCache)v.getTag(),v).show();
						return false;
					}
				});
				
				ll_order_create.addView(view);
				totalOrderPrice=OrderCalculate.computeAdd(totalOrderPrice, orderCatch.getTotalPrice());
			}
			new OrderCacheDB(this).updateOrderCacheOrderNumber(orderNumber);
		}
		tv_order_create_total_price.setText(String.valueOf(totalOrderPrice));//设置总价的值
	}
	
	/**
	 * 
	 * @param bContent 询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog deleteDialog(String bContent,final OrderCache orderCache,final View currentView) {
		final Dialog deleteDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.delelte_dialog, null);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		TextView tv=(TextView)view.findViewById(R.id.tv_back_content);
		tv.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				if(currentView!=null){
					new OrderCacheDB(CreateOrderActivity.this).deleteOrderCatchById(orderCache.getId()+"");
					totalOrderPrice=OrderCalculate.computeSubduct(totalOrderPrice, orderCache.getTotalPrice());
					tv_order_create_total_price.setText(String.valueOf(totalOrderPrice));
					ll_order_create.removeView(currentView);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.setContentView(view);
		deleteDialog.setCancelable(false);
		return deleteDialog;
			
	}
	
	/**
	 * 单击监听
	 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order_create_sure:
				placeAnOrderDialog("是否确定提交该订单？").show();
				break;
			case R.id.ll_order_create_more:
				orderCreateMore();
				break;
				
			case R.id.ll_order_create_print:
				print();
				break;

			default:
				break;
			}
		}
	};
	
	private void print() {
		PSSConfDB pssConfDB = new PSSConfDB(this);
		PSSConf pssConf = pssConfDB.findPSSConf();
		try {
			MenuOrderActivity.print(this, new JSONArray(pssConf.getOrderPrintStyle()), this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printLoop(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		for (OrderCache row : orderCatchList) {
			for (Element e : columns) {
				if (e.getType() == Element.TYPE_TEXT) {
					switch (e.getVariable()) {
						case 1://公司
							String tmp = prefs.getCompanyName();
							e.setContent(tmp);
							break;
							
						case 2://用户
							tmp = prefs.getUserName();
							e.setContent(tmp);
							break;
							
						case 3://订单号
							e.setContent(orderNumber);
							break;
						
						case 4://产品
							e.setContent(row.getOrderProductName());
							break;
							
						case 5://数量
							e.setContent(String.valueOf(row.getOrderQuantity()));
							break;
							
						case 6://单价
							e.setContent(String.valueOf(row.getUnitPrice()));
							break;
							
						case 7://合计
							e.setContent(OrderCalculate.computeMultiply(String.valueOf(row.getOrderQuantity()), String.valueOf(row.getUnitPrice())));
							break;
							
						case 8://总计
							e.setContent(totalOrderPrice);
							break;
							
						case 9://订单时间
							e.setContent(DateUtil.getDate());
							break;
							
						case 11://状态
							e.setContent("未生效");
							break;
							
						case 12://配货完成
							e.setContent("0%");
							break;
							
						case 13://店面
							e.setContent(storeName);
							break;
							
						case 14://电话
							e.setContent(PublicUtils.receivePhoneNO(getApplicationContext()));
							break;
					}
				}
				MenuOrderActivity.printElement(e);
			}
			MenuOrderActivity.printNewLine();
		}
	}


	@Override
	public void printRow(List<Element> columns) {
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(this);
		
		for (Element e : columns) {
			if (e.getType() == Element.TYPE_TEXT) {
				switch (e.getVariable()) {
					case 1://公司
						String tmp = prefs.getCompanyName();
						e.setContent(tmp);
						break;
						
					case 2://用户
						tmp = prefs.getUserName();
						e.setContent(tmp);
						break;
						
					case 3://订单号
						e.setContent(orderNumber);
						break;
						
					case 8://总计
						e.setContent(totalOrderPrice);
						break;
						
					case 9://订单时间
						e.setContent(DateUtil.getDate());
						break;
						
					case 13://店面
						e.setContent(storeName);
						break;
						
					case 14://电话
						e.setContent(PublicUtils.receivePhoneNO(getApplicationContext()));
						break;
				}
			}
			MenuOrderActivity.printElement(e);
		}
	}
	
	/**
	 * 
	 * @param bContent 询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog placeAnOrderDialog(String bContent) {
		final Dialog deleteDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.delelte_dialog, null);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		TextView tv=(TextView)view.findViewById(R.id.tv_back_content);
		tv.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				placeAnOrder2();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.setContentView(view);
		deleteDialog.setCancelable(false);
		return deleteDialog;
			
	}
	
	
	/**
	 * 继续创建订单
	 */
	private void orderCreateMore(){
		Intent intent=new Intent(this, CreateOrderDetailActivity.class);
		Bundle bundle=new Bundle();
		bundle.putBoolean("isCreateOrder", true);//创建新产品订单
		bundle.putString("storeId", storeId);//店面ID
		bundle.putString("storeName", storeName);//店面名称
		bundle.putString("orderNumber", orderNumber);//订单编号
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}
	
	/**
	 * 下单
	 */
	private void placeAnOrder2(){
		showloadDialog();
		//查询产品
		List<OrderCache> list=new OrderCacheDB(CreateOrderActivity.this).findAllOrderCacheList();
		if(list.isEmpty()){//没有产品
			if(loadDialog!=null && loadDialog.isShowing()){
				loadDialog.dismiss();
			}
			ToastOrder.makeText(CreateOrderActivity.this, "没有产品信息", ToastOrder.LENGTH_SHORT).show();
		}else{
			try {
				String json=toJason(list);
				if(!TextUtils.isEmpty(json)){
					String url=UrlInfo.UrlPSSOrderSave(CreateOrderActivity.this);
					RequestParams params=new RequestParams();
					params.put("orderid", orderNumber);
					params.put("storeid", storeId);
					params.put("proinfo", json);
					JLog.d(TAG, "下单URL："+url);
					JLog.d(TAG, "orderid："+orderNumber);
					JLog.d(TAG, "storeid："+storeId);
					GcgHttpClient.getInstance(this).post(url, params, new HttpResponseListener(){
						@Override
						public void onStart() {
						}
						
						@Override
						public void onFailure(Throwable error, String content) {
							if(loadDialog!=null && loadDialog.isShowing()){
								loadDialog.dismiss();
							}
							ToastOrder.makeText(CreateOrderActivity.this, "下单失败", ToastOrder.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFinish() {
							
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							if(loadDialog!=null && loadDialog.isShowing()){
								loadDialog.dismiss();
							}
							try {
								JSONObject jsonObject = new JSONObject(content);
								String resultcode = jsonObject.getString(Constants.RESULT_CODE);
								if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
									ToastOrder.makeText(CreateOrderActivity.this, "下单成功", ToastOrder.LENGTH_SHORT).show();
									new OrderCacheDB(CreateOrderActivity.this).removeAllData();//下单成功后清除所有产品信息
									CreateOrderActivity.this.finish();
								}else{
									ToastOrder.makeText(CreateOrderActivity.this, "下单失败", ToastOrder.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								JLog.d(TAG, e.getMessage());
								ToastOrder.makeText(CreateOrderActivity.this, "下单失败", ToastOrder.LENGTH_SHORT).show();
							}
							
						}
					});
					
				}else{//数据有误
					if(loadDialog!=null && loadDialog.isShowing()){
						loadDialog.dismiss();
					}
					ToastOrder.makeText(CreateOrderActivity.this, "下单失败", ToastOrder.LENGTH_SHORT).show();
				}
			
			} catch (Exception e) {
				if(loadDialog!=null && loadDialog.isShowing()){
					loadDialog.dismiss();
				}
				JLog.d(TAG, e.getMessage());
				ToastOrder.makeText(CreateOrderActivity.this, "下单失败", ToastOrder.LENGTH_SHORT).show();
			}
		}
	}
	
	
//	/**
//	 * 下单
//	 */
//	private void placeAnOrder(){
//		showloadDialog();
//		new Thread(){
//			public void run() {
//				try {
//					List<OrderCache> list=new OrderCacheDB(CreateOrderActivity.this).findAllOrderCacheList();
//					if(list.isEmpty()){//没有产品
//						Message msg=placeAnOrderHandler.obtainMessage(1);
//						placeAnOrderHandler.sendMessage(msg);
//					}else{
//						String json=toJason(list);
//						if(!TextUtils.isEmpty(json)){
//							String url=UrlInfo.UrlPSSOrderSave(CreateOrderActivity.this);
//							HashMap<String, String> params=new HashMap<String, String>();
//							params.put("orderid", orderNumber);
//							params.put("storeid", storeId);
//							params.put("proinfo", json);
//							JLog.d(TAG, "下单URL："+url);
//							JLog.d(TAG, "orderid："+orderNumber);
//							JLog.d(TAG, "storeid："+storeId);
//							String result=new HttpHelper(CreateOrderActivity.this,HttpHelper.TIMEOUT_SHORT).connectPost(url, params);
//							if(TextUtils.isEmpty(result)){//下单失败
//								Message msg=placeAnOrderHandler.obtainMessage(2);
//								placeAnOrderHandler.sendMessage(msg);
//							}else{
//								JSONObject jsonObject = new JSONObject(result);
//								String resultcode = jsonObject.getString(Constants.RESULT_CODE);
//								if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
//									Message msg=placeAnOrderHandler.obtainMessage(3);
//									placeAnOrderHandler.sendMessage(msg);
//									new OrderCacheDB(CreateOrderActivity.this).removeAllData();//下单成功后清除所有产品信息
//								}else{
//									Message msg=placeAnOrderHandler.obtainMessage(2);
//									placeAnOrderHandler.sendMessage(msg);
//								}
//							}
//						}else{//数据有误
//							Message msg=placeAnOrderHandler.obtainMessage(2);
//							placeAnOrderHandler.sendMessage(msg);
//						}
//					}
//				} catch (Exception e) {
//					JLog.d(TAG, "异常："+e.getMessage());
//					Message msg=placeAnOrderHandler.obtainMessage(2);
//					placeAnOrderHandler.sendMessage(msg);
//				}
//				
//			};
//		}.start();
//	}
//	
//	/**
//	 * 下单handle
//	 */
//	private Handler placeAnOrderHandler=new Handler(){
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			int what=msg.what;
//			if(loadDialog!=null && loadDialog.isShowing()){
//				loadDialog.dismiss();
//			}
//			switch (what) {
//			case 1://没有产品订单
//				Toast.makeText(CreateOrderActivity.this, "没有产品信息", Toast.LENGTH_SHORT).show();
//				break;
//			case 2://下单失败
//				Toast.makeText(CreateOrderActivity.this, "下单失败", Toast.LENGTH_SHORT).show();
//				break;
//			case 3://下单成功
//				Toast.makeText(CreateOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
//				CreateOrderActivity.this.finish();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
	
	/**
	 * 将表格中的控件选择的值转化为json数据
	 * @param tableList
	 * @return
	 * @throws JSONException 
	 */
	private String toJason(List<OrderCache> list) throws JSONException {
		String json = null;
		JSONArray jsonArray = new JSONArray();
		if (list != null) {
			JSONObject jsonObject = null;
			for (int i = 0; i < list.size(); i++) {
				OrderCache orderCatch = list.get(i);
				jsonObject = new JSONObject();
				jsonObject.put("proid", orderCatch.getOrderProductId());// 产品ID
				jsonObject.put("productname", orderCatch.getOrderProductName());// 产品名称
				jsonObject.put("orderprice", orderCatch.getUnitPrice());// 产品单价
				jsonObject.put("ordercount", orderCatch.getOrderQuantity());// 订货数量
				jsonArray.put(jsonObject);
			}
			json = jsonArray.toString();
			JLog.d(TAG, "json=>" + json);
		}

		return json;
	}
	
	/**
	 * 显示加载dialog
	 */
	private void showloadDialog(){
		if(loadDialog!=null && loadDialog.isShowing()){
			loadDialog.dismiss();
			loadDialog=null;
		}
		loadDialog=new MyProgressDialog(this,R.style.CustomProgressDialog,getResources().getString(R.string.initTable));
		loadDialog.show();
	}


	@Override
	public void printPromotion(List<Element> columns) {
		
	}


	@Override
	public void printZhekou(List<Element> columns) {
		
	}


	@Override
	public void printDingdanbianhao(List<Element> columns) {
		
	}

}
