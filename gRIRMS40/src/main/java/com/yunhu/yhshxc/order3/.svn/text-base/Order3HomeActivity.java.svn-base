package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

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

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3ProductItem.OnShoppingCarListner;
import com.yunhu.yhshxc.order3.zhy.tree.bean.Node;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeListViewAdapter;
import com.yunhu.yhshxc.order3.zhy.tree.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.yunhu.yhshxc.order3.zhy.tree_view.SimpleTreeAdapter;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.widget.DropDown;
import com.yunhu.yhshxc.widget.DropDown.OnFuzzyQueryListener;
import com.yunhu.yhshxc.widget.DropDown.OnResultListener;
import com.yunhu.yhshxc.widget.ToastOrder;


public class Order3HomeActivity extends AbsBaseActivity implements OnClickListener, OnFuzzyQueryListener, OnShoppingCarListner {
	private LinearLayout ll_xia_shopping_car;
	private LinearLayout ll_xiandan_manage;
	private LinearLayout ll_xiadan_changyong;
	private LinearLayout ll_xiadan_all;
	private LinearLayout ll_order3_expandd;
	private ListView lv_xiadan;
	private ListView lv_all;
	private ImageView iv_changyong;
	private ImageView iv_all;
	private TreeListViewAdapter mAdapter;
	private TreeListViewAdapter commonAdapter;
	private List<OrgStore> allStore;// 所有店面
	private DropDown spinner;
	private String storeId;
	private String orgId;
	private String scanCode;
	private TextView tv_shopping_car_ll;
	private TextView tv_tuozhan;

