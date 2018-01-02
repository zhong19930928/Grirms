package com.yunhu.yhshxc.activity.carSales.scene.view;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesProductCtrlDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.scene.sellingGoods.CarSaleProductDetailActivity;
import com.yunhu.yhshxc.activity.carSales.scene.sellingGoods.CarSaleShoppingCartActivity;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class ShoppingCarView {
	private View view;
	private Context context;
	private CheckBox cb_shopping_car_item;
	private TextView tv_shopping_car_item_name;
	private TextView tv_kucun;
	private TextView tv_shopping_car_item_sigle_price;
	private EditText tv_shopping_car_item_num;
	private TextView tv_shopping_car_item_price_sum;
	private LinearLayout ll_cuxiao;
	private TextView iv_shopping_car_item_sub;
	private TextView iv_shopping_car_item_add;
	private TextView tv_shopping_car_item_rule;
	private CarSalesUtil util;
	private OnCheckBox onCheckBox;
	private CarSalesProductCtrlDB ctrlDb;
	private CarSalesPromotionDB promotionDB;
	private OnProNumCh onProNumCh;
	int position;
	CarSalesShoppingCartDB carSalesShoppingCartDB;
	CarSalesProductCtrl carSalesProductCtrl;
	CarSalesShoppingCart shoppingCart;
	CarSalesProduct carSalesProduct;
	private Map<String,String> map;
	public ShoppingCarView(Context context,OnCheckBox onCheckBox,OnProNumCh onProNumCh){
		this.context = context;
		view = View.inflate(context, R.layout.order3_shopping_car_item, null);
		this.onCheckBox = onCheckBox;
		this.onProNumCh = onProNumCh;
		view = View.inflate(context, R.layout.order3_shopping_car_item, null);
		cb_shopping_car_item = (CheckBox) view.findViewById(R.id.cb_shopping_car_item);
		tv_shopping_car_item_name = (TextView) view.findViewById(R.id.tv_shopping_car_item_name);
		tv_shopping_car_item_sigle_price = (TextView) view.findViewById(R.id.tv_shopping_car_item_sigle_price);
		iv_shopping_car_item_sub = (TextView) view.findViewById(R.id.iv_shopping_car_item_sub);
		tv_kucun = (TextView) view.findViewById(R.id.tv_kucun);
		tv_shopping_car_item_num = (EditText) view.findViewById(R.id.tv_shopping_car_item_num);
		tv_shopping_car_item_num.addTextChangedListener(watcher);
		iv_shopping_car_item_add = (TextView) view.findViewById(R.id.iv_shopping_car_item_add);
		tv_shopping_car_item_price_sum = (TextView) view.findViewById(R.id.tv_shopping_car_item_price_sum);
		ll_cuxiao = (LinearLayout) view.findViewById(R.id.ll_cuxiao);
		tv_shopping_car_item_rule = (TextView) view.findViewById(R.id.tv_shopping_car_item_rule);
		cb_shopping_car_item.setOnCheckedChangeListener(changeListner);
		tv_shopping_car_item_name.setOnClickListener(listener);
		iv_shopping_car_item_sub.setOnClickListener(listener);
		iv_shopping_car_item_add.setOnClickListener(listener);
		util = new CarSalesUtil(context);
		ctrlDb = new CarSalesProductCtrlDB(context);
		carSalesShoppingCartDB = new CarSalesShoppingCartDB(context);
		promotionDB = new CarSalesPromotionDB(context);
		map = util.getStockCtrl();
		String minString = SharedPreferencesForCarSalesUtil.getInstance(context).getMinNum();
		if(Double.parseDouble(minString)>0){
			min = Double.parseDouble(minString);
		}else{
			min = 1;
		}
	}
	public CheckBox getCB(){
		return cb_shopping_car_item;
	}
	public View getView(){
		return view;
	}
	public void initData(int index,CarSalesShoppingCart data){
		this.position = index;
		this.shoppingCart = data;
		if(data != null ){
			carSalesProductCtrl = ctrlDb.findProductCtrlByProductIdAndUnitId(data.getProductId(),data.getUnitId());
			CarSalesProduct product = util.product(data.getProductId(), data.getUnitId());
			carSalesProduct = carSalesProductCtrl!=null?product:null;
				if(carSalesProduct!=null){
					String maxString = SharedPreferencesForCarSalesUtil.getInstance(context).getMaxNum();
					if(Double.parseDouble(maxString)<=carSalesProduct.getInventory()){
						max = Double.parseDouble(maxString);
					}else{
						max = carSalesProduct.getInventory();
					}
					if(max>0){
						
					}else{
						max = carSalesProduct.getInventory();
					}
					if(data.getPitcOn()==0){
						cb_shopping_car_item.setChecked(false);
					}else{
						cb_shopping_car_item.setChecked(true);
					}
					tv_shopping_car_item_name.setText(carSalesProduct.getName());  //应该根据产品id去数据库查询产品名称
					tv_shopping_car_item_sigle_price.setText(PublicUtils.formatDouble(data.getNowProductPrict()));  //应该根据产品id去数据库查询产品单价
					
					if(!TextUtils.isEmpty(map.get(String.valueOf(carSalesProductCtrl.getId())))){
						double number = Double.parseDouble(map.get(String.valueOf(carSalesProductCtrl.getId())));
						if(number>0){
							tv_kucun.setText("库存："+PublicUtils.formatDouble(number));
						}else{
							tv_kucun.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()));
						}
					}else{
						tv_kucun.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()));
					}
					
					tv_shopping_car_item_num.setText(PublicUtils.formatDouble(data.getNumber()));
					if(carSalesProduct.isPromotion()){//如果有促销，判断条件        
						ll_cuxiao.setVisibility(View.VISIBLE);
						tv_shopping_car_item_rule.setText("促");  //添加促销内容
						if((data.getDisAmount()+data.getDiscountPrice())!=0){
							tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(data.getDisAmount()+data.getDiscountPrice()));
						}else{
							tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(data.getSubtotal()));
						}
					}else{
						ll_cuxiao.setVisibility(View.INVISIBLE);
						tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(data.getSubtotal()));
					}
				}
		}
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.iv_shopping_car_item_sub:   //数量减少
				sub();
				break;
			case R.id.iv_shopping_car_item_add:  //数量增加
				add();
				break;
			case R.id.tv_shopping_car_item_name:
				startToShoppingCarDetail();
			default:
				break;
			}
		}

		
	};
	private void startToShoppingCarDetail() {
		Intent intent = new Intent(context,CarSaleProductDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("index", 0);
		bundle.putInt("productId", shoppingCart.getProductId());
		bundle.putInt("unitId", shoppingCart.getUnitId());
		intent.putExtras(bundle);
		context.startActivity(intent);
		((CarSaleShoppingCartActivity) context).finish();
	}
	
	private void add() {
		int ii = 0;
		String ss = tv_shopping_car_item_num.getText().toString();
    	if(!TextUtils.isEmpty(ss)){
    		if(ss.equals(".")){
    			ss = "0";
			}else if (ss.startsWith(".")) {
				ss = "0"+ss;
			}else if(ss.endsWith(".")){
				ss = ss+"0";
			}
    	}else{
    		ss = "0";
    	}
//		String ss = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
		if(!TextUtils.isEmpty(ss)){
			ii= (int) Double.parseDouble(ss);
		}
		int s = ++ii;
		
		if(max>=min){
			if(s<=max){
				tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
			}else{
				tv_shopping_car_item_num.setText(PublicUtils.formatDouble(max));
				if(carSalesProduct!=null){
					ToastOrder.makeText(context, "最多可购买"+PublicUtils.formatDouble(max)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
				}
			}
		}else{
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
		}
		changeTotalPrice();
		onProNumCh.onProNumChanged();
	}
	public void saveStockNumCtrl(Map<String,String> map,CarSalesUtil util){
		util.saveStockCtrl(map);
	}
//	int max = SharedPreferencesForCarSalesUtil.getInstance(context).getMaxNum();
//	int min = SharedPreferencesForCarSalesUtil.getInstance(context).getMinNum();
	double max;
	double min;
	private void sub() {
		int ii = 0;
		String ss = tv_shopping_car_item_num.getText().toString();
    	if(!TextUtils.isEmpty(ss)){
    		if(ss.equals(".")){
    			ss = "0";
			}else if (ss.startsWith(".")) {
				ss = "0"+ss;
			}else if(ss.endsWith(".")){
				ss = ss+"0";
			}
    	}else{
    		ss = "0";
    	}
//		String ss = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
		if(TextUtils.isEmpty(ss)){
			ii=1;
		}else{
			ii= (int) Double.parseDouble(ss);
		}
		int s = --ii;
		if(s>=min){
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
		}else{
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(min));
			if(carSalesProduct!=null){
				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
			}
		}
		changeTotalPrice();
		onProNumCh.onProNumChanged();
	}
	public interface OnProNumCh{
		public void onProNumChanged();
	}
	double totalPrice;
	private void changeTotalPrice() {
		if(shoppingCart!=null){
			String item_num = tv_shopping_car_item_num.getText().toString();
	    	if(!TextUtils.isEmpty(item_num)){
	    		if(item_num.equals(".")){
	    			item_num = "0";
				}else if (item_num.startsWith(".")) {
					item_num = "0"+item_num;
				}else if(item_num.endsWith(".")){
					item_num = item_num+"0";
				}
	    	}else{
	    		item_num = "0";
	    	}
//			String item_num = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
			String ids = shoppingCart.getPromotionIds();
			if(!TextUtils.isEmpty(ids)&&!ids.equals("0")){
				CarSalesPromotion p = promotionDB.findPromotionByPromotionId(Integer.parseInt(ids));
				util.fenLei(p, carSalesProduct, shoppingCart, Double.parseDouble(item_num));
			}
			if (!TextUtils.isEmpty(item_num)) {
				totalPrice = getTotal(Double.parseDouble(item_num),shoppingCart.getNowProductPrict());
				if (shoppingCart.getDiscountPrice() != 0) {
					tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(shoppingCart.getDiscountPrice()));
				}else if(shoppingCart.getDisAmount()!= 0){
					tv_shopping_car_item_price_sum.setText( PublicUtils.formatDouble(shoppingCart.getDisAmount()));
				}else {
					tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(totalPrice));
					shoppingCart.setDiscountPrice(0);
					shoppingCart.setDisAmount(0);
				}
				shoppingCart.setSubtotal(Double.parseDouble(PublicUtils.formatDouble(totalPrice)));
				shoppingCart.setNumber(Double.parseDouble(item_num));
				carSalesShoppingCartDB.updateCarSalesShoppingCart(shoppingCart);
			}
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
	    	map = util.getStockCtrl();
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
							ToastOrder.makeText(context, "最多可购买"+PublicUtils.formatDouble(max)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
						}
		    			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(max));
		    		}else {
		    			if(carSalesProduct!=null){
		    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			tv_shopping_car_item_num.setText(String.valueOf(min));
		    		}
		    	}else{
		    		if(Double.parseDouble(str)>=min){
		    			
		    		}else{
		    			if(carSalesProduct!=null){
		    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			tv_shopping_car_item_num.setText(String.valueOf(min));
		    		}
		    		
		    	}
	    		map.put(String.valueOf(carSalesProductCtrl.getId()), String.valueOf(carSalesProduct.getInventory()-Double.parseDouble(str)));
	    		if((carSalesProduct.getInventory()-Double.parseDouble(str))>=0){
	    			tv_kucun.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()-Double.parseDouble(str)));
	    		}
	    	}else{
	    		if(carSalesProduct!=null){
    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+carSalesProduct.getUnit(), ToastOrder.LENGTH_LONG).show();
    			}
