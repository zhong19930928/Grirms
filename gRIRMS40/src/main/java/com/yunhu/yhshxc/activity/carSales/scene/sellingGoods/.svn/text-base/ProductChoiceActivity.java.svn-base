package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.management.BaseActivity;
import com.yunhu.yhshxc.activity.carSales.scene.view.SellingGoodsView.OnShoppingCarListner;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.activity.carSales.zhy.tree_view.SellingGoodsTreeAdapter;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import tf.test.SerialPort;

/**
 * 卖货
 * 
 * @author xuelinlin
 *
 */
public class ProductChoiceActivity extends BaseActivity implements OnClickListener, OnShoppingCarListner {
	private LinearLayout ll_xia_shopping_car;
	private LinearLayout ll_xiadan_changyong;
	private LinearLayout ll_xiadan_all;
	private ListView lv_xiadan;
	private ListView lv_all;
	private ImageView iv_changyong;
	private ImageView iv_all;
	private TreeListViewAdapter mAdapter;
	private TreeListViewAdapter commonAdapter;
	private TextView tv_shopping_car_ll;
	private ImageButton ib_scanning;
	private boolean isOpenAll = false;
	private boolean isOpenCommom = true;
	private final int INDEX = 1;
	private String storeId;
	private CarSalesUtil util;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesShoppingCartDB carSalesShoppingCartDB;
	private CarSalesPromotionDB promotionDB;
	private Map<String, String> map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_scale_selling_goods_main);
		carSalesShoppingCartDB = new CarSalesShoppingCartDB(this);
		clearData();
		registRefreshProduct();
		registSubmitSuccessReceiver();
		storeId = SharedPreferencesForCarSalesUtil.getInstance(this).getStoreId();
		util = new CarSalesUtil(this);
		map = util.getStockCtrl();
		promotionDB = new CarSalesPromotionDB(this);
		ctrlDB = new CarSalesProductCtrlDB(ProductChoiceActivity.this);
		initWedigt();
		initData();
		registS200PhoneReceiver();
	}

	private void registS200PhoneReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PhoneModelManager.ACTION_SPECIALKEY);
		this.registerReceiver(s200receiver, filter);

	}

	/**
	 * 清除购物车和缓存数据
	 */
	private void clearData() {
		carSalesShoppingCartDB.delete();
		SharedPreferencesForCarSalesUtil.getInstance(this).clearStockCtrl();
		// clearNotSubmitData();
		SharedPreferencesForCarSalesUtil.getInstance(this).clearSubmitMeassage();
	}

	/**
	 * 清除拓展模块数据
	 */
	private void clearNotSubmitData() {
		String t = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesTimestamp();
		if (!TextUtils.isEmpty(t)) {
			SubmitDB db = new SubmitDB(this);
			Submit submit = db.findSubmitByTimestamp(t);
			if (submit != null && submit.getState() == Submit.STATE_NO_SUBMIT) {
				db.deleteSubmitById(submit.getId());
				new SubmitItemDB(this).deleteSubmitItemBySubmitId(submit.getId());
			}
		}
	}

	private void initWedigt() {
		ll_xia_shopping_car = (LinearLayout) this.findViewById(R.id.ll_xia_shopping_car);
		ll_xiadan_changyong = (LinearLayout) this.findViewById(R.id.ll_xiadan_changyong);
		ll_xiadan_all = (LinearLayout) this.findViewById(R.id.ll_xiadan_all);
		ll_xiadan_all.setOnClickListener(this);
		ll_xiadan_changyong.setOnClickListener(this);
		lv_xiadan = (ListView) this.findViewById(R.id.lv_xiadan);
		lv_all = (ListView) this.findViewById(R.id.lv_all);
		iv_changyong = (ImageView) this.findViewById(R.id.iv_changyong);
		iv_all = (ImageView) this.findViewById(R.id.iv_all);
		tv_shopping_car_ll = (TextView) this.findViewById(R.id.tv_shopping_car_ll);
		ib_scanning = (ImageButton) this.findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(this);
		ll_xia_shopping_car.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		try {
			initAllProductCtrl();
			initCommonProductCtrl();
		} catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi")
	private void initCommonProductCtrl() throws IllegalArgumentException, IllegalAccessException {
		// 换实体类对象 泛型匹配
		List<CarSalesProductCtrl> productCtrlList = util.commonProductCtrlList();
		if (productCtrlList.size() > 0) {

			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
			commonAdapter = new SellingGoodsTreeAdapter<CarSalesProductCtrl>(lv_xiadan, ProductChoiceActivity.this,
					productCtrlList, 10, ProductChoiceActivity.this);
			commonAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {

					if (node.isLeaf()) {
						if (node.getProductCtrl() != null) {
							CarSalesProduct product = util.product(node.getProductCtrl().getProductId(),
									node.getProductCtrl().getUnitId());
							if (product != null) {
								util.saveStockCtrl(map);
								Intent i = new Intent(ProductChoiceActivity.this, CarSaleProductDetailActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt("index", INDEX);
								bundle.putInt("productId", product.getProductId());
								bundle.putInt("unitId", product.getUnitId());
								i.putExtras(bundle);
								startActivity(i);
							} else {
								ToastOrder.makeText(ProductChoiceActivity.this, "没有可购买的产品", ToastOrder.LENGTH_SHORT)
										.show();
							}
						} else {
							return;
						}

					}
				}

			});
			lv_xiadan.setAdapter(commonAdapter);
			lv_xiadan.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	@SuppressLint("NewApi")
	private void initAllProductCtrl() throws IllegalArgumentException, IllegalAccessException {
		List<CarSalesProductCtrl> productCtrlList = util.allProductCtrlList();

		if (productCtrlList != null && productCtrlList.size() > 0) {
			mAdapter = new SellingGoodsTreeAdapter<CarSalesProductCtrl>(lv_all, ProductChoiceActivity.this,
					productCtrlList, 10, ProductChoiceActivity.this);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						CarSalesProductCtrl ctrl = node.getProductCtrl();
						CarSalesProduct product = util.product(ctrl.getProductId(), ctrl.getUnitId());
						if (product != null) {
							util.saveStockCtrl(map);
							Intent i = new Intent(ProductChoiceActivity.this, CarSaleProductDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("productId", ctrl.getProductId());
							bundle.putInt("unitId", ctrl.getUnitId());
							bundle.putInt("index", INDEX);
							i.putExtras(bundle);
							startActivity(i);
						} else {
							ToastOrder.makeText(ProductChoiceActivity.this, "没有可购买的产品", ToastOrder.LENGTH_SHORT).show();
						}
					} else {
						return;
					}
				}
			});
			lv_all.setAdapter(mAdapter);
			lv_all.setOverScrollMode(View.OVER_SCROLL_NEVER);

		} else {
			ToastOrder.makeText(this, "产品不存在", ToastOrder.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_scanning:// 产品条形码扫描
			scan();
			break;
		case R.id.ll_xia_shopping_car:// 进入购物车页面
			goToShoppingCar();
			break;
		case R.id.ll_xiadan_all:// 显示或隐藏所有商品列表
			openOrDown();
			break;
		case R.id.ll_xiadan_changyong:// 显示或隐藏常用产品列表
			openCommon();
			break;
		default:
			break;
		}

	}

	private void openCommon() {
		// if(!TextUtils.isEmpty(storeId)){
		if (isOpenCommom) {
			isOpenCommom = false;
			lv_xiadan.setVisibility(View.GONE);
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
		} else {
			isOpenCommom = true;
			lv_xiadan.setVisibility(View.VISIBLE);
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
		}
		// }else{
		// Toast.makeText(ProductChoiceActivity.this,
		// "请先选择客户",Toast.LENGTH_SHORT).show();
		// }
	}

	private void openOrDown() {
		// if(!TextUtils.isEmpty(storeId)){
		if (isOpenAll) {
			isOpenAll = false;
			lv_all.setVisibility(View.GONE);
			iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
		} else {
			isOpenAll = true;
			lv_all.setVisibility(View.VISIBLE);
			iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
		}
		// }else{
		// Toast.makeText(ProductChoiceActivity.this,
		// "请先选择客户",Toast.LENGTH_SHORT).show();
		// }
	}

	private void goToShoppingCar() {
		// if (!TextUtils.isEmpty(storeId)) {
		int count = carSalesShoppingCartDB.count();
		if (count > 0) {
			util.saveStockCtrl(map);
			Intent intent = new Intent(ProductChoiceActivity.this, CarSaleShoppingCartActivity.class);
			startActivity(intent);
		} else {
			ToastOrder.makeText(this, "您没选购产品", ToastOrder.LENGTH_SHORT).show();
		}
		// }else{
		// Toast.makeText(ProductChoiceActivity.this,
		// "请先选择客户",Toast.LENGTH_SHORT).show();
		// }

	}

	/**
	 * 扫码
	 */
	private void scan() {
		if (!TextUtils.isEmpty(storeId)) {
			// Intent i = new Intent(this, CaptureActivity.class);
			Intent i = PhoneModelManager.getIntent(this,false);
			if (i!=null) {				
				
				startActivityForResult(i, 201);
			}
		} else {
			ToastOrder.makeText(ProductChoiceActivity.this, "请先选择客户", ToastOrder.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照 扫描返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.scan_succeeded && requestCode == 201) {
			String scanCode = null;
			scanCode = data.getStringExtra("SCAN_RESULT");
			scanForResult(scanCode);
		}
	}

	private void scanForResult(String code) {
		if (!TextUtils.isEmpty(code)) {
			CarSalesProduct product = util.productForCode(code);
			if (product != null) {
				initCart(product);
				Intent i = new Intent(ProductChoiceActivity.this, CarSaleShoppingCartActivity.class);
				startActivity(i);
			} else {
				ToastOrder.makeText(ProductChoiceActivity.this, "该产品不存在", ToastOrder.LENGTH_LONG).show();
			}
		}
	}

	private void initCart(CarSalesProduct product) {
		CarSalesShoppingCart cart = carSalesShoppingCartDB.findCarSalesShoppingCartByProductId(product.getProductId(),
				product.getUnitId());
		if (cart == null) {
			String orgCode = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesOrgId();
			int level = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreLevel();
			List<CarSalesPromotion> promos = promotionDB.findPromotionByProductIdAndUnitId(product.getProductId(),
					product.getUnitId(), orgCode, level);
			cart = new CarSalesShoppingCart();
			cart.setProductId(product.getProductId());
			cart.setUnitId(product.getUnitId());
			// int min =
			// SharedPreferencesForCarSalesUtil.getInstance(ProductChoiceActivity.this).getMinNum();
			double min = 0;
			String minString = SharedPreferencesForCarSalesUtil.getInstance(ProductChoiceActivity.this).getMinNum();
			if (Double.parseDouble(minString) > 0) {
				min = Double.parseDouble(minString);
			} else {
				min = 1;
			}
			cart.setNumber(min);
			cart.setSubtotal(getTotal(min, product.getPrice()));
			cart.setPitcOn(1);
			CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(),
					product.getUnitId());
			if (ctrl != null) {
				cart.setpId(ctrl.getpId());
				map.put(String.valueOf(ctrl.getId()), String.valueOf(product.getInventory() - min));
				util.saveStockCtrl(map);
			}
			cart.setNowProductPrict(product.getPrice());
			if (promos != null && promos.size() > 0) {
				cart.setPromotionIds(String.valueOf(promos.get(0).getPromotionId()));
				util.fenLei(promos.get(0), product, cart, min);
			} else {
				cart.setPromotionIds(null);
			}
			carSalesShoppingCartDB.insert(cart);
		} else {
			carSalesShoppingCartDB.updateCarSalesShoppingCart(cart);
		}
	}

	public double getTotal(double number, double price) {
		double total = number * price;
		return total;
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			initCommonProductCtrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map = util.getStockCtrl();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			TextView tv = arr.get(key);
			if (tv != null) {
				if (!TextUtils.isEmpty(map.get(key))) {
					// JLog.d("aaa", key+" +++++++ "+map.get(key));
					// if(Double.parseDouble(map.get(key))>0){
					// }
					tv.setText("库存 : " + map.get(key));
				}
			} else {

			}
		}
		mAdapter.notifyDataSetChanged();
		if (commonAdapter != null) {
			commonAdapter.notifyDataSetChanged();
		}
		initShoppingCartCount();
	}

	/**
	 * 设置购物车数量
	 */
	private void initShoppingCartCount() {
		int count = carSalesShoppingCartDB.count();
		if (count > 0) {
			tv_shopping_car_ll.setVisibility(View.VISIBLE);
			if (count > 99) {
				tv_shopping_car_ll.setText("99+");
			} else {
				tv_shopping_car_ll.setText("" + count);
			}
		} else {
			tv_shopping_car_ll.setVisibility(View.INVISIBLE);
		}
	}

	Map<String, TextView> arr = new HashMap<String, TextView>();

	@Override
	public void onFindAllList(CarSalesProductCtrl data, TextView tv, double number) {
		initShoppingCartCount();
		// arr.put(data.getId(), tv);
		map.put(String.valueOf(data.getId()), String.valueOf(number));
		util.saveStockCtrl(map);
		if (number > 0) {
			tv.setText("库存：" + PublicUtils.formatDouble(number));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			int count = carSalesShoppingCartDB.count();
			if (count > 0) {
				backDialog("退出后将清除购物车以及未提交数据,是否确定退出?").show();
				return true;
			} else {
				finish();
			}
		} else {// 如果不是返回键,就判断是否是特殊机型的扫描按键,如果是就调用扫描头
			if (PhoneModelManager.isStartScan(this, keyCode, event)) {
				scan();
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private Dialog backDialog(String bContent) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		backContent.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			/**
			 * 用户退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				ProductChoiceActivity.this.finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			/**
			 * 取消退出
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);// 将对话框设置为模态
		return backDialog;
	}

	private void registSubmitSuccessReceiver() {
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_CARSALES_CREATE_SUCCESS);
		registerReceiver(submitSuccessReceiver, filter);
	}

	private void registRefreshProduct() {
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_CAR_SALES_REFRASH_PRODUCT);
		registerReceiver(refreshProduct, filter);
	}

	/**
	 * 下订单提交数据成功
	 */
	private BroadcastReceiver submitSuccessReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.BROADCAST_CARSALES_CREATE_SUCCESS.equals(intent.getAction())) {
				try {
					initCommonProductCtrl();
				} catch (Exception e) {
					ToastOrder.makeText(ProductChoiceActivity.this, "常用列表数据更新异常", ToastOrder.LENGTH_SHORT).show();
				}
			}
		}

	};
	/**
	 * 重新刷新列表
	 */
	private BroadcastReceiver refreshProduct = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.BROADCAST_CAR_SALES_REFRASH_PRODUCT.equals(intent.getAction())) {
				initData();
			}
		}
	};

	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(refreshProduct);
		unregisterReceiver(submitSuccessReceiver);
		unregisterReceiver(s200receiver);
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
		outState.putString("storeId", storeId);
		outState.putString("map", mapToString(map));
		outState.putBoolean("isOpenAll", isOpenAll);
		outState.putBoolean("isOpenCommom", isOpenCommom);
		super.onSaveInstanceState(outState);
	}

//	@Override
//	protected void restoreConstants(Bundle savedInstanceState) {
//		super.restoreConstants(savedInstanceState);
//		isOpenCommom = savedInstanceState.getBoolean("isOpenCommom", true);
//		isOpenAll = savedInstanceState.getBoolean("isOpenAll", false);
//		map = stringToMap(savedInstanceState.getString("map"));
//		storeId = savedInstanceState.getString("storeId");
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
