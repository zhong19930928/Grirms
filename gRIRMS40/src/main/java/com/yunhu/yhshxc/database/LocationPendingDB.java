package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.location.bo.LocationPending;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 数据库操作
 */
public class LocationPendingDB {
	private DatabaseHelper openHelper;

	public LocationPendingDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/*
	 * 查询所有定位信息
	 */
	public List<LocationPending> findAllLocation() {
		List<LocationPending> list = new ArrayList<LocationPending>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.LOCATION_PENDING).append(" order by id");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putLocation(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/*
	 * 查询所有定位信息
	 */
	public List<LocationPending> findLocationByType(int type) {
		List<LocationPending> list = new ArrayList<LocationPending>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.LOCATION_PENDING).append(" where type = ").append(type).append(" order by id");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putLocation(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/*
	 * 临时方法（仅查一条"limit 1"），查询守店定位信息
	 */
	public List<LocationPending> findLocationByTypeAttendance() {
		List<LocationPending> list = new ArrayList<LocationPending>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.LOCATION_PENDING).append(" where type = ").append(TablePending.TYPE_ATTENDANCE_LOATION).append(" order by id limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putLocation(cursor));
			}
		}
		cursor.close();
		return list;
	}
	
	/*
	 * 插入Location
	 */
	public void insertLocation(LocationPending location) {
		ContentValues cv = putContentValues(location);
		openHelper.insert(openHelper.LOCATION_PENDING, cv);
	}

	public void deleteLocationById(String ids){
		if(PublicUtils.isIntegerArray(ids)){
			StringBuffer sql = new StringBuffer();
			sql.append(" delete from ").append(openHelper.LOCATION_PENDING).append(" where id in (").append(ids).append(")");
			openHelper.execSQL(sql.toString());	
		}
	}
	
	public void deleteAllLocation(){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ").append(openHelper.LOCATION_PENDING);
		openHelper.execSQL(sql.toString());
	}
	
	private LocationPending putLocation(Cursor cursor) {
		int i = 0;
		LocationPending location = new LocationPending();
		location.setId(cursor.getInt(i++));
		location.setValue(cursor.getString(i++));
		location.setType(cursor.getInt(i++));
		return location;
	}

	private ContentValues putContentValues(LocationPending location) {
		ContentValues cv = new ContentValues();
		cv.put("value", location.getValue());
		cv.put("type", location.getType());
		return cv;
	}

}
