package com.yunhu.yhshxc.attendance.leave;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;

public class LeaveInfoDB {

	private DatabaseHelper openHelper;

	public LeaveInfoDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	public Long insert(LeaveInfo productConf) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Long id = db.insert(openHelper.IS_LEAVE, null,putContentValues(productConf));

		return id;

	}

	public List<LeaveInfo> findAllLeaves(){
		List<LeaveInfo> list = new ArrayList<LeaveInfo>();
		StringBuffer findSql=new StringBuffer();
		findSql.append("select * from ").append(openHelper.IS_LEAVE).append(" where 1=1");
		Cursor cursor = openHelper.query(findSql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putProductConf(cursor));
			}
		}
		cursor.close();
		return list;
	}
	

	

	public void delete() {
		openHelper.delete(openHelper.IS_LEAVE, null, null);
	}

	private LeaveInfo putProductConf(Cursor cursor) {
		int i = 0;
		LeaveInfo product = new LeaveInfo();
		product.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		product.setType(cursor.getString(i++));
		product.setName(cursor.getString(i++));
		product.setMaxDays(cursor.getString(i++));
		return product;
	}

	private ContentValues putContentValues(LeaveInfo productConf) {
		ContentValues cv = new ContentValues();
		cv.put("type", productConf.getType());
		cv.put("name", productConf.getName());
		cv.put("maxDays", productConf.getMaxDays());
		
		return cv;
	}

}
