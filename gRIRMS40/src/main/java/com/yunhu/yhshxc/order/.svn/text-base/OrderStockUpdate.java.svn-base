package com.yunhu.yhshxc.order;

import gcg.org.debug.JLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.database.PSSConfDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order.listener.OnProductChoosedListener;
import com.yunhu.yhshxc.order.view.ProductLayout;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

/**
 * 修改库存
 * @author jishen
 *
 */
public class OrderStockUpdate extends Activity{
	private final String TAG="OrderStockUpdate";
	private TextView tv_order_stock_update_store_name;//店面名称
//	private TextView tv_order_stock_update_product;//产品名称
	private TextView tv_order_stock_number;//估算库存
	private EditText et_order_stock_update_stock_check;//盘点库存
	private Spinner sp_order_stock_update_reason;//差异原因
	private LinearLayout ll_order_stock_update_estimate_correctly;//估算正确
	private LinearLayout ll_order_stock_update_submit;//提交
	private LinearLayout ll_order_stock_update_product;//产品
	private String storeName,storeId,productName,productNumber;//店面名称 店面ID 产品名称 剩余库存
	private int productId;// 产品ID
	private List<Dictionary> reasonList;//差异原因
	private String reasonId;//差异原因id
	private Dialog loadDialog;
	private boolean addProduct;//是否是添加产品 true 表示添加产品 false 表示不是添加
	private ProductLayout productLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_stock_update);
		tv_order_stock_update_store_name=(TextView)findViewById(R.id.tv_order_stock_update_store_name);
//		tv_order_stock_update_product=(TextView)findViewById(R.id.tv_order_stock_update_product);
		tv_order_stock_number=(TextView)findViewById(R.id.tv_order_stock_number);
		et_order_stock_update_stock_check=(EditText)findViewById(R.id.et_order_stock_update_stock_check);
		sp_order_stock_update_reason=(Spinner)findViewById(R.id.sp_order_stock_update_reason);
		ll_order_stock_update_estimate_correctly=(LinearLayout)findViewById(R.id.ll_order_stock_update_estimate_correctly);
		ll_order_stock_update_submit=(LinearLayout)findViewById(R.id.ll_order_stock_update_submit);
		ll_order_stock_update_product=(LinearLayout)findViewById(R.id.ll_order_stock_update_product);
		ll_order_stock_update_estimate_correctly.setOnClickListener(onClickListener);
		ll_order_stock_update_submit.setOnClickListener(onClickListener);
		storeName=getIntent().getStringExtra("storeName");
		storeId=getIntent().getStringExtra("storeId");
		productName=getIntent().getStringExtra("productName");
		productNumber=getIntent().getStringExtra("productNumber");
		productId=getIntent().getIntExtra("productId", 0);
		addProduct=getIntent().getBooleanExtra("addProduct", false);
		tv_order_stock_update_store_name.setText(storeName);
