package com.yunhu.yhshxc.database;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.order2.Order2Data;
import com.yunhu.yhshxc.order2.bo.NewOrder;
import com.yunhu.yhshxc.utility.PublicUtils;

public class NewOrderDB {

	private DatabaseHelper openHelper;

	public NewOrderDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertNewOrder(NewOrder newOrder) throws JSONException {
		ContentValues cv = putContentValues(newOrder);
		openHelper.insert(openHelper.NEW_ORDER, cv);
	}
	
	
	public NewOrder findNewOrderByStoreId(String storeId) throws JSONException{
		NewOrder newOrder = null;
		if(PublicUtils.isInteger(storeId)){
			StringBuffer findSql=new StringBuffer();
			findSql.append("select * from ").append(openHelper.NEW_ORDER).append(" where storeId = '").append(storeId).append("'");
			Cursor cursor = openHelper.query(findSql.toString(), null);
			
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					newOrder = putNewOrder(cursor);
				}
			}
			cursor.close();
		}
		return newOrder;
	}
	
	public void updateNewOrder(NewOrder newOrder) throws JSONException{
		ContentValues cv = putContentValues(newOrder);
		openHelper.update( openHelper.NEW_ORDER, cv, " storeId=? ", new String[] { String.valueOf(newOrder.getStoreId()) });	}
	
	public void clearNewOrderByStoreId(String storeId){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.NEW_ORDER).append(" where storeId = '").append(storeId).append("'");
		openHelper.execSQL(sql.toString());
	}

	private NewOrder putNewOrder(Cursor cursor) throws JSONException {
		int i = 1;
		NewOrder newOrder = new NewOrder();
		newOrder.setStoreId(cursor.getString(i++));
		String orderData = cursor.getString(i++);
		newOrder.setOrder(Order2Data.json2Order(orderData));
		return newOrder;
	}

	private ContentValues putContentValues(NewOrder newOrder) throws JSONException {
		ContentValues cv = new ContentValues();
		cv.put("storeId", newOrder.getStoreId());
		cv.put("orderData", Order2Data.order2Json(newOrder.getOrder()));
		return cv;
	}

}
