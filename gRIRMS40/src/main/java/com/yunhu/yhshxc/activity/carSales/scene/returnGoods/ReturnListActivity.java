package com.yunhu.yhshxc.activity.carSales.scene.returnGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.management.BaseActivity;
import com.yunhu.yhshxc.activity.carSales.manager.CarSalesDataManager;
import com.yunhu.yhshxc.activity.carSales.print.CarSalesPrintForReturnGoods;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesPopupWindow;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.activity.carSales.zhy.tree_view.ReturnGoodsTreeAdapter;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.GCGListView;
import com.yunhu.yhshxc.widget.ToastOrder;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;
import tf.test.SerialPort;

public class ReturnListActivity extends BaseActivity implements OnList, DataSource {
	private GCGListView lv_all;
	private LinearLayout ll_home_yulan;
	private LinearLayout ll_prince;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_submit;
	private EditText et_tuikuan;
	private EditText et_leave_message;
	private ImageButton ib_scanning;
	private ReturnGoodsTreeAdapter adapter;
	private CarSalesStockDB carSalesStockDB;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesUtil util;
	Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_scale_return_goods_list);
		carSalesStockDB = new CarSalesStockDB(this);
		ctrlDB = new CarSalesProductCtrlDB(this);
		util = new CarSalesUtil(this);
		bundle = new Bundle();
		initWidget();
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
	// 是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint() {
		int isPrint = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsReturn());
		if (isPrint == 1) {
			ll_prince.setVisibility(View.GONE);
		} else {
			ll_prince.setVisibility(View.VISIBLE);
		}
		int isPhoto = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsReturnPhoto());
		if (isPhoto == 1) {
			ll_take_photo.setVisibility(View.GONE);
		} else {
			ll_take_photo.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("NewApi")
	private void initData() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		List<CarSalesProductCtrl> productCtrlsChild = util.returnProductCtrlList();
		for (int i = 0; i < productCtrlsChild.size(); i++) {
			ctrls.put(productCtrlsChild.get(i).getId(), productCtrlsChild.get(i));
			bundle.putString(String.valueOf(productCtrlsChild.get(i).getId()),
					productCtrlsChild.get(i).getReturnCount());
		}
		double allPrice = getTotalPrice();
		et_tuikuan.setText(PublicUtils.formatDouble(allPrice));
		SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this)
				.setTuiKuan(PublicUtils.formatDouble(allPrice));
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new ReturnGoodsTreeAdapter<CarSalesProductCtrl>(lv_all, this, productCtrlsChildsss, 10, this);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReturnListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
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
		ll_prince = (LinearLayout) findViewById(R.id.ll_prince);
		ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
		ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
		et_leave_message = (EditText) findViewById(R.id.et_leave_message);
		et_tuikuan = (EditText) findViewById(R.id.et_tuikuan);
		ib_scanning = (ImageButton) findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(listner);
		et_leave_message.addTextChangedListener(lyWatcher);
		et_tuikuan.addTextChangedListener(tkWatcher);
		controlPrint();
		et_tuikuan.setText(SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getTuiKuan());
		et_leave_message
				.setText(SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getLeaveMessage());
	}

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

	/**
	 * 退款监听，将退款数额存在ShaperPreferences中
	 */
	private TextWatcher tkWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String tk = s.toString();
			if (!TextUtils.isEmpty(tk)) {
				if (tk.equals(".")) {
					tk = "0";
				} else if (tk.startsWith(".")) {
					tk = "0" + tk;
				} else if (tk.endsWith(".")) {
					tk = tk + "0";
				}
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).setTuiKuan(tk);
			} else {
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).setTuiKuan("0");
			}
		}
	};
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
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).setLeaveMessage(msg);
			} else {
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).setLeaveMessage("");
			}
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
		popupWindow.show(null, SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getPhotoNameOne(),
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getPhotoNameTwo());
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
					ctrl.setReturnCount("1");
					ctrls.put(ctrl.getId(), ctrl);
					bundle.putString(String.valueOf(ctrl.getId()), String.valueOf(1));
					ToastOrder.makeText(ReturnListActivity.this, "添加成功", ToastOrder.LENGTH_LONG).show();
				} else {
					ToastOrder.makeText(ReturnListActivity.this, "退货列表已存在", ToastOrder.LENGTH_LONG).show();
					return;
				}

			} else {
				ToastOrder.makeText(ReturnListActivity.this, "该产品不存在，请确认是否扫描错误", ToastOrder.LENGTH_LONG).show();
				return;
			}
		}
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedCtrl(cs);
		initadapter();
		double allPrice = getTotalPrice();
		et_tuikuan.setText(PublicUtils.formatDouble(allPrice));
		SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this)
				.setTuiKuan(PublicUtils.formatDouble(allPrice));
	}

	@SuppressLint("NewApi")
	private void initadapter() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		List<CarSalesProductCtrl> productCtrlsChild = util.returnProductCtrlList();
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new ReturnGoodsTreeAdapter<CarSalesProductCtrl>(lv_all, this, productCtrlsChildsss, 10, this);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReturnListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
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
		ctrlDB.updateAllChangedCtrl(cs);
		if (bundle != null) {
			setResult(2, ReturnListActivity.this.getIntent().putExtras(bundle));
		}
		ReturnListActivity.this.finish();
	}

	/**
	 * 打印退货单
	 */
	private CarSalesPrintForReturnGoods carSalesPrint;

	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(ReturnListActivity.this, "car_sales_print_returning_goods.txt");
		try {
			List<CarSalesProductCtrl> productCtrls = util.returnProductCtrlList();
			carSalesPrint = new CarSalesPrintForReturnGoods(this);
			CarSales carSales = getCarSales(productCtrls);
			carSalesPrint.setCarSales(carSales);
			carSalesPrint.print(ReturnListActivity.this, new JSONArray(json), ReturnListActivity.this);
			int printCount = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount();
			SharedPreferencesForCarSalesUtil.getInstance(this).setCarSalesPrintCount(printCount + 1);
			;
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
	}

	private int isPromotion;

	public CarSales getCarSales(List<CarSalesProductCtrl> productCtrls) {
		CarSales carSales = null;
		carSales = new CarSales();
		carSales.setCarId(
				String.valueOf(SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getCarId()));
		carSales.setStoreId(SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getStoreId());
		carSales.setStoreName(
				SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this).getCarSalesStoreName());
		carSales.setCarSalesNo(util.carSalesNumber(true));
		carSales.setCarSalesTime(DateUtil.getCurDateTime());
		carSales.setImage1(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameOne());
		carSales.setImage2(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameTwo());
		carSales.setPrintCount(SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount());
		List<CarSalesProductData> carSalesPros = new ArrayList<CarSalesProductData>();
		if (productCtrls.size() > 0) {
			for (int i = 0; i < productCtrls.size(); i++) {
				CarSalesProductCtrl c = productCtrls.get(i);
				CarSalesProduct p = util.product(c.getProductId(), c.getUnitId());
				if (p != null) {
					CarSalesProductData pda = new CarSalesProductData();
					pda.setProductId(p.getProductId());
					String count = TextUtils.isEmpty(c.getReturnCount()) ? "0" : c.getReturnCount();
					pda.setCarSalesCount(Double.parseDouble(count));
					pda.setActualCount(Double.parseDouble(count));
					pda.setProductUnit(p.getUnit());
					pda.setUnitId(p.getUnitId());
					pda.setCarSalesPrice(p.getPrice());
					double allPrice = p.getPrice() * Double.parseDouble(count);
					pda.setCarSalesAmount(allPrice);
					pda.setActualAmount(allPrice);
					pda.setProductName(p.getName());
					pda.setIsCarSalesMain(1);
					// pda.setPromotionId(Integer.parseInt(p.getPromotionIds()));
					if (p.isPromotion()) {
						isPromotion = 1;
					}
					carSalesPros.add(pda);
				}
			}

		}
		carSales.setIsPromotion(isPromotion);
		if (carSalesPros.size() > 0) {
			carSales.setProductList(carSalesPros);
		}
		double amount = 0;
		double actualAmount = 0;
		for (int i = 0; i < carSalesPros.size(); i++) {
			amount += carSalesPros.get(i).getCarSalesAmount();
			actualAmount += carSalesPros.get(i).getActualAmount();
		}
		carSales.setCarSalesAmount(amount);
		carSales.setActualAmount(actualAmount);
		String tuiKuan = SharedPreferencesForCarSalesUtil.getInstance(this).getTuiKuan();
		carSales.setReturnAmount(Double.parseDouble(tuiKuan));
		String msg = SharedPreferencesForCarSalesUtil.getInstance(this).getLeaveMessage();
		carSales.setNote(msg);// 留言
		carSales.setStock(true);
		return carSales;
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

	private void submit() {
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedCtrl(cs);
		List<CarSalesProductCtrl> productCtrls = util.returnProductCtrlList();
		List<CarSalesProductCtrl> productCtrlsNew = new ArrayList<CarSalesProductCtrl>();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl ctrl = productCtrls.get(i);
			if (!TextUtils.isEmpty(ctrl.getReturnCount()) || ctrl.getReturnCount().equalsIgnoreCase("0")
					|| ctrl.getReturnCount().equalsIgnoreCase("0.0")) {
				productCtrlsNew.add(ctrl);
			}
		}
		setCommonList(productCtrls);
		updateStock(productCtrlsNew);
		CarSales carSales = getCarSales(productCtrlsNew);
		CarSalesDataManager manager = new CarSalesDataManager(this);
		manager.submit(carSales, UrlInfo.doReturnInfo(ReturnListActivity.this));
		SharedPreferencesForCarSalesUtil.getInstance(this).clearReturnSubmitMeassage();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl c = productCtrls.get(i);
			c.setReturnCount("");
			ctrlDB.updateProductCtrlReturnCount(c);
			bundle.putString(String.valueOf(c.getId()), "");
		}
		util.carSalesNumber(false);
		sendBroadcast(new Intent(Constants.BROADCAST_CARSALES_CREATE_SUCCESS));
		ctrls.clear();
		ToastOrder.makeText(ReturnListActivity.this, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
		returning();
	}

	/**
	 * 更新产品库存
	 */
	private void updateStock(List<CarSalesProductCtrl> productCtrls) {
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProduct product = util.product(productCtrls.get(i).getProductId(), productCtrls.get(i).getUnitId());
			if (product != null) {
				// 库存数 缺货数
				CarSalesStock stock = carSalesStockDB.findCarSalesStockByProductIdAndUnitId(product.getProductId(),
						product.getUnitId());
				if (stock != null) {
					double returncount = !TextUtils.isEmpty(productCtrls.get(i).getReturnCount())
							? Double.parseDouble(productCtrls.get(i).getReturnCount()) : 0;
					double stockNum = returncount + stock.getStockNum();
					double outNum = stock.getStockoutNum() - returncount;
					if (outNum <= 0) {
						outNum = 0;
					}
					// double reCount =
					// !TextUtils.isEmpty(productCtrls.get(i).getReplenishmentCount())?Double.parseDouble(productCtrls.get(i).getReplenishmentCount()):0;
					double replenishmentNum = stock.getReplenishmentNum() - returncount;
					if (replenishmentNum <= 0) {
						replenishmentNum = 0;
					}
					stock.setStockNum(stockNum);
					stock.setStockoutNum(outNum);
					stock.setReplenishmentNum(replenishmentNum);
					carSalesStockDB.updateCarSalesStock(stock);
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
		if (ctrl != null) {
			if (ctrls.containsKey(ctrl.getId())) {
				ctrls.remove(ctrl);
			}
			if (!TextUtils.isEmpty(str)) {
				ctrl.setReturnCount(str);
			} else {
				ctrl.setReturnCount("");
			}
			ctrls.put(ctrl.getId(), ctrl);
			bundle.putString(String.valueOf(ctrl.getId()), ctrl.getReturnCount());
		}
		double allPrice = getTotalPrice();
		et_tuikuan.setText(PublicUtils.formatDouble(allPrice));
		SharedPreferencesForCarSalesUtil.getInstance(ReturnListActivity.this)
				.setTuiKuan(PublicUtils.formatDouble(allPrice));
	}

	private double getTotalPrice() {
		double price = 0;
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			CarSalesProductCtrl ctrl = ctrls.get(key);
			CarSalesProduct product = util.product(ctrl.getProductId(), ctrl.getUnitId());
			if (product != null) {
				String num = !TextUtils.isEmpty(bundle.getString(String.valueOf(key)))
						? bundle.getString(String.valueOf(key)) : "0";
				price += getTotal(Double.parseDouble(num), product.getPrice());
			}
		}
		return price;
	}

	public double getTotal(double num, double price) {
		return num * price;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			returning();
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putAll(bundle);
		outState.putInt("isPromotion", isPromotion);
		super.onSaveInstanceState(outState);
	}

//	@Override
//	protected void restoreConstants(Bundle savedInstanceState) {
//		super.restoreConstants(savedInstanceState);
//		isPromotion = savedInstanceState.getInt("isPromotion");
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
