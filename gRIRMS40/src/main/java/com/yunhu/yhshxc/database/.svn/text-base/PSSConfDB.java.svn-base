package com.yunhu.yhshxc.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.order.bo.PSSConf;

public class PSSConfDB {

	private String TAG = "PSSConfDB";
	private DatabaseHelper openHelper;
	private Context context;
	
	public PSSConfDB(Context context) {
		this.context = context;
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(PSSConf pssConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.PSS_CONF_TABLE, null, putContentValues(pssConf));
		Log.d(TAG + "===保存任务==>", "id = " + id);
		return id;

	}
	
	/**
	 * 获取退货字典表数据
	 * 
	 */
	public List<Dictionary> findReturnedReasonList() {
		
		PSSConf pssconf = findPSSConf();
		if(pssconf != null && pssconf.getReturnedReasonDictTable() != null){
			List<Dictionary> list = new DictDB(context).findDictList(pssconf.getReturnedReasonDictTable(), pssconf.getReturnedReasonDictDataId(),pssconf.getDictOrderBy(),pssconf.getDictIsAsc());
			return list;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 获取盘货差异原因字典表数据
	 * 
	 */
	public List<Dictionary> findStocktakeDifferList() {
		
		PSSConf pssconf = findPSSConf();
		if(pssconf != null && pssconf.getReturnedReasonDictTable() != null){
			List<Dictionary> list = new DictDB(context).findDictList(pssconf.getStocktakeDifferDictTable(), pssconf.getStocktakeDifferDictDataId(),pssconf.getDictOrderBy(),pssconf.getDictIsAsc());
			return list;
		}else{
			return null;
		}
		
	}
	
	public PSSConf findPSSConf(){
		PSSConf pssconf = null;
		Cursor cursor = openHelper.query(openHelper.PSS_CONF_TABLE, null, null, null, null, null, null);
		if(cursor!=null){
			if (cursor.moveToNext()) {
				pssconf = putProductConf(cursor);
			}
			cursor.close();
		}
		return pssconf;
	}
	
	public void delete(){
		openHelper.delete(openHelper.PSS_CONF_TABLE, null, null);
	}
	
	private PSSConf putProductConf(Cursor cursor) {
		int i = 0;
		PSSConf pssConf = new PSSConf();
		pssConf.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		pssConf.setPhoneFun(cursor.getString(i++));
		pssConf.setCreateOrderStartTime(cursor.getString(i++));
		pssConf.setCreateOrderEndTime(cursor.getString(i++));
		pssConf.setSalesStartTime(cursor.getString(i++));
		pssConf.setSalesEndTime(cursor.getString(i++));
		pssConf.setReturnedStartTime(cursor.getString(i++));
		pssConf.setReturnedEndTime(cursor.getString(i++));
		pssConf.setReturnedReasonDictTable(cursor.getString(i++));
		pssConf.setReturnedReasonDictDataId(cursor.getString(i++));
		pssConf.setStocktakeDifferDictTable(cursor.getString(i++));
		pssConf.setStocktakeDifferDictDataId(cursor.getString(i++));
		pssConf.setCreateOrderTimeConf(cursor.getString(i++));
		pssConf.setCreateOrderTimeWeekly(cursor.getString(i++));
		pssConf.setSalesTimeConf(cursor.getString(i++));
		pssConf.setSalesTimeWeekly(cursor.getString(i++));
		pssConf.setReturnedTimeConf(cursor.getString(i++));
		pssConf.setReturnedTimeWeekly(cursor.getString(i++));
		pssConf.setIsPriceEdit(cursor.getString(i++));
		pssConf.setOrderPrintStyle(cursor.getString(i++));
		pssConf.setStockPrintStyle(cursor.getString(i++));
		pssConf.setDictOrderBy(cursor.getString(i++));
		pssConf.setDictIsAsc(cursor.getString(i++));
		return pssConf;
	}
	
	private ContentValues putContentValues(PSSConf pssConf){
		ContentValues cv = new ContentValues();
		cv.put("phone_fun", pssConf.getPhoneFun());
		cv.put("create_order_start_time", pssConf.getCreateOrderStartTime());
		cv.put("create_order_end_time", pssConf.getCreateOrderEndTime());
		cv.put("sales_start_time", pssConf.getSalesStartTime());
		cv.put("sales_end_time", pssConf.getSalesEndTime());
		cv.put("returned_start_time", pssConf.getReturnedStartTime());
		cv.put("returned_end_time", pssConf.getReturnedEndTime());
		cv.put("returned_reason_dict_table", pssConf.getReturnedReasonDictTable());
		cv.put("returned_reason_dict_data_id", pssConf.getReturnedReasonDictDataId());
		cv.put("stocktake_differ_dict_table", pssConf.getStocktakeDifferDictTable());
		cv.put("stocktake_differ_dict_data_id", pssConf.getStocktakeDifferDictDataId());
		cv.put("create_order_time_conf", pssConf.getCreateOrderTimeConf());
		cv.put("create_order_time_weekly", pssConf.getCreateOrderTimeWeekly());
		cv.put("sales_time_conf", pssConf.getSalesTimeConf());
		cv.put("sales_time_weekly", pssConf.getSalesTimeWeekly());
		cv.put("returned_time_conf", pssConf.getReturnedTimeConf());
		cv.put("returned_time_weekly", pssConf.getReturnedTimeWeekly());
		cv.put("is_price_edit", pssConf.getIsPriceEdit());
		cv.put("order_print_style", pssConf.getOrderPrintStyle());
		cv.put("stock_print_style", pssConf.getStockPrintStyle());
		cv.put("dict_order_by", pssConf.getDictOrderBy());
		cv.put("dict_is_asc", pssConf.getDictIsAsc());
		return cv;
	}
}
