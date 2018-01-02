package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3Dis;

public class Order3DisDB {

	private DatabaseHelper openHelper;

	public Order3DisDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertOrderContants(Order3Dis dis) {
		ContentValues cv = putContentValues(dis);
		openHelper.insert(openHelper.ORDER3_DIS, cv);
	}
	
	
	public Order3Dis findOrder3DisByDisId(int disId){
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_DIS).append(" where disId = ").append(disId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		Order3Dis order3Dis = null;
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				order3Dis = putOrder3Dis(cursor);
			}
		}
		cursor.close();
		return order3Dis;
	}
	
	public List<Order3Dis> findOrder3DisByProductId(int productId){
		List<Order3Dis> list = new ArrayList<Order3Dis>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_DIS).append(" where productId = ").append(productId);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		Order3Dis order3Dis = null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				order3Dis = putOrder3Dis(cursor);
				list.add(order3Dis);
			}
		}
		cursor.close();
		return list;
		
//		StringBuffer findSql=new StringBuffer();
//		findSql.append("select * from ").append(openHelper.ORDER3_DIS).append(" where productId = ").append(productId);
//		findSql.append(" and length (").append(orgCode).append(")").append(" - ").append(" length (replace ( ").append(orgCode).append(",orgCode , '' )) > 0 ");
//		Cursor cursor = openHelper.query(findSql.toString(), null);
//		Order3Dis order3Dis = null;
//		if (cursor.getCount() > 0) {
//			if (cursor.moveToNext()) {
//				order3Dis = putOrder3Dis(cursor);
//			}
//		}
//		cursor.close();
//		return order3Dis;
	}
	
	public void clearOrder3Dis(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.ORDER3_DIS);
		openHelper.execSQL(sql.toString());
	}

	private Order3Dis putOrder3Dis(Cursor cursor) {
		int i = 0;
		Order3Dis dis = new Order3Dis();
		dis.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		dis.setDisId(cursor.getInt(i++));
		dis.setProductId(cursor.getInt(i++));
		dis.setOrgId(cursor.getInt(i++));
		dis.setOrgCode(cursor.getString(i++));
		dis.setOrgLevel(cursor.getInt(i++));
		return dis;
	}

	private ContentValues putContentValues(Order3Dis dis) {
		ContentValues cv = new ContentValues();
		cv.put("disId", dis.getDisId());
		cv.put("productId", dis.getProductId());
		cv.put("orgId", dis.getOrgId());
		cv.put("orgCode", dis.getOrgCode());
		cv.put("orgLevel", dis.getOrgLevel());
		return cv;
	}

}
