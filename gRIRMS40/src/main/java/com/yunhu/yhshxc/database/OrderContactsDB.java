package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.order2.bo.OrderContacts;

public class OrderContactsDB {

	private DatabaseHelper openHelper;

	public OrderContactsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertOrderContants(OrderContacts orderContacts) {
		ContentValues cv = putContentValues(orderContacts);
		openHelper.insert(openHelper.ORDER_CONTACTS, cv);
	}
	
	public List<OrderContacts> findAllOrderContactsByStoreId(String storeId){
		List<OrderContacts> list = new ArrayList<OrderContacts>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrderContacts(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public OrderContacts findOrderContactsByContactsIdAndStoreId(int orderContactsId,String storeId){
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		findSql.append(" and orderContactsId=").append(orderContactsId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		OrderContacts orderContacts = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				orderContacts = putOrderContacts(cursor);
			}
		}
		cursor.close();
		return orderContacts;
	}
	
	public void clearOrderContacts(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.ORDER_CONTACTS);
		openHelper.execSQL(sql.toString());
	}

	private OrderContacts putOrderContacts(Cursor cursor) {
		int i = 1;
		OrderContacts orderContacts = new OrderContacts();
		orderContacts.setStoreId(cursor.getString(i++));
		orderContacts.setOrderContactsId(cursor.getInt(i++));
		orderContacts.setUserAddress(cursor.getString(i++));
		orderContacts.setUserName(cursor.getString(i++));
		orderContacts.setUserPhone1(cursor.getString(i++));
		orderContacts.setUserPhone2(cursor.getString(i++));
		orderContacts.setUserPhone3(cursor.getString(i++));
		return orderContacts;
	}

	private ContentValues putContentValues(OrderContacts orderContacts) {
		ContentValues cv = new ContentValues();
		cv.put("storeId", orderContacts.getStoreId());
		cv.put("orderContactsId", orderContacts.getOrderContactsId());
		cv.put("userAddress", orderContacts.getUserAddress());
		cv.put("userName", orderContacts.getUserName());
		cv.put("userPhone1", orderContacts.getUserPhone1());
		cv.put("userPhone2", orderContacts.getUserPhone2());
		cv.put("userPhone3", orderContacts.getUserPhone3());
		return cv;
	}

}
