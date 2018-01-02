package com.yunhu.yhshxc.order3.view;

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
import com.yunhu.yhshxc.order3.ProductDetailActivity;
import com.yunhu.yhshxc.order3.ShoppingCarActivity;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.db.Order3ProductCtrlDB;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class Order3ShoppingCarItem {
	private View view;
	private Context context;
	private CheckBox cb_shopping_car_item;
	private TextView tv_shopping_car_item_name;
	private TextView tv_shopping_car_item_sigle_price;
//	private TextView tv_shopping_car_item_sub;
	private EditText tv_shopping_car_item_num;
//	private TextView tv_shopping_car_item_add;
	private TextView tv_shopping_car_item_price_sum;
	private LinearLayout ll_cuxiao;
	private TextView iv_shopping_car_item_sub;
	private TextView iv_shopping_car_item_add;
	private TextView tv_shopping_car_item_rule;
	private Order3Util order3Util;
	private OnCheckBox onCheckBox;
	private Order3ProductCtrlDB ctrlDb;
	private Order3PromotionDB promotionDB;
	private OnProNumCh onProNumCh;
	public Order3ShoppingCarItem(Context context,OnCheckBox onCheckBox,OnProNumCh onProNumCh){
		this.context = context;
		this.onCheckBox = onCheckBox;
		this.onProNumCh = onProNumCh;
		view = View.inflate(context, R.layout.order3_shopping_car_item, null);
		cb_shopping_car_item = (CheckBox) view.findViewById(R.id.cb_shopping_car_item);
		tv_shopping_car_item_name = (TextView) view.findViewById(R.id.tv_shopping_car_item_name);
		tv_shopping_car_item_sigle_price = (TextView) view.findViewById(R.id.tv_shopping_car_item_sigle_price);
		iv_shopping_car_item_sub = (TextView) view.findViewById(R.id.iv_shopping_car_item_sub);
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
		order3Util = new Order3Util(context);
		ctrlDb = new Order3ProductCtrlDB(context);
		order3ShoppingCartDB = new Order3ShoppingCartDB(context);
		promotionDB = new Order3PromotionDB(context);
		if(min>0){
			
		}else{
			min = 1;
		}
		if(max <=0){
			max = Double.MAX_VALUE;
		}
	}
	public CheckBox getCB(){
		return cb_shopping_car_item;
	}
	public View getView(){
		return view;
	}
	Order3ShoppingCartDB order3ShoppingCartDB;
	Order3ProductCtrl order3ProductCtrl;
	Order3ShoppingCart shoppingCart;
	Order3Product order3Product;
	int position;
	public void initData(int index,Order3ShoppingCart data){
		this.position = index;
		this.shoppingCart = data;
		if(data != null ){
			order3ProductCtrl = ctrlDb.findProductCtrlByProductIdAndUnitId(data.getProductId(),data.getUnitId());
//			order3Product = order3ProductCtrl!=null?order3ProductCtrl.getProduct():null;
				if (order3ProductCtrl !=null && order3ProductCtrl.isProductLevel()) {
					order3Product = new Order3Util(context).product(order3ProductCtrl.getProductId(), order3ProductCtrl.getUnitId());
				}
				if(order3Product!=null){
					if(data.getPitcOn()==0){
						cb_shopping_car_item.setChecked(false);
					}else{
						cb_shopping_car_item.setChecked(true);
					}
					tv_shopping_car_item_name.setText(order3Product.getName());  //应该根据产品id去数据库查询产品名称
					tv_shopping_car_item_sigle_price.setText(PublicUtils.formatDouble(data.getNowProductPrict()));  //应该根据产品id去数据库查询产品单价
					
					tv_shopping_car_item_num.setText(PublicUtils.formatDouble(data.getNumber()));
					if(order3Product.isPromotion()){//如果有促销，判断条件        
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
		Intent intent = new Intent(context,ProductDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("index", 0);
		bundle.putInt("productId", shoppingCart.getProductId());
		bundle.putInt("unitId", shoppingCart.getUnitId());
		intent.putExtras(bundle);
		context.startActivity(intent);
		((ShoppingCarActivity) context).finish();
	}
	
	private void add() {
		int ii = 0;
		String ss = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
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
				tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
			}else{
				tv_shopping_car_item_num.setText(PublicUtils.formatDouble(max));
				if(order3Product!=null){
					ToastOrder.makeText(context, "最多可购买"+PublicUtils.formatDouble(max)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
				}
			}
		}else{
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
		}
		
		changeTotalPrice();
		onProNumCh.onProNumChanged();
	}
	double max = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(context).getMaxNum());
	double min = Double.parseDouble(SharedPreferencesForOrder3Util.getInstance(context).getMinNum());
	
	private void sub() {
		int ii = 0;
		String ss = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
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
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(s));
		}else{
			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(min));
			if(order3Product!=null){
				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
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
			String ids = shoppingCart.getPromotionIds();
			if(!TextUtils.isEmpty(ids)&&!ids.equals("0")){
				Order3Promotion p = promotionDB.findPromotionByPromotionId(Integer.parseInt(ids));
				proFenLei(p);
			}
			String s = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
			if(s.equals(".")){
				s = "0";
			}else if (s.startsWith(".")) {
				s = "0"+s;
			}else if(s.endsWith(".")){
				s = s+"0";
			}
			if (!TextUtils.isEmpty(s)) {
				totalPrice = getTotal(Double.parseDouble(s),shoppingCart.getNowProductPrict());
				if (singleFullNumDis != 0) {
					tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(singleFullNumDis));
					shoppingCart.setDiscountPrice(singleFullNumDis);
				}else if(singleFullNumPri!= 0){
					tv_shopping_car_item_price_sum.setText( PublicUtils.formatDouble(singleFullNumPri));
					shoppingCart.setDisAmount(singleFullNumPri);
				}else if(singleFullPriceDis!=0){
					tv_shopping_car_item_price_sum.setText( PublicUtils.formatDouble(singleFullPriceDis));
					shoppingCart.setDiscountPrice(singleFullPriceDis);
				}else if(singleFullPricePri!=0){
					tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(singleFullPricePri));
					shoppingCart.setDisAmount(singleFullPricePri);
				}else {
					tv_shopping_car_item_price_sum.setText(PublicUtils.formatDouble(totalPrice));
					shoppingCart.setDiscountPrice(0);
					shoppingCart.setDisAmount(0);
				}
				shoppingCart.setSubtotal(Double.parseDouble(PublicUtils.formatDouble(totalPrice)));
				shoppingCart.setNumber(Double.parseDouble(s));
				order3ShoppingCartDB.updateOrder3ShoppingCart(shoppingCart);
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
							ToastOrder.makeText(context, "最多可购买"+PublicUtils.formatDouble(max)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
						}
		    			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(max));
		    		}else {
		    			if(order3Product!=null){
		    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
//		    			tv_shopping_car_item_num.setText(String.valueOf(min));
		    		}
		    	}else{
		    		if(Double.parseDouble(str)>=min){
		    			
		    		}else{
		    			if(order3Product!=null){
		    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
		    			}
		    			tv_shopping_car_item_num.setText(PublicUtils.formatDouble(min));
		    		}
		    	}
	    		
	    	}else{
	    		if(order3Product!=null){
    				ToastOrder.makeText(context,"至少购买"+PublicUtils.formatDouble(min)+order3Product.getUnit(), ToastOrder.LENGTH_LONG).show();
    			}
//    			tv_shopping_car_item_num.setText(String.valueOf(min));
	    	}
	    	changeTotalPrice();
			onProNumCh.onProNumChanged();
	    	
	    }

		
	};
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
	Order3Promotion pro;
	/**
	 * 促销分类
	 */
	private void proFenLei(Order3Promotion pros) {
		
			this.pro = pros;
			String s  = TextUtils.isEmpty(tv_shopping_car_item_num.getText().toString())?"0":tv_shopping_car_item_num.getText().toString();
			if(s.equals(".")){
				s = "0";
			}else if (s.startsWith(".")) {
				s = "0"+s;
			}else if(s.endsWith(".")){
				s = s+"0";
			}
			if(!TextUtils.isEmpty(s)){
				singleFullNum = Double.parseDouble(s);
				singleFullPrice = singleFullNum*shoppingCart.getNowProductPrict();
				singleFullNumDis = 0;
				singleFullNumZeng = 0;
				singleFullNumProduct = null;
				singleFullPriceDis = 0;
				singleFullPriceProduct = null;
				singleFullPricePri = 0;
		        switch (pro.getPreType()) {
				case 1:// 单品买满数量     
					if(singleFullNum>=pro.getmCnt()){
						
						proFenLei1(pro.getPreType(),pro.getDisType());
					}
					
					break;
				case 2://单品买满金额
					if(singleFullPrice>=pro.getAmount()){
						proFenLei2(pro.getPreType(),pro.getDisType());
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
	private void proFenLei1(int preType,int disType) {
		int a = (int) (singleFullNum/pro.getmCnt());
			switch (disType) {
			case 3://打折  
				singleFullNumDis = discount();
				shoppingCart.setDiscountPrice(singleFullNumDis);
				break;
			case 1://送赠品
				singleFullNumProduct = order3Util.product(Integer.parseInt(pro.getsId()),Integer.parseInt(pro.getsUid()));//根据mTab,mId,mUid去查赠品
				singleFullNumZeng = zengpin(a);
				shoppingCart.setDisNumber(singleFullNumZeng);
				shoppingCart.setPro(singleFullNumProduct);
				shoppingCart.setGiftId(Integer.parseInt(pro.getsId()));
				shoppingCart.setGiftUnitId(Integer.parseInt(pro.getsUid()));
				break;
			case 2://减免金额
				singleFullNumPri = subMoney(a);
				shoppingCart.setDisAmount(singleFullNumPri);
				break;
			default:
				break;
			}
		
	}
	/**
	 * 单品买满金额
	 * @param preType
	 * @param disType
	 */
	private void proFenLei2(int preType,int disType) {
		int a = (int) (singleFullPrice/pro.getAmount());
			switch (disType) {
			case 3://打折  
				singleFullPriceDis = discount();
				shoppingCart.setDiscountPrice(singleFullPriceDis);
				break;
			case 1://送赠品
				singleFullPriceProduct = order3Util.product(Integer.parseInt(pro.getsId()),Integer.parseInt(pro.getsUid()));//根据mTab,mId,mUid去查赠品
				singleFullPriceZeng = zengpin(a);
				shoppingCart.setDisNumber(singleFullPriceZeng);
				shoppingCart.setPro(singleFullPriceProduct);
				shoppingCart.setGiftId(singleFullPriceProduct.getProductId());
				shoppingCart.setGiftUnitId(singleFullPriceProduct.getUnitId());
				break;
			case 2://减免金额
				singleFullPricePri = subMoney(a);
				shoppingCart.setDisAmount(singleFullPricePri);
				break;

			default:
				break;
			}

	}
	private double subMoney(int bei) {
		double disAmount = pro.getDisAmount()*bei;//返还现金金额
		double prePriceJ = singleFullNum*shoppingCart.getNowProductPrict();
		double nowPriceJ = prePriceJ - disAmount;
		shoppingCart.setPreAmount(disAmount);
		return nowPriceJ;

		
	}
	private double zengpin(int bei) {
		
		double zengpinNum = pro.getsCnt()*bei;//赠送产品数；
		return zengpinNum;

	}

	private double discount() {
		double disRate =  changeDouble(Integer.parseInt(pro.getDisRate()));//折扣率
   		double prePrice = singleFullNum*shoppingCart.getNowProductPrict();
   		double nowPrice = prePrice*disRate;
   		double p = prePrice-nowPrice;
   		shoppingCart.setPrePrice(Double.parseDouble(PublicUtils.formatDouble(p)));

   		return nowPrice;

	}
	public double getTotal(double number,double price ){
		double total = number*price;
		return total;
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
	
	public interface OnCheckBox {
		public void allPrice(int position,boolean isCheck,Order3ProductCtrl order3ProductCtrl);
	}
	
	private OnCheckedChangeListener changeListner = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			String s = tv_shopping_car_item_price_sum.getText().toString().substring(2).trim();
			if(isChecked){
				if(shoppingCart!=null){
					shoppingCart.setPitcOn(1);
					order3ShoppingCartDB.updateOrder3ShoppingCart(shoppingCart);
				}

				if(order3ProductCtrl!=null){
					onCheckBox.allPrice(position,true,order3ProductCtrl);
				}

			}else{
				if(shoppingCart!=null){
					shoppingCart.setPitcOn(0);
					order3ShoppingCartDB.updateOrder3ShoppingCart(shoppingCart);
				}
				if(order3ProductCtrl!=null){
					onCheckBox.allPrice(position,false,order3ProductCtrl);
				}
				
			}
			
		}
	};
}
