package com.yunhu.yhshxc.activity.carSales.scene.returnGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.management.BaseActivity;
import com.yunhu.yhshxc.activity.carSales.scene.view.ReturnGoodsView.OnList;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.activity.carSales.zhy.tree_view.ReturnGoodsTreeAdapter;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.widget.ToastOrder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import tf.test.SerialPort;

/**
 * 退货
 * 
 * @author xuelinlin
 *
 */
public class ReturnGoodsAcitivity extends BaseActivity implements OnList {
	private LinearLayout ll_xiadan_changyong;
	private LinearLayout ll_xiadan_all;
	private LinearLayout ll_xia_shopping_car;
	private ImageView iv_changyong;
	private ImageView iv_all;
	private ListView lv_xiadan;
	private ListView lv_all;
	private ImageButton ib_scanning;
	private TextView tv_shopping_car_ll;
	private TextView tv_bar_name;
	private TextView tv_location_name;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesUtil util;
	private ReturnGoodsTreeAdapter mAdapter;
	private ReturnGoodsTreeAdapter commonAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_scale_loading_main);
		ctrlDB = new CarSalesProductCtrlDB(this);
		util = new CarSalesUtil(this);
		clearData();
		registRefreshProduct();
		registSubmitSuccessReceiver();
		initWidget();
		initData();
		initReturnGooodsCount();
		registS200PhoneReceiver();
	}

	private void registS200PhoneReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PhoneModelManager.ACTION_SPECIALKEY);
		this.registerReceiver(s200receiver, filter);

	}

	public void clearData() {
		List<CarSalesProductCtrl> productCtrls = util.returnProductCtrlList();
		for (int i = 0; i < productCtrls.size(); i++) {
			CarSalesProductCtrl c = productCtrls.get(i);
			c.setReturnCount("");
			ctrlDB.updateProductCtrlReturnCount(c);
		}
		SharedPreferencesForCarSalesUtil.getInstance(this).clearReturnSubmitMeassage();
	}

	private void initWidget() {
		ll_xiadan_changyong = (LinearLayout) findViewById(R.id.ll_xiadan_changyong);
		ll_xiadan_changyong.setOnClickListener(listner);
		ll_xiadan_all = (LinearLayout) findViewById(R.id.ll_xiadan_all);
		ll_xiadan_all.setOnClickListener(listner);
		ll_xia_shopping_car = (LinearLayout) findViewById(R.id.ll_xia_shopping_car);
		ll_xia_shopping_car.setOnClickListener(listner);
		iv_changyong = (ImageView) findViewById(R.id.iv_changyong);
		iv_all = (ImageView) findViewById(R.id.iv_all);
		lv_xiadan = (ListView) findViewById(R.id.lv_xiadan);
		lv_all = (ListView) findViewById(R.id.lv_all);
		ib_scanning = (ImageButton) findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(listner);
		tv_shopping_car_ll = (TextView) findViewById(R.id.tv_shopping_car_ll);
		tv_bar_name = (TextView) findViewById(R.id.tv_bar_name);
		tv_bar_name.setText("退货产品选择");
		tv_location_name = (TextView) findViewById(R.id.tv_location_name);
		tv_location_name.setText("退货列表");
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
		List<CarSalesProductCtrl> productCtrlList = util.commonProductCtrlList();
		if (productCtrlList.size() > 0) {
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
			commonAdapter = new ReturnGoodsTreeAdapter<CarSalesProductCtrl>(lv_xiadan, ReturnGoodsAcitivity.this,
					productCtrlList, 10, ReturnGoodsAcitivity.this);
			commonAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (!node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReturnGoodsAcitivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
						}
					}
				}
			});
			lv_xiadan.setAdapter(commonAdapter);
			lv_xiadan.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	// List<CarSalesProductCtrl> productCtrlList ;
	@SuppressLint("NewApi")
	private void initAllProductCtrl() throws IllegalArgumentException, IllegalAccessException {
		List<CarSalesProductCtrl> productCtrlList = util.allProductCtrlList();
		if (productCtrlList != null && productCtrlList.size() > 0) {
			mAdapter = new ReturnGoodsTreeAdapter<CarSalesProductCtrl>(lv_all, ReturnGoodsAcitivity.this,
					productCtrlList, 10, ReturnGoodsAcitivity.this);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						if (!node.getProductCtrl().isProductLevel()) {
							ToastOrder.makeText(ReturnGoodsAcitivity.this, "没有可选购产品", ToastOrder.LENGTH_LONG).show();
						}
					}
				}
			});
			lv_all.setAdapter(mAdapter);
			lv_all.setOverScrollMode(View.OVER_SCROLL_NEVER);

		} else {
			ToastOrder.makeText(this, "产品不存在", ToastOrder.LENGTH_LONG).show();
		}
	}

	/**
	 * 设置退货数量
	 */
	private void initReturnGooodsCount() {
		int count = 0;
		// Set<Integer> set = ctrls.keySet();
		// Iterator<Integer> it = set.iterator();
		// while(it.hasNext()){
		// String s =ctrls.get(it.next()).getReturnCount();
		// if(!TextUtils.isEmpty(s)&&Double.parseDouble(s)!=0){
		// count += 1;
		// }
		// }
		for (Map.Entry<String, CarSalesProductCtrl> entry : ctrls.entrySet()) {
			CarSalesProductCtrl ctrl = entry.getValue();
			String s = ctrl.getReturnCount();
			if (!TextUtils.isEmpty(s) && Double.parseDouble(s) != 0) {
				count += 1;
			}
		}
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

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ib_scanning:
				scan();
				break;
			case R.id.ll_xiadan_changyong:
				openCommon();
				break;
			case R.id.ll_xiadan_all:
				openOrDown();
				break;
			case R.id.ll_xia_shopping_car:
				goReturnGoodsList();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		try {
			initCommonProductCtrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initReturnGooodsCount();
	}

	private boolean isOpenCommom = true;
	private boolean isOpenAll = false;

	private void openCommon() {
		if (isOpenCommom) {
			isOpenCommom = false;
			lv_xiadan.setVisibility(View.GONE);
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
		} else {
			isOpenCommom = true;
			lv_xiadan.setVisibility(View.VISIBLE);
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
		}
	}

	private void openOrDown() {
		if (isOpenAll) {
			isOpenAll = false;
			lv_all.setVisibility(View.GONE);
			iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
		} else {
			isOpenAll = true;
			lv_all.setVisibility(View.VISIBLE);
			iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
		}
	}

	private void scan() {
		// Intent i = new Intent(this, CaptureActivity.class);
		Intent i = PhoneModelManager.getIntent(this,false);
		if (i!=null) {				
			
			startActivityForResult(i, 205);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == R.id.scan_succeeded && requestCode == 205) {
			String scanCode = null;
			scanCode = data.getStringExtra("SCAN_RESULT");
			scanForResult(scanCode);
		} else if (requestCode == 1 && resultCode == 2) {
			Bundle bundle = data.getExtras();
			updataView(bundle);
		}
	}

	private void updataView(Bundle bundle) {

		Set<String> set = bundle.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			EditText et = arr.get(key);
			if (et != null) {
				String num = bundle.getString(String.valueOf(key));
				if (!TextUtils.isEmpty(num)) {
					et.setText(num);
				} else {
					et.setText(null);
					et.setHint("退货数量");
				}
			}
			if (!TextUtils.isEmpty(bundle.getString(key))) {
				if (ctrls.get(key) != null) {
					ctrls.get(key).setReturnCount(bundle.getString(String.valueOf(key)));
				} else {
					CarSalesProductCtrl c = ctrlDB.findProductCtrlById(Integer.parseInt(key));
					c.setReturnCount(bundle.getString(String.valueOf(key)));
					if (c != null) {
						ctrls.put(key, c);
					}
				}
			} else {
				ctrls.remove(Integer.valueOf(key));
			}
		}
		mAdapter.notifyDataSetChanged();
		if (commonAdapter != null) {
			commonAdapter.notifyDataSetChanged();
		}
	}

	private void scanForResult(String code) {
		if (!TextUtils.isEmpty(code)) {
			CarSalesProduct product = util.productForCode(code);
			if (product != null) {
				CarSalesProductCtrl ctrl = ctrlDB.findProductCtrlByProductIdAndUnitId(product.getProductId(),
						product.getUnitId());
				if (!ctrls.containsKey(ctrl.getId())) {
					EditText et = arr.get(ctrl.getId());
					if (et != null) {
						et.requestFocus();
						// InputMethodManager im = (InputMethodManager)
						// et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						// im.toggleSoftInput(0,
						// InputMethodManager.SHOW_FORCED);
					} else {
						ctrl.setReturnCount("1");
						ctrls.put(ctrl.getId(), ctrl);
						ToastOrder.makeText(ReturnGoodsAcitivity.this, "添加成功", ToastOrder.LENGTH_LONG).show();
					}
					return;
				} else {
					ToastOrder.makeText(ReturnGoodsAcitivity.this, "退货列表已存在", ToastOrder.LENGTH_LONG).show();
					return;
				}
				// for(int i = 0; i<ctrls.size(); i++){
				// CarSalesProductCtrl c = ctrls.get(i);
				// if(c.getId() ==
				// ctrl.getId()&&!TextUtils.isEmpty(c.getReturnCount())&&!c.getReturnCount().equalsIgnoreCase("0")&&!c.getReturnCount().equalsIgnoreCase("0.0")){
				// Toast.makeText(ReturnGoodsAcitivity.this, "退货列表已存在",
				// Toast.LENGTH_LONG).show();
				// return;
				// }else{
				// int key = arr.keyAt(i);
				// EditText et = arr.get(key);
				// if(et!=null){
				// et.setFocusable(true);
				// }
				// }
				// }

				// ctrlDB.updateAllChangedCtrl(ctrls);
				// Intent intent = new
				// Intent(ReturnGoodsAcitivity.this,ReturnListActivity.class);
				// startActivity(intent);
			} else {
				ToastOrder.makeText(ReturnGoodsAcitivity.this, "该产品不存在，请确认是否扫描错误", ToastOrder.LENGTH_LONG).show();
			}
		}
	}

	private void goReturnGoodsList() {
		List<CarSalesProductCtrl> cs = new ArrayList<CarSalesProductCtrl>();
		Set<String> set = ctrls.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			cs.add(ctrls.get(it.next()));
		}
		ctrlDB.updateAllChangedCtrl(cs);
		List<CarSalesProductCtrl> productCtrlsChild = util.returnProductCtrlList();
		if (productCtrlsChild != null && productCtrlsChild.size() > 0) {
			Intent intent = new Intent(ReturnGoodsAcitivity.this, ReturnListActivity.class);
			startActivityForResult(intent, 1);
		} else {
			ToastOrder.makeText(ReturnGoodsAcitivity.this, "您没选择产品", ToastOrder.LENGTH_LONG).show();
		}
	}

	Map<String, EditText> arr = new HashMap<String, EditText>();
	Map<String, CarSalesProductCtrl> ctrls = new HashMap<String, CarSalesProductCtrl>();

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
				if (!TextUtils.isEmpty(str)) {
					ctrl.setReturnCount(str);
				} else {
					ctrl.setReturnCount("");
				}
				ctrls.put(ctrl.getId(), ctrl);
			} else {
				if (!TextUtils.isEmpty(str)) {
					ctrl.setReturnCount(str);
					ctrls.put(ctrl.getId(), ctrl);
				} else {
					ctrl.setReturnCount("");
				}
			}
			arr.put(ctrl.getId(), et);
		}
		initReturnGooodsCount();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			int count = 0;
			Set<String> set = ctrls.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String s = ctrls.get(it.next()).getReturnCount();
				if (!TextUtils.isEmpty(s) && Double.parseDouble(s) != 0) {
					count += 1;
				}
			}
			if (count > 0) {
				backDialog("退出后将清除退货列表数据,是否确定退出?").show();
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
				ReturnGoodsAcitivity.this.finish();
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

	@Override
	public void onPutView(Node node, EditText ev) {
		CarSalesProductCtrl ctrl = node.getProductCtrl();
		if (ctrl != null) {
			// if(ctrls.contains(ctrl)){
			// }else{
			// ctrls.add(ctrl);
			// }
			arr.put(ctrl.getId(), ev);
		}
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
					ToastOrder.makeText(ReturnGoodsAcitivity.this, "常用列表数据更新异常", ToastOrder.LENGTH_SHORT).show();
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isOpenCommom", isOpenCommom);
		outState.putBoolean("isOpenAll", isOpenAll);
		super.onSaveInstanceState(outState);
	}

//	@Override
//	protected void restoreConstants(Bundle savedInstanceState) {
//		super.restoreConstants(savedInstanceState);
//		isOpenCommom = savedInstanceState.getBoolean("isOpenCommom", true);
//		isOpenAll = savedInstanceState.getBoolean("isOpenAll", false);
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
