package com.yunhu.yhshxc.order3.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;
import com.yunhu.yhshxc.order3.bo.Order3ProductConf;

public class Order3ProductConfDB {

	private DatabaseHelper openHelper;

	public Order3ProductConfDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(Order3ProductConf productConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.ORDER3_PRODUCT_CONF, null,putContentValues(productConf));

		return id;

	}

	
	public Order3ProductConf findLastProductConf() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CONF).append(" where next is null ");
		sql.append(" and type = ").append(Order3ProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public Order3ProductConf findProductConfByTableName(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CONF).append(" where dict_table = '").append(name).append("'");
		sql.append(" and type = ").append(Order3ProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public Order3ProductConf findProductConfByNext(String next) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CONF).append(" where dict_table  =  '").append(next).append("'");
		sql.append(" and type = ").append(Order3ProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}
	
	public Order3ProductConf findUnitProductConf() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CONF).append(" where type =  ").append(Order3ProductConf.TYPE_UNIT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		Order3ProductConf conf = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				conf = putProductConf(cursor);
			}
			cursor.close();
		}
		return conf;
	}


	
	public List<Order3ProductConf> findListByType(int type) {
		List<Order3ProductConf> list = new ArrayList<Order3ProductConf>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.ORDER3_PRODUCT_CONF).append(" where type =  ").append(Order3ProductConf.TYPE_PRODUCT);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Order3ProductConf conf = putProductConf(cursor);
				list.add(conf);
			}
			cursor.close();
		}
		return list;
	}

	public void delete() {
		openHelper.delete(openHelper.ORDER3_PRODUCT_CONF, null, null);
	}

	private Order3ProductConf putProductConf(Cursor cursor) {
		int i = 0;
		Order3ProductConf product = new Order3ProductConf();
		product.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		product.setDictTable(cursor.getString(i++));
		product.setDictDataId(cursor.getString(i++));
		product.setDictCols(cursor.getString(i++));
		product.setNext(cursor.getString(i++));
		product.setName(cursor.getString(i++));
		product.setType(cursor.getInt(i++));
		return product;
	}

	private ContentValues putContentValues(Order3ProductConf productConf) {
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
