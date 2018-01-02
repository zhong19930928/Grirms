package com.yunhu.yhshxc.activity.carSales.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.parser.OrgParser;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.URLWrapper;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSalesDataManager {
	private final String TAG = "CarSalesDataManager";
	private Context context;
	private CarSalesUtil carSalesUtil = null;
	public CarSalesDataManager(Context context) {
		this.context = context;
		carSalesUtil = new CarSalesUtil(context);
	}
	
	
	/**
	 * 未上报车销数据
	 */
	public void unSubmitData(){
		Intent intent  = new Intent(context, CarSalesSubmitManagerActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 同步促销信息
	 */
	private Dialog synDialog = null;
	public void sycCX(){
		int isPromotion = SharedPreferencesForCarSalesUtil.getInstance(context).getIsPromotion();
		if (isPromotion == 2) {
			synDialog =  new MyProgressDialog(context,R.style.CustomProgressDialog,"正在同步促销信息...");
			String url = UrlInfo.carPromotionInfo(context);
			GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					try {
//						JLog.d("aaa",content);
						JSONObject obj = new JSONObject(content);
						String resultCode = obj.getString("resultcode");
						if ("0000".equals(resultCode)) {
							JSONArray array = obj.getJSONArray("car_promotion");
							new CarSalesParse(context).parserProductPromotion(array);
							sendInitDataBroadcastReceiver();
							Intent intent = new Intent(context,CarSalesSyncInfoActivity.class);
							context.startActivity(intent);
						}else{
							throw new Exception();
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastOrder.makeText(context, "同步数据异常", ToastOrder.LENGTH_SHORT).show();
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
					ToastOrder.makeText(context, "促销信息同步失败", ToastOrder.LENGTH_SHORT).show();
				}
			});
		}else{
			ToastOrder.makeText(context, "该用户不享受促销", ToastOrder.LENGTH_SHORT).show();
		}
	
	}
	
	/**
	 * 查看促销
	 */
	public void seeCX(){
		int isPromotion = SharedPreferencesForCarSalesUtil.getInstance(context).getIsPromotion();
		if (isPromotion ==2) {
			Intent intent = new Intent(context,CarSalesSyncInfoActivity.class);
			context.startActivity(intent);
		}else{
			ToastOrder.makeText(context, "该用户不享受促销", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 同步产品
	 */
	private Dialog synCPDialog = null;
	public void sycCP(){
		synCPDialog =  new MyProgressDialog(context,R.style.CustomProgressDialog,"正在同步配置信息...");
		String url = UrlInfo.carConfInfo(context);
		GcgHttpClient.getInstance(context).get(url, null, new HttpResponseListener() {
			
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
	 * 解析配置信息
	 * @param content
	 */
	public void sycCP(final String content){
		
		new Thread(){
			public void run() {
				try {
					JSONObject object = new JSONObject(content);
					new CarSalesParse(context).parserAll(object);
					new CarSalesUtil(context).initCarSalesProductCtrl();
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
				ToastOrder.makeText(context, "配置信息同步成功", ToastOrder.LENGTH_SHORT).show();
				break;
			case 2:
				ToastOrder.makeText(context, "配置数据异常", ToastOrder.LENGTH_SHORT).show();
				break;
			case 3:
				ToastOrder.makeText(context, "配置同步失败", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	
	/**
	 * 同步客户信息
	 */
	private Dialog sycKHDialog=null;
	public void sycKH(){
		sycKHDialog =  new MyProgressDialog(context,R.style.CustomProgressDialog,"正在同步客户信息...");
		URLWrapper wrapper = new URLWrapper(UrlInfo.getUrlInitOperation(context));
		wrapper.addParameter("operation", Func.ORG_STORE);
		String url = wrapper.getRequestURL();
		GcgHttpClient.getInstance(context).get(url,null, new HttpResponseListener() {

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
				ToastOrder.makeText(context, "客户信息同步失败", ToastOrder.LENGTH_SHORT).show();
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
					new OrgParser(context).parseAll(content);
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
				ToastOrder.makeText(context, "客户同步成功", ToastOrder.LENGTH_SHORT).show();
				break;
			case 2:
				ToastOrder.makeText(context, "客户数据异常", ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 费用报销
	 */
	public void applyForReimbursement(){
		Intent intent = new Intent(context, ApplyForReimbursementActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 卖货,退货,装车,卸车,补货申请,缺货登记，库存盘点
	 */
	public void submit(CarSales sales,String url){
		HashMap<String, String> params = submitParams(sales);
//		JLog.d(TAG, "params:"+params!=null?params.toString():"null");
		if (params!=null) {
			submitDataBackground(sales, params,url);
			if (!sellPhotoList.isEmpty()) {
				for (int i = 0; i < sellPhotoList.size(); i++) {
					String name = sellPhotoList.get(i);
					submitPhotoBackground(sales,name);
				}
			}
			SubmitWorkManager.getInstance(context).commit();
		}else{
			ToastOrder.makeText(context, "请选择产品", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * 提交车销数据
	 * @param sales
	 * @param params
	 */
	private void submitDataBackground(CarSales sales,HashMap<String, String> params,String url){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent("车销:"+sales.getCarSalesNo());
		vo.setTitle("车销数据");
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_DATA);
		vo.setUrl(url);
		vo.setParams(params);
		SubmitWorkManager.getInstance(context).performJustSubmit(vo);
	}
	
	/**
	 * 提交车销图片
	 * @param name
	 */
	private void submitPhotoBackground(CarSales sales,String name){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent("车销:"+sales.getCarSalesNo());
		vo.setTitle("车销图片");
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setUrl(UrlInfo.getUrlPhoto(context));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("companyid", String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
		params.put("md5Code", MD5Helper.getMD5Checksum2(Constants.CARSALES_PATH + name));
		vo.setParams(params);
		vo.setImagePath(Constants.CARSALES_PATH + name);
		SubmitWorkManager.getInstance(context).performImageUpload(vo);
	}
	
	
	private List<String> sellPhotoList = new ArrayList<String>();//图片集合
	private HashMap<String, String> submitParams(CarSales sales){
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			if (sales == null) {
				return null;
			}
			String image1 = sales.getImage1();
			String image2 = sales.getImage2();
			if (!TextUtils.isEmpty(image1)) {
				sellPhotoList.add(image1);
			}
			if (!TextUtils.isEmpty(image2)) {
				sellPhotoList.add(image2);
			}
			List<CarSalesProductData> listData = sales.getProductList();
			if (listData == null ) {
				return null;
			}
			List<CarSales> orderList = new ArrayList<CarSales>();
			orderList.add(sales);
			String data = carSalesUtil.submitJson(orderList);
			params.put("phoneno", PublicUtils.receivePhoneNO(context));
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(context, "车销数据异常", ToastOrder.LENGTH_SHORT).show();
		}
		return params;
	}
	
	/**
	 * 库存盘点和缺货登记
	 */
	public void submitStockTaking(String url){
		if (url.equals(UrlInfo.doStockInfo(context))) {
			
		}
	}
	
	/**
	 * 刷新产品通知
	 */
	private void sendInitDataBroadcastReceiver(){
		Intent intent = new Intent(Constants.BROADCAST_CAR_SALES_REFRASH_PRODUCT);
		context.sendBroadcast(intent);
	}
	
	/**
	 * 刷新店面通知
	 */
	private void sendRefrashStoreBroadcastReceiver(){
		Intent intent = new Intent(Constants.BROADCAST_CAR_SALES_REFRASH_STORE);
		context.sendBroadcast(intent);
	}
	
}
