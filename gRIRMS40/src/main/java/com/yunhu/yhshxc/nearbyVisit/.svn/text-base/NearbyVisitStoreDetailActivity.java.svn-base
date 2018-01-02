package com.yunhu.yhshxc.nearbyVisit;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.location.LocationMaster;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.nearbyVisit.view.NearbyVisitStoreDetailItem;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.SharedPreferencesUtilForNearby;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import gcg.org.debug.JLog;

public class NearbyVisitStoreDetailActivity extends AbsBaseActivity implements ReceiveLocationListener{
	
	//激活key
	public static final String KEY = "XB111-20120131-03-Z-F-A11111";
	//应用跟目录
	private static String mAppPath = null;
	//应用名
	private static String mAppName = null;
	//dpi
	private static int mDensityDpi = 0; 
	
	private TextView tv_nearby_store_title;
	private ImageView iv_nearby_visit_location;
	private LinearLayout ll_nearby_store_info;
	private LinearLayout ll_nearby_visit_detail_btn_data;
	private LinearLayout ll_nearby_visit_detail_btn_history;
	private LinearLayout ll_nearby_visit_detail_btn_store_info;
	private LinearLayout ll_nearby_visit_detail_btn_visit;
	private TextView tv_nearby_visit_detail_btn_data;
	private TextView tv_nearby_visit_detail_btn_history;
	private TextView tv_nearby_visit_detail_btn_store_info;
	private TextView tv_nearby_visit_detail_btn_visit;
	private int storeId;//当前店面ID
	private String patchId;//数据ID
	private String storeName;//店面名称
	private String menuId;//模块ID
	private String storeInfoData;//店面属性json数据
	private double storeLat,storeLon;//店面经纬度
	
	private LinearLayout ll_nearby_store_info_location,ll_nearby_store_info_contacts,ll_nearby_store_info_contacts_line;
	private TextView tv_nearby_store_info_location,tv_nearby_store_info_contacts;
	
	private final int LOCATION_TYPE_MAP = 1;//跳转到地图定位 
	private final int LOCATION_TYPE_STORE = 2; //初始化店面位置定位
	private final int LOCATION_TYPE_LOCATION=3;//仅仅定位获取当前位置
	private int locationType;//定位类型
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_visit_store_detail_activity);
		getIntentData();
		initModuleList();
		initWidget();
		initStoreInfo(storeInfoData);
		registerReceiver(receive, new IntentFilter(Constants.BROADCAST_ACTION_NEARBY_COPY));
		initKey();
	}
	
	private void initKey() {
		// TODO Auto-generated method stub
		// 设置应用程序数据根目录
				// 存放包括导航离线数据，资源数据，运行是的临时数据文件等
		mAppPath = Constants.SDCARD_PATH +"/mapbar/app";
		
		// 应用程序名称，可随意设置
		mAppName = "GRIRMS4.0";
		// 获取屏幕对应的DPI
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		mDensityDpi = dm.densityDpi;
//		NativeEnvironmentInit(mAppName,KEY);
	}

