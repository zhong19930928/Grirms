package com.yunhu.yhshxc.database;

import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.FuncCache;
import com.yunhu.yhshxc.bo.SubmitItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 缓存控件
 *
 */
public class FuncCacheDB {

	private DatabaseHelper openHelper;

	public FuncCacheDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/**
	 * 插入一条或者更新
	 */
	public void insertFuncCache(FuncCache funcCache) {
		
		SQLiteDatabase db = openHelper.getWritableDatabase();
		// 先根据funcId查找,不存在则插入,存在则更新
		Cursor cursor = db.rawQuery("select * from FUNC_CACHE where funcId = ? and modleId = ?",
				new String[] { funcCache.getFuncId(), funcCache.getModleId() });
		if (cursor.moveToNext()) {// 说明存在数据,则进行更新
			
			db.update("FUNC_CACHE", putContentValues(funcCache), " funcId = ? and modleId = ? ",
					new String[] { funcCache.getFuncId(), funcCache.getModleId() });
//			db.execSQL("update FUNC_CACHE set paramValue = ? , paramId = ? where funcId = ? and modleId = ? ",new Object[]{funcCache.getParamValue(),funcCache.getParamId(),funcCache.getFuncId(),funcCache.getModleId()});
		} else {// 插入新数据
			ContentValues cv = putContentValues(funcCache);
			db.insert("FUNC_CACHE", null, cv);
		}

		cursor.close();
//		db.close();
	}

	public void insertSubmitToCache(SubmitItem submitItem) {
		try {
			FuncCache funcCache = new FuncCache();
			funcCache.setFuncId(submitItem.getParamName());
			int funcType = submitItem.getType();
//		if (funcType == Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP || funcType == Func.TYPE_MULTI_CHOICE_SPINNER_COMP
//				|| funcType == Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP
//				|| funcType == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) {

				funcCache.setParamId(submitItem.getParamValue());
				funcCache.setParamValue(submitItem.getParamStr());
				if (funcType==Func.TYPE_SELECT_OTHER) {
//				funcCache.setParamValue(submitItem.getParamValue());
					String[] strs = submitItem.getParamName().split("_");
					funcCache.setFuncId(strs[0]);
//				funcCache.setParamId("-1");
				}else if (funcType == Func.TYPE_SQL_BIG_DATA) {
					funcCache.setParamValue(submitItem.getNote());				
				}
//		} else {
//			funcCache.setParamValue(submitItem.getParamValue());
//		}
			funcCache.setModleId(submitItem.getTargetId());
			if (!TextUtils.isEmpty(funcCache.getModleId())) {		
				insertFuncCache(funcCache);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public void updateValueToCache(Func func, String value) {
	
		//先查出来已存在的FuncCache,然后赋值ParamValue,在更新		
		FuncCache funcCache = findFunCacheByFuncId(func.getFuncId()+"",func.getTargetid()+"");
		funcCache.setParamValue(value);
		SQLiteDatabase db = openHelper.getWritableDatabase();
//		db.update("FUNC_CACHE", putContentValues(funcCache), " funcId = ? and modleId = ? ",
//				new String[] { funcCache.getFuncId(), funcCache.getModleId() });
		db.execSQL("update FUNC_CACHE set paramValue = ? , paramId = ? where funcId = ? and modleId = ? ",new Object[]{funcCache.getParamValue(),funcCache.getParamId(),funcCache.getFuncId(),funcCache.getModleId()});
		
//		db.close();
	}
  
	/**
	 * 查询一条数据FuncCache
	 * 
	 * @param funcCache
	 * @return
	 */
	public FuncCache findFunCacheByFuncId(String funcId, String modleId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		FuncCache funcCache =null;
		try{
			Cursor cursor = db.rawQuery("select * from FUNC_CACHE where funcId = ? and modleId = ?",
					new String[] { funcId, modleId });
			while (cursor.moveToNext()) {
				funcCache = new FuncCache();
				funcCache.setFuncId(cursor.getString(cursor.getColumnIndex("funcId")));
				funcCache.setModleId(cursor.getString(cursor.getColumnIndex("modleId")));
				funcCache.setParamId(cursor.getString(cursor.getColumnIndex("paramId")));
				funcCache.setParamValue(cursor.getString(cursor.getColumnIndex("paramValue")));
			}	
			cursor.close();
//			db.close();
		}catch(Exception e){
			
		}


		

		return funcCache;

	}

	private ContentValues putContentValues(FuncCache funcCache) {
		ContentValues cv = new ContentValues();
		 String modleId =   funcCache.getModleId(); 
		 String funcid = funcCache.getFuncId();
		 String paramValue = funcCache.getParamValue();
		 String paramid = funcCache.getParamId();
		if (!TextUtils.isEmpty(modleId)) {			
			cv.put("modleId", modleId);
		}
		if (!TextUtils.isEmpty(funcid)) {			
			cv.put("funcId", funcid);
		}
		if (!TextUtils.isEmpty(paramValue)) {			
			cv.put("paramValue", paramValue);
		}
		if (!TextUtils.isEmpty(paramid)) {			
			cv.put("paramId", paramid);
		}

		return cv;
	}

}
