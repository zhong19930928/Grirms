package com.yunhu.yhshxc.activity.carSales.scene.chargeArrears;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.Arrears;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.print.CarSalesPrintForQianKuan;
import com.yunhu.yhshxc.activity.carSales.scene.view.ChargeArrearsItemView.OnCheckedIdOver;
import com.yunhu.yhshxc.activity.carSales.scene.view.ChargeArrearsView;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesPopupWindow;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CarSalesParse;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loopj.android.http.RequestParams;

/**
 * 收取钱款
 * 
 * @author xuelinlin
 * 
 */
public class ChargeArrearsActivity extends AbsBaseActivity implements
		OnCheckedIdOver,DataSource {
	private EditText et_tuikuan;
	private LinearLayout btn_submit;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_location;
	private LinearLayout ll_print;
	private LinearLayout ll_store;
	private LinearLayout ll_shou;
	private TextView tv_line;
	private PullToRefreshListView lv_charge_arrears;
	private ChargeArrearsAdapter adapter;
	private Map<Integer,Arrears> list = new HashMap<Integer,Arrears>();
	private CarSalesUtil carSalesUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_sales_charge_arrears);
		carSalesUtil = new CarSalesUtil(this);
		SharedPreferencesForCarSalesUtil.getInstance(ChargeArrearsActivity.this).clearPhotoName();
		initWidget();
		initParseData();
	}
	// 是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint() {
		int isPrint = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsReceivables());
		int isPhoto = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsReceivablesPhoto());
		if(isPrint == 1 &&isPhoto == 1){
			ll_print.setVisibility(View.GONE);
			ll_take_photo.setVisibility(View.INVISIBLE);
		}else if(isPrint == 1 &&isPhoto == 2){
			ll_print.setVisibility(View.GONE);
			ll_take_photo.setVisibility(View.VISIBLE);
		}else if(isPrint == 2 &&isPhoto == 1){
			ll_print.setVisibility(View.VISIBLE);
			ll_take_photo.setVisibility(View.GONE);
		}else if(isPrint == 2 &&isPhoto == 2){
			ll_print.setVisibility(View.VISIBLE);
			ll_take_photo.setVisibility(View.VISIBLE);
		}
	}
	private void initWidget() {
		lv_charge_arrears = (PullToRefreshListView) findViewById(R.id.lv_charge_arrears);
		et_tuikuan = (EditText) findViewById(R.id.et_tuikuan);
		btn_submit = (LinearLayout) findViewById(R.id.btn_submit);
		ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photos);
		ll_print = (LinearLayout) findViewById(R.id.ll_print);
		ll_store = (LinearLayout) findViewById(R.id.ll_store);
		ll_shou = (LinearLayout) findViewById(R.id.ll_shou);
		ll_location = (LinearLayout) findViewById(R.id.ll_location);
		tv_line = (TextView) findViewById(R.id.tv_line);
		btn_submit.setOnClickListener(listner);
		ll_take_photo.setOnClickListener(listner);
		ll_print.setOnClickListener(listner);
		et_tuikuan.addTextChangedListener(watcher);
		adapter = new ChargeArrearsAdapter(ChargeArrearsActivity.this,sales);
		lv_charge_arrears.setAdapter(adapter);
		String label = DateUtils.formatDateTime(this.getApplicationContext(), System.currentTimeMillis(),
				   DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		lv_charge_arrears.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		lv_charge_arrears.setMode(Mode.PULL_FROM_END);
		lv_charge_arrears.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					refresh();
				}
			}
		});
		
	}

	private Dialog searchDialog = null;

	private void initParseData() {
		searchDialog = new MyProgressDialog(this, R.style.CustomProgressDialog,
				"正在加载...");
		String url = UrlInfo.carBalanceInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("aaa", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								parserSearchThread(content);
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							searchHandler.sendEmptyMessage(3);
						}
					}

					@Override
					public void onStart() {
						if (searchDialog != null && !searchDialog.isShowing()) {
							searchDialog.show();
						}
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Throwable error, String content) {
						searchHandler.sendEmptyMessage(4);
					}
				});
	}
	int page = 1;
	private RequestParams params() {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		params.put("carId", SharedPreferencesForCarSalesUtil.getInstance(this)
				.getCarId());
		params.put("storeName", SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreName());
		params.put("storeId", SharedPreferencesForCarSalesUtil.getInstance(this).getStoreId());
		params.put("page", page < 1 ? 1:page);
		JLog.d(TAG, "params:"+params.toString());
		return params;
	}

	List<CarSales> sales = new ArrayList<CarSales>();

	/**
	 * 解析查询到的数据
	 * 
	 * @param contnet
	 */
	private void parserSearchThread(final String content) {
		new Thread() {
			public void run() {
				try {
					sales = new CarSalesParse(ChargeArrearsActivity.this).parserChargeArrearsCarSales(content, true);
					list.clear();
					if (sales.isEmpty()) {
						searchHandler.sendEmptyMessage(2);
					} else {
						for (int i = 0; i < sales.size(); i++) {
							for(int j = 0; j<sales.get(i).getArrears().size();j++){
								list.put(sales.get(i).getArrears().get(j).getId(), sales.get(i).getArrears().get(j));
							}
							
						}
						searchHandler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					searchHandler.sendEmptyMessage(3);
				}
			};
		}.start();
	}

	private Handler searchHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (searchDialog != null && searchDialog.isShowing()) {
				searchDialog.dismiss();
			}
			switch (what) {
			case 1:// 解析成功
				adapter.refresh(sales);
				ll_store.setVisibility(View.VISIBLE);
				ll_shou.setVisibility(View.VISIBLE);
				btn_submit.setVisibility(View.VISIBLE);
				tv_line.setVisibility(View.VISIBLE);
				ll_location.setVisibility(View.VISIBLE);
				controlPrint();
				break;
			case 2:
				ToastOrder.makeText(ChargeArrearsActivity.this, "没有欠款",
						ToastOrder.LENGTH_LONG).show();
				break;
			case 3:
				ToastOrder.makeText(ChargeArrearsActivity.this, R.string.ERROR_DATA,
						ToastOrder.LENGTH_LONG).show();
				break;
			case 4:
				ToastOrder.makeText(ChargeArrearsActivity.this, R.string.reload_failure,
						ToastOrder.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (isChange) {
				isChange = false;
			} else {
				list.clear();
				refreshAllView(s);
			}

		}
	};

	private void refreshAllView(Editable s) {
		String ss = s.toString();
		if (!sales.isEmpty() && sales.size() > 0) {
			map.clear();
			if(!TextUtils.isEmpty(ss)){
	    		if(ss.equals(".")){
	    			ss = "0";
				}else if (ss.startsWith(".")) {
					ss = "0"+ss;
				}else if(ss.endsWith(".")){
					ss = ss+"0";
				}
	    	}else{
	    		ss = "0";
	    	}
			adapter.setIn(ss);
			adapter.notifyDataSetChanged();
		}
	}
	Map<Integer, String> map = new HashMap<Integer, String>();
	class ChargeArrearsAdapter extends BaseAdapter {
		private Context context;
		private List<CarSales> sales;
		private Map<Integer,ChargeArrearsView> views;
		private String in ;
		double subInput;
		public String getIn() {
			return in;
			
		}

		public void setIn(String in) {
			this.in = in;
			subInput = Double.parseDouble(TextUtils.isEmpty(in)?"0":in);
		}

		public ChargeArrearsAdapter(Context context, List<CarSales> sales) {
			this.context = context;
			this.sales = sales;
			views = new HashMap<Integer, ChargeArrearsView>();
		}

		@Override
		public int getCount() {
			return sales.size();
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
			ChargeArrearsView view = null;
			if (convertView == null) {
				view = new ChargeArrearsView(context,ChargeArrearsActivity.this);
				convertView = view.getView();
				convertView.setTag(view);
			} else {
				view = (ChargeArrearsView) convertView.getTag();
			}
			if(TextUtils.isEmpty(map.get(position))||map.get(position).equalsIgnoreCase("0")||map.get(position).equalsIgnoreCase("0.0")){
				if(position == sales.size()-1){
					view.setSubInput(subInput,true);
				}else{
					view.setSubInput(subInput,false);
				}
				carTotal(position);
			}else{
				double sub = Double.parseDouble(map.get(position));
				if(position == sales.size()-1){
					view.setSubInput(sub,true);
				}else{
					view.setSubInput(sub,false);
				}
			}
			view.initData(sales.get(position));
			return convertView;
		}
		
		
		
		public void carTotal(int position){
			if(TextUtils.isEmpty(map.get(position))||map.get(position).equalsIgnoreCase("0")||map.get(position).equalsIgnoreCase("0.0")){
				map.put(position, String.valueOf(subInput));
				CarSales car = sales.get(position);
				double carTotal = car.getUnPayAmount();
				double tempInput = subInput-carTotal;
				subInput = tempInput<=0 ? 0:tempInput;
			}
		}
		public void refresh(List<CarSales> list3) {
			sales = list3;
			notifyDataSetChanged();
		}

	}

	@Override
	public void onChecked(boolean isChecked, Arrears data) {
		if (list.containsKey(data.getId())) {
			Arrears d = list.get(data.getId());
			if (isChecked) {
				d.setIsOver(1);
			} else {
				d.setIsOver(0);
			}
			list.put(data.getId(), d);
		}else{
			if (isChecked) {
				data.setIsOver(1);
			} else {
				data.setIsOver(0);
			}
			list.put(data.getId(), data);
		}
	}

	@Override
	public void onSkChanged(Editable s, Arrears data) {
		String str = s.toString();
		if(!TextUtils.isEmpty(str)){
    		if(str.equals(".")){
    			str = "0";
			}else if (str.startsWith(".")) {
				str = "0"+str;
			}else if(str.endsWith(".")){
				str = str+"0";
			}
    	}else{
    		str = "0";
    	}
		if (list.containsKey(data.getId())) {
			Arrears d = list.get(data.getId());
			if (!TextUtils.isEmpty(str)) {
				d.setSkAmount(Double.parseDouble(str));
			} else {
				d.setSkAmount(0);
			}
			list.put(data.getId(), d);
		}else{
			if (!TextUtils.isEmpty(str)) {
				data.setSkAmount(Double.parseDouble(str));
			} else {
				data.setSkAmount(0);
			}
			list.put(data.getId(), data);
		}
	}

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_take_photos:
				takePhoto();
				break;
			case R.id.ll_print:
				print();
				break;
			case R.id.btn_submit:
				submit();
				break;
			default:
				break;
			}
		}
	};
	CarSalesPrintForQianKuan carSalesPrint;
	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(ChargeArrearsActivity.this, "car_sales_print_qiankuan.txt");
		try {
			carSalesPrint = new CarSalesPrintForQianKuan(this);
			carSalesPrint.setList(list);
			carSalesPrint.setMount(et_tuikuan.getText().toString());
			carSalesPrint.print(ChargeArrearsActivity.this, new JSONArray(json), ChargeArrearsActivity.this);
			int printCount = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount();
			SharedPreferencesForCarSalesUtil.getInstance(this).setCarSalesPrintCount(printCount+1);
		}catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
	}
	private void submit() {
		List<Arrears> listNew = new ArrayList<Arrears>();
		Set<Integer> set = list.keySet();
		Iterator<Integer> it = set.iterator();
		while(it.hasNext()){
			int key = it.next();
			listNew.add(list.get(key));
		}
		submitCarSalesBalance(listNew,UrlInfo.doCarBalanceInfo(ChargeArrearsActivity.this));
		SharedPreferencesForCarSalesUtil.getInstance(ChargeArrearsActivity.this).clearPhotoName();
		ChargeArrearsActivity.this.finish();
	}
	private CarSalesPopupWindow popupWindow;
	private void takePhoto() {
		popupWindow = new CarSalesPopupWindow(this);
		popupWindow.show(null,SharedPreferencesForCarSalesUtil.getInstance(ChargeArrearsActivity.this).getPhotoNameOne(),SharedPreferencesForCarSalesUtil.getInstance(ChargeArrearsActivity.this).getPhotoNameTwo());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 if (popupWindow!=null) {
			popupWindow.onActivityResult(requestCode, resultCode, data);
		 }
	}
	/**
	 * 收款记录数据
	 */
	@SuppressWarnings("unused")
	public void submitCarSalesBalance(List<Arrears> ears, String url) {
		HashMap<String, String> params = submitParamsBalance(ears);
		JLog.d("aaa", "params:" + params != null ? params.toString() : "null");
		if (params != null) {
			submitBalanceDataBackground(params, url);
			SubmitWorkManager.getInstance(ChargeArrearsActivity.this).commit();
			ToastOrder.makeText(ChargeArrearsActivity.this, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
		} else {
			ToastOrder.makeText(ChargeArrearsActivity.this, "没有可提交的数据",
					ToastOrder.LENGTH_SHORT).show();
		}
	}

	private void submitBalanceDataBackground(HashMap<String, String> params,
			String url) {
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent("收取欠款");
		vo.setTitle("车销数据");
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_DATA);
		vo.setUrl(url);
		vo.setParams(params);
		SubmitWorkManager.getInstance(ChargeArrearsActivity.this)
				.performJustSubmit(vo);
	}

	private HashMap<String, String> submitParamsBalance(List<Arrears> ears) {
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			if (ears == null) {
				return null;
			}
			String data = carSalesUtil.submitCarBalanceInfoJson(ears);
			params.put("phoneno",
					PublicUtils.receivePhoneNO(ChargeArrearsActivity.this));
			params.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(ChargeArrearsActivity.this, "车销数据异常",
					ToastOrder.LENGTH_SHORT).show();
		}
		return params;
	}

	double allMoneyPrice = 0;
	private boolean isChange = false;

	@Override
	public void onSingleChanged(boolean isSingle, CharSequence s) {
		if (et_tuikuan.hasFocus()) {
			isChange = false;
		} else {
			isChange = true;
			String price1 = s.toString();
	    	if(!TextUtils.isEmpty(price1)){
	    		if(price1.equals(".")){
	    			price1 = "0";
				}else if (price1.startsWith(".")) {
					price1 = "0"+price1;
				}else if(price1.endsWith(".")){
					price1 = price1+"0";
				}
	    	}else{
	    		price1 = "0";
	    	}
	    	double price = Double.parseDouble(price1);
			if (!isSingle) {
				String ss = et_tuikuan.getText().toString();
		    	if(!TextUtils.isEmpty(ss)){
		    		if(ss.equals(".")){
		    			ss = "0";
					}else if (ss.startsWith(".")) {
						ss = "0"+ss;
					}else if(ss.endsWith(".")){
						ss = ss+"0";
					}
		    	}else{
		    		ss = "0";
		    	}
				allMoneyPrice =  Double.parseDouble(ss);
				allMoneyPrice = allMoneyPrice - price;
			} else {
				et_tuikuan.setText(PublicUtils.formatDouble(allMoneyPrice
						+ price));
			}
		}

	}
	private void refresh() {
		page+=1;
		String url = UrlInfo.carBalanceInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject obj = new JSONObject(content);
					String resultcode = obj.getString("resultcode");
					if ("0000".equals(resultcode)) {
						parderRefreshThread(content);
					}else{
						throw new Exception();
					}
				} catch (Exception e) {
					refreshHandler.sendEmptyMessage(2);
				}
			}
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				lv_charge_arrears.onRefreshComplete();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				refreshHandler.sendEmptyMessage(3);
			}
		});
	}
	/**
	 * 解析刷新数据
	 * @param content
	 */
	private void parderRefreshThread(final String content){
		new Thread(){
			public void run() {
				try {
					List<CarSales> newOrderList =new CarSalesParse(ChargeArrearsActivity.this).parserChargeArrearsCarSales(content, false);
					if(newOrderList.size()>0){
						for (int i = 0; i < newOrderList.size(); i++) {
							for(int j = 0; j<newOrderList.get(i).getArrears().size();j++){
								list.put(newOrderList.get(i).getArrears().get(j).getId(), newOrderList.get(i).getArrears().get(j));
							}
							
						}
						sales.addAll(newOrderList);
						refreshHandler.sendEmptyMessage(1);
					}else{
						refreshHandler.sendEmptyMessage(4);
					}	
				} catch (Exception e) {
					refreshHandler.sendEmptyMessage(2);

				}
			};
		}.start();
	} 
	private Handler refreshHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				map.clear();
				String ss = et_tuikuan.getText().toString();
		    	if(!TextUtils.isEmpty(ss)){
		    		if(ss.equals(".")){
		    			ss = "0";
					}else if (ss.startsWith(".")) {
						ss = "0"+ss;
					}else if(ss.endsWith(".")){
						ss = ss+"0";
					}
		    	}else{
		    		ss = "0";
		    	}
				adapter.setIn(ss);
				adapter.refresh(sales);
				break;
			case 2:
				ToastOrder.makeText(ChargeArrearsActivity.this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 3:
				ToastOrder.makeText(ChargeArrearsActivity.this, R.string.reload_failure, ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;
			case 4:
				ToastOrder.makeText(ChargeArrearsActivity.this, "数据加载完毕", ToastOrder.LENGTH_SHORT).show();
				page-=1;
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void printLoop(List<Element> columns) {
		carSalesPrint.printLoop(columns);
	}

	@Override
	public void printRow(List<Element> columns) {
		carSalesPrint.printRow(columns);
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
	private String mapToString(Map<Integer, String> map) {
		String mapStr = null;
		if (map != null && !map.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				sb.append(";").append(entry.getKey() != null ? entry.getKey() : "").append(",").append(entry.getValue() != null ? entry.getValue() : "");
			}
			mapStr = sb.substring(1).toString();
		}
		return mapStr;
	}

	private Map<Integer, String> stringToMap(String value) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		if (!TextUtils.isEmpty(value)) {
			String[] mapList = value.split(";");
			for (int i = 0; i < mapList.length; i++) {
				String[] str = mapList[i].split(",");
				if (str.length == 1 && !TextUtils.isEmpty(str[0])) {
					map.put(Integer.parseInt(str[0]), null);
				}else if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
					map.put(Integer.parseInt(str[0]), str[1]);
				}
			}
		}
		return map;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("page", page);
		outState.putString("map", mapToString(map));
		outState.putDouble("allMoneyPrice", allMoneyPrice);
		outState.putBoolean("isChange", isChange);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		page = savedInstanceState.getInt("page");
		map = stringToMap( savedInstanceState.getString("map"));
		allMoneyPrice = savedInstanceState.getDouble("allMoneyPrice");
		isChange = savedInstanceState.getBoolean("isChange", false);
	}
}
