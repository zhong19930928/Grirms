package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.OrderCacheDB;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.bo.OrderCache;
import com.yunhu.yhshxc.order.bo.PSSConf;
import com.yunhu.yhshxc.order.listener.OnProductChoosedListener;
import com.yunhu.yhshxc.order.view.OrderProductInfo;
import com.yunhu.yhshxc.order.view.ProductLayout;
import com.yunhu.yhshxc.parser.ParsePSS;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CreateOrderDetailActivity extends Activity implements OnProductChoosedListener{
	private final String TAG="CreateOrderDetailActivity";
	private LinearLayout ll_order_priview;//预览
	private LinearLayout ll_order_delete;//删除
	private LinearLayout ll_order_create_detail_product;//订单产品
	private ProductLayout productLayout;
	private TextView tv_order_create_detail_availableOrderQuantity;//可订货数量
	private TextView tv_order_create_detail_periodDate;//数据时段
	private TextView tv_order_create_detail_totalSales;//总计销售量
	private TextView tv_order_create_detail_projectedInventoryQuantity;//预计库存量
	private TextView tv_order_create_detail_tip;//提示
	private TextView tv_order_create_detail_store_name;//店面名称
	private TextView tv_order_create_detail_delete;//删除
	private EditText et_order_create_detail_price;//单价
	private EditText et_order_create_detail_orderQuantity;//订货数量
	private boolean isCreateOrder=false;//是否是创建订单
	private Bundle bundle=null;//传递数据的bundle
	private OrderCache orderCatch=null;
	private Dialog loadDialog=null;
	private String orderNumber=null;//订单编号
	private String storeId,storeName;// 店面ID 店面名称 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_create_detail);
		ll_order_create_detail_product=(LinearLayout)findViewById(R.id.ll_order_create_detail_product);
		tv_order_create_detail_availableOrderQuantity=(TextView)findViewById(R.id.tv_order_create_detail_availableOrderQuantity);
		tv_order_create_detail_periodDate=(TextView)findViewById(R.id.tv_order_create_detail_periodDate);
		tv_order_create_detail_totalSales=(TextView)findViewById(R.id.tv_order_create_detail_totalSales);
		tv_order_create_detail_projectedInventoryQuantity=(TextView)findViewById(R.id.tv_order_create_detail_projectedInventoryQuantity);
		tv_order_create_detail_tip=(TextView)findViewById(R.id.tv_order_create_detail_tip);
		tv_order_create_detail_store_name=(TextView)findViewById(R.id.tv_order_create_detail_store_name);
		tv_order_create_detail_delete=(TextView)findViewById(R.id.tv_order_create_detail_delete);
		et_order_create_detail_price=(EditText)findViewById(R.id.et_order_create_detail_price);
		et_order_create_detail_orderQuantity=(EditText)findViewById(R.id.et_order_create_detail_orderQuantity);
		ll_order_priview=(LinearLayout)findViewById(R.id.ll_order_priview);
		ll_order_delete=(LinearLayout)findViewById(R.id.ll_order_delete);
		ll_order_priview.setOnClickListener(onClickListener);
		ll_order_delete.setOnClickListener(onClickListener);
		bundle=getIntent().getBundleExtra("bundle");
		storeId=bundle.getString("storeId");
		storeName=bundle.getString("storeName");
		tv_order_create_detail_tip.setText("");
		tv_order_create_detail_store_name.setText(storeName);
		et_order_create_detail_orderQuantity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(tv_order_create_detail_tip.getText().toString())){
					tv_order_create_detail_tip.setText("");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		//设置单价是否可修改
		PSSConf pssConf=new PSSConfDB(this).findPSSConf();
		if(pssConf!=null){
			String is_price_edit=pssConf.getIsPriceEdit();
			if(TextUtils.isEmpty(is_price_edit) || is_price_edit.equals("1")){//可修改的
				et_order_create_detail_price.setEnabled(true);
			}else{
				et_order_create_detail_price.setEnabled(false);
			}
		}
		initLayout();
		
	}
	
	/**
	 * 初始化页面布局
	 */
	private void initLayout(){
		
		isCreateOrder=bundle.getBoolean("isCreateOrder");
		if(isCreateOrder){//创建订单
			productLayout=new ProductLayout(this);
			productLayout.setOnProductChoosedListener(this);
			ll_order_create_detail_product.addView(productLayout);
			tv_order_create_detail_delete.setText("继续创建订单");
		}else{
			tv_order_create_detail_delete.setText("删除");
			orderCatch=new OrderCacheDB(this).findOrderCacheId(bundle.getString("id"));
			if(orderCatch!=null){
				OrderProductInfo orderProductInfo=new OrderProductInfo(this,orderCatch.getOrderProductName());
				ll_order_create_detail_product.addView(orderProductInfo.getView());
				String availableOrderQuantity=orderCatch.getAvailableOrderQuantity()+"";//可订货数量
				String periodDate=orderCatch.getPeriodDate();//数据时间段
				String totalSales=orderCatch.getTotalSales()+"";//总计销量
				String projectedInventoryQuantity=orderCatch.getProjectedInventoryQuantity()+"";//预计库存量
				if (availableOrderQuantity.equals("1")) {//1显示不限
					tv_order_create_detail_availableOrderQuantity.setText("不限");
					tv_order_create_detail_availableOrderQuantity.setTextColor(getResources().getColor(R.color.green));
				}else{
					tv_order_create_detail_availableOrderQuantity.setText(availableOrderQuantity);
				}
				
				tv_order_create_detail_periodDate.setText(periodDate);
				tv_order_create_detail_totalSales.setText(totalSales);
				tv_order_create_detail_projectedInventoryQuantity.setText(projectedInventoryQuantity);
				et_order_create_detail_price.setText(orderCatch.getUnitPrice()+"");//单价
				et_order_create_detail_orderQuantity.setText(orderCatch.getOrderQuantity()==0?"":orderCatch.getOrderQuantity()+"");//订货数量
			}
		}
	}
	
	/**
	 * 单击监听事件
	 */
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order_priview:
				priviewAndSave();
				break;
			case R.id.ll_order_delete:
				if(isCreateOrder){
					createMoreOrder();
				}else{
					delete();
				}
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 继续创建新订单
	 */
	private void createMoreOrder(){
		if(priviewAndSave()){//先保存旧的订单
			Intent intent=new Intent(this, CreateOrderDetailActivity.class);
			Bundle bundle=new Bundle();
			bundle.putBoolean("isCreateOrder", true);//创建新产品订单
			bundle.putString("storeId", storeId);//店面ID
			bundle.putString("storeName", storeName);//店面名称
			bundle.putString("orderNumber", orderNumber);//订单编号
			intent.putExtra("bundle", bundle);
			startActivity(intent);
		}
	}
	
	/**
	 * 预览
	 * 返回true 表示保存成功 false表示不满足保存条件
	 */
	private boolean  priviewAndSave(){
		String unitPrice=et_order_create_detail_price.getText().toString();
		String orderQuantity=et_order_create_detail_orderQuantity.getText().toString();
		if(orderCatch==null){
			ToastOrder.makeText(this, "请选择产品", ToastOrder.LENGTH_SHORT).show();
			return false;
		}
		if(TextUtils.isEmpty(unitPrice) || Double.parseDouble(unitPrice)<=0){
			ToastOrder.makeText(this, "请输入单价", ToastOrder.LENGTH_SHORT).show();
			return false;
		}
		if(TextUtils.isEmpty(orderQuantity) || Double.parseDouble(orderQuantity)<=0){
			tv_order_create_detail_tip.setText("提示  没输入订货数量");
			return false;
		}
		
//		if(orderCatch!=null){
//			//查找数据库中是已经有该产品订单 true有 false 没有
//			boolean isHasProduct=new OrderCacheDB(this).findOrderCacheByOrderProductId(orderCatch.getOrderProductId()+"")==null?false:true;
//			orderCatch.setUnitPrice(Double.parseDouble(et_order_create_detail_price.getText().toString()));
//			orderCatch.setOrderQuantity(Integer.parseInt(et_order_create_detail_orderQuantity.getText().toString()));
//			if(isHasProduct){
//				askDialog("该产品已经有一个订单,是否替换？",orderCatch).show();
//			}else{
//				new OrderCacheDB(this).insert(orderCatch);
//				finish();
//			}
//		}
		
		if(orderCatch!=null){
			orderCatch.setUnitPrice(Double.parseDouble(et_order_create_detail_price.getText().toString()));
			orderCatch.setOrderQuantity(Long.parseLong(et_order_create_detail_orderQuantity.getText().toString()));
			if(isCreateOrder){//如果是创建订单的话就插入
				new OrderCacheDB(this).insert(orderCatch);
			}else{//修改
				new OrderCacheDB(this).updateOrderCache(orderCatch);
			}
		}
		finish();
		return true;
	}
	
	/**
	 * 如果表中已经有该产品的订单信息，提示用户是否替换原订单
	 * @param bContent 询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog askDialog(String bContent,final OrderCache orderCatch) {
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
				new OrderCacheDB(CreateOrderDetailActivity.this).updateOrderCache(orderCatch);
				CreateOrderDetailActivity.this.finish();
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
	 * 删除
	 */
	private void delete(){
		deleteDialog("是否确定删除该产品？").show();
	}
	/**
	 * 
	 * @param bContent 询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog deleteDialog(String bContent) {
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
				String id=bundle.getString("id");
				new OrderCacheDB(CreateOrderDetailActivity.this).deleteOrderCatchById(id);
				CreateOrderDetailActivity.this.finish();
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
	 * 获取订单产品信息
	 * @author jishen
	 *
	 */
	private void getOrderProductInfo(final String orderProductId,final String productName){
		String url=UrlInfo.getUrlPSSCreateOrderProductInfo(CreateOrderDetailActivity.this,storeId,orderProductId);
		JLog.d(TAG, "获取产品信息URL："+url);
		
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				loadDialog=new MyProgressDialog(CreateOrderDetailActivity.this,R.style.CustomProgressDialog,getResources().getString(R.string.initTable));
				loadDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if(loadDialog!=null && loadDialog.isShowing()){
					loadDialog.dismiss();
				}
				retryDialog("没查询到产品信息,是否重试？").show();
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
						orderCatch=new ParsePSS(CreateOrderDetailActivity.this).getOrderCacheInfo(content);//解析
						if(orderCatch!=null){
							orderNumber=bundle.getString("orderNumber");
							orderCatch.setOrderNumber(orderNumber);
							orderCatch.setOrderProductName(productName);
							orderCatch.setOrderProductId(Integer.parseInt(orderProductId));
							refalseUI();
						}else{//没有查询到信息
							retryDialog("没查询到产品信息,是否重试？").show();
						}
					}else{//失败
						retryDialog("没查询到产品信息,是否重试？").show();
					}
				} catch (Exception e) {//数据异常，失败
					JLog.d(TAG, "异常："+e.getMessage());
					retryDialog("没查询到产品信息,是否重试？").show();
				}
				
			}
			
		});
	}
	
	
