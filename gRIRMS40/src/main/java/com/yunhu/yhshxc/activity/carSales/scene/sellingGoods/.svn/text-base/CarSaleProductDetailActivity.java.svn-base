package com.yunhu.yhshxc.activity.carSales.scene.sellingGoods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.scene.view.CarSalePromotionSyncInfo;
import com.yunhu.yhshxc.activity.carSales.scene.view.CarSalesProductDiscountInfo;
import com.yunhu.yhshxc.activity.carSales.scene.view.CarSalesProductInfoView;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class CarSaleProductDetailActivity extends AbsBaseActivity {
	private TextView tv_product_detail_name;
	private TextView tv_product_detail_dicount_info;
	private TextView tv_product_detail;
	private EditText tv_product_detail_price;
	private TextView tv_sub;
	private TextView tv_add;
	private EditText et_data;
	private TextView tv_price;
	private Button btn_addtoshoppingcar;
	private TextView tv_product_detail_price_info;
	private LinearLayout ll_home;
	private LinearLayout ll_sub;
	private LinearLayout ll_add;
	private LinearLayout ll_product_detail_info;

	private TextView tv_info;
//	private LinearLayout ll_shopping_car_container;
	private LinearLayout ll_product_detail_dicount_info;
	private ScrollView sv_product_detail_info;
	private boolean isDis = false;
	private double min ;
	private double max ;
	private int mIndex = 1;
	private int productId;
	private int unitId;
	
	private CarSalesProduct carSalesProduct;
	private CarSalesProductCtrl carSalesProductCtrl;
	private CarSalesShoppingCart cart;
	private CarSalesShoppingCartDB cartDb;
	private CarSalesUtil util;
	private CarSalesProductCtrlDB ctrlDB;
	private CarSalesPromotionDB promotionDB;
	List<CarSalesPromotion> promotions;
	private Map<String,String> map;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_product_detai);
		String minString = SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getMinNum();
		if(Double.parseDouble(minString)>0){
			min = Double.parseDouble(minString);
		}else{
			min = 1;
		}
		init();
		initCart();
		initView();
		initData();
		initProductInfo();
	}
	private void init() {
//		min = SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getMinNum();
//		max = SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getMaxNum();
		cartDb = new CarSalesShoppingCartDB(CarSaleProductDetailActivity.this);
		ctrlDB = new CarSalesProductCtrlDB(CarSaleProductDetailActivity.this);
		util = new CarSalesUtil(CarSaleProductDetailActivity.this);
		promotionDB= new CarSalesPromotionDB(CarSaleProductDetailActivity.this);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mIndex = bundle.getInt("index");
		productId = bundle.getInt("productId");
		unitId = bundle.getInt("unitId");
		carSalesProduct = util.product(productId, unitId);
		carSalesProductCtrl = ctrlDB.findProductCtrlByProductIdAndUnitId(productId, unitId);
		map = util.getStockCtrl();
		String maxString = SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getMaxNum();
		if(Double.parseDouble(maxString)<=carSalesProduct.getInventory()){
			max = Double.parseDouble(maxString);
		}else{
			max = carSalesProduct.getInventory();
		}
		if(max>0){
			
		}else{
			max = carSalesProduct.getInventory();
		}
	}
	private void initProductInfo() {
		Map<String, String> infoMap = util.productInfo(productId);
		ll_product_detail_info.removeAllViews();
		if(infoMap!=null&&infoMap.size()>0){
			tv_info.setVisibility(View.VISIBLE);
			sv_product_detail_info.setVisibility(View.VISIBLE);
			Set keySet = infoMap.keySet(); // key的set集合  
	        Iterator it = keySet.iterator();  
	        while(it.hasNext()){  
	            String k = (String) it.next(); // key  
	            String v = infoMap.get(k);  //value   
	            if(!TextUtils.isEmpty(v)){
	            	CarSalesProductInfoView view = new CarSalesProductInfoView(CarSaleProductDetailActivity.this);
		            view.ininData(k, v);
		            ll_product_detail_info.addView(view.getView());
	            }
	        }  
		}else{
			tv_info.setVisibility(View.GONE);
			sv_product_detail_info.setVisibility(View.GONE);
		}
	}
	String promoId;
	private void initData() {
		tv_product_detail_name.setText(carSalesProduct.getName());
		promoId = cart.getPromotionIds();
		if(carSalesProduct.isPromotion()){ //判断是否有打折促销,有的话显示并设置内容，没有的话隐藏
			isDis = true;
			tv_product_detail_dicount_info.setVisibility(View.VISIBLE);
			searchPro();
			showDiscount();
			initPromitionInfo();
		} else{
			isDis = false;
			tv_product_detail_dicount_info.setVisibility(View.GONE);
		}
//		tv_product_detail.setText("库存："+carSalesProduct.getInventory()+carSalesProduct.getUnit());//库存量  数量+单位
//		tv_product_detail.setText("库存：-");//库存量  数量+单位
		if(!TextUtils.isEmpty(map.get(String.valueOf(carSalesProductCtrl.getId())))){
			double number = Double.parseDouble(map.get(String.valueOf(carSalesProductCtrl.getId())));
			if(number>0){
				tv_product_detail.setText("库存："+PublicUtils.formatDouble(number)+carSalesProduct.getUnit());
			}else{
				tv_product_detail.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory())+carSalesProduct.getUnit());
			}
			
		}else{
			tv_product_detail.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory())+carSalesProduct.getUnit());
		}
		if(SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getIsPhonePrice()==1){
			tv_product_detail_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));//单价
			tv_product_detail_price.setEnabled(false);
		}else if(SharedPreferencesForCarSalesUtil.getInstance(CarSaleProductDetailActivity.this).getIsPhonePrice()==2){
			tv_product_detail_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));//单价
			tv_product_detail_price.setEnabled(true);
		}
		tv_product_detail_price.addTextChangedListener(priceWatcher);
			
		et_data.setText(PublicUtils.formatDouble(cart.getNumber()));
		// tv_price.setText("￥"+PublicUtils.formatDouble(order3Product.getPrice()));
