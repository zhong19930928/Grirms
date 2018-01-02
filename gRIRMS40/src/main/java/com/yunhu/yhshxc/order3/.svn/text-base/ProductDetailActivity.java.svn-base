package com.yunhu.yhshxc.order3;

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
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.bo.Order3PromotionSyncInfo;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.db.Order3ProductCtrlDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3ProductDiscountInfo;
import com.yunhu.yhshxc.order3.view.Order3ProductInfo;
import com.yunhu.yhshxc.order3.view.Order3ShoppingCar;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class ProductDetailActivity extends AbsBaseActivity {
	private double max ;
	private double min ;
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
	private Order3ProductCtrl productCtrl = null;
	private LinearLayout ll_home;
	private LinearLayout ll_sub;
	private LinearLayout ll_add;
	private LinearLayout ll_product_detail_info;

	private TextView tv_info;
	private LinearLayout ll_shopping_car_container;
	private LinearLayout ll_product_detail_dicount_info;
	private ScrollView sv_product_detail_info;
	private Order3Product order3Product;
	private Order3ShoppingCart cart;
	private Order3ShoppingCartDB cartDb;
	private Order3Util order3Util;
	private int productId;
	private int unitId;
	private int mIndex = 1;
	private boolean isDis = false;
	private Order3ProductCtrlDB ctrlDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order3_product_detai);
		min = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(ProductDetailActivity.this).getMinNum());
		max = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(ProductDetailActivity.this).getMaxNum());
		if(min>0){
		}else{
			min = 1;
		}
		if(max <=0){
			max = Double.MAX_VALUE;
		}
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mIndex = bundle.getInt("index");
		productId = bundle.getInt("productId");
		unitId = bundle.getInt("unitId");
		promotionDB = new Order3PromotionDB(ProductDetailActivity.this);
		order3Util = new Order3Util(this);
		order3Product = order3Util.product(productId, unitId);
		cartDb = new Order3ShoppingCartDB(this);
		order3ShoppingCartDB = new Order3ShoppingCartDB(this);
		ctrlDB = new Order3ProductCtrlDB(this);
		initCart();
		initView();
		initData();
		initProductInfo();
		
	}

	StringBuffer sb = new StringBuffer();
	public void initCart(){
		cart = cartDb.findOrder3ShoppingCartByProductId(productId, unitId);
		if(cart==null){
			String orgCode = SharedPreferencesForOrder3Util.getInstance(this).getOrder3OrgId();
			int level = SharedPreferencesForOrder3Util.getInstance(this).getOrder3StoreLevel();
			List<Order3Promotion> promos = promotionDB.findPromotionByProductIdAndUnitId(productId, unitId,orgCode,level);
			cart = new Order3ShoppingCart();
			cart.setProductId(productId);
			cart.setUnitId(unitId);
			cart.setNumber(min);
			if (order3Product!=null) {
				cart.setSubtotal(getTotal(min, order3Product.getPrice()));
			}
			cart.setPitcOn(1);
			Order3ProductCtrl ctr = ctrlDB.findProductCtrlByProductIdAndUnitId(productId, unitId);
			if(ctr!=null){
				String pid = !TextUtils.isEmpty(ctr.getpId())?ctr.getpId():"";
				cart.setpId(pid);
			}
			cart.setNowProductPrict(order3Product.getPrice());
			if(promos!=null&&promos.size()>0){
				cart.setPromotionIds(String.valueOf(promos.get(0).getPromotionId()));
				proFenLeiInit(promos.get(0));
			}else{
				cart.setPromotionIds(null);
			}
		}
	}
	private void proFenLeiInit(Order3Promotion order3Promotion) {
		initProFenLei();
		if (min!=0) {
			double sinFullPrice = min * order3Product.getPrice();
			switch (order3Promotion.getPreType()) {
			case 1:// 单品买满数量
				if (min >= order3Promotion.getmCnt()) {
					int a = (int) (min/order3Promotion.getmCnt());
					switch (order3Promotion.getDisType()) {
					case 3://打折  
						singleFullNumDis = discount(min,order3Promotion,order3Product.getPrice());
						cart.setDiscountPrice(singleFullNumDis);
						break;
					case 1://送赠品
						singleFullNumProduct = order3Util.product(Integer.parseInt(order3Promotion.getsId()),Integer.parseInt(order3Promotion.getsUid()));//根据mTab,mId,mUid去查赠品
						singleFullNumZeng = zengpin(a,order3Promotion);
						cart.setDisNumber(singleFullNumZeng);
						cart.setPro(singleFullNumProduct);
						cart.setGiftId(singleFullNumProduct.getProductId());
						cart.setGiftUnitId(singleFullNumProduct.getUnitId());
						break;
					case 2://减免金额
						singleFullNumPri = subMoney(a,order3Promotion,min,order3Product.getPrice());
						cart.setDisAmount(singleFullNumPri);
						break;
					default:
						break;
					}
				}
				break;
			case 2:// 单品买满金额
				if (sinFullPrice >= order3Promotion.getAmount()) {
					int b = (int) (sinFullPrice/order3Promotion.getAmount());
					switch (order3Promotion.getDisType()) {
					case 3://打折  
						singleFullPriceDis = discount(min,order3Promotion,order3Product.getPrice());
						cart.setDiscountPrice(singleFullPriceDis);
						break;
					case 1://送赠品
						singleFullPriceProduct = order3Util.product(Integer.parseInt(order3Promotion.getsId()),Integer.parseInt(order3Promotion.getsUid()));//根据mTab,mId,mUid去查赠品
						singleFullPriceZeng = zengpin(b,order3Promotion);
						cart.setDisNumber(singleFullPriceZeng);
						cart.setPro(singleFullPriceProduct);
						cart.setGiftId(singleFullPriceProduct.getProductId());
						cart.setGiftUnitId(singleFullPriceProduct.getUnitId());
						break;
					case 2://减免金额
						singleFullPricePri = subMoney(b,order3Promotion,min,order3Product.getPrice());
						cart.setDisAmount(singleFullPricePri);
						break;

					default:
						break;
					}
				}
				break;

			default:
				break;
			}
		}
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
		ll_shopping_car_container = (LinearLayout) this.findViewById(R.id.ll_shopping_car_container);
		ll_shopping_car_container.setOnClickListener(listner);
		
		ll_home = (LinearLayout) this.findViewById(R.id.ll_home);
		ll_home.setOnClickListener(listner);
	}
	 Order3ShoppingCar order3ShoppingCar;
	 Order3PromotionDB promotionDB;
	 List<Order3Promotion> promotions;
	 String promoId;
	private void initData() {
		tv_product_detail_name.setText(order3Product.getName());
		promoId = cart.getPromotionIds();
		if(order3Product.isPromotion()){ //判断是否有打折促销,有的话显示并设置内容，没有的话隐藏
			isDis = true;
			tv_product_detail_dicount_info.setVisibility(View.VISIBLE);
			searchPro();
			showDiscount();
			initPromitionInfo();
		} else{
			isDis = false;
			tv_product_detail_dicount_info.setVisibility(View.GONE);
		}
//		tv_product_detail.setText("库存："+order3Product.getInventory()+order3Product.getUnit());//库存量  数量+单位
		tv_product_detail.setText("库存：-");//库存量  数量+单位
		if(SharedPreferencesForOrder3Util.getInstance(ProductDetailActivity.this).getIsPhonePrice()==1){
			tv_product_detail_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));//单价
			tv_product_detail_price.setEnabled(false);
		}else if(SharedPreferencesForOrder3Util.getInstance(ProductDetailActivity.this).getIsPhonePrice()==2){
			tv_product_detail_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));//单价
			tv_product_detail_price.setEnabled(true);
		}
		tv_product_detail_price.addTextChangedListener(priceWatcher);
			
		et_data.setText(PublicUtils.formatDouble(cart.getNumber()));
		// tv_price.setText("￥"+PublicUtils.formatDouble(order3Product.getPrice()));
