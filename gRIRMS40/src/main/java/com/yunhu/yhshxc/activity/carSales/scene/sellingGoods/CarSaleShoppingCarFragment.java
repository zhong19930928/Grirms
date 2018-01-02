package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.scene.view.ShoppingCarView;
import com.yunhu.yhshxc.activity.carSales.scene.view.ShoppingCarView.OnCheckBox;
import com.yunhu.yhshxc.activity.carSales.scene.view.ShoppingCarView.OnProNumCh;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import tf.test.SerialPort;
import tf.test.SpecialKey;

public class CarSaleShoppingCarFragment extends Fragment implements OnCheckBox, OnProNumCh {
	private ListView lv_shopping_car;
	private TextView tv_shopping_car_allmoney;
	private ImageButton ib_scanning;
	private CarSaleShoppingCarAdapter adapter;
	private List<CarSalesShoppingCart> carSalesShoppingCarts;
	private Context mContext;
	private CarSalesShoppingCartDB carSalesShoppingCartDB;
	private CarSalesPromotionDB promotionDB;
	private CarSalesProductCtrlDB ctrlDB;
	CarSalesProduct carSalesProduct;
	CarSalesUtil util;
	// 购物车中所有选中享受总和优惠的产品
	private List<CarSalesShoppingCart> cartsCheckedIsDouble = new ArrayList<CarSalesShoppingCart>();
	// 购物车中所有选中不享受总和优惠的产品
	private List<CarSalesShoppingCart> cartsCheckedNoDouble = new ArrayList<CarSalesShoppingCart>();
	
