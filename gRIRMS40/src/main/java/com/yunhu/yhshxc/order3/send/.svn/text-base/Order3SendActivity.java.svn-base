package com.yunhu.yhshxc.order3.send;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.db.Order3DB;
import com.yunhu.yhshxc.order3.db.Order3ProductDataDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;
import com.loopj.android.http.RequestParams;

public class Order3SendActivity extends FragmentActivity implements OnFuzzyQueryListener{
	
	private final String TAG = "Order3SendActivity";
	private Order3SendCombineListFragment combineFragment = null;
	private DropDown spinner_send;//订单客户下拉列表
	private Button btn_starttime,btn_endtime;//订单开始时间和结束时间
	private EditText et_send_search;//订单编号
	private Button btn_order3_search;//查找按钮
	private LinearLayout ll_search_tip;
	private LinearLayout ll_order3_send_search;
	private TextView tv_search_tip;
	private List<OrgStore> allStore;// 所有店面
	private String storeId;
	private String startDate,endDate;
	private LinearLayout ll_send_detail;
	private LinearLayout ll_send_detail_mark;
	private LinearLayout ll_send_order_info;
	private TextView tv_shopping_car_ll;//打印预览
	private TextView tv_xiandan_manage;//配送完
	private Order3SendMark order3SendMark;
	private LinearLayout priview,sendAll;
	private String orderNoStr = "";//订单编号
	private String khmcStr= "";//客户名称
	private String contactStr= "";//联系人资料
	private Order3ProductDataDB order3ProductDataDB;
	private TextView tv_order3_name;
	public LinearLayout order3_send_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_send);
		order3_send_title= (LinearLayout) findViewById(R.id.order3_send_title);
		order3ProductDataDB = new Order3ProductDataDB(this);
		initWidget();
		initIntentData();
		initSearchDate();
		registeSendSuccessReceiver();
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
					spinner_send.setVisibility(View.GONE);
				}else{
					initStoreData(null);
				}
			}
		}else{
			initStoreData(null);
		}
	}
	
	
	private void initWidget(){
		tv_xiandan_manage = (TextView) findViewById(R.id.tv_xiandan_manage);
		tv_shopping_car_ll = (TextView) findViewById(R.id.tv_shopping_car_ll);
		spinner_send = (DropDown) findViewById(R.id.spinner_send);
		spinner_send.setOnResultListener(resultListener);
		spinner_send.setOnFuzzyQueryListener(this);
		btn_starttime = (Button)findViewById(R.id.btn_starttime);
		btn_starttime.setOnClickListener(listener);
		btn_endtime = (Button)findViewById(R.id.btn_endtime);
		btn_endtime.setOnClickListener(listener);
		et_send_search = (EditText)findViewById(R.id.et_send_search);
		btn_order3_search = (Button)findViewById(R.id.btn_order3_search);
		btn_order3_search.setOnClickListener(listener);
		ll_search_tip = (LinearLayout)findViewById(R.id.ll_search_tip);
		ll_search_tip.setOnClickListener(listener);
		tv_search_tip = (TextView)findViewById(R.id.tv_search_tip);
		ll_send_detail = (LinearLayout)findViewById(R.id.ll_send_detail);
		ll_send_detail_mark = (LinearLayout)findViewById(R.id.ll_send_detail_mark);
		ll_send_order_info = (LinearLayout)findViewById(R.id.ll_send_order_info);
		priview = (LinearLayout)findViewById(R.id.btn_order3_send_priview);
		sendAll = (LinearLayout)findViewById(R.id.btn_order3_send_all);
		priview.setOnClickListener(listener);
		sendAll.setOnClickListener(listener);
		ll_order3_send_search = (LinearLayout)findViewById(R.id.ll_order3_send_search);
		tv_order3_name = (TextView)findViewById(R.id.tv_order3_name);
	}
	
	private void initOrderMark(){
		ll_send_detail_mark.removeAllViews();
		order3SendMark = new Order3SendMark(this);
		ll_send_detail_mark.addView(order3SendMark.getView());
	}
	
	private void initOrderInfo(List<Order3> list){
		ll_send_order_info.removeAllViews();
		if (list!=null && !list.isEmpty()) {
			StringBuffer contact = new StringBuffer();//联系人
			String adress = "";//客户地址
			StringBuffer orders = new StringBuffer();//订单号
			for (int i = 0; i < list.size(); i++) {
				Order3 order = list.get(i);
				if (TextUtils.isEmpty(khmcStr)) {
					khmcStr = order.getStoreName();
				}
				if (TextUtils.isEmpty(contact.toString())) {
					int contactId = order.getContactId();
					Order3Contact c = new Order3ContactsDB(this).findOrderContactsByContactsIdAndStoreId(contactId, order.getStoreId());
					if (c!=null) {
						String userName = c.getUserName();
						contact.append(userName).append("\n");
						String phone_1 = c.getUserPhone1();
						String phone_2 = c.getUserPhone2();
						String phone_3 = c.getUserPhone3();
						if (!TextUtils.isEmpty(phone_1)) {
							contact.append(phone_1).append("\n");
						}
						if (!TextUtils.isEmpty(phone_2)) {
							contact.append(phone_2).append("\n");
						}
						if (!TextUtils.isEmpty(phone_3)) {
							contact.append(phone_3);
						}
						adress = c.getUserAddress();
						contactStr = contact.substring(0, contact.length()-1);
					}
				}
				orders.append(order.getOrderNo()).append("	").append(order.getOrderTime()).append("\n");
			}
			orderNoStr = orders.substring(0, orders.length()-1);
			Order3SendKH sendKH = new Order3SendKH(this);
			sendKH.setKU(khmcStr);
			sendKH.setLXR(contactStr);
			sendKH.setKHDZ(adress);
			sendKH.setDD(orderNoStr);
			ll_send_order_info.addView(sendKH.getVeiw());
		}
	}
	
	/**
	 * 设置送活动总额
	 * @param amount
	 */
	private void setSendOrderAmount(double amount){
		if (order3SendMark!=null) {
			order3SendMark.setAmount(PublicUtils.formatDouble(amount));
		}
	}
	
	/**
	 *初始化查询日期
	 */
	private void initSearchDate(){
		startDate = DateUtil.getDateByDate(DateUtil.getInternalDateByDay(new Date(), -1));
		endDate = DateUtil.getCurDate();
		btn_starttime.setText(startDate);
		btn_endtime.setText(endDate);
	}
	
	/**
	 * 初始化发货产品信息
	 * @param srcList
	 */
	private Order3SendDataList order3SendDataList = null;
	private void initSendDetailList(List<List<Order3SendData>> srcList){
		if (!srcList.isEmpty()) {
			order3SendDataList = new Order3SendDataList(this);
			View view = order3SendDataList.getView();
			order3SendDataList.setSrcData(srcList);
			ll_send_detail.removeAllViews();
			ll_send_detail.addView(view);
			initOrderMark();
		}else{
			ll_send_detail.removeAllViews();
			ll_send_detail_mark.removeAllViews();
			parserSendDataHandler.sendEmptyMessage(4);
		}
	}
	
	private double sendAmount = 0;//送货订单总额
	private double amountHistory = 0;//送货历史收款
	private double unPayAmount = 0;//送货订单未付金额
	
	private Map<Integer, List<Order3SendData>> sendDataMap = new HashMap<Integer, List<Order3SendData>>();
	private void initSendData(List<Order3> combineOrderList){
		sendAmount = 0;
		amountHistory = 0;
		unPayAmount = 0;
		initOrderInfo(combineOrderList);
		sendDataMap.clear();
		List<List<Order3SendData>> srcList = new ArrayList<List<Order3SendData>>();
		Order3PromotionDB order3PromotionDB = new Order3PromotionDB(this);
		for (int i = 0; i < combineOrderList.size(); i++) {
			Order3 order = combineOrderList.get(i);
			List<Order3ProductData> dataList = order3ProductDataDB.findOrder3ProductDataByOrderNo(order.getOrderNo());
			for (int j = 0; j < dataList.size(); j++) {
				Order3ProductData data = dataList.get(j);
				if (data.getIsOrderMain() == 2) {
					Order3Promotion promotion = order3PromotionDB.findPromotionByPromotionId(data.getPromotionId());
					if (promotion!=null && (promotion.getPreType() == 1 || promotion.getPreType() == 2 ) && (promotion.getDisType() == 3 || promotion.getDisType() == 2)) {
						continue;
					}
				}
				Order3SendData send = new Order3SendData();
				send.setDataId(data.getDataId());
				send.setAmount(data.getActualAmount());
				send.setDate(order.getOrderTime());
				send.setIsPresent(data.getIsOrderMain());
				send.setOrderNmber(order.getOrderNo());
				send.setProductId(data.getProductId());
				send.setProductName(data.getProductName());
				send.setUnit(data.getProductUnit());
//				send.setSendNumber(data.getOrderCount());
				send.setUnSendNumber(data.getActualCount() - data.getSendCount());
				if (sendDataMap.containsKey(data.getProductId())) {
					List<Order3SendData> list = sendDataMap.get(data.getProductId());
					list.add(send);
				}else{
					List<Order3SendData> sendList = new ArrayList<Order3SendData>();
					sendList.add(send);
					sendDataMap.put(data.getProductId(), sendList);
				}
			}
			sendAmount = sendAmount+order.getActualAmount();
			amountHistory = amountHistory +order.getPayAmount();
			unPayAmount = unPayAmount + order.getUnPayAmount();
		}
		
		for (Map.Entry<Integer, List<Order3SendData>> entry : sendDataMap.entrySet()) {
			   srcList.add(entry.getValue());
		}
		initSendDetailList(srcList);
		setSendOrderAmount(sendAmount);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_starttime:
				searchData(TYPE_START,startDate);
				break;
			case R.id.btn_endtime:
				searchData(TYPE_END,endDate);
				break;
			case R.id.btn_order3_search:
				search();
				break;
			case R.id.ll_search_tip:
				tip();
				break;
			case R.id.btn_order3_send_priview:
				priview();
				break;
			case R.id.btn_order3_send_all:
				sendAll();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 打印预览
	 */
	private void priview(){
		if (!TextUtils.isEmpty(orderNoStr) && order3SendMark!=null) {
			Intent intent = new Intent(this, Order3SendPriviewActivity.class);
			intent.putExtra("orderNo", orderNoStr);
			intent.putExtra("khmc", khmcStr);
			intent.putExtra("contact", contactStr);
			intent.putExtra("orderAmount", order3SendMark.receiveAmount());
			intent.putExtra("sendMsg", order3SendMark.sendMessage());
			intent.putExtra("amount", PublicUtils.formatDouble(sendAmount));//送货订单总额
			intent.putExtra("amountHistory", PublicUtils.formatDouble(amountHistory));//送货历史收款
			intent.putExtra("unPayAmount", PublicUtils.formatDouble(sendAmount-amountHistory));//送货订单未付金额
			startActivity(intent);
		}else{
			ToastOrder.makeText(this, "请选择送货单", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 配送完
	 */
	private void sendAll(){
		String text = tv_xiandan_manage.getText().toString();
		if (order3SendDataList!=null) {
			if (text.contains("配送完")) {
				order3SendDataList.sendType(order3SendDataList.SETND_TYPE_ALL);
				tv_xiandan_manage.setText("重填");
			}else{
				order3SendDataList.sendType(order3SendDataList.SETND_TYPE_RESET);
				tv_xiandan_manage.setText("配送完");
			}
		}else{
			ToastOrder.makeText(this, "请选择送货单", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
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
					btn_starttime.setText(startDate);
				}else{
					endDate = currentValue;
					btn_endtime.setText(endDate);
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
	
	/**
	 * 查询
	 */
	private Dialog searchDialog = null;
	private void search(){
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
						parserSendDataThread(content);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					parserSendDataHandler.sendEmptyMessage(2);
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
				parserSendDataHandler.sendEmptyMessage(3);
			}
		});
	}
	
	/**
	 * 解析送货数据
	 * @param content
	 */
	private void parserSendDataThread(final String content){
		new Thread(){
			public void run() {
				try {
					List<Order3> orderList = new Order3Util(Order3SendActivity.this).parserOrder3(content,true);
					Message msg = parserSendDataHandler.obtainMessage();
					msg.obj = orderList;
					msg.what = 1;
					parserSendDataHandler.sendMessage(msg);
				} catch (Exception e) {
					parserSendDataHandler.sendEmptyMessage(2);
				}
			};
		}.start();
	}
	
	private Handler parserSendDataHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (searchDialog!=null && searchDialog.isShowing()) {
				searchDialog.dismiss();
			}
			switch (what) {
			case 1://解析成功
				List<Order3> orderList = (List<Order3>) msg.obj;
				if (orderList.size()>=2) {
					addCombineFragment(orderList);
					order3_send_title.setVisibility(View.GONE);
				}else if(orderList.size() == 1){
					Order3 order3 = orderList.get(0);
					order3.setIsCommbine(1);
					new Order3DB(Order3SendActivity.this).updateOrder3(order3);
					initSendData(orderList);
				}else{
					initSendData(orderList);
				}
				break;
			case 2:
				ToastOrder.makeText(Order3SendActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
				ToastOrder.makeText(Order3SendActivity.this, "订单查询失败", ToastOrder.LENGTH_SHORT).show();
				break;
			case 4:
				ToastOrder.makeText(Order3SendActivity.this, "没查询到数据", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	private RequestParams searchParams(){
		RequestParams params = new RequestParams();
		params.put("fromTime", startDate.replaceAll("-", ""));
		params.put("toTime", endDate.replaceAll("-", ""));
		params.put("storeId", storeId);
		params.put("orderNum", et_send_search.getText().toString());
		params.put("isSend", 1);
		JLog.d(TAG, "params:"+params.toString());
		return params;
	}
	
	/**
	 * 弹出和收起查询条件
	 */
	private void tip(){
		if (ll_order3_send_search.getVisibility() == View.VISIBLE) {
			ll_order3_send_search.setVisibility(View.GONE);
			tv_search_tip.setText("弹出");
		}else{
			ll_order3_send_search.setVisibility(View.VISIBLE);
			tv_search_tip.setText("收起");
		}
	}
	
	
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
		spinner_send.setSrcDictList(srcList);
		initStoreSelect(srcList);
	}

	@Override
	public void onTextChanged(CharSequence s) {
		initStoreData(s.toString());
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
					spinner_send.setSelected(dic);
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
	 * 添加订单合并列表Fragment
	 */
	private void addCombineFragment(List<Order3> list){
		removeCombineFragment();
		combineFragment = new Order3SendCombineListFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fl_order3_send, combineFragment).commit();
		combineFragment.setSrcData(list);
	}
	
	/**
	 * 移除订单合并Fragment
	 */
	public void removeCombineFragment(){
		if (combineFragment != null && combineFragment.isResumed()) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); 
			transaction.remove(combineFragment);
			transaction.commit();
			combineFragment = null;
			order3_send_title.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (combineFragment!=null) {
				removeCombineFragment();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);

	}
	
	public void initCombineFragment(List<Order3> combineOrderList){
//		List<Order3> combineOrderList = order3DB.findCombineOrder();
		initSendData(combineOrderList);
	}
	
	
	private BroadcastReceiver sendSuccessReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_ORDER3_SEND_SUCCESS.equals(intent.getAction())) {
				Order3SendActivity.this.finish();
			}
		}
		
	};
	
	private void registeSendSuccessReceiver(){
		registerReceiver(sendSuccessReceiver, new IntentFilter(Constants.BROADCAST_ORDER3_SEND_SUCCESS));
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(sendSuccessReceiver);
	}
	
	
	
}