//		tv_price.setText(PublicUtils.formatDouble(cart.getNowProductPrict()));
		
		et_data.addTextChangedListener(watcher);
		
		if(!TextUtils.isEmpty(promoId)&&!promoId.equals("0")){
			Order3Promotion promotion = promotionDB.findPromotionByPromotionId(Integer.parseInt(promoId));
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
		
		
	//对购物车进行设置
		order3ShoppingCar = new Order3ShoppingCar(this);
		order3ShoppingCar.initShoppingCartCount();;//判断购物车里数量是多少然后设置
		ll_shopping_car_container.addView(order3ShoppingCar.getView());
		PublicUtils.setEditTextSelection(et_data);
	}
	
	/**
	 * 查询促销规则
	 */
	private void searchPro() {
		
		String orgCode = SharedPreferencesForOrder3Util.getInstance(this).getOrder3OrgId();
		int level = SharedPreferencesForOrder3Util.getInstance(this).getOrder3StoreLevel();
		promotions = promotionDB.findPromotionByProductIdAndUnitId(productId, unitId,orgCode,level);
		
	}
	private double singleFullNum;
	private double singleFullPrice;
	private double singleFullNumDis;//单品买满数量打折
	private double singleFullNumPri;//单品买满数量减免
	private double singleFullNumZeng;//单品买满数量送赠品数
	private double singleFullPriceDis;//单品买满金额打折
	private double singleFullPricePri;//单品买满金额减免
	private double singleFullPriceZeng;//单品买满金额送赠品数
	private Order3Product singleFullNumProduct;
	private Order3Product singleFullPriceProduct;
	private void initProFenLei(){
		singleFullNumDis = 0;
		singleFullNumProduct = null;
		singleFullNumZeng = 0;
		singleFullNumPri = 0;
		singleFullPriceDis= 0;
		singleFullPriceProduct = null;
		singleFullPriceZeng = 0;
		singleFullPricePri = 0;
	}