	//S200手机的适配
	private SpecialKey mSpecialKey;
	private int fdUart = -1;
	private static final String SCANNER_DEV = "/dev/ttyMT0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// View view = inflater.inflate(R.layout.car_scale_shopping_car, null);
		mContext=getActivity();
		View view = inflater.inflate(R.layout.car_scale_shopping_car, container, false);
		initdb();
		carSalesShoppingCarts = carSalesShoppingCartDB.findAllList();
		initView(view);
		registS200PhoneReceiver();
		return view;
	}

	private void registS200PhoneReceiver() {
		if (PhoneModelManager.isScanS200Phone(mContext)) {// 如果是S200手机就进行按键监听的注册
			mSpecialKey= new SpecialKey();
			mSpecialKey.startListenthread();
			mSpecialKey.SendContext(mContext);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(PhoneModelManager.ACTION_SPECIALKEY);
		mContext.registerReceiver(s200receiver, filter);

	}

	public void initdb() {
		carSalesShoppingCartDB = new CarSalesShoppingCartDB(getActivity());
		promotionDB = new CarSalesPromotionDB(getActivity());
		ctrlDB = new CarSalesProductCtrlDB(getActivity());
		util = new CarSalesUtil(getActivity());
	}

	private void initView(View view) {
		lv_shopping_car = (ListView) view.findViewById(R.id.lv_shopping_car);
		tv_shopping_car_allmoney = (TextView) view.findViewById(R.id.tv_shopping_car_allmoney);
		ib_scanning = (ImageButton) view.findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(listner);
		adapter = new CarSaleShoppingCarAdapter(getActivity(), carSalesShoppingCarts);
		lv_shopping_car.setAdapter(adapter);
		initTotalPrice();
	}

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			scan();
		}
	};

	private void scan() {
		// Intent i = new Intent(getActivity(), CaptureActivity.class);
		Intent i = PhoneModelManager.getIntent(getActivity(),false);
		if (i!=null) {				
			
			startActivityForResult(i, 202);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.scan_succeeded && requestCode == 202) {
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
				carSalesShoppingCarts = carSalesShoppingCartDB.findAllList();
				adapter.refresh(carSalesShoppingCarts);
			} else {
				ToastOrder.makeText(getActivity(), "该产品不存在", ToastOrder.LENGTH_LONG).show();
			}
		}
	}

	private void initCart(CarSalesProduct product) {
		CarSalesShoppingCart cart = carSalesShoppingCartDB.findCarSalesShoppingCartByProductId(product.getProductId(),
				product.getUnitId());
		if (cart == null) {
			String orgCode = SharedPreferencesForCarSalesUtil.getInstance(getActivity()).getCarSalesOrgId();
			int level = SharedPreferencesForCarSalesUtil.getInstance(getActivity()).getCarSalesStoreLevel();
			List<CarSalesPromotion> promos = promotionDB.findPromotionByProductIdAndUnitId(product.getProductId(),
					product.getUnitId(), orgCode, level);
			cart = new CarSalesShoppingCart();
			cart.setProductId(product.getProductId());
			cart.setUnitId(product.getUnitId());
			// int min =
			// SharedPreferencesForCarSalesUtil.getInstance(getActivity()).getMinNum();
			double min = 0;
			String minString = SharedPreferencesForCarSalesUtil.getInstance(getActivity()).getMinNum();
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
				Map<String, String> map = ((CarSaleShoppingCartActivity) getActivity()).getMap();
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

	class CarSaleShoppingCarAdapter extends BaseAdapter {
		private Context context;
		private List<CarSalesShoppingCart> carts;

		public CarSaleShoppingCarAdapter(Context context, List<CarSalesShoppingCart> carts) {
			this.context = context;
			this.carts = carts;
		}

		@Override
		public int getCount() {
			return carts.size();
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
			ShoppingCarView view = null;
			if (convertView == null) {
				view = new ShoppingCarView(getActivity(), CarSaleShoppingCarFragment.this,
						CarSaleShoppingCarFragment.this);
				convertView = view.getView();
				convertView.setTag(view);
			} else {
				view = (ShoppingCarView) convertView.getTag();
			}

			view.initData(position, carts.get(position));
			return convertView;
		}

		public void refresh(List<CarSalesShoppingCart> cs) {
			carts = cs;
			notifyDataSetChanged();
		}
	}

	@Override
	public void onProNumChanged() {
		initTotalPrice();
	}

	List<CarSalesProductCtrl> ProductsList = new ArrayList<CarSalesProductCtrl>();

	@Override
	public void allPrice(int position, boolean isCheck, CarSalesProductCtrl order3ProductCtrl) {
		if (isCheck) {
			ProductsList.add(order3ProductCtrl);
		} else {
			ProductsList.remove(order3ProductCtrl);
		}
		initTotalPrice();
	}

	public List<CarSalesProductCtrl> getProductsList() {
		return ProductsList;
	}

	public void initTotalPrice() {
		carSalesShoppingCarts = carSalesShoppingCartDB.findAllList();
		double allMoneyPub = util.allMoney(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts);
		double allMonNoDou = util.allMoneyNoDouble(cartsCheckedNoDouble);
		if (SharedPreferencesForCarSalesUtil.getInstance(getActivity()).getIsPromotion() == 1) {
			allNoDoubleMoeny = allMoneyPub + allMonNoDou;
			tv_shopping_car_allmoney.setText("共计: ￥ " + PublicUtils.formatDouble(allNoDoubleMoeny) + " 元");
		} else {
			double allM = 0;
			double allNum = util.allNum(cartsCheckedIsDouble);
			double fenLeiFND3 = util.zongHeFullNum(cartsCheckedIsDouble);// 买满数量打折后价钱
																			// 买满数量减免金额
			double fenLeiFND4 = util.zongHeFullNumDis(allMoneyPub, allNum);// 买满数量打折后价钱
																			// 买满数量减免打折
			double fenLeiFND5 = util.zongHeFullPrice(allMoneyPub);// 买满数量打折后价钱
																	// 买满金额减免金额
			double fenLeiFND6 = util.zongHeFullPriceDis(allMoneyPub);// 买满数量打折后价钱
																		// 买满金额减免打折
			double[] a = { fenLeiFND3, fenLeiFND4, fenLeiFND5, fenLeiFND6 };
			// JLog.d("aaa", fenLeiFND3+" "+fenLeiFND4+" "+fenLeiFND5+"
			// "+fenLeiFND6);
			double s = 0;
			for (int i = 0; i < 4; i++) {
				allM = Math.max(allM, a[i]);
			}
			if (allM != 0) {
				allNoDoubleMoeny = allMoneyPub + allMonNoDou - allM;
				tv_shopping_car_allmoney.setText("共计: ￥ " + PublicUtils.formatDouble(allNoDoubleMoeny) + " 元");
			} else {
				allNoDoubleMoeny = allMoneyPub + allMonNoDou;
				tv_shopping_car_allmoney.setText("共计: ￥ " + PublicUtils.formatDouble(allNoDoubleMoeny) + " 元");
			}
		}

	}

	double allNoDoubleMoeny;

	public double getActulAmo() {
		initTotalPrice();
		return allNoDoubleMoeny;
	}

	public double getTotalPrice() {
		return util.allMoney(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts)
				+ util.allMoneyNoDouble(cartsCheckedNoDouble);
	}

	
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mContext.unregisterReceiver(s200receiver);
	
	}

	
	

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
						Toast.makeText(mContext, "扫描成功", Toast.LENGTH_SHORT).show();
						scanForResult(str_read);
					} else {
						Toast.makeText(mContext, "扫描失败,请重新扫描", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					SerialPortClose();
				}

			}

		}
	};
	private void SerialPortClose() {
		try {
			SerialPort.close(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		fdUart = -1;
	}

	private void SerialPortOpen() {
		try {
			fdUart = SerialPort.open(SCANNER_DEV);
			if (fdUart <= 0) {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
