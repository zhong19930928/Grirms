package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesStock;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesStockDB {
	private DatabaseHelper openHelper;
	private Context context;

	public CarSalesStockDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 获取库存统计总数量
	 * @return
	 */
	public int count() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ")
				.append(openHelper.CAR_SALES_STOCK);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}

	public Long insert(CarSalesStock stock) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.CAR_SALES_STOCK, null,
				putContentValues(stock));
		return id;
	}

	/**
	 * 根据产品ID和单位ID查找产品的库存
	 * 
	 * @param productId
	 *            产品ID
	 * @param unitId
	 *            单位ID
	 * @return
	 */
	public CarSalesStock findCarSalesStockByProductIdAndUnitId(int productId,
			int unitId) {
		CarSalesStock stock = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_STOCK)
				.append(" where productId = ").append(productId);
		sql.append(" and unitId = ").append(unitId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				stock = putCarSalesStock(cursor);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return stock;
	}

	/**
	 * 查找库存列表
	 * 
	 * @return
	 */
	public List<CarSalesStock> findCarSalesStock() {
		List<CarSalesStock> list = new ArrayList<CarSalesStock>();
		CarSalesStock stock = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_STOCK);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				stock = putCarSalesStock(cursor);
				list.add(stock);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * 分页查找库存列表
	 * 
	 * @return
	 */
	public List<CarSalesStock> findCarSalesStockByPage(int pages) {
		List<CarSalesStock> list = new ArrayList<CarSalesStock>();
		CarSalesStock stock = null;
		StringBuffer sql = new StringBuffer();
		String str = (pages - 1) * 20 + ",20";
		sql.append(" select * from ").append(openHelper.CAR_SALES_STOCK).append(" order by stockNum desc")
				.append(" limit ").append(str);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				stock = putCarSalesStock(cursor);
				list.add(stock);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}


	/**
	 * 分页查找缺货库存列表
	 * 
	 * @return
	 */
	public List<CarSalesStock> findStockoutCarSalesStockBypages(int pages) {
		List<CarSalesStock> list = new ArrayList<CarSalesStock>();
		CarSalesStock stock = null;
		StringBuffer sql = new StringBuffer();
		String str = (pages - 1) * 20 + ",20";
		sql.append(" select * from ").append(openHelper.CAR_SALES_STOCK)
				.append(" where stockoutNum <> '0' and stockoutNum <> '0.0' " ).append(" order by stockoutNum desc").append(" limit ").append(str);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				stock = putCarSalesStock(cursor);
				list.add(stock);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * 获取缺货统计总数量
	 * @return
	 */
	public int countStockOut() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		int count = 0;
		StringBuffer sql = new StringBuffer("select count(*) from ").append(
				openHelper.CAR_SALES_STOCK).append(" where stockoutNum <> '0' and stockoutNum <> '0.0'");
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}

	public void updateCarSalesStock(CarSalesStock stock) {
		openHelper.update(openHelper.CAR_SALES_STOCK, putContentValues(stock),
				" id = " + stock.getId(), null);
	}
	
	public void updateCarSalesStockLable(CarSalesStock stock) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_STOCK);
		sql.append(" set productName = '").append(stock.getProductName()).append("'")
				.append(" where productId=").append(stock.getProductId())
				.append(" and unitId = ").append(stock.getUnitId());
		openHelper.execSQL(sql.toString());
	}

	public void updateCarSalesStockNum(int productId, int unitId, String num) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_STOCK);
		sql.append(" set stockNum = '").append(num).append("'")
				.append(" where productId=").append(productId)
				.append(" and unitId = ").append(unitId);
		openHelper.execSQL(sql.toString());
	}

	public void updateCarSalesStockOutNum(int productId, int unitId,
			String outNum) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_STOCK);
		sql.append(" set stockoutNum = '").append(outNum).append("'")
				.append(" where productId=").append(productId)
				.append(" and unitId = ").append(unitId);
		openHelper.execSQL(sql.toString());
	}

	public void updateCarSalesStockByNumAndOutNumber(int productId, int unitId,
			String returnNum, String outNum) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_STOCK);
		sql.append(" set stockNum = '").append(returnNum).append("'")
				.append(" and stockoutNum = '").append(outNum).append("'")
				.append(" where productId=").append(productId)
				.append(" and unitId = ").append(unitId);
		openHelper.execSQL(sql.toString());
	}

	/**
	 * 更新补货数量
	 * @param productId
	 * @param unitId
	 * @param replenishmentNum
	 */
	public void updateCarSalesStockReplenishmentNum(int productId,int unitId,String replenishmentNum){
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(openHelper.CAR_SALES_STOCK);
		sql.append(" set replenishmentNum = '").append(replenishmentNum).append("'").append(" where productId=").append(productId).append(" and unitId = ").append(unitId);
		openHelper.execSQL(sql.toString());
	}
	



	public void delete() {
		openHelper.delete(openHelper.CAR_SALES_STOCK, null, null);
	}

	private CarSalesStock putCarSalesStock(Cursor cursor) {
		CarSalesStock stock = new CarSalesStock();
		int i = 0;
		stock.setId(cursor.isNull(i) ? null : cursor.getInt(i));
		i++;
		stock.setProductId(cursor.getInt(i++));
		stock.setUnitId(cursor.getInt(i++));
		stock.setProductName(cursor.getString(i++));
		String stockNum = cursor.getString(i++);
		stock.setStockNum(TextUtils.isEmpty(stockNum) ? 0 : Double
				.parseDouble(stockNum));
		String stockoutNum = cursor.getString(i++);
		stock.setStockoutNum(TextUtils.isEmpty(stockoutNum) ? 0 : Double
				.parseDouble(stockoutNum));
		String replenishmentNum = cursor.getString(i++);
		stock.setReplenishmentNum(TextUtils.isEmpty(replenishmentNum) ? 0
				: Double.parseDouble(replenishmentNum));
		stock.setUnit(cursor.getString(i++));
		return stock;
	}

	private ContentValues putContentValues(CarSalesStock stock) {
		ContentValues cv = new ContentValues();
		cv.put("productId", stock.getProductId());
		cv.put("unitId", stock.getUnitId());
		cv.put("productName", stock.getProductName());
		cv.put("stockNum", stock.getStockNum());
		cv.put("stockoutNum", stock.getStockoutNum());
		cv.put("replenishmentNum", stock.getReplenishmentNum());
		cv.put("unit", stock.getUnit());
		return cv;
	}

}
