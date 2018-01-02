package com.yunhu.yhshxc.order2;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.database.NewOrderDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order2.bo.NewOrder;
import com.yunhu.yhshxc.order2.bo.Order2;
import com.yunhu.yhshxc.order2.bo.OrderDetail;
import com.yunhu.yhshxc.order2.bo.OrderSpec;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class OrderCreateActivity extends IndicatorFragmentActivity {

	private final String TAG = "OrderCreateActivity";
    private static final int ORDER_DETAIL = 0;//订单明细
    private static final int ORDER_EXPLAIN = 1;//订单说明
    private LinearLayout ll_submit_order2;//提交按钮
    private LinearLayout ll_clean_order2;//重置按钮
    public DropDown order2_spinner_store;//客户
    public TextView tv_order_client;
    public TextView tv_order_store;
	private List<OrgStore> allStore;//所有店面
	public OrderSpec orderSpec;//订单说明
	public List<OrderDetail> orderDetailList;
	private String orderDate;
	private OrderDetailFragment orderDetailFragment;
	private OrderExplainFragment orderExplainFragment;
	private TabInfo tabInfo_1,tabInfo_2;
	public Order2 order;
	public String storeId;
	public NewOrderDB newOrderDB;
	public NewOrder newOrder;
	public String orderNumber;//订单编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll_submit_order2 = (LinearLayout)findViewById(R.id.ll_submit_order2);
        ll_submit_order2.setOnClickListener(listener);
        ll_clean_order2 = (LinearLayout)findViewById(R.id.ll_clean_order2);
        ll_clean_order2.setOnClickListener(listener);
        order2_spinner_store = (DropDown)findViewById(R.id.order2_spinner_store);
        order2_spinner_store.setOnFuzzyQueryListener(this);
        order2_spinner_store.setOnResultListener(resultListener);
        tv_order_client = (TextView)findViewById(R.id.tv_order_client);
        tv_order_store = (TextView)findViewById(R.id.tv_order_store);
        newOrderDB = new NewOrderDB(this);
        initData();
    }
    
    
    protected void initData(){
        initOrderNumberAndOrderDate();
//        initOrder();
    	initStoreData(null);
    }
    
    
    protected void initOrder(Dictionary storeDic){
    	try {
    		storeId = storeDic.getDid();
        	newOrder = newOrderDB.findNewOrderByStoreId(storeId);
        	if (newOrder!=null) {
				order = newOrder.getOrder();
				orderDetailList = order.getOrderDetailList();
				orderSpec = order.getOrderSpec();
				order.setOrderNo(orderNumber);
				order.setOrderDate(orderDate);
				newOrder.setOrder(order);
				newOrderDB.updateNewOrder(newOrder);
			}else{
				order = new Order2();
    			orderDetailList = new ArrayList<OrderDetail>();
    			orderSpec = new OrderSpec();
    			order.setOrderDetailList(orderDetailList);
    			order.setOrderSpec(orderSpec);
    			order.setStoreId(storeId);
    			order.setStoreName(storeDic.getCtrlCol());
    			order.setOrderNo(orderNumber);
				order.setOrderDate(orderDate);
    			newOrder = new NewOrder();
    			newOrder.setStoreId(storeId);
    			newOrder.setOrder(order);
    			newOrderDB.insertNewOrder(newOrder);
			}
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 初始化订单编号和订单日期
     */
    private void initOrderNumberAndOrderDate(){
    	String time = DateUtil.dateToDateString(new Date(), DateUtil.YYMMDDHHMMSSSSS);
    	int  radom = (int)(Math.random()*100);
    	orderNumber = time + radom;
    	orderDate = DateUtil.getCurDateTime();
    }

    /**
     * 添加Tab
     */
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabInfo_1 = new TabInfo(ORDER_DETAIL, "订单明细",OrderDetailFragment.class);
		tabInfo_2 = new TabInfo(ORDER_EXPLAIN, "订单说明",OrderExplainFragment.class);
		tabs.add(tabInfo_1);
		tabs.add(tabInfo_2);
		return ORDER_DETAIL;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initFragmen();
	}
	
	/**
	 * 刷新Fragment页面数据
	 */
	public void initFragmen(){
		try {
//			order = Order2Data.json2Order(SharedPreferencesForOrder2Util.getInstance(this).getOrder2());
			orderDetailFragment = (OrderDetailFragment) tabInfo_1.fragment;
			orderExplainFragment = (OrderExplainFragment) tabInfo_2.fragment;
			orderDetailFragment.initData();
			orderExplainFragment.initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_submit_order2:
				if (TextUtils.isEmpty(storeId)) {
					ToastOrder.makeText(OrderCreateActivity.this, "请选择客户", ToastOrder.LENGTH_SHORT).show();
				}else if (order.getOrderDetailList()==null || order.getOrderDetailList().isEmpty()) {
					ToastOrder.makeText(OrderCreateActivity.this, "你没有选择产品", ToastOrder.LENGTH_SHORT).show();
				}else{
					tipDialog(R.id.ll_submit_order2).show();
				}
				break;
			case R.id.ll_clean_order2:
				tipDialog(R.id.ll_clean_order2).show();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 
	 * @param type viewid
	 * @return
	 */
	private Dialog tipDialog(final int id) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		confirm.setText("确  定");
		cancel.setText("取  消");
		if (id==R.id.ll_submit_order2) {
			backContent.setText("是否确定提交订单?");
		}else{
			backContent.setText("是否确定清空订单?");
		}
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				if (id==R.id.ll_submit_order2) {
					submitOrder();
				}else{
					reset();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			/**
			 * 取消退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);// 将对话框设置为模态
		return backDialog;
	}
	
	/**
	 * 重置订单
	 */
	private void reset(){
//		SharedPreferencesForOrder2Util.getInstance(this).setOrder2(null);
		if (!TextUtils.isEmpty(storeId)) {
			newOrderDB.clearNewOrderByStoreId(storeId);
		}
		order = new Order2();
		orderDetailList = new ArrayList<OrderDetail>();
		orderSpec = new OrderSpec();
		order.setOrderDetailList(orderDetailList);
		order.setOrderSpec(orderSpec);
		order.setStoreId(storeId);
		order.setOrderNo(orderNumber);
		order.setOrderDate(orderDate);
		newOrder = new NewOrder();
		newOrder.setStoreId(storeId);
		newOrder.setOrder(order);
		storeId = null;
		order2_spinner_store.resetChoose();
		initFragmen();
		
	}
	
	protected int getMainViewResId() {
	    return R.layout.order_main_activity;
	}
	
	/**
	 * 提交订单
	 */
	private Dialog submitDialog = null;
	private void submitOrder(){
		try {
			submitDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在提交...");
			String submitJson = Order2Data.order2Json(order);
			String url = UrlInfo.doOrderNewInfo(this);
			RequestParams requestParams = new RequestParams();
			requestParams.put("phoneno", PublicUtils.receivePhoneNO(this));
			requestParams.put("storeId", storeId);
			requestParams.put("order", submitJson);
			JLog.d(TAG, "storeId:"+storeId +"\n"+"order:"+submitJson);
			GcgHttpClient.getInstance(this).post(url, requestParams,new HttpResponseListener() {
				@Override
				public void onStart() {
					submitDialog.show();
				}


				@Override
				public void onFailure(Throwable error, String content) {
					JLog.d(TAG, content+"");
					ToastOrder.makeText(OrderCreateActivity.this, "网络异常订单提交失败", ToastOrder.LENGTH_SHORT).show();
				}

				@Override
				public void onFinish() {
					submitDialog.dismiss();
				}


				@Override
				public void onSuccess(int statusCode, String content) {
					JLog.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							submitOrderSuccessFinish();
							ToastOrder.makeText(OrderCreateActivity.this, "订单提交成功", ToastOrder.LENGTH_SHORT).show();
							OrderCreateActivity.this.finish();
						}else{
							ToastOrder.makeText(OrderCreateActivity.this, "订单提交失败", ToastOrder.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastOrder.makeText(OrderCreateActivity.this, "订单提交失败", ToastOrder.LENGTH_SHORT).show();
					}					
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(OrderCreateActivity.this, "订单数据异常", ToastOrder.LENGTH_SHORT).show();
		}
	
	}
	
	/**
	 * 提交订单成功以后
	 */
	protected void submitOrderSuccessFinish(){}
	/**
	 * 初始化所有客户数据
	 */
	protected void initStoreData(String fuzzy){
		allStore = new OrgStoreDB(this).findAllOrgList(fuzzy);
		List<Dictionary> srcList = new ArrayList<Dictionary>();
		if (allStore!=null && allStore.size()>0) {
			OrgStore store = null;
			Dictionary dic = null;
			
			for (int i = 0; i < allStore.size(); i++) {
				store = allStore.get(i);
				dic = new Dictionary();
				dic.setDid(String.valueOf(store.getStoreId()));
				dic.setCtrlCol(store.getStoreName());
				srcList.add(dic);
			}
		}
		order2_spinner_store.setSrcDictList(srcList);
		initStoreSelect(srcList);
	}
	
	/**
	 * 设置选中的店面
	 * @param srcList 所有店面
	 */
	private void initStoreSelect(List<Dictionary> srcList){
		if (!TextUtils.isEmpty(storeId) && srcList.size()>0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (storeId.equals(dic.getDid())) {
					order2_spinner_store.setSelected(dic);
					break;
				}
			}
		}
	}

	@Override
	public void onTextChanged(CharSequence s) {
		initStoreData(s.toString());
	}
	
	private OnResultListener resultListener = new OnResultListener() {
		
		@Override
		public void onResult(List<Dictionary> result) {
			if (result !=null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				initOrder(dic);
				initFragmen();
//				storeId = dic.getDid();
//				order.setStoreId(storeId);
//				order.setStoreName(dic.getCtrlCol());
//				try {
//					SharedPreferencesForOrder2Util.getInstance(OrderCreateActivity.this).setOrder2(Order2Data.order2Json(order));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
			
		}
	};
	
	/**
	 * 拍照 扫描返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode==100 && requestCode==100) {
				String updateIndex = data.getStringExtra("updateIndex");
				String orderDetailJson = data.getStringExtra("updateDetailJson");
				OrderDetail detail = Order2Data.json2OrderDetail(new JSONObject(orderDetailJson));
				if (!TextUtils.isEmpty(updateIndex)) {//修改返回
					int index = Integer.parseInt(updateIndex);
					orderDetailList.set(index, detail);
				}else{//添加返回
					orderDetailList.add(detail);
				}
				compute();
				newOrderDB.updateNewOrder(newOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算订单
	 */
	private void compute(){
		double totalAmount = 0;
		for (int i = 0; i < orderDetailList.size(); i++) {
			OrderDetail detail = orderDetailList.get(i);
			totalAmount += detail.getAmount();
		}
		order.setOrderDetailList(orderDetailList);
		order.setAmount(String.valueOf(totalAmount));
		double unPay = Double.parseDouble(TextUtils.isEmpty(order.getAmount())?"0":order.getAmount()) - orderSpec.getPay() - orderSpec.getDiscount();
		orderSpec.setUnPay(unPay);
		order.setOrderSpec(orderSpec);
		order.setAmount(String.valueOf(totalAmount));
		newOrder.setOrder(order);
	}
	
}
