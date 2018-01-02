package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.order.bo.OrderItem;

public class OrderSaleDB {
	private DatabaseHelper openHelper;

	public OrderSaleDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public void save(OrderItem item, String storeid) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("storeid", storeid);
		values.put("proid", item.getProductId());
		values.put("name", item.getName());
		values.put("price", item.getPrice());
		values.put("sales", item.getSales());

		Cursor cursor = db.query(openHelper.ORDER_SALE, null, "id = " + item.getId() + " AND storeid = " + storeid, null, null, null, null);
		if (cursor.getCount() == 0) {
			long id = db.insert(openHelper.ORDER_SALE, null, values);
			item.setId((int)id);
		}
		else {
			db.update(openHelper.ORDER_SALE, values, "id = " + item.getId(), null);
		}
		cursor.close();
	}
	
	public void delete(int id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete(openHelper.ORDER_SALE, "id = " + id, null);
	}
	
	public void deleteAll(String storeid) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete(openHelper.ORDER_SALE, "storeid = " + storeid, null);
	}
	
	public List<OrderItem> getAll(String storeid) {
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.query(openHelper.ORDER_SALE, null, "storeid = " + storeid, null, null, null, null);
		while (cursor.moveToNext()) {
			OrderItem item = new OrderItem();
			item.setId(cursor.getInt(cursor.getColumnIndex("id")));
			item.setProductId(cursor.getInt(cursor.getColumnIndex("proid")));
			item.setName(cursor.getString(cursor.getColumnIndex("name")));
			item.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
			item.setSales(cursor.getLong(cursor.getColumnIndex("sales")));
			items.add(item);
		}
		cursor.close();
		
		return items;
	}
}
