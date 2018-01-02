package com.yunhu.yhshxc.order.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.order.OrderStockUpdate;
import com.yunhu.yhshxc.order.bo.Inventory;

/**
 * 库存管理列表
 * @author jishen
 *
 */
public class OrderStockControlItem {
	private View view=null;
	private TextView tv_order_stock_control_date;//日期范围
	private TextView tv_order_stock_control_product_name;//产品名称
	private TextView tv_order_stock_control_stock_number;//库存剩余
	private TextView tv_order_stock_control_last_inventory;//上次盘点
	private TextView tv_order_stock_control_returned_purchase;//期间退货
	private TextView tv_order_stock_control_effectively_stock;//有效进货
	private TextView tv_order_stock_control_toatal_sales;//总销量
	private String storeId;String storeName;
	private Inventory currentInventory;
	private Context context;
	public OrderStockControlItem(Context mContext) {
		this.context=mContext;
		view=View.inflate(mContext, R.layout.order_stock_control_item, null);
		tv_order_stock_control_date=(TextView)view.findViewById(R.id.tv_order_stock_control_date);
		tv_order_stock_control_product_name=(TextView)view.findViewById(R.id.tv_order_stock_control_product_name);
		tv_order_stock_control_stock_number=(TextView)view.findViewById(R.id.tv_order_stock_control_stock_number);
		tv_order_stock_control_last_inventory=(TextView)view.findViewById(R.id.tv_order_stock_control_last_inventory);
		tv_order_stock_control_returned_purchase=(TextView)view.findViewById(R.id.tv_order_stock_control_returned_purchase);
		tv_order_stock_control_effectively_stock=(TextView)view.findViewById(R.id.tv_order_stock_control_effectively_stock);
		tv_order_stock_control_toatal_sales=(TextView)view.findViewById(R.id.tv_order_stock_control_toatal_sales);
		view.setOnClickListener(onClickListener);
	}
	
	
	/**
	 * 单击事件监听
	 */
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			intentToOrderStockUpdate();
		}
	};
	
	private void intentToOrderStockUpdate(){
		Intent intent=new Intent(context,OrderStockUpdate.class);
		intent.putExtra("storeName", storeName);//店面名称
		intent.putExtra("storeId", storeId);//店面Id
		intent.putExtra("productName", currentInventory.getProductName());//产品名称
		intent.putExtra("productId", currentInventory.getProductId());//产品ID
		intent.putExtra("productNumber",tv_order_stock_control_stock_number.getText().toString());//预计库存数
//		context.startActivity(intent)(intent);
		((Activity) context).startActivityForResult(intent,R.id.order_stock_submit_return);
	}
	
	/**
	 * 设置产品店面信息
	 * @param storeId 店面ID
	 * @param storeName店面名称
	 */
	public void setStoreInfo(String storeId,String storeName){
		this.storeId=storeId;
		this.storeName=storeName;
	}
	
	/**
	 * 设置数据源
	 * @param inventory
	 */
	public void setDate(Inventory inventory){
		this.currentInventory=inventory;
		if(inventory!=null){
			setOrderStockControlDate(inventory.getReportTime());//时间
			setProductName(inventory.getProductName());//产品名称
			setLastInventory("+"+inventory.getInventoryCount());//上次盘点
			setReturnPurchase("-"+inventory.getReturnedCount());//退货
			setEffectivelyStock("+"+inventory.getStockCount());//有效进货
			setTotalSales("-"+inventory.getTotalSales());//总销量
			long stockProductNumber=inventory.getInventoryCount()+inventory.getStockCount()-inventory.getReturnedCount()-inventory.getTotalSales();
			if(stockProductNumber<0){
				stockProductNumber=0;
			}
			setStockProductNumber(stockProductNumber+"");
		}
	}
	
	/**
	 * 设置日期范围
	 * @param data
	 */
	private void setOrderStockControlDate(String data){
		tv_order_stock_control_date.setText(data);
	}
	
	/**
	 * 设置产品名称
	 * @param name
	 */
	private void setProductName(String name){
		tv_order_stock_control_product_name.setText(name);
	}
	
	/**
	 * 设置剩余库存
	 */
	private void setStockProductNumber(String number){
		tv_order_stock_control_stock_number.setText(number);
	}
	/**
	 * 设置上次盘点的值
	 */
	private void setLastInventory(String lastInventory){
		tv_order_stock_control_last_inventory.setText(lastInventory);
	}
	/**
	 * 设置期间退货
	 */
	private void setReturnPurchase(String retrunPurchase){
		tv_order_stock_control_returned_purchase.setText(retrunPurchase);
	}
	/**
	 * 有效进货
	 */
	private void setEffectivelyStock(String effectivelyStock){
		tv_order_stock_control_effectively_stock.setText(effectivelyStock);
	}
	/**
	 * 设置总销量
	 */
	private void setTotalSales(String totalSales){
		tv_order_stock_control_toatal_sales.setText(totalSales);
	}
	
	/**
	 * 返回view
	 * @return
	 */
	public View getView(){
		return view;
	}
}

