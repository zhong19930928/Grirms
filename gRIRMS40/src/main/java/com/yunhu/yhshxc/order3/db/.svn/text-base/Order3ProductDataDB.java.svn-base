package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3ProductData;

public class Order3ProductDataDB {

	private DatabaseHelper openHelper;

	public Order3ProductDataDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertOrder(Order3ProductData data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.ORDER3_PRODUCT_DATA, cv);
	}
	
	public List<Order3ProductData> findOrder3ProductDataByOrderNo(String no){
		List<Order3ProductData> list = new ArrayList<Order3ProductData>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_PRODUCT_DATA).append(" where orderNo = '").append(no).append("'");
		findSql.append(" and productId <> 0");
		findSql.append(" and unitId <> 0");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrder3ProductData(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public List<Order3ProductData> findOrder3ProductDataByOrderNoContainPromotion(String no){
		List<Order3ProductData> list = new ArrayList<Order3ProductData>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.ORDER3_PRODUCT_DATA).append(" where orderNo = '").append(no).append("'");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putOrder3ProductData(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void updateOrder3ProductData(Order3ProductData data){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.ORDER3_PRODUCT_DATA, putContentValues(data), "id="+data.getId(), null);
	}
	public void updateOrder3ProductDataUnSendCount(int dataId,double sendCount){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.ORDER3_PRODUCT_DATA);
		sql.append(" set currentSendCount = '").append(sendCount).append("'").append(" where dataId=").append(dataId);
		openHelper.execSQL(sql.toString());
	}
	
	public void clearOrder3ProductData(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.ORDER3_PRODUCT_DATA);
		openHelper.execSQL(sql.toString());
	}

	private Order3ProductData putOrder3ProductData(Cursor cursor) {
		int i = 0;
		Order3ProductData data = new Order3ProductData();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setDataId(cursor.getInt(i++));
		data.setProductId(cursor.getInt(i++));
		String orderCount = cursor.getString(i++);
		data.setOrderCount(TextUtils.isEmpty(orderCount)?0:Double.parseDouble(orderCount));
		String actualCount = cursor.getString(i++);
		data.setActualCount(TextUtils.isEmpty(actualCount)?0:Double.parseDouble(actualCount));
		data.setProductUnit(cursor.getString(i++));
		data.setUnitId(cursor.getInt(i++));
		String orderPrice = cursor.getString(i++);
		data.setOrderPrice(TextUtils.isEmpty(orderPrice)?0:Double.parseDouble(orderPrice));
		String orderAmount = cursor.getString(i++);
		data.setOrderAmount(TextUtils.isEmpty(orderAmount)?0:Double.parseDouble(orderAmount));
		String actualAmount = cursor.getString(i++);
		data.setActualAmount(TextUtils.isEmpty(actualAmount)?0:Double.parseDouble(actualAmount));
		data.setStatus(cursor.getString(i++));
		data.setIsOrderMain(cursor.getInt(i++));
		data.setPromotionId(cursor.getInt(i++));
		data.setMainProductId(cursor.getInt(i++));
		data.setOrderRemark(cursor.getString(i++));
		data.setOrderNo(cursor.getString(i++));
		String sendCount = cursor.getString(i++);
		data.setSendCount(TextUtils.isEmpty(sendCount)?0:Double.parseDouble(sendCount));
		String cSendCount = cursor.getString(i++);
		data.setCurrentSendCount(TextUtils.isEmpty(cSendCount)?0:Double.parseDouble(cSendCount));;
		data.setProductName(cursor.getString(i++));
		return data;
	}

	private ContentValues putContentValues(Order3ProductData data) {
		ContentValues cv = new ContentValues();
		cv.put("dataId", data.getDataId());
		cv.put("productId", data.getProductId());
		cv.put("orderCount", data.getOrderCount());
		cv.put("actualCount", data.getActualCount());
		cv.put("productUnit", data.getProductUnit());
		cv.put("unitId", data.getUnitId());
		cv.put("orderPrice", data.getOrderPrice());
		cv.put("orderAmount", data.getOrderAmount());
		cv.put("actualAmount", data.getActualAmount());
		cv.put("status", data.getStatus());
		cv.put("isOrderMain", data.getIsOrderMain());
		cv.put("promotionId", data.getPromotionId());
		cv.put("mainProductId", data.getMainProductId());
		cv.put("orderRemark", data.getOrderRemark());
		cv.put("orderNo", data.getOrderNo());
		cv.put("sendCount", data.getSendCount());
		cv.put("currentSendCount", data.getCurrentSendCount());
		cv.put("productName", data.getProductName());
		return cv;
	}

}
