package com.yunhu.yhshxc.order3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order3.bo.Order3Product;
import com.yunhu.yhshxc.order3.bo.Order3ProductCtrl;
import com.yunhu.yhshxc.order3.bo.Order3Promotion;
import com.yunhu.yhshxc.order3.bo.Order3ShoppingCart;
import com.yunhu.yhshxc.order3.db.Order3PromotionDB;
import com.yunhu.yhshxc.order3.db.Order3ShoppingCartDB;
import com.yunhu.yhshxc.order3.util.Order3Util;
import com.yunhu.yhshxc.order3.util.SharedPreferencesForOrder3Util;
import com.yunhu.yhshxc.order3.view.Order3ShoppingCarItem;
import com.yunhu.yhshxc.order3.view.Order3ShoppingCarItem.OnCheckBox;
import com.yunhu.yhshxc.order3.view.Order3ShoppingCarItem.OnProNumCh;
import com.yunhu.yhshxc.utility.PublicUtils;

public class ShoppingCarFragment extends Fragment implements OnCheckBox,OnProNumCh{
	private ListView lv_shopping_car;
	private TextView tv_shopping_car_allmoney;
	private ShoppingCarAdapter adapter;
	private List<Order3ShoppingCart> order3Shoppingcars ;

	private Order3ShoppingCartDB order3ShoppingCartDB;
	private Order3PromotionDB promotionDB;
	Order3Product order3Product;
	Order3Util order3Util;
	//购物车中所有选中享受总和优惠的产品
	private List<Order3ShoppingCart> cartsCheckedIsDouble = new ArrayList<Order3ShoppingCart>();
	//购物车中所有选中不享受总和优惠的产品
	private List<Order3ShoppingCart> cartsCheckedNoDouble = new ArrayList<Order3ShoppingCart>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order3_shopping_car,null);
		initView(v);
		return v;
	}
	
	private void initView(View v) {
		initdb();
		lv_shopping_car = (ListView) v.findViewById(R.id.lv_shopping_car);
		tv_shopping_car_allmoney = (TextView) v.findViewById(R.id.tv_shopping_car_allmoney);
		adapter = new ShoppingCarAdapter(getActivity(),order3Shoppingcars);
		lv_shopping_car.setAdapter(adapter);
		initTotalPrice();
	}

	public void initdb(){
		order3ShoppingCartDB = new Order3ShoppingCartDB(getActivity());
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		order3Util = new Order3Util(getActivity());
		promotionDB = new Order3PromotionDB(getActivity());
	}
	
	/**
	 * 总和买满数量减免金额
	 * @return
	 */
	private double zongHeFullNum(){
	
		double finalPrice = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3OrgId(), 5,2,level);
		if(pos5.size()>0){
			for(int i = 0; i<pos5.size(); i++){
				if(allNum()>=pos5.get(i).getmCnt()){
					int b = (int) (allNum()/pos5.get(i).getmCnt());
					double a = preAmount(pos5.get(i),b);
					if(finalPrice<a){
						finalPrice =a;
					}
				}
			
			}
		}
		
		return finalPrice;

		
	}
	/**
	 * 总和买满数量减免打折
	 * @return
	 */
	private double zongHeFullNumDis(){
		double finalPrice = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3StoreLevel();
		List<Order3Promotion> pos5 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3OrgId(), 5,3,level);
		if(pos5.size()>0){
			for(int i = 0; i<pos5.size(); i++){
				if(allNum()>=pos5.get(i).getmCnt()){
					double a = prePrice(pos5.get(i));
					if(finalPrice<a){
						finalPrice = a;
					}
				}
				
			}
		}
		return finalPrice;
	}

	/**
	 * 总和买满金额减免金额
	 * @return
	 */
	private double zongHeFullPrice(){
		double finalPrice = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3StoreLevel();
		List<Order3Promotion> pos6 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3OrgId(), 6,2,level);
		if(pos6.size()>0){
			for(int i = 0; i<pos6.size();i++){
				if(allMoney()>=pos6.get(i).getAmount()){
					int b = (int) (allMoney()/pos6.get(i).getAmount());
					double a = preAmount(pos6.get(i),b);
					if(finalPrice<a){
						finalPrice =a;
					}
				}
			}
		}
		return finalPrice;
		
	}
	/**
	 * 总和买满金额减免打折
	 * @return
	 */
	private double zongHeFullPriceDis(){
		double finalPrice = 0;
		int level = SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3StoreLevel();
		List<Order3Promotion> pos6 = promotionDB.findPromotionByPreType(SharedPreferencesForOrder3Util.getInstance(getActivity()).getOrder3OrgId(), 6,3,level);
		if(pos6.size()>0){
			for(int i = 0; i<pos6.size();i++){
				if(allMoney()>=pos6.get(i).getAmount()){
					double a = prePrice(pos6.get(i));
					if(finalPrice<a){
						finalPrice = a;
					}
				}
			}
		}
		return finalPrice;
		
	}
	public void initTotalPrice(){
		order3Shoppingcars = order3ShoppingCartDB.findAllList();
		if(SharedPreferencesForOrder3Util.getInstance(getActivity()).getIsPromotion()==1){
			 allNoDoubleMoeny = allMoney()+allMoneyNoDouble();
			tv_shopping_car_allmoney.setText("共计: ￥ "+PublicUtils.formatDouble(allNoDoubleMoeny)+" 元");
		}else{
			double allM = 0;
			double fenLeiFND3 = zongHeFullNum();// 买满数量打折后价钱 买满数量减免金额
			double fenLeiFND4 = zongHeFullNumDis();// 买满数量打折后价钱 买满数量减免打折
			double fenLeiFND5 = zongHeFullPrice();// 买满数量打折后价钱 买满金额减免金额
			double fenLeiFND6 = zongHeFullPriceDis();// 买满数量打折后价钱 买满金额减免打折
			double[] a = { fenLeiFND3, fenLeiFND4, fenLeiFND5, fenLeiFND6 };
//			JLog.d("aaa", fenLeiFND3+"   "+fenLeiFND4+"  "+fenLeiFND5+"  "+fenLeiFND6);
			double s = 0;
			for(int i = 0; i<4;i++){
				allM = Math.max(allM, a[i]);
			}
			if(allM!=0){
				allNoDoubleMoeny =allMoney()+ allMoneyNoDouble()-allM;
				tv_shopping_car_allmoney.setText("共计: ￥ "+PublicUtils.formatDouble(allNoDoubleMoeny)+" 元");
			}else{
				 allNoDoubleMoeny = allMoney()+allMoneyNoDouble();
				tv_shopping_car_allmoney.setText("共计: ￥ "+PublicUtils.formatDouble(allNoDoubleMoeny)+" 元");
			}
		}

	}
	
	double allNoDoubleMoeny;
	public double getActulAmo(){
		initTotalPrice();
		return allNoDoubleMoeny;
	}
	public double getTotalPrice(){
		return allMoney()+allMoneyNoDouble();
	}
	/**
	 * 购物车中所有享受多重优惠产品的总数量
	 */
	public double allNum(){
		double all = 0;
		for(int i = 0; i<cartsCheckedIsDouble.size();i++){
			all+=cartsCheckedIsDouble.get(i).getNumber();
		}
		return all;
	}
	/**
	 * 购物车中所有不享受多重优惠产品的总数量
	 */
	public double allNumNoDouble(){
		double all = 0;
		for (int i = 0; i < cartsCheckedNoDouble.size(); i++) {
			all+=cartsCheckedNoDouble.get(i).getNumber();
		}
		return all;
	}
	
	/**
	 * 初始化优惠数据
	 */
	public void allChecked(){
		cartsCheckedIsDouble.clear();
		cartsCheckedNoDouble.clear();
		for(int i = 0; i<order3Shoppingcars.size();i++){
			Order3ShoppingCart cart = order3Shoppingcars.get(i);
			if(cart.getPitcOn() == 1){//选中
				String ids = cart.getPromotionIds();
				if(!TextUtils.isEmpty(ids)&&!ids.equals("0")){
					Order3Promotion o = promotionDB.findPromotionByPromotionId(Integer.parseInt(ids));
					if(o!=null){
						if (o.getIsDouble()==1) {//享受多重优惠的
							cartsCheckedIsDouble.add(cart);
						}else{
							cartsCheckedNoDouble.add(cart);
						}
					}else{
//						cartsCheckedNoDouble.add(cart);
						cartsCheckedIsDouble.add(cart);
					}	
				}else{
					cartsCheckedIsDouble.add(cart);
				}
			}
			
		}
	}
	
	/**
	 * 购物车中所有享受多重优惠产品的总价格
	 * @return
	 */
	public double allMoney() {
		allChecked();
		double money = 0;
		for (int i = 0; i < cartsCheckedIsDouble.size(); i++) {
			Order3ShoppingCart cart = cartsCheckedIsDouble.get(i);
			if((cart.getDisAmount() + cart.getDiscountPrice())!=0){
				money += cart.getDisAmount()+cart.getDiscountPrice();
			}else{
				money += cart.getSubtotal();
			}
		}
		return money;
	}
	/**
	 * 购物车中所有不享受多重优惠产品的总价格
	 * @return
	 */
	private double allMoneyNoDouble() {
		double money = 0;
		for (int i = 0; i < cartsCheckedNoDouble.size(); i++) {
			Order3ShoppingCart cart = cartsCheckedNoDouble.get(i);
			if((cart.getDisAmount() + cart.getDiscountPrice())!=0){
				money += cart.getDisAmount()+cart.getDiscountPrice();
			}else{
				money += cart.getSubtotal();
			}
		}
		return money;
	}

	public double zengpin(Order3Promotion pro,int bei) {
		double zengpinNum = pro.getsCnt()*bei;// 赠送产品数；
		return zengpinNum;
	}
	
	public double discountZonghe(Order3Promotion pro){
		double disRate = changeDouble(Integer.parseInt(pro.getDisRate()!=null?pro.getDisRate():"0"));// 折扣率
		double prePrice = allMoney();
		double nowPrice = prePrice * disRate;
		return nowPrice;
	}
	public double prePrice(Order3Promotion pro){  //打折后减免金额
		double disRate = changeDouble(Integer.parseInt(pro.getDisRate()!=null?pro.getDisRate():"0"));// 折扣率
		double prePrice = allMoney();
		double nowPrice = prePrice * disRate;
		double p = prePrice-nowPrice;
		return p;
	}
	public double subMoneyZonHe(Order3Promotion pro){
		double disAmount = pro.getDisAmount();// 返还现金金额
		double prePriceJ = allMoney();
		double nowPriceJ = prePriceJ - disAmount;
//		JLog.d("aaa", "prePriceJ  "+prePriceJ+"   disAmount  "+disAmount);
		return nowPriceJ;
	}

	public double preAmount(Order3Promotion pro,int bei) {
		double disAmount = pro.getDisAmount()*bei;// 返还现金金额
		return disAmount;
	}
	public double changeDouble(int s){
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
	class ShoppingCarAdapter extends BaseAdapter{
		private List<Order3ShoppingCart> order3Shopping;
		public ShoppingCarAdapter(Context context,List<Order3ShoppingCart> order3Shopping){
			this.order3Shopping = order3Shopping;
		}
		
		@Override
		public int getCount() {
			if(order3Shopping != null){
				return order3Shopping.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return order3Shopping.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Order3ShoppingCarItem view = null;
			if(convertView == null){
				view = new Order3ShoppingCarItem(getActivity(),ShoppingCarFragment.this,ShoppingCarFragment.this);
				convertView = view.getView();
				convertView.setTag(view);
			}else{
				view = (Order3ShoppingCarItem) convertView.getTag();
			}
			Order3ShoppingCart data  = order3Shopping.get(position);
			view.initData(position,data);
			return convertView;
		}
		
	}

	List<Order3ProductCtrl> order3ProductsList = new ArrayList<Order3ProductCtrl>();
	@Override
	public void allPrice(int position,boolean isCheck,Order3ProductCtrl order3ProductCtrl) {
		
		if(isCheck){
			order3ProductsList.add(order3ProductCtrl);
		}else{
			order3ProductsList.remove(order3ProductCtrl);
		}
		initTotalPrice();
	}
	public List<Order3ProductCtrl> getOrder3ProductsList(){
		return order3ProductsList;
	}

	@Override
	public void onProNumChanged() {
		initTotalPrice();
	}
	
}
