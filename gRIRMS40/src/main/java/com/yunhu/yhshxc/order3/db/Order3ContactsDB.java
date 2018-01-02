package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3Contact;

public class Order3ContactsDB {

	private DatabaseHelper openHelper;

	public Order3ContactsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertOrderContants(Order3Contact orderContacts) {
		ContentValues cv = putContentValues(orderContacts);
		if (orderContacts!=null && orderContacts.getOrderContactsId()>0) {
			openHelper.insert(openHelper.ORDER3_CONTACTS, cv);
		}
	}
	
	public List<Order3Contact> findAllOrderContactsByStoreId(String storeId){
		List<Order3Contact> list = new ArrayList<Order3Contact>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrderContacts(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public Order3Contact findOrderContactsByContactsIdAndStoreId(int orderContactsId,String storeId){
		if (orderContactsId<=0) {
			return null;
		}
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		findSql.append(" and orderContactsId=").append(orderContactsId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		Order3Contact orderContacts = null;
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
		sql.append("delete from ").append(openHelper.ORDER3_CONTACTS);
		openHelper.execSQL(sql.toString());
	}

	private Order3Contact putOrderContacts(Cursor cursor) {
		int i = 1;
		Order3Contact orderContacts = new Order3Contact();
		orderContacts.setStoreId(cursor.getString(i++));
		orderContacts.setOrderContactsId(cursor.getInt(i++));
		orderContacts.setUserAddress(cursor.getString(i++));
		orderContacts.setUserName(cursor.getString(i++));
		orderContacts.setUserPhone1(cursor.getString(i++));
		orderContacts.setUserPhone2(cursor.getString(i++));
		orderContacts.setUserPhone3(cursor.getString(i++));
		return orderContacts;
	}

	private ContentValues putContentValues(Order3Contact orderContacts) {
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
