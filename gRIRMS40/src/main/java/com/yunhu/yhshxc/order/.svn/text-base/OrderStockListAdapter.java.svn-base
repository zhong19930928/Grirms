package com.yunhu.yhshxc.order;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.order.bo.Inventory;
import com.yunhu.yhshxc.order.view.OrderStockControlItem;

/**
 * 库存管理列表数据适配器
 * @author jishen
 *
 */
public class OrderStockListAdapter extends BaseAdapter{

	private List<Inventory> inventoryList=null;//数据源、
	private Context context;
	private String storeId,storeName;
	
	public OrderStockListAdapter(Context mContext,List<Inventory> inventoryList) {
		this.context=mContext;
		this.inventoryList=inventoryList;
	}
	@Override
	public int getCount() {
		return inventoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return inventoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Inventory inventory=inventoryList.get(position);
		OrderStockControlItem orderStockControlItem=null;
		if(convertView==null){
			orderStockControlItem=new OrderStockControlItem(context);
			orderStockControlItem.setStoreInfo(storeId, storeName);
			convertView=orderStockControlItem.getView();
			convertView.setTag(orderStockControlItem);
		}else{
			orderStockControlItem=(OrderStockControlItem) convertView.getTag();
		}
		orderStockControlItem.setDate(inventory);
		return convertView;
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
	
}
