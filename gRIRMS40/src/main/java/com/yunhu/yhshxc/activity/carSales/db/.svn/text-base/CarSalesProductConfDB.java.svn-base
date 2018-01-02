package com.yunhu.yhshxc.activity.carSales.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.activity.carSales.bo.CarSalesProductConf;
import com.yunhu.yhshxc.database.DatabaseHelper;

public class CarSalesProductConfDB {

	private DatabaseHelper openHelper;

	public CarSalesProductConfDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(CarSalesProductConf productConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.CAR_SALES_PRODUCT_CONF, null,putContentValues(productConf));

		return id;

	}

	
	public CarSalesProductConf findLastProductConf() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CONF).append(" where next is null ");
		sql.append(" and type = ").append(CarSalesProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public CarSalesProductConf findProductConfByTableName(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CONF).append(" where dict_table = '").append(name).append("'");
		sql.append(" and type = ").append(CarSalesProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public CarSalesProductConf findProductConfByNext(String next) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CONF).append(" where dict_table  =  '").append(next).append("'");
		sql.append(" and type = ").append(CarSalesProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public CarSalesProductConf findUnitProductConf() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CONF).append(" where type =  ").append(CarSalesProductConf.TYPE_UNIT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		CarSalesProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}


	
	public List<CarSalesProductConf> findListByType(int type) {
		List<CarSalesProductConf> list = new ArrayList<CarSalesProductConf>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.CAR_SALES_PRODUCT_CONF).append(" where type =  ").append(CarSalesProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				CarSalesProductConf conf = putProductConf(cursor);
				list.add(conf);
			}
			cursor.close();
		}
		return list;
	}

	public void delete() {
		openHelper.delete(openHelper.CAR_SALES_PRODUCT_CONF, null, null);
	}

	private CarSalesProductConf putProductConf(Cursor cursor) {
		int i = 0;
		CarSalesProductConf product = new CarSalesProductConf();
		product.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		product.setDictTable(cursor.getString(i++));
		product.setDictDataId(cursor.getString(i++));
		product.setDictCols(cursor.getString(i++));
		product.setNext(cursor.getString(i++));
		product.setName(cursor.getString(i++));
		product.setType(cursor.getInt(i++));
		return product;
	}

	private ContentValues putContentValues(CarSalesProductConf productConf) {
		ContentValues cv = new ContentValues();
		cv.put("dict_table", productConf.getDictTable());
		cv.put("dict_data_id", productConf.getDictDataId());
		cv.put("dictCols", productConf.getDictCols());
		cv.put("next", productConf.getNext());
		cv.put("name", productConf.getName());
		cv.put("type", productConf.getType());
		return cv;
	}

}
