package com.yunhu.yhshxc.order.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order.CreateOrderDetailActivity;
import com.yunhu.yhshxc.order.OrderCalculate;
import com.yunhu.yhshxc.order.bo.OrderCache;

public class OrderProductItem {
	private View view=null;//当前产品view
	private TextView tv_order_product_name;//产品名称
	private TextView tv_order_product_number;//订货数量
	private TextView tv_order_product_price;//产品单价
	private TextView tv_order_product_totalPrice;//产品总价
	private String storeId=null;//店面ID
	private String storeName=null;//店面名称
	private Context context;
	private OrderCache orderCache=null;
	public OrderProductItem(Context mContext,OrderCache orderCache) {
		this.context=mContext;
		this.orderCache=orderCache;
		view=View.inflate(mContext, R.layout.order_product_item, null);
		tv_order_product_name=(TextView)view.findViewById(R.id.tv_order_product_name);
		tv_order_product_number=(TextView)view.findViewById(R.id.tv_order_product_number);
		tv_order_product_price=(TextView)view.findViewById(R.id.tv_order_product_price);
		tv_order_product_totalPrice=(TextView)view.findViewById(R.id.tv_order_product_totalPrice);
		view.setTag(orderCache);
		setData(orderCache);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intentToDetail();
			}
		});
		
	}
	
	/**
	 * 设置店面ID
	 * @param storeId
	 */
	public void setStoreInfo(String storeId,String storeName){
		this.storeId=storeId;
		this.storeName=storeName;
	}
	
	/**
	 * 跳转到详细页面
	 */
	private void intentToDetail(){
		Intent intent=new Intent(context, CreateOrderDetailActivity.class);
		Bundle bundle=new Bundle();
		bundle.putBoolean("isCreateOrder", false);//不是创建订单
		bundle.putString("productId", orderCache.getOrderProductId()+"");//产品ID
		bundle.putString("storeId", storeId);//店面ID
		bundle.putString("storeName", storeName);//店面 名称
		bundle.putString("id", orderCache.getId()+"");//产品的主键ID
		intent.putExtra("bundle", bundle);
		context.startActivity(intent);
	}
	
	/**
	 * 设置数据
	 * @param orderCache 数据源
	 */
	private void setData(OrderCache orderCache){
		if(orderCache!=null){
			tv_order_product_name.setText(orderCache.getOrderProductName());//产品名称
			tv_order_product_number.setText(orderCache.getOrderQuantity()+"");//订货数量
			tv_order_product_price.setText("单价："+orderCache.getUnitPrice());//单价
			String totalPrice=OrderCalculate.computeMultiply(String.valueOf(orderCache.getOrderQuantity()), String.valueOf(orderCache.getUnitPrice()));
			tv_order_product_totalPrice.setText(totalPrice);
			orderCache.setTotalPrice(totalPrice);
		}
	}
	
	/**
	 * 返回view
	 * @return
	 */
	public View getView(){
		return view;
	}
}
