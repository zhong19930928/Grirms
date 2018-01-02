package com.yunhu.yhshxc.activity.carSales.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.manager.CarSalesDataManager;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.activity.carSales.zhy.tree_view.ReplenishmentTreeAdapter;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PhoneModelManager;
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
import android.widget.TextView;
import android.widget.Toast;
import tf.test.SerialPort;

public class ReplenishmentListActivity extends BaseActivity implements OnList {
	private GCGListView lv_all;
	private LinearLayout ll_home_yulan;
	private LinearLayout ll_prince;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_submit;
	private ImageButton ib_scanning;
	private LinearLayout leftL;
	private EditText et_tuikuan;
	private EditText et_leave_message;
	private TextView tv_menu;
	private ReplenishmentTreeAdapter adapter;
	private CarSalesStockDB carSalesStockDB;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesUtil util;
	private Bundle bundle;

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
	@SuppressLint("NewApi")
	private void initData() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		List<CarSalesProductCtrl> productCtrlsChild = util.rplenishmentProductCtrlList();
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new ReplenishmentTreeAdapter<CarSalesProductCtrl>(lv_all, ReplenishmentListActivity.this,
					productCtrlsChildsss, 10, ReplenishmentListActivity.this);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReplenishmentListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG)
									.show();
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
		leftL.setVisibility(View.GONE);
		ll_prince = (LinearLayout) findViewById(R.id.ll_prince);
		ll_prince.setVisibility(View.GONE);
		ll_take_photo = (LinearLayout) findViewById(R.id.ll_take_photo);
		ll_take_photo.setVisibility(View.GONE);
		ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
		et_leave_message = (EditText) findViewById(R.id.et_leave_message);
		et_tuikuan = (EditText) findViewById(R.id.et_tuikuan);
		tv_menu = (TextView) findViewById(R.id.tv_menu);
		tv_menu.setText("补货列表");
		ib_scanning = (ImageButton) findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(listner);
		et_leave_message.addTextChangedListener(lyWatcher);
		et_leave_message.setText(
				SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).getLeaveMessage());
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
				SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).setLeaveMessage(msg);
			} else {
				SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).setLeaveMessage("");
			}
		}
	};

	public void LayoutOnclickMethod(View layout) {
		switch (layout.getId()) {
		case R.id.ll_home_yulan:// 返回
			returning();
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
		}
	}

	private void scanForResult(String code) throws IllegalAccessException {
		if (!TextUtils.isEmpty(code)) {
			CarSalesProduct product = util.productForCode(code);
			if (product != null) {
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(),
						product.getUnitId());
				if (!ctrls.containsKey(ctrl.getId())) {
					ctrl.setReplenishmentCount("1");
					ctrls.put(ctrl.getId(), ctrl);
					bundle.putString(String.valueOf(ctrl.getId()), String.valueOf(1));
					ToastOrder.makeText(ReplenishmentListActivity.this, "添加成功", ToastOrder.LENGTH_LONG).show();
				} else {
					ToastOrder.makeText(ReplenishmentListActivity.this, "补货列表已存在", ToastOrder.LENGTH_LONG).show();
					return;
				}

			} else {
				ToastOrder.makeText(ReplenishmentListActivity.this, "该产品不存在，请确认是否扫描错误", ToastOrder.LENGTH_LONG).show();
				return;
			}
		}
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedRplenishmentCtrl(cs);
		initadapter();
	}

	@SuppressLint("NewApi")
	private void initadapter() throws IllegalArgumentException, IllegalAccessException {
		HashMap<String, CarSalesProductCtrl> arr = new HashMap<String, CarSalesProductCtrl>();
		List<CarSalesProductCtrl> productCtrlsChild = util.rplenishmentProductCtrlList();
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			for (int i = 0; i < productCtrlsChild.size(); i++) {
				ctrlDB.findReturnProductParent(productCtrlsChild.get(i), arr);
			}
		}
		List<CarSalesProductCtrl> productCtrlsChildsss = ctrlDB.getAllReturnList(productCtrlsChild, arr);
		if (productCtrlsChildsss != null && productCtrlsChildsss.size() > 0) {
			adapter = new ReplenishmentTreeAdapter<CarSalesProductCtrl>(lv_all, this, productCtrlsChildsss, 10, this);
			adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReplenishmentListActivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG)
									.show();
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
		ctrlDB.updateAllChangedRplenishmentCtrl(cs);
		;
		if (bundle != null) {
			setResult(223, ReplenishmentListActivity.this.getIntent().putExtras(bundle));
		}
		this.finish();
	}

	private void submit() {
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedRplenishmentCtrl(cs);
		List<CarSalesProductCtrl> productCtrls = util.rplenishmentProductCtrlList();
		List<CarSalesProductCtrl> productCtrlsNew = new ArrayList<CarSalesProductCtrl>();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl ctrl = productCtrls.get(i);
			if (!TextUtils.isEmpty(ctrl.getReplenishmentCount()) || ctrl.getReplenishmentCount().equalsIgnoreCase("0")
					|| ctrl.getReplenishmentCount().equalsIgnoreCase("0.0")) {
				productCtrlsNew.add(ctrl);
			}
		}
		setCommonList(productCtrls);
		updateStock(productCtrlsNew);
		CarSales carSales = getCarSales(productCtrlsNew);
		CarSalesDataManager manager = new CarSalesDataManager(this);
		manager.submit(carSales, UrlInfo.doFillInfo(ReplenishmentListActivity.this));
		SharedPreferencesForCarSalesUtil.getInstance(this).clearReturnSubmitMeassage();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl c = productCtrls.get(i);
			c.setReplenishmentCount("");
			ctrlDB.updateProductCtrlrRplenishmentCount(c);
			bundle.putString(String.valueOf(c.getId()), "");
		}
		// util.carSalesNumber(false);
		sendBroadcast(new Intent(Constants.BROADCAST_CARSALES_CREATE_SUCCESS));
		ctrls.clear();
		ToastOrder.makeText(ReplenishmentListActivity.this, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
		returning();
	}

	private void updateStock(List<CarSalesProductCtrl> productCtrls) {
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProduct product = util.product(productCtrls.get(i).getProductId(), productCtrls.get(i).getUnitId());
			if (product != null) {
				// 补货数
				double replenishmentNum = Double.parseDouble(productCtrls.get(i).getReplenishmentCount());
				carSalesStockDB.updateCarSalesStockReplenishmentNum(productCtrls.get(i).getProductId(),
						productCtrls.get(i).getUnitId(), String.valueOf(replenishmentNum));
			}
		}
	}

	private void setCommonList(List<CarSalesProductCtrl> productCtrls) {
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl produCtrl = ctrlDB.findProductCtrlByProductIdAndUnitId(
					productCtrls.get(i).getProductId(), productCtrls.get(i).getUnitId());
			if (produCtrl != null) {
				ctrlDB.updateProductCtrlCount(produCtrl);
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
				ctrl.setReplenishmentCount(str);
			} else {
				ctrl.setReplenishmentCount("");
			}
			ctrls.put(ctrl.getId(), ctrl);
			bundle.putString(String.valueOf(ctrl.getId()), ctrl.getReplenishmentCount());
		}
	}

	public CarSales getCarSales(List<CarSalesProductCtrl> productCtrls) {
		CarSales carSales = null;
		carSales = new CarSales();
		carSales.setCarId(String
				.valueOf(SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).getCarId()));
		carSales.setStoreId(SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).getStoreId());
		carSales.setStoreName(
				SharedPreferencesForCarSalesUtil.getInstance(ReplenishmentListActivity.this).getCarSalesStoreName());
		carSales.setCarSalesNo(util.carSalesNumber(true));
		carSales.setCarSalesTime(DateUtil.getCurDateTime());
		List<CarSalesProductData> carSalesPros = new ArrayList<CarSalesProductData>();
		if (productCtrls.size() > 0) {
			for (int i = 0; i < productCtrls.size(); i++) {
				CarSalesProductCtrl c = productCtrls.get(i);
				CarSalesProduct p = util.product(c.getProductId(), c.getUnitId());
				if (p != null) {
					CarSalesProductData pda = new CarSalesProductData();
					pda.setProductId(p.getProductId());
					String count = TextUtils.isEmpty(c.getReplenishmentCount()) ? "0" : c.getReplenishmentCount();
					pda.setCarSalesCount(Double.parseDouble(count));
					pda.setActualCount(Double.parseDouble(count));
					pda.setProductUnit(p.getUnit());
					pda.setUnitId(p.getUnitId());
					pda.setCarSalesPrice(p.getPrice());
					pda.setProductName(p.getName());
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			returning();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPutView(Node node, EditText ev) {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putAll(bundle);
		super.onSaveInstanceState(outState);
	}

//	@Override
//	protected void restoreConstants(Bundle savedInstanceState) {
//		super.restoreConstants(savedInstanceState);
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