	private ImageButton ib_scanning;
	private Order3Util order3Util;
	private boolean isOpenAll = false;
	private boolean isOpenCommom = true;
	private final int INDEX = 1;
	private Order3ShoppingCartDB order3ShoppingCartDB;
	private TextView tv_order3_name;
	private int menuType;
	private int targetId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_xiadan);
		menuType = getIntent().getExtras().getInt("menuType", 0);
		targetId = getIntent().getExtras().getInt("targetId", 0);
		order3ShoppingCartDB = new Order3ShoppingCartDB(this);
		clearData();
		registRefrashReceiver();
		registSubmitSuccessReceiver();
		registRefrashStoreReceiver();
		storeId = SharedPreferencesForOrder3Util.getInstance(this).getStoreId();
		order3Util = new Order3Util(this);
		initWedigt();
		initIntentData();
		controlExpand();
	}
	
	
	private void initIntentData(){
		Intent intent = getIntent();
		if (intent!=null) {
			storeId = intent.getStringExtra("storeId");
			if (TextUtils.isEmpty(storeId)) {
				initStoreData(null);
			}else{
				OrgStore store = new OrgStoreDB(this).findOrgStoreByStoreId(storeId);
				if (store!=null) {
					Dictionary dic = new Dictionary();
					dic.setDid(String.valueOf(store.getStoreId()));
					dic.setCtrlCol(store.getStoreName());
					dic.setNote(store.getOrgCode());
					dic.setLevel(store.getLevel());
					initKH(dic);
					tv_order3_name.setVisibility(View.VISIBLE);
					tv_order3_name.setText(store.getStoreName());
					spinner.setVisibility(View.GONE);
				}else{
					initStoreData(null);
				}
			}
		}else{
			initStoreData(null);
		}
	}
	private void initWedigt() {
		tv_tuozhan = (TextView) this.findViewById(R.id.tv_tuozhan);
		String name = SharedPreferencesForOrder3Util.getInstance(Order3HomeActivity.this).getLinkName();
		tv_tuozhan.setText(name);
		ll_xia_shopping_car = (LinearLayout) this.findViewById(R.id.ll_xia_shopping_car);
		ll_xiadan_changyong = (LinearLayout) this.findViewById(R.id.ll_xiadan_changyong);
		ll_xiadan_all = (LinearLayout) this.findViewById(R.id.ll_xiadan_all);
		ll_order3_expandd = (LinearLayout) this.findViewById(R.id.ll_order3_expandd);
		ll_xiadan_all.setOnClickListener(this);
		ll_xiadan_changyong.setOnClickListener(this);
		lv_xiadan = (ListView) this.findViewById(R.id.lv_xiadan);
		lv_all = (ListView) this.findViewById(R.id.lv_all);
		iv_changyong = (ImageView) this.findViewById(R.id.iv_changyong);
		iv_all = (ImageView) this.findViewById(R.id.iv_all);
		ll_xiandan_manage = (LinearLayout) this.findViewById(R.id.ll_xiandan_manage);
		tv_shopping_car_ll = (TextView) this.findViewById(R.id.tv_shopping_car_ll);
		ll_xiandan_manage.setOnClickListener(this);
		spinner = (DropDown) this.findViewById(R.id.spinner1);
		spinner.setOnFuzzyQueryListener((OnFuzzyQueryListener) this);
		spinner.setOnResultListener(resultListener);
		ib_scanning = (ImageButton) this.findViewById(R.id.ib_scanning);
		ib_scanning.setOnClickListener(this);
		ll_xia_shopping_car.setOnClickListener(this);
		tv_order3_name = (TextView)findViewById(R.id.tv_order3_name);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initShoppingCartCount();
	}
	
	/**
	 * 设置购物车数量
	 */
	private void initShoppingCartCount(){
		int count = order3ShoppingCartDB.count();
		if (count > 0 ) {
			tv_shopping_car_ll.setVisibility(View.VISIBLE);
			if(count>99){
				tv_shopping_car_ll.setText("99+");
			}else{
				tv_shopping_car_ll.setText(""+count);
			}
			
		}else{
			tv_shopping_car_ll.setVisibility(View.INVISIBLE);
		}
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
	
	/**
	 * 刷新数据广播
	 */
	private BroadcastReceiver initDataReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.BROADCAST_ORDER3_REFRASH_PRODUCT .equals(action)) {
				initData();
			}
		}
		
	};
	
	/**
	 * 初始化全部列表
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressLint("NewApi")
	private void initAllProductCtrl() throws IllegalArgumentException, IllegalAccessException{
		List<Order3ProductCtrl> productCtrlList = order3Util.allProductCtrlList();
		if (productCtrlList.size() > 0) {
			mAdapter = new SimpleTreeAdapter<Order3ProductCtrl>(lv_all,Order3HomeActivity.this, productCtrlList, 0,Order3HomeActivity.this);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {

					if (node.isLeaf()) {
						Order3ProductCtrl ctrl = node.getProduct();
						if (ctrl != null && ctrl.isProductLevel()) {
							Intent i = new Intent(Order3HomeActivity.this,ProductDetailActivity.class);
							Bundle bundle = new Bundle();	
							bundle.putInt("index", INDEX);
							bundle.putInt("productId", ctrl.getProductId());
							bundle.putInt("unitId", ctrl.getUnitId());
							i.putExtras(bundle);
							startActivity(i);
							
						} else {
							ToastOrder.makeText(Order3HomeActivity.this, "没有可购买的产品", ToastOrder.LENGTH_SHORT).show();
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
	 * 初始化常用列表
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressLint("NewApi")
	private void initCommonProductCtrl() throws IllegalArgumentException, IllegalAccessException{
		// 换实体类对象 泛型匹配
		List<Order3ProductCtrl> productCtrlList = order3Util.commonProductCtrlList();
		if (productCtrlList.size() > 0) {
			iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
			commonAdapter = new SimpleTreeAdapter<Order3ProductCtrl>(lv_xiadan, Order3HomeActivity.this, productCtrlList, 0,Order3HomeActivity.this);
			commonAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(Node node, int position) {

					if (node.isLeaf()) {
						Order3ProductCtrl ctrl = node.getProduct();
						if (ctrl != null && ctrl.isProductLevel()) {
							Intent i = new Intent(Order3HomeActivity.this,ProductDetailActivity.class);
							Bundle bundle = new Bundle();	
							bundle.putInt("index", INDEX);
							bundle.putInt("productId", ctrl.getProductId());
							bundle.putInt("unitId", ctrl.getUnitId());
							i.putExtras(bundle);
							startActivity(i);
							
						} else {
							ToastOrder.makeText(Order3HomeActivity.this, "没有可购买的产品", ToastOrder.LENGTH_SHORT).show();
						}
					}
				}

			});
			lv_xiadan.setAdapter(commonAdapter);
			lv_xiadan.setOverScrollMode(View.OVER_SCROLL_NEVER); 
		}
	}

	/**
	 * 初始化所有客户数据
	 */
	private List<Dictionary> srcStoreList;
	private void initStoreData(String fuzzy) {
		allStore = new OrgStoreDB(this).findAllOrgList(fuzzy);
		srcStoreList = new ArrayList<Dictionary>();
		if (allStore != null && allStore.size() > 0) {
			OrgStore store = null;
			Dictionary dic = null;

			for (int i = 0; i < allStore.size(); i++) {
				store = allStore.get(i);
				dic = new Dictionary();
				dic.setDid(String.valueOf(store.getStoreId()));
				dic.setCtrlCol(store.getStoreName());
				dic.setNote(store.getOrgCode());
				dic.setLevel(store.getLevel());
				srcStoreList.add(dic);
			}
		}
		spinner.setSrcDictList(srcStoreList);
		initStoreSelect(srcStoreList);
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
	private void initStoreSelect(List<Dictionary> srcList) {
		if (!TextUtils.isEmpty(storeId) && srcList.size() > 0) {
			for (int i = 0; i < srcList.size(); i++) {
				Dictionary dic = srcList.get(i);
				if (storeId.equals(dic.getDid())) {
					spinner.setSelected(dic);
					break;
				}
			}
		}
	}

	private OnResultListener resultListener = new OnResultListener() {

		@Override
		public void onResult(List<Dictionary> result) {
			if (result != null && !result.isEmpty()) {
				Dictionary dic = result.get(0);
				if (TextUtils.isEmpty(storeId)) {//第一次选择客户
					initKH(dic);
				}else if(!storeId.equals(dic.getDid())){//选择的客户改变了
					int count = order3ShoppingCartDB.count();
					if (count>0) {//购物车有值的时候提示
						changeStoreDialog("更改客户以后将清除购物车数据,是否更改?", dic).show();;
					}else{//没值的时候直接更改
						initKH(dic);
					}
				}
				
			}

		}
	};
	
	private Dialog changeStoreDialog(String bContent,final Dictionary dic) {
		final Dialog backDialog = new Dialog(this, R.style.transparentDialog);
		View view = View.inflate(this, R.layout.back_dialog, null);
		TextView backContent = (TextView) view.findViewById(R.id.tv_back_content);
		Button confirm = (Button) view.findViewById(R.id.btn_back_confirm);
		confirm.setText("更改");
		Button cancel = (Button) view.findViewById(R.id.btn_back_cancel);
		backContent.setText(bContent);
		confirm.setOnClickListener(new OnClickListener() {

			/**
			 * 更改
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				initKH(dic);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			/**
			 * 取消
			 */
			@Override
			public void onClick(View v) {
				backDialog.dismiss();
				initStoreSelect(srcStoreList);
			}
		});
		backDialog.setContentView(view);
		backDialog.setCancelable(false);// 将对话框设置为模态
		return backDialog;
	}

	
	/**
	 * 初始化客户信息
	 * @param dic
	 */
	private void initKH(Dictionary dic){
		storeId = dic.getDid();
		orgId = dic.getNote();
		SharedPreferencesForOrder3Util.getInstance(this).clearLeaveMeassage();
		SharedPreferencesForOrder3Util.getInstance(this).setOrder3OrgId(orgId);
		SharedPreferencesForOrder3Util.getInstance(this).setOrder3StoreName(dic.getCtrlCol());
		SharedPreferencesForOrder3Util.getInstance(this).setStoreId(storeId);
		SharedPreferencesForOrder3Util.getInstance(this).setOrder3StoreLevel(dic.getLevel());
		order3ShoppingCartDB.delete();
//		tv_shopping_car_ll.setText("购物车");
		initShoppingCartCount();
		initData();
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
		case R.id.ll_xiandan_manage:// 进入管理页面
			management();
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
	private void controlExpand(){
		final int targetId = SharedPreferencesForOrder3Util.getInstance(Order3HomeActivity.this).getLinkMod();
		if (targetId == 0) {
			ll_order3_expandd.setVisibility(View.GONE);
		}else{
			ll_order3_expandd.setVisibility(View.VISIBLE);
		}
		ll_order3_expandd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(storeId)){
					expand(targetId);
				}else{
					ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	private void expand(int targetId){
		Bundle bundle = new Bundle();
		bundle.putInt("targetId",targetId);//数据ID
		bundle.putInt("menuType", Menu.TYPE_ORDER3);//菜单类型
		Intent intent = new Intent(Order3HomeActivity.this, Order3FuncActivity.class);//跳转到上报页面
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}
	private void openCommon() {
		if(!TextUtils.isEmpty(storeId)){
			if (isOpenCommom) {
				isOpenCommom = false;
				lv_xiadan.setVisibility(View.GONE);
				iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
			} else {
				isOpenCommom = true;
				lv_xiadan.setVisibility(View.VISIBLE);
				iv_changyong.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
			}
		}else{
			ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
		}
		
	}

	private void openOrDown() {
		if(!TextUtils.isEmpty(storeId)){
			if (isOpenAll) {
				isOpenAll = false;
				lv_all.setVisibility(View.GONE);
				iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ec));
			} else {
				isOpenAll = true;
				lv_all.setVisibility(View.VISIBLE);
				iv_all.setImageDrawable(getResources().getDrawable(R.drawable.tree_ex));
			}
		}else{
			ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
		}
		
	}

	private void management() {
		if (!TextUtils.isEmpty(storeId)) {
			Intent intent = new Intent(Order3HomeActivity.this,Order3ManagerActivity.class);
			startActivity(intent);
		}else{
			ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
		}
	}

	private void goToShoppingCar() {
		if (!TextUtils.isEmpty(storeId)) {
			int count = order3ShoppingCartDB.count();
			if (count > 0 ) {
				Intent i = new Intent(Order3HomeActivity.this,ShoppingCarActivity.class);
				i.putExtra("menuType",menuType);
				i.putExtra("targetId", targetId);
				startActivity(i);
			}else{
				ToastOrder.makeText(this, "您没选购产品", ToastOrder.LENGTH_SHORT).show();
			}
			
		}else{
			ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
		}
	}

	/**
	 * 扫码
	 */
	private void scan() {
		if (!TextUtils.isEmpty(storeId)) {
//			Intent i = new Intent(this, CaptureActivity.class);
			Intent i = PhoneModelManager.getIntent(this, true);
			if (i!=null) {			
				startActivityForResult(i, 200);
			}
		}else{
			ToastOrder.makeText(Order3HomeActivity.this, "请先选择客户",ToastOrder.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照 扫描返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		// 拍照 扫描返回
		if (resultCode == R.id.scan_succeeded && requestCode == 200) {
			scanCode = data.getStringExtra("SCAN_RESULT");
			scanForResult(scanCode);
		}
	}

	/**
	 * 扫码成功返回
	 */
	private void scanForResult(String code) {
		if (!TextUtils.isEmpty(code)) {
			Order3Product product = order3Util.productForCode(code);
			if (product != null) {
				Intent i = new Intent(Order3HomeActivity.this,ProductDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("index", INDEX);
				bundle.putInt("productId", product.getProductId());
				bundle.putInt("unitId", product.getUnitId());
				i.putExtras(bundle);
				startActivity(i);
			} else {
				ToastOrder.makeText(Order3HomeActivity.this, "该产品不存在",ToastOrder.LENGTH_LONG).show();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			int count = order3ShoppingCartDB.count();
			if (count > 0 ) {
				backDialog("退出后将清除购物车以及未提交数据,是否确定退出?").show();
				return true;
			}else{
				finish();
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
				Order3HomeActivity.this.finish();
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
	
	/**
	 * 清除购物车和缓存数据
	 */
	private void clearData(){
		order3ShoppingCartDB.delete();
		clearNotSubmitData();
		SharedPreferencesForOrder3Util.getInstance(Order3HomeActivity.this).clearLeaveMeassage();
	}

	/**
	 * 清除拓展模块数据
	 */
	private void clearNotSubmitData(){
		String t = SharedPreferencesForOrder3Util.getInstance(this).getOrderTimestamp();
		if (!TextUtils.isEmpty(t)) {
			SubmitDB db = new SubmitDB(this);
			Submit submit = db.findSubmitByTimestamp(t);
			if (submit!=null && submit.getState() == Submit.STATE_NO_SUBMIT) {
				db.deleteSubmitById(submit.getId());
				new SubmitItemDB(this).deleteSubmitItemBySubmitId(submit.getId());
			}
		}
	}
	
	@Override
	public void onFindAllList() {
		initShoppingCartCount();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		unregisterReceiver(initDataReceiver);
		unregisterReceiver(submitSuccessReceiver);
		unregisterReceiver(refrashStoreReceiver);
	}
	
	private void registSubmitSuccessReceiver(){
		IntentFilter filter = new IntentFilter(Constants.BROADCAST_ORDER3_CREATE_SUCCESS);
		registerReceiver(submitSuccessReceiver, filter);
	}
	
	private void registRefrashReceiver(){
		IntentFilter fliter = new IntentFilter(Constants.BROADCAST_ORDER3_REFRASH_PRODUCT);
		registerReceiver(initDataReceiver, fliter);
	}
	

	private void registRefrashStoreReceiver(){
		IntentFilter fliter = new IntentFilter(Constants.BROADCAST_ORDER3_REFRASH_STORE);
		registerReceiver(refrashStoreReceiver, fliter);
	}
	
	/**
	 * 刷新店面数据广播
	 */
	private BroadcastReceiver refrashStoreReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_ORDER3_REFRASH_STORE.equals(intent.getAction())) {
				initStoreData(null);
			}
		}
		
	};
	
	/**
	 * 下订单提交数据成功
	 */
	private BroadcastReceiver submitSuccessReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent!=null && Constants.BROADCAST_ORDER3_CREATE_SUCCESS.equals(intent.getAction())) {
				try {
					initCommonProductCtrl();
				} catch (Exception e) {
					ToastOrder.makeText(Order3HomeActivity.this, "常用列表数据更新异常", ToastOrder.LENGTH_SHORT).show();
				}
			}
		}
		
	};
}