//	public void NativeEnvironmentInit(String appName, String key) {
//		NativeEnvParams params = new NativeEnvParams(mAppPath, appName,
//				mDensityDpi, key, new NativeEnv.AuthCallback() {
//			@Override
//			public void onDataAuthComplete(int errorCode) {
//				Auth.getInstance().setListener(new Auth.Listener() {
//					@Override
//					public void onCompletion(int type, int code) {
//					}
//				});
//				Auth.getInstance().updateLicense();
//
//			}
//
//			@Override
//			public void onSdkAuthComplete(int errorCode) {
//
//			}
//		});
//		NativeEnv.init(getApplicationContext(), params);
//		WorldManager.getInstance().init();
//	}
	private void getIntentData(){
		storeInfoData = getIntent().getStringExtra("storeInfoData");
		menuId = getIntent().getStringExtra("menuId");
	}
	
	/**
	 * 初始化店面地址
	 */
	private void initStoreLocation(String location){
		if (storeLat > 0 && storeLon > 0) {
			if (TextUtils.isEmpty(location)) {
				tv_nearby_store_info_location.setText(setString(R.string.nearby_visit_13));
			}else{
				tv_nearby_store_info_location.setText(location);
			}
			iv_nearby_visit_location.setVisibility(View.GONE);
		}else{
			iv_nearby_visit_location.setVisibility(View.VISIBLE);
			tv_nearby_store_info_location.setText(setString(R.string.nearby_visit_14));
		}
		ll_nearby_store_info_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				location(LOCATION_TYPE_MAP);
			}
		});
	}
	
	/**
	 * 初始化联系人
	 */
	private void initContacts(String contacts){
		if (TextUtils.isEmpty(contacts)) {
			ll_nearby_store_info_contacts.setVisibility(View.GONE);
			ll_nearby_store_info_contacts_line.setVisibility(View.GONE);
		}else{
			ll_nearby_store_info_contacts.setVisibility(View.VISIBLE);
			ll_nearby_store_info_contacts_line.setVisibility(View.VISIBLE);
			tv_nearby_store_info_contacts.setText(contacts);
		}
	}
	
	private void initStoreInfo(String storeInfoData){
		JLog.d(TAG, storeInfoData);
		try {
			JSONArray array = new JSONArray(storeInfoData);
			if (array!=null && array.length()>0) {
				JSONArray infoArr = array.getJSONArray(0);
				for (int i = 0; i < infoArr.length(); i++) {
					if (i==0) {
						Object obj = infoArr.get(i);
						if (obj!=null && obj instanceof Long) {
							Long l = infoArr.getLong(i);
							if (l!=null) {
								patchId = String.valueOf(infoArr.getLong(i));
							}
						}
					}else if(i==1){
						storeId = infoArr.getInt(i);
					}else if(i==2){
						String info = infoArr.getString(i);
						String[] str = info.split("\\~\\$\\$");
						if (str!=null) {
							if (str.length == 2) {//title: 店面名称
								storeName = str[1];
								setTitle(storeName);
								initStoreLocation("");
								initContacts("");
							}else if(str.length == 4){// title : 店面名称 lon lat
								storeName = str[1];
								setTitle(storeName);
								if (!TextUtils.isEmpty(str[3]) && !TextUtils.isEmpty(str[2])) {
									setStroeLatLon(Double.parseDouble(str[3]), Double.parseDouble(str[2]));
								}
								initStoreLocation("");
								initContacts("");
							}else if(str.length == 5){// title : 店面名称 lon lat 地址
								storeName = str[1];
								setTitle(storeName);
								if (!TextUtils.isEmpty(str[3]) && !TextUtils.isEmpty(str[2])) {
									setStroeLatLon(Double.parseDouble(str[3]), Double.parseDouble(str[2]));
								}
								if (!TextUtils.isEmpty(str[4])) {
									initStoreLocation(str[4]);
								}else{
									initStoreLocation("");
								}
							}else if(str.length == 6){// title : 店面名称 lon lat 地址 联系人
								storeName = str[1];
								setTitle(storeName);
								if (!TextUtils.isEmpty(str[3]) && !TextUtils.isEmpty(str[2])) {
									setStroeLatLon(Double.parseDouble(str[3]), Double.parseDouble(str[2]));
								}
								if (!TextUtils.isEmpty(str[4])) {
									initStoreLocation(str[4]);
								}else{
									initStoreLocation("");
								}
								if (!TextUtils.isEmpty(str[5])) {
									initContacts(str[5]);
								}else{
									initContacts("");
								}
							}
						}else{
							setTitle("");
						}
					}else{
						String info = infoArr.getString(i);
						String[] str = info.split("\\~\\$\\$");
						NearbyVisitStoreDetailItem item = new NearbyVisitStoreDetailItem(this);
						if (str!=null && str.length==2) {
							item.setData(str[0], str[1]);
						}else{
							item.setData(str[0], "");
						}
						ll_nearby_store_info.addView(item.getView());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 设置标题
	 * @param name
	 */
	private void setTitle(String name){
		tv_nearby_store_title.setText(name);
	}
	
	/**
	 * 设置店面经纬度
	 * @param lat
	 * @param lon
	 */
	private void setStroeLatLon(double lat,double lon){
		this.storeLat = lat;
		this.storeLon = lon;
	}
		
	private void initWidget(){
		tv_nearby_store_title = (TextView)findViewById(R.id.tv_nearby_store_title);
		iv_nearby_visit_location = (ImageView)findViewById(R.id.iv_nearby_visit_location);
		iv_nearby_visit_location.setOnClickListener(listener);

		ll_nearby_store_info = (LinearLayout)findViewById(R.id.ll_nearby_store_info);
		ll_nearby_visit_detail_btn_data = (LinearLayout)findViewById(R.id.ll_nearby_visit_detail_btn_data);
		ll_nearby_visit_detail_btn_data.setOnClickListener(listener);
		ll_nearby_visit_detail_btn_history = (LinearLayout)findViewById(R.id.ll_nearby_visit_detail_btn_history);
		ll_nearby_visit_detail_btn_history.setOnClickListener(listener);
		ll_nearby_visit_detail_btn_store_info = (LinearLayout)findViewById(R.id.ll_nearby_visit_detail_btn_store_info);
		ll_nearby_visit_detail_btn_store_info.setOnClickListener(listener);
		ll_nearby_visit_detail_btn_visit = (LinearLayout)findViewById(R.id.ll_nearby_visit_detail_btn_visit);
		ll_nearby_visit_detail_btn_visit.setOnClickListener(listener);
		tv_nearby_visit_detail_btn_data = (TextView)findViewById(R.id.tv_nearby_visit_detail_btn_data);
		tv_nearby_visit_detail_btn_history = (TextView)findViewById(R.id.tv_nearby_visit_detail_btn_history);
		tv_nearby_visit_detail_btn_store_info = (TextView)findViewById(R.id.tv_nearby_visit_detail_btn_store_info);
		tv_nearby_visit_detail_btn_visit = (TextView)findViewById(R.id.tv_nearby_visit_detail_btn_visit);
		
		tv_nearby_visit_detail_btn_data.setText(SharedPreferencesUtilForNearby.getInstance(this).getNearbyBtnMdl(menuId));
		tv_nearby_visit_detail_btn_history.setText(SharedPreferencesUtilForNearby.getInstance(this).getNearbyBtnHistry(menuId));
		tv_nearby_visit_detail_btn_store_info.setText(SharedPreferencesUtilForNearby.getInstance(this).getNearbyBtnStore(menuId));
		tv_nearby_visit_detail_btn_visit.setText(SharedPreferencesUtilForNearby.getInstance(this).getNearbyBtnVisit(menuId));
		
		ll_nearby_store_info_location = (LinearLayout)findViewById(R.id.ll_nearby_store_info_location);
		ll_nearby_store_info_contacts = (LinearLayout)findViewById(R.id.ll_nearby_store_info_contacts);
		ll_nearby_store_info_contacts_line = (LinearLayout)findViewById(R.id.ll_nearby_store_info_contacts_line);
		tv_nearby_store_info_location = (TextView)findViewById(R.id.tv_nearby_store_info_location);
		tv_nearby_store_info_contacts = (TextView)findViewById(R.id.tv_nearby_store_info_contacts);
		
		
		//显示拜访数据
		if (visitBtnShowControl()) {
			ll_nearby_visit_detail_btn_visit.setVisibility(View.VISIBLE);
		}
		//显示数据模板
		ll_nearby_visit_detail_btn_data.setVisibility(View.GONE);
		//显示历史数据
		if (histryBtnShowControl()) {
			ll_nearby_visit_detail_btn_history.setVisibility(View.VISIBLE);
		}
		//显示店面信息
		if (!TextUtils.isEmpty(SharedPreferencesUtilForNearby.getInstance(this).getNearbyStoreInfo(menuId))) {
			ll_nearby_visit_detail_btn_store_info.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 历史信息按钮控制
	 */
	private boolean visitBtnShowControl(){
		boolean flag = false;
		if (!moduleList.isEmpty()) {
			for (int i = 0; i < moduleList.size(); i++) {
				Module module = moduleList.get(i);
				int type = module.getType();
				if (type == Constants.MODULE_TYPE_REPORT) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 历史信息按钮控制
	 */
	private boolean histryBtnShowControl(){
		boolean flag = false;
		if (!moduleList.isEmpty()) {
			for (int i = 0; i < moduleList.size(); i++) {
				Module module = moduleList.get(i);
				int type = module.getType();
				if (type == Constants.MODULE_TYPE_QUERY || type == Constants.MODULE_TYPE_VERIFY || type == Constants.MODULE_TYPE_UPDATE) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 初始化Module
	 */
	private List<Module> moduleList = null;
	private List<Module> initModuleList(){
		moduleList = new ModuleDB(this).findModuleByMenuId(Integer.parseInt(menuId));
		return moduleList;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_nearby_visit_location:
				location(LOCATION_TYPE_STORE);
				break;
			case R.id.ll_nearby_visit_detail_btn_data:
				dataStencil();
				break;
			case R.id.ll_nearby_visit_detail_btn_history:
				showHistoryPopup();
				break;
			case R.id.ll_nearby_visit_detail_btn_store_info:
				storeInfoControl();
				break;
			case R.id.ll_nearby_visit_detail_btn_visit:
				visit();
				break;


			default:
				break;
			}
		}
	};
	
	/**
	 * 查看位置
	 */
	private Dialog locationLoadingDialog;
	private void location(int locType){
		locationType = locType;
		locationLoadingDialog  = new MyProgressDialog(this,R.style.CustomProgressDialog,setString(R.string.nearby_visit_05));
		locationLoadingDialog.show();
//		new LocationFactory(this).startNewLocation(this,true);
		new LocationFactoy(this, this).startLocationHH();
	}

	
		

   

	/**
	 * 数据模板
	 */
	private void dataStencil(){
		
	}
	
	/**
	 * 历史数据
	 */
	private void historyStencil(int type){
		Bundle bundle = new Bundle();
		bundle.putInt("storeId", storeId);//店面ID
		bundle.putString("patchId", patchId);//店面ID
		bundle.putInt("targetId", Integer.parseInt(menuId));//数据ID
		bundle.putString("storeName", storeName);//店面名称
		bundle.putInt("menuType", Menu.TYPE_NEARBY);//菜单类型
		bundle.putInt("type", type);//类型
		Intent replenishIntent = new Intent(this, NearbyVisitHistoryActivity.class);
		replenishIntent.putExtra("bundle", bundle);
		startActivity(replenishIntent);
	}
	
	
	private void storeInfoControl(){
		String storeInfo = SharedPreferencesUtilForNearby.getInstance(this).getNearbyStoreInfo(menuId);
		if(!TextUtils.isEmpty(storeInfo)){
			if ("1".equals(storeInfo)) {//只有店面信息查看
				storeInfo(Func.IS_STORE_VIEW);
			}else if("2".equals(storeInfo)){//只有店面信息修改
				storeInfo(Func.IS_STORE_UPDATE);
			}else{//既有店面信息查看也有店面信息修改
				showStoreInfo();
			}
		}
	}
	
	/**
	 * 店面属性
	 */
	private void storeInfo(int type){
		Intent intent = new Intent(this,NearbyVisitStoreInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("storeId", storeId);
		bundle.putInt("targetId", Integer.parseInt(menuId));
		bundle.putString("storeName", storeName);
		bundle.putInt("storeInfo", type);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}
	
	/**
	 * 拜访
	 */
	private void visit(){
		Intent intent = new Intent(this,NearbyVisitFuncActivity.class);
		Bundle bundle = new Bundle();
		 //存放targetId
		bundle.putInt("targetId", Integer.parseInt(menuId));
		 //存放storeId
		bundle.putInt("storeId", storeId);
		bundle.putString("storeName", storeName);
		bundle.putString("patchId", patchId);
		bundle.putInt("menuType", Menu.TYPE_NEARBY);//菜单类型   之前没有传，单独添加，因为EditTextFunc插入数据是需要menuType去查找submitId
		// 在intent中放入bundle
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 显示历史信息数据
	 */
	public void showHistoryPopup() {
		View contentView = View.inflate(this, R.layout.nearby_visit_history_popupwindow2, null);
		Button quertBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_history_pup_search);
		Button updateBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_history_pup_update);
		Button verifyBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_history_pup_verify);
		
		for (int i = 0; i < moduleList.size(); i++) {
			Module module = moduleList.get(i);
			int type = module.getType();
			if (type == Constants.MODULE_TYPE_QUERY) {//查询
				quertBtn.setVisibility(View.VISIBLE);
				continue;
			}
			if (type == Constants.MODULE_TYPE_VERIFY) {//审核
				verifyBtn.setVisibility(View.VISIBLE);
				continue;
			}
			if (type == Constants.MODULE_TYPE_UPDATE) {//修改
				updateBtn.setVisibility(View.VISIBLE);
				continue;
			}
		}
		
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		popupWindow.showAtLocation(((Activity) this).findViewById(R.id.ll_nearby_bottom), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		quertBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				historyStencil(Constants.MODULE_TYPE_QUERY);
				popupWindow.dismiss();
			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				historyStencil(Constants.MODULE_TYPE_UPDATE);
				popupWindow.dismiss();
			}
		});
		verifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				historyStencil(Constants.MODULE_TYPE_VERIFY);
				popupWindow.dismiss();
			}
		});
		
	}
	
	
	public void showStoreInfo() {
		View contentView = View.inflate(this, R.layout.nearby_visit_storeinfo, null);
		Button quertBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_check);
		Button updateBtn = (Button)contentView.findViewById(R.id.btn_nearby_visit_store_info_update);
		
		final PopupWindow popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();//更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个默认的背景
		popupWindow.showAtLocation(((Activity) this).findViewById(R.id.ll_nearby_bottom), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
		quertBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				storeInfo(Func.IS_STORE_VIEW);
				popupWindow.dismiss();
			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				storeInfo(Func.IS_STORE_UPDATE);
				popupWindow.dismiss();
			}
		});
	}

	@Override
	public void onReceiveResult(LocationResult result) {
		if (locationLoadingDialog!=null && locationLoadingDialog.isShowing()) {
			locationLoadingDialog.dismiss();
		}
		if (result!=null && result.isStatus()) {
			if (locationType == LOCATION_TYPE_MAP) {
				double lat = result.getLatitude();
				double lon = result.getLongitude();
				String adress = result.getAddress();
				Intent intent = new Intent(this, NearbyVisitStoreMapForBDActivity.class);
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.putExtra("adress", adress);
				intent.putExtra("storeName", storeName);
				intent.putExtra("storeLat", storeLat);
				intent.putExtra("storeLon", storeLon);
				startActivity(intent);
			}else if(locationType == LOCATION_TYPE_STORE){
				try {
					storeLocationDialog(result);
				} catch (Exception e) {
					JLog.e(e);
					ToastOrder.makeText(NearbyVisitStoreDetailActivity.this, setString(R.string.nearby_visit_16), ToastOrder.LENGTH_LONG).show();
				}
				
			}
			
		}else{
			ToastOrder.makeText(this, setString(R.string.nearby_visit_15), ToastOrder.LENGTH_SHORT).show();
		}
		locationType = 0;
	}
	
	/**
	 * 初始店面位置定位信息展示
	 * @param locationResult
	 */
	public void storeLocationDialog(final LocationResult locationResult) {
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.init_nearby_store_location_dialog, null);
		LinearLayout confirm = (LinearLayout) view.findViewById(R.id.ll_location_dialog_confirm);
		LinearLayout newLocation = (LinearLayout) view.findViewById(R.id.ll_location_dialog_newlocation);
		LinearLayout cancel = (LinearLayout) view.findViewById(R.id.iv_location_dialog_cancle);
		TextView locationContent = (TextView) view.findViewById(R.id.tv_location_dialog_content);
		StringBuffer buffer = new StringBuffer();
		if (locationResult != null) {
			buffer.append("\n").append(setString(R.string.nearby_visit_17)).append(locationResult.getLocType());
		} 
		String adress = locationResult.getAddress();
		if (TextUtils.isEmpty(adress)) {
			locationContent.setText(getResources().getString(R.string.location_success_no_addr) + buffer.toString());
		}else{
			locationContent.setText(adress + buffer.toString());
		}

		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				submitStoreLocation(locationResult);
			}
		});
		newLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				location(LOCATION_TYPE_STORE);
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	/**
	 * 提交采集的定位结果
	 * @param result
	 */
	private void submitStoreLocation(final LocationResult result){
		final Dialog dialog = new MyProgressDialog(this,R.style.CustomProgressDialog,setString(R.string.nearby_visit_18));
		RequestParams params = new RequestParams();
		params.put("storeid", String.valueOf(storeId));
		params.put("lon", result.getLongitude());
		params.put("lat", result.getLatitude());
		params.put("address", result.getAddress());
		params.put("action", LocationMaster.ACTION);
		params.put("version", LocationMaster.VERSION);
		params.put("status", result.getPosType());
		params.put("acc", result.getRadius());

		GcgHttpClient.getInstance(this).post(UrlInfo.doStoreAddressInfo(this), params, new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					String  resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						setStroeLatLon(result.getLatitude(), result.getLongitude());
						initStoreLocation(result.getAddress());
					}else{
						ToastOrder.makeText(NearbyVisitStoreDetailActivity.this, R.string.toast_one7, ToastOrder.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					ToastOrder.makeText(NearbyVisitStoreDetailActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onStart() {
				if (dialog!=null && !dialog.isShowing()) {
					dialog.show();
				}
			}
			
			@Override
			public void onFinish() {
				if (dialog!=null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				submitStoreLocationFail(result);
			}
		});
	}
	
	/**
	 * 
	 * @param result
	 * isDelete true表示要删除数据
	 */
	public void submitStoreLocationFail(final LocationResult result) {
		final Dialog dialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.submit_store_loc_fail, null);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				submitStoreLocation(result);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("patchId", patchId);
		outState.putString("storeName", storeName);
		outState.putString("menuId", menuId);
		outState.putString("storeInfoData", storeInfoData);
		outState.putInt("storeId", storeId);
		outState.putDouble("storeLat", storeLat);
		outState.putDouble("storeLon", storeLon);
		outState.putInt("locationType", locationType);
	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receive);
//		WorldManager.getInstance().cleanup();
//		NativeEnv.cleanup();
		super.onDestroy();
	}
	
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		patchId = savedInstanceState.getString("patchId");
		storeName = savedInstanceState.getString("storeName");
		menuId = savedInstanceState.getString("menuId");
		storeInfoData = savedInstanceState.getString("storeInfoData");
		storeId = savedInstanceState.getInt("storeId");
		storeLat = savedInstanceState.getDouble("storeLat");
		storeLon = savedInstanceState.getDouble("storeLon");
		locationType = savedInstanceState.getInt("locationType");
	}
	
	
	/**
	 * 如果查询列表页面用户选择了复制按钮或者修改审核按钮的话就会发送广播，这时候要把这个页面给关闭掉
	 */
	private BroadcastReceiver receive = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			NearbyVisitStoreDetailActivity.this.finish();
		}
	};

	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
