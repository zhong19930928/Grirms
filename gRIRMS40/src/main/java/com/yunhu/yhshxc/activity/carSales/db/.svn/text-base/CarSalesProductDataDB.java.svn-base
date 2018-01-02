package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductData;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesProductDataDB {

	private DatabaseHelper openHelper;

	public CarSalesProductDataDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public void insertCarSalesProductData(CarSalesProductData data) {
		ContentValues cv = putContentValues(data);
		openHelper.insert(openHelper.CAR_SALES_PRODUCT_DATA, cv);
	}
	
	public List<CarSalesProductData> findCarSalesProductDataByCarSalesNo(String no){
		List<CarSalesProductData> list = new ArrayList<CarSalesProductData>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES_PRODUCT_DATA).append(" where carSalesNo = '").append(no).append("'");
		findSql.append(" and productId <> 0");
		findSql.append(" and unitId <> 0");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putCarSalesProductData(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public List<CarSalesProductData> findCarSalesProductDataByCarSalesNoContainPromotion(String no){
		List<CarSalesProductData> list = new ArrayList<CarSalesProductData>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.CAR_SALES_PRODUCT_DATA).append(" where carSalesNo = '").append(no).append("'");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putCarSalesProductData(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	public void updateCarSalesProductData(CarSalesProductData data){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.CAR_SALES_PRODUCT_DATA, putContentValues(data), "id="+data.getId(), null);
	}
	public void updateCarSalesProductDataUnSendCount(int dataId,double sendCount){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_PRODUCT_DATA);
		sql.append(" set currentSendCount = '").append(sendCount).append("'").append(" where dataId=").append(dataId);
		openHelper.execSQL(sql.toString());
	}
	
	public void clearCarSalesProductData(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(openHelper.CAR_SALES_PRODUCT_DATA);
		openHelper.execSQL(sql.toString());
	}

	private CarSalesProductData putCarSalesProductData(Cursor cursor) {
		int i = 0;
		CarSalesProductData data = new CarSalesProductData();
		data.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		data.setDataId(cursor.getInt(i++));
		data.setProductId(cursor.getInt(i++));
		String carSalesCount = cursor.getString(i++);
		data.setCarSalesCount(TextUtils.isEmpty(carSalesCount)?0:Double.parseDouble(carSalesCount));
		String actualCount = cursor.getString(i++);
		data.setActualCount(TextUtils.isEmpty(actualCount)?0:Double.parseDouble(actualCount));
		data.setProductUnit(cursor.getString(i++));
		data.setUnitId(cursor.getInt(i++));
		String carSalesPrice = cursor.getString(i++);
		data.setCarSalesPrice(TextUtils.isEmpty(carSalesPrice)?0:Double.parseDouble(carSalesPrice));
		String carSalesAmount = cursor.getString(i++);
		data.setCarSalesAmount(TextUtils.isEmpty(carSalesAmount)?0:Double.parseDouble(carSalesAmount));
		String actualAmount = cursor.getString(i++);
		data.setActualAmount(TextUtils.isEmpty(actualAmount)?0:Double.parseDouble(actualAmount));
		data.setStatus(cursor.getString(i++));
		data.setIsCarSalesMain(cursor.getInt(i++));
		data.setPromotionId(cursor.getInt(i++));
		data.setMainProductId(cursor.getInt(i++));
		data.setCarSalesRemark(cursor.getString(i++));
		data.setCarSalesNo(cursor.getString(i++));
		String sendCount = cursor.getString(i++);
		data.setSendCount(TextUtils.isEmpty(sendCount)?0:Double.parseDouble(sendCount));
		String cSendCount = cursor.getString(i++);
		data.setCurrentSendCount(TextUtils.isEmpty(cSendCount)?0:Double.parseDouble(cSendCount));;
		data.setProductName(cursor.getString(i++));
		String stockNum = cursor.getString(i++);
		data.setStockNum(TextUtils.isEmpty(stockNum)?0:Double.parseDouble(stockNum));
		return data;
	}

	private ContentValues putContentValues(CarSalesProductData data) {
		ContentValues cv = new ContentValues();
		cv.put("dataId", data.getDataId());
		cv.put("productId", data.getProductId());
		cv.put("carSalesCount", data.getCarSalesCount());
		cv.put("actualCount", data.getActualCount());
		cv.put("productUnit", data.getProductUnit());
		cv.put("unitId", data.getUnitId());
		cv.put("carSalesPrice", data.getCarSalesPrice());
		cv.put("carSalesAmount", data.getCarSalesAmount());
		cv.put("actualAmount", data.getActualAmount());
		cv.put("status", data.getStatus());
		cv.put("isCarSalesMain", data.getIsCarSalesMain());
		cv.put("promotionId", data.getPromotionId());
		cv.put("mainProductId", data.getMainProductId());
		cv.put("carSalesRemark", data.getCarSalesRemark());
		cv.put("carSalesNo", data.getCarSalesNo());
		cv.put("sendCount", data.getSendCount());
		cv.put("currentSendCount", data.getCurrentSendCount());
		cv.put("productName", data.getProductName());
		cv.put("stockNum", data.getStockNum());
		return cv;
	}

}
