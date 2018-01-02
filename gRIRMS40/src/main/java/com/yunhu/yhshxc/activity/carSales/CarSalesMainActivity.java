package com.yunhu.yhshxc.activity.carSales;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.dialog.DiaLogAddStore;
import com.yunhu.yhshxc.activity.carSales.management.LoadingActivity;
import com.yunhu.yhshxc.activity.carSales.management.LoadingRecordActivity;
import com.yunhu.yhshxc.activity.carSales.management.RegistrationActivity;
import com.yunhu.yhshxc.activity.carSales.management.ReplenishmentActivity;
import com.yunhu.yhshxc.activity.carSales.management.StockTakingActivity;
import com.yunhu.yhshxc.activity.carSales.management.UnloadingActivity;
import com.yunhu.yhshxc.activity.carSales.management.UnloadingRecordActivity;
import com.yunhu.yhshxc.activity.carSales.manager.CarSalesDataManager;
import com.yunhu.yhshxc.activity.carSales.scene.chargeArrears.ChargeArrearsActivity;
import com.yunhu.yhshxc.activity.carSales.scene.returnGoods.ReturnGoodsAcitivity;
import com.yunhu.yhshxc.activity.carSales.scene.sellingGoods.ProductChoiceActivity;
import com.yunhu.yhshxc.activity.carSales.statistics.CarSalesSaleStatistics;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.LocationResult;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
//import com.gcg.grirms.location.GCGNewLocation;
import com.yunhu.yhshxc.location.ReceiveLocationListener;
import com.yunhu.yhshxc.location2.LocationFactoy;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.MapDistance;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSalesMainActivity extends AbsBaseActivity implements
		OnClickListener, OnFuzzyQueryListener {

	/**
	 * 车销_功能选择页面
	 * 
	 * @author abby.zhao
	 * @version 2015-06-15
	 */
	private MyProgressDialog progressDialog;
	private DiaLogAddStore diaLogAddStore = null;
	private MapDistance mapDistance = new MapDistance();

	private LinearLayout btnCarSaleSite;// 车销现场按钮
	private LinearLayout btnCarSaleStock;// 库存管理
	private LinearLayout btnCarSaleSale;// 销售统计按钮
	private LinearLayout btnCarSaleManager;// 管理按钮
	/**
	 * 车销现场及其按钮
	 */
	private LinearLayout llCarsaleSite;// 车销现场展示页面
	private DropDown spCarSalesCustomer;// 选择客户下拉列表
	private Spinner spCarSaleScreen;// 筛选按钮
	private Button btnCarSaleSalesToCustomer;// 向客户卖货
	private Button btnCarSaleSalesReturn;// 客户退货
	private Button btnCarSalesCollectMoney;// 收取欠款
	/**
	 * 库存管理及其按钮
	 */
	private LinearLayout llCarSaleStockManager;// 库存管理展示页面
	private Button btnCarSalesLoad;// 装车按钮
	private Button btnCarSalesLoadRecord;// 查看装车记录按钮
	private Button btnCarSalesUnload;// 卸车按钮
	private Button btnCarSalesUnloadRecord;// 查看卸车记录按钮
	private Button btnCarSalesReplenishment;// 补货申请按钮
	private Button btnCarSaleStockout;// 缺货登记按钮
	private Button btnCarSalesTakeStock;// 盘点库存按钮
	/**
	 * 销售统计及其按钮
	 */
	private LinearLayout llCarSalesSale;// 销售统计展示页面
	private Button btnCarSaleStockStatistics;// 库存统计按钮
	private Button btnCarSalesSaleStatistics;// 销售统计按钮
	private Button btnCarSalesStockoutStatistics;// 缺货统计按钮
	private Button btnCarSalesDebtStatistics;// 欠款统计
	/**
	 * 数据管理及其按钮
	 */
	private LinearLayout llCarSalesManager;// 数据管理展示页面
	private Button btnCarSalesUnreport;// 未上报车销数据按钮
	private Button btnCarSalesSysnSales;// 同步促销信息按钮
	private Button btnCarSalesShowSales;// 查看促销信息按钮
	private Button btnCarSalesSysnProduct;// 同步产品信息
	private Button btnCarSalesSysnCustomer;// 同步客户信息按钮
	private Button btnCarSalesSysnReimburse;// 费用报销按钮

	// 数据
	private List<OrgStore> allStore;// 所有店面
	private String storeId;
	private Double Longitude = null;
	private Double Latitude = null;
	private Double distanceSelect = null;
	private Boolean isFirst = true;
	private String selected = "未选择";
	
	
	private TextView ll_homeClock;

	/**
	 * 调用
	 */
	private CarSalesDataManager carSalesDataManager = null;
	private CarSalesSaleStatistics carSalesSaleStatistics = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_sales);
		ll_homeClock= (TextView) findViewById(R.id.ll_homeClock);
		carSalesDataManager = new CarSalesDataManager(this);
		carSalesSaleStatistics = new CarSalesSaleStatistics(this);
		registRefeshReceiver();
		init();// 初始化数据
	}

	/**
	 * 初始化实例初始化页面布局
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	private void init() {

		btnCarSaleSite = (LinearLayout) findViewById(R.id.btn_carsales_site);
		btnCarSaleStock = (LinearLayout) findViewById(R.id.btn_carsales_stock);
		btnCarSaleSale = (LinearLayout) findViewById(R.id.btn_carsales_sale);
		btnCarSaleManager = (LinearLayout) findViewById(R.id.btn_carsales_manager);
		/**
		 * 车销现场页面展示
		 */
		llCarsaleSite = (LinearLayout) findViewById(R.id.ll_carsales_site);
		spCarSalesCustomer = (DropDown) findViewById(R.id.sp_carsales_customer);
		spCarSaleScreen = (Spinner) findViewById(R.id.sp_carsales_screen);
		btnCarSaleSalesToCustomer = (Button) findViewById(R.id.btn_carsales_sales_to_customer);
		btnCarSaleSalesReturn = (Button) findViewById(R.id.btn_carsales_sales_return);
		btnCarSalesCollectMoney = (Button) findViewById(R.id.btn_carsales_collect_money);
		/**
		 * 库存管理页面展示
		 */
		llCarSaleStockManager = (LinearLayout) findViewById(R.id.ll_carsales_stock_manager);
		btnCarSalesLoad = (Button) findViewById(R.id.btn_carsales_load);
		btnCarSalesLoadRecord = (Button) findViewById(R.id.btn_carsales_load_record);
		btnCarSalesUnload = (Button) findViewById(R.id.btn_carsales_unload);
		btnCarSalesUnloadRecord = (Button) findViewById(R.id.btn_carsales_unload_record);
		btnCarSalesReplenishment = (Button) findViewById(R.id.btn_carsales_replenishment);
		btnCarSaleStockout = (Button) findViewById(R.id.btn_carsales_stockout);
		btnCarSalesTakeStock = (Button) findViewById(R.id.btn_carsales_take_stock);
		/**
		 * 销售统计页面展示
		 */
		llCarSalesSale = (LinearLayout) findViewById(R.id.ll_carsales_sale);
		btnCarSaleStockStatistics = (Button) findViewById(R.id.btn_carsales_stock_statistics);
		btnCarSalesSaleStatistics = (Button) findViewById(R.id.btn_carsales_sale_statistics);
		btnCarSalesStockoutStatistics = (Button) findViewById(R.id.btn_carsales_stockout_statistics);
		btnCarSalesDebtStatistics = (Button) findViewById(R.id.btn_carsales_debt_statistics);
		/**
		 * 数据管理页面展示
		 */
		llCarSalesManager = (LinearLayout) findViewById(R.id.ll_carsales_manager);
		btnCarSalesUnreport = (Button) findViewById(R.id.btn_carsales_unreport);
		btnCarSalesSysnSales = (Button) findViewById(R.id.btn_carsales_sysn_sales);
		btnCarSalesShowSales = (Button) findViewById(R.id.btn_carsales_show_sales);
		btnCarSalesSysnProduct = (Button) findViewById(R.id.btn_carsales_sysn_product);
		btnCarSalesSysnCustomer = (Button) findViewById(R.id.btn_carsales_sysn_customer);
		btnCarSalesSysnReimburse = (Button) findViewById(R.id.btn_carsales_reimburse);

		isShow();

		/**
		 * 设置按钮点击
		 */
		btnCarSaleSite.setOnClickListener(this);
		btnCarSaleStock.setOnClickListener(this);
		btnCarSaleSale.setOnClickListener(this);
		btnCarSaleManager.setOnClickListener(this);
		btnCarSalesUnreport.setOnClickListener(this);
		/**
		 * 数据管理按钮
		 */
		btnCarSalesSysnSales.setOnClickListener(this);
		btnCarSalesSysnProduct.setOnClickListener(this);
		btnCarSalesSysnCustomer.setOnClickListener(this);
		btnCarSalesSysnReimburse.setOnClickListener(this);
		btnCarSalesShowSales.setOnClickListener(this);
		/**
		 * 销售统计按钮
		 */
		btnCarSaleStockStatistics.setOnClickListener(this);
		btnCarSalesSaleStatistics.setOnClickListener(this);
		btnCarSalesStockoutStatistics.setOnClickListener(this);
		btnCarSalesDebtStatistics.setOnClickListener(this);

		/**
		 * 车销现场按钮
		 */
		btnCarSaleSalesToCustomer.setOnClickListener(this);
		btnCarSaleSalesReturn.setOnClickListener(this);
		btnCarSalesCollectMoney.setOnClickListener(this);
		/**
		 * 库存管理按钮
		 */

		btnCarSalesLoad.setOnClickListener(this);
		btnCarSalesLoadRecord.setOnClickListener(this);
		btnCarSalesUnload.setOnClickListener(this);
		btnCarSalesUnloadRecord.setOnClickListener(this);
		btnCarSalesReplenishment.setOnClickListener(this);
		btnCarSaleStockout.setOnClickListener(this);
		btnCarSalesTakeStock.setOnClickListener(this);

		/**
		 * 加载下拉列表数据
		 */
		spCarSalesCustomer.setOnFuzzyQueryListener((OnFuzzyQueryListener) this);
		spCarSalesCustomer.setOnResultListener(resultListener);
		initStoreData(null);

		ArrayAdapter<String> adapter;
		String m[] = { PublicUtils.getResourceString(CarSalesMainActivity.this,R.string.no_fance), "500m", "1000m", "2000m", "5000m" };

		adapter = new ArrayAdapter<String>(this, R.layout.screen_spinner_item,
				m);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCarSaleScreen.setAdapter(adapter);
		spCarSaleScreen.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selected = parent.getItemAtPosition(position).toString();
				if (selected.equals(PublicUtils.getResourceString(CarSalesMainActivity.this,R.string.no_fance))) {
					initStoreData(null);
				} else {
					distanceSelect = Double.parseDouble(selected.substring(0,
							selected.length() - 1));
					if (isFirst) {
						progressDialog = new MyProgressDialog(
								CarSalesMainActivity.this,
								R.style.CustomProgressDialog, getResources()
										.getString(R.string.loading_location));
						progressDialog.show();
						startLocation(receiveLocationListener);
						isFirst = false;
					} else {
						initStoreData(null);
					}

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void isShow() {
		//是否展示同步促销
		if("1".equals(SharedPreferencesForCarSalesUtil.getInstance(getApplicationContext()).getIsPromotion())){
			btnCarSalesSysnSales.setVisibility(View.GONE);
		}else{
			btnCarSalesSysnSales.setVisibility(View.VISIBLE);
		}
		
		//是否展示查看促销
		
		if("1".equals(SharedPreferencesForCarSalesUtil.getInstance(getApplicationContext()).getIsPromotion())){
			btnCarSalesShowSales.setVisibility(View.GONE);
		}else{
			btnCarSalesShowSales.setVisibility(View.VISIBLE);
		}
		
		
		// 费用报销按钮展示 IS_HAS_COST
		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasCost())) {
			btnCarSalesSysnReimburse.setVisibility(View.VISIBLE);
		}else{
			btnCarSalesSysnReimburse.setVisibility(View.GONE);
		}

		// 库存盘点按钮展示
		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasStock())) {
			btnCarSalesTakeStock.setVisibility(View.VISIBLE);
		}else{
			btnCarSalesTakeStock.setVisibility(View.GONE);
		}

		// 是否需要卖货或收款 IS_HAS_SALES
		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasSales())) {
			btnCarSaleSalesToCustomer.setVisibility(View.VISIBLE);
			btnCarSalesCollectMoney.setVisibility(View.VISIBLE);
		}else{
			btnCarSaleSalesToCustomer.setVisibility(View.GONE);
			btnCarSalesCollectMoney.setVisibility(View.GONE);
		}

		// 是否需要补货申请

		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasFill())) {
			btnCarSalesReplenishment.setVisibility(View.VISIBLE);
		}else{
			btnCarSalesReplenishment.setVisibility(View.GONE);
		}

		// 是否需要装车和装车记录 IS_HAS_LODING

		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasLoding())) {
			btnCarSalesLoad.setVisibility(View.VISIBLE);
			btnCarSalesLoadRecord.setVisibility(View.VISIBLE);
		}else{
			btnCarSalesLoad.setVisibility(View.GONE);
			btnCarSalesLoadRecord.setVisibility(View.GONE);
		}

		// 是否需要卸车和卸车记录 IS_HAS_TRUCK

		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasTruck())) {
			btnCarSalesUnload.setVisibility(View.VISIBLE);
			btnCarSalesUnloadRecord.setVisibility(View.VISIBLE);
		}else{
			btnCarSalesUnload.setVisibility(View.GONE);
			btnCarSalesUnloadRecord.setVisibility(View.GONE);
		}

		// 是否需要客户退货

		if ("1".equals(SharedPreferencesForCarSalesUtil.getInstance(
				getApplicationContext()).getIsHasReturn())) {
			btnCarSaleSalesReturn.setVisibility(View.VISIBLE);
		}else{
			btnCarSaleSalesReturn.setVisibility(View.GONE);
		}

	}

	/**
	 * 初始化所有客户数据
	 */
	public List<Dictionary> srcStoreList;
	private List<Dictionary> srcStoreNoList;

	private void initStoreData(String fuzzy) {
		allStore = new OrgStoreDB(this).findAllOrgList(fuzzy);
		srcStoreList = new ArrayList<Dictionary>();
		srcStoreNoList = new ArrayList<Dictionary>();
		
		Dictionary dictionary = new Dictionary();
		dictionary.setDid("-1");
		dictionary.setCtrlCol("其他...");
		dictionary.setLevel(-1);
		dictionary.setNote("-1");
		List<Dictionary> othersList = new ArrayList<Dictionary>();
		othersList.add(dictionary);
		srcStoreList.addAll(othersList);
		
		if (allStore != null && allStore.size() > 0) {

			OrgStore store = null;
			for (int i = 0; i < allStore.size(); i++) {

				store = allStore.get(i);
				// 如果选择为距离不限或者为未选择状态，则添加全部
				if (selected.equals("不限") || selected.equals("未选择")) {
					srcStoreList.add(dicByStore(store));
				} else {
					// 如果没有经纬度则添加
					if (store.getStoreLat() == null
							|| store.getStoreLon() == null) {

						srcStoreNoList.add(dicByStore(store));
					} else {
						if (Latitude == null || Longitude == null) {
							srcStoreList.add(dicByStore(store));
						} else {
							double storeLat = store.getStoreLat();
							double storeLon = store.getStoreLon();
							@SuppressWarnings("static-access")
							double distance = mapDistance.getDistance(storeLat,
									storeLon, Latitude, Longitude);
							if (distance <= distanceSelect) {
								srcStoreList.add(dicByStore(store));
							}

						}
					}
				}

			}
			srcStoreList.addAll(srcStoreNoList);

			

		}
		spCarSalesCustomer.setSrcDictList(srcStoreList);
		initStoreSelect(srcStoreList);
	}

	/**
	 * 店面转Dictionary
	 * 
	 * @param store
	 * @return
	 */
	private Dictionary dicByStore(OrgStore store) {
		Dictionary dic = new Dictionary();
		dic.setDid(String.valueOf(store.getStoreId()));
		dic.setCtrlCol(store.getStoreName());
		dic.setNote(store.getOrgCode());
		dic.setLevel(store.getLevel());
		return dic;
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
	public void initStoreSelect(List<Dictionary> srcList) {
		boolean isSelect = false;
		if (!TextUtils.isEmpty(storeId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (storeId.equals(dic.getDid())) {

					spCarSalesCustomer.setSelected(dic);

					isSelect = true;
					break;
				}
			}
		}
		if (!isSelect) {// 如果此时筛选出来的没有上次选择的店面则把storeId清空，下拉框显示请选择
			storeId = null;
			spCarSalesCustomer.refreshComp(null);
		}
	}

	String orgId;
	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				String did = dic.getDid();
				if ("-1".equals(did)) {
					diaLogAddStore = new DiaLogAddStore(
							CarSalesMainActivity.this,
							R.style.CustomProgressDialog, getResources()
									.getString(R.string.loading_location),
							spCarSalesCustomer);

					diaLogAddStore.show();
				} else {
					storeId = dic.getDid();
					orgId = dic.getNote();
					SharedPreferencesForCarSalesUtil.getInstance(
							CarSalesMainActivity.this).clearLeaveMeassage();
					SharedPreferencesForCarSalesUtil.getInstance(
							CarSalesMainActivity.this).setCarSalesOrgId(orgId);
					;
					SharedPreferencesForCarSalesUtil.getInstance(
							CarSalesMainActivity.this).setCarSalesStoreName(
							dic.getCtrlCol());
					SharedPreferencesForCarSalesUtil.getInstance(
							CarSalesMainActivity.this).setStoreId(storeId);
					SharedPreferencesForCarSalesUtil.getInstance(
							CarSalesMainActivity.this).setCarSalesStoreLevel(
							dic.getLevel());
				}

			}

		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		/**
		 * 点击库存管理按钮，显示库存管理界面
		 */
		case R.id.btn_carsales_stock:
			ll_homeClock.setText("库存管理");
			llCarsaleSite.setVisibility(View.GONE);
			llCarSaleStockManager.setVisibility(View.VISIBLE);
			llCarSalesSale.setVisibility(View.GONE);
			llCarSalesManager.setVisibility(View.GONE);

			break;
		/**
		 * 点击管理按钮，显示管理界面
		 */
		case R.id.btn_carsales_site:
			ll_homeClock.setText("车销现场");
			llCarsaleSite.setVisibility(View.VISIBLE);
			llCarSaleStockManager.setVisibility(View.GONE);
			llCarSalesSale.setVisibility(View.GONE);
			llCarSalesManager.setVisibility(View.GONE);
			break;
		/**
		 * 点击管理按钮，显示管理界面
		 */
		case R.id.btn_carsales_sale:
			ll_homeClock.setText("销售统计");
			llCarsaleSite.setVisibility(View.GONE);
			llCarSaleStockManager.setVisibility(View.GONE);
			llCarSalesSale.setVisibility(View.VISIBLE);
			llCarSalesManager.setVisibility(View.GONE);
			break;
		/**
		 * 点击管理按钮，显示管理界面
		 */
		case R.id.btn_carsales_manager:
			ll_homeClock.setText("数据管理");
			llCarsaleSite.setVisibility(View.GONE);
			llCarSaleStockManager.setVisibility(View.GONE);
			llCarSalesSale.setVisibility(View.GONE);
			llCarSalesManager.setVisibility(View.VISIBLE);
			break;
		/**
		 * 未上报车销数据按钮点击
		 */
		case R.id.btn_carsales_unreport:

			carSalesDataManager.unSubmitData();
			break;
		/**
		 * 同步促销信息按钮点击事件
		 */
		case R.id.btn_carsales_sysn_sales:
			// if
			// (TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(
			// this).getCarSalesStoreName())) {
			// Toast.makeText(this, "请先选择店面", Toast.LENGTH_SHORT).show();
			// } else {
			// carSalesDataManager.sycCX();
			// }


			carSalesDataManager.sycCX();
			break;

		/**
		 * 查看促销信息
		 */
		case R.id.btn_carsales_show_sales:
