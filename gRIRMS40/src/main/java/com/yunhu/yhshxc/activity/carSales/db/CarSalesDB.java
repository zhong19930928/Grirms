package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSales;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesDB {

	private DatabaseHelper openHelper;

	public CarSalesDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertCarSales(CarSales CarSales) {
		ContentValues cv = putContentValues(CarSales);
		openHelper.insert(openHelper.CAR_SALES, cv);
	}
	
	public List<CarSales> findAllCarSales(){
		List<CarSales> list = new ArrayList<CarSales>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES);
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putCarSales(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public List<CarSales> findCombineCarSales(){
		List<CarSales> list = new ArrayList<CarSales>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES).append(" where isCommbine = 1").append(" CarSales by CarSalesNo");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putCarSales(cursor));
			}
		}
		cursor.close();
		return list;
	}
	public CarSales findCarSalesByCarSalesNoAndStoreId(String CarSalesNo){
		CarSales CarSales = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES).append(" where CarSalesNo = ").append("'").append(CarSalesNo).append("'");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				CarSales = putCarSales(cursor);
			}
			cursor.close();
		}
		return CarSales;
	}
	public void updateCarSales(CarSales carSales){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.CAR_SALES, putContentValues(carSales), "CarSalesId="+carSales.getCarSalesId(), null);
	}
	
	public void clearCarSales(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.CAR_SALES);
		openHelper.execSQL(sql.toString());
	}

	private CarSales putCarSales(Cursor cursor) {
		int i = 0;
		CarSales carSales = new CarSales();
		carSales.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		carSales.setCarSalesId(cursor.getInt(i++));
		carSales.setCarSalesNo(cursor.getString(i++));
		carSales.setStoreId(cursor.getString(i++));
		carSales.setStoreName(cursor.getString(i++));
		carSales.setContactId(cursor.getInt(i++));
		String CarSalesAmount = cursor.getString(i++);
		carSales.setCarSalesAmount(TextUtils.isEmpty(CarSalesAmount)?0:Double.parseDouble(CarSalesAmount));
		String actualAmount = cursor.getString(i++);
		carSales.setActualAmount(TextUtils.isEmpty(actualAmount)?0:Double.parseDouble(actualAmount));
		String payAmount = cursor.getString(i++);
		carSales.setPayAmount(TextUtils.isEmpty(payAmount)?0:Double.parseDouble(payAmount));
		String specialDiscount = cursor.getString(i++);
		carSales.setCarSalesDiscount(TextUtils.isEmpty(specialDiscount)?0:Double.parseDouble(specialDiscount));
		String unPayAmount = cursor.getString(i++);
		carSales.setUnPayAmount(TextUtils.isEmpty(unPayAmount)?0:Double.parseDouble(unPayAmount));
		carSales.setNote(cursor.getString(i++));
		carSales.setIsPromotion(cursor.getInt(i++));
		carSales.setCarSalesTime(cursor.getString(i++));
		carSales.setCarSalesState(cursor.getString(i++));
		carSales.setIsCommbine(cursor.getInt(i++));
		carSales.setSalesDate(cursor.getString(i++));
		carSales.setCarNo(cursor.getString(i++));
		carSales.setSalesUser(cursor.getString(i++));
		carSales.setSalesPhone(cursor.getString(i++));
		carSales.setDriverUser(cursor.getString(i++));
		carSales.setDriverPhone(cursor.getString(i++));
		carSales.setStatus(cursor.getString(i++));
		carSales.setCarId(cursor.getString(i++));
		String returnAmount = cursor.getString(i++);
		carSales.setReturnAmount(TextUtils.isEmpty(returnAmount)?0:Double.parseDouble(returnAmount));
		return carSales;
	}

	private ContentValues putContentValues(CarSales carSales) {
		ContentValues cv = new ContentValues();
		cv.put("carSalesId", carSales.getCarSalesId());
		cv.put("carSalesNo", carSales.getCarSalesNo());
		cv.put("storeId", carSales.getStoreId());
		cv.put("storeName", carSales.getStoreName());
		cv.put("contactId", carSales.getContactId());
		cv.put("carSalesAmount", carSales.getCarSalesAmount());
		cv.put("actualAmount", carSales.getActualAmount());
		cv.put("payAmount", carSales.getPayAmount());
		cv.put("carSalesDiscount", carSales.getCarSalesDiscount());
		cv.put("unPayAmount", carSales.getUnPayAmount());
		cv.put("note", carSales.getNote());
		cv.put("isPromotion", carSales.getIsPromotion());
		cv.put("carSalesTime", carSales.getCarSalesTime());
		cv.put("carSalesState", carSales.getCarSalesState());
		cv.put("isCommbine", carSales.getIsCommbine());
		cv.put("salesDate", carSales.getSalesDate());
		cv.put("carNo", carSales.getCarNo());
		cv.put("salesUser", carSales.getSalesUser());
		cv.put("salesPhone", carSales.getSalesPhone());
		cv.put("driverUser", carSales.getDriverUser());
		cv.put("driverPhone", carSales.getDriverPhone());
		cv.put("status", carSales.getStatus());
		cv.put("carId", carSales.getCarId());
		cv.put("returnAmount", carSales.getReturnAmount());
		
		return cv;
	}

}
