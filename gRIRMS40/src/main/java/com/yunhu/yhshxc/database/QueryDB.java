package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.bo.Query;

public class QueryDB {
	private DatabaseHelper openHelper;

	public QueryDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	public void insertQuery(Query query){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(query);
		db.insert(openHelper.QUERY, null, cv);
	}
	
	public List<Query> findQueryByMid(int mid){
		List<Query> queryList = new ArrayList<Query>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from "+openHelper.QUERY).append(" where 1=1 ");
		sql.append(" and mid =  ").append(mid);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				queryList.add(putQuery(cursor));
			}
		}
		cursor.close();
		return queryList;
	}
	
	
	public void removeQueryByMid(int mid){
		StringBuffer sql=new StringBuffer();
		sql.append("delete  from ").append(openHelper.QUERY).append(" where mid = ").append(mid);
		openHelper.execSQL(sql.toString());
	}
	
	public Query findQueryByMidAndFuncId(int mid,String funcid){
		Query query = null;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from "+openHelper.QUERY).append(" where 1=1 ");
		sql.append(" and mid =  ").append(mid);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				query = putQuery(cursor);
			}
		}
		cursor.close();
		return query;
	}
	
	private ContentValues putContentValues(Query query){
		ContentValues cv = new ContentValues();
		cv.put("mid", query.getMid());
		cv.put("funcid", query.getFuncId());
		cv.put("value", query.getValue());
		return cv;
	}
	
	private Query putQuery(Cursor cursor) {
		int i = 0;
		Query query = new Query();
		query.setId(cursor.getInt(i++));
		query.setMid(cursor.getInt(i++));
		query.setFuncId(cursor.getString(i++));
		query.setValue(cursor.getString(i++));
		return query;
	}

}