//		tv_order_stock_update_product.setText(productName);
		tv_order_stock_number.setText(TextUtils.isEmpty(productNumber)?"0":productNumber);
		reasonList=new PSSConfDB(this).findStocktakeDifferList();
		if(reasonList!=null){
			ArrayAdapter<Dictionary> adapter=new ArrayAdapter<Dictionary>(this, android.R.layout.simple_spinner_item, reasonList);
			adapter.setDropDownViewResource(R.layout.sprinner_check_item);
			sp_order_stock_update_reason.setAdapter(adapter);
			sp_order_stock_update_reason.setOnItemSelectedListener(onItemSelectedListener);
		}
		addProduct();//添加产品
	}
	
	/**
	 * 添加产品
	 */
	private void addProduct(){
		if(addProduct){//库存添加产品
			productLayout=new ProductLayout(this);
			productLayout.setOnProductChoosedListener(new OnProductChoosedListener() {
				
				@Override
				public void onOptionsItemChoosed(Dictionary obj) {
					if(obj!=null){
						productId=Integer.parseInt(obj.getDid());
						productName=obj.getCtrlCol();
					}
				}
			});
			ll_order_stock_update_product.addView(productLayout);
		}else{//修改库存
			TextView tv_name=new TextView(this);
			tv_name.setText(productName);
			tv_name.setTextColor(getResources().getColor(R.color.order_title));
			tv_name.setTextSize(20);
			ll_order_stock_update_product.addView(tv_name);
		}
	}
	
	/**
	 * 选择货物偏差原因下拉框
	 */
	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			reasonId=reasonList.get(position).getDid();
			JLog.d(TAG, "差异原因 ID"+reasonId+" "+reasonList.get(position).getCtrlCol());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	/**
	 * 单击监听
	 */
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_order_stock_update_estimate_correctly:
			case R.id.ll_order_stock_update_submit:
				submit(v.getId());
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 提交
	 */
	private void submit(int id){
		if(productId==0 || TextUtils.isEmpty(productName)){
			ToastOrder.makeText(OrderStockUpdate.this, "请选择产品", ToastOrder.LENGTH_SHORT).show();
			return;
		}
		String etNumber=et_order_stock_update_stock_check.getText().toString();
		if(TextUtils.isEmpty(etNumber)){
			ToastOrder.makeText(OrderStockUpdate.this, "请输入盘点库存", ToastOrder.LENGTH_SHORT).show();
			return;
		}
		String content="";
		final HashMap<String, String> params=new HashMap<String, String>();
		params.put("storeid", storeId);
		params.put("productname", productName);
		params.put("proid", productId+"");
		JLog.d(TAG, "storeid:"+storeId);
		JLog.d(TAG, "productname:"+productName);
		JLog.d(TAG, "proid:"+productId);
		switch (id) {
		case R.id.ll_order_stock_update_estimate_correctly:
			content ="盘点库存与估算正确是否一致？";
			if(addProduct){//添加库存产品的时候
				productNumber="0";//估算产品是0
			}
			params.put("total", productNumber);
			JLog.d(TAG, "total:"+productNumber);
			break;
		case R.id.ll_order_stock_update_submit:
			params.put("total",etNumber);
			params.put("ressonid", reasonId);
			JLog.d(TAG, "total:"+etNumber);
			JLog.d(TAG, "ressonid:"+reasonId);
			content ="是否确定提交？";
			break;
		}
		final OrderConfirmDialog dialog=new OrderConfirmDialog(this);
		TextView tv_contene=dialog.getMessageText();
		tv_contene.setText(content);
		Button okBtn=dialog.getOkButton();
		Button cancelBtn=dialog.getCancelButton();
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				startSubmit(params);
				startSubmit2(params);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * 开始提交
	 * @param params 要提交的数据
	 */
	
	private void startSubmit2(HashMap<String, String> params){
		showloadDialog();
		RequestParams requesParams=new RequestParams();
		for (Map.Entry<String, String> entry:params.entrySet()) {
			requesParams.put(entry.getKey(), entry.getValue());
			JLog.d(TAG, entry.getKey()+":"+entry.getValue());
		}
		String url=UrlInfo.UrlPSSOrderInventorySave(OrderStockUpdate.this);
		JLog.d(TAG, "url:"+url);
		GcgHttpClient.getInstance(this).post(url, requesParams, new HttpResponseListener(){
			@Override
			public void onStart() {
			}
			@Override
			public void onFailure(Throwable error, String content) {
				if(loadDialog!=null && loadDialog.isShowing()){
					loadDialog.dismiss();
				}
				JLog.d(TAG, "提交result："+content);
				ToastOrder.makeText(OrderStockUpdate.this, R.string.toast_one7, ToastOrder.LENGTH_SHORT).show();
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
					JLog.d(TAG, "提交result："+content);
					JSONObject jsonObject = new JSONObject(content);
					String resultcode = jsonObject.getString(Constants.RESULT_CODE);
					if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
						ToastOrder.makeText(OrderStockUpdate.this, R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
						setResult(R.id.order_stock_submit_return);
						finish();
					}else{//获取失败
						ToastOrder.makeText(OrderStockUpdate.this, R.string.toast_one7, ToastOrder.LENGTH_SHORT).show();
					}
					
				} catch (Exception e) {
					JLog.d(TAG, "异常："+e.getMessage());
					ToastOrder.makeText(OrderStockUpdate.this, R.string.toast_one7, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
//	/**
//	 * 开始提交
//	 * @param params 要提交的数据
//	 */
//	private void startSubmit(final HashMap<String, String> params){
//		showloadDialog();
//		new Thread(){
//			public void run() {
//				try {
//					String url=UrlInfo.UrlPSSOrderInventorySave(OrderStockUpdate.this);
//					JLog.d(TAG, "url:"+url);
//					String result=new HttpHelper(OrderStockUpdate.this,HttpHelper.TIMEOUT_SHORT).connectPost(url, params);
//					JLog.d(TAG, "提交result："+result);
//					if(TextUtils.isEmpty(result)){
//						Message msg=submitHandler.obtainMessage(2);
//						submitHandler.sendMessage(msg);
//					}else{
//						JSONObject jsonObject = new JSONObject(result);
//						String resultcode = jsonObject.getString(Constants.RESULT_CODE);
//						if(resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS)){//返回0000获取成功
//							Message msg=submitHandler.obtainMessage(1);
//							submitHandler.sendMessage(msg);
//						}else{//获取失败
//							Message msg=submitHandler.obtainMessage(2);
//							submitHandler.sendMessage(msg);
//						}
//					}
//				} catch (Exception e) {
//					JLog.d(TAG, "异常："+e.getMessage());
//					Message msg=submitHandler.obtainMessage(2);
//					submitHandler.sendMessage(msg);
//				}
//			};
//		}.start();
//	}
//	
//	/**
//	 * 提交
//	 */
//	private Handler submitHandler=new Handler(){
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			int what=msg.what;
//			if(loadDialog!=null && loadDialog.isShowing()){
//				loadDialog.dismiss();
//			}
//			switch (what) {
//			case 1://提交成功
//				Toast.makeText(OrderStockUpdate.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();
//				setResult(R.id.order_stock_submit_return);
//				finish();
//				break;
//			case 2://提交失败
//				Toast.makeText(OrderStockUpdate.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
//				break;
//			}
//		};
//	};
//	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				setResult(R.id.order_stock_return);
				finish();
				return true;
		}
		return super.onKeyDown(keyCode, event);
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
}
