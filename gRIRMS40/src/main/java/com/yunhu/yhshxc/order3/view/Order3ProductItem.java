package com.yunhu.yhshxc.order3.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yunhu.yhshxc.order3.zhy.tree.bean.Node;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;


public class Order3ProductItem {
	private View view;
	private TextView tv_products_list_item_name;
	private TextView tv_num_products_list_item;
	private TextView tv_price_products_list_item;
	private TextView sale_products_list_item;
	private Button add_to_shopping_car;
	private ImageView id_treenode_icon;
	private TextView id_treenode_label;
	private LinearLayout ll;
	private Context context;
	private Node node;
	private Order3ShoppingCartDB order3ShoppingCartDB;
	private Order3ProductCtrl productCtrl = null;
	private Order3Util order3Util;
	private OnShoppingCarListner onShoppingCarListner;
	public Order3ProductItem(Context context,Node node,OnShoppingCarListner onShoppingCarListner){

		this.context = context;
		this.onShoppingCarListner = onShoppingCarListner;
		this.node = node;
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
		order3ShoppingCartDB = new Order3ShoppingCartDB(context);
		promotionDB = new Order3PromotionDB(context);
		String minstring = SharedPreferencesForOrder3Util.getInstance(context).getMinNum();
		min = Double.parseDouble(minstring);
		if(min>0){
		}else{
			min = 1;
		}
		order3Util = new Order3Util(context);
	}
	public View getView(){
		return view;
	}
	private Order3Product product = null;

	public void initData(Order3ProductCtrl data){//集合的每个对象里包括 （id   pid  产品类别  和产品对象）
		String orgCode = SharedPreferencesForOrder3Util.getInstance(context).getOrder3OrgId();
		int level = SharedPreferencesForOrder3Util.getInstance(context).getOrder3StoreLevel();
		
		if(node.getIcon() == -1){
			id_treenode_icon.setVisibility(View.GONE);
		}else{
			id_treenode_icon.setVisibility(View.VISIBLE);
			id_treenode_icon.setImageResource(node.getIcon());
		}

		this.productCtrl = data;

		if(node.isLeaf()){  //添加具体的产品信息
			
//			Order3Product product = data.getProduct();
			if (data.isProductLevel()) {
				product = new Order3Util(context).product(data.getProductId(), data.getUnitId());
			}
			if(product !=null){
				promotions = promotionDB.findPromotionByProductIdAndUnitId(product.getProductId(), product.getUnitId(),orgCode,level);
				ll.setVisibility(View.VISIBLE);
				id_treenode_label.setVisibility(View.GONE);
				tv_products_list_item_name.setText(product.getName());
				tv_num_products_list_item.setText("库存：-");
				tv_price_products_list_item.setText("￥"+PublicUtils.formatDouble(product.getPrice()));
				//如果有促销  显示“促”字 
				if (product.isPromotion()) {
					sale_products_list_item.setVisibility(View.VISIBLE);
				} else {
					sale_products_list_item.setVisibility(View.INVISIBLE);
				}
				
			}else{
				ll.setVisibility(View.GONE);
				id_treenode_label.setVisibility(View.VISIBLE);
	     		id_treenode_label.setText(data.getLabel());
			}
		}else{  //添加产品类别
			ll.setVisibility(View.GONE);
			id_treenode_label.setVisibility(View.VISIBLE);
     		id_treenode_label.setText(data.getLabel());
		}
		
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
	double min = 0;
	Order3PromotionDB promotionDB;
	List<Order3Promotion> promotions = new ArrayList<Order3Promotion>();
	private void addToShoppingCar() {
//		Order3Product product = node.getProduct().getProduct();
		if (product!=null) {
			Order3ShoppingCart cart = order3ShoppingCartDB.findOrder3ShoppingCartByProductId(product.getProductId(),product.getUnitId());
			if (cart==null) {
				cart = new Order3ShoppingCart();
				cart.setProductId(product.getProductId());
				cart.setUnitId(product.getUnitId());
				cart.setNumber(min);
				cart.setSubtotal(min*product.getPrice());
				cart.setPitcOn(1);
				String pid = !TextUtils.isEmpty(node.getpId())?node.getpId():"";
				cart.setpId(pid);
				if(promotions!=null&&promotions.size()>0){
					cart.setPromotionIds(String.valueOf(promotions.get(0).getPromotionId()));
					proFenLeiInit(promotions.get(0),product,cart);
				}else{
					cart.setPromotionIds(null);
				}
				cart.setNowProductPrict(product.getPrice());
				order3ShoppingCartDB.insert(cart);
			}
			onShoppingCarListner.onFindAllList();
			ToastOrder.makeText(context, "添加成功", ToastOrder.LENGTH_SHORT).show();
		}
	}
	
	public interface OnShoppingCarListner {
		public void onFindAllList();
	}
	private void proFenLeiInit(Order3Promotion order3Promotion,Order3Product order3Product,Order3ShoppingCart cart) {
//		initProFenLei();
		if (min!=0) {
			double sinFullPrice = min * order3Product.getPrice();
			switch (order3Promotion.getPreType()) {
			case 1:// 单品买满数量
				if (min >= order3Promotion.getmCnt()) {
					int a = (int) (min/order3Promotion.getmCnt());
					switch (order3Promotion.getDisType()) {
					case 3://打折  
						double singleFullNumDis = discount(min,order3Promotion,order3Product.getPrice(),cart);
						cart.setDiscountPrice(singleFullNumDis);
						break;
					case 1://送赠品
						Order3Product singleFullNumProduct = order3Util.product(Integer.parseInt(order3Promotion.getsId()),Integer.parseInt(order3Promotion.getsUid()));//根据mTab,mId,mUid去查赠品
						double singleFullNumZeng = zengpin(a,order3Promotion);
						cart.setDisNumber(singleFullNumZeng);
						cart.setPro(singleFullNumProduct);
						cart.setGiftId(singleFullNumProduct.getProductId());
						cart.setGiftUnitId(singleFullNumProduct.getUnitId());
						break;
					case 2://减免金额
						double singleFullNumPri = subMoney(a,order3Promotion,min,order3Product.getPrice(),cart);
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
						double singleFullPriceDis = discount(min,order3Promotion,order3Product.getPrice(),cart);
						cart.setDiscountPrice(singleFullPriceDis);
						break;
					case 1://送赠品
						Order3Product singleFullPriceProduct = order3Util.product(Integer.parseInt(order3Promotion.getsId()),Integer.parseInt(order3Promotion.getsUid()));//根据mTab,mId,mUid去查赠品
						double singleFullPriceZeng = zengpin(b,order3Promotion);
						cart.setDisNumber(singleFullPriceZeng);
						cart.setPro(singleFullPriceProduct);
						cart.setGiftId(singleFullPriceProduct.getProductId());
						cart.setGiftUnitId(singleFullPriceProduct.getUnitId());
						break;
					case 2://减免金额
						double singleFullPricePri = subMoney(b,order3Promotion,min,order3Product.getPrice(),cart);
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
	private double subMoney(int bei,Order3Promotion o,double number,double price,Order3ShoppingCart cart) {
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

	private double discount(double number,Order3Promotion order3Promotion,double price,Order3ShoppingCart cart) {
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
}