//			if (TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(
//					this).getCarSalesStoreName())) {
//				Toast.makeText(this, "请先选择店面", Toast.LENGTH_SHORT).show();
//			} else {
//				carSalesDataManager.seeCX();
//			}
			carSalesDataManager.seeCX();
			break;
		/**
		 * 同步产品信息按钮点击事件
		 */
		case R.id.btn_carsales_sysn_product:
//			if (TextUtils.isEmpty(SharedPreferencesForCarSalesUtil.getInstance(
//					this).getCarSalesStoreName())) {
//				Toast.makeText(this, "请先选择店面", Toast.LENGTH_SHORT).show();
//			} else {
//				carSalesDataManager.sycCP();
//			}
			carSalesDataManager.sycCP();
			break;
		/**
		 * 同步客户信息按钮点击事件
		 */
		case R.id.btn_carsales_sysn_customer:
			carSalesDataManager.sycKH();
			break;
		/**
		 * 费用报销按钮点击事件
		 */
		case R.id.btn_carsales_reimburse:
			carSalesDataManager.applyForReimbursement();
			break;
		/**
		 * 库存统计按钮点击事件
		 */
		case R.id.btn_carsales_stock_statistics:
			carSalesSaleStatistics.stockStatistics();
			break;
		/**
		 * 销售统计按钮点击事件
		 */
		case R.id.btn_carsales_sale_statistics:
			carSalesSaleStatistics.salesStatistics();
			break;
		/**
		 * 缺货统计按钮点击事件
		 */
		case R.id.btn_carsales_stockout_statistics:
			carSalesSaleStatistics.stockOutStatistics();
			break;
		/**
		 * 欠款统计按钮点击事件
		 */
		case R.id.btn_carsales_debt_statistics:
			carSalesSaleStatistics.debtStatistics();
			break;
		/**
		 * 向客户卖货按钮点击事件
		 */
		case R.id.btn_carsales_sales_to_customer:
			if ("".equals(SharedPreferencesForCarSalesUtil.getInstance(
					getApplicationContext()).getCarSalesStoreName())) {
				ToastOrder.makeText(CarSalesMainActivity.this, "请先选择店面",
						ToastOrder.LENGTH_SHORT).show();
			} else {
				sellingGoods();
			}

			break;
		/**
		 * 客户退货按钮点击事件
		 */
		case R.id.btn_carsales_sales_return:

			if ("".equals(SharedPreferencesForCarSalesUtil.getInstance(
					getApplicationContext()).getCarSalesStoreName())) {
				ToastOrder.makeText(CarSalesMainActivity.this, "请先选择店面",
						ToastOrder.LENGTH_SHORT).show();
			} else {
				returnGoods();
			}

			break;
		/**
		 * 收取欠款
		 */
		case R.id.btn_carsales_collect_money:
