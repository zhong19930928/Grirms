package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotionInfo;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesContactsDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesStockDB;
import com.yunhu.yhshxc.activity.carSales.manager.CarSalesDataManager;
import com.yunhu.yhshxc.activity.carSales.print.CarSalesPrintForSellingGoods;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesPopupWindow;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.kt40.KT40ScanActivity;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.Templet.DataSource;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PhoneModelManager;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSaleShoppingCartActivity extends FragmentActivity implements DataSource{
	private final String TAG = "ShoppingCarActivity";
	private LinearLayout ll_prince;
	private LinearLayout ll_take_photo;
	private LinearLayout ll_submit;
	private TextView tv_discount;
	private TextView tv_collection;
	private TextView tv_yulan;
	private LinearLayout ll_location_bar;
	private LinearLayout ll_yulan_btns;
	private LinearLayout layout_order;
	private LinearLayout ll_home_yulan;
	private LinearLayout layout_discount;
	private LinearLayout layout_collection;
	// 帧布局对象,就是用来存放Fragment的容器
//	public static Fragment[] fragments ;
	private FrameLayout frameLayout;
	private int current = 0;
	// 定义FragmentManager对象
	FragmentManager fManager;
	
	private CarSaleShoppingCarFragment shoppingCarFragment;
	private CarSaleShouKuanFragment shouKuanFragment;
	private CarSaleZengpinFragment zenpinFragment;
	private CarSaleYulanFragment yuLanFragment;
	
	private CarSalesPromotionDB promotionDB;
	private CarSalesProductCtrlDB ctrlDb;
	public CarSalesUtil util;
	public String storeId;
	public CarSalesContact carSalesContact;
	private List<CarSalesShoppingCart> carSalesShoppingCarts;
	private CarSalesShoppingCartDB carSalesShoppingCartDB;
	private List<CarSalesPromotionInfo> infos ;;
	private CarSalesStockDB carSalesStockDB;
	private Map<String,String> map ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_sales_main_shopping_car);
		infos = new ArrayList<CarSalesPromotionInfo>();
		carSalesShoppingCartDB = new CarSalesShoppingCartDB(this);
		promotionDB = new CarSalesPromotionDB(this);
		ctrlDb = new CarSalesProductCtrlDB(this);
		carSalesStockDB = new CarSalesStockDB(this);
		util = new CarSalesUtil(this);
		map = util.getStockCtrl();
		storeId = SharedPreferencesForCarSalesUtil.getInstance(this).getStoreId();
		initAllViews();
	}

	// 是否需要手机打印（1、不要；2、要；默认：1）
	private void controlPrint() {
		int isPrint = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsSales());
		if (isPrint == 1) {
			ll_prince.setVisibility(View.GONE);
		} else {
			ll_prince.setVisibility(View.VISIBLE);
		}
		int isPhoto = Integer.parseInt(SharedPreferencesForCarSalesUtil.getInstance(this).getIsSalesPhoto());
		if (isPhoto == 1) {
			ll_take_photo.setVisibility(View.GONE);
		} else {
			ll_take_photo.setVisibility(View.VISIBLE);
		}
	}
	private void initAllViews() {
		fManager = getSupportFragmentManager();
		frameLayout = (FrameLayout) this.findViewById(R.id.framelayout);
		ll_location_bar = (LinearLayout) this.findViewById(R.id.ll_location_bar);
		ll_yulan_btns = (LinearLayout) this.findViewById(R.id.ll_yulan_btns);
		layout_discount = (LinearLayout) this.findViewById(R.id.layout_discount);
		layout_collection = (LinearLayout) this.findViewById(R.id.layout_collection);
		ll_prince = (LinearLayout) this.findViewById(R.id.ll_prince);
		ll_take_photo = (LinearLayout) this.findViewById(R.id.ll_take_photo);
		ll_submit = (LinearLayout) this.findViewById(R.id.ll_submit);
		tv_discount = (TextView) this.findViewById(R.id.tv_discount);
		tv_collection = (TextView) this.findViewById(R.id.tv_collection);
		tv_yulan = (TextView) this.findViewById(R.id.tv_shopping_car);
		layout_order = (LinearLayout) this.findViewById(R.id.layout_order);
		ll_home_yulan = (LinearLayout) this.findViewById(R.id.ll_home_yulan);
		controlPrint();
		initFragments();
	}
	private void initFragments() {
		setChioceItem(0);
		initData();
	}
	
	private void setChioceItem(int i) {
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChoice();
		hideFragments(transaction);
		switch (i) {
		case 0:
			if (shoppingCarFragment == null) {
				shoppingCarFragment = new CarSaleShoppingCarFragment();
				transaction.add(R.id.framelayout, shoppingCarFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(shoppingCarFragment);
			}
			current = 0;
			transaction.commit();
			break;
		case 1:
			if (shouKuanFragment == null) {
				shouKuanFragment = new CarSaleShouKuanFragment();
				transaction.add(R.id.framelayout, shouKuanFragment);
			} else {
				transaction.show(shouKuanFragment);
			}
			current = 1;
			transaction.commit();
			initData();
//			layout_discount.setBackgroundResource(R.color.order3_locationbar_pressed);
			break;
		case 2:
			if (zenpinFragment == null) {
				zenpinFragment = new CarSaleZengpinFragment();
				transaction.add(R.id.framelayout, zenpinFragment);
			} else {
				zenpinFragment.initData();
				transaction.show(zenpinFragment);
			}
			current = 2;
			transaction.commit();
			initData();
//			layout_collection.setBackgroundResource(R.color.order3_locationbar_pressed);
			break;
		case 3:
			if (yuLanFragment == null) {
				yuLanFragment = new CarSaleYulanFragment();
				transaction.add(R.id.framelayout, yuLanFragment);
			} else {
				transaction.show(yuLanFragment);
				yuLanFragment.initData();
			}
			current = 3;
			ll_location_bar.setVisibility(View.GONE);
			ll_yulan_btns.setVisibility(View.VISIBLE);
			transaction.commit();
			initData();
			break;
		default:
			break;
		}
	}
	private void hideFragments(FragmentTransaction transaction) {
		if (shoppingCarFragment != null) {
			transaction.hide(shoppingCarFragment);
		}
		if (shouKuanFragment != null) {
			transaction.hide(shouKuanFragment);
		}
		if (zenpinFragment != null) {
			transaction.hide(zenpinFragment);
		}
		if (yuLanFragment != null) {
			transaction.hide(yuLanFragment);
		}
	}
	public void LayoutOnclickMethod(View layout) {
		switch (layout.getId()) {
		case R.id.layout_discount://收款留言
			setChioceItem(1);
			break;
		case R.id.layout_collection://本次赠品
			setChioceItem(2);
			break;
		case R.id.layout_shopping_car://订单预览
			setChioceItem(3);
			break;
		case R.id.layout_order:
			goHome();
			break;
		case R.id.ll_home_yulan:
			goHome();
			break;
		case R.id.ll_prince://打印
			print();
			break;
		case R.id.ll_take_photo://拍照
			showPopuWindow();
			break;
		case R.id.ll_submit://提交
			submit();
			break;
		default:
			break;
		}
	}
	/**
	 * 提交的购物信息,成为常用列表
	 */
	public void setCommonList(){
		for(int i = 0; i<carSalesShoppingCarts.size(); i++){
			if(carSalesShoppingCarts.get(i).getPitcOn()==1){
				CarSalesProductCtrl produCtrl  = ctrlDb.findProductCtrlByProductIdAndUnitId(carSalesShoppingCarts.get(i).getProductId(), carSalesShoppingCarts.get(i).getUnitId());
				if(produCtrl!=null){
					ctrlDb.updateProductCtrlCount(produCtrl);
				}
			}
		}
	}
	private void submit() {
		double min = 0;
		String minString = SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getMinNum();
		if(Double.parseDouble(minString)>0){
			min = Double.parseDouble(minString);
		}else{
			min = 1;
		}
		
		CarSales  carSale = getCarSales();

		//		 提交时更新库存数量
		
		List<CarSalesProductData> datas = carSale.getProductList();
		SharedPreferencesForCarSalesUtil.getInstance(this).clearStockCtrl();
		for(int i = 0; i<datas.size(); i++){
			CarSalesProductData productData = datas.get(i);
			CarSalesProductCtrl ctrl = ctrlDb.findProductCtrlByProductIdAndUnitId(productData.getProductId(), productData.getUnitId());
			CarSalesProduct pro = util.product(productData.getProductId(), productData.getUnitId());
			if(ctrl!=null&&pro!=null){
				if(productData.getIscarSalesMain()==1){//主产品
					if(productData.getStockNum()<0){
						String unit = !TextUtils.isEmpty(pro.getUnit())?pro.getUnit():"";
						ToastOrder.makeText(CarSaleShoppingCartActivity.this, productData.getProductName()+"库存仅剩"+PublicUtils.formatDouble(pro.getInventory())+unit+",无法提交订单", ToastOrder.LENGTH_LONG).show();
						return;
					}else if(min!=0&&productData.getActualCount()<min){
						ToastOrder.makeText(CarSaleShoppingCartActivity.this, productData.getProductName()+"至少购买"+PublicUtils.formatDouble(min)+pro.getUnit(), ToastOrder.LENGTH_LONG).show();
						return;
					}else if(productData.getActualCount() <=0){
						ToastOrder.makeText(CarSaleShoppingCartActivity.this, productData.getProductName()+"数量不能为0,无法提交订单", ToastOrder.LENGTH_LONG).show();
						return;
					}
				}else if(productData.getIscarSalesMain()==2){//赠品
					if(productData.getMainProductId()!=0&&productData.getActualAmount()==0){
						if(productData.getStockNum()<0){
							String unit = !TextUtils.isEmpty(pro.getUnit())?pro.getUnit():"";
							ToastOrder.makeText(CarSaleShoppingCartActivity.this, "赠品"+productData.getProductName()+"库存仅剩"+PublicUtils.formatDouble(pro.getInventory())+unit+",无法提交订单", ToastOrder.LENGTH_LONG).show();
							return;
						}
					}
				}
			}
		}
		for(int i = 0; i<datas.size(); i++){
			CarSalesProductData productData = datas.get(i);
			CarSalesProductCtrl ctrl = ctrlDb.findProductCtrlByProductIdAndUnitId(productData.getProductId(), productData.getUnitId());
			CarSalesProduct pro = util.product(productData.getProductId(), productData.getUnitId());
			if(ctrl!=null&&pro!=null){
				if(productData.getIscarSalesMain()==1){//主产品
					map.put(String.valueOf(ctrl.getId()), String.valueOf(productData.getStockNum()));
					carSalesStockDB.updateCarSalesStockNum(productData.getProductId(), productData.getUnitId(), String.valueOf(productData.getStockNum()));
				}else if(productData.getIscarSalesMain()==2){//赠品
					if(productData.getMainProductId()!=0&&productData.getActualAmount()==0){
						map.put(String.valueOf(ctrl.getId()), String.valueOf(productData.getStockNum()));
						carSalesStockDB.updateCarSalesStockNum(productData.getProductId(), productData.getUnitId(), String.valueOf(productData.getStockNum()));
					}
				}
			}
		}
		util.saveStockCtrl(map);
		setCommonList();
		CarSalesDataManager manager = new CarSalesDataManager(this);
		manager.submit(carSale, UrlInfo.doSalesInfo(CarSaleShoppingCartActivity.this));
		SharedPreferencesForCarSalesUtil.getInstance(this).clearSubmitMeassage();
		new CarSalesShoppingCartDB(CarSaleShoppingCartActivity.this).deleteSub();
		sendBroadcast(new Intent(Constants.BROADCAST_CARSALES_CREATE_SUCCESS));
		ToastOrder.makeText(CarSaleShoppingCartActivity.this, R.string.submit_ok, ToastOrder.LENGTH_LONG).show();
		this.finish();
	}
	
	private CarSalesPopupWindow popupWidow;
	private void showPopuWindow() {
		popupWidow = new CarSalesPopupWindow(this);
		popupWidow.show(null,SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getPhotoNameOne(),SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getPhotoNameTwo());
	}
	/**
	 * 打印订单
	 */
	private CarSalesPrintForSellingGoods carSalesPrint;
	private void print() {
		FileHelper help = new FileHelper();
		String json = help.readJsonString(CarSaleShoppingCartActivity.this, "car_sales_print_selling_goods.txt");
		try {
			carSalesPrint = new CarSalesPrintForSellingGoods(this);
			CarSales carSales = getCarSales();
			carSalesPrint.setCarSales(carSales);
			carSalesPrint.setCarSalesPromotionInfoList(infos);
			carSalesPrint.print(CarSaleShoppingCartActivity.this, new JSONArray(json), CarSaleShoppingCartActivity.this);
			int printCount = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount();
			SharedPreferencesForCarSalesUtil.getInstance(this).setCarSalesPrintCount(printCount+1);;
		}catch (Exception e) {
			e.printStackTrace();
			ToastOrder.makeText(this, "打印数据异常", ToastOrder.LENGTH_SHORT).show();
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (popupWidow!=null) {
			popupWidow.onActivityResult(requestCode, resultCode, data);
		}
	}
	private void clearChoice() {
//		layout_discount.setBackgroundResource(R.color.order3_locationbar_bg);
//		layout_collection.setBackgroundResource(R.color.order3_locationbar_bg);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 //监听按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
        	if(current!=0){
        		initFragments();
        		ll_location_bar.setVisibility(View.VISIBLE);
				ll_yulan_btns.setVisibility(View.GONE);
        		return true;
        	}else{
        		finish();
        	}
        }else{//如果不是返回键,就判断是否是特殊机型的扫描按键,如果是就调用扫描头
			if (PhoneModelManager.isStartScan(this, keyCode, event)) {
				if ("KT40".equals(android.os.Build.MODEL)||"KT40Q".equals(android.os.Build.MODEL)) {
		    		Intent i = new Intent(this, KT40ScanActivity.class);
		    		startActivityForResult(i, 202);
				}
			}
		}
        return super.onKeyDown(keyCode, event);
	}
	private void goHome() {
		this.finish();
	}

	private void initData() {
		infos.clear();
		carSalesShoppingCarts = carSalesShoppingCartDB.findAllList();
		shoppingCarFragment.initdb();
		if(SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getIsPromotion()==1){
			
		}else{
			danPinInfo();
			zongHeInfo(carSalesShoppingCarts);
		}
	}
	List<CarSalesShoppingCart> cartsCheckedIsDouble = new ArrayList<CarSalesShoppingCart>();
	List<CarSalesShoppingCart> cartsCheckedNoDouble = new ArrayList<CarSalesShoppingCart>();
	/**
	 * 总和赠品折扣信息
	 */
	private void zongHeInfo(List<CarSalesShoppingCart> carSalesShoppingCarts) {
		
		util.allChecked(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts);
		double allMoney = util.allMoney(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts);
		Map<String,String> mapNumDis = util.zongHeFullNumDis(cartsCheckedIsDouble,allMoney);//买满数量打折
		Map<String,String> mapNumJM = util.zongHeFullNumJM(cartsCheckedIsDouble, allMoney);//买满数量减免
		Map<String,String> mapPriceDis = util.zongHeFullNumDis6(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts, allMoney);//买满金额打折
		Map<String,String> mapPriceJM = util.zongHeFullNumJM6(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts, allMoney);//买满金额减免
		Map<String,Double> mapNum  = util.zongHeFullNum5(cartsCheckedIsDouble) ;//送赠品
		Map<String,Double> mapNum6  = util.zongHeFullNum6(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts, allMoney) ;//送赠品
		double allNumAmount = Math.abs(Double.parseDouble(mapNumDis.get("pre")));
		double allNumAmount1 = Math.abs(Double.parseDouble(mapNumJM.get("pre")));
		double allNumGift = mapNum.get("num");
		double allPriceAmount = Math.abs(Double.parseDouble(mapPriceDis.get("pre")));
		double allPriceAmount1 = Math.abs(Double.parseDouble(mapPriceJM.get("pre")));
		double allPriceGift = mapNum6.get("num");
		CarSalesPromotionInfo info = new CarSalesPromotionInfo();
//		JLog.d("aaa", allNumAmount+"   "+allNumAmount1+"   "+allPriceAmount+"   "+allPriceAmount1);
		double[] a = {allNumAmount,allNumAmount1,allPriceAmount,allPriceAmount1};
		double allM = 0;
		double s = 0;
		for(int i = 0; i<4;i++){
			allM = Math.max(allM, a[i]);
		}
		if(allM!=0){
			if(allM==allNumAmount){
				info.setTitle(mapNumDis.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allNumAmount)+"元");
				info.setPromotionId((int) Double.parseDouble(mapNumDis.get("proid")));
			}else if(allM==allNumAmount1){
				info.setTitle(mapNumJM.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allNumAmount1)+"元");
				info.setPromotionId((int) Double.parseDouble(mapNumJM.get("proid")));
			}else if(allM==allPriceAmount){
				info.setTitle(mapPriceDis.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allPriceAmount)+"元");
				info.setPromotionId((int) Double.parseDouble(mapPriceDis.get("proid")));
			}else if(allM==allPriceAmount1){
				info.setTitle(mapPriceJM.get("name"));
				info.setDetails("减免"+PublicUtils.formatDouble(allPriceAmount1)+"元");
				info.setPromotionId((int) Double.parseDouble(mapPriceJM.get("proid")));
			}
			info.setActualAmount(allM);
			info.setOrderPrice(allM);
		}else if(allNumGift>=allPriceGift&&allNumGift!=0){
			int level1 = SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarSalesStoreLevel();
			List<CarSalesPromotion> pos = promotionDB.findPromotionByPreType(SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarSalesOrgId(), 5,1,level1);
			Double b = mapNum.get("index");
			int v = b.intValue();
			info.setTitle(pos.get(v).getName());
			info.setPromotionId(mapNum.get("proid").intValue());
			CarSalesProduct product = util.product(Integer.parseInt(!TextUtils.isEmpty(pos.get(v).getsId())?pos.get(v).getsId():"0"),Integer.parseInt(!TextUtils.isEmpty(pos.get(v).getsUid())?pos.get(v).getsUid():"0"));
			if(product!=null){
				info.setDetails("赠送"+product.getName()+" "+PublicUtils.formatDouble(allNumGift)+" "+product.getUnit());
				info.setGiftId(product.getProductId());
				info.setGiftUnit(product.getUnitId());
			}
			info.setGiftNum(allNumGift);
			info.setActualAmount(0);
			info.setOrderPrice(0);
		}else if(allNumGift<=allPriceGift&&allPriceGift!=0){
			int level1 = SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarSalesStoreLevel();
			List<CarSalesPromotion> pos = promotionDB.findPromotionByPreType(SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarSalesOrgId(), 5,1,level1);
			Double b = mapNum6.get("index");
			int v = b.intValue();
			info.setTitle(pos.get(v).getName());
			info.setPromotionId(mapNum6.get("proid").intValue());
			CarSalesProduct product = util.product(Integer.parseInt(pos.get(v).getsId()),Integer.parseInt(pos.get(v).getsUid()));
			if(product!=null){
				info.setDetails("赠送"+product.getName()+" "+PublicUtils.formatDouble(allNumGift)+" "+product.getUnit());
				info.setGiftId(product.getProductId());
				info.setGiftUnit(product.getUnitId());
			}
			info.setGiftNum(allNumGift);
			info.setActualAmount(0);
			info.setOrderPrice(0);
		}
