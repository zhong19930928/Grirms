package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yunhu.yhshxc.order.bo.ProductConf;

/**
 * 
 * @author David.hou 数据库操作
 */
public class PSSProductConfDB {

	private String TAG = "PSSProductDB";
	private DatabaseHelper openHelper;

	public PSSProductConfDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(ProductConf productConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.PSS_PRODUCT_CONF_TABLE, null, putContentValues(productConf));
		Log.d(TAG + "===保存任务==>", "id = " + id);
		
		return id;

	}
	
	public List<ProductConf> findList(){
		List<ProductConf> list = new ArrayList<ProductConf>();
		Cursor cursor = openHelper.query(openHelper.PSS_PRODUCT_CONF_TABLE, null, null, null, null, null, null);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				list.add(putProductConf(cursor));
			}
			cursor.close();
		}
		return list;
	}
	
	public void delete(){
		openHelper.delete(openHelper.PSS_PRODUCT_CONF_TABLE, null, null);
	}
	
	private ProductConf putProductConf(Cursor cursor) {
		int i = 0;
		ProductConf product = new ProductConf();
		product.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		product.setDictTable(cursor.getString(i++));
		product.setDictDataId(cursor.getString(i++));
		product.setDictCols(cursor.getString(i++));
		product.setNext(cursor.getString(i++));
		product.setName(cursor.getString(i++));
		product.setDictOrderBy(cursor.getString(i++));
		product.setDictIsAsc(cursor.getString(i++));
		return product;
	}
	
	private ContentValues putContentValues(ProductConf productConf){
		ContentValues cv = new ContentValues();
		cv.put("dict_table", productConf.getDictTable());
		cv.put("dict_data_id", productConf.getDictDataId());
		cv.put("dictCols", productConf.getDictCols());
		cv.put("next", productConf.getNext());
		cv.put("name", productConf.getName());
		cv.put("dict_order_by", productConf.getDictOrderBy());
		cv.put("dict_is_asc", productConf.getDictIsAsc());
		return cv;
	}
}
