package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.order.bo.OrderCache;

/**
 * 缓存订单db
 * @author jishen
 *
 */
public class OrderCacheDB {

	private String TAG = "OrderCacheDB";
	private DatabaseHelper openHelper;
	
	public OrderCacheDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 插入
	 * @param orderCache
	 * @return
	 */
	public Long insert(OrderCache orderCache) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.ORDER_CACHE, null, putContentValues(orderCache));
//		Log.d(TAG + "===保存任务==>", "id = " + id);
		return id;

	}
	
	/**
	 * 修改
	 * @param orderCache
	 */
	public void updateOrderCache(OrderCache orderCache){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(orderCache);
		db.update(openHelper.ORDER_CACHE,cv,"id=?",new String[] { orderCache.getId() + ""});
	}
	
	/**
	 * 查询所有产品
	 * @return
	 */
	public  List<OrderCache> findAllOrderCacheList(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<OrderCache> list=new ArrayList<OrderCache>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.ORDER_CACHE);
		Cursor cursor=db.rawQuery(sql.toString(), null);
		if(cursor.getCount()>0){
			while (cursor.moveToNext()) {
				list.add(putOrderCache(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 根据产品iD查找产品产品
	 * @return
	 */
	public OrderCache findOrderCacheByOrderProductId(String orderProductId){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.ORDER_CACHE).append(" where orderProductId = ").append(orderProductId);
		Cursor cursor=db.rawQuery(sql.toString(), null);
		OrderCache orderCache=null;
		if(cursor.getCount()>0){
			if (cursor.moveToNext()) {
				orderCache=putOrderCache(cursor);
			}
		}
		cursor.close();
		return orderCache;
	}
	
	/**
	 * 根据产品iD查找产品产品
	 * @return
	 */
	public OrderCache findOrderCacheId(String id){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.ORDER_CACHE).append(" where id = ").append(id);
		Cursor cursor=db.rawQuery(sql.toString(), null);
		OrderCache orderCache=null;
		if(cursor.getCount()>0){
			if (cursor.moveToNext()) {
				orderCache=putOrderCache(cursor);
			}
		}
		cursor.close();
		return orderCache;
	}
	
	/**
	 * 更改产品的订单编号
	 * @param orderCache
	 */
	public void updateOrderCacheOrderNumber(String number){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" update ").append(openHelper.ORDER_CACHE).append(" set orderNumber = ").append("'").append(number).append("'");
		db.execSQL(sql.toString());
	}
	
	/**
	 * 计算总价
	 * @return
	 */
	public String computeTotalPrice(){
		String allPrice=null;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" select sum('totalPrice'").append(")").append(" from ").append(openHelper.ORDER_CACHE);
		Cursor c = db.rawQuery(sql.toString(), null);
		if(c.getCount()>0){
			if(c.moveToNext()){
				allPrice=c.getDouble(0)+"";
			}
		}
		return allPrice;
	}
	
	/**
	 * 清除所有数据
	 */
	public void removeAllData(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" delete from ").append(openHelper.ORDER_CACHE).append(" where 1=1");
		db.execSQL(sql.toString());
	}
	
	/**
	 * 通过产品ID删除产品
	 * @param orderProductId 产品ID
	 */
	public void deleteOrderCatchById(String id){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append(" delete from ").append(openHelper.ORDER_CACHE).append(" where id = ").append(id);
		db.execSQL(sql.toString());
	}
	private OrderCache putOrderCache(Cursor cursor) {
		int i = 0;
		OrderCache orderCache  = new OrderCache();
		orderCache.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		orderCache.setOrderNumber(cursor.getString(i++));
		orderCache.setOrderProductId(cursor.getInt(i++));
		orderCache.setOrderProductName(cursor.getString(i++));
		orderCache.setUnitPrice(cursor.getDouble(i++));
		orderCache.setTotalPrice(cursor.getString(i++));
		orderCache.setAvailableOrderQuantity(cursor.getLong(i++));
		orderCache.setPeriodDate(cursor.getString(i++));
		orderCache.setTotalSales(cursor.getLong(i++));
		orderCache.setProjectedInventoryQuantity(cursor.getLong(i++));
		orderCache.setOrderQuantity(cursor.getLong(i++));
		return orderCache;
	}
	
	private ContentValues putContentValues(OrderCache orderCache){
		ContentValues cv = new ContentValues();
		cv.put("orderNumber", orderCache.getOrderNumber());
		cv.put("orderProductId", orderCache.getOrderProductId());
		cv.put("orderProductName", orderCache.getOrderProductName());
		cv.put("unitPrice", orderCache.getUnitPrice());
		cv.put("totalPrice", orderCache.getTotalPrice());
		cv.put("availableOrderQuantity", orderCache.getAvailableOrderQuantity());
		cv.put("periodDate", orderCache.getPeriodDate());
		cv.put("totalSales", orderCache.getTotalSales());
		cv.put("projectedInventoryQuantity", orderCache.getProjectedInventoryQuantity());
		cv.put("orderQuantity", orderCache.getOrderQuantity());
		return cv;
	}
}