//    			tv_shopping_car_item_num.setText(String.valueOf(min));
    			map.put(String.valueOf(carSalesProductCtrl.getId()), String.valueOf(carSalesProduct.getInventory()-min));
    			if((carSalesProduct.getInventory()-min)>=0){
	    			tv_kucun.setText("库存："+PublicUtils.formatDouble(carSalesProduct.getInventory()-min));
	    		}
	    	}
	    	
	    	util.saveStockCtrl(map);
	    	changeTotalPrice();
			onProNumCh.onProNumChanged();
	    }
	};
	
	
	public double getTotal(double number,double price ){
		double total = number*price;
		return total;
	}
	
	
	public interface OnCheckBox {
		public void allPrice(int position,boolean isCheck,CarSalesProductCtrl order3ProductCtrl);
	}
	
	private OnCheckedChangeListener changeListner = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				if(shoppingCart!=null){
					shoppingCart.setPitcOn(1);
					carSalesShoppingCartDB.updateCarSalesShoppingCart(shoppingCart);
				}

				if(carSalesProductCtrl!=null){
					onCheckBox.allPrice(position,true,carSalesProductCtrl);
				}

			}else{
				if(shoppingCart!=null){
					shoppingCart.setPitcOn(0);
					carSalesShoppingCartDB.updateCarSalesShoppingCart(shoppingCart);
				}
				if(carSalesProductCtrl!=null){
					onCheckBox.allPrice(position,false,carSalesProductCtrl);
				}
				
			}
			
		}
	};
}