//	/**
//	 * 获取订单产品信息
//	 * @author jishen
//	 *
//	 */
//	private class GetOrderProductInfo extends AsyncTask{
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			loadDialog=new MyProgressDialog(CreateOrderDetailActivity.this,R.style.CustomProgressDialog,getResources().getString(R.string.initTable));
//			loadDialog.show();
//		}
//		@Override
//		protected Object doInBackground(Object... params) {
//			String orderProductId=(String) params[0];
//			String productName=(String) params[1];
//			String url=UrlInfo.getUrlPSSCreateOrderProductInfo(CreateOrderDetailActivity.this,storeId,orderProductId);
//			JLog.d(TAG, "获取产品信息URL："+url);
//			String result=new HttpHelper(CreateOrderDetailActivity.this,HttpHelper.TIMEOUT_SHORT).connectGet(url);
//			JLog.d(TAG, "result:"+result);
//			try {
//				if(TextUtils.isEmpty(result)){//返回结果是空 获取失败
//					Message msg=loadHander.obtainMessage(2);
//					loadHander.sendMessage(msg);
//				}else{
//					JSONObject jsonObject = new JSONObject(result);
//					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
//					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
//						orderCatch=new ParsePSS(CreateOrderDetailActivity.this).getOrderCacheInfo(result);//解析
//						if(orderCatch!=null){
//							orderNumber=bundle.getString("orderNumber");
//							orderCatch.setOrderNumber(orderNumber);
//							orderCatch.setOrderProductName(productName);
//							orderCatch.setOrderProductId(Integer.parseInt(orderProductId));
//							Message msg=loadHander.obtainMessage(3);
//							loadHander.sendMessage(msg);
//						}else{//没有查询到信息
//							Message msg=loadHander.obtainMessage(1);
//							loadHander.sendMessage(msg);
//						}
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
//			case 1://没查询到信息
//			case 2://加载失败
//				retryDialog("没查询到产品信息,是否重试？").show();
//				break;
//			case 3://加载成功
//				refalseUI();
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
	
	/**
	 * 查询到信息以后刷新UI
	 */
	private void refalseUI(){
		String availableOrderQuantity=orderCatch.getAvailableOrderQuantity()+"";//可订货数量
		String periodDate=orderCatch.getPeriodDate();//数据时间段
		String totalSales=orderCatch.getTotalSales()+"";//总计销量
		String projectedInventoryQuantity=orderCatch.getProjectedInventoryQuantity()+"";//预计库存量
		tv_order_create_detail_availableOrderQuantity.setText(availableOrderQuantity);
		if (availableOrderQuantity.equals("1")) {//1显示不限
			tv_order_create_detail_availableOrderQuantity.setText("不限");
			tv_order_create_detail_availableOrderQuantity.setTextColor(getResources().getColor(R.color.green));
		}else{
			tv_order_create_detail_availableOrderQuantity.setText(availableOrderQuantity);
		}
		tv_order_create_detail_periodDate.setText(periodDate);
		tv_order_create_detail_totalSales.setText(totalSales);
		tv_order_create_detail_projectedInventoryQuantity.setText(projectedInventoryQuantity);
		et_order_create_detail_price.setText(orderCatch.getUnitPrice()+"");//单价
		et_order_create_detail_orderQuantity.setText(orderCatch.getOrderQuantity()==0?"":orderCatch.getOrderQuantity()+"");//订货数量
	}

	
	private String selectPruductId;//当前选中的产品ID
	private String selectPruductName;//当前选中的产品名称
	@Override
	public void onOptionsItemChoosed(Dictionary obj) {
		if(obj!=null){
			JLog.d(TAG, "Dictionary did:"+obj.getDid()+"name:"+obj.getCtrlCol());
			orderCatch=null;
			selectPruductId=obj.getDid();
			selectPruductName=obj.getCtrlCol();
//			new GetOrderProductInfo().execute(selectPruductId,selectPruductName);
			getOrderProductInfo(selectPruductId,selectPruductName);
		}
		
	}
	
	/**
	 * 
	 * @param bContent 询问用户的文字
	 * @return 返回一个构造好的Dialog对象
	 */
	private Dialog retryDialog(String bContent) {
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
//				new GetOrderProductInfo().execute(selectPruductId,selectPruductName);
				getOrderProductInfo(selectPruductId,selectPruductName);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
				CreateOrderDetailActivity.this.finish();
			}
		});
		deleteDialog.setContentView(view);
		deleteDialog.setCancelable(false);
		return deleteDialog;
			
	}
	
}
