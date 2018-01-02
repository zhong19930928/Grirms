package com.yunhu.yhshxc.activity.carSales.management;

import tf.test.SerialPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductDataDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.manager.CarSalesDataManager;
import com.yunhu.yhshxc.activity.carSales.print.CarSalesPrintForLoadingGoods;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesPopupWindow;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.activity.carSales.zhy.tree_view.LoadingTreeAdapter;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.GCGListView;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 装车列表
 * 
 * @author xuelinlin
 *
 */
public class LoadingListActivity extends BaseActivity implements OnList, DataSource {
	private GCGListView lv_all;
	private LinearLayout ll_home_yulan;
	private LinearLayout ll_prince;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_submit;
	private LinearLayout leftL;
	private LinearLayout et_ll;
	private ImageButton ib_scanning;
	private LinearLayout ll2;
	private EditText et_tuikuan;
	private EditText et_leave_message;
	private TextView tv_menu;
	private LoadingTreeAdapter adapter;
	private CarSalesStockDB carSalesStockDB;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesDB carSalesDB;
	private CarSalesProductDataDB dataDB;
	private CarSalesUtil util;
	private Bundle bundle;
	private String carSalesNo;
	private boolean isEnble = true;
	private List<CarSalesProductCtrl> productCtrlsChild = new ArrayList<CarSalesProductCtrl>();
	private int isRecord;
	private Map<String, String> map;
	private Map<String, String> firstCount = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_scale_return_goods_list);
		carSalesStockDB = new CarSalesStockDB(this);
		ctrlDB = new CarSalesProductCtrlDB(this);
		carSalesDB = new CarSalesDB(this);
		dataDB = new CarSalesProductDataDB(this);
		util = new CarSalesUtil(this);
		map = util.getCtrlCount();
		bundle = new Bundle();
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		carSalesNo = b.getString("carSalesNo");
		isRecord = b.getInt("isRecord");
		initWidget();
		if (isRecord == 1) {
			CarSales c = carSalesDB.findCarSalesByCarSalesNoAndStoreId(carSalesNo);
			if (c != null) {
				clearData();
				initYulan(carSalesNo);
				et_leave_message.setText(c.getNote());
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setLeaveMessage(c.getNote());
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setCarSalesNo(carSalesNo);
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setPhotoNameOne(c.getImage1());
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setPhotoNameTwo(c.getImage2());
				if (c.getCarSalesState().equalsIgnoreCase("审批退回")) {
					isEnble = true;
				} else {
					isEnble = false;
					setEnable();
				}
			}
		}
		try {
			initData();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		registS200PhoneReceiver();
	}

	private void registS200PhoneReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PhoneModelManager.ACTION_SPECIALKEY);
		this.registerReceiver(s200receiver, filter);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(s200receiver);

	}

	@SuppressLint("NewApi")
	private void setEnable() {
		ll_home_yulan.setEnabled(false);
		ll_prince.setEnabled(false);
		ll_take_photo.setEnabled(false);
		ll_submit.setEnabled(false);
		et_leave_message.setEnabled(false);
		ib_scanning.setVisibility(View.INVISIBLE);
		ll2.setVisibility(View.GONE);
		et_ll.setBackgroundColor(Color.WHITE);
	}

	private void initYulan(String carSalesNo) {
		List<CarSalesProductData> data = dataDB.findCarSalesProductDataByCarSalesNoContainPromotion(carSalesNo);
		if (data != null && !data.isEmpty()) {
			for (int i = 0; i < data.size(); i++) {
				CarSalesProductData pd = data.get(i);
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(pd.getProductId(),
						pd.getUnitId());
				ctrl.setLoadingCount(String.valueOf(pd.getCarSalesCount()));
				ctrl.setDataId(pd.getDataId());
				if (ctrl != null) {
					productCtrlsChild.add(ctrl);
				}
			}
			ctrlDB.updateAllChangedLoadingCtrl(productCtrlsChild);
		}
	}

	@SuppressLint("NewApi")
	private void initData() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		if (productCtrlsChild.size() > 0) {

		} else {
			productCtrlsChild = util.loadingProductCtrlList();
		}
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new LoadingTreeAdapter<CarSalesProductCtrl>(lv_all, this, productCtrlsChildsss, 10, this,
					isEnble);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(LoadingListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
						}
					}
				}
			});
			lv_all.setAdapter(adapter);
			lv_all.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	private void initWidget() {
		lv_all = (GCGListView) findViewById(R.id.lv_all);
		ll_home_yulan = (LinearLayout) findViewById(R.id.ll_home_yulan);
		leftL = (LinearLayout) findViewById(R.id.leftL);
		et_ll = (LinearLayout) findViewById(R.id.et_ll);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll_prince = (LinearLayout) findViewById(R.id.ll_prince);
		ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
		ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
		et_leave_message = (EditText) findViewById(R.id.et_leave_message);
		et_tuikuan = (EditText) findViewById(R.id.et_tuikuan);
		ib_scanning = (ImageButton) findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(listner);
		leftL.setVisibility(View.GONE);
		tv_menu = (TextView) findViewById(R.id.tv_menu);
		tv_menu.setText("装车列表");
		controlPrint();
		et_leave_message.addTextChangedListener(lyWatcher);
		et_leave_message
				.setText(SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getLeaveMessage());
	}

	// 是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint() {
		int isPrint = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsLoding());
		if (isPrint == 1) {
			ll_prince.setVisibility(View.GONE);
		} else {
			ll_prince.setVisibility(View.VISIBLE);
		}
		int isPhoto = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsLodingPhoto());
		if (isPhoto == 1) {
			ll_take_photo.setVisibility(View.GONE);
		} else {
			ll_take_photo.setVisibility(View.VISIBLE);
		}
	}

	private TextWatcher lyWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String msg = s.toString();
			if (!TextUtils.isEmpty(msg)) {
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setLeaveMessage(msg);
			} else {
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).setLeaveMessage("");
			}
		}
	};

	public void LayoutOnclickMethod(View layout) {
		switch (layout.getId()) {

		case R.id.ll_take_photo:// 拍照
			takePhoto();
			break;
		case R.id.ll_home_yulan:// 返回
			returning();
			break;
		case R.id.ll_prince:// 打印
			print();
			break;
		case R.id.ll_submit:// 提交
			submit();
			break;
		default:
			break;
		}

	};

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			scan();
		}
	};

	private void scan() {
		// Intent i = new Intent(this, CaptureActivity.class);
		Intent i = PhoneModelManager.getIntent(this,false);
		if (i!=null) {				
			startActivityForResult(i, 206);
		}

	}

	private CarSalesPopupWindow popupWindow;

	private void takePhoto() {
		popupWindow = new CarSalesPopupWindow(this);
		popupWindow.show(null, SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getPhotoNameOne(),
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getPhotoNameTwo());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.scan_succeeded && requestCode == 206) {
			String scanCode = null;
			scanCode = data.getStringExtra("SCAN_RESULT");
			try {
				scanForResult(scanCode);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (popupWindow != null) {
			popupWindow.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void scanForResult(String code) throws IllegalAccessException {
		if (!TextUtils.isEmpty(code)) {
			CarSalesProduct product = util.productForCode(code);
			if (product != null) {
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(),
						product.getUnitId());
				if (!ctrls.containsKey(ctrl.getId())) {
					ctrl.setLoadingCount("1");
					ctrls.put(ctrl.getId(), ctrl);
					bundle.putString(String.valueOf(ctrl.getId()), String.valueOf(1));
					ToastOrder.makeText(LoadingListActivity.this, "添加成功", ToastOrder.LENGTH_LONG).show();
				} else {
					ToastOrder.makeText(LoadingListActivity.this, "装车列表已存在", ToastOrder.LENGTH_LONG).show();
					return;
				}

			} else {
				ToastOrder.makeText(LoadingListActivity.this, "该产品不存在，请确认是否扫描错误", ToastOrder.LENGTH_LONG).show();
				return;
			}
		}
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedLoadingCtrl(cs);
		initadapter();
	}

	@SuppressLint("NewApi")
	private void initadapter() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		productCtrlsChild = util.loadingProductCtrlList();
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new LoadingTreeAdapter<CarSalesProductCtrl>(lv_all, this, productCtrlsChildsss, 10, this,
					isEnble);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(LoadingListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
						}
					}
				}
			});
			lv_all.setAdapter(adapter);
			lv_all.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	Map<String, CarSalesProductCtrl> ctrls = new HashMap<String, CarSalesProductCtrl>();

	private void returning() {
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedLoadingCtrl(cs);
		if (isRecord == 1) {
			util.saveCtrlCount(firstCount);
			Intent intent = new Intent(LoadingListActivity.this, LoadingActivity.class);
			bundle.putInt("jilu", 10);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			if (bundle != null) {
				setResult(22, LoadingListActivity.this.getIntent().putExtras(bundle));
			}
		}
		this.finish();
	}

	/**
	 * 打印订单
	 */
	private CarSalesPrintForLoadingGoods carSalesPrint;

	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(LoadingListActivity.this, "car_sales_print_loading_goods.txt");
		try {
			List<CarSalesProductCtrl> productCtrls = util.loadingProductCtrlList();
			carSalesPrint = new CarSalesPrintForLoadingGoods(this, true);
			CarSales carSales = getCarSales(productCtrls);
			carSalesPrint.setCarSales(carSales);
			carSalesPrint.print(LoadingListActivity.this, new JSONArray(json), LoadingListActivity.this);
			int printCount = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount();
			SharedPreferencesForCarSalesUtil.getInstance(this).setCarSalesPrintCount(printCount + 1);
			;
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
	}

	private void submit() {
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedLoadingCtrl(cs);
		List<CarSalesProductCtrl> productCtrls = util.loadingProductCtrlList();
		List<CarSalesProductCtrl> productCtrlsNew = new ArrayList<CarSalesProductCtrl>();
		CarSales c = null;
		if (!TextUtils.isEmpty(carSalesNo)) {
			c = carSalesDB.findCarSalesByCarSalesNoAndStoreId(carSalesNo);
			if (c != null) {
				for (int i = 0; i < productCtrls.size(); i++) {
					CarSalesProductCtrl ctrl = productCtrls.get(i);
					productCtrlsNew.add(ctrl);
				}
			}
		} else {
			for (int i = 0; i < productCtrls.size(); i++) {
				CarSalesProductCtrl ctrl = productCtrls.get(i);
				if (!TextUtils.isEmpty(ctrl.getLoadingCount()) || ctrl.getLoadingCount().equalsIgnoreCase("0")
						|| ctrl.getLoadingCount().equalsIgnoreCase("0.0")) {
					productCtrlsNew.add(ctrl);
				}
			}
		}
		updateStock(productCtrlsNew);
		CarSales carSales = getCarSales(productCtrlsNew);
		CarSalesDataManager manager = new CarSalesDataManager(this);
		manager.submit(carSales, UrlInfo.doLodingInfo(LoadingListActivity.this));
		SharedPreferencesForCarSalesUtil.getInstance(this).clearReturnSubmitMeassage();
		SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).clearCarSalesCheckCount();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl e = productCtrls.get(i);
			e.setLoadingCount("");
			ctrlDB.updateProductCtrlLoadingCount(e);
			bundle.putString(String.valueOf(e.getId()), "");
		}
		// util.carSalesNumber(false);
		sendBroadcast(new Intent(Constants.BROADCAST_CARSALES_CREATE_SUCCESS));
		ctrls.clear();
		setCommonList(productCtrls);
		ToastOrder.makeText(LoadingListActivity.this, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
		if (!TextUtils.isEmpty(carSalesNo)) {
			clearData();
			SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).clearCarSalesCheckCount();
			LoadingListActivity.this.finish();
			Intent intent = new Intent(LoadingListActivity.this, LoadingRecordActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else {
			returning();
		}
	}

	/**
	 * 更新库存列表
	 * 
	 * @param productCtrls
	 */
	private void updateStock(List<CarSalesProductCtrl> productCtrls) {
		Set<String> set = bundle.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (!key.equalsIgnoreCase("carSalesNo") && !key.equalsIgnoreCase("isRecord")) {
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlById(Integer.parseInt(key));
				String loadingCount = !TextUtils.isEmpty(bundle.getString(key)) ? bundle.getString(key) : "0";
				if (ctrl != null) {
					CarSalesStock stock = carSalesStockDB.findCarSalesStockByProductIdAndUnitId(ctrl.getProductId(),
							ctrl.getUnitId());
					if (stock != null) {
						double stockNum = Double.parseDouble(loadingCount) + stock.getStockNum();
						double outNum = stock.getStockoutNum() - Double.parseDouble(loadingCount);
						double replenishmentNum = stock.getReplenishmentNum() - Double.parseDouble(loadingCount);
						stock.setStockNum(stockNum);
						if (outNum < 0) {
							stock.setStockoutNum(0);
						} else {
							stock.setStockoutNum(outNum);
						}
						if (replenishmentNum < 0) {
							stock.setReplenishmentNum(0);
						} else {
							stock.setReplenishmentNum(replenishmentNum);
						}
						carSalesStockDB.updateCarSalesStock(stock);
					}
				}
			}
		}
		map = util.getCtrlCount();
		Set<String> set1 = map.keySet();
		Iterator<String> it1 = set1.iterator();
		while (it1.hasNext()) {
			String key1 = it1.next();
			if (TextUtils.isEmpty(bundle.getString(key1))) {
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlById(Integer.parseInt(key1));
				String loadingCount = !TextUtils.isEmpty(map.get(key1)) ? ("-" + map.get(key1)) : "0";
				if (ctrl != null) {
					CarSalesStock stock = carSalesStockDB.findCarSalesStockByProductIdAndUnitId(ctrl.getProductId(),
							ctrl.getUnitId());
					if (stock != null) {
						double stockNum = Double.parseDouble(loadingCount) + stock.getStockNum();
						double outNum = stock.getStockoutNum() - Double.parseDouble(loadingCount);
						double replenishmentNum = stock.getReplenishmentNum() - Double.parseDouble(loadingCount);
						stock.setStockNum(stockNum);
						if (outNum < 0) {
							stock.setStockoutNum(0);
						} else {
							stock.setStockoutNum(outNum);
						}
						if (replenishmentNum < 0) {
							stock.setReplenishmentNum(0);
						} else {
							stock.setReplenishmentNum(replenishmentNum);
						}
						carSalesStockDB.updateCarSalesStock(stock);
					}
				}
			}
		}
	}

	@Override
	public void onListNum(Editable s, Node node, EditText et) {

		String str = s.toString();
		if (!TextUtils.isEmpty(str)) {
			if (str.equals(".")) {
				str = "0";
			} else if (str.startsWith(".")) {
				str = "0" + str;
			} else if (str.endsWith(".")) {
				str = str + "0";
			}
		} else {
			str = "";
		}
		CarSalesProductCtrl ctrl = node.getProductCtrl();
		String foreCount = !TextUtils.isEmpty(ctrl.getLoadingCount()) ? ctrl.getLoadingCount() : "0";
		double cha = 0;
		if (ctrl != null) {
			if (ctrls.containsKey(ctrl.getId())) {
				ctrls.remove(ctrl);
				if (!TextUtils.isEmpty(str)) {
					ctrl.setLoadingCount(str);
					cha = Double.parseDouble(ctrl.getLoadingCount()) - Double.parseDouble(foreCount);
				} else {
					ctrl.setLoadingCount("");
					cha = -Double.parseDouble(foreCount);
				}
				ctrls.put(ctrl.getId(), ctrl);
			} else {
				if (isRecord == 1) {
					if (!TextUtils.isEmpty(str)) {
						ctrl.setLoadingCount(str);
						firstCount.put(String.valueOf(ctrl.getId()), str);
						cha = Double.parseDouble(ctrl.getLoadingCount()) - Double.parseDouble(foreCount);
						ctrls.put(ctrl.getId(), ctrl);
					} else {
						ctrl.setLoadingCount("");
					}
				} else {
					String s2 = map.get(String.valueOf(ctrl.getId()));
					String count = !TextUtils.isEmpty(s2) ? s2 : "0";
					if (!TextUtils.isEmpty(str)) {
						ctrl.setLoadingCount(str);
						cha = Double.parseDouble(str) - Double.parseDouble(count);
						ctrls.put(ctrl.getId(), ctrl);
					} else {
						ctrl.setLoadingCount("");
					}
				}
			}
			if (!TextUtils.isEmpty(bundle.getString(String.valueOf(ctrl.getId())))) {
				double count = Double.parseDouble(bundle.getString(String.valueOf(ctrl.getId())));
				cha = cha + count;
			}
			ctrls.put(ctrl.getId(), ctrl);
			bundle.putString(String.valueOf(ctrl.getId()), String.valueOf(cha));
			if (!isEnble) {
				et.setEnabled(false);
			}
		}
	}

	private void setCommonList(List<CarSalesProductCtrl> returnList) {
		for (int i = 0; i < returnList.size(); i++) {
			CarSalesProductCtrl produCtrl = ctrlDB.findProductCtrlByProductIdAndUnitId(returnList.get(i).getProductId(),
					returnList.get(i).getUnitId());
			if (produCtrl != null) {
				ctrlDB.updateProductCtrlCount(produCtrl);
			}
		}
	}

	@SuppressWarnings("null")
	public CarSales getCarSales(List<CarSalesProductCtrl> productCtrls) {
		CarSales carSales = null;
		String carSaleNo = SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getCarSalesNo();
		if (!TextUtils.isEmpty(carSaleNo)) {
			carSales = carSalesDB.findCarSalesByCarSalesNoAndStoreId(carSaleNo);
		} else {
			carSales = new CarSales();
			carSales.setCarSalesNo(util.carSalesNumber(true));
			carSales.setCarId(
					String.valueOf(SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getCarId()));
			carSales.setPrintCount(SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount());
		}
		carSales.setImage1(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameOne());
		carSales.setImage2(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameTwo());
		carSales.setStoreName(
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getCarSalesStoreName());
		carSales.setStoreId(SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).getStoreId());
		carSales.setCarSalesTime(DateUtil.getCurDateTime());
		List<CarSalesProductData> carSalesPros = new ArrayList<CarSalesProductData>();
		if (productCtrls.size() > 0) {
			for (int i = 0; i < productCtrls.size(); i++) {
				CarSalesProductCtrl c = productCtrls.get(i);
				CarSalesProduct p = util.product(c.getProductId(), c.getUnitId());
				if (p != null) {
					CarSalesProductData pda = new CarSalesProductData();
					pda.setProductId(p.getProductId());
					String count = TextUtils.isEmpty(c.getLoadingCount()) ? "0" : c.getLoadingCount();
					pda.setCarSalesCount(Double.parseDouble(count));
					pda.setActualCount(Double.parseDouble(count));
					pda.setProductUnit(p.getUnit());
					pda.setUnitId(p.getUnitId());
					pda.setCarSalesPrice(p.getPrice());
					pda.setProductName(p.getName());
					pda.setDataId(c.getDataId());
					carSalesPros.add(pda);
				}
			}
		}
		if (carSalesPros.size() > 0) {
			carSales.setProductList(carSalesPros);
		}
		String msg = SharedPreferencesForCarSalesUtil.getInstance(this).getLeaveMessage();
		carSales.setNote(msg);// 留言
		carSales.setStock(true);
		return carSales;
	}

	private void clearData() {
		List<CarSalesProductCtrl> productCtrls = util.loadingProductCtrlList();
		SharedPreferencesForCarSalesUtil.getInstance(this).clearReturnSubmitMeassage();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl c = productCtrls.get(i);
			c.setLoadingCount("");
			ctrlDB.updateProductCtrlLoadingCount(c);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isRecord == 1) {
				clearData();
				SharedPreferencesForCarSalesUtil.getInstance(LoadingListActivity.this).clearCarSalesCheckCount();
				LoadingListActivity.this.finish();
			} else {
				List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
				Set<String> set = ctrls.keySet();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					cs.add(ctrls.get(it.next()));
				}
				ctrlDB.updateAllChangedLoadingCtrl(cs);
				if (bundle != null) {
					setResult(22, LoadingListActivity.this.getIntent().putExtras(bundle));
					this.finish();
				}
			}
		} else {// 如果不是返回键,就判断是否是特殊机型的扫描按键,如果是就调用扫描头
			if (PhoneModelManager.isStartScan(this, keyCode, event)) {
				scan();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

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

	@Override
	public void onPutView(Node node, EditText ev) {

	}

	private String mapToString(Map<String, String> map) {
		String mapStr = null;
		if (map != null && !map.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				sb.append(";").append(entry.getKey() != null ? entry.getKey() : "").append(",")
						.append(entry.getValue() != null ? entry.getValue() : "");
			}
			mapStr = sb.substring(1).toString();
		}
		return mapStr;
	}

	private Map<String, String> stringToMap(String value) {
		Map<String, String> map = new HashMap<String, String>();
		if (!TextUtils.isEmpty(value)) {
			String[] mapList = value.split(";");
			for (int i = 0; i < mapList.length; i++) {
				String[] str = mapList[i].split(",");
				if (str.length == 1 && !TextUtils.isEmpty(str[0])) {
					map.put(str[0], null);
				} else if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
					map.put(str[0], str[1]);
				}
			}
		}
		return map;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putAll(bundle);
		outState.putString("carSalesNo", carSalesNo);
		outState.putInt("isRecord", isRecord);
		outState.putBoolean("isEnble", isEnble);
		outState.putString("map", mapToString(map));
		outState.putString("firstCount", mapToString(firstCount));
		super.onSaveInstanceState(outState);
	}

//	@Override
//	protected void restoreConstants(Bundle savedInstanceState) {
//		super.restoreConstants(savedInstanceState);
//		carSalesNo = savedInstanceState.getString("carSalesNo");
//		isRecord = savedInstanceState.getInt("isRecord");
//		isEnble = savedInstanceState.getBoolean("isEnble", true);
//		map = stringToMap(savedInstanceState.getString("map"));
//		firstCount = stringToMap(savedInstanceState.getString("firstCount"));
//	}

	/**
	 * 针对S200型号手机扫描键和扫描结果的监听
	 * 
	 * @author qingli
	 *
	 */
	private BroadcastReceiver s200receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// Log.d(TAG, "ScankeyListenerService onReceive intent
			// ="+(intent.getAction()));
			if (PhoneModelManager.ACTION_SPECIALKEY.equals(intent.getAction())) {
				SerialPortOpen();

				if (fdUart <= 0) {
					return;
				}

				/* scan data */
				String str_read = "";
				try {
					str_read = SerialPort.receiveData(fdUart);
					if (!TextUtils.isEmpty(str_read)) {
						Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
						scanForResult(str_read);
					} else {
						Toast.makeText(context, "扫描失败,请重新扫描", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					SerialPortClose();
				}

			}

		}
	};
}