//			if ("".equals(SharedPreferencesForCarSalesUtil.getInstance(
//					getApplicationContext()).getCarSalesStoreName())) {
//				Toast.makeText(CarSalesMainActivity.this, "请先选择店面",
//						Toast.LENGTH_SHORT).show();
//			} else {
//				chargeArrears();
//			}
			chargeArrears();

			break;
		/**
		 * 装车
		 */
		case R.id.btn_carsales_load:
			loading();
			break;
		/**
		 * 装车记录查看
		 */
		case R.id.btn_carsales_load_record:
			recordLook();
			break;
		/**
		 * 卸车
		 */
		case R.id.btn_carsales_unload:
			unloading();
			break;
		/**
		 * 卸车记录查看
		 */
		case R.id.btn_carsales_unload_record:
			unLoadRecordLook();
			break;
		/**
		 * 补货申请
		 */
		case R.id.btn_carsales_replenishment:
			replenishment();
			break;
		/**
		 * 缺货登记
		 */
		case R.id.btn_carsales_stockout:
			stockout();
			break;
		/**
		 * 库存盘点
		 */
		case R.id.btn_carsales_take_stock:

			takeStock();
			break;

		default:
			break;
		}
	}

	private void unloading() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				UnloadingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("jilu", 5);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void loading() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				LoadingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("jilu", 5);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void takeStock() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				StockTakingActivity.class);
		startActivity(intent);
	}

	private void stockout() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				RegistrationActivity.class);
		startActivity(intent);
	}

	private void replenishment() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				ReplenishmentActivity.class);
		startActivity(intent);
	}

	private void unLoadRecordLook() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				UnloadingRecordActivity.class);
		startActivity(intent);
	}

	private void recordLook() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				LoadingRecordActivity.class);
		startActivity(intent);
	}

	private void chargeArrears() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				ChargeArrearsActivity.class);
		startActivity(intent);
	}

	private void returnGoods() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				ReturnGoodsAcitivity.class);
		startActivity(intent);
	}

	private void sellingGoods() {
		Intent intent = new Intent(CarSalesMainActivity.this,
				ProductChoiceActivity.class);
		startActivity(intent);
	}

	/**
	 * 开始定位
	 * 
	 * @param listener
	 */
	public void startLocation(ReceiveLocationListener listener) {
//		GCGNewLocation gcgNewLocation = new GCGNewLocation(this);
//		gcgNewLocation.setNeedAddress(false);
//		gcgNewLocation.setListener(listener);
//		gcgNewLocation.requestLocation();
		new LocationFactoy(this, listener).startLocationHH();
	}

	/**
	 * 定位回调监听
	 */
	ReceiveLocationListener receiveLocationListener = new ReceiveLocationListener() {

		@Override
		public void onReceiveResult(LocationResult result) {
			if (result != null) {
				ToastOrder.makeText(CarSalesMainActivity.this, "筛选成功,请选择店面",
						ToastOrder.LENGTH_SHORT).show();
				Longitude = result.getLongitude();
				Latitude = result.getLatitude();
				initStoreData(null);
			} else {
				ToastOrder.makeText(CarSalesMainActivity.this, "获取位置失败",
						ToastOrder.LENGTH_SHORT).show();
			}
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(refeshReceiver);
		super.onDestroy();
	}
	
	/**
	 * 车销刷新店面广播
	 */
	private BroadcastReceiver refeshReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_CAR_SALES_REFRASH_STORE.equals(intent.getAction())) {
				initStoreData(null);
			}
		}
		
	};

	private void registRefeshReceiver(){
		IntentFilter f = new IntentFilter(Constants.BROADCAST_CAR_SALES_REFRASH_STORE);
		registerReceiver(refeshReceiver, f);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			SharedPreferencesForCarSalesUtil.getInstance(this)
					.clearLeaveMeassage();

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isFirst", isFirst);
		if (Longitude != null) {
			outState.putDouble("Longitude", Longitude);
		}
		if (Latitude != null) {
			outState.putDouble("Latitude", Latitude);
		}
		if (distanceSelect != null) {
			outState.putDouble("distanceSelect", distanceSelect);
		}
		outState.putString("storeId", storeId);
		outState.putString("selected", selected);
	}

	/**
	 * 读取数据-->非正常状态下使用
	 */
	@Override
	protected void restoreConstants(Bundle savedInstanceState) {
		super.restoreConstants(savedInstanceState);
		isFirst = savedInstanceState.getBoolean("isFirst");
		Longitude = savedInstanceState.getDouble("Longitude");
		Latitude = savedInstanceState.getDouble("Latitude");
		distanceSelect = savedInstanceState.getDouble("distanceSelect");
		storeId = savedInstanceState.getString("storeId");
		selected = savedInstanceState.getString("selected");
	}
}
