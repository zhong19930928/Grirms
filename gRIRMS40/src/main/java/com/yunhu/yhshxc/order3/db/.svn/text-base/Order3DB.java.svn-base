package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3;

public class Order3DB {

	private DatabaseHelper openHelper;

	public Order3DB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertOrder(Order3 order) {
		ContentValues cv = putContentValues(order);
		openHelper.insert(openHelper.ORDER3, cv);
	}
	
	public List<Order3> findAllOrder(){
		List<Order3> list = new ArrayList<Order3>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrder(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public List<Order3> findCombineOrder(){
		List<Order3> list = new ArrayList<Order3>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3).append(" where isCommbine = 1").append(" order by orderNo");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrder(cursor));
			}
		}
		cursor.close();
		return list;
	}
	public Order3 findOrder3ByOrderNoAndStoreId(String orderNo){
		Order3 order3 = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3).append(" where orderNo = ").append(orderNo);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				order3 = putOrder(cursor);
			}
			cursor.close();
		}
		return order3;
	}
	public void updateOrder3(Order3 order){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.ORDER3, putContentValues(order), "orderId="+order.getOrderId(), null);
	}
	
	public void clearOrder(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.ORDER3);
		openHelper.execSQL(sql.toString());
	}

	private Order3 putOrder(Cursor cursor) {
		int i = 0;
		Order3 order = new Order3();
		order.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		order.setOrderId(cursor.getInt(i++));
		order.setOrderNo(cursor.getString(i++));
		order.setStoreId(cursor.getString(i++));
		order.setStoreName(cursor.getString(i++));
		order.setContactId(cursor.getInt(i++));
		String orderAmount = cursor.getString(i++);
		order.setOrderAmount(TextUtils.isEmpty(orderAmount)?0:Double.parseDouble(orderAmount));
		String actualAmount = cursor.getString(i++);
		order.setActualAmount(TextUtils.isEmpty(actualAmount)?0:Double.parseDouble(actualAmount));
		String payAmount = cursor.getString(i++);
		order.setPayAmount(TextUtils.isEmpty(payAmount)?0:Double.parseDouble(payAmount));
		String specialDiscount = cursor.getString(i++);
		order.setOrderDiscount(TextUtils.isEmpty(specialDiscount)?0:Double.parseDouble(specialDiscount));
		String unPayAmount = cursor.getString(i++);
		order.setUnPayAmount(TextUtils.isEmpty(unPayAmount)?0:Double.parseDouble(unPayAmount));
		order.setNote(cursor.getString(i++));
		order.setIsPromotion(cursor.getInt(i++));
		order.setOrderTime(cursor.getString(i++));
		order.setOrderState(cursor.getString(i++));
		order.setIsCommbine(cursor.getInt(i++));
		return order;
	}

	private ContentValues putContentValues(Order3 order) {
		ContentValues cv = new ContentValues();
		cv.put("orderId", order.getOrderId());
		cv.put("orderNo", order.getOrderNo());
		cv.put("storeId", order.getStoreId());
		cv.put("storeName", order.getStoreName());
		cv.put("contactId", order.getContactId());
		cv.put("orderAmount", order.getOrderAmount());
		cv.put("actualAmount", order.getActualAmount());
		cv.put("payAmount", order.getPayAmount());
		cv.put("orderDiscount", order.getOrderDiscount());
		cv.put("unPayAmount", order.getUnPayAmount());
		cv.put("note", order.getNote());
		cv.put("isPromotion", order.getIsPromotion());
		cv.put("orderTime", order.getOrderTime());
		cv.put("orderState", order.getOrderState());
		cv.put("isCommbine", order.getIsCommbine());
		return cv;
	}

}
