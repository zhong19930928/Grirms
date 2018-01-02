package com.yunhu.yhshxc.doubletask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yunhu.yhshxc.database.DatabaseHelper;

public class DoubleTaskManagerDB {
	private DatabaseHelper openHelper;
	
	public DoubleTaskManagerDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}
	
	
	public void insert(DoubleTaskManager DoubleTaskManager){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues cv = putContentValues(DoubleTaskManager);
		db.insert(openHelper.DOUBLE_TASK_MANAGER, null, cv);
	}
	
	public boolean  isHasDoubleTaskManager(int menuId,int dataId){
		boolean flag = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.DOUBLE_TASK_MANAGER).append(" where menuId=");
		sql.append(menuId).append(" and dataId=").append(dataId);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor!=null && cursor.getCount()>0) {
			if (cursor.moveToNext()) {
				DoubleTaskManager m = putDoubleTaskManager(cursor);
				if (m!=null && m.getDataId() == dataId && m.getMenuId() == menuId) {
					flag = true;
				}
			}
		}
		cursor.close();
		return flag;
	}
	
	public void controlDoubleTaskManagerNum(){
		int count = findCounts();
		if (count>=1000) {
			delDoubleTaskManagerList();
		}
	}
	
	
	
	public int  findCounts() {
		int count = 0;
		StringBuffer sql = new StringBuffer(" select count(*) from ").append(openHelper.DOUBLE_TASK_MANAGER);
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		cursor.close();
		return count;
	}
	
	
	public void delDoubleTaskManagerList(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(openHelper.DOUBLE_TASK_MANAGER).append(" order by id limit 1");
		Cursor cursor = openHelper.query(sql.toString(), null);
		DoubleTaskManager manager = null;
		if (cursor!=null && cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				manager = putDoubleTaskManager(cursor);
			}
			cursor.close();
		}
		if (manager!=null) {
			int id = manager.getId();
			int deId = id+100;
			deleteById(deId);
		}
		
	}
	
	
	public void deleteById(int id){
		String sql = "delete from "+openHelper.DOUBLE_TASK_MANAGER+" wnere id<"+id;
		openHelper.execSQL(sql);
	}
	
	
	private ContentValues putContentValues(DoubleTaskManager data) {
		ContentValues cv = new ContentValues();
		cv.put("menuId", data.getMenuId());
		cv.put("dataId", data.getDataId());
		return cv;
	}
	
	private DoubleTaskManager putDoubleTaskManager(Cursor cursor) {
		int i = 0;
		DoubleTaskManager module = new DoubleTaskManager();
		module.setId(cursor.isNull(i) ? null : cursor.getInt(i));i++;
		module.setMenuId(cursor.getInt(i++));
		module.setDataId(cursor.getInt(i++));
		return module;
	}
	
}
