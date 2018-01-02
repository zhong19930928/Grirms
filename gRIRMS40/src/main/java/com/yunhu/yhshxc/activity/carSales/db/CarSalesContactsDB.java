package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesContact;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesContactsDB {

	private DatabaseHelper openHelper;

	public CarSalesContactsDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertCarSalesContact(CarSalesContact contacts) {
		ContentValues cv = putContentValues(contacts);
		if (contacts!=null && contacts.getContactsId()>0) {
			openHelper.insert(openHelper.CAR_SALES_CONTACTS, cv);
		}
	}
	
	public List<CarSalesContact> findAllCarSalesContactsByStoreId(String storeId){
		List<CarSalesContact> list = new ArrayList<CarSalesContact>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putCarSalesContact(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public CarSalesContact findCarSalesContactByContactsIdAndStoreId(int CarSalesContactsId,String storeId){
		if (CarSalesContactsId<=0) {
			return null;
		}
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES_CONTACTS).append(" where storeId = '").append(storeId).append("'");
		findSql.append(" and contactsId=").append(CarSalesContactsId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		CarSalesContact contacts = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				contacts = putCarSalesContact(cursor);
			}
		}
		cursor.close();
		return contacts;
	}
	
	public void clearCarSalesContacts(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.CAR_SALES_CONTACTS);
		openHelper.execSQL(sql.toString());
	}

	private CarSalesContact putCarSalesContact(Cursor cursor) {
		int i = 1;
		CarSalesContact contacts = new CarSalesContact();
		contacts.setStoreId(cursor.getString(i++));
		contacts.setContactsId(cursor.getInt(i++));
		contacts.setUserAddress(cursor.getString(i++));
		contacts.setUserName(cursor.getString(i++));
		contacts.setUserPhone1(cursor.getString(i++));
		contacts.setUserPhone2(cursor.getString(i++));
		contacts.setUserPhone3(cursor.getString(i++));
		return contacts;
	}

	private ContentValues putContentValues(CarSalesContact contact) {
		ContentValues cv = new ContentValues();
		cv.put("storeId", contact.getStoreId());
		cv.put("contactsId", contact.getContactsId());
		cv.put("userAddress", contact.getUserAddress());
		cv.put("userName", contact.getUserName());
		cv.put("userPhone1", contact.getUserPhone1());
		cv.put("userPhone2", contact.getUserPhone2());
		cv.put("userPhone3", contact.getUserPhone3());
		return cv;
	}

}