//		info.setOrderPrice(shoppingCarFragment.allMoney());
		if(!TextUtils.isEmpty(info.getTitle())){
			infos.add(info);
		}
	}
	/**
	 * 单品赠品折扣信息
	 */
	private void danPinInfo() {
		if(carSalesShoppingCarts.size()>0){
			for(int i = 0; i<carSalesShoppingCarts.size();i++){
				CarSalesShoppingCart cart = carSalesShoppingCarts.get(i);
				String ids = cart.getPromotionIds();
				if(cart.getPitcOn()==1 && !TextUtils.isEmpty(ids)&&!ids.equals("0")){
					CarSalesPromotion pro = promotionDB.findPromotionByPromotionId(Integer.parseInt(ids));
					if(pro!=null){
						if(pro.getPreType() == 1){//买满数量
							if(cart.getNumber()>=pro.getmCnt()){
								CarSalesPromotionInfo info = new CarSalesPromotionInfo();
								info.setTitle(pro.getName());
								if(pro.getDisType()==3){//打折
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPrePrice())+"元");
									info.setActualAmount(cart.getPrePrice());//减免值
									info.setOrderPrice(cart.getDiscountPrice());//实际价格
								}else if(pro.getDisType()==1){
									CarSalesProduct p  = util.product(cart.getGiftId(), cart.getGiftUnitId());
									info.setDetails("赠送"+p.getName()+" "+PublicUtils.formatDouble(cart.getDisNumber())+" "+p.getUnit());
									info.setGiftId(cart.getGiftId());
									info.setGiftUnit(cart.getGiftUnitId());
									info.setGiftNum(cart.getDisNumber());
									
								}else if(pro.getDisType()==2){
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPreAmount())+"元");
									info.setActualAmount(cart.getPreAmount());//减免值
									info.setOrderPrice(cart.getDisAmount());//实际价格
								}
								info.setPromotionId(pro.getPromotionId());
								info.setProductId(cart.getProductId());
								info.setUnitId(cart.getUnitId());
								infos.add(info);
							}
							
						}else if(pro.getPreType() == 2){//买满金额
							if(cart.getSubtotal()>=pro.getAmount()){
								CarSalesPromotionInfo info = new CarSalesPromotionInfo();
								info.setTitle(pro.getName());
								if(pro.getDisType()==3){//打折
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPrePrice())+"元");
									info.setActualAmount(cart.getPrePrice());//减免值
									info.setOrderPrice(cart.getDiscountPrice());//实际价格
								}else if(pro.getDisType()==1){//赠送产品
									CarSalesProduct p  = util.product(cart.getGiftId(), cart.getGiftUnitId());
									info.setDetails("赠送"+p.getName()+" "+PublicUtils.formatDouble(cart.getDisNumber())+" "+p.getUnit());
									info.setGiftId(cart.getGiftId());
									info.setGiftUnit(cart.getGiftUnitId());
									info.setGiftNum(cart.getDisNumber());
									
								}else if(pro.getDisType()==2){//减免金额
									info.setDetails("减免"+PublicUtils.formatDouble(cart.getPreAmount())+"元");
									info.setActualAmount(cart.getPreAmount());//减免值
									info.setOrderPrice(cart.getDisAmount());//实际价格
								}
								info.setPromotionId(pro.getPromotionId());
								info.setProductId(cart.getProductId());
								info.setUnitId(cart.getUnitId());
								infos.add(info);
							}
						}
					}
				}
			}
		}
	}
	
	private CarSales carSales;
	private int isPromotion;
	public CarSales getCarSales(){
		carSales = new CarSales();
		carSales.setStoreId(storeId);
		carSales.setCarId(String.valueOf(SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarId()));
		carSales.setStoreName(SharedPreferencesForCarSalesUtil.getInstance(CarSaleShoppingCartActivity.this).getCarSalesStoreName());
		carSales.setCarSalesNo(util.carSalesNumber(true));
		carSales.setCarSalesTime(DateUtil.getCurDateTime());
		carSales.setImage1(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameOne());
		carSales.setImage2(SharedPreferencesForCarSalesUtil.getInstance(this).getPhotoNameTwo());
		carSales.setPrintCount(SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesPrintCount());
//		carSales.setSendPrintCount(SharedPreferencesForOrder3Util.getInstance(this).getOrderSendPrintCount());
		
		if(carSalesContact==null){
			int contachId = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesContactId();
			if (contachId!=0) {
				carSalesContact = new CarSalesContactsDB(this).findCarSalesContactByContactsIdAndStoreId(contachId, storeId);
			}
		}
		if (carSalesContact!=null) {
			carSales.setContact(carSalesContact);
			carSales.setContactId(carSalesContact.getContactsId());
		}
		List<CarSalesProductData> order3Pros = new ArrayList<CarSalesProductData>();
		if(carSalesShoppingCarts.size()>0){
			for(int i = 0; i<carSalesShoppingCarts.size(); i++){
				CarSalesShoppingCart c = carSalesShoppingCarts.get(i);
				if(c.getPitcOn()==1){
					CarSalesProduct p = util.product(c.getProductId(), c.getUnitId());
					if(p!=null){
						CarSalesProductData pda = new CarSalesProductData();
						pda.setProductId(c.getProductId());
						pda.setCarSalesCount(c.getNumber());
						pda.setActualCount(c.getNumber());
						pda.setProductUnit(p.getUnit());
						pda.setUnitId(c.getUnitId());
						pda.setCarSalesPrice(p.getPrice());
						pda.setCarSalesAmount(c.getSubtotal());
						pda.setProductName(p.getName());
						if(c.getDisAmount()!=0&&c.getDisAmount()!=c.getSubtotal()){
							pda.setActualAmount(c.getDisAmount());
						}else if(c.getDiscountPrice()!=0&&c.getDiscountPrice()!=c.getSubtotal()){
							pda.setActualAmount(c.getDiscountPrice());
						}else{
							pda.setActualAmount(c.getSubtotal());
						}
						pda.setIsCarSalesMain(1);
						pda.setStockNum(p.getInventory()-c.getNumber());
						pda.setPromotionId(Integer.parseInt(c.getPromotionIds()));
						if(!TextUtils.isEmpty(c.getPromotionIds())&&(!c.getPromotionIds().equals("0"))){
							isPromotion = 1;
						}
						order3Pros.add(pda);
					}
					
				}
			}
		}
		
		if(infos.size()>0){
			for(int i = 0; i<infos.size();i++){
				CarSalesPromotionInfo fo = infos.get(i);
				CarSalesProduct gift = util.product(fo.getGiftId(), fo.getGiftUnit());
				CarSalesProductData pda = new CarSalesProductData();
				if(gift!=null){//赠品信息
					pda.setProductId(fo.getGiftId());
					pda.setCarSalesCount(fo.getGiftNum());
					pda.setActualCount(fo.getGiftNum());
					pda.setProductUnit(gift.getUnit());
					pda.setUnitId(fo.getGiftUnit());
					pda.setMainProductId(fo.getProductId());
					pda.setPromotionId(fo.getPromotionId());
					pda.setProductName(gift.getName());
					pda.setIsCarSalesMain(2);
					pda.setStockNum(gift.getInventory()-fo.getGiftNum());
					order3Pros.add(pda);
				}else if(fo.getProductId()==0){//总和打折减免信息
//					double a = fo.getOrderPrice()-fo.getActualAmount();
					pda.setActualAmount(fo.getActualAmount());
					pda.setCarSalesAmount(fo.getActualAmount());
					pda.setPromotionId(fo.getPromotionId());
					pda.setIsCarSalesMain(2);
					order3Pros.add(pda);
				}else{//单品折扣减免信息
					CarSalesProduct duct = util.product(fo.getProductId(), fo.getUnitId());
					if(duct!=null){
						pda.setProductId(duct.getProductId());
//						pda.setOrderCount(c.getNumber());
//						pda.setActualCount(c.getNumber());
						pda.setProductUnit(duct.getUnit());
						pda.setUnitId(duct.getUnitId());
						pda.setCarSalesPrice(duct.getPrice());;//商品单价
						pda.setCarSalesAmount(fo.getOrderPrice());;//实际价格
						pda.setActualAmount(fo.getActualAmount());//减免值
						pda.setProductName(duct.getName());
						pda.setIsCarSalesMain(2);
						pda.setPromotionId(fo.getPromotionId());
						
						order3Pros.add(pda);
					}
				}
				if(fo.getPromotionId()!=0){
					isPromotion =1;
				}
			}
		}
		carSales.setIsPromotion(isPromotion);
		if(order3Pros.size()>0){
			carSales.setProductList(order3Pros);
		}
		String shouKuan = SharedPreferencesForCarSalesUtil.getInstance(this).getShouKuan();
		carSales.setPayAmount(Double.parseDouble(shouKuan));//预收款
		String jianMian = SharedPreferencesForCarSalesUtil.getInstance(this).getJianMian();
		carSales.setCarSalesDiscount(Double.parseDouble(jianMian));;//特别折扣
		String msg = SharedPreferencesForCarSalesUtil.getInstance(this).getLeaveMessage();
		carSales.setNote(msg);
		carSalesShoppingCarts = carSalesShoppingCartDB.findAllList();
		util.allChecked(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts);
		double a = util.allMoney(cartsCheckedIsDouble, cartsCheckedNoDouble, carSalesShoppingCarts)+util.allMoneyNoDouble(cartsCheckedNoDouble);
		carSales.setCarSalesAmount(a);
//		JLog.d("aaa","---------- "+a );
		carSales.setActualAmount(getActulAmount()-carSales.getCarSalesDiscount());
//		JLog.d("aaa","---------- "+(getActulAmount()-carSales.getCarSalesDiscount()));
		carSales.setUnPayAmount((carSales.getActualAmount()-carSales.getPayAmount()));
		carSales.setStatus(SharedPreferencesForCarSalesUtil.getInstance(this).getStatus());
		carSales.setStock(true);
		return carSales;
	}
	public double getActulAmount(){//现价 折后
		return shoppingCarFragment == null ? 0:shoppingCarFragment.getActulAmo();
	}
	public List<CarSalesPromotionInfo> getProInfo(){
		initData();
		return infos;
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
		carSalesPrint.printPromotion(columns);
	}
	@Override
	public void printZhekou(List<Element> columns) {
		
	}
	@Override
	public void printDingdanbianhao(List<Element> columns) {
		
	}
	public Map<String,String> getMap(){
		map = util.getStockCtrl();
		return map;
	}
	private String mapToString(Map<String, String> map) {
		String mapStr = null;
		if (map != null && !map.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				sb.append(";").append(entry.getKey() != null ? entry.getKey() : "").append(",").append(entry.getValue() != null ? entry.getValue() : "");
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
				}else if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
					map.put(str[0], str[1]);
				}
			}
		}
		return map;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("current", current);
		outState.putInt("isPromotion", isPromotion);
		outState.putString("storeId", storeId);
		outState.putString("map", mapToString(map));
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		current = savedInstanceState.getInt("current");
		isPromotion = savedInstanceState.getInt("isPromotion");
		storeId = savedInstanceState.getString("storeId");
		map = stringToMap(savedInstanceState.getString("map"));
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	

}
