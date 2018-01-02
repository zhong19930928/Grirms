package com.yunhu.yhshxc.activity.carSales.scene.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProduct;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductCtrl;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesPromotion;
import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesPromotionDB;
import com.yunhu.yhshxc.activity.carSales.db.CarSalesShoppingCartDB;
import com.yunhu.yhshxc.activity.carSales.util.CarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.util.SharedPreferencesForCarSalesUtil;
import com.yunhu.yhshxc.activity.carSales.zhy.tree.bean.Node;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

public class SellingGoodsView {
	private View view;
	private Context context;
	private Node node;
	private TextView tv_products_list_item_name;
	private TextView tv_num_products_list_item;
	private TextView tv_price_products_list_item;
	private TextView sale_products_list_item;
	private Button add_to_shopping_car;
	private ImageView id_treenode_icon;
	private TextView id_treenode_label;
	private LinearLayout ll;
	private CarSalesShoppingCartDB carSalesShoppingCartDB;
	private CarSalesUtil util;
	private CarSalesPromotionDB promotionDB;
	private double min;
	private CarSalesProductCtrl data;
	private OnShoppingCarListner onShoppingCarListner;
	private List<CarSalesPromotion> promotions = new ArrayList<CarSalesPromotion>();
	private Map<String,String> map;
	public SellingGoodsView(Context context,Node node, OnShoppingCarListner onShoppingCarListner){
		this.context = context;
		this.node = node;
		this.onShoppingCarListner = onShoppingCarListner;
		view = View.inflate(context, R.layout.order3_xiadan_item, null);
		tv_products_list_item_name = (TextView) view.findViewById(R.id.tv_products_list_item_name);
		tv_num_products_list_item = (TextView) view.findViewById(R.id.tv_num_products_list_item);
		tv_price_products_list_item = (TextView) view.findViewById(R.id.tv_price_products_list_item);
		sale_products_list_item = (TextView) view.findViewById(R.id.sale_products_list_item);
		add_to_shopping_car = (Button) view.findViewById(R.id.add_to_shopping_car);
		add_to_shopping_car.setOnClickListener(listner);
		id_treenode_icon = (ImageView) view.findViewById(R.id.id_treenode_icon);
		id_treenode_label = (TextView) view.findViewById(R.id.id_treenode_label);
		ll = (LinearLayout) view.findViewById(R.id.ll);
		carSalesShoppingCartDB = new CarSalesShoppingCartDB(context);
		util = new CarSalesUtil(context);
		promotionDB = new CarSalesPromotionDB(context);
		map = util.getStockCtrl();
//		min = SharedPreferencesForCarSalesUtil.getInstance(context).getMinNum();
		String minString = SharedPreferencesForCarSalesUtil.getInstance(context).getMinNum();
		if(Double.parseDouble(minString)>0){
			min = Double.parseDouble(minString);
		}else{
			min = 1;
		}
	}
	public void initData(CarSalesProductCtrl data){
		this.data = data;
		String orgCode = SharedPreferencesForCarSalesUtil.getInstance(context).getCarSalesOrgId();
		int level = SharedPreferencesForCarSalesUtil.getInstance(context).getCarSalesStoreLevel();
		if(node.getIcon() == -1){
			id_treenode_icon.setVisibility(View.GONE);
		}else{
			id_treenode_icon.setVisibility(View.VISIBLE);
			id_treenode_icon.setImageResource(node.getIcon());
		}

		if(node.isLeaf()){  //添加具体的产品信息
			CarSalesProduct product = util.product(data.getProductId(), data.getUnitId());
			if(product !=null){
				promotions = promotionDB.findPromotionByProductIdAndUnitId(product.getProductId(), product.getUnitId(),orgCode,level);
				ll.setVisibility(View.VISIBLE);
				id_treenode_label.setVisibility(View.GONE);
				tv_products_list_item_name.setText(product.getName());
				if(!TextUtils.isEmpty(map.get(String.valueOf(data.getId())))){
					double number = Double.parseDouble(map.get(String.valueOf(data.getId())));
					if(number>0){
						tv_num_products_list_item.setText("库存："+PublicUtils.formatDouble(number));
					}else{
						tv_num_products_list_item.setText("库存："+PublicUtils.formatDouble(product.getInventory()));
					}
					
				}else{
					tv_num_products_list_item.setText("库存："+PublicUtils.formatDouble(product.getInventory()));
					map.put(String.valueOf(data.getId()), String.valueOf(product.getInventory()));
				}
				tv_price_products_list_item.setText("￥"+PublicUtils.formatDouble(product.getPrice()));
				//如果有促销  显示“促”字 
				if (product.isPromotion()) {
					sale_products_list_item.setVisibility(View.VISIBLE);
				} else {
					sale_products_list_item.setVisibility(View.INVISIBLE);
				}
				
			}else{
				ll.setVisibility(View.GONE);
//				id_treenode_icon.setVisibility(View.INVISIBLE);
				id_treenode_label.setVisibility(View.VISIBLE);
	     		id_treenode_label.setText(data.getLabel());
			}
		}else{  //添加产品类别
			ll.setVisibility(View.GONE);
			id_treenode_label.setVisibility(View.VISIBLE);
     		id_treenode_label.setText(data.getLabel());
		}
		
	}
	public View getView(){
		return view;
	}
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.add_to_shopping_car:
				addToShoppingCar();
				break;

			default:
				break;
			}
		}
	};
	private void addToShoppingCar() {
		CarSalesProduct product = util.product(node.getProductCtrl().getProductId(), node.getProductCtrl().getUnitId());
		if (product!=null) {
			CarSalesShoppingCart cart = carSalesShoppingCartDB.findCarSalesShoppingCartByProductId(product.getProductId(),product.getUnitId());
			if (cart==null) {
				cart = new CarSalesShoppingCart();
				cart.setProductId(product.getProductId());
				cart.setUnitId(product.getUnitId());
				cart.setNumber(min);
				cart.setSubtotal(min*product.getPrice());
				cart.setPitcOn(1);
				cart.setpId(node.getpId());
				if(product.isPromotion()){
					if(promotions!=null&&promotions.size()>0){
						cart.setPromotionIds(String.valueOf(promotions.get(0).getPromotionId()));
						util.fenLei(promotions.get(0), product, cart, min);
					}else{
						cart.setPromotionIds(null);
					}
				}else{
					cart.setPromotionIds(null);
				}
				cart.setNowProductPrict(product.getPrice());
				carSalesShoppingCartDB.insert(cart);
				onShoppingCarListner.onFindAllList(data,tv_num_products_list_item,product.getInventory()-cart.getNumber());
			}
			ToastOrder.makeText(context, "添加成功", ToastOrder.LENGTH_SHORT).show();
		}
	}
	public interface OnShoppingCarListner {
		public void onFindAllList(CarSalesProductCtrl data,TextView tv,double number);
	}
	
}
