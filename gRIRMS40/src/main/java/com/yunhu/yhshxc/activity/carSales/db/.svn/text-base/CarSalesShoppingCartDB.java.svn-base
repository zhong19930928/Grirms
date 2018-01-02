package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesShoppingCart;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesShoppingCartDB {	
	
private DatabaseHelper openHelper;
private Context context;
	public CarSalesShoppingCartDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public Long insert(CarSalesShoppingCart cart) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.CAR_SALES_SHOPPING_CART, null, putContentValues(cart));
		
		return id;
	
	}
	
	public List<CarSalesShoppingCart> findAllList(){
		List<CarSalesShoppingCart> list = new ArrayList<CarSalesShoppingCart>();
		Cursor cursor = openHelper.query(openHelper.CAR_SALES_SHOPPING_CART, null, null, null, null, null, null);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				list.add(putCarSalesShoppingCart(cursor));
			}
			cursor.close();
		}
		return list;
	}
	
	public int count(){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ").append(openHelper.CAR_SALES_SHOPPING_CART);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor!=null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	
	public CarSalesShoppingCart findCarSalesShoppingCartByProductId(int productId,int unitId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		CarSalesShoppingCart CarSalesShoppingCart = null;
		sql.append("select * from ").append(openHelper.CAR_SALES_SHOPPING_CART).append(" where productId=").append(productId).append(" and unitId = ").append(unitId);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				CarSalesShoppingCart =putCarSalesShoppingCart(cursor);
			}
		}
		cursor.close();
		return CarSalesShoppingCart;
	}
	
	/**
	 * 查找选中的同一类的产品
	 * @param pId
	 * @return
	 */
	public List<CarSalesShoppingCart> findCarSalesShoppingCartByPid(int pId) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		CarSalesShoppingCart CarSalesShoppingCart = null;
		List<CarSalesShoppingCart> list = new ArrayList<CarSalesShoppingCart>();
		sql.append("select * from ").append(openHelper.CAR_SALES_SHOPPING_CART).append(" where pId='").append(pId).append("' and pitcOn = 1");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				CarSalesShoppingCart =putCarSalesShoppingCart(cursor);
				list.add(CarSalesShoppingCart);
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 查找选中的同一类的产品
	 * @param pId
	 * @return
	 */
	public List<CarSalesShoppingCart> findSameKindCarSalesShoppingCart() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		CarSalesShoppingCart CarSalesShoppingCart = null;
		List<CarSalesShoppingCart> list = new ArrayList<CarSalesShoppingCart>();
		sql.append("select * from ").append(openHelper.CAR_SALES_SHOPPING_CART).append(" where pitcOn = 1").append(" group by pId");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				CarSalesShoppingCart =putCarSalesShoppingCart(cursor);
				list.add(CarSalesShoppingCart);
			}
		}
		cursor.close();
		return list;
	}
	
	
	public void updateCarSalesShoppingCart(CarSalesShoppingCart cart){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update(openHelper.CAR_SALES_SHOPPING_CART, putContentValues(cart), "id="+cart.getId(), null);
		
	}
	
	public void delete(){
		openHelper.delete(openHelper.CAR_SALES_SHOPPING_CART, null, null);
	}
	
	public void deleteSub(){
		openHelper.delete(openHelper.CAR_SALES_SHOPPING_CART, "pitcOn=1", null);
	}
	
	private CarSalesShoppingCart putCarSalesShoppingCart(Cursor cursor) {
		int i = 0;
		CarSalesShoppingCart cart = new CarSalesShoppingCart();
		cart.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		cart.setProductId(cursor.getInt(i++));
		cart.setpId(cursor.getString(i++));
		String promotionIds = cursor.getString(i++);
		cart.setPromotionIds(TextUtils.isEmpty(promotionIds) ? String.valueOf(0):promotionIds);
		cart.setUnitId(cursor.getInt(i++));
		String number = cursor.getString(i++);
		cart.setNumber(TextUtils.isEmpty(number) ? 0 : Double.parseDouble(number));
		String subtotal = cursor.getString(i++);
		cart.setSubtotal(TextUtils.isEmpty(subtotal) ? 0 : Double.parseDouble(subtotal));
		cart.setPitcOn(cursor.getInt(i++));
		String disAmount = cursor.getString(i++);
		cart.setDisAmount(TextUtils.isEmpty(disAmount) ? 0 : Double.parseDouble(disAmount));
		String disNumber = cursor.getString(i++);
		cart.setDisNumber(TextUtils.isEmpty(disNumber) ? 0 : Double.parseDouble(disNumber));
		String discountPrice = cursor.getString(i++);
		cart.setDiscountPrice(TextUtils.isEmpty(discountPrice) ? 0 : Double.parseDouble(discountPrice));
		cart.setGiftId(cursor.getInt(i++));
		cart.setGiftUnitId(cursor.getInt(i++));
		String preAmount = cursor.getString(i++);
		cart.setPreAmount(TextUtils.isEmpty(preAmount) ? 0 : Double.parseDouble(preAmount));
		String prePrice = cursor.getString(i++);
		cart.setPrePrice(TextUtils.isEmpty(prePrice) ? 0 : Double.parseDouble(prePrice));
		String nowProductPrict = cursor.getString(i++);
		cart.setNowProductPrict(TextUtils.isEmpty(nowProductPrict) ? 0 : Double.parseDouble(nowProductPrict));
		return cart;
	}
	
	private ContentValues putContentValues(CarSalesShoppingCart cart){
		ContentValues cv = new ContentValues();
		cv.put("productId", cart.getProductId());
		cv.put("pId", cart.getpId());
		cv.put("promotionIds", cart.getPromotionIds());
		cv.put("unitId", cart.getUnitId());
		cv.put("number", cart.getNumber());
		cv.put("subtotal", cart.getSubtotal());
		cv.put("pitcOn", cart.getPitcOn());
		cv.put("disAmount", cart.getDisAmount());
		cv.put("disNumber", cart.getDisNumber());
		cv.put("discountPrice", cart.getDiscountPrice());
		cv.put("giftId", cart.getGiftId());
		cv.put("giftUnitId", cart.getGiftUnitId());
		cv.put("preAmount", cart.getPreAmount());
		cv.put("prePrice", cart.getPrePrice());
		cv.put("nowProductPrict", cart.getNowProductPrict());
		return cv;
	}
	
}