//		tv_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));
		
		et_data.addTextChangedListener(watcher);
		if(carSalesProduct.isPromotion()){
			if(!TextUtils.isEmpty(promoId)&&!promoId.equals("0")){
				CarSalesPromotion promotion = promotionDB.findPromotionByPromotionId(Integer.parseInt(promoId));
				tv_product_detail_price_info.setText(promotion.getName());
				if((cart.getDisAmount()+cart.getDiscountPrice())!=0){
					tv_price.setText(PublicUtils.formatDouble(cart.getDisAmount()+cart.getDiscountPrice()));//总价，随数量的变化而自动变化
				}else{
					tv_price.setText(PublicUtils.formatDouble(cart.getSubtotal()));//总价，随数量的变化而自动变化
				}
				
			}else{
				tv_product_detail_price_info.setText("无");
				tv_price.setText(PublicUtils.formatDouble(cart.getSubtotal()));//总价，随数量的变化而自动变化
			}
		}else{
			tv_product_detail_price_info.setText("无");
			tv_price.setText(PublicUtils.formatDouble(cart.getSubtotal()));//总价，随数量的变化而自动变化
		}
		
	}
	private void initPromitionInfo() {
		ll_product_detail_dicount_info.removeAllViews();
		for( int i = 0; i<promotions.size();i++){
//			final CheckBox cB;
			CarSalesProductDiscountInfo info = new CarSalesProductDiscountInfo(CarSaleProductDetailActivity.this);
			 final CarSalesPromotion promoti = promotions.get(i);
			 CarSalePromotionSyncInfo syncInfo = new CarSalePromotionSyncInfo();
			syncInfo.setSyncInfoName(promoti.getName());
			syncInfo.setSyncValidTerm("有效期:"+promoti.getfTime()+" 至 "+promoti.gettTime());
			syncInfo.setSyncDisFunction(promoti.getMark());
			info.initData(syncInfo);
			ll_product_detail_dicount_info.addView(info.getView());
			
			cB = info.getCheckBox();
			boxes.add(cB);
			if(!TextUtils.isEmpty(promoId)&&promoId.equals(String.valueOf(promoti.getPromotionId()))){
				cB.setChecked(true);
			}else{
				cB.setChecked(false);
			}
			final int p = i;
			if(promotions.size() == 1){
				cB.setEnabled(false);
			}else{
				cB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int position = 0;
						if(isChecked){
							cart.setPromotionIds(String.valueOf(promoti.getPromotionId()));
							buttonView.setTag(p);
							position = p;
							isSelectInfo(buttonView);
							tv_product_detail_price_info.setText(promoti.getName());
							promoId = String.valueOf(promoti.getPromotionId());
						}else{
						}
						int size = 0;
						for(int i = 0; i<boxes.size();i++){
							if(boxes.get(i).isChecked()||position!=0){
								break;
							}else{
								size++;
								continue;
							}
						}
						if(size==boxes.size()){
							cart.setPromotionIds(null);
							util.initPro(cart);
							promoId = String.valueOf(0);
							tv_product_detail_price_info.setText("无");
						}
						changeTotalPrice();
					}
				});
			}
			
		}
	}
	private CheckBox cB;
	List<CheckBox> boxes = new ArrayList<CheckBox>();
	private void isSelectInfo(CompoundButton buttonView){
		int p = (Integer) buttonView.getTag();
		for (int i = 0; i < boxes.size(); i++) {
			if (p == i) {
				boxes.get(i).setChecked(true);
			} else {
				boxes.get(i).setChecked(false);
			}
		}
		
	}
	private void searchPro() {
		String orgCode = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesOrgId();
		int level = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreLevel();
		promotions = promotionDB.findPromotionByProductIdAndUnitId(productId, unitId,orgCode,level);
	}
	private void initView() {
		tv_info = (TextView) this.findViewById(R.id.tv_info);
		ll_product_detail_info = (LinearLayout) this.findViewById(R.id.ll_product_detail_info);
		sv_product_detail_info = (ScrollView) this.findViewById(R.id.sv_product_detail_info);
		tv_product_detail_name = (TextView) this.findViewById(R.id.tv_product_detail_name);
		tv_product_detail_dicount_info = (TextView) this.findViewById(R.id.tv_product_detail_dicount_info);
		ll_product_detail_dicount_info = (LinearLayout) this.findViewById(R.id.ll_product_detail_dicount_info);
		tv_product_detail_dicount_info.setOnClickListener(listner);
		tv_product_detail = (TextView) this.findViewById(R.id.tv_product_detail);
		tv_product_detail_price = (EditText) this.findViewById(R.id.tv_product_detail_price);
		ll_sub = (LinearLayout) this.findViewById(R.id.ll_sub);
		ll_add = (LinearLayout) this.findViewById(R.id.ll_add);
		ll_sub.setOnClickListener(listner);
		ll_add.setOnClickListener(listner);
		tv_price = (TextView) this.findViewById(R.id.tv_price);
		tv_product_detail_price_info = (TextView) this.findViewById(R.id.tv_product_detail_price_info);
		et_data = (EditText) this.findViewById(R.id.et_data);
		btn_addtoshoppingcar = (Button) this.findViewById(R.id.btn_addtoshoppingcar);
		btn_addtoshoppingcar.setOnClickListener(listner);
//		ll_shopping_car_container = (LinearLayout) this.findViewById(R.id.ll_shopping_car_container);
//		ll_shopping_car_container.setOnClickListener(listner);
		
		ll_home = (LinearLayout) this.findViewById(R.id.ll_home);
		ll_home.setOnClickListener(listner);
	}
	private void initCart() {
		cart = cartDb.findCarSalesShoppingCartByProductId(productId, unitId);
		if(cart==null){
			String orgCode = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesOrgId();
			int level = SharedPreferencesForCarSalesUtil.getInstance(this).getCarSalesStoreLevel();
			List<CarSalesPromotion> promos = promotionDB.findPromotionByProductIdAndUnitId(productId, unitId,orgCode,level);
			cart = new CarSalesShoppingCart();
			cart.setProductId(productId);
			cart.setUnitId(unitId);
			cart.setNumber(min);
			cart.setSubtotal(getTotal(min, carSalesProduct.getPrice()));
			cart.setPitcOn(1);
			cart.setpId(ctrlDB.findProductCtrlByProductIdAndUnitId(productId, unitId).getpId());
			cart.setNowProductPrict(carSalesProduct.getPrice());
			if(promos!=null&&promos.size()>0){
				cart.setPromotionIds(String.valueOf(promos.get(0).getPromotionId()));
				util.fenLei(promos.get(0), carSalesProduct, cart, min);
			}else{
				cart.setPromotionIds(null);
			}
		}
	}
	private TextWatcher priceWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
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
//	    		tv_product_detail_price.setText("1");
	    		str = "0";
	    	}
	    	cart.setNowProductPrict(Double.parseDouble(str));
	    	changeTotalPrice();
			PublicUtils.setEditTextSelection(et_data);
			PublicUtils.setEditTextSelection(tv_product_detail_price);
		}

		
	};
	private void changeTotalPrice() {
		String allNum = et_data.getText().toString();
    	if(!TextUtils.isEmpty(allNum)){
    		if(allNum.equals(".")){
    			allNum = "0";
			}else if (allNum.startsWith(".")) {
				allNum = "0"+allNum;
			}else if(allNum.endsWith(".")){
				allNum = allNum+"0";
			}
    	}else{
    		allNum = "0";
    	}
//		String allNum = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if (!TextUtils.isEmpty(allNum)) {
			if(!TextUtils.isEmpty(promoId)&&!promoId.equals("0")){
				CarSalesPromotion promotion = promotionDB.findPromotionByPromotionId(Integer.parseInt(promoId));
				util.fenLei(promotion, carSalesProduct, cart, Double.parseDouble(allNum));
			}
			changeResult();
		}
	}
	private void changeResult() {
		String allNum = et_data.getText().toString();
    	if(!TextUtils.isEmpty(allNum)){
    		if(allNum.equals(".")){
    			allNum = "0";
			}else if (allNum.startsWith(".")) {
				allNum = "0"+allNum;
			}else if(allNum.endsWith(".")){
				allNum = allNum+"0";
			}
    	}else{
    		allNum = "0";
    	}
    	String price = tv_product_detail_price.getText().toString();
    	if(!TextUtils.isEmpty(price)){
    		if(price.equals(".")){
    			price = "0";
			}else if (price.startsWith(".")) {
				price = "0"+price;
			}else if(price.endsWith(".")){
				price = price+"0";
			}
    	}else{
    		price = "0";
    	}
//		String price = TextUtils.isEmpty(tv_product_detail_price.getText().toString())?"0":tv_product_detail_price.getText().toString();
//		String allNum = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if (!TextUtils.isEmpty(allNum)) {
			double totalPrice = getTotal(Double.parseDouble(allNum),Double.valueOf(price));
			if (cart.getDiscountPrice() != 0) {
				tv_price.setText(PublicUtils.formatDouble(cart.getDiscountPrice()));
			}else if(cart.getDisAmount()!= 0){
				tv_price.setText(PublicUtils.formatDouble(cart.getDisAmount()));
			}else {
				tv_price.setText(PublicUtils.formatDouble(totalPrice));
			}
			cart.setNowProductPrict(Double.valueOf(price));
			cart.setSubtotal(Double.parseDouble(PublicUtils.formatDouble(totalPrice)));
			cart.setNumber(Double.parseDouble(allNum));
		}
	}
	private TextWatcher watcher = new TextWatcher() {
	    
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	        
	    }
	    
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	        
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
	    	String str = s.toString();
	    	if(!TextUtils.isEmpty(str)){
	    		if(str.equals(".")){
					str = "0";
				}else if (str.startsWith(".")) {
					str = "0"+str;
				}else if(str.endsWith(".")){
					str = str+"0";
				}
	    		if(max>=min){
		    		if(Double.parseDouble(str)<=max&&Double.parseDouble(str)>=min){
		    			
		    		}else if(Double.parseDouble(str)>max){
		    			if(carSalesProduct!=null){
							ToastOrder.makeText(CarSaleProductDetailActivity.this, "最多可购买"+PublicUtils.formatDouble(max)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
						}
		    			et_data.setText(PublicUtils.formatDouble(max));
		    		}else {
		    			if(carSalesProduct!=null){
		    				ToastOrder.makeText(CarSaleProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			et_data.setText(String.valueOf(min));
		    		}
		    	}else{
		    		if(Double.parseDouble(str)>=min){
		    			
		    		}else{
		    			if(carSalesProduct!=null){
		    				ToastOrder.makeText(CarSaleProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			et_data.setText(String.valueOf(min));
		    		}
		    	}
	    		map.put(String.valueOf(carSalesProductCtrl.getId()), String.valueOf(carSalesProduct.getInventory()-Double.parseDouble(str)));
	    		if((carSalesProduct.getInventory()-Double.parseDouble(str))>=0){
	    			tv_product_detail.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()-Double.parseDouble(str))+carSalesProduct.getUnit());
	    		}
	    	}else{
	    		if(carSalesProduct!=null){
    				ToastOrder.makeText(CarSaleProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
    			}
//    			et_data.setText(String.valueOf(min));
	    		map.put(String.valueOf(carSalesProductCtrl.getId()), String.valueOf(carSalesProduct.getInventory()-min));
    			if((carSalesProduct.getInventory()-min)>=0){
    				tv_product_detail.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()-min)+carSalesProduct.getUnit());
	    		}
	    	}
	    	changeTotalPrice();
			PublicUtils.setEditTextSelection(et_data);
			PublicUtils.setEditTextSelection(tv_product_detail_price);
	    }
	};
	
	public double getTotal(double number,double price ){
		double total = number*price;
		return total;
	}
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_addtoshoppingcar:
				addToShoppingCar();
				break;
			case R.id.ll_home:
				goToHome();
				break;
			case R.id.ll_sub:
				sub();
				break;
			case R.id.ll_add:
				add();
				break;
			case R.id.tv_product_detail_dicount_info://设置显示促销信息
				showDiscount();
				break;