//	Order3Promotion pro;
	/**
	 * 促销分类
	 */
	private void proFenLei(Order3Promotion pros) {
		initProFenLei();
		initPro();
//		this.pro = pros;
		String et = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if (et.equals(".")) {
			et = "0";
		} else if (et.startsWith(".")) {
			et = "0" + et;
		} else if (et.endsWith(".")) {
			et = et + "0";
		}
		if (!TextUtils.isEmpty(et)) {
			singleFullNum = Double.parseDouble(et);
			String s = TextUtils.isEmpty(tv_product_detail_price.getText().toString())?"0":tv_product_detail_price.getText().toString();
			if(s.equals(".")){
				s = "0";
			}else if (s.startsWith(".")) {
				s = "0"+s;
			}else if(s.endsWith(".")){
				s = s+"0";
			}
			singleFullPrice = singleFullNum * Double.valueOf(s);
			switch (pros.getPreType()) {
			case 1:// 单品买满数量
				if (singleFullNum >= pros.getmCnt()) {
					proFenLei1(pros.getPreType(), pros.getDisType(),pros);
				}
				break;
			case 2:// 单品买满金额
				if (singleFullPrice >= pros.getAmount()) {
					proFenLei2(pros.getPreType(), pros.getDisType(),pros);
				}
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 单品买满数量 
	 */
	private void proFenLei1(int preType,int disType,Order3Promotion pro) {
		int a = (int) (singleFullNum/pro.getmCnt());
		
			switch (disType) {
			case 3://打折  
				singleFullNumDis = discount(singleFullNum,pro,cart.getNowProductPrict());
				cart.setDiscountPrice(singleFullNumDis);
				break;
			case 1://送赠品
				singleFullNumProduct = order3Util.product(Integer.parseInt(pro.getsId()),Integer.parseInt(pro.getsUid()));//根据mTab,mId,mUid去查赠品
				singleFullNumZeng = zengpin(a,pro);
				cart.setDisNumber(singleFullNumZeng);
				cart.setPro(singleFullNumProduct);
				cart.setGiftId(singleFullNumProduct.getProductId());
				cart.setGiftUnitId(singleFullNumProduct.getUnitId());
				break;
			case 2://减免金额
				singleFullNumPri = subMoney(a,pro,singleFullNum,cart.getNowProductPrict());
				cart.setDisAmount(singleFullNumPri);
				
				break;
			default:
				break;
			}
		
	}
	private void initPro(){
		cart.setDiscountPrice(0);
		cart.setDisAmount(0);
		cart.setDisNumber(0);
		cart.setPreAmount(0);
		cart.setPrePrice(0);
		cart.setPro(null);
		cart.setGiftId(0);
		cart.setGiftUnitId(0);
		
	}
	/**
	 * 单品买满金额
	 * @param preType
	 * @param disType
	 */
	private void proFenLei2(int preType,int disType,Order3Promotion pro) {
		int b = (int) (singleFullPrice/pro.getAmount());
//		initPro();
			switch (disType) {
			case 3://打折  
				singleFullPriceDis = discount(singleFullNum,pro,cart.getNowProductPrict());
				cart.setDiscountPrice(singleFullPriceDis);
				break;
			case 1://送赠品
				singleFullPriceProduct = order3Util.product(Integer.parseInt(pro.getsId()),Integer.parseInt(pro.getsUid()));//根据mTab,mId,mUid去查赠品
				singleFullPriceZeng = zengpin(b,pro);
				cart.setDisNumber(singleFullPriceZeng);
				cart.setPro(singleFullPriceProduct);
				cart.setGiftId(singleFullPriceProduct.getProductId());
				cart.setGiftUnitId(singleFullPriceProduct.getUnitId());
				break;
			case 2://减免金额
				singleFullPricePri = subMoney(b,pro,singleFullNum,cart.getNowProductPrict());
				cart.setDisAmount(singleFullPricePri);
				break;

			default:
				break;
			}

	}
	private double subMoney(int bei,Order3Promotion o,double number,double price) {
		double disAmount = o.getDisAmount()*bei;//返还现金金额
		double prePriceJ = number*price;
		double nowPriceJ = prePriceJ - disAmount;
		cart.setPreAmount(disAmount);
		return nowPriceJ;
	}
	private double zengpin(int bei,Order3Promotion order3Promotion) {
		double zengpinNum = order3Promotion.getsCnt()*bei;//赠送产品数；
		return zengpinNum;

	}

	private double discount(double number,Order3Promotion order3Promotion,double price) {
		double disRate = changeDouble(Integer.parseInt(order3Promotion.getDisRate())) ;//折扣率
   		double prePrice = number*price;
   		double nowPrice = prePrice*disRate;
   		double p = prePrice-nowPrice;
   		cart.setPrePrice(Double.parseDouble(PublicUtils.formatDouble(p)));
   		return nowPrice;

	}
	public  double changeDouble(int s){
		int g = s/10;
		int y = s%10;
		StringBuffer sb = new StringBuffer();
		if(g==0){
			sb.append("0.0").append(String.valueOf(y));
			return Double.parseDouble(sb.toString());
		}else{
			sb.append("0.").append(String.valueOf(g)).append(String.valueOf(y));
			return Double.parseDouble(sb.toString());
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
	    	}
	    	
//	    	else{
//	    		tv_product_detail_price.setText("");
//	    	}
	    	changeTotalPrice();
			PublicUtils.setEditTextSelection(et_data);
			PublicUtils.setEditTextSelection(tv_product_detail_price);
		}
	};
	
	double totalPrice ;
	
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
		    			if(order3Product!=null){
							ToastOrder.makeText(ProductDetailActivity.this, "最多可购买"+PublicUtils.formatDouble(max)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
						}
		    			et_data.setText(PublicUtils.formatDouble(max));
		    		}else {
		    			if(order3Product!=null){
		    				ToastOrder.makeText(ProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			et_data.setText(PublicUtils.formatDouble(min));
		    		}
		    	}else{
		    		if(Double.parseDouble(str)>=min){
		    			
		    		}else{
		    			if(order3Product!=null){
		    				ToastOrder.makeText(ProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			et_data.setText(PublicUtils.formatDouble(min));
		    		}
		    		
		    	}
	    		
	    	}else{
	    		if(order3Product!=null){
    				ToastOrder.makeText(ProductDetailActivity.this,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
    			}
//    			et_data.setText(PublicUtils.formatDouble(min));
	    	}
	    	changeTotalPrice();
			PublicUtils.setEditTextSelection(et_data);
			PublicUtils.setEditTextSelection(tv_product_detail_price);
			
	    }

		
	};

	private void changeTotalPrice() {
		String s = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if(s.equals(".")){
			s = "0";
		}else if (s.startsWith(".")) {
			s = "0"+s;
		}else if(s.endsWith(".")){
			s = s+"0";
		}
		if (!TextUtils.isEmpty(s)) {
			singleFullNumDis = 0;
			singleFullNumPri= 0;
			singleFullPriceDis = 0;
			singleFullPricePri = 0;
			singleFullPriceZeng = 0;
			singleFullPriceProduct= null;
			if(!TextUtils.isEmpty(promoId)&&!promoId.equals("0")){
				Order3Promotion promotion = promotionDB.findPromotionByPromotionId(Integer.parseInt(promoId));
				proFenLei(promotion);
			}
			changeResult();
		}

	}
	public void changeResult(){
		String et = TextUtils.isEmpty(et_data.getText().toString())?"0":et_data.getText().toString();
		if(et.equals(".")){
			et = "0";
		}else if (et.startsWith(".")) {
			et = "0"+et;
		}else if(et.endsWith(".")){
			et = et+"0";
		}
		if (!TextUtils.isEmpty(et)) {
			String s = TextUtils.isEmpty(tv_product_detail_price.getText().toString())?"0":tv_product_detail_price.getText().toString();
			if(s.equals(".")){
				s = "0";
			}else if (s.startsWith(".")) {
				s = "0"+s;
			}else if(s.endsWith(".")){
				s = s+"0";
			}
			totalPrice = getTotal(Double.parseDouble(et),Double.valueOf(s));
			if (singleFullNumDis != 0) {
				tv_price.setText(PublicUtils.formatDouble(singleFullNumDis));
				cart.setDiscountPrice(singleFullNumDis);
			}else if(singleFullNumPri!= 0){
				tv_price.setText(PublicUtils.formatDouble(singleFullNumPri));
				cart.setDisAmount(singleFullNumPri);
			}else if(singleFullPriceDis!=0){
				tv_price.setText(PublicUtils.formatDouble(singleFullPriceDis));
				cart.setDiscountPrice(singleFullPriceDis);
			}else if(singleFullPricePri!=0){
				tv_price.setText(PublicUtils.formatDouble(singleFullPricePri));
				cart.setDisAmount(singleFullPricePri);
			}else {
				tv_price.setText(PublicUtils.formatDouble(totalPrice));
			}
			cart.setNowProductPrict(Double.valueOf(s));
			cart.setSubtotal(Double.parseDouble(PublicUtils.formatDouble(totalPrice)));
			cart.setNumber(Double.parseDouble(et));
		
		}
	}
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
			case R.id.ll_shopping_car_container:
				
				toToShoppingCar();
				
				break;
			default:
				break;
			}
		}

	};
	private void toToShoppingCar() {
		
		Intent intent = new Intent(ProductDetailActivity.this,ShoppingCarActivity.class);
		startActivity(intent);
		finish();
	}
	private void goToHome() {
		Intent intent = new Intent(ProductDetailActivity.this,Order3HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	private List<CheckBox> boxes = new ArrayList<CheckBox>();
	private void initPromitionInfo(){
		ll_product_detail_dicount_info.removeAllViews();
		for(int i = 0; i<promotions.size();i++){
			Order3ProductDiscountInfo info = new Order3ProductDiscountInfo(ProductDetailActivity.this);
			 final Order3Promotion promoti = promotions.get(i);
			
			Order3PromotionSyncInfo syncInfo = new Order3PromotionSyncInfo();
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
			if(promotions.size() != 1){
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
							size++;
							if(boxes.get(i).isChecked()||position!=0){
								break;
							}else{
								continue;
							}
						}
						if(size==boxes.size()){
							cart.setPromotionIds(null);
							initPro();
							promoId = String.valueOf(0);
							tv_product_detail_price_info.setText("无");
						}
						
						changeTotalPrice();
					}
				});
			}else{
				cB.setEnabled(false);
			}
		}
	}
	private CheckBox cB;
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
		if(ss.equals(".")){
			ss = "0";
		}else if (ss.startsWith(".")) {
			ss = "0"+ss;
		}else if(ss.endsWith(".")){
			ss = ss+"0";
		}
		if(!TextUtils.isEmpty(ss)){
			ii= (int) Double.parseDouble(ss);
		}
		int s = ++ii;
		
		if(max>=min){
			if(s<=max){
				et_data.setText(PublicUtils.formatDouble(s));
			}else{
				et_data.setText(PublicUtils.formatDouble(max));
				if(order3Product!=null){
					ToastOrder.makeText(ProductDetailActivity.this, "最多可购买"+PublicUtils.formatDouble(max)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
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
		if(ss.equals(".")){
			ss = "0";
		}else if (ss.startsWith(".")) {
			ss = "0"+ss;
		}else if(ss.endsWith(".")){
			ss = ss+"0";
		}
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
			if(order3Product!=null){
				ToastOrder.makeText(ProductDetailActivity.this, "至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
			}
		}

		changeTotalPrice();
	}
	
	
	
	 Order3ShoppingCartDB order3ShoppingCartDB;
	private void addToShoppingCar() {
		Order3ProductCtrl c = ctrlDB.findProductCtrlByProductIdAndUnitId(order3Product.getProductId(), order3Product.getUnitId());
		if(cart.getNumber()<min){
			ToastOrder.makeText(ProductDetailActivity.this, "至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
			return;
		}else if(cart.getNumber()>max){
			ToastOrder.makeText(ProductDetailActivity.this, "最多购买"+PublicUtils.formatDouble(max)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
			return;
		}
		Order3ShoppingCart oCar = order3ShoppingCartDB.findOrder3ShoppingCartByProductId(cart.getProductId(), cart.getUnitId());
		if (oCar !=null) {
			order3ShoppingCartDB.updateOrder3ShoppingCart(cart);
		}else{
			order3ShoppingCartDB.insert(cart);
		}
		ToastOrder.makeText(ProductDetailActivity.this, "添加成功", ToastOrder.LENGTH_SHORT).show();
		
		order3ShoppingCar.initShoppingCartCount();
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
	
	private void initProductInfo(){
		Map<String, String> infoMap = order3Util.productInfo(productId);
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
	            	Order3ProductInfo view = new Order3ProductInfo(ProductDetailActivity.this);
		            view.ininData(k, v);
		            ll_product_detail_info.addView(view.getView());
	            }
	            
	        }  
	      
		}else{
			tv_info.setVisibility(View.GONE);
			sv_product_detail_info.setVisibility(View.GONE);
		}
	}
	
}
