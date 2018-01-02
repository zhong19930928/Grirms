package com.yunhu.yhshxc.order3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.order3.bo.Order3Contact;
import com.yunhu.yhshxc.order3.db.Order3ContactsDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.parser.Order3Parse;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3ManagerActivity extends AbsBaseActivity{
	
	private LinearLayout ll_syc_kh;//同步客户信息
	private LinearLayout ll_syc_cx;//同步促销信息
	private LinearLayout ll_syc_lxr;//同步联系人
	private LinearLayout ll_syc_cp;//同步产品
	private LinearLayout ll_syc_dis;//同步分销信息
	private Order3ContactsDB orderContactsDB;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_manager_activity);
		orderContactsDB = new Order3ContactsDB(this);
		initWidget();
		contactControl();
	}
	
	
	private void initWidget(){
		ll_syc_kh = (LinearLayout)findViewById(R.id.ll_syc_kh);
		ll_syc_kh.setOnClickListener(listener);
		ll_syc_cx = (LinearLayout)findViewById(R.id.ll_syc_cx);
		ll_syc_cx.setOnClickListener(listener);
		ll_syc_lxr = (LinearLayout)findViewById(R.id.ll_syc_lxr);
		ll_syc_lxr.setOnClickListener(listener);
		ll_syc_cp = (LinearLayout)findViewById(R.id.ll_syc_cp);
		ll_syc_cp.setOnClickListener(listener);
		ll_syc_dis = (LinearLayout)findViewById(R.id.ll_syc_dis);
		ll_syc_dis.setOnClickListener(listener);
	}
	
	private void contactControl(){
		int isOrderUser = SharedPreferencesForOrder3Util.getInstance(this).getIsOrderUser();
		if (isOrderUser == 1) {
			ll_syc_lxr.setVisibility(View.GONE);
		}else{
			ll_syc_lxr.setVisibility(View.VISIBLE);

		}
	}
	
	/**
	 * 同步客户信息
	 */
	private Dialog sycKHDialog=null;
	private void sycKH(){
		sycKHDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在同步客户信息...");
		URLWrapper wrapper = new URLWrapper(UrlInfo.getUrlInitOperation(this));
		wrapper.addParameter("operation", Func.ORG_STORE);
		String url = wrapper.getRequestURL();
		GcgHttpClient.getInstance(this).get(url,null, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						parserOrgStore(content);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					parserOrgStoreHander.sendEmptyMessage(2);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				ToastOrder.makeText(Order3ManagerActivity.this, "客户信息同步失败", ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onStart() {
				sycKHDialog.show();
			}

			@Override
			public void onFinish() {
			}

		});
	}
	
	/**
	 * 解析客户信息
	 * @param content
	 */
	private void parserOrgStore(final String content){
		
		new Thread(){
			public void run() {
				try {
					new OrgParser(Order3ManagerActivity.this).parseAll(content);
					parserOrgStoreHander.sendEmptyMessage(1);
				} catch (Exception e) {
					parserOrgStoreHander.sendEmptyMessage(2);
				}
			};
		}.start();
	}
	
	private Handler parserOrgStoreHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (sycKHDialog!=null && sycKHDialog.isShowing()) {
				sycKHDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				sendRefrashStoreBroadcastReceiver();
				ToastOrder.makeText(Order3ManagerActivity.this, "客户同步成功", ToastOrder.LENGTH_SHORT).show();
				break;
			case 2:
				ToastOrder.makeText(Order3ManagerActivity.this, "客户数据异常", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 同步促销信息
	 */
	private void sycCX(){
		int isPromotion = SharedPreferencesForOrder3Util.getInstance(this).getIsPromotion();
		if (isPromotion == 2) {
			synDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在同步促销信息...");
			String url = UrlInfo.queryOrder3ProductPromotion(this);
			GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					try {
//						JLog.d("aaa",content);
						JSONObject obj = new JSONObject(content);
						String resultCode = obj.getString("resultcode");
						if ("0000".equals(resultCode)) {
							Order3Parse parser = new Order3Parse(Order3ManagerActivity.this);
							if (PublicUtils.isValid(obj, parser.THREE_PROMOTION)) {
								JSONArray promotionArray = obj.getJSONArray(parser.THREE_PROMOTION);
								parser.parserProductPromotion(promotionArray);
							}
							sendInitDataBroadcastReceiver();
							Intent intent = new Intent(Order3ManagerActivity.this,Order3SyncInfoActivity.class);
							startActivity(intent);
						}else{
							throw new Exception();
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastOrder.makeText(Order3ManagerActivity.this, "同步数据异常", ToastOrder.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onStart() {
					synDialog.show();
				}
				
				@Override
				public void onFinish() {
					synDialog.dismiss();
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(Order3ManagerActivity.this, "促销信息同步失败", ToastOrder.LENGTH_SHORT).show();
				}
			});
		}else{
			ToastOrder.makeText(this, "该用户不享受促销", ToastOrder.LENGTH_SHORT).show();
		}
	}
	/**
	 * 同步联系人
	 */
	private Dialog synDialog = null;
	private void sycLXR(){
		synDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在同步联系人...");
		String storeId = SharedPreferencesForOrder3Util.getInstance(this).getStoreId();
		if (!TextUtils.isEmpty(storeId)) {
			String url = UrlInfo.queryOrderNewUserInfo(this, storeId);
			GcgHttpClient.getInstance(this).post(url, null,new HttpResponseListener() {
				@Override
				public void onStart() {
					synDialog.show();
				}


				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(Order3ManagerActivity.this, "联系人同步失败", ToastOrder.LENGTH_SHORT).show();

				}

				@Override
				public void onFinish() {
					synDialog.dismiss();
				}


				@Override
				public void onSuccess(int statusCode, String content) {

//					JLog.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							if (obj.has("orderUser") && !TextUtils.isEmpty(obj.getString("orderUser"))) {
								JSONArray arr = obj.getJSONArray("orderUser");
								orderContactsDB.clearOrderContacts();
								Order3Parse parse = new Order3Parse(Order3ManagerActivity.this);
								for (int i = 0; i < arr.length(); i++) {
									JSONObject userObj = arr.getJSONObject(i);
									Order3Contact orderContacts = parse.parserContact(userObj);
									orderContactsDB.insertOrderContants(orderContacts);
								}
							}else{
								ToastOrder.makeText(Order3ManagerActivity.this, "没有联系人信息", ToastOrder.LENGTH_SHORT).show();
							}
						}else{
							ToastOrder.makeText(Order3ManagerActivity.this, "联系人同步失败", ToastOrder.LENGTH_SHORT).show();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						ToastOrder.makeText(Order3ManagerActivity.this, "联系人同步失败", ToastOrder.LENGTH_SHORT).show();
					}
				}

			});
		}else{
			ToastOrder.makeText(Order3ManagerActivity.this, "请先选择客户", ToastOrder.LENGTH_SHORT).show();
		}
	}
	/**
	 * 同步产品
	 */
	private Dialog synCPDialog = null;
	private void sycCP(){
		synCPDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在同步配置信息...");
		String url = UrlInfo.queryThreeOrderConfInfo(this);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				sycCP(content);
			}
			
			@Override
			public void onStart() {
				synCPDialog.show();
			}
			
			@Override
			public void onFinish() {
//				synDialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				sycCPHander.sendEmptyMessage(3);
			}
		});
	
	}
	/**
	 * 同步分销
	 */
	private Dialog synDisDialog = null;
	private void sycDIS(){
		int dis = SharedPreferencesForOrder3Util.getInstance(this).getIsdistSales();
		if (dis == 2) {
			synDisDialog =  new MyProgressDialog(this,R.style.CustomProgressDialog,"正在同步分销信息...");
			String url = UrlInfo.queryThreeDistribution(this);
			GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					try {
						JSONObject obj = new JSONObject(content);
						String resultcode = obj.getString("resultcode");
						if ("0000".equals(resultcode)) {
							Order3Parse parser = new Order3Parse(Order3ManagerActivity.this);
							if (PublicUtils.isValid(obj, parser.ORDERDIS)) {
								JSONArray disArray = obj.getJSONArray(parser.ORDERDIS);
								parser.parserOrder3Dis(disArray);
							}
							sendInitDataBroadcastReceiver();
							ToastOrder.makeText(Order3ManagerActivity.this, "分销信息同步成功", ToastOrder.LENGTH_SHORT).show();
						}else{
							throw new Exception();
						}
					} catch (Exception e) {
						ToastOrder.makeText(Order3ManagerActivity.this, "分销信息数据异常", ToastOrder.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onStart() {
					synDisDialog.show();
				}
				
				@Override
				public void onFinish() {
					synDisDialog.dismiss();
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(Order3ManagerActivity.this, "分销信息同步失败", ToastOrder.LENGTH_SHORT).show();
				}
			});
		}else{
			ToastOrder.makeText(Order3ManagerActivity.this, "该用户没有分销信息", ToastOrder.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 解析配置信息
	 * @param content
	 */
	private void sycCP(final String content){
		
		new Thread(){
			public void run() {
				try {
					JSONObject object = new JSONObject(content);
//					new Order3Parse(Order3ManagerActivity.this).parserOrder3(object);
					new Order3Parse(Order3ManagerActivity.this).parserAll(object);
					new Order3Util(Order3ManagerActivity.this).initOrder3ProductCtrl();
					sycCPHander.sendEmptyMessage(1);
				} catch (JSONException e) {
					e.printStackTrace();
					sycCPHander.sendEmptyMessage(2);
				}
			};
		}.start();
	}
	
	private Handler sycCPHander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (synCPDialog!=null && synCPDialog.isShowing()) {
				synCPDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				sendInitDataBroadcastReceiver();
				ToastOrder.makeText(Order3ManagerActivity.this, "配置信息同步成功", ToastOrder.LENGTH_SHORT).show();
				break;
			case 2:
				ToastOrder.makeText(Order3ManagerActivity.this, "配置数据异常", ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
				ToastOrder.makeText(Order3ManagerActivity.this, "配置同步失败", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	
	private OnClickListener listener  =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_syc_kh:
				sycKH();
				break;
			case R.id.ll_syc_cx:
				sycCX();
				break;
			case R.id.ll_syc_lxr:
				sycLXR();
				break;
			case R.id.ll_syc_cp:
				sycCP();
				break;
			case R.id.ll_syc_dis:
				sycDIS();
				break;

			default:
				break;
			}
		}
	};
	
	private void sendInitDataBroadcastReceiver(){
		Intent intent = new Intent(Constants.BROADCAST_ORDER3_REFRASH_PRODUCT);
		sendBroadcast(intent);
	}
	
	private void sendRefrashStoreBroadcastReceiver(){
		Intent intent = new Intent(Constants.BROADCAST_ORDER3_REFRASH_STORE);
		sendBroadcast(intent);
	}
}