//			case R.id.ll_shopping_car_container:
//				toToShoppingCar();
//				break;
			default:
				break;
			}
		}
	};
	private void toToShoppingCar() {
		
		Intent intent = new Intent(CarSaleProductDetailActivity.this,CarSaleShoppingCartActivity.class);
		startActivity(intent);
		finish();
	}

	private void showDiscount() {
		if(isDis){
			isDis = false;
			ll_product_detail_dicount_info.setVisibility(View.VISIBLE);
		}else{
			isDis = true;
			ll_product_detail_dicount_info.setVisibility(View.GONE);
		}
	}

	private void add() {
		int ii = 0;
		String ss  = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if(!TextUtils.isEmpty(ss)){
			ii= (int) Double.parseDouble(ss);
		}
		int s = ++ii;
		
		if(max>=min){
			if(s<=max){
				et_data.setText(PublicUtils.formatDouble(s));
			}else{
				et_data.setText(PublicUtils.formatDouble(max));
				if(carSalesProduct!=null){
					ToastOrder.makeText(CarSaleProductDetailActivity.this, "最多可购买"+PublicUtils.formatDouble(max)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
				}
			}
		}else{
			et_data.setText(PublicUtils.formatDouble(s));
		}
		
		changeTotalPrice();
	}

	private void sub() {
		int ii = 0;
		String ss  = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if(TextUtils.isEmpty(ss)){
			ii=1;
		}else{
			ii= (int) Double.parseDouble(ss);
		}
		int s = --ii;
		
		if(s>=min){
			et_data.setText(PublicUtils.formatDouble(s));
		}else{
			et_data.setText(PublicUtils.formatDouble(min));
			if(carSalesProduct!=null){
				ToastOrder.makeText(CarSaleProductDetailActivity.this, "至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
			}
		}
		changeTotalPrice();
	}

	private void goToHome() {
		Intent intent = new Intent(CarSaleProductDetailActivity.this,ProductChoiceActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	private void addToShoppingCar() {
		util.saveStockCtrl(map);
		CarSalesProductCtrl c = ctrlDB.findProductCtrlByProductIdAndUnitId(cart.getProductId(), cart.getUnitId());
		map.put(String.valueOf(c.getId()), String.valueOf(carSalesProduct.getInventory()-cart.getNumber()));
		CarSalesShoppingCart oCar = cartDb.findCarSalesShoppingCartByProductId(cart.getProductId(), cart.getUnitId());
		if (oCar !=null) {
			cartDb.updateCarSalesShoppingCart(cart);
		}else{
			cartDb.insert(cart);
		}
		ToastOrder.makeText(CarSaleProductDetailActivity.this, "添加成功", ToastOrder.LENGTH_SHORT).show();
//		order3ShoppingCar.initShoppingCartCount();
		if(mIndex==1){
			finish();
		}else if(mIndex == 0){
			toToShoppingCar();
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(mIndex==1){
				finish();
			}else if(mIndex == 0){
				toToShoppingCar();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